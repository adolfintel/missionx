/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ActionsVEditor.java
 *
 * Created on 11-gen-2010, 9.45.43
 */
package org.easyway.vectorialEditor;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author RemalKoil
 */
public class ActionsVEditor extends javax.swing.JPanel {

    /** Creates new form ActionsVEditor */
    public ActionsVEditor() {

        initComponents();

        points.setModel(new javax.swing.AbstractListModel() {

            @Override
            public int getSize() {
                return Data.points.size();
            }

            @Override
            public Object getElementAt(int i) {
                return Data.points.get(i);
            }
        });

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        points = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        ySpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        xSpinner = new javax.swing.JSpinner();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        points.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                pointsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(points);

        jLabel1.setText("Y:");

        ySpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        jLabel2.setText("X:");

        xSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));

        addButton.setText("Add");

        removeButton.setText("Remove");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ySpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeButton)
                        .addContainerGap())
                    .addComponent(xSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(removeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(xSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void updateSpinners() {
        int x = (int)Data.selectedPoint.x;
        int y = (int)Data.selectedPoint.y;
        xSpinner.setValue(x);
        ySpinner.setValue(y);
    }

    private void pointsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_pointsValueChanged
        Data.selectedPoint = (Vector3f) points.getSelectedValue();
        VectorialEditorMain.previewVEditor1.repaint();
        updateSpinners();
    }//GEN-LAST:event_pointsValueChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList points;
    private javax.swing.JButton removeButton;
    private javax.swing.JSpinner xSpinner;
    private javax.swing.JSpinner ySpinner;
    // End of variables declaration//GEN-END:variables

    void update() {
        points.updateUI();
    }
}
