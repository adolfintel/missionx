/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClassSelectorPanel.java
 *
 * Created on 29-apr-2009, 18.34.50
 */
package org.easyway.ueditor2.forms;

import java.awt.Color;

/**
 *
 * @author Daniele
 */
public class ClassSelectorPanel extends javax.swing.JPanel {

    Class clazz;
    static Color defBgColor;
    static Color newBgColor = new Color(157, 206, 255);

    /** Creates new form ClassSelectorPanel */
    public ClassSelectorPanel(Class clazz) {
        this.clazz = clazz;
        initComponents();
        if (defBgColor == null) {
            defBgColor = getBackground();
        }
        Package pakkage = clazz.getPackage();
        packageLabel.setText("Package: " + pakkage.getName());
        classNameLabel.setText(clazz.getSimpleName().equals("") ? clazz.getName() : clazz.getSimpleName());
    }

    public Class getSelectedClass() {
        return classNameLabel.isSelected() ? clazz : null;
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

        classNameLabel = new javax.swing.JCheckBox();
        packageLabel = new javax.swing.JLabel();

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });

        classNameLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        classNameLabel.setText("className");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${background}"), classNameLabel, org.jdesktop.beansbinding.BeanProperty.create("background"));
        bindingGroup.addBinding(binding);

        classNameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                classNameLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                classNameLabelMouseExited(evt);
            }
        });

        packageLabel.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        packageLabel.setText("Package:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(classNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(packageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(classNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(packageLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        setBackground(newBgColor);
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        setBackground(defBgColor);
    }//GEN-LAST:event_formMouseExited

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        classNameLabel.setSelected(!classNameLabel.isSelected());
    }//GEN-LAST:event_formMouseClicked

    private void classNameLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_classNameLabelMouseEntered
        setBackground(newBgColor);
    }//GEN-LAST:event_classNameLabelMouseEntered

    private void classNameLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_classNameLabelMouseExited
        setBackground(defBgColor);
    }//GEN-LAST:event_classNameLabelMouseExited

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox classNameLabel;
    private javax.swing.JLabel packageLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
