/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DAW
 */
public class NonEditableModel extends DefaultTableModel {

    public NonEditableModel(Object[][] data, String[] columnNames) {
        
    }

    public NonEditableModel() {
        super();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
