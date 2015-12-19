/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.rightPanels;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import org.easyway.ueditor2.effects.Effect;
import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.texture.Texture;
import org.easyway.ueditor2.TextureManagerDialog;

public class BeanEditor extends JPanel implements PropertyChangeListener {

    protected Object beanObject;
    protected JTable table;
    protected PropertyTableData data;

    public BeanEditor() {
        this(null);
    }

    public BeanEditor(Object bean) {
        beanObject = bean;
        setBounds(0, 0, 400, 300);
        setLayout(new BorderLayout());
        data = new PropertyTableData(beanObject);
        table = new JTable(data);

        TableCellRenderer defaultRenderer = table.getDefaultRenderer(JButton.class);
        table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(defaultRenderer));

        TableCellRenderer labelDefaultRenderer = table.getDefaultRenderer(Property.class);
        table.setDefaultRenderer(Property.class, new JTableReadOnlyRenderer(labelDefaultRenderer));
        table.addMouseListener(new JTableButtonMouseListener(table));

        JScrollPane ps = new JScrollPane();
        ps.getViewport().add(table);
        add(ps, BorderLayout.CENTER);

        setVisible(true);

    }

    public synchronized void setBean(final Object bean) {
        if (bean != null) {
            beanObject = bean;
            data = new PropertyTableData(beanObject);
            table.setModel(data);
        } else {
            table.setModel(new DefaultTableModel());
        }
    }

    public synchronized void updateBeanInfo() {
        final Object bObject = beanObject;
        if (bObject == null || data == null || data.list == null) {
            return;
        }
        //setBean(beanObject);
        for (int index = 0; index < data.list.size(); ++index) {
            try {
                PropertyDescriptor pd = data.list.get(index);
                Method method = pd.getReadMethod();
                if (method == null || method.getParameterTypes().length != 0) {
                    continue;
                }
                Object result = method.invoke(bObject, (Object[]) null);
                Object obj1 = data.properties[index][1];
                //if ((obj1 != null && result != null && result.equals(obj1)) ||
                // (((obj1 != null && result == null) || (obj1 == null && result != null)) && obj1 != result)) {
                if (obj1 != result) {
                    data.properties[index][1] = data.objToString(result);
                    table.tableChanged(new TableModelEvent(data, index));
                }

            } catch (IllegalAccessException ex) {
                Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                if (!data.properties[index][1].equals("error")) {
                    Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        table.repaint();
        repaint();
    }

    public synchronized void propertyChange(PropertyChangeEvent evt) {
        data.setProperty(evt.getPropertyName(), evt.getNewValue());
    }
    WindowListener updateOnCloseListener = new WindowAdapter() {

        @Override
        public void windowClosed(WindowEvent e) {
            updateBeanInfo();
        }
    };

    class PropertyDialogEditorMouseListener extends MouseAdapter {

        PropertyDescriptor prop;

        PropertyDialogEditorMouseListener(PropertyDescriptor prop) {
            this.prop = prop;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                Object obj = prop.getPropertyEditorClass().newInstance();
                if (obj instanceof Customizer) {
                    ((Customizer) obj).setObject(beanObject);
                }
                //Set up the dialog that the button brings up.
                if (obj instanceof Component) {
                    JDialog dialog = new JDialog((Frame) null, "PropertyDialog", true);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.add((Component) obj);
                    dialog.setPreferredSize(((Component) obj).getPreferredSize());
                    if (dialog.getSize().getHeight() < 100) {
                        dialog.setSize(600, 400);
                    }
                    dialog.setVisible(true);
                    dialog.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosed(WindowEvent e) {
                            super.windowClosed(e);

                        }
                    });
                }
            } catch (InstantiationException ex) {
                Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class PropertyTableData extends AbstractTableModel {

        protected boolean[] writables;
        protected Object[][] properties;
        protected int numProps = 0;
        protected Vector<PropertyDescriptor> list;

        public PropertyTableData(final Object bean) {

            if (bean == null) {
                return;
            }
            try {
                Introspector.flushCaches();
                BeanInfo info = Introspector.getBeanInfo(bean.getClass());

                BeanDescriptor descr = info.getBeanDescriptor();
                System.out.println("Selected: " + descr.getBeanClass());
                PropertyDescriptor[] props = info.getPropertyDescriptors();
                numProps = props.length;
                list = new Vector<PropertyDescriptor>(numProps);

                for (int index = 0; index < numProps; index++) {

                    String name = props[index].getDisplayName();
                    boolean added = false;

                    // sort names
                    for (int i = 0; i < list.size(); i++) {
                        String str = ((PropertyDescriptor) list.elementAt(i)).getDisplayName();

                        if (name.compareToIgnoreCase(str) < 0) {
                            list.insertElementAt(props[index], i);
                            added = true;
                            break;
                        }
                    }

                    // add at the end
                    if (!added) {
                        list.addElement(props[index]);
                    }

                }

                properties = new Object[numProps][3];
                writables = new boolean[numProps];
                for (int index = 0; index < numProps; index++) {
                    final PropertyDescriptor prop = list.elementAt(index);

                    //System.out.println("-      Prop: " + prop.getDisplayName() + " in " + bean.getClass());

                    Method mRead = prop.getReadMethod();
                    boolean isReadOnly = !(writables[index] = (prop.getWriteMethod() != null));
                    properties[index][0] = new Property(null, prop.getDisplayName(), isReadOnly);
                    Object value = null;
                    if (mRead != null && mRead.getParameterTypes().length == 0) {
                        try {
                            value = mRead.invoke(beanObject, (Object[]) null);
                            properties[index][1] = objToString(value);
                        } catch (InvocationTargetException e) {
                            properties[index][1] = "error";
                        }
                    } else {
                        properties[index][1] = "error";
                    }
                    if (prop.getPropertyEditorClass() != null) {
                        final JButton btn = new JButton("...");
                        btn.addMouseListener(new PropertyDialogEditorMouseListener(prop));
                        properties[index][2] = btn;

                    } else {
                        try {//(value instanceof ITexture) {
                            if (prop.getPropertyType() == null) {
                                continue;
                            }
                            // try to cast to ITexture
                            // if isn't an instance of ITexture throw a ClassCastException
                            prop.getPropertyType().asSubclass(ITexture.class);
                            final JButton btn = new JButton("...");
                            btn.addMouseListener(new MouseAdapter() {

                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    final TextureManagerDialog dialog = new TextureManagerDialog(true);
                                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                                    dialog.addWindowListener(new WindowAdapter() {

                                        @Override
                                        public void windowClosed(WindowEvent e) {

                                            ITexture texture = dialog.getSelectedTexture();
                                            if (texture != null) {
                                                try {
                                                    prop.getWriteMethod().invoke(bean, texture);
                                                } catch (IllegalAccessException ex) {
                                                    Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (IllegalArgumentException ex) {
                                                    Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (InvocationTargetException ex) {
                                                    Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                            updateBeanInfo();
                                            super.windowClosed(e);
                                        }
                                    });
                                    dialog.setVisible(true);
                                }
                            });
                            properties[index][2] = btn;
                        } catch (ClassCastException e) {
                        }
                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
                /*JOptionPane.showMessageDialog(
                BeanEditor.this, "Error: " + ex.toString(),
                "Warning", JOptionPane.WARNING_MESSAGE);*/
            }

        }

        public void setProperty(String name, Object value) {
            for (int index = 0; index < numProps; index++) {
                if (name.equals(properties[index][0])) {
                    properties[index][1] = objToString(value);
                    table.tableChanged(new TableModelEvent(this, index));
                    table.repaint();
                    break;
                }
            }

        }

        public int getRowCount() {
            return numProps;
        }

        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int nCol) {
            switch (nCol) {
                case 0:
                    return "Property";
                case 1:
                    return "Value";
                case 2:
                    return "...";
            }
            return "error";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Property.class;
                case 1:
                    return super.getColumnClass(columnIndex);
                case 2:
                    return JButton.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int nRow, int nCol) {
            return (nCol == 1) && (writables[nRow]);
        }

        public Object getValueAt(int nRow, int nCol) {
            return properties[nRow][nCol];
            /*if (nRow < 0 || nRow >= getRowCount()) {
            return "";
            }*/
            /*switch (nCol) {
            case 0:
            return properties[nRow][0];
            case 1:
            return properties[nRow][1];
            case 2:
            return properties[nRow][2];
            }*/
            //return "";
        }

        @Override
        public void setValueAt(Object value, int nRow, int nCol) {
            if (nRow < 0 || nRow >= getRowCount()) {
                return;
            }

            String str = value.toString();
            PropertyDescriptor prop = (PropertyDescriptor) list.elementAt(nRow);
            Class cls = prop.getPropertyType();
            Object obj = stringToObj(str, cls);

            if (obj == null) {
                return;        // can't process
            }
            Method mWrite = prop.getWriteMethod();
            if (mWrite == null || mWrite.getParameterTypes().length != 1) {
                return;
            }

            try {
                mWrite.invoke(beanObject, new Object[]{obj});
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        BeanEditor.this, "Error: " + ex.toString(),
                        "Warning", JOptionPane.WARNING_MESSAGE);

            }
            properties[nRow][1] = str;

        }

        public String objToString(Object value) {
            if (value == null) {
                return "null";
            }

            if (value instanceof Dimension) {
                Dimension dim = (Dimension) value;
                return "" + dim.width + "," + dim.height;
            } else if (value instanceof Insets) {
                Insets ins = (Insets) value;
                return "" + ins.left + "," + ins.top + "," + ins.right + "," + ins.bottom;
            } else if (value instanceof Rectangle) {
                Rectangle rc = (Rectangle) value;
                return "" + rc.x + "," + rc.y + "," + rc.width + "," + rc.height;
            } else if (value instanceof Color) {
                Color col = (Color) value;
                return "" + col.getRed() + "," + col.getGreen() + "," + col.getBlue();
            }

            return value.toString();
        }

        public Object stringToObj(final String str, Class cls) {
            try {
                if (str == null) {
                    return null;
                }

                String name = cls.getName();

                if (name.equals("java.lang.String")) {
                    return str;
                } else if (name.equals("int")) {
                    return new Integer(str);
                } else if (name.equals("long")) {
                    return new Long(str);
                } else if (name.equals("float")) {
                    return new Float(str);
                } else if (name.equals("double")) {
                    return new Double(str);
                } else if (name.equals("boolean")) {
                    return new Boolean(str);
                } else if (name.equals("java.awt.Dimension")) {
                    int[] i = strToInts(str);
                    return new Dimension(i[0], i[1]);
                } else if (name.equals("java.awt.Insets")) {
                    int[] i = strToInts(str);
                    return new Insets(i[0], i[1], i[2], i[3]);
                } else if (name.equals("java.awt.Rectangle")) {
                    int[] i = strToInts(str);
                    return new Rectangle(i[0], i[1], i[2], i[3]);
                } else if (name.equals("java.awt.Color")) {
                    int[] i = strToInts(str);
                    return new Color(i[0], i[1], i[2]);
                } else if (name.equals(ITexture.class.getName()) || name.equals(Texture.class.getName())) {

                    Effect effect = new Effect() {

                        @Override
                        public void create() {
                            Texture.getTexture(str);
                            destroy();
                        }
                    };
                    while (!effect.isDestroyed()) {
                        Thread.yield();
                    }
                    return Texture.getTexture(str);
                }

                return null;    // not supported

            } catch (Exception ex) {
                return null;
            }

        }

        public int[] strToInts(String str) throws Exception {
            int[] i = new int[4];
            StringTokenizer tokenizer = new StringTokenizer(str, ",");
            for (int k = 0; k < i.length &&
                    tokenizer.hasMoreTokens(); k++) {
                i[k] = Integer.parseInt(tokenizer.nextToken());
            }
            return i;

        }
    }

    class JTableButtonMouseListener implements MouseListener {

        private JTable table;

        private void forwardEventToButton(MouseEvent e) {
            TableColumnModel columnModel = table.getColumnModel();
            int column = columnModel.getColumnIndexAtX(e.getX());
            int row = e.getY() / table.getRowHeight();
            Object value;
            JButton button;
            MouseEvent buttonEvent;

            if (row >= table.getRowCount() || row < 0 ||
                    column >= table.getColumnCount() || column < 0) {
                return;
            }

            value = table.getValueAt(row, column);
            if (value instanceof JButton) {
                button = (JButton) value;
                buttonEvent = (MouseEvent) SwingUtilities.convertMouseEvent(table, e, button);
                button.dispatchEvent(buttonEvent);
                // This is necessary so that when a button is pressed and released
                // it gets rendered properly.  Otherwise, the button may still appear
                // pressed down when it has been released.
                button.repaint();
                table.repaint();
                button.repaint();
            }

        }

        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            forwardEventToButton(e);
        }

        public void mouseEntered(MouseEvent e) {
            forwardEventToButton(e);
        }

        public void mouseExited(MouseEvent e) {
            forwardEventToButton(e);
        }

        public void mousePressed(MouseEvent e) {
            forwardEventToButton(e);
        }

        public void mouseReleased(MouseEvent e) {
            forwardEventToButton(e);
        }
    }

    class JTableReadOnlyRenderer implements TableCellRenderer {

        private TableCellRenderer defaultRenderer;

        public JTableReadOnlyRenderer(TableCellRenderer defaultRenderer) {
            this.defaultRenderer = defaultRenderer;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Property) {
                Property p = (Property) value;
                if (p.isReadOnly()) {

                    JLabel label = new JLabel(p.getPropName());
                    label.setForeground(Color.RED);
                    return label;
                }

            }
            if (value instanceof Component) {
                return (Component) value;
            }
            return defaultRenderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
        }
    }

    class JTableButtonRenderer implements TableCellRenderer {

        private TableCellRenderer defaultRenderer;

        public JTableButtonRenderer(TableCellRenderer defaultRenderer) {
            this.defaultRenderer = defaultRenderer;
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected,
                boolean hasFocus,
                int row, int column) {
            if (value instanceof Component) {
                return (Component) value;
            }
            return defaultRenderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
        }
    }
}
