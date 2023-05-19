/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.awt.Color;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import componentes.BCrypt;
import componentes.Validador;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author DAW
 */
public class Conexion {

    private final String db = "jdbc:mysql://localhost:3306/crud_app";
    private final String db_user = "ogp";
    private final String db_password = "ogp";

    private static Conexion instance = new Conexion();

    private Conexion() {
    }

    public static Conexion getInstance() {
        return instance;
    }

    public boolean InciarSesion(JPasswordField jtf, JLabel label, String usuario, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);

            PreparedStatement statement = connection.prepareStatement("select * from usuarios where usuario = ?");
            statement.setString(1, usuario);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String user = resultSet.getString("usuario");
                String pass = resultSet.getString("pass");
                if (usuario.equals(user) && BCrypt.checkpw(password, pass)) {
                    System.out.println("Exito");
                    jtf.setBackground(Color.WHITE);
                    label.setVisible(false);
                    resultSet.close();
                    statement.close();
                    connection.close();
                    return true;
                } else {
                    jtf.setBackground(Color.red);
                    label.setVisible(true);
                    resultSet.close();
                    statement.close();
                    connection.close();
                    return false;
                }

            } else {
                jtf.setBackground(Color.red);
                label.setVisible(true);
                resultSet.close();
                statement.close();
                connection.close();
                return false;
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
        return false;
    }

    public boolean ComprobarRoleUsuario(String usuario) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);

            PreparedStatement statement = connection.prepareStatement("select * from usuarios where usuario = ?");
            statement.setString(1, usuario);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                return role.equals("admin");
            } else {
                String role = resultSet.getString("role");
                return role.equals("admin");
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
        return false;
    }

    public void UpdatePassword(String usuario, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);

            String generatedSecuredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt());

            PreparedStatement statement = connection.prepareStatement("update usuarios set pass = ? where usuario = ?");
            statement.setString(1, generatedSecuredPasswordHash);
            statement.setString(2, usuario);

            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void MostrarTablaProductos(DefaultTableModel tabla) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from productos inner join proveedores on productos.proveedor=proveedores.idproveedor")) {

                while (resultSet.next()) {
                    String datos[] = new String[7];
                    datos[0] = String.valueOf(resultSet.getInt("idproductos"));
                    datos[1] = resultSet.getString("nombreproducto");
                    datos[2] = resultSet.getString("lineaproducto");
                    datos[3] = resultSet.getString("descripcion");
                    datos[4] = String.valueOf(resultSet.getInt("cantidadEnStock"));
                    datos[5] = String.valueOf(resultSet.getFloat("pvp"));
                    datos[6] = resultSet.getString("nombreEmpresa");
                    tabla.addRow(datos);
                }
                resultSet.close();
                statement.close();
                connection.close();

            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void MostrarTablaLineaProductos(DefaultTableModel tabla) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from lineasproducto")) {

                while (resultSet.next()) {
                    String datos[] = new String[2];
                    datos[0] = resultSet.getString("lineaproducto");
                    datos[1] = resultSet.getString("descripcion");
                    tabla.addRow(datos);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void MostrarTablaProveedores(DefaultTableModel tabla) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from proveedores")) {

                while (resultSet.next()) {
                    String datos[] = new String[6];
                    datos[0] = String.valueOf(resultSet.getInt("idproveedor"));
                    datos[1] = resultSet.getString("nombreEmpresa");
                    datos[2] = resultSet.getString("nombreContacto");
                    datos[3] = String.valueOf(resultSet.getInt("ciudad"));
                    datos[4] = resultSet.getString("telefono");
                    datos[5] = resultSet.getString("paginaweb");
                    tabla.addRow(datos);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void MostrarTablaPedidos(DefaultTableModel tabla) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from pedidos")) {

                while (resultSet.next()) {
                    String datos[] = new String[7];
                    datos[0] = String.valueOf(resultSet.getInt("idpedidos"));
                    datos[1] = String.valueOf(resultSet.getInt("idcliente"));
                    datos[2] = String.valueOf(resultSet.getInt("idempleados"));
                    datos[3] = resultSet.getDate("fechaPedido").toString();
                    datos[4] = resultSet.getString("direccionPedido");
                    datos[5] = resultSet.getString("codigopostalPedido");
                    datos[6] = String.valueOf(resultSet.getInt("idempresaEnvio"));
                    tabla.addRow(datos);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void MostrarTablaEmpleados(DefaultTableModel tabla) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select idempleados,nombre,concat(apellido1,' ',apellido2) as apellidos,DNI,telefono,email,usuario,role from empleados inner join usuarios using(idusuario)")) {

                while (resultSet.next()) {
                    String datos[] = new String[8];
                    datos[0] = String.valueOf(resultSet.getInt("idempleados"));
                    datos[1] = resultSet.getString("nombre");
                    datos[2] = resultSet.getString("apellidos");
                    datos[3] = resultSet.getString("DNI");
                    datos[4] = resultSet.getString("telefono");
                    datos[5] = resultSet.getString("email");
                    datos[6] = resultSet.getString("usuario");
                    datos[7] = resultSet.getString("role");
                    tabla.addRow(datos);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void MostrarTablaEmpresasEnvio(DefaultTableModel tabla) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from empresasenvio")) {

                while (resultSet.next()) {
                    String datos[] = new String[4];
                    datos[0] = String.valueOf(resultSet.getInt("idempresasEnvio"));
                    datos[1] = resultSet.getString("nombre");
                    datos[2] = resultSet.getString("telefono");
                    datos[3] = resultSet.getString("email");
                    tabla.addRow(datos);
                }
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void EliminarProducto(int id) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);
            PreparedStatement statement = connection.prepareStatement("delete from productos where idproductos = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto eliminado con exito.", "Exito", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException exception) {
            System.out.println("Error:SQL" + exception);
        }
    }

    public void EliminarLineaProducto(String id) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);
            PreparedStatement statement = connection.prepareStatement("delete from lineasproducto where lineaproducto = ?");
            statement.setString(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Linea de producto eliminada con exito.", "Exito", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException exception) {
            System.out.println("Error:SQL" + exception);
        }
    }

    public void EliminarProveedor(int id) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);
            PreparedStatement statement = connection.prepareStatement("delete from proveedores where idproveedor = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito.", "Exito", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException exception) {
            System.out.println("Error:SQL" + exception);
        }
    }

    public void EliminarPedido(int id) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);
            PreparedStatement statement = connection.prepareStatement("delete from pedidos where idpedidos = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Pedido eliminado con exito.", "Exito", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException exception) {
            System.out.println("Error:SQL" + exception);
        }
    }

    public void EliminarEmpleado(int id) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);
            PreparedStatement statement = connection.prepareStatement("delete from empleados where idempleados = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empleado eliminado con exito.", "Exito", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException exception) {
            System.out.println("Error:SQL" + exception);
        }
    }

    public void EliminarEmpresaEnvio(int id) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);
            PreparedStatement statement = connection.prepareStatement("delete from empresasenvio where idempresasEnvio = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empresa de envio eliminada con exito.", "Exito", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException exception) {
            System.out.println("Error:SQL" + exception);
        }
    }

    public void BuscarPorTabla(int indice, String[] parametros) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);

            Validador validador = Validador.getInstance();

            switch (indice) {
                case 1: {
                    try {
                        String estamento = "select * from productos inner join proveedores using(idproveedor) where ";
                        if (!parametros[0].equals("")) {
                            estamento = estamento + "nombreproducto = ? and ";
                        }
                        if (!parametros[1].equals("")) {
                            estamento = estamento + "lineaproducto = ? and ";
                        }
                        if (validador.validarNumerico(parametros[2])) {
                            estamento = estamento + "cantidadEnStock > ? and ";
                        }
                        if (validador.validarNumerico(parametros[3])) {
                            estamento = estamento + "cantidadEnStock < ? and ";
                        }
                        if (validador.validarNumerico(parametros[4])) {
                            estamento = estamento + "pvp > ? and ";
                        }
                        if (validador.validarNumerico(parametros[5])) {
                            estamento = estamento + "pvp < ? and ";
                        }
                        if (!parametros[6].equals("")) {
                            estamento = estamento + "nombreEmpresa = ? and ";
                        }

                        estamento = estamento.substring(0, estamento.length() - 4);
                        PreparedStatement statement = connection.prepareStatement(estamento);

                        if (!parametros[0].equals("")) {
                            statement.setString(1, parametros[0]); // nombreProducto
                        }
                        if (!parametros[1].equals("")) {
                            statement.setString(2, parametros[1]); // lineaproducto
                        }
                        if (validador.validarNumerico(parametros[2])) {
                            statement.setInt(3, Integer.valueOf(parametros[2])); // cantidadEnStock min
                        }
                        if (validador.validarNumerico(parametros[3])) {
                            statement.setInt(4, Integer.valueOf(parametros[3])); // cantidadEnStock max
                        }
                        if (validador.validarNumerico(parametros[4])) {
                            statement.setInt(5, Integer.valueOf(parametros[4])); // pvp min
                        }
                        if (validador.validarNumerico(parametros[5])) {
                            statement.setInt(6, Integer.valueOf(parametros[5])); // pvp max
                        }
                        if (!parametros[6].equals("")) {
                            statement.setString(7, parametros[6]); // nombreEmpresa
                        }

                        ResultSet resultSet = statement.executeQuery();
                        while (resultSet.next()) {
                            String datos[] = new String[6];
                            datos[0] = resultSet.getString("nombreproducto");
                            datos[1] = resultSet.getString("lineaproducto");
                            datos[2] = resultSet.getString("descripcion");
                            datos[3] = String.valueOf(resultSet.getInt("cantidadEnStock"));
                            datos[4] = String.valueOf(resultSet.getFloat("pvp"));
                            datos[5] = resultSet.getString("proveedor");
                            for (int i = 0; i < datos.length; i++) {
                                System.out.println(datos[i]);
                            }
                        }
                        resultSet.close();
                        statement.close();
                        connection.close();
                    } catch (SQLException exception) {

                    }
                    break;
                }
                case 2: {

                    break;
                }
                case 3: {

                    break;
                }
                case 4: {

                    break;
                }
            }

        } catch (ClassNotFoundException | SQLException exception) {

        }
    }

    public void MostrarComboBoxLineaProducto(DefaultComboBoxModel modelo) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from lineasproducto")) {
                ArrayList al = new ArrayList();
                al.add("Linea de producto");
                while (resultSet.next()) {

                    String datos = resultSet.getString("lineaproducto");
                    al.add(datos);

                }
                modelo.addAll(al);
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void MostrarComboBoxProveedores(DefaultComboBoxModel modelo) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from proveedores")) {
                ArrayList al = new ArrayList();
                al.add("Proveedor");
                while (resultSet.next()) {

                    String datos = resultSet.getString("nombreEmpresa");
                    al.add(datos);

                }
                modelo.addAll(al);
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void MostrarComboBoxCiudades(DefaultComboBoxModel modelo) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(db, db_user, db_password) // below two lines are used for connectivity.
                    ;
                     Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from ciudades")) {
                ArrayList al = new ArrayList();
                al.add("Ciudad");
                while (resultSet.next()) {

                    String datos = resultSet.getString("nombreCiudad");
                    al.add(datos);

                }
                modelo.addAll(al);
                resultSet.close();
                statement.close();
                connection.close();
            }

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }

    public void ModificarTupla(String[] datos) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);

            PreparedStatement statement = connection.prepareStatement("UPDATE Prod SET `nombreproducto` = ?,`lineaproducto` = ?,`descripcion` = ?,`cantidadEnStock` = ?,`pvp` = ?,`nombreEmpresa` = ? FROM `crud_app`.`productos` AS Prod INNER JOIN `crud_app`.`proveedores` AS Prov ON Prod.proveedor = Prov.idproveedor WHERE `idproductos` = ?");
            statement.setString(1, datos[1]);
            statement.setString(2, datos[2]);
            statement.setString(3, datos[3]);
            statement.setInt(4, Integer.parseInt(datos[4]));
            statement.setBigDecimal(5, BigDecimal.valueOf(Double.parseDouble(datos[5])));
            statement.setString(6, datos[5]);
            statement.setInt(7, Integer.parseInt(datos[0]));
            
            statement.executeUpdate();

        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
        }
    }
}
