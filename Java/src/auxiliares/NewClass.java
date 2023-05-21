/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliares;

/**
 *
 * @author oscar
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;

public class NewClass {
    private JFrame frame;
    private JTable tablaProductos;
    private DefaultTableModel dtmProductos;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NewClass();
            }
        });
    }

    public NewClass() {
        frame = new JFrame("Ejemplo de tabla");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dtmProductos = new DefaultTableModel();
        dtmProductos.addColumn("Producto");
        dtmProductos.addColumn("Precio");
        dtmProductos.addColumn("Eliminado");

        tablaProductos = new JTable(dtmProductos);
        tablaProductos.setDefaultRenderer(Object.class, new ColorRenderer());

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tablaProductos.getSelectedRow();
                if (selectedRow != -1) {
                    dtmProductos.setValueAt(true, selectedRow, 2);
                    tablaProductos.repaint();
                }
            }
        });
        frame.getContentPane().add(btnEliminar, BorderLayout.SOUTH);

        frame.setSize(400, 300);
        frame.setVisible(true);

        // Filas de ejemplo
        dtmProductos.addRow(new Object[]{"Producto 1", 10.0, false});
        dtmProductos.addRow(new Object[]{"Producto 2", 15.0, false});
        dtmProductos.addRow(new Object[]{"Producto 3", 20.0, false});
    }

    private class ColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Verificar si la fila fue eliminada
            boolean eliminada = (Boolean) dtmProductos.getValueAt(row, dtmProductos.getColumnCount() - 1);

            // Establecer el color de fondo de la fila
            if (eliminada) {
                component.setBackground(Color.RED);
            } else {
                component.setBackground(table.getBackground());
            }

            return component;
        }
    }
}
