/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import componentes.NonEditableModel;
import componentes.Validador;
import java.awt.Color;
import javax.swing.table.TableRowSorter;
import conexion.Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DAW
 */
public class MainApp extends javax.swing.JFrame {

    NonEditableModel dtm_productos = new NonEditableModel();
    NonEditableModel dtm_lineaProductos = new NonEditableModel();
    NonEditableModel dtm_proveedores = new NonEditableModel();
    NonEditableModel dtm_pedidos = new NonEditableModel();
    NonEditableModel dtm_empleados = new NonEditableModel();
    NonEditableModel dtm_empresasEnvio = new NonEditableModel();

    Conexion conex = Conexion.getInstance();
    Validador validador = Validador.getInstance();
    private String user;

    /**
     * Creates new form main
     */
    public MainApp(String usuario, boolean admin) {
        initComponents();
        this.setSize(750, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("Aplicación   -   " + usuario);
        jm_usuario.setText(usuario);
        jtp_main.setSize(750, 450);
        this.user = usuario;


        /*INICIO TABLAS*/
        String titulos_productos[] = {"ID", "Productos", "Linea de producto", "Descripcion", "En stock", "PVP", "Proveedor"};
        String titulos_lineaProductos[] = {"Linea de producto", "Descripción"};
        String titulos_proveedores[] = {"ID", "Empresa", "Contacto", "Ciudad", "Teléfono", "Página web"};
        String titulos_pedidos[] = {"ID", "Cliente", "Empleado", "Fecha Pedido", "Dirección Pedido", "Ciudad Pedido", "Codigo Postal Pedido", "Empresa Envio"};
        String titulos_empleados[] = {"ID", "Nombre", "Apellidos", "DNI", "Teléfono", "Correo", "Usuario", "Rol"};
        String titulos_empresasEnvio[] = {"ID", "Nombre", "Teléfono", "Correo"};

        dtm_productos.setColumnIdentifiers(titulos_productos);
        dtm_lineaProductos.setColumnIdentifiers(titulos_lineaProductos);
        dtm_proveedores.setColumnIdentifiers(titulos_proveedores);
        dtm_pedidos.setColumnIdentifiers(titulos_pedidos);
        dtm_empleados.setColumnIdentifiers(titulos_empleados);
        dtm_empresasEnvio.setColumnIdentifiers(titulos_empresasEnvio);

        jTable1.setModel(dtm_productos);
        jTable2.setModel(dtm_lineaProductos);
        jTable3.setModel(dtm_proveedores);
        jTable4.setModel(dtm_pedidos);
        jTable5.setModel(dtm_empleados);
        jTable6.setModel(dtm_empresasEnvio);

        conex.MostrarTablaProductos(dtm_productos);
        conex.MostrarTablaLineaProductos(dtm_lineaProductos);
        conex.MostrarTablaProveedores(dtm_proveedores);
        conex.MostrarTablaPedidos(dtm_pedidos);
        conex.MostrarTablaEmpleados(dtm_empleados);
        conex.MostrarTablaEmpresasEnvio(dtm_empresasEnvio);

        jTable1.setAutoCreateRowSorter(true);
        jTable2.setAutoCreateRowSorter(true);
        jTable3.setAutoCreateRowSorter(true);
        jTable4.setAutoCreateRowSorter(true);
        jTable5.setAutoCreateRowSorter(true);
        jTable6.setAutoCreateRowSorter(true);
        if (!admin) {
            jtp_main.remove(jScrollPane6);
            jtp_main.remove(jScrollPane8);
        }

        /* FIN TABLAS */
 /* DIALOGS*/
        jDialog_BusquedaAvanzada.setLocationRelativeTo(null);
        jpane_productos.setVisible(false);
        jpane_lineas.setVisible(false);
        jpane_proveedores.setVisible(false);
        jpane_pedidos.setVisible(false);

        jDialog_InsertarTupla.setLocationRelativeTo(null);
        jpane_productos1.setVisible(false);
        jpane_lineas1.setVisible(false);
        jpane_proveedores1.setVisible(false);

        jDialog_ModificarTupla.setLocationRelativeTo(null);
        jpane_pedidos1.setVisible(false);
        jpane_productos2.setVisible(false);
        jpane_lineas2.setVisible(false);
        jpane_proveedores2.setVisible(false);
        /* FIN DIALOGS*/

        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /* COMBOBOX MODELS*/
        DefaultComboBoxModel dcbm_lineaProducto = new DefaultComboBoxModel();
        DefaultComboBoxModel dcbm_proveedores = new DefaultComboBoxModel();
        DefaultComboBoxModel dcbm_ciudades = new DefaultComboBoxModel();
        DefaultComboBoxModel dcbm_tablas = new DefaultComboBoxModel();

        jcb_lineaProducto1.setModel(dcbm_lineaProducto);
        jcb_lineaProducto.setModel(dcbm_lineaProducto);
        jcb_lineaProducto2.setModel(dcbm_lineaProducto);

        jcb_proveedorProducto1.setModel(dcbm_proveedores);
        jcb_proveedorProducto.setModel(dcbm_proveedores);
        jcb_proveedorProducto2.setModel(dcbm_proveedores);

        jcb_ciudadProveedor.setModel(dcbm_ciudades);
        jcb_ciudadPedido.setModel(dcbm_ciudades);
        jComboBox2.setModel(dcbm_ciudades);
        jcb_ciudadPedido1.setModel(dcbm_ciudades);
        jcb_ciudadProveedor1.setModel(dcbm_ciudades);

        jcb_tabla.setModel(dcbm_tablas);
        jcb_tabla1.setModel(dcbm_tablas);

        conex.MostrarComboBoxLineaProducto(dcbm_lineaProducto);
        conex.MostrarComboBoxProveedores(dcbm_proveedores);
        conex.MostrarComboBoxCiudades(dcbm_ciudades);
        /* Lo siguiente enseña las tablas en InsertarTupla y Busqueda*/

        ArrayList al = new ArrayList();
        al.add("Selecciona una tabla");
        for (int i = 0; i < jtp_main.getTabCount(); i++) {
            String dato = jtp_main.getTitleAt(i);
            al.add(dato);
        }
        dcbm_tablas.addAll(al);

        /*SETTER VALORES POR DEFECTO*/
        jcb_lineaProducto1.setSelectedIndex(0);
        jcb_lineaProducto.setSelectedIndex(0);
        jcb_proveedorProducto1.setSelectedIndex(0);
        jcb_proveedorProducto.setSelectedIndex(0);
        jcb_ciudadProveedor.setSelectedIndex(0);
        jcb_ciudadPedido.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jcb_tabla.setSelectedIndex(0);
        jcb_tabla1.setSelectedIndex(0);
        /*FIN COMBOBOX MODELS*/
    }

    MainApp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void buscar(String busqueda) {
        DefaultTableModel dtm1 = (DefaultTableModel) jTable1.getModel();
        DefaultTableModel dtm2 = (DefaultTableModel) jTable2.getModel();
        DefaultTableModel dtm3 = (DefaultTableModel) jTable3.getModel();
        DefaultTableModel dtm4 = (DefaultTableModel) jTable4.getModel();
        DefaultTableModel dtm5 = (DefaultTableModel) jTable5.getModel();
        DefaultTableModel dtm6 = (DefaultTableModel) jTable6.getModel();

        TableRowSorter<DefaultTableModel> trs1 = new TableRowSorter<>(dtm1);
        TableRowSorter<DefaultTableModel> trs2 = new TableRowSorter<>(dtm2);
        TableRowSorter<DefaultTableModel> trs3 = new TableRowSorter<>(dtm3);
        TableRowSorter<DefaultTableModel> trs4 = new TableRowSorter<>(dtm4);
        TableRowSorter<DefaultTableModel> trs5 = new TableRowSorter<>(dtm5);
        TableRowSorter<DefaultTableModel> trs6 = new TableRowSorter<>(dtm6);

        jTable1.setRowSorter(trs1);
        jTable2.setRowSorter(trs2);
        jTable3.setRowSorter(trs3);
        jTable4.setRowSorter(trs4);
        jTable3.setRowSorter(trs5);
        jTable4.setRowSorter(trs6);

        trs1.setRowFilter(RowFilter.regexFilter("^" + busqueda));
        trs2.setRowFilter(RowFilter.regexFilter("^" + busqueda));
        trs3.setRowFilter(RowFilter.regexFilter("^" + busqueda));
        trs4.setRowFilter(RowFilter.regexFilter("^" + busqueda));
        trs5.setRowFilter(RowFilter.regexFilter("^" + busqueda));
        trs6.setRowFilter(RowFilter.regexFilter("^" + busqueda));

    }

    /*
        public void BusquedaAvanzada(String[] parametros){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(conex.getDb(), conex.getDb_user(), conex.getDb_password());
            PreparedStatement statement = connection.prepareStatement("select * from productos where nombreproducto = ? and lineaproducto = ? and cantidadEnStock < ? and cantidadEnStock > ? and pvp < ? and pvp > ? and proveedor = ?");
            for(int i = 0; i<parametros.length ;i++){
                statement.setString(i+1, parametros[i]);
            }
            
            ResultSet resultSet = statement.executeQuery();
            conex.MostrarTablaProductos(dtm_productos,resultSet);
            resultSet.close();
            statement.close();
            connection.close();
        }catch (ClassNotFoundException | SQLException exception) {
            System.out.println(exception);
            
        }
    }
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_BusquedaAvanzada = new javax.swing.JDialog();
        jcb_tabla = new javax.swing.JComboBox<>();
        jb_buscar = new javax.swing.JButton();
        jpane_pedidos = new javax.swing.JPanel();
        jtf_clientePedido = new javax.swing.JTextField();
        jtf_empleadoPedido = new javax.swing.JTextField();
        jtf_codigoPostalPedido = new javax.swing.JTextField();
        jtf_diaPedido = new javax.swing.JTextField();
        jtf_mesPedido = new javax.swing.JTextField();
        jtf_añoPedido = new javax.swing.JTextField();
        jtf_direccionPedido = new javax.swing.JTextField();
        jtf_empresaEnvioPedido = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jcb_ciudadPedido = new javax.swing.JComboBox<>();
        jpane_proveedores = new javax.swing.JPanel();
        jtf_empresaProveedor = new javax.swing.JTextField();
        jtf_contactoProveedor = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jcb_ciudadProveedor = new javax.swing.JComboBox<>();
        jtf_telefonoProveedor = new javax.swing.JTextField();
        jtf_paginaWebProveedor = new javax.swing.JTextField();
        jpane_lineas = new javax.swing.JPanel();
        jtf_nombreLineaProducto = new javax.swing.JTextField();
        jtf_descripcionLineaProducto = new javax.swing.JTextField();
        jpane_productos = new javax.swing.JPanel();
        jtf_producto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jcb_lineaProducto = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jtf_minStock = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtf_maxStock = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtf_minPvp = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtf_maxPvp = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jcb_proveedorProducto = new javax.swing.JComboBox<>();
        jDialog_InsertarTupla = new javax.swing.JDialog();
        jcb_tabla1 = new javax.swing.JComboBox<>();
        jb_buscar1 = new javax.swing.JButton();
        jpane_productos1 = new javax.swing.JPanel();
        jtf_producto1 = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jta_descripcionProducto = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jcb_lineaProducto1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jtf_EnStock = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtf_Pvp = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jcb_proveedorProducto1 = new javax.swing.JComboBox<>();
        jpane_proveedores1 = new javax.swing.JPanel();
        jTextField22 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jpane_lineas1 = new javax.swing.JPanel();
        jTextField21 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jDialog_ModificarTupla = new javax.swing.JDialog();
        jpane_productos2 = new javax.swing.JPanel();
        jtf_idproducto2 = new javax.swing.JTextField();
        jtf_producto2 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jcb_lineaProducto2 = new javax.swing.JComboBox<>();
        jtf_descripcionProducto2 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jtf_stock1 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jtf_pvp1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jcb_proveedorProducto2 = new javax.swing.JComboBox<>();
        jpane_lineas2 = new javax.swing.JPanel();
        jtf_nombreLineaProducto1 = new javax.swing.JTextField();
        jtf_descripcionLineaProducto1 = new javax.swing.JTextField();
        jpane_proveedores2 = new javax.swing.JPanel();
        jtf_empresaProveedor1 = new javax.swing.JTextField();
        jtf_contactoProveedor1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jcb_ciudadProveedor1 = new javax.swing.JComboBox<>();
        jtf_telefonoProveedor1 = new javax.swing.JTextField();
        jtf_paginaWebProveedor1 = new javax.swing.JTextField();
        jpane_pedidos1 = new javax.swing.JPanel();
        jtf_clientePedido1 = new javax.swing.JTextField();
        jtf_empleadoPedido1 = new javax.swing.JTextField();
        jtf_codigoPostalPedido1 = new javax.swing.JTextField();
        jtf_diaPedido1 = new javax.swing.JTextField();
        jtf_mesPedido1 = new javax.swing.JTextField();
        jtf_añoPedido1 = new javax.swing.JTextField();
        jtf_direccionPedido1 = new javax.swing.JTextField();
        jtf_empresaEnvioPedido1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jcb_ciudadPedido1 = new javax.swing.JComboBox<>();
        jb_modificar = new javax.swing.JButton();
        jtp_main = new javax.swing.JTabbedPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        barraBusqueda = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jm_insertar = new javax.swing.JMenu();
        jmi_insertar = new javax.swing.JMenuItem();
        jmi_modificar = new javax.swing.JMenuItem();
        jmi_nuevoPedido = new javax.swing.JMenuItem();
        jmi_eliminar = new javax.swing.JMenuItem();
        jm_usuario = new javax.swing.JMenu();
        jmi_perfil = new javax.swing.JMenuItem();
        jmi_cerrarSesion = new javax.swing.JMenuItem();

        jDialog_BusquedaAvanzada.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_BusquedaAvanzada.setTitle("Búsqueda Avanzada");
        jDialog_BusquedaAvanzada.setMinimumSize(new java.awt.Dimension(300, 400));
        jDialog_BusquedaAvanzada.setModal(true);
        jDialog_BusquedaAvanzada.setResizable(false);
        jDialog_BusquedaAvanzada.setSize(new java.awt.Dimension(300, 400));
        jDialog_BusquedaAvanzada.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcb_tabla.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Productos", "Lineas de producto", "Proveedores", "Pedidos" }));
        jcb_tabla.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcb_tablaItemStateChanged(evt);
            }
        });
        jDialog_BusquedaAvanzada.getContentPane().add(jcb_tabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, 30));

        jb_buscar.setText("Buscar");
        jb_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_buscarActionPerformed(evt);
            }
        });
        jDialog_BusquedaAvanzada.getContentPane().add(jb_buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, -1, 30));

        jpane_pedidos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_clientePedido.setText("Cliente");
        jtf_clientePedido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_clientePedidoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_clientePedidoFocusLost(evt);
            }
        });
        jpane_pedidos.add(jtf_clientePedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 220, -1));

        jtf_empleadoPedido.setText("Empleado");
        jtf_empleadoPedido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_empleadoPedidoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_empleadoPedidoFocusLost(evt);
            }
        });
        jpane_pedidos.add(jtf_empleadoPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 220, -1));

        jtf_codigoPostalPedido.setText("Codigo Postal Pedido");
        jtf_codigoPostalPedido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_codigoPostalPedidoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_codigoPostalPedidoFocusLost(evt);
            }
        });
        jpane_pedidos.add(jtf_codigoPostalPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 220, -1));

        jtf_diaPedido.setText("DD");
        jpane_pedidos.add(jtf_diaPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 40, -1));

        jtf_mesPedido.setText("MM");
        jpane_pedidos.add(jtf_mesPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 40, -1));

        jtf_añoPedido.setText("AAAA");
        jtf_añoPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtf_añoPedidoActionPerformed(evt);
            }
        });
        jpane_pedidos.add(jtf_añoPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 50, -1));

        jtf_direccionPedido.setText("Dirección");
        jtf_direccionPedido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_direccionPedidoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_direccionPedidoFocusLost(evt);
            }
        });
        jpane_pedidos.add(jtf_direccionPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 220, -1));

        jtf_empresaEnvioPedido.setText("Empresa Envio");
        jpane_pedidos.add(jtf_empresaEnvioPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 220, -1));

        jLabel14.setText("Ciudad");
        jpane_pedidos.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        jcb_ciudadPedido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_pedidos.add(jcb_ciudadPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 190, 150, -1));

        jDialog_BusquedaAvanzada.getContentPane().add(jpane_pedidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 300, 260));

        jpane_proveedores.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_empresaProveedor.setText("Empresa");
        jtf_empresaProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_empresaProveedorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_empresaProveedorFocusLost(evt);
            }
        });
        jpane_proveedores.add(jtf_empresaProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, -1));

        jtf_contactoProveedor.setText("Contacto");
        jtf_contactoProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_contactoProveedorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_contactoProveedorFocusLost(evt);
            }
        });
        jpane_proveedores.add(jtf_contactoProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 220, -1));

        jLabel8.setText("Ciudad");
        jpane_proveedores.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jcb_ciudadProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_proveedores.add(jcb_ciudadProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 160, 30));

        jtf_telefonoProveedor.setText("Teléfono");
        jtf_telefonoProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_telefonoProveedorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_telefonoProveedorFocusLost(evt);
            }
        });
        jpane_proveedores.add(jtf_telefonoProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 220, -1));

        jtf_paginaWebProveedor.setText("Página web");
        jtf_paginaWebProveedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_paginaWebProveedorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_paginaWebProveedorFocusLost(evt);
            }
        });
        jpane_proveedores.add(jtf_paginaWebProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 220, -1));

        jDialog_BusquedaAvanzada.getContentPane().add(jpane_proveedores, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 300, 250));

        jpane_lineas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_nombreLineaProducto.setText("Linea de producto");
        jtf_nombreLineaProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_nombreLineaProductoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_nombreLineaProductoFocusLost(evt);
            }
        });
        jpane_lineas.add(jtf_nombreLineaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, -1));

        jtf_descripcionLineaProducto.setText("Descripción");
        jtf_descripcionLineaProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_descripcionLineaProductoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_descripcionLineaProductoFocusLost(evt);
            }
        });
        jpane_lineas.add(jtf_descripcionLineaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 220, -1));

        jDialog_BusquedaAvanzada.getContentPane().add(jpane_lineas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 300, 140));

        jpane_productos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_producto.setText("Producto");
        jtf_producto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_productoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_productoFocusLost(evt);
            }
        });
        jpane_productos.add(jtf_producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, -1));

        jLabel3.setText("Linea");
        jpane_productos.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, 20));

        jcb_lineaProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_productos.add(jcb_lineaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 150, -1));

        jLabel4.setText("Stock");
        jpane_productos.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jtf_minStock.setText("Min");
        jtf_minStock.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_minStockFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_minStockFocusLost(evt);
            }
        });
        jtf_minStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtf_minStockActionPerformed(evt);
            }
        });
        jpane_productos.add(jtf_minStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 50, -1));

        jLabel7.setText("-");
        jpane_productos.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 30, -1));

        jtf_maxStock.setText("Max");
        jtf_maxStock.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_maxStockFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_maxStockFocusLost(evt);
            }
        });
        jpane_productos.add(jtf_maxStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 50, -1));

        jLabel5.setText("PVP");
        jpane_productos.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, -1, -1));

        jtf_minPvp.setText("Min");
        jtf_minPvp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_minPvpFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_minPvpFocusLost(evt);
            }
        });
        jpane_productos.add(jtf_minPvp, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 50, -1));

        jLabel6.setText("-");
        jpane_productos.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 30, -1));

        jtf_maxPvp.setText("Max");
        jtf_maxPvp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_maxPvpFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_maxPvpFocusLost(evt);
            }
        });
        jpane_productos.add(jtf_maxPvp, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 50, -1));

        jLabel2.setText("Proveedor");
        jpane_productos.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jcb_proveedorProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_productos.add(jcb_proveedorProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 150, -1));

        jDialog_BusquedaAvanzada.getContentPane().add(jpane_productos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 300, 250));

        jDialog_InsertarTupla.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_InsertarTupla.setTitle("Insertar");
        jDialog_InsertarTupla.setAlwaysOnTop(true);
        jDialog_InsertarTupla.setModal(true);
        jDialog_InsertarTupla.setResizable(false);
        jDialog_InsertarTupla.setSize(new java.awt.Dimension(420, 420));
        jDialog_InsertarTupla.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcb_tabla1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Productos", "Lineas de producto", "Proveedores" }));
        jcb_tabla1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcb_tabla1ItemStateChanged(evt);
            }
        });
        jDialog_InsertarTupla.getContentPane().add(jcb_tabla1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 220, 30));

        jb_buscar1.setText("Insertar");
        jb_buscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_buscar1ActionPerformed(evt);
            }
        });
        jDialog_InsertarTupla.getContentPane().add(jb_buscar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, -1, 30));

        jpane_productos1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_producto1.setText("Producto");
        jtf_producto1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_producto1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_producto1FocusLost(evt);
            }
        });
        jtf_producto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtf_producto1ActionPerformed(evt);
            }
        });
        jpane_productos1.add(jtf_producto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 270, -1));

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(234, 20));

        jta_descripcionProducto.setColumns(20);
        jta_descripcionProducto.setLineWrap(true);
        jta_descripcionProducto.setRows(3);
        jta_descripcionProducto.setText("Descripción");
        jta_descripcionProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jta_descripcionProductoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jta_descripcionProductoFocusLost(evt);
            }
        });
        jScrollPane4.setViewportView(jta_descripcionProducto);

        jpane_productos1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 270, 70));

        jLabel9.setText("Linea");
        jpane_productos1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 70, 20));

        jcb_lineaProducto1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_productos1.add(jcb_lineaProducto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 200, -1));

        jLabel10.setText("Stock");
        jpane_productos1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 80, -1));

        jtf_EnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtf_EnStockActionPerformed(evt);
            }
        });
        jpane_productos1.add(jtf_EnStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 200, -1));

        jLabel11.setText("PVP");
        jpane_productos1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 70, -1));
        jpane_productos1.add(jtf_Pvp, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 200, -1));

        jLabel12.setText("Proveedor");
        jpane_productos1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 100, -1));

        jcb_proveedorProducto1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_productos1.add(jcb_proveedorProducto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, 200, -1));

        jDialog_InsertarTupla.getContentPane().add(jpane_productos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 420, 270));

        jpane_proveedores1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField22.setText("Contacto");
        jTextField22.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField22FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField22FocusLost(evt);
            }
        });
        jpane_proveedores1.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 270, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_proveedores1.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 210, 30));

        jLabel13.setText("Ciudad");
        jpane_proveedores1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 90, -1));

        jTextField23.setText("Teléfono");
        jTextField23.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField23FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField23FocusLost(evt);
            }
        });
        jpane_proveedores1.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 270, -1));

        jTextField24.setText("Página web");
        jTextField24.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField24FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField24FocusLost(evt);
            }
        });
        jpane_proveedores1.add(jTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 270, -1));

        jTextField25.setText("Empresa");
        jTextField25.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField25FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField25FocusLost(evt);
            }
        });
        jpane_proveedores1.add(jTextField25, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 270, -1));

        jDialog_InsertarTupla.getContentPane().add(jpane_proveedores1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 420, 250));

        jpane_lineas1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField21.setText("Linea de producto");
        jTextField21.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField21FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField21FocusLost(evt);
            }
        });
        jpane_lineas1.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 270, -1));

        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(3);
        jTextArea2.setText("Descripción");
        jTextArea2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextArea2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextArea2FocusLost(evt);
            }
        });
        jScrollPane5.setViewportView(jTextArea2);

        jpane_lineas1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 270, 70));

        jDialog_InsertarTupla.getContentPane().add(jpane_lineas1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 420, 270));

        jMenuItem1.setText("Eliminar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Modificar");
        jPopupMenu1.add(jMenuItem2);

        jDialog_ModificarTupla.setSize(new java.awt.Dimension(420, 420));
        jDialog_ModificarTupla.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpane_productos2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_idproducto2.setEditable(false);
        jtf_idproducto2.setText("ID");
        jpane_productos2.add(jtf_idproducto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, -1));

        jtf_producto2.setText("Producto");
        jpane_productos2.add(jtf_producto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 220, -1));

        jLabel17.setText("Linea");
        jpane_productos2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, 20));

        jcb_lineaProducto2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_productos2.add(jcb_lineaProducto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 150, -1));
        jpane_productos2.add(jtf_descripcionProducto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 210, -1));

        jLabel18.setText("Stock");
        jpane_productos2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        jtf_stock1.setText("Stock");
        jtf_stock1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtf_stock1ActionPerformed(evt);
            }
        });
        jpane_productos2.add(jtf_stock1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 150, -1));

        jLabel20.setText("PVP");
        jpane_productos2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, -1));

        jtf_pvp1.setText("PVP");
        jpane_productos2.add(jtf_pvp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 150, -1));

        jLabel22.setText("Proveedor");
        jpane_productos2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jcb_proveedorProducto2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_productos2.add(jcb_proveedorProducto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 150, -1));

        jDialog_ModificarTupla.getContentPane().add(jpane_productos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 280, 260));

        jpane_lineas2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_nombreLineaProducto1.setText("Linea de producto");
        jtf_nombreLineaProducto1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_nombreLineaProducto1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_nombreLineaProducto1FocusLost(evt);
            }
        });
        jpane_lineas2.add(jtf_nombreLineaProducto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, -1));

        jtf_descripcionLineaProducto1.setText("Descripción");
        jtf_descripcionLineaProducto1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_descripcionLineaProducto1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_descripcionLineaProducto1FocusLost(evt);
            }
        });
        jpane_lineas2.add(jtf_descripcionLineaProducto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 220, -1));

        jDialog_ModificarTupla.getContentPane().add(jpane_lineas2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 140));

        jpane_proveedores2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_empresaProveedor1.setText("Empresa");
        jtf_empresaProveedor1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_empresaProveedor1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_empresaProveedor1FocusLost(evt);
            }
        });
        jpane_proveedores2.add(jtf_empresaProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, -1));

        jtf_contactoProveedor1.setText("Contacto");
        jtf_contactoProveedor1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_contactoProveedor1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_contactoProveedor1FocusLost(evt);
            }
        });
        jpane_proveedores2.add(jtf_contactoProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 220, -1));

        jLabel16.setText("Ciudad");
        jpane_proveedores2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jcb_ciudadProveedor1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_proveedores2.add(jcb_ciudadProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 160, 30));

        jtf_telefonoProveedor1.setText("Teléfono");
        jtf_telefonoProveedor1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_telefonoProveedor1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_telefonoProveedor1FocusLost(evt);
            }
        });
        jpane_proveedores2.add(jtf_telefonoProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 220, -1));

        jtf_paginaWebProveedor1.setText("Página web");
        jtf_paginaWebProveedor1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_paginaWebProveedor1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_paginaWebProveedor1FocusLost(evt);
            }
        });
        jpane_proveedores2.add(jtf_paginaWebProveedor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 220, -1));

        jDialog_ModificarTupla.getContentPane().add(jpane_proveedores2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 250));

        jpane_pedidos1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtf_clientePedido1.setText("Cliente");
        jtf_clientePedido1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_clientePedido1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_clientePedido1FocusLost(evt);
            }
        });
        jpane_pedidos1.add(jtf_clientePedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 220, -1));

        jtf_empleadoPedido1.setText("Empleado");
        jtf_empleadoPedido1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_empleadoPedido1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_empleadoPedido1FocusLost(evt);
            }
        });
        jpane_pedidos1.add(jtf_empleadoPedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 220, -1));

        jtf_codigoPostalPedido1.setText("Codigo Postal Pedido");
        jtf_codigoPostalPedido1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_codigoPostalPedido1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_codigoPostalPedido1FocusLost(evt);
            }
        });
        jpane_pedidos1.add(jtf_codigoPostalPedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 220, -1));

        jtf_diaPedido1.setText("DD");
        jpane_pedidos1.add(jtf_diaPedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 40, -1));

        jtf_mesPedido1.setText("MM");
        jpane_pedidos1.add(jtf_mesPedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 40, -1));

        jtf_añoPedido1.setText("AAAA");
        jtf_añoPedido1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtf_añoPedido1ActionPerformed(evt);
            }
        });
        jpane_pedidos1.add(jtf_añoPedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 50, -1));

        jtf_direccionPedido1.setText("Dirección");
        jtf_direccionPedido1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtf_direccionPedido1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtf_direccionPedido1FocusLost(evt);
            }
        });
        jpane_pedidos1.add(jtf_direccionPedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 220, -1));

        jtf_empresaEnvioPedido1.setText("Empresa Envio");
        jpane_pedidos1.add(jtf_empresaEnvioPedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 220, -1));

        jLabel15.setText("Ciudad");
        jpane_pedidos1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        jcb_ciudadPedido1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpane_pedidos1.add(jcb_ciudadPedido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 190, 150, -1));

        jDialog_ModificarTupla.getContentPane().add(jpane_pedidos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 260));

        jb_modificar.setText("Modificar");
        jb_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_modificarActionPerformed(evt);
            }
        });
        jDialog_ModificarTupla.getContentPane().add(jb_modificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, -1, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(750, 500));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtp_main.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtp_mainMouseClicked(evt);
            }
        });

        jScrollPane7.setComponentPopupMenu(jPopupMenu1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Producto", "Linea producto", "Descripción", "En stock", "P.V.P", "Proveedor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setComponentPopupMenu(jPopupMenu1);
        jTable1.setShowGrid(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTable1);

        jtp_main.addTab("Productos", jScrollPane7);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable2.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(jTable2);

        jtp_main.addTab("Lineas de producto", jScrollPane1);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable3.setComponentPopupMenu(jPopupMenu1);
        jScrollPane2.setViewportView(jTable3);

        jtp_main.addTab("Proveedores", jScrollPane2);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable4.setComponentPopupMenu(jPopupMenu1);
        jScrollPane3.setViewportView(jTable4);

        jtp_main.addTab("Pedidos", jScrollPane3);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable5.setComponentPopupMenu(jPopupMenu1);
        jScrollPane6.setViewportView(jTable5);

        jtp_main.addTab("Empleados", jScrollPane6);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable6.setComponentPopupMenu(jPopupMenu1);
        jScrollPane8.setViewportView(jTable6);

        jtp_main.addTab("Empresas de envio", jScrollPane8);

        getContentPane().add(jtp_main, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 750, 400));

        barraBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                barraBusquedaFocusLost(evt);
            }
        });
        barraBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barraBusquedaActionPerformed(evt);
            }
        });
        barraBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                barraBusquedaKeyReleased(evt);
            }
        });
        getContentPane().add(barraBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 240, 30));

        jLabel1.setText("Buscar :");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 50, 30));

        jButton1.setText("🔍 Búsqueda avanzada");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, -1, 30));

        jm_insertar.setText("Base de datos");

        jmi_insertar.setText("Insertar");
        jmi_insertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmi_insertarActionPerformed(evt);
            }
        });
        jm_insertar.add(jmi_insertar);

        jmi_modificar.setText("Modificar");
        jmi_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmi_modificarActionPerformed(evt);
            }
        });
        jm_insertar.add(jmi_modificar);

        jmi_nuevoPedido.setText("Nuevo pedido");
        jm_insertar.add(jmi_nuevoPedido);

        jmi_eliminar.setText("Eliminar");
        jmi_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmi_eliminarActionPerformed(evt);
            }
        });
        jm_insertar.add(jmi_eliminar);

        jMenuBar1.add(jm_insertar);

        jm_usuario.setText("Usuario");
        jMenuBar1.add(Box.createHorizontalGlue());

        jmi_perfil.setText("Perfil");
        jm_usuario.add(jmi_perfil);

        jmi_cerrarSesion.setText("Cerrar sesion");
        jmi_cerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmi_cerrarSesionActionPerformed(evt);
            }
        });
        jm_usuario.add(jmi_cerrarSesion);

        jMenuBar1.add(jm_usuario);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmi_cerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmi_cerrarSesionActionPerformed
        // TODO add your handling code here:
        this.dispose();
        JFrame inicio_sesion = new Inicio_sesion();
        inicio_sesion.show();
    }//GEN-LAST:event_jmi_cerrarSesionActionPerformed

    private void barraBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barraBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_barraBusquedaActionPerformed

    private void barraBusquedaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barraBusquedaKeyReleased
        // TODO add your handling code here:
        buscar(barraBusqueda.getText());
    }//GEN-LAST:event_barraBusquedaKeyReleased

    private void barraBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_barraBusquedaFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_barraBusquedaFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jDialog_BusquedaAvanzada.show();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jmi_insertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmi_insertarActionPerformed
        // TODO add your handling code here:
        jDialog_InsertarTupla.show();
    }//GEN-LAST:event_jmi_insertarActionPerformed

    private void jcb_tablaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcb_tablaItemStateChanged
        // TODO add your handling code here:
        if (jcb_tabla.getSelectedItem().toString().equals("Selecciona una tabla") && evt.getStateChange() == 2) {
            jpane_productos.setVisible(false);
            jpane_lineas.setVisible(false);
            jpane_proveedores.setVisible(false);
            jpane_pedidos.setVisible(false);
        }
        if (jcb_tabla.getSelectedItem().toString().equals("Productos") && evt.getStateChange() == 2) {
            jpane_productos.setVisible(true);
            jpane_lineas.setVisible(false);
            jpane_proveedores.setVisible(false);
            jpane_pedidos.setVisible(false);
        }
        if (jcb_tabla.getSelectedItem().toString().equals("Lineas de producto") && evt.getStateChange() == 2) {
            jpane_productos.setVisible(false);
            jpane_lineas.setVisible(true);
            jpane_proveedores.setVisible(false);
            jpane_pedidos.setVisible(false);
        }
        if (jcb_tabla.getSelectedItem().toString().equals("Proveedores") && evt.getStateChange() == 2) {
            jpane_productos.setVisible(false);
            jpane_lineas.setVisible(false);
            jpane_proveedores.setVisible(true);
            jpane_pedidos.setVisible(false);
        }
        if (jcb_tabla.getSelectedItem().toString().equals("Pedidos") && evt.getStateChange() == 2) {
            jpane_productos.setVisible(false);
            jpane_lineas.setVisible(false);
            jpane_proveedores.setVisible(false);
            jpane_pedidos.setVisible(true);
        }
    }//GEN-LAST:event_jcb_tablaItemStateChanged

    private void jb_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_buscarActionPerformed
        // TODO add your handling code here:
        String[] parametrosBusquedaProductos = {
            jtf_producto.getText(),
            jcb_lineaProducto.getSelectedItem().toString(),
            jtf_minStock.getText(), jtf_maxStock.getText(),
            jtf_minPvp.getText(), jtf_maxPvp.getText(),
            jcb_proveedorProducto.getSelectedItem().toString()
        };
        String[] parametrosBusquedaLineasProducto = {
            jtf_nombreLineaProducto.getText(),
            jtf_descripcionLineaProducto.getText()
        };
        String[] parametrosBusquedaProveedores = {
            jtf_empresaProveedor.getText(),
            jtf_contactoProveedor.getText(),
            jcb_ciudadProveedor.getSelectedItem().toString(),
            jtf_telefonoProveedor.getText(),
            jtf_paginaWebProveedor.getText()
        };
        String[] parametrosBusquedaPedidos = {
            jtf_clientePedido.getText(),
            jtf_empleadoPedido.getText(),
            jtf_codigoPostalPedido.getText(),
            jtf_diaPedido.getText(),
            jtf_mesPedido.getText(),
            jtf_añoPedido.getText(),
            jtf_direccionPedido.getText(),
            jtf_empresaEnvioPedido.getText(),
            jcb_ciudadPedido.getSelectedItem().toString()
        };

        if (jcb_tabla.getSelectedItem().toString().equals("Productos")) {

            conex.BuscarPorTabla(1, parametrosBusquedaProductos);
        }
        if (jcb_tabla.getSelectedItem().toString().equals("Lineas de producto")) {

        }
        if (jcb_tabla.getSelectedItem().toString().equals("Proveedores")) {

        }
        if (jcb_tabla.getSelectedItem().toString().equals("Pedidos")) {

        }
        this.dispose();
    }//GEN-LAST:event_jb_buscarActionPerformed

    private void jtf_minStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_minStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtf_minStockActionPerformed

    private void jtf_añoPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_añoPedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtf_añoPedidoActionPerformed

    private void jcb_tabla1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcb_tabla1ItemStateChanged
        // TODO add your handling code here:
        if (jcb_tabla1.getSelectedItem().toString().equals("Selecciona una tabla") && evt.getStateChange() == 2) {
            jpane_productos1.setVisible(false);
            jpane_lineas1.setVisible(false);
            jpane_proveedores1.setVisible(false);
        }
        if (jcb_tabla1.getSelectedItem().toString().equals("Productos") && evt.getStateChange() == 2) {
            jpane_productos1.setVisible(true);
            jpane_lineas1.setVisible(false);
            jpane_proveedores1.setVisible(false);

        }
        if (jcb_tabla1.getSelectedItem().toString().equals("Lineas de producto") && evt.getStateChange() == 2) {
            jpane_productos1.setVisible(false);
            jpane_lineas1.setVisible(true);
            jpane_proveedores1.setVisible(false);

        }
        if (jcb_tabla1.getSelectedItem().toString().equals("Proveedores") && evt.getStateChange() == 2) {
            jpane_productos1.setVisible(false);
            jpane_lineas1.setVisible(false);
            jpane_proveedores1.setVisible(true);

        }
    }//GEN-LAST:event_jcb_tabla1ItemStateChanged

    private void jb_buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_buscar1ActionPerformed
        // TODO add your handling code here:
        String[] parametros = {
            jtf_producto.getText(),
            jta_descripcionProducto.getText(),
            jcb_lineaProducto.getSelectedItem().toString(),
            jtf_EnStock.getText(),
            jtf_Pvp.getText(),
            jcb_proveedorProducto.getSelectedItem().toString()
        };

        this.dispose();
    }//GEN-LAST:event_jb_buscar1ActionPerformed

    private void jtf_producto1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_producto1FocusGained
        // TODO add your handling code here:
        if (jtf_producto1.getText().equals("Producto")) {
            jtf_producto1.setText("");
        }
    }//GEN-LAST:event_jtf_producto1FocusGained

    private void jtf_producto1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_producto1FocusLost
        // TODO add your handling code here:
        if (jtf_producto1.getText().equals("")) {
            jtf_producto1.setText("Producto");
        }
    }//GEN-LAST:event_jtf_producto1FocusLost

    private void jtf_EnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_EnStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtf_EnStockActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_jTable1MouseClicked

    private void actualizarTablas() {
        dtm_productos.setRowCount(0);
        conex.MostrarTablaProductos(dtm_productos);

        dtm_lineaProductos.setRowCount(0);
        conex.MostrarTablaLineaProductos(dtm_lineaProductos);

        dtm_proveedores.setRowCount(0);
        conex.MostrarTablaProveedores(dtm_proveedores);

        dtm_pedidos.setRowCount(0);
        conex.MostrarTablaPedidos(dtm_pedidos);

        dtm_empleados.setRowCount(0);
        conex.MostrarTablaEmpleados(dtm_empleados);

        dtm_empresasEnvio.setRowCount(0);
        conex.MostrarTablaEmpresasEnvio(dtm_empresasEnvio);
    }

    private void eliminarTupla() {
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Productos")) {
            try {
                // TODO add your handling code here:
                int id = Integer.valueOf((jTable1.getValueAt(jTable1.getSelectedRow(), 0)).toString());

                if (id > 0 && id < 10) {
                    conex.EliminarProducto(id);
                    actualizarTablas();
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Lineas de producto")) {
            try {
                // TODO add your handling code here:
                String id = (jTable2.getValueAt(jTable2.getSelectedRow(), 0)).toString();

                if (id != null) {
                    conex.EliminarLineaProducto(id);
                    actualizarTablas();
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Proveedores")) {
            try {
                // TODO add your handling code here:
                int id = Integer.valueOf((jTable3.getValueAt(jTable3.getSelectedRow(), 0)).toString());
                if (id > 0 && id < 10) {
                    conex.EliminarProveedor(id);
                    actualizarTablas();
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Pedidos")) {
            try {
                // TODO add your handling code here:
                int id = Integer.valueOf((jTable4.getValueAt(jTable4.getSelectedRow(), 0)).toString());

                if (id > 0 && id < 10) {
                    conex.EliminarPedido(id);
                    actualizarTablas();
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Empleados")) {
            try {
                // TODO add your handling code here:
                int id = Integer.valueOf((jTable5.getValueAt(jTable5.getSelectedRow(), 0)).toString());

                if (id > 0 && id < 10) {
                    conex.EliminarEmpleado(id);
                    actualizarTablas();
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Empresas de envio")) {
            try {
                // TODO add your handling code here:
                int id = Integer.valueOf((jTable6.getValueAt(jTable6.getSelectedRow(), 0)).toString());

                if (id > 0 && id < 10) {
                    conex.EliminarEmpresaEnvio(id);
                    actualizarTablas();
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "No ha seleccionado ninguna fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        eliminarTupla();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jtf_producto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_producto1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtf_producto1ActionPerformed

    /*Ocultar o mostrar al ganar o perder el foco de los jTextField*/
    private void jta_descripcionProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jta_descripcionProductoFocusGained
        // TODO add your handling code here:
        if (jta_descripcionProducto.getText().equals("Descripción")) {
            jta_descripcionProducto.setText("");
        }
    }//GEN-LAST:event_jta_descripcionProductoFocusGained

    private void jta_descripcionProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jta_descripcionProductoFocusLost
        // TODO add your handling code here:
        if (jta_descripcionProducto.getText().equals("")) {
            jta_descripcionProducto.setText("Descripción");
        }
    }//GEN-LAST:event_jta_descripcionProductoFocusLost

    private void jTextField21FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField21FocusGained
        // TODO add your handling code here:
        if (jTextField21.getText().equals("Linea de producto")) {
            jTextField21.setText("");
        }
    }//GEN-LAST:event_jTextField21FocusGained

    private void jTextField21FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField21FocusLost
        // TODO add your handling code here:
        if (jTextField21.getText().equals("")) {
            jTextField21.setText("Linea de producto");
        }
    }//GEN-LAST:event_jTextField21FocusLost

    private void jTextArea2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea2FocusGained
        // TODO add your handling code here:
        if (jTextArea2.getText().equals("Descripción")) {
            jTextArea2.setText("");
        }
    }//GEN-LAST:event_jTextArea2FocusGained

    private void jTextArea2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea2FocusLost
        // TODO add your handling code here:
        if (jTextArea2.getText().equals("")) {
            jTextArea2.setText("Descripción");
        }
    }//GEN-LAST:event_jTextArea2FocusLost

    private void jTextField25FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField25FocusGained
        // TODO add your handling code here:
        if (jTextField25.getText().equals("Empresa")) {
            jTextField25.setText("");
        }
    }//GEN-LAST:event_jTextField25FocusGained

    private void jTextField25FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField25FocusLost
        // TODO add your handling code here:
        if (jTextField25.getText().equals("")) {
            jTextField25.setText("Empresa");
        }
    }//GEN-LAST:event_jTextField25FocusLost

    private void jTextField22FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField22FocusGained
        // TODO add your handling code here:
        if (jTextField22.getText().equals("Contacto")) {
            jTextField22.setText("");
        }
    }//GEN-LAST:event_jTextField22FocusGained

    private void jTextField22FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField22FocusLost
        // TODO add your handling code here:
        if (jTextField22.getText().equals("")) {
            jTextField22.setText("Contacto");
        }
    }//GEN-LAST:event_jTextField22FocusLost

    private void jTextField23FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField23FocusGained
        // TODO add your handling code here:
        if (jTextField23.getText().equals("Teléfono")) {
            jTextField23.setText("");
        }
    }//GEN-LAST:event_jTextField23FocusGained

    private void jTextField23FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField23FocusLost
        // TODO add your handling code here:
        if (jTextField23.getText().equals("")) {
            jTextField23.setText("Teléfono");
        }
    }//GEN-LAST:event_jTextField23FocusLost

    private void jTextField24FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField24FocusGained
        // TODO add your handling code here:
        if (jTextField24.getText().equals("Página web")) {
            jTextField24.setText("");
        }
    }//GEN-LAST:event_jTextField24FocusGained

    private void jTextField24FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField24FocusLost
        // TODO add your handling code here:
        if (jTextField24.getText().equals("")) {
            jTextField24.setText("Página web");
        }
    }//GEN-LAST:event_jTextField24FocusLost

    private void jtp_mainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtp_mainMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jtp_mainMouseClicked

    private void jtf_productoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_productoFocusGained
        // TODO add your handling code here:
        if (jtf_producto.getText().equals("Producto")) {
            jtf_producto.setText("");
        }
    }//GEN-LAST:event_jtf_productoFocusGained

    private void jtf_productoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_productoFocusLost
        // TODO add your handling code here:
        if (jtf_producto.getText().equals("")) {
            jtf_producto.setText("Producto");
        }
    }//GEN-LAST:event_jtf_productoFocusLost

    private void jtf_minStockFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_minStockFocusGained
        // TODO add your handling code here:
        if (jtf_minStock.getText().equals("Min")) {
            jtf_minStock.setText("");
        }
    }//GEN-LAST:event_jtf_minStockFocusGained

    private void jtf_minStockFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_minStockFocusLost
        // TODO add your handling code here:
        if (jtf_minStock.getText().equals("")) {
            jtf_minStock.setText("Min");
        }
    }//GEN-LAST:event_jtf_minStockFocusLost

    private void jtf_maxStockFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_maxStockFocusGained
        // TODO add your handling code here:
        if (jtf_maxStock.getText().equals("Max")) {
            jtf_maxStock.setText("");
        }
    }//GEN-LAST:event_jtf_maxStockFocusGained

    private void jtf_maxStockFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_maxStockFocusLost
        // TODO add your handling code here:
        if (jtf_maxStock.getText().equals("")) {
            jtf_maxStock.setText("Max");
        }
    }//GEN-LAST:event_jtf_maxStockFocusLost

    private void jtf_minPvpFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_minPvpFocusGained
        // TODO add your handling code here:
        if (jtf_minPvp.getText().equals("Min")) {
            jtf_minPvp.setText("");
        }
    }//GEN-LAST:event_jtf_minPvpFocusGained

    private void jtf_minPvpFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_minPvpFocusLost
        // TODO add your handling code here:
        if (jtf_minPvp.getText().equals("")) {
            jtf_minPvp.setText("Min");
        }
    }//GEN-LAST:event_jtf_minPvpFocusLost

    private void jtf_maxPvpFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_maxPvpFocusGained
        // TODO add your handling code here:
        if (jtf_maxPvp.getText().equals("Max")) {
            jtf_maxPvp.setText("");
        }
    }//GEN-LAST:event_jtf_maxPvpFocusGained

    private void jtf_maxPvpFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_maxPvpFocusLost
        // TODO add your handling code here:
        if (jtf_maxPvp.getText().equals("")) {
            jtf_maxPvp.setText("Max");
        }
    }//GEN-LAST:event_jtf_maxPvpFocusLost

    private void jtf_nombreLineaProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_nombreLineaProductoFocusGained
        // TODO add your handling code here:
        if (jtf_nombreLineaProducto.getText().equals("Linea de producto")) {
            jtf_nombreLineaProducto.setText("");
        }
    }//GEN-LAST:event_jtf_nombreLineaProductoFocusGained

    private void jtf_nombreLineaProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_nombreLineaProductoFocusLost
        // TODO add your handling code here:
        if (jtf_nombreLineaProducto.getText().equals("")) {
            jtf_nombreLineaProducto.setText("Linea de producto");
        }
    }//GEN-LAST:event_jtf_nombreLineaProductoFocusLost

    private void jtf_descripcionLineaProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_descripcionLineaProductoFocusGained
        // TODO add your handling code here:
        if (jtf_descripcionLineaProducto.getText().equals("Descripción")) {
            jtf_descripcionLineaProducto.setText("");
        }
    }//GEN-LAST:event_jtf_descripcionLineaProductoFocusGained

    private void jtf_descripcionLineaProductoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_descripcionLineaProductoFocusLost
        // TODO add your handling code here:
        if (jtf_descripcionLineaProducto.getText().equals("")) {
            jtf_descripcionLineaProducto.setText("Descripción");
        }
    }//GEN-LAST:event_jtf_descripcionLineaProductoFocusLost

    private void jtf_empresaProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_empresaProveedorFocusGained
        // TODO add your handling code here:
        if (jtf_empresaProveedor.getText().equals("Empresa")) {
            jtf_empresaProveedor.setText("");
        }
    }//GEN-LAST:event_jtf_empresaProveedorFocusGained

    private void jtf_empresaProveedorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_empresaProveedorFocusLost
        // TODO add your handling code here:
        if (jtf_empresaProveedor.getText().equals("")) {
            jtf_empresaProveedor.setText("Empresa");
        }
    }//GEN-LAST:event_jtf_empresaProveedorFocusLost

    private void jtf_contactoProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_contactoProveedorFocusGained
        // TODO add your handling code here:
        if (jtf_contactoProveedor.getText().equals("Contacto")) {
            jtf_contactoProveedor.setText("");
        }
    }//GEN-LAST:event_jtf_contactoProveedorFocusGained

    private void jtf_contactoProveedorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_contactoProveedorFocusLost
        // TODO add your handling code here:
        if (jtf_contactoProveedor.getText().equals("")) {
            jtf_contactoProveedor.setText("Contacto");
        }
    }//GEN-LAST:event_jtf_contactoProveedorFocusLost

    private void jtf_telefonoProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_telefonoProveedorFocusGained
        // TODO add your handling code here:
        if (jtf_telefonoProveedor.getText().equals("Teléfono")) {
            jtf_telefonoProveedor.setText("");
        }
    }//GEN-LAST:event_jtf_telefonoProveedorFocusGained

    private void jtf_telefonoProveedorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_telefonoProveedorFocusLost
        // TODO add your handling code here:
        if (jtf_telefonoProveedor.getText().equals("")) {
            jtf_telefonoProveedor.setText("Teléfono");
        }
    }//GEN-LAST:event_jtf_telefonoProveedorFocusLost

    private void jtf_paginaWebProveedorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_paginaWebProveedorFocusGained
        // TODO add your handling code here:
        if (jtf_paginaWebProveedor.getText().equals("Página web")) {
            jtf_paginaWebProveedor.setText("");
        }
    }//GEN-LAST:event_jtf_paginaWebProveedorFocusGained

    private void jtf_paginaWebProveedorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_paginaWebProveedorFocusLost
        // TODO add your handling code here:
        if (jtf_paginaWebProveedor.getText().equals("")) {
            jtf_paginaWebProveedor.setText("Página web");
        }
    }//GEN-LAST:event_jtf_paginaWebProveedorFocusLost

    private void jtf_clientePedidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_clientePedidoFocusGained
        // TODO add your handling code here:
        if (jtf_clientePedido.getText().equals("Cliente")) {
            jtf_clientePedido.setText("");
        }
    }//GEN-LAST:event_jtf_clientePedidoFocusGained

    private void jtf_clientePedidoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_clientePedidoFocusLost
        // TODO add your handling code here:
        if (jtf_clientePedido.getText().equals("")) {
            jtf_clientePedido.setText("Cliente");
        }
    }//GEN-LAST:event_jtf_clientePedidoFocusLost

    private void jtf_empleadoPedidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_empleadoPedidoFocusGained
        // TODO add your handling code here:
        if (jtf_empleadoPedido.getText().equals("Empleado")) {
            jtf_empleadoPedido.setText("");
        }
    }//GEN-LAST:event_jtf_empleadoPedidoFocusGained

    private void jtf_empleadoPedidoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_empleadoPedidoFocusLost
        // TODO add your handling code here: 
        if (jtf_empleadoPedido.getText().equals("")) {
            jtf_empleadoPedido.setText("Empleado");
        }
    }//GEN-LAST:event_jtf_empleadoPedidoFocusLost

    private void jtf_codigoPostalPedidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_codigoPostalPedidoFocusGained
        // TODO add your handling code here:
        if (jtf_codigoPostalPedido.getText().equals("Codigo Postal Pedido")) {
            jtf_codigoPostalPedido.setText("");
        }
    }//GEN-LAST:event_jtf_codigoPostalPedidoFocusGained

    private void jtf_codigoPostalPedidoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_codigoPostalPedidoFocusLost
        // TODO add your handling code here:
        if (jtf_codigoPostalPedido.getText().equals("")) {
            jtf_codigoPostalPedido.setText("Codigo Postal Pedido");
        }
    }//GEN-LAST:event_jtf_codigoPostalPedidoFocusLost

    private void jtf_direccionPedidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_direccionPedidoFocusGained
        // TODO add your handling code here:
        if (jtf_direccionPedido.getText().equals("Dirección")) {
            jtf_direccionPedido.setText("");
        }
    }//GEN-LAST:event_jtf_direccionPedidoFocusGained

    private void jtf_direccionPedidoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_direccionPedidoFocusLost
        // TODO add your handling code here:
        if (jtf_direccionPedido.getText().equals("")) {
            jtf_direccionPedido.setText("Dirección");
        }
    }//GEN-LAST:event_jtf_direccionPedidoFocusLost

    private void jmi_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmi_eliminarActionPerformed
        // TODO add your handling code here:
        eliminarTupla();
    }//GEN-LAST:event_jmi_eliminarActionPerformed

    private void jtf_clientePedido1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_clientePedido1FocusGained
        // TODO add your handling code here:
        if (jtf_clientePedido1.getText().equals("Cliente")) {
            jtf_clientePedido1.setText("");
        }
    }//GEN-LAST:event_jtf_clientePedido1FocusGained

    private void jtf_clientePedido1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_clientePedido1FocusLost
        // TODO add your handling code here:
        if (jtf_clientePedido1.getText().equals("")) {
            jtf_clientePedido1.setText("Cliente");
        }
    }//GEN-LAST:event_jtf_clientePedido1FocusLost

    private void jtf_empleadoPedido1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_empleadoPedido1FocusGained
        // TODO add your handling code here:
        if (jtf_empleadoPedido1.getText().equals("Empleado")) {
            jtf_empleadoPedido1.setText("");
        }
    }//GEN-LAST:event_jtf_empleadoPedido1FocusGained

    private void jtf_empleadoPedido1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_empleadoPedido1FocusLost
        // TODO add your handling code here:
        if (jtf_empleadoPedido1.getText().equals("")) {
            jtf_empleadoPedido1.setText("Empleado");
        }
    }//GEN-LAST:event_jtf_empleadoPedido1FocusLost

    private void jtf_codigoPostalPedido1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_codigoPostalPedido1FocusGained
        // TODO add your handling code here:
        if (jtf_codigoPostalPedido1.getText().equals("Codigo Postal Pedido")) {
            jtf_codigoPostalPedido1.setText("");
        }
    }//GEN-LAST:event_jtf_codigoPostalPedido1FocusGained

    private void jtf_codigoPostalPedido1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_codigoPostalPedido1FocusLost
        // TODO add your handling code here:
        if (jtf_codigoPostalPedido1.getText().equals("")) {
            jtf_codigoPostalPedido1.setText("Codigo Postal Pedido");
        }
    }//GEN-LAST:event_jtf_codigoPostalPedido1FocusLost

    private void jtf_añoPedido1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_añoPedido1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jtf_añoPedido1ActionPerformed

    private void jtf_direccionPedido1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_direccionPedido1FocusGained
        // TODO add your handling code here:
        if (jtf_direccionPedido1.getText().equals("Dirección")) {
            jtf_direccionPedido1.setText("");
        }

    }//GEN-LAST:event_jtf_direccionPedido1FocusGained

    private void jtf_direccionPedido1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_direccionPedido1FocusLost
        // TODO add your handling code here:
        if (jtf_direccionPedido1.getText().equals("")) {
            jtf_direccionPedido1.setText("Dirección");
        }
    }//GEN-LAST:event_jtf_direccionPedido1FocusLost

    private void jtf_empresaProveedor1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_empresaProveedor1FocusGained
        // TODO add your handling code here:
        if (jtf_empresaProveedor1.getText().equals("Empresa")) {
            jtf_empresaProveedor1.setText("");
        }
    }//GEN-LAST:event_jtf_empresaProveedor1FocusGained

    private void jtf_empresaProveedor1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_empresaProveedor1FocusLost
        // TODO add your handling code here:
        if (jtf_empresaProveedor1.getText().equals("")) {
            jtf_empresaProveedor1.setText("Empresa");
        }
    }//GEN-LAST:event_jtf_empresaProveedor1FocusLost

    private void jtf_contactoProveedor1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_contactoProveedor1FocusGained
        // TODO add your handling code here:
        if (jtf_contactoProveedor1.getText().equals("Contacto")) {
            jtf_contactoProveedor1.setText("");
        }
    }//GEN-LAST:event_jtf_contactoProveedor1FocusGained

    private void jtf_contactoProveedor1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_contactoProveedor1FocusLost
        // TODO add your handling code here:
        if (jtf_contactoProveedor1.getText().equals("")) {
            jtf_contactoProveedor1.setText("Contacto");
        }
    }//GEN-LAST:event_jtf_contactoProveedor1FocusLost

    private void jtf_telefonoProveedor1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_telefonoProveedor1FocusGained
        // TODO add your handling code here:
        if (jtf_telefonoProveedor1.getText().equals("Teléfono")) {
            jtf_telefonoProveedor1.setText("");
        }
    }//GEN-LAST:event_jtf_telefonoProveedor1FocusGained

    private void jtf_telefonoProveedor1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_telefonoProveedor1FocusLost
        // TODO add your handling code here:
        if (jtf_telefonoProveedor1.getText().equals("")) {
            jtf_telefonoProveedor1.setText("Teléfono");
        }
    }//GEN-LAST:event_jtf_telefonoProveedor1FocusLost

    private void jtf_paginaWebProveedor1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_paginaWebProveedor1FocusGained
        // TODO add your handling code here:
        if (jtf_paginaWebProveedor1.getText().equals("Página web")) {
            jtf_paginaWebProveedor1.setText("");
        }
    }//GEN-LAST:event_jtf_paginaWebProveedor1FocusGained

    private void jtf_paginaWebProveedor1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_paginaWebProveedor1FocusLost
        // TODO add your handling code here:
        if (jtf_paginaWebProveedor1.getText().equals("")) {
            jtf_paginaWebProveedor1.setText("Página web");
        }
    }//GEN-LAST:event_jtf_paginaWebProveedor1FocusLost

    private void jtf_nombreLineaProducto1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_nombreLineaProducto1FocusGained
        // TODO add your handling code here:
        if (jtf_nombreLineaProducto1.getText().equals("Linea de producto")) {
            jtf_nombreLineaProducto1.setText("");
        }
    }//GEN-LAST:event_jtf_nombreLineaProducto1FocusGained

    private void jtf_nombreLineaProducto1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_nombreLineaProducto1FocusLost
        // TODO add your handling code here:
        if (jtf_nombreLineaProducto1.getText().equals("")) {
            jtf_nombreLineaProducto1.setText("Linea de producto");
        }
    }//GEN-LAST:event_jtf_nombreLineaProducto1FocusLost

    private void jtf_descripcionLineaProducto1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_descripcionLineaProducto1FocusGained
        // TODO add your handling code here:
        if (jtf_descripcionLineaProducto1.getText().equals("Descripción")) {
            jtf_descripcionLineaProducto1.setText("");
        }
    }//GEN-LAST:event_jtf_descripcionLineaProducto1FocusGained

    private void jtf_descripcionLineaProducto1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtf_descripcionLineaProducto1FocusLost
        // TODO add your handling code here:
        if (jtf_descripcionLineaProducto1.getText().equals("")) {
            jtf_descripcionLineaProducto1.setText("Descripción");
        }
    }//GEN-LAST:event_jtf_descripcionLineaProducto1FocusLost

    private void jtf_stock1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_stock1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtf_stock1ActionPerformed

    private void jmi_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmi_modificarActionPerformed
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Productos")) {

            // TODO add your handling code here:  
            jpane_productos2.setVisible(true);
            String[] datos = new String[7];
            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                datos[i] = jTable1.getValueAt(jTable1.getSelectedRow(), i).toString();
            }
            jtf_idproducto2.setText(datos[0]);
            jtf_producto2.setText(datos[1]);
            jcb_lineaProducto2.setSelectedItem(datos[2]);
            jtf_descripcionProducto2.setText(datos[3]);
            jtf_stock1.setText(datos[4]);
            jtf_pvp1.setText(datos[5]);
            jcb_proveedorProducto2.setSelectedItem(datos[6]);
            jDialog_ModificarTupla.show();
        }

        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Lineas de producto")) {

            // TODO add your handling code here:
            jpane_lineas2.setVisible(true);
            String[] datos = new String[2];
            for (int i = 0; i < jTable2.getColumnCount(); i++) {
                datos[i] = jTable2.getValueAt(jTable2.getSelectedRow(), i).toString();
            }
            String id = datos[0];
            jtf_nombreLineaProducto1.setText(datos[0]);
            jtf_descripcionLineaProducto1.setText(datos[1]);

        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Proveedores")) {

            jpane_proveedores2.setVisible(true);
            String[] datos = new String[6];
            for (int i = 0; i < jTable3.getColumnCount(); i++) {
                datos[i] = jTable3.getValueAt(jTable3.getSelectedRow(), i).toString();
            }
            int id = Integer.valueOf(datos[0]);
            jtf_empresaProveedor1.setText(datos[1]);
            jtf_contactoProveedor1.setText(datos[2]);
            jcb_ciudadProveedor1.setSelectedItem(datos[3]);
            jtf_telefonoProveedor1.setText(datos[4]);
            jtf_paginaWebProveedor1.setText(datos[5]);
        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Pedidos")) {

            int id = Integer.valueOf((jTable4.getValueAt(jTable4.getSelectedRow(), 0)).toString());
            jpane_pedidos1.setVisible(true);

        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Empleados")) {

            int id = Integer.valueOf((jTable5.getValueAt(jTable5.getSelectedRow(), 0)).toString());

        }
        if (jtp_main.getTitleAt(jtp_main.getSelectedIndex()).equals("Empresas de envio")) {

            int id = Integer.valueOf((jTable6.getValueAt(jTable6.getSelectedRow(), 0)).toString());

        }


    }//GEN-LAST:event_jmi_modificarActionPerformed

    private void jb_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_modificarActionPerformed
        // TODO add your handling code here:
        String[] datos = {
            jtf_idproducto2.getText(),
            jtf_producto2.getText(),
            jcb_lineaProducto2.getSelectedItem().toString(),
            jtf_descripcionProducto2.getText(),
            jtf_stock1.getText(),
            jtf_pvp1.getText(),
            jcb_proveedorProducto2.getSelectedItem().toString()
        };
       conex.ModificarTupla(datos);
       actualizarTablas();
    }//GEN-LAST:event_jb_modificarActionPerformed
    /*FIN Ocultar o mostrar al ganar o perder el foco de los jTextField*/
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
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainApp("User", true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField barraBusqueda;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JDialog jDialog_BusquedaAvanzada;
    private javax.swing.JDialog jDialog_InsertarTupla;
    private javax.swing.JDialog jDialog_ModificarTupla;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JButton jb_buscar;
    private javax.swing.JButton jb_buscar1;
    private javax.swing.JButton jb_modificar;
    private javax.swing.JComboBox<String> jcb_ciudadPedido;
    private javax.swing.JComboBox<String> jcb_ciudadPedido1;
    private javax.swing.JComboBox<String> jcb_ciudadProveedor;
    private javax.swing.JComboBox<String> jcb_ciudadProveedor1;
    private javax.swing.JComboBox<String> jcb_lineaProducto;
    private javax.swing.JComboBox<String> jcb_lineaProducto1;
    private javax.swing.JComboBox<String> jcb_lineaProducto2;
    private javax.swing.JComboBox<String> jcb_proveedorProducto;
    private javax.swing.JComboBox<String> jcb_proveedorProducto1;
    private javax.swing.JComboBox<String> jcb_proveedorProducto2;
    private javax.swing.JComboBox<String> jcb_tabla;
    private javax.swing.JComboBox<String> jcb_tabla1;
    private javax.swing.JMenu jm_insertar;
    private javax.swing.JMenu jm_usuario;
    private javax.swing.JMenuItem jmi_cerrarSesion;
    private javax.swing.JMenuItem jmi_eliminar;
    private javax.swing.JMenuItem jmi_insertar;
    private javax.swing.JMenuItem jmi_modificar;
    private javax.swing.JMenuItem jmi_nuevoPedido;
    private javax.swing.JMenuItem jmi_perfil;
    private javax.swing.JPanel jpane_lineas;
    private javax.swing.JPanel jpane_lineas1;
    private javax.swing.JPanel jpane_lineas2;
    private javax.swing.JPanel jpane_pedidos;
    private javax.swing.JPanel jpane_pedidos1;
    private javax.swing.JPanel jpane_productos;
    private javax.swing.JPanel jpane_productos1;
    private javax.swing.JPanel jpane_productos2;
    private javax.swing.JPanel jpane_proveedores;
    private javax.swing.JPanel jpane_proveedores1;
    private javax.swing.JPanel jpane_proveedores2;
    private javax.swing.JTextArea jta_descripcionProducto;
    private javax.swing.JTextField jtf_EnStock;
    private javax.swing.JTextField jtf_Pvp;
    private javax.swing.JTextField jtf_añoPedido;
    private javax.swing.JTextField jtf_añoPedido1;
    private javax.swing.JTextField jtf_clientePedido;
    private javax.swing.JTextField jtf_clientePedido1;
    private javax.swing.JTextField jtf_codigoPostalPedido;
    private javax.swing.JTextField jtf_codigoPostalPedido1;
    private javax.swing.JTextField jtf_contactoProveedor;
    private javax.swing.JTextField jtf_contactoProveedor1;
    private javax.swing.JTextField jtf_descripcionLineaProducto;
    private javax.swing.JTextField jtf_descripcionLineaProducto1;
    private javax.swing.JTextField jtf_descripcionProducto2;
    private javax.swing.JTextField jtf_diaPedido;
    private javax.swing.JTextField jtf_diaPedido1;
    private javax.swing.JTextField jtf_direccionPedido;
    private javax.swing.JTextField jtf_direccionPedido1;
    private javax.swing.JTextField jtf_empleadoPedido;
    private javax.swing.JTextField jtf_empleadoPedido1;
    private javax.swing.JTextField jtf_empresaEnvioPedido;
    private javax.swing.JTextField jtf_empresaEnvioPedido1;
    private javax.swing.JTextField jtf_empresaProveedor;
    private javax.swing.JTextField jtf_empresaProveedor1;
    private javax.swing.JTextField jtf_idproducto2;
    private javax.swing.JTextField jtf_maxPvp;
    private javax.swing.JTextField jtf_maxStock;
    private javax.swing.JTextField jtf_mesPedido;
    private javax.swing.JTextField jtf_mesPedido1;
    private javax.swing.JTextField jtf_minPvp;
    private javax.swing.JTextField jtf_minStock;
    private javax.swing.JTextField jtf_nombreLineaProducto;
    private javax.swing.JTextField jtf_nombreLineaProducto1;
    private javax.swing.JTextField jtf_paginaWebProveedor;
    private javax.swing.JTextField jtf_paginaWebProveedor1;
    private javax.swing.JTextField jtf_producto;
    private javax.swing.JTextField jtf_producto1;
    private javax.swing.JTextField jtf_producto2;
    private javax.swing.JTextField jtf_pvp1;
    private javax.swing.JTextField jtf_stock1;
    private javax.swing.JTextField jtf_telefonoProveedor;
    private javax.swing.JTextField jtf_telefonoProveedor1;
    private javax.swing.JTabbedPane jtp_main;
    // End of variables declaration//GEN-END:variables
}
