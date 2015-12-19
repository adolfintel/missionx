/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Navigator.java
 *
 * Created on 3-apr-2009, 12.28.35
 */
package org.easyway.ueditor2.southPanels;

import org.easyway.ueditor2.commands.editor.ZoomCommand;
import org.easyway.objects.Camera;
import org.easyway.system.StaticRef;
import org.easyway.ueditor2.EditorCore;

/**
 *
 * @author Daniele
 */
public class Navigator extends javax.swing.JPanel {

    /** Creates new form Navigator */
    public Navigator() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        arrowButton1 = new org.easyway.ueditor2.forms.ArrowButton();
        arrowButton2 = new org.easyway.ueditor2.forms.ArrowButton();
        arrowButton3 = new org.easyway.ueditor2.forms.ArrowButton();
        arrowButton4 = new org.easyway.ueditor2.forms.ArrowButton();

        setMaximumSize(new java.awt.Dimension(84, 84));
        setMinimumSize(new java.awt.Dimension(84, 84));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/origin.GIF"))); // NOI18N
        jButton1.setToolTipText("reset to origin axis");
        jButton1.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton1.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton1.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/grid.GIF"))); // NOI18N
        jButton4.setToolTipText("reset to origin axis");
        jButton4.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton4.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton4.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/description.gif"))); // NOI18N
        jButton6.setToolTipText("reset to origin axis");
        jButton6.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton6.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton6.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/ZoomIn.GIF"))); // NOI18N
        jButton8.setToolTipText("reset to origin axis");
        jButton8.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton8.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton8.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/ZoomOut.GIF"))); // NOI18N
        jButton10.setToolTipText("reset to origin axis");
        jButton10.setMaximumSize(new java.awt.Dimension(24, 24));
        jButton10.setMinimumSize(new java.awt.Dimension(24, 24));
        jButton10.setPreferredSize(new java.awt.Dimension(24, 24));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        arrowButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/arrowUp.GIF"))); // NOI18N
        arrowButton1.setMaximumSize(new java.awt.Dimension(24, 24));
        arrowButton1.setMinimumSize(new java.awt.Dimension(24, 24));
        arrowButton1.setPreferredSize(new java.awt.Dimension(24, 24));
        arrowButton1.setYspeed(-8.0);

        arrowButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/arrowRight.GIF"))); // NOI18N
        arrowButton2.setMaximumSize(new java.awt.Dimension(24, 24));
        arrowButton2.setMinimumSize(new java.awt.Dimension(24, 24));
        arrowButton2.setPreferredSize(new java.awt.Dimension(24, 24));
        arrowButton2.setXspeed(8.0);

        arrowButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/arrowDown.GIF"))); // NOI18N
        arrowButton3.setMaximumSize(new java.awt.Dimension(24, 24));
        arrowButton3.setMinimumSize(new java.awt.Dimension(24, 24));
        arrowButton3.setPreferredSize(new java.awt.Dimension(24, 24));
        arrowButton3.setYspeed(8.0);

        arrowButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/easyway/ueditor2/images/arrowLeft.GIF"))); // NOI18N
        arrowButton4.setMaximumSize(new java.awt.Dimension(24, 24));
        arrowButton4.setMinimumSize(new java.awt.Dimension(24, 24));
        arrowButton4.setPreferredSize(new java.awt.Dimension(24, 24));
        arrowButton4.setXspeed(-8.0);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(arrowButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(arrowButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(arrowButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(arrowButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(arrowButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(arrowButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(arrowButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(arrowButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        final Camera camera = StaticRef.getCamera();
        camera.setx(0.0f);
        camera.sety(0.0f);
        new ZoomCommand(1.0f, false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        EditorCore.drawGrid = !EditorCore.drawGrid;
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        new CameraPropertiesDialog(EditorCore.getMainFrame(), true).setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        new ZoomCommand(-0.1f);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new ZoomCommand(0.1f);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void arrowButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arrowButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.easyway.ueditor2.forms.ArrowButton arrowButton1;
    private org.easyway.ueditor2.forms.ArrowButton arrowButton2;
    private org.easyway.ueditor2.forms.ArrowButton arrowButton3;
    private org.easyway.ueditor2.forms.ArrowButton arrowButton4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    // End of variables declaration//GEN-END:variables
}