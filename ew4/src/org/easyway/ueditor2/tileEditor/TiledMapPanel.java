/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TiledMapPanel.java
 *
 * Created on 27-apr-2009, 13.00.47
 */
package org.easyway.ueditor2.tileEditor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import org.easyway.ueditor2.EditorCore;
import org.easyway.ueditor2.StartUEditor2;
import org.easyway.tiles.TileMapLayer;
import org.easyway.tiles.TileMap;
import org.easyway.tiles.TileSet;

/**
 *
 * @author Daniele
 */
public class TiledMapPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1272476548290964733L;
    TileMap currentTileMap;
    TileDrawer tileDrawer;

    public boolean isNotDrawing() {
        return tileDrawer == null;
    }

    /** Creates new form TiledMapPanel */
    public TiledMapPanel() {
        initComponents();
        init();
        initLayers();
        initTileSet();
    }

    public void refresh() {
        init();
        initLayers();
        initTileSet();
    }

    public void init() {
        listTileMap.removeAllItems();
        for (TileMap tileMap : TileMap.tileMapList) {
            listTileMap.addItem(tileMap);
        }
        listTileMap.repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        LayerMapMenu = new javax.swing.JPopupMenu();
        newLayerMap = new javax.swing.JMenuItem();
        existLayerMap = new javax.swing.JMenuItem();
        loadLayerMap = new javax.swing.JMenuItem();
        tileMapMenu = new javax.swing.JPopupMenu();
        newTiledMap = new javax.swing.JMenuItem();
        loadTiledMap = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        listTileMap = new javax.swing.JComboBox();
        deleteTileMapButton = new javax.swing.JButton();
        editTileMapButton = new javax.swing.JButton();
        addTiledMapButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        layerID = new javax.swing.JComboBox();
        removeLayerButton = new javax.swing.JButton();
        addLayerMapButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        tileSetComboBox = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        startDrawingButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        newLayerMap.setText("New");
        newLayerMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newLayerButtonActionPerformed(evt);
            }
        });
        LayerMapMenu.add(newLayerMap);

        existLayerMap.setText("Existent Layer Map");
        LayerMapMenu.add(existLayerMap);

        loadLayerMap.setText("Load From File");
        LayerMapMenu.add(loadLayerMap);

        newTiledMap.setText("New");
        newTiledMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTileMapButtonActionPerformed(evt);
            }
        });
        tileMapMenu.add(newTiledMap);

        loadTiledMap.setText("Load From File");
        tileMapMenu.add(loadTiledMap);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setText("TileMap:");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), listTileMap, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        listTileMap.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listTileMapItemStateChanged(evt);
            }
        });
        listTileMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listTileMapActionPerformed(evt);
            }
        });

        deleteTileMapButton.setText("Delete");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), deleteTileMapButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        editTileMapButton.setText("Edit");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), editTileMapButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        addTiledMapButton.setText("+");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), addTiledMapButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        addTiledMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTiledMapButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listTileMap, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addTiledMapButton)
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(editTileMapButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteTileMapButton)
                .addGap(72, 72, 72))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(listTileMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTiledMapButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteTileMapButton)
                    .addComponent(editTileMapButton))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setText("Layer id:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), layerID, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        layerID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                layerIDItemStateChanged(evt);
            }
        });

        removeLayerButton.setText("Remove");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), removeLayerButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        addLayerMapButton.setText("+");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), addLayerMapButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        addLayerMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLayerMapButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Select");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), jButton1, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("TileSet:");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), tileSetComboBox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        tileSetComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tileSetComboBoxItemStateChanged(evt);
            }
        });

        jButton2.setText("+");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${notDrawing}"), jButton2, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(layerID, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addLayerMapButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(removeLayerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tileSetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(layerID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addLayerMapButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeLayerButton)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tileSetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jLabel3))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        startDrawingButton.setText("Start Drawing");
        startDrawingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDrawingButtonActionPerformed(evt);
            }
        });

        jButton3.setText("Refresh all");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startDrawingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(407, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startDrawingButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void initLayers() {
        currentTileMap = (TileMap) listTileMap.getSelectedItem();
        if (currentTileMap == null || currentTileMap.getTileManagers() == null) {
            return;
        }
        layerID.removeAllItems();
        for (TileMapLayer tileManager : currentTileMap.getTileManagers()) {
            if (tileManager == null) {
                continue;
            }
            layerID.addItem(tileManager);
        }
        layerID.repaint();
    }

    private void initTileSet() {
        ArrayList<TileSet> list = TileSet.getAllTileSet();
        tileSetComboBox.removeAllItems();
        for (TileSet ts : list) {
            tileSetComboBox.addItem(ts);
        }

        if (currentTileMap != null) {
            TileMapLayer tm = (TileMapLayer) layerID.getSelectedItem();
            if (tm != null) {
                if (tm.getTileSet() != null) {
                    tileSetComboBox.setSelectedItem(tm.getTileSet());
                } else if (tileSetComboBox.getItemAt(0) != null) {
                    tm.setTileSet((TileSet) tileSetComboBox.getItemAt(0));
                }
            }
        }
        tileSetComboBox.repaint();
    }

    private void newTileMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTileMapButtonActionPerformed
        NewTileMapDialog dialog = new NewTileMapDialog(EditorCore.getMainFrame(), true);
        dialog.setVisible(true);
        dialog.requestFocus();
        dialog.requestFocusInWindow();

        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                init();
                super.windowClosed(e);
            }
        });

    }//GEN-LAST:event_newTileMapButtonActionPerformed

    private void listTileMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listTileMapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_listTileMapActionPerformed

    private void listTileMapItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listTileMapItemStateChanged
        initLayers();
    }//GEN-LAST:event_listTileMapItemStateChanged

    private void newLayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newLayerButtonActionPerformed
        if (currentTileMap == null) {
            return;
        }
        NewTileManagerDialog dialog = new NewTileManagerDialog(EditorCore.getMainFrame(), true, currentTileMap);

        dialog.setVisible(true);
        dialog.requestFocusInWindow();
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                initLayers();
                layerID.repaint();
                super.windowClosed(e);
            }
        });
    }//GEN-LAST:event_newLayerButtonActionPerformed

    private void addLayerMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLayerMapButtonActionPerformed
        LayerMapMenu.show(addLayerMapButton, 0, 0);
    }//GEN-LAST:event_addLayerMapButtonActionPerformed

    private void addTiledMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTiledMapButtonActionPerformed
        tileMapMenu.show(addTiledMapButton, 0, 0);
}//GEN-LAST:event_addTiledMapButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        EditorCore.getInstance().setSelected(layerID.getSelectedItem());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        final TileSetManagerDialog dialog = new TileSetManagerDialog(EditorCore.getMainFrame(), true, true);
        dialog.setVisible(true);

        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                initTileSet();
                TileSet ts = dialog.getSelectedTileSet();
                if (ts != null) {
                    tileSetComboBox.setSelectedItem(ts);
                }
                super.windowClosed(e);
            }
        });
    }//GEN-LAST:event_jButton2ActionPerformed

    public TileMapLayer getSelectedTileManager() {
        return (TileMapLayer) layerID.getSelectedItem();
    }

    private void layerIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_layerIDItemStateChanged
        TileMapLayer tm = (TileMapLayer) layerID.getSelectedItem();
        if (tm == null) {
            return;
        }

        TileSet ts = tm.getTileSet();
        if (ts != null) {
            tileSetComboBox.setSelectedItem(ts);
        } else if (tileSetComboBox.getItemAt(0) != null) {
            tm.setTileSet((TileSet) tileSetComboBox.getItemAt(0));
        }
        final EditorCore core = EditorCore.getInstance();
        EditorCore.setWidthGrid(tm.getTileWidth());
        EditorCore.setHeightGrid(tm.getTileHeight());

    }//GEN-LAST:event_layerIDItemStateChanged

    private void tileSetComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tileSetComboBoxItemStateChanged
        if (currentTileMap != null) {
            TileMapLayer tm = (TileMapLayer) layerID.getSelectedItem();
            TileSet ts = (TileSet) tileSetComboBox.getSelectedItem();
            if (tm != null && ts != null) {
                tm.setTileSet(ts);
            }
        }
    }//GEN-LAST:event_tileSetComboBoxItemStateChanged

    private void startDrawingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDrawingButtonActionPerformed

        if (getSelectedTileManager() == null) {
            return;
        }
        final TileMapLayer tileManagerSelected = getSelectedTileManager();
        if (tileManagerSelected.getTileSet() == null) {
            return;
        }
        final EditorCore core = EditorCore.getInstance();
        EditorCore.setWidthGrid(tileManagerSelected.getTileWidth());
        EditorCore.setHeightGrid(tileManagerSelected.getTileHeight());

        StartUEditor2 frame = EditorCore.getMainFrame();
        if (startDrawingButton.getText().equals("Start Drawing")) {
            frame.getRightTabbedPanel().removeTabAt(0); // property
            frame.getRightTabbedPanel().removeTabAt(0); // palette (objects)

            TilePalettePanel tilePanel = new TilePalettePanel(tileManagerSelected.getTileSet());
            frame.getRightTabbedPanel().addTab("Palette", tilePanel);

            EditorCore.canSelect = false;
            EditorCore.canMoveObject = false;
            core.setSelected(null);

            tileDrawer = new TileDrawer(tileManagerSelected, tilePanel);
            firePropertyChange("notDrawing", true, false);
            startDrawingButton.setText("Stop Drawing");
        } else {
            if (tileDrawer != null) {
                tileDrawer.destroy();
                tileDrawer = null;
            }

            EditorCore.canSelect = true;
            EditorCore.canMoveObject = true;

            frame.getRightTabbedPanel().removeTabAt(0); // palette (tiles)
            frame.getRightTabbedPanel().addTab("Property", frame.getBeanEditor());
            frame.getRightTabbedPanel().addTab("Palette", frame.getPalette());
            startDrawingButton.setText("Start Drawing");
            firePropertyChange("notDrawing", false, true);
        }
}//GEN-LAST:event_startDrawingButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        refresh();
        System.out.println("REFRESHED");
    }//GEN-LAST:event_jButton3ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu LayerMapMenu;
    private javax.swing.JButton addLayerMapButton;
    private javax.swing.JButton addTiledMapButton;
    private javax.swing.JButton deleteTileMapButton;
    private javax.swing.JButton editTileMapButton;
    private javax.swing.JMenuItem existLayerMap;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox layerID;
    private javax.swing.JComboBox listTileMap;
    private javax.swing.JMenuItem loadLayerMap;
    private javax.swing.JMenuItem loadTiledMap;
    private javax.swing.JMenuItem newLayerMap;
    private javax.swing.JMenuItem newTiledMap;
    private javax.swing.JButton removeLayerButton;
    private javax.swing.JButton startDrawingButton;
    private javax.swing.JPopupMenu tileMapMenu;
    private javax.swing.JComboBox tileSetComboBox;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
