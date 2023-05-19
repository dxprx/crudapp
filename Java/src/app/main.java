/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import clases.*;
import com.sun.jdi.connect.spi.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import mysql.Conexion;

/**
 *
 * @author oscar
 */
public class main extends javax.swing.JFrame {

    private static Conexion conexion = Conexion.getInstance();
    private List<Producto> productos_data = new ArrayList<>();
    private List<LineaProducto> lineasproductos_data = new ArrayList<>();
    private List<Proveedor> proveedores_data = new ArrayList<>();
    private List<Cliente> clientes_data = new ArrayList<>();
    private List<Empleado> empleados_data = new ArrayList<>();
    private List<DetallesPedido> detallespedidos_data = new ArrayList<>();
    private List<Pedido> pedidos_data = new ArrayList<>();
    private List<Ciudad> ciudades_data = new ArrayList<>();

    private DefaultTableModel dtm_productos = new DefaultTableModel();
    private DefaultTableModel dtm_pedidos = new DefaultTableModel();
    private DefaultTableModel dtm_detallespedido = new DefaultTableModel();
    private DefaultTableModel dtm_empleados = new DefaultTableModel();
    private DefaultTableModel dtm_clientes = new DefaultTableModel();
    private DefaultTableModel dtm_proveedores = new DefaultTableModel();
    private DefaultTableModel dtm_lineasproductos = new DefaultTableModel();

    /**
     * Creates new form main
     *
     * @throws java.sql.SQLException
     */
    public main() throws SQLException {
        initComponents();
        mostrarTablas();
        columnasComboBox();
    }

    private void mostrarTablas() {
        mostrarProductos();
        mostrarProveedores();
        mostrarLineasProductos();
        mostrarClientes();
        mostrarEmpleados();
        mostrarPedidos();

        getCiudades();
    }

    private void columnasComboBox() {
        JComboBox<String> comboBox_proveedores = new JComboBox<>();

        for (Proveedor proveedor : proveedores_data) {
            comboBox_proveedores.addItem(proveedor.getNombreEmpresa());
        }

        DefaultCellEditor cellEditor_proveedores = new DefaultCellEditor(comboBox_proveedores);
        TableColumn comboBoxColumn_proveedores = tabla_productos.getColumn("Proveedor");
        comboBoxColumn_proveedores.setCellEditor(cellEditor_proveedores);

        JComboBox<String> comboBox_ciudades = new JComboBox<>();

        for (Ciudad ciudad : ciudades_data) {
            comboBox_ciudades.addItem(ciudad.getNombreCiudad());
        }

        DefaultCellEditor cellEditor_ciudades = new DefaultCellEditor(comboBox_ciudades);
        TableColumn comboBoxColumn_ciudades = tabla_proveedores.getColumn("Ciudad");
        comboBoxColumn_ciudades.setCellEditor(cellEditor_ciudades);

    }

    private void getCiudades() {
        try {
            conexion.conectar();
            ciudades_data = conexion.seleccionarCiudades();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void mostrarProductos() {

        String titulos_productos[] = {"ID", "Productos", "Linea de producto", "Descripcion", "En stock", "PVP", "Proveedor"};
        dtm_productos.setColumnIdentifiers(titulos_productos);
        tabla_productos.setModel(dtm_productos);

        try {
            conexion.conectar();
            productos_data = conexion.seleccionarProductos();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Producto producto : productos_data) {
            Object[] resultado = {
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getLineaProducto().getLinea(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getPvp(),
                producto.getProveedor().getNombreEmpresa()
            };
            dtm_productos.addRow(resultado);
        }
    }

    private void mostrarLineasProductos() {

        String titulos_lineasproductos[] = {"Linea productos", "Descripción"};
        dtm_lineasproductos.setColumnIdentifiers(titulos_lineasproductos);
        tabla_lineasproductos.setModel(dtm_lineasproductos);

        try {
            conexion.conectar();
            lineasproductos_data = conexion.seleccionarLineasProducto();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (LineaProducto linea : lineasproductos_data) {
            String[] resultado = {
                linea.getLinea(),
                linea.getDescripcion()
            };
            dtm_lineasproductos.addRow(resultado);
        }
    }

    private void mostrarProveedores() {

        String titulos_proveedores[] = {"ID", "Nombre Empresa", "Nombre Contacto", "Ciudad", "CCAA", "Telefono", "Página Web"};
        dtm_proveedores.setColumnIdentifiers(titulos_proveedores);
        tabla_proveedores.setModel(dtm_proveedores);

        try {
            conexion.conectar();
            proveedores_data = conexion.seleccionarProveedores();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Proveedor proveedor : proveedores_data) {
            Object[] resultado = {
                proveedor.getIdproveedor(),
                proveedor.getNombreEmpresa(),
                proveedor.getNombreContacto(),
                proveedor.getCiudad().getNombreCiudad(),
                proveedor.getCiudad().getNombreComunidad(),
                proveedor.getTelefono(),
                proveedor.getPaginaweb()};
            dtm_proveedores.addRow(resultado);
        }
    }

    private void mostrarClientes() {

        String titulos_clientes[] = {"ID", "Nombre", "Teléfono", "Dirección", "Ciudad", "CCAA", "Código postal", "Email"};
        dtm_clientes.setColumnIdentifiers(titulos_clientes);
        tabla_clientes.setModel(dtm_clientes);

        try {
            conexion.conectar();
            clientes_data = conexion.seleccionarClientes();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Cliente cliente : clientes_data) {
            Object[] resultado = {
                cliente.getIdClientes(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getCiudad().getNombreCiudad(),
                cliente.getCiudad().getNombreComunidad(),
                cliente.getCodigoPostal(),
                cliente.getEmail()
            };
            dtm_clientes.addRow(resultado);
        }
    }

    private void mostrarEmpleados() {

        String titulos_empleados[] = {"ID", "Nombre", "Primer apellido", "Segundo apellido", "DNI", "Teléfono", "Email", "ID Usuario", "Usuario", "Privilegios"};
        dtm_empleados.setColumnIdentifiers(titulos_empleados);
        tabla_empleados.setModel(dtm_empleados);

        try {
            conexion.conectar();
            empleados_data = conexion.seleccionarEmpleados();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Empleado empleado : empleados_data) {
            Object[] resultado = {
                empleado.getIdempleado(),
                empleado.getNombre(),
                empleado.getApellido1(),
                empleado.getApellido2(),
                empleado.getDni(),
                empleado.getTelefono(),
                empleado.getEmail(),
                empleado.getUsuario().getIdUsuario(),
                empleado.getUsuario().getUsuario(),
                empleado.getUsuario().getRole()
            };
            dtm_empleados.addRow(resultado);
        }
    }

    private void mostrarDetallesPedidos() {

        String titulos_pedidos[] = {"ID", "ID Producto", "Cantidad", "Precio de venta", "Cliente", "Empleado", "Fecha", "Dirección", "Ciudad", "Código postal"};
        dtm_detallespedido.setColumnIdentifiers(titulos_pedidos);
        tabla_pedidos.setModel(dtm_detallespedido);

        try {
            conexion.conectar();
            detallespedidos_data = conexion.seleccionarDetallesPedidos();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (DetallesPedido pedido : detallespedidos_data) {
            Object[] resultado = {
                pedido.getIdpedido().getIdpedido(),
                pedido.getIdproducto().getIdProducto(),
                pedido.getCantidad(),
                pedido.getPrecio_venta(),
                pedido.getIdpedido().getCliente().getNombre(),
                pedido.getIdpedido().getEmpleado().getNombre(),
                pedido.getIdpedido().getFechaPedido(),
                pedido.getIdpedido().getDireccionPedido(),
                pedido.getIdpedido().getCiudadPedido().getNombreCiudad(),
                pedido.getIdpedido().getCodigopostalPedido()
            };
            dtm_detallespedido.addRow(resultado);
        }
    }

    private void mostrarPedidos() {

        String titulos_pedidos[] = {"ID", "Cliente", "Empleado", "Fecha", "Dirección", "Ciudad", "Código postal"};
        dtm_pedidos.setColumnIdentifiers(titulos_pedidos);
        tabla_pedidos.setModel(dtm_pedidos);

        try {
            conexion.conectar();
            pedidos_data = conexion.seleccionarPedidos();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Pedido pedido : pedidos_data) {
            Object[] resultado = {
                pedido.getIdpedido(),
                pedido.getCliente().getNombre(),
                pedido.getEmpleado().getNombre(),
                pedido.getFechaPedido(),
                pedido.getDireccionPedido(),
                pedido.getCiudadPedido().getNombreCiudad(),
                pedido.getCodigopostalPedido()
            };
            dtm_pedidos.addRow(resultado);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_productos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_proveedores = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_lineasproductos = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla_clientes = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla_empleados = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabla_pedidos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabla_productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabla_productos);

        jTabbedPane1.addTab("Productos", jScrollPane1);

        tabla_proveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tabla_proveedores);

        jTabbedPane1.addTab("Proveedores", jScrollPane2);

        tabla_lineasproductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tabla_lineasproductos);

        jTabbedPane1.addTab("Líneas productos", jScrollPane3);

        tabla_clientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tabla_clientes);

        jTabbedPane1.addTab("Clientes", jScrollPane4);

        tabla_empleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tabla_empleados);

        jTabbedPane1.addTab("Empleados", jScrollPane5);

        tabla_pedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tabla_pedidos);

        jTabbedPane1.addTab("Pedidos", jScrollPane6);

        jButton1.setText("Insertar");

        jButton2.setText("Eliminar");

        jButton3.setText("Modificar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1041, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jButton1)
                .addGap(49, 49, 49)
                .addComponent(jButton2)
                .addGap(49, 49, 49)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 771, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Productos")) {
            Object[] datos = new Object[7];

            for (int i = 0; i < tabla_productos.getColumnCount(); i++) {
                dtm_productos.getValueAt(tabla_productos.getSelectedRow(), i);
                productos_data.get(i);
            }

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Proveedores")) {

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Líneas productos")) {

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes")) {

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Empleados")) {

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Pedidos")) {

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new main().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tabla_clientes;
    private javax.swing.JTable tabla_empleados;
    private javax.swing.JTable tabla_lineasproductos;
    private javax.swing.JTable tabla_pedidos;
    private javax.swing.JTable tabla_productos;
    private javax.swing.JTable tabla_proveedores;
    // End of variables declaration//GEN-END:variables
}
