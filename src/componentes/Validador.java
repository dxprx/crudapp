/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

import java.util.regex.*;
import javax.swing.JOptionPane;

/**
 *
 * @author DAW
 */
public class Validador {

    private static Validador instance = new Validador();

    private Validador() {

    }
    
    private String error = "Error de validación";
    
    public static Validador getInstance() {
        return instance;
    }
    
    public boolean validarNumerico(String dato){
        Pattern patron = Pattern.compile("[0-9]*");
        Matcher match = patron.matcher(dato);
        if(!match.find()){
        JOptionPane.showMessageDialog(null, "El dato introducido no es un número.", error, JOptionPane.ERROR_MESSAGE);
        return false;
        }
        return true;
    }
}
