/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Palette.java
 *
 * Created on 24-apr-2009, 16.13.33
 */
package org.easyway.ueditor2.rightPanels;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.easyway.ueditor2.ClassSelectorDialog;
import org.easyway.ueditor2.EditorCore;
import org.easyway.ueditor2.effects.Effect;
import org.easyway.ueditor2.system.ClassLocation;
import org.easyway.ueditor2.system.ClassLocator;
import org.easyway.ueditor2.system.FilterUtility;

/**
 *
 * @author Daniele
 */
public class Palette extends javax.swing.JPanel {

    DefaultListModel listModel = new DefaultListModel();
    DefaultListModel originalListModel = new DefaultListModel();
    public BeanEditor beanEditor;
    public EditorCore editorCore;

    /** Creates new form Palette */
    public Palette() {
        initComponents();
        paletteList.setModel(listModel);
    }

    public boolean addElementToPalette(Class clazz) {
        if (!originalListModel.contains(clazz)) {
            listModel.addElement(clazz);
            originalListModel.addElement(clazz);
            return true;
        }
        return false;
    }

    public void removeElementFromPalette(Class clazz) {
        int index;
        if ((index = originalListModel.indexOf(clazz)) >= 0) {
            originalListModel.removeElementAt(index);
            if ((index = listModel.indexOf(clazz)) >= 0) {
                listModel.removeElementAt(index);
            }
        }


    }

    public void filter() {
        listModel.clear();
        for (Object clazz : originalListModel.toArray()) {
            if (clazz.toString().toLowerCase().contains(filterField.getText().toLowerCase())) {
                listModel.addElement(clazz);
            }

        }
    }

    public void createSelected() {
        if (beanEditor == null) {
            return;
        }

        final Class clazz = (Class) paletteList.getSelectedValue();
        if (clazz == null) {
            return;
        }

        new Effect() {

            @Override
            public void loop() {
                try {
                    Object newObject = clazz.newInstance();
                    editorCore.setSelected(newObject);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Palette.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Palette.class.getName()).log(Level.SEVERE, null, ex);
                } catch (final Exception e) {
                    destroy();
                    new Thread() {

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(EditorCore.getMainFrame(), "EXCEPTION:\n " + e.getMessage(), "EXCEPTION", JOptionPane.ERROR_MESSAGE);
                        }
                    }.start();
                } finally {
                    destroy();
                }

            }
        };
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addClassesMenu = new javax.swing.JPopupMenu();
        classMenu = new javax.swing.JMenuItem();
        pathMenu = new javax.swing.JMenuItem();
        fileMenu = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        paletteList = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        filterField = new javax.swing.JTextField();
        applyFilterButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        classMenu.setText("add single Class");
        classMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classMenuActionPerformed(evt);
            }
        });
        addClassesMenu.add(classMenu);

        pathMenu.setText("add classes from package");
        pathMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pathMenuActionPerformed(evt);
            }
        });
        addClassesMenu.add(pathMenu);

        fileMenu.setText("add classes from txt file");
        fileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });
        addClassesMenu.add(fileMenu);

        paletteList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paletteListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(paletteList);

        jButton1.setText("Create");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("new JavaBean");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        applyFilterButton.setText("Apply");
        applyFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyFilterButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Filter:");

        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterField, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applyFilterButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(applyFilterButton)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        createSelected();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addClassesMenu.show(jButton2, 0, 0);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void classMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classMenuActionPerformed

        try {
            String response = JOptionPane.showInputDialog(EditorCore.getMainFrame(), "Write the path of a new object\nExample: org.mygame.Hero", "Load a new Palette Object", JOptionPane.QUESTION_MESSAGE);
            if (response == null) {
                return;
            }
            Class clazz = Class.forName(response);
            if (!FilterUtility.isInstanceOf(clazz, Serializable.class)) {
                JOptionPane.showMessageDialog(EditorCore.getMainFrame(), "The object should implements the Serializable interface!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            addElementToPalette(clazz);
        } catch (ClassNotFoundException ex) {
            // Logger.getLogger(Palette.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_classMenuActionPerformed

    private void pathMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pathMenuActionPerformed
        String pakkage = null;
        try {
            pakkage = JOptionPane.showInputDialog(EditorCore.getMainFrame(), "Write the path of a package containing objects\nExample: org.mygame\n" +
                    "Will be added only the objects that implements the Serializable Interface", "Load a new Palette Object", JOptionPane.QUESTION_MESSAGE);
            if (pakkage == null) {
                return;
            }
            ClassLocator locator = new ClassLocator(pakkage);
            if (locator == null) {
                return;
            }
            List<ClassLocation> locations = locator.getAllClassLocations();
            ArrayList<Class> classList = new ArrayList<Class>(locations.size());
            for (ClassLocation location : locations) {
                classList.add(Class.forName(location.getClassName()));
            }

            final ClassSelectorDialog dialog = new ClassSelectorDialog(EditorCore.getMainFrame(), classList);
            dialog.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {

                    ArrayList<Class> classes = dialog.getSelectedClasses();
                    for (Class cls : classes) {
                        addElementToPalette(cls);
                    }

                    repaint();
                    super.windowClosing(e);
                }
            });
            dialog.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(Palette.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            // Logger.getLogger(Palette.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(EditorCore.getMainFrame(), "Pakage "+pakkage+" si not valid", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_pathMenuActionPerformed

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileMenuActionPerformed

    private void paletteListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paletteListMouseClicked
        if (evt.getClickCount() >= 2) {
            createSelected();
        }
    }//GEN-LAST:event_paletteListMouseClicked

    private void applyFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyFilterButtonActionPerformed
        filter();
    }//GEN-LAST:event_applyFilterButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        final Class clazz = (Class) paletteList.getSelectedValue();
        if (clazz == null) {
            return;
        }

        removeElementFromPalette(clazz);
    }//GEN-LAST:event_jButton3ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu addClassesMenu;
    private javax.swing.JButton applyFilterButton;
    private javax.swing.JMenuItem classMenu;
    private javax.swing.JMenuItem fileMenu;
    private javax.swing.JTextField filterField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList paletteList;
    private javax.swing.JMenuItem pathMenu;
    // End of variables declaration//GEN-END:variables
}