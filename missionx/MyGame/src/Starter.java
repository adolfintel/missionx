
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import net.java.games.input.Controller;
import org.lwjgl.opengl.GL11;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Starter.java
 *
 * Created on 9-ott-2009, 18.57.55
 */
/**
 *
 * @author Do$$e
 */
public class Starter extends javax.swing.JFrame {

    private float xSize = 440, ySize = 520;
    private int DEVSTATUS = 4;
    private int VER = 2;
    private class UpdateThread extends Thread {

        boolean showErrors = false;

        public UpdateThread(boolean showErrors) {
            this.showErrors = showErrors;
        }

        @Override
        public void run() {
            try {
                BufferedInputStream in = null;
                if (cUseProxy.isSelected()) {
                    HttpURLConnection uc = (HttpURLConnection) new URL("http://missionx.sourceforge.net/update/lastVersion.dat").openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp.getText(), Integer.parseInt(proxyPort.getText()))));
                    uc.connect();
                    in = new BufferedInputStream(uc.getInputStream());
                } else {
                    in = new BufferedInputStream(new URL("http://missionx.sourceforge.net/update/lastVersion.dat").openStream());
                }
                //java.io.BufferedInputStream in = new java.io.BufferedInputStream(new java.net.URL("http://missionx.sourceforge.net/update/lastVersion.dat").openStream());
                byte version[] = new byte[2]; //first byte: dev status; second byte:version
                in.read(version);
                in.read(); //skip 1 byte
                byte address[] = new byte[256];
                in.read(address);
                String link = new String(address).trim();
                in.close();
                String[] args = new String[2];
                if (version[0] == 0) {
                    args[0] = "Prealpha " + (int) version[1];
                }
                if (version[0] == 1) {
                    args[0] = "Alpha " + (int) version[1];
                }
                if (version[0] == 2) {
                    args[0] = "Beta " + (int) version[1];
                }
                if (version[0] == 3) {
                    args[0] = "Release Candidate " + (int) version[1];
                }
                if (version[0] == 4) {
                    args[0] = "Stable v1." + (int) version[1];
                }
                args[1] = link;
                System.out.println("Latest version (online):" + args[0] + "\nDL Link: " + args[1]);
                if (version[0] > DEVSTATUS || (version[0] == DEVSTATUS && version[1] > VER)) {
                    Updater.main(args);
                } else {
                    if (cmbLang.getSelectedIndex() == 0) {
                        if (showErrors) {
                            JOptionPane.showMessageDialog(new JOptionPane(), "No updates found");
                        }
                    } else {
                        if (showErrors) {
                            JOptionPane.showMessageDialog(new JOptionPane(), "Nessun aggiornamento trovato");
                        }
                    }
                }
            } catch (Exception e) {
                if (cmbLang.getSelectedIndex() == 0) {
                    if (showErrors) {
                        JOptionPane.showMessageDialog(new JOptionPane(), "Couldn't check for updates", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (showErrors) {
                        JOptionPane.showMessageDialog(new JOptionPane(), "Non è stato possibile controllare gli aggiornamenti", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            stop();
        }
    }
    Vector<DisplayMode> m = new Vector<DisplayMode>();

    /** Creates new form Starter */
    public Starter() {
        if (System.getProperty("os.name").startsWith("Linux") || System.getProperty("os.name").toLowerCase().contains("mac")) {
            setUndecorated(false);
            //JOptionPane.showMessageDialog(this, "Warning: linux support is still EXPERIMENTAL and might be buggy. If you find a bug , please report it to dosse91@live.it.\nYour support is appreciated.");
        } else {
            setUndecorated(true);
        }
        initComponents();

        if (System.getProperty("os.name").startsWith("Linux") || System.getProperty("os.name").toLowerCase().contains("mac")) {
            setSize((int) xSize + 8, (int) ySize);
        } else {
            setSize((int) xSize, (int) ySize);
        }
        setLocation(50, 50);
        if (System.getProperty("os.name").startsWith("Linux") || System.getProperty("os.name").toLowerCase().contains("mac")) {
            titleBar.setVisible(false);
            bMinimize.setVisible(false);
            bClose.setVisible(false);
            configAppPanel.setLocation(0, 0);
        }
        org.easyway.system.Core.setLibrary();
        Toolkit t = Toolkit.getDefaultToolkit();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice d = ge.getDefaultScreenDevice();
        DisplayMode[] list = d.getDisplayModes();
        for (DisplayMode dm : list) {
            m.add(dm);
        }
        for (int i = 0; i < m.size(); i++) {
            if (((float) m.get(i).getWidth() / (float) m.get(i).getHeight()) != 4f / 3f || m.get(i).getWidth() < 640) {
                m.remove(i);
                i--;
            }

        }
        for (int i = 0; i < m.size(); i++) {
            for (int j = 0; j < m.size(); j++) {
                if (j != i) {
                    if (m.get(j).getWidth() == m.get(i).getWidth() && m.get(j).getHeight() == m.get(i).getHeight()) {
                        m.remove(j);
                        j--;
                    }
                }
            }
        }
        for (DisplayMode dm : m) {
            cmbRes.addItem(dm.getWidth() + "x" + dm.getHeight());
        }
        loadSettings();
        if(m.size()==0){JOptionPane.showMessageDialog(this,cmbLang.getSelectedIndex()==0?"Sorry, but your screen is not supported.\nTry updating your video drivers.":"Spiacente, ma il tuo schermo non è supportato.\nProva ad aggiornare i driver video.","MissionX",JOptionPane.ERROR_MESSAGE);System.exit(0);}
        //if (System.getProperty("os.name").startsWith("Linux")){ cmbCont.removeAllItems(); cmbCont.addItem("Mouse and keyboard"); cmbCont.setSelectedIndex(0); bCtrlSettings.setVisible(false); jLabel4.setVisible(false);}
        cmbLangActionPerformed(null);
        cUseProxyActionPerformed(null);
        new UpdateThread(false).start();
        //if(cmbSFX.getSelectedIndex()==0&&System.getProperty("os.name").contains("Linux")) cmbSFX.setSelectedIndex(1);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        configAppPanel = new javax.swing.JPanel();
        logo = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        configPanels = new javax.swing.JTabbedPane();
        settings1 = new javax.swing.JPanel();
        cmbSFX = new javax.swing.JComboBox();
        lblSFX = new javax.swing.JLabel();
        cmbMusic = new javax.swing.JComboBox();
        lblRes = new javax.swing.JLabel();
        cmbLang = new javax.swing.JComboBox();
        lblLang = new javax.swing.JLabel();
        lblGfxQ = new javax.swing.JLabel();
        lblMusic = new javax.swing.JLabel();
        lblCont = new javax.swing.JLabel();
        cmbCont = new javax.swing.JComboBox();
        bCtrlSettings = new javax.swing.JButton();
        cmbGfxQ = new javax.swing.JComboBox();
        bFS = new javax.swing.JCheckBox();
        cmbRes = new javax.swing.JComboBox();
        lblContInfo = new javax.swing.JLabel();
        lblGfxQInfo = new javax.swing.JLabel();
        settings2 = new javax.swing.JPanel();
        lblFRS = new javax.swing.JLabel();
        mSpeed = new javax.swing.JSlider();
        lblSens = new javax.swing.JLabel();
        VSynch = new javax.swing.JCheckBox();
        frSmoothing = new javax.swing.JSlider();
        cUseProxy = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        proxyPort = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        proxyIp = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        mods = new javax.swing.JPanel();
        cmbMission = new javax.swing.JComboBox();
        lblMission = new javax.swing.JLabel();
        txtMission = new javax.swing.JTextField();
        cmbSkin = new javax.swing.JComboBox();
        lblSkin = new javax.swing.JLabel();
        txtSkin = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        actionPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        bPlay = new javax.swing.JButton();
        bDef = new javax.swing.JButton();
        bResetHS = new javax.swing.JButton();
        bQuit = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        bMinimize = new javax.swing.JButton();
        bClose = new javax.swing.JButton();
        titleBar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MissionX");
        setName("MissionX Config App"); // NOI18N
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        configAppPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        configAppPanel.setLayout(null);

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loaderLogo.png"))); // NOI18N

        javax.swing.GroupLayout logoLayout = new javax.swing.GroupLayout(logo);
        logo.setLayout(logoLayout);
        logoLayout.setHorizontalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logoLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(lblLogo)
                .addContainerGap())
        );
        logoLayout.setVerticalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, Short.MAX_VALUE)
        );

        configAppPanel.add(logo);
        logo.setBounds(10, 10, 420, 90);

        settings1.setLayout(null);

        cmbSFX.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enabled", "Disabled" }));
        settings1.add(cmbSFX);
        cmbSFX.setBounds(105, 8, 150, 25);

        lblSFX.setText("Sound Effects");
        settings1.add(lblSFX);
        lblSFX.setBounds(5, 10, 100, 20);

        cmbMusic.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enabled", "Disabled" }));
        settings1.add(cmbMusic);
        cmbMusic.setBounds(105, 40, 150, 25);

        lblRes.setText("Resolution");
        settings1.add(lblRes);
        lblRes.setBounds(5, 70, 100, 20);

        cmbLang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "English", "Italiano" }));
        cmbLang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLangActionPerformed(evt);
            }
        });
        settings1.add(cmbLang);
        cmbLang.setBounds(105, 100, 150, 25);

        lblLang.setText("Language");
        settings1.add(lblLang);
        lblLang.setBounds(5, 100, 100, 20);

        lblGfxQ.setText("Graphics quality");
        settings1.add(lblGfxQ);
        lblGfxQ.setBounds(5, 190, 100, 20);

        lblMusic.setText("Music");
        settings1.add(lblMusic);
        lblMusic.setBounds(5, 40, 100, 20);

        lblCont.setText("Controller");
        settings1.add(lblCont);
        lblCont.setBounds(5, 130, 80, 20);

        cmbCont.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mouse", "Gamepad/Joystick" }));
        cmbCont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbContActionPerformed(evt);
            }
        });
        settings1.add(cmbCont);
        cmbCont.setBounds(105, 130, 150, 25);

        bCtrlSettings.setText("Settings");
        bCtrlSettings.setEnabled(false);
        bCtrlSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCtrlSettingsActionPerformed(evt);
            }
        });
        settings1.add(bCtrlSettings);
        bCtrlSettings.setBounds(260, 130, 90, 25);

        cmbGfxQ.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Very low", "Low", "Medium", "High", "Auto (recommended)" }));
        cmbGfxQ.setSelectedIndex(4);
        cmbGfxQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGfxQActionPerformed(evt);
            }
        });
        settings1.add(cmbGfxQ);
        cmbGfxQ.setBounds(105, 190, 150, 25);

        bFS.setText("Fullscreen");
        settings1.add(bFS);
        bFS.setBounds(270, 70, 140, 23);
        settings1.add(cmbRes);
        cmbRes.setBounds(105, 70, 150, 25);

        lblContInfo.setForeground(new java.awt.Color(204, 204, 204));
        lblContInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContInfo.setText("You can switch between mouse and gamepad at any time by pressing Enter");
        settings1.add(lblContInfo);
        lblContInfo.setBounds(0, 160, 430, 20);

        lblGfxQInfo.setForeground(new java.awt.Color(204, 204, 204));
        lblGfxQInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGfxQInfo.setText("Tip: On netbooks and old computers, choose low or medium settings");
        lblGfxQInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        settings1.add(lblGfxQInfo);
        lblGfxQInfo.setBounds(0, 220, 430, 20);

        configPanels.addTab("Settings", settings1);

        settings2.setLayout(null);

        lblFRS.setText("Framerate Smoothing");
        settings2.add(lblFRS);
        lblFRS.setBounds(5, 40, 140, 30);

        mSpeed.setMaximum(150);
        mSpeed.setMinimum(20);
        mSpeed.setValue(90);
        settings2.add(mSpeed);
        mSpeed.setBounds(140, 70, 160, 30);

        lblSens.setText("Control sensivity");
        settings2.add(lblSens);
        lblSens.setBounds(5, 70, 140, 30);

        VSynch.setSelected(true);
        VSynch.setText("VSynch");
        VSynch.setToolTipText("This enables or disables the Veritcal Synch. If you disable this, you'll get better performance but the game might be unstable and show glitches");
        settings2.add(VSynch);
        VSynch.setBounds(5, 0, 90, 40);

        frSmoothing.setMaximum(60);
        frSmoothing.setMinimum(2);
        frSmoothing.setToolTipText("Framerate smoothing prevents the game from slowing down or going too fast when the performance are too low or too high. Changing this value is NOT recommended.");
        frSmoothing.setValue(10);
        settings2.add(frSmoothing);
        frSmoothing.setBounds(140, 40, 160, 30);

        cUseProxy.setText("Connect through HTTP proxy");
        cUseProxy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cUseProxyActionPerformed(evt);
            }
        });
        settings2.add(cUseProxy);
        cUseProxy.setBounds(20, 130, 260, 30);

        jLabel1.setText("Proxy address");
        settings2.add(jLabel1);
        jLabel1.setBounds(20, 160, 100, 25);
        settings2.add(proxyPort);
        proxyPort.setBounds(230, 160, 50, 25);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(":");
        settings2.add(jLabel4);
        jLabel4.setBounds(210, 160, 20, 25);
        settings2.add(proxyIp);
        proxyIp.setBounds(110, 160, 100, 25);

        jButton1.setText("Check for updates");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        settings2.add(jButton1);
        jButton1.setBounds(20, 190, 260, 25);

        jLabel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Updates"));
        settings2.add(jLabel5);
        jLabel5.setBounds(10, 110, 290, 120);

        configPanels.addTab("Advanced Settings", settings2);

        mods.setLayout(null);

        cmbMission.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Other (write under)" }));
        cmbMission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMissionActionPerformed(evt);
            }
        });
        mods.add(cmbMission);
        cmbMission.setBounds(90, 11, 150, 25);

        lblMission.setText("Mission");
        mods.add(lblMission);
        lblMission.setBounds(10, 13, 100, 20);

        txtMission.setEnabled(false);
        mods.add(txtMission);
        txtMission.setBounds(10, 43, 410, 30);

        cmbSkin.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Other (write under)" }));
        cmbSkin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSkinActionPerformed(evt);
            }
        });
        mods.add(cmbSkin);
        cmbSkin.setBounds(90, 80, 150, 25);

        lblSkin.setText("Skin");
        mods.add(lblSkin);
        lblSkin.setBounds(10, 80, 100, 20);

        txtSkin.setEnabled(false);
        mods.add(txtSkin);
        txtSkin.setBounds(10, 110, 410, 30);

        configPanels.addTab("Custom maps and mods", mods);

        configAppPanel.add(configPanels);
        configPanels.setBounds(2, 100, 436, 270);
        configAppPanel.add(jSeparator2);
        jSeparator2.setBounds(0, 370, 440, 30);

        actionPanel.setLayout(null);

        jLabel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));
        actionPanel.add(jLabel2);
        jLabel2.setBounds(0, 3, 420, 100);

        bPlay.setText("Play");
        bPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPlayActionPerformed(evt);
            }
        });
        actionPanel.add(bPlay);
        bPlay.setBounds(10, 20, 190, 30);

        bDef.setText("Default settings");
        bDef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDefActionPerformed(evt);
            }
        });
        actionPanel.add(bDef);
        bDef.setBounds(10, 60, 190, 30);

        bResetHS.setText("Reset Hi-Scores");
        bResetHS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bResetHSActionPerformed(evt);
            }
        });
        actionPanel.add(bResetHS);
        bResetHS.setBounds(220, 20, 190, 30);

        bQuit.setText("Quit");
        bQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bQuitActionPerformed(evt);
            }
        });
        actionPanel.add(bQuit);
        bQuit.setBounds(220, 60, 190, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel3.setText("© 2010 froggySoft.  This software is licensed under the GPL license agreement.");
        actionPanel.add(jLabel3);
        jLabel3.setBounds(10, 100, 430, 20);

        configAppPanel.add(actionPanel);
        actionPanel.setBounds(10, 370, 420, 120);

        getContentPane().add(configAppPanel);
        configAppPanel.setBounds(0, 20, 440, 500);

        bMinimize.setBackground(new java.awt.Color(204, 0, 0));
        bMinimize.setFont(new java.awt.Font("Lucida Console", 0, 10));
        bMinimize.setText("-");
        bMinimize.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        bMinimize.setFocusPainted(false);
        bMinimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMinimizeActionPerformed(evt);
            }
        });
        getContentPane().add(bMinimize);
        bMinimize.setBounds(362, 0, 35, 19);

        bClose.setBackground(new java.awt.Color(204, 0, 0));
        bClose.setFont(new java.awt.Font("Lucida Console", 0, 10));
        bClose.setText("X");
        bClose.setFocusPainted(false);
        bClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });
        getContentPane().add(bClose);
        bClose.setBounds(402, 0, 35, 19);

        titleBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loaderBar_Focused.png"))); // NOI18N
        titleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                titleBarMouseDragged(evt);
            }
        });
        getContentPane().add(titleBar);
        titleBar.setBounds(0, 0, 440, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private int loadSettings() {
        try {
            Thread.sleep(100);
            File f = new File("missionX.cfg");
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            cmbSFX.setSelectedIndex(((Integer) ois.readObject()).intValue());
            cmbMusic.setSelectedIndex(((Integer) ois.readObject()).intValue());
            cmbLang.setSelectedIndex(((Integer) ois.readObject()).intValue());
            int aux = (((Integer) ois.readObject()).intValue());
            if (aux == 0) {  //very low gfx quality
                cmbGfxQ.setSelectedIndex(0);
            } else if (aux == 1) {  //low gfx quality
                cmbGfxQ.setSelectedIndex(1);
            } else if (aux == 2) {  //mid gfx quality
                cmbGfxQ.setSelectedIndex(2);
            } else if (aux == 3) {  //hi gfx quality
                cmbGfxQ.setSelectedIndex(3);
            } else if (aux == 4) {  //very hi gfx quality
                cmbGfxQ.setSelectedIndex(4);
            }
            mSpeed.setValue(((Integer) ois.readObject()).intValue());
            frSmoothing.setValue(((Integer) ois.readObject()).intValue());
            Boolean b = (Boolean) ois.readObject();
            VSynch.setSelected(b.booleanValue());
            String s = (String) ois.readObject();
            if (s.equals("defSkin")) {
                cmbSkin.setSelectedIndex(0);

            } else {
                cmbSkin.setSelectedIndex(1);
                txtSkin.setEnabled(true);
                txtSkin.setText(s);
            }
            s = (String) ois.readObject();
            if (s.equals("MX")) {
                cmbMission.setSelectedIndex(0);

            } else {
                cmbMission.setSelectedIndex(1);
                txtMission.setEnabled(true);
                txtMission.setText(s);
            }
            boolean fs = (((Boolean) ois.readObject()).booleanValue());
            bFS.setSelected(fs);
            aux = (((Integer) ois.readObject()).intValue());
            if (aux == 1) {
                cmbCont.setSelectedIndex(1);
                bCtrlSettings.setEnabled(true);
                if (!checkGP()) {
                    cmbCont.setSelectedIndex(0);
                    bCtrlSettings.setEnabled(false);
                }
            } else {
                cmbCont.setSelectedIndex(0);

            }
            mygame.CommonVar.GPFire = ((Integer) ois.readObject()).intValue();
            mygame.CommonVar.GPRockets = ((Integer) ois.readObject()).intValue();
            mygame.CommonVar.GPPause = ((Integer) ois.readObject()).intValue();
            cUseProxy.setSelected(((Boolean) ois.readObject()).booleanValue());
            proxyIp.setText((String) (ois.readObject()));
            proxyPort.setText((String) (ois.readObject()));
            aux = ((Integer) ois.readObject()).intValue();
            if (aux >= cmbRes.getItemCount()) {
                aux = cmbRes.getItemCount() - 1;
            }
            if (aux < 0) {
                aux = 0;
            }
            cmbRes.setSelectedIndex(aux);
            ois.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("Can't load settings: " + e.toString());
            return 1; //io error
        }
        return 0; //ok
    }

    private int saveSettings() {
        try {
            File f = new File("missionX.cfg");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new Integer(cmbSFX.getSelectedIndex()));
            oos.writeObject(new Integer(cmbMusic.getSelectedIndex()));
            oos.writeObject(new Integer(cmbLang.getSelectedIndex()));
            oos.writeObject(new Integer(cmbGfxQ.getSelectedIndex()));
            oos.writeObject(new Integer(mSpeed.getValue()));
            oos.writeObject(new Integer(frSmoothing.getValue()));
            oos.writeObject(new Boolean(VSynch.isSelected()));

            if (cmbSkin.getSelectedIndex() == 0) {
                oos.writeObject("defSkin");

            } else {
                oos.writeObject(txtSkin.getText());

            }
            if (cmbMission.getSelectedIndex() == 0) {
                oos.writeObject("MX");

            } else {
                oos.writeObject(txtMission.getText());

            }
            oos.writeObject(new Boolean(bFS.isSelected()));
            oos.writeObject(new Integer(cmbCont.getSelectedIndex()));
            oos.writeObject(new Integer(mygame.CommonVar.GPFire));
            oos.writeObject(new Integer(mygame.CommonVar.GPRockets));
            oos.writeObject(new Integer(mygame.CommonVar.GPPause));
            oos.writeObject(new Boolean(cUseProxy.isSelected()));
            oos.writeObject(proxyIp.getText());
            oos.writeObject(proxyPort.getText());
            oos.writeObject(new Integer(cmbRes.getSelectedIndex()));
            oos.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("Can't save settings: " + e.toString());
            return 1;
        }
        return 0;
    }

    private void disabilitaForm() {
        actionPanel.setEnabled(false);
        bPlay.setEnabled(false);
        bClose.setEnabled(false);
        bDef.setEnabled(false);
        bMinimize.setEnabled(false);
        bClose.setEnabled(false);
        bQuit.setEnabled(false);
        VSynch.setEnabled(false);
        bResetHS.setEnabled(false);
        cmbGfxQ.setEnabled(false);
        bFS.setEnabled(false);
        cmbLang.setEnabled(false);
        cmbMission.setEnabled(false);
        cmbMusic.setEnabled(false);
        cmbSFX.setEnabled(false);
        frSmoothing.setEnabled(false);
        mSpeed.setEnabled(false);
        txtMission.setEnabled(false);
        txtSkin.setEnabled(false);
        cmbSkin.setEnabled(false);
        configPanels.setEnabled(false);
        bPlay.setSelected(true);
        cmbCont.setEnabled(false);
        bCtrlSettings.setEnabled(false);
        cUseProxy.setEnabled(false);
        proxyIp.setEnabled(false);
        proxyPort.setEnabled(false);
        cmbRes.setEnabled(false);
    }

    private void abilitaForm() {
        actionPanel.setEnabled(true);
        bPlay.setEnabled(true);
        bClose.setEnabled(true);
        bDef.setEnabled(true);
        bMinimize.setEnabled(true);
        bClose.setEnabled(true);
        bQuit.setEnabled(true);
        VSynch.setEnabled(true);
        bResetHS.setEnabled(true);
        cmbGfxQ.setEnabled(true);
        cmbLang.setEnabled(true);
        cmbMission.setEnabled(true);
        cmbMusic.setEnabled(true);
        cmbSFX.setEnabled(true);
        frSmoothing.setEnabled(true);
        mSpeed.setEnabled(true);
        cmbSkin.setEnabled(true);
        cmbSkinActionPerformed(null);
        cmbMission.setEnabled(true);
        cmbMissionActionPerformed(null);
        configPanels.setEnabled(true);
        bPlay.setSelected(true);
        cmbCont.setEnabled(true);
        if (cmbCont.getSelectedIndex() == 1) {
            bCtrlSettings.setEnabled(true);
        }
        cUseProxy.setEnabled(true);
        cUseProxyActionPerformed(null);
        cmbRes.setEnabled(true);
    }

    @SuppressWarnings("empty-statement")
    private void bPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPlayActionPerformed

        if (cmbSkin.getSelectedIndex() == 1 && txtSkin.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: you must write the skin name or set the default skin!");
            return;
        }
        if (cmbSkin.getSelectedIndex() == 1) {
            File f = new File("images/menu/" + txtSkin.getText());
            if (!(new File("images/menu/" + txtSkin.getText()).isDirectory() && (new File("sfx/" + txtSkin.getText()).isDirectory()))) {
                JOptionPane.showMessageDialog(this, "Error: skin " + txtSkin.getText() + " not found!");
                return;
            }
        }
        //disabilitaForm();
        switch (cmbSFX.getSelectedIndex()) {
            case 0:
                mygame.CommonVar.useSFX = true;
                break;
            case 1:
                mygame.CommonVar.useSFX = false;
                break;
        }
        switch (cmbMusic.getSelectedIndex()) {
            case 0:
                mygame.CommonVar.music = true;
                break;
            case 1:
                mygame.CommonVar.music = false;
                break;
        }
        mygame.CommonVar.CDMode = true;
        int n;
        if (cmbGfxQ.getSelectedIndex() == 0) {
            mygame.CommonVar.gfxQuality = mygame.CommonVar.gfx_vlow;
            mygame.CommonVar.lowRes=true;
        }
        if (cmbGfxQ.getSelectedIndex() == 1) {
            mygame.CommonVar.gfxQuality = mygame.CommonVar.gfx_low;
            mygame.CommonVar.lowRes=true;
        }
        if (cmbGfxQ.getSelectedIndex() == 2) {
            mygame.CommonVar.gfxQuality = mygame.CommonVar.gfx_mid;
            mygame.CommonVar.lowRes=true;
        }
        if (cmbGfxQ.getSelectedIndex() == 3) {
            mygame.CommonVar.gfxQuality = mygame.CommonVar.gfx_hi;
        }
        if (cmbGfxQ.getSelectedIndex() >3) {
            mygame.CommonVar.gfxQuality = 0;
        }

        mygame.CommonVar.fullscreen = bFS.isSelected();

        if (cmbLang.getSelectedIndex() == 0) {
            mygame.CommonVar.loc = "en";

        } else {
            mygame.CommonVar.loc = "it";

        }
        if (cmbGfxQ.getSelectedIndex()>=2) {
            mygame.CommonVar.smoothing = true;
        } else {
            mygame.CommonVar.smoothing = false;
        }
        mygame.CommonVar.mouseSpeed = (float) mSpeed.getValue() / 100;
        org.easyway.system.StaticRef.frameRateSmoothing = frSmoothing.getValue();
        mygame.CommonVar.VSynch = VSynch.isSelected();
        if (cmbSkin.getSelectedIndex() == 0) {
            mygame.CommonVar.skin = "defSkin";

        } else {
            mygame.CommonVar.skin = txtSkin.getText();

        }
        if (cmbMission.getSelectedIndex() == 0) {
            mygame.CommonVar.level = "maps/missionx.txt";

        } else {
            if (txtMission.getText().contains(".txt")) {
                mygame.CommonVar.level = txtMission.getText();
            } else {
                mygame.CommonVar.level = txtMission.getText() + "/maps/default.txt";
            }

        }
        String mapfile = mygame.CommonVar.level;
        mygame.CommonVar.preLoadBackgrounds = true;
        if (cmbCont.getSelectedIndex() == 0) {
            mygame.CommonVar.useGamePad = false;
        } else {
            mygame.CommonVar.useGamePad = true;
        }
        if (mapfile.startsWith("/")) {
            mapfile = mapfile.substring(1);
        }
        int index;
        // path = path.replaceAll("\\", "/");
        while ((index = mapfile.indexOf("\\")) != -1) {
            mapfile = mapfile.substring(0, index) + '/' + mapfile.substring(index + 1);
        }
        try {
            InputStreamReader isr = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(mapfile));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Map not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        disabilitaForm();
        saveSettings();
        mygame.CommonVar.xRes = m.get(cmbRes.getSelectedIndex()).getWidth();
        mygame.CommonVar.yRes = m.get(cmbRes.getSelectedIndex()).getHeight();
        new mygame.Menu();
        new GameStartedChecker().start();  //attende fino a quando il gioco è pronto
    }//GEN-LAST:event_bPlayActionPerformed
    private class GameStartedChecker extends Thread {

        public GameStartedChecker() {
        }

        public void run() {
            while (!mygame.CommonVar.gameStarted) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            setVisible(false); //nasconde la finestra della cfg
        }
    }
    private void bDefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDefActionPerformed
        cmbSFX.setSelectedIndex(0);
        cmbMusic.setSelectedIndex(0);
        cmbLang.setSelectedIndex(0);
        cmbGfxQ.setSelectedIndex(4);
        mSpeed.setValue(85);
        frSmoothing.setValue(10);
        VSynch.setSelected(true);
        cmbSkin.setSelectedIndex(0);
        txtSkin.setEnabled(false);
        bFS.setSelected(false);
        cmbMission.setSelectedIndex(0);
        txtMission.setEnabled(false);
        txtMission.setText("");
        txtSkin.setText("");
        cmbCont.setSelectedIndex(0);
        bCtrlSettings.setEnabled(false);
        mygame.CommonVar.GPFire = 0;
        mygame.CommonVar.GPRockets = 1;
        mygame.CommonVar.GPPause = 5;
        cUseProxy.setSelected(false);
        cUseProxyActionPerformed(null);
    }//GEN-LAST:event_bDefActionPerformed

    private void bResetHSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bResetHSActionPerformed
        File f = new File(".");
        File[] list = f.listFiles();
        for (File x : list) {
            if (x.getName().contains(".hs")) {
                x.delete();
            }
        }
        //new File("missionx.hs").delete();
        JOptionPane.showMessageDialog(this, "Done!");
    }//GEN-LAST:event_bResetHSActionPerformed

    private void bQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bQuitActionPerformed
        saveSettings();
        new CloseAnimation(this).start();

    }//GEN-LAST:event_bQuitActionPerformed
    private class CloseAnimation extends Thread {

        private Starter s = null;

        public CloseAnimation(Starter s) {
            this.s = s;
        }

        public void run() {
            for (float i = 1; i > 0; i -= 0.05f) {
                setSize((int) xSize, (int) (ySize * i));
                s.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            setSize((int) xSize, 20);
            for (float i = 1; i > 0; i -= 0.05f) {
                setSize((int) (xSize * i), 20);
                s.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.exit(0);
        }
    }

    private void bMinimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMinimizeActionPerformed
        setState(ICONIFIED);
    }//GEN-LAST:event_bMinimizeActionPerformed

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseActionPerformed
        this.bQuitActionPerformed(null);
    }//GEN-LAST:event_bCloseActionPerformed

    private void cmbMissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMissionActionPerformed
        if (cmbMission.getSelectedIndex() == 1) {
            txtMission.setEnabled(true);
        } else {
            txtMission.setEnabled(false);
        }
}//GEN-LAST:event_cmbMissionActionPerformed

    private void cmbSkinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSkinActionPerformed
        if (cmbSkin.getSelectedIndex() == 1) {
            txtSkin.setEnabled(true);
        } else {
            txtSkin.setEnabled(false);
        }
}//GEN-LAST:event_cmbSkinActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        bQuitActionPerformed(null);
    }//GEN-LAST:event_formWindowClosing

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        titleBar.setIcon(new ImageIcon("images/loaderBar_Focused.png"));
    }//GEN-LAST:event_formWindowGainedFocus

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
        titleBar.setIcon(new ImageIcon("images/loaderBar_UnFocused.png"));
    }//GEN-LAST:event_formWindowLostFocus

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (System.getProperty("os.name").contains("Windows")) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM MX_WIN.exe"); //destroy the splash screen!
            } catch (IOException ex) {
                Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_formWindowOpened

    private void titleBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleBarMouseDragged
        try {
            Point p = java.awt.MouseInfo.getPointerInfo().getLocation();
            //SwingUtilities.convertPointToScreen(p, this);
            setLocation(p.x - 220, p.y - 10);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_titleBarMouseDragged

    private void cmbContActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbContActionPerformed
        if (cmbCont.getSelectedIndex() == 1) {
            bCtrlSettings.setEnabled(true);
            if (!checkGP()) {
                cmbCont.setSelectedIndex(0);
                bCtrlSettings.setEnabled(false);
                //JOptionPane.showMessageDialog(this, "Couldn't find any compatible gamepad/joystick", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            bCtrlSettings.setEnabled(false);
        }
    }//GEN-LAST:event_cmbContActionPerformed
    private boolean checkGP() {
        Controller[] c = net.java.games.input.ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (Controller x : c) {
            if (x.getType().toString().equalsIgnoreCase("Stick") || x.getType().toString().equalsIgnoreCase("Gamepad")) {
                return true;
            }
        }
        return false;
    }
    private void bCtrlSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCtrlSettingsActionPerformed
        GPSettings gps = new GPSettings();
        gps.setVisible(true);
        gps.setLocation(250, 250);
    }//GEN-LAST:event_bCtrlSettingsActionPerformed

    private void cmbGfxQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGfxQActionPerformed

    }//GEN-LAST:event_cmbGfxQActionPerformed

    private void cmbLangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLangActionPerformed
        int comboStat[] = new int[7];
        //let's save the status of the combos to avoid resetting
        comboStat[0] = cmbSFX.getSelectedIndex();
        comboStat[1] = cmbMusic.getSelectedIndex();
        comboStat[2] = bFS.isSelected() ? 1 : 0;
        comboStat[3] = cmbCont.getSelectedIndex();
        comboStat[4] = cmbGfxQ.getSelectedIndex();
        comboStat[5] = cmbSkin.getSelectedIndex();
        comboStat[6] = cmbMission.getSelectedIndex();
        //let's change combos and labels
        if (cmbLang.getSelectedIndex() == 1) { //italiano
            cmbSFX.removeAllItems();
            cmbSFX.addItem("Attivo");
            cmbSFX.addItem("Disattivo");
            cmbSFX.setSelectedIndex(comboStat[0]);
            cmbMusic.removeAllItems();
            cmbMusic.addItem("Attivo");
            cmbMusic.addItem("Disattivo");
            cmbMusic.setSelectedIndex(comboStat[1]);
            bFS.setSelected(comboStat[2] == 1 ? true : false);
            cmbCont.removeAllItems();
            cmbCont.addItem("Mouse");
            cmbCont.addItem("Gamepad/Joystick");
            cmbCont.setSelectedIndex(comboStat[3]);
            cmbGfxQ.removeAllItems();
            cmbGfxQ.addItem("Molto bassa");
            cmbGfxQ.addItem("Bassa");
            cmbGfxQ.addItem("Media");
            cmbGfxQ.addItem("Alta");
            cmbGfxQ.addItem("Automatica (consigliato)");
            cmbGfxQ.setSelectedIndex(comboStat[4]);
            cmbSkin.removeAllItems();
            cmbSkin.addItem("Default");
            cmbSkin.addItem("Altra (scrivi sotto)");
            cmbSkin.setSelectedIndex(comboStat[5]);
            cmbMission.removeAllItems();
            cmbMission.addItem("Default");
            cmbMission.addItem("Altra (scrivi sotto)");
            cmbMission.setSelectedIndex(comboStat[6]);
            bCtrlSettings.setText("Configura");
            bPlay.setText("Gioca");
            bResetHS.setText("Resetta punteggi");
            bDef.setText("Resetta impostazioni");
            bQuit.setText("Esci");
            lblSFX.setText("Suoni");
            lblMusic.setText("Musica");
            lblRes.setText("Risluzione");
            lblFRS.setText("Framerate Smoothing");
            lblCont.setText("Controlli");
            lblContInfo.setText("Puoi cambiare tra mouse e gamepad mentre giochi premendo Invio");
            lblLang.setText("Lingua");
            lblSens.setText("Sensibilità controlli");
            lblMission.setText("Missione");
            lblGfxQ.setText("Qualità grafica");
            lblSkin.setText("Skin");
            jLabel2.setBorder(new TitledBorder("Azioni"));
            settings1.setName("Impostazioni");
            settings2.setName("Altre impostazioni");
            mods.setName("Contenuti personali");
            configPanels.removeAll();
            configPanels.add(settings1);
            configPanels.add(settings2);
            configPanels.add(mods);
            jLabel5.setBorder(new TitledBorder("Aggiornamenti"));
            cUseProxy.setText("Connetti attraverso proxy HTTP");
            jLabel1.setText("Indirizzo proxy");
            jButton1.setText("Controlla aggiornamenti");
            lblGfxQInfo.setText("Su netbook e computer vecchi, scegli qualità bassa o media");
        } else { //english
            cmbSFX.removeAllItems();
            cmbSFX.addItem("Enabled");
            cmbSFX.addItem("Disabled");
            cmbSFX.setSelectedIndex(comboStat[0]);
            cmbMusic.removeAllItems();
            cmbMusic.addItem("Enabled");
            cmbMusic.addItem("Disabled");
            cmbMusic.setSelectedIndex(comboStat[1]);
            bFS.setSelected(comboStat[2] == 1 ? true : false);
            cmbCont.removeAllItems();
            cmbCont.addItem("Mouse");
            cmbCont.addItem("Gamepad/Joystick");
            cmbCont.setSelectedIndex(comboStat[3]);
            cmbGfxQ.removeAllItems();
            cmbGfxQ.addItem("Very low");
            cmbGfxQ.addItem("Low");
            cmbGfxQ.addItem("Medium");
            cmbGfxQ.addItem("High");
            cmbGfxQ.addItem("Auto (recommended)");
            cmbGfxQ.setSelectedIndex(comboStat[4]);
            cmbSkin.removeAllItems();
            cmbSkin.addItem("Default");
            cmbSkin.addItem("Other (write under)");
            cmbSkin.setSelectedIndex(comboStat[5]);
            cmbMission.removeAllItems();
            cmbMission.addItem("Default");
            cmbMission.addItem("Other (write under)");
            cmbMission.setSelectedIndex(comboStat[6]);
            bCtrlSettings.setText("Settings");
            bPlay.setText("Play");
            bResetHS.setText("Reset hi scores");
            bDef.setText("Default settings");
            bQuit.setText("Quit");
            lblSFX.setText("Sound effects");
            lblMusic.setText("Music");
            lblRes.setText("Risluzione");
            lblFRS.setText("Framerate Smoothing");
            lblCont.setText("Controller");
            lblContInfo.setText("You can switch between mouse and gamepad at any time by pressing Enter");
            lblLang.setText("Language");
            lblSens.setText("Controller sensivity");
            lblMission.setText("Mission");
            lblGfxQ.setText("Graphics quality");
            lblSkin.setText("Skin");
            jLabel2.setBorder(new TitledBorder("Actions"));
            settings1.setName("Settings");
            settings2.setName("Advanced settings");
            mods.setName("Custom maps and mods");
            configPanels.removeAll();
            configPanels.add(settings1);
            configPanels.add(settings2);
            configPanels.add(mods);
            jLabel5.setBorder(new TitledBorder("Updates"));
            cUseProxy.setText("Connect through HTTP Proxy");
            jLabel1.setText("Proxy address");
            jButton1.setText("Check for updates");
            lblGfxQInfo.setText("Tip: On netbooks and old computers, choose low or medium settings");
        }
        //Change font for linux
        if (System.getProperty("os.name").startsWith("Linux")) {
            lblSFX.setFont(new Font("DejaVu", 0, 11));
            lblMusic.setFont(new Font("DejaVu", 0, 11));
            lblRes.setFont(new Font("DejaVu", 0, 11));
            lblFRS.setFont(new Font("DejaVu", 0, 11));
            lblCont.setFont(new Font("DejaVu", 0, 11));
            lblContInfo.setFont(new Font("DejaVu", 0, 10));
            lblLang.setFont(new Font("DejaVu", 0, 11));
            lblSens.setFont(new Font("DejaVu", 0, 11));
            lblMission.setFont(new Font("DejaVu", 0, 11));
            lblGfxQ.setFont(new Font("DejaVu", 0, 11));
            lblSkin.setFont(new Font("DejaVu", 0, 11));
            cUseProxy.setFont(new Font("DejaVu", 0, 11));
            jLabel1.setFont(new Font("DejaVu", 0, 11));
            jButton1.setFont(new Font("DejaVu", 0, 11));
            VSynch.setFont(new Font("DejaVu", 0, 11));
            configPanels.setFont(new Font("DejaVu", 0, 11));
            lblGfxQInfo.setFont(new Font("DejaVu", 0, 11));
            bFS.setFont(new Font("DejaVu", 0, 11));
        }
    }//GEN-LAST:event_cmbLangActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new UpdateThread(true).start(); //show errors

    }//GEN-LAST:event_jButton1ActionPerformed

    private void cUseProxyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cUseProxyActionPerformed
        if (cUseProxy.isSelected()) {
            proxyIp.setEnabled(true);
            proxyPort.setEnabled(true);
        } else {
            proxyIp.setEnabled(false);
            proxyPort.setEnabled(false);
            proxyIp.setText("");
            proxyPort.setText("");
        }
    }//GEN-LAST:event_cUseProxyActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {

                    //UIManager.setLookAndFeel("com.nilo.plaf.nimrod.NimRODLookAndFeel");

                    NimRODTheme nt = new NimRODTheme();
                    nt.setPrimary1(new Color(255, 24, 24));
                    nt.setPrimary2(new Color(255, 34, 34));
                    nt.setPrimary3(new Color(255, 44, 44));
                    nt.setSecondary1(new Color(25, 30, 39));
                    nt.setSecondary2(new Color(35, 40, 49));
                    nt.setSecondary3(new Color(45, 50, 59));
                    nt.setWhite(new Color(73, 72, 84));
                    nt.setBlack(new Color(255, 255, 255));
                    nt.setMenuOpacity(195);
                    nt.setFrameOpacity(180);
                    NimRODLookAndFeel lef = new NimRODLookAndFeel();
                    lef.setCurrentTheme(nt);
                    UIManager.setLookAndFeel(lef);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Warning: Couldn't set LookAndFeel. The config app will look bad, but the game won't be compromised.", "NimROD Error", JOptionPane.ERROR_MESSAGE);
                }

                new Starter().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox VSynch;
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton bClose;
    private javax.swing.JButton bCtrlSettings;
    private javax.swing.JButton bDef;
    private javax.swing.JCheckBox bFS;
    private javax.swing.JButton bMinimize;
    private javax.swing.JButton bPlay;
    private javax.swing.JButton bQuit;
    private javax.swing.JButton bResetHS;
    private javax.swing.JCheckBox cUseProxy;
    private javax.swing.JComboBox cmbCont;
    private javax.swing.JComboBox cmbGfxQ;
    private javax.swing.JComboBox cmbLang;
    private javax.swing.JComboBox cmbMission;
    private javax.swing.JComboBox cmbMusic;
    private javax.swing.JComboBox cmbRes;
    private javax.swing.JComboBox cmbSFX;
    private javax.swing.JComboBox cmbSkin;
    private javax.swing.JPanel configAppPanel;
    private javax.swing.JTabbedPane configPanels;
    private javax.swing.JSlider frSmoothing;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblCont;
    private javax.swing.JLabel lblContInfo;
    private javax.swing.JLabel lblFRS;
    private javax.swing.JLabel lblGfxQ;
    private javax.swing.JLabel lblGfxQInfo;
    private javax.swing.JLabel lblLang;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMission;
    private javax.swing.JLabel lblMusic;
    private javax.swing.JLabel lblRes;
    private javax.swing.JLabel lblSFX;
    private javax.swing.JLabel lblSens;
    private javax.swing.JLabel lblSkin;
    private javax.swing.JPanel logo;
    private javax.swing.JSlider mSpeed;
    private javax.swing.JPanel mods;
    private javax.swing.JTextField proxyIp;
    private javax.swing.JTextField proxyPort;
    private javax.swing.JPanel settings1;
    private javax.swing.JPanel settings2;
    private javax.swing.JLabel titleBar;
    private javax.swing.JTextField txtMission;
    private javax.swing.JTextField txtSkin;
    // End of variables declaration//GEN-END:variables
}
