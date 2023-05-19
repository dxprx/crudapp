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
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class NewClass {

    public static void main(String[] args) {
        JFrame frame = new JFrame("ComboBox TableCell Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear datos de ejemplo para el modelo de tabla
        Object[][] data = {
            {1, "Opción 1"},
            {2, "Opción 2"},
            {3, "Opción 3"}
        };
        String[] columnNames = {"ID", "Opciones"};

        // Crear el modelo de tabla con los datos de ejemplo
        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        // Crear el JTable con el modelo de tabla
        JTable table = new JTable(model);

        // Crear el JComboBox con las opciones
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Opción 1");
        comboBox.addItem("Opción 2");
        comboBox.addItem("Opción 3");

        // Crear el editor de celda con el JComboBox
        DefaultCellEditor cellEditor = new DefaultCellEditor(comboBox);

        // Obtener la columna en la que deseas que aparezca el combobox
        TableColumn comboBoxColumn = table.getColumnModel().getColumn(1);

        // Establecer el editor de celda para la columna
        comboBoxColumn.setCellEditor(cellEditor);

        // Agregar la tabla a un JScrollPane y mostrarlo en el JFrame
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }
}
