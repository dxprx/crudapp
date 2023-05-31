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
import java.util.regex.Pattern;

public class Validador {

    private static Validador instance = new Validador();

    private Validador() {

    }

    public static Validador getInstance() {
        return instance;
    }

    public static boolean validarTelefono(String telefono) {
        String regex = "\\d{9}";
        return Pattern.matches(regex, telefono);
    }

    public static boolean validarCodigoPostal(String codigoPostal) {
        String regex = "\\d{5}";
        return Pattern.matches(regex, codigoPostal);
    }

    public static boolean validarCorreoElectronico(String correoElectronico) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, correoElectronico);
    }

    public static boolean validarNumeroPositivo(String numero) {
        String regex = "\\d+(\\.\\d+)?";
        return Pattern.matches(regex, numero) && Float.parseFloat(numero) > 0;
    }

    public boolean validarPaginaWeb(String paginaWeb) {
        // Expresión regular para validar URLs de páginas web
        String patron = "^(http://|https://)?(www\\.)?[a-zA-Z0-9]+(\\.[a-zA-Z]{2,}){1,3}(/\\S*)?$";
        return Pattern.matches(patron, paginaWeb);
    }

    public boolean validarDNI(String dni) {
        // Expresión regular para validar el formato del DNI (8 dígitos seguidos de una letra)
        String regex = "\\d{8}[A-Za-z]";

        return dni.matches(regex);
    }

    public boolean noEstaVacio(String texto) {
        // Expresión regular que verifica que el string no esté vacío
        String regex = "^(?!\\s*$).+";

        return texto.matches(regex);
    }
}
