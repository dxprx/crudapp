/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import clases.*;
import com.sun.jdi.connect.spi.Connection;
import componentes.NonEditableModel;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author oscar
 */
public class Aplicacion extends javax.swing.JFrame {

    private static Conexion conexion = Conexion.getInstance();
    private List<Producto> productos_data = new ArrayList<>();
    private List<LineaProducto> lineasproductos_data = new ArrayList<>();
    private List<Proveedor> proveedores_data = new ArrayList<>();
    private List<Cliente> clientes_data = new ArrayList<>();
    private List<Empleado> empleados_data = new ArrayList<>();
    private List<DetallesPedido> detallespedidos_data = new ArrayList<>();
    private List<Pedido> pedidos_data = new ArrayList<>();
    private List<Ciudad> ciudades_data = new ArrayList<>();

    private NonEditableModel dtm_productos = new NonEditableModel();
    private NonEditableModel dtm_pedidos = new NonEditableModel();
    private NonEditableModel dtm_detallespedido = new NonEditableModel();
    private NonEditableModel dtm_empleados = new NonEditableModel();
    private NonEditableModel dtm_clientes = new NonEditableModel();
    private NonEditableModel dtm_proveedores = new NonEditableModel();
    private NonEditableModel dtm_lineasproductos = new NonEditableModel();

    private DefaultComboBoxModel dcbm_ciudades = new DefaultComboBoxModel();
    

    private DefaultTableCellRenderer rowRendererProductos = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Producto producto = productos_data.get(row);

            // Cambiar el color de fondo de la fila si está eliminada
            if (producto.isEliminado()) {
                component.setBackground(Color.RED);
            } else if (isSelected) {
                component.setBackground(Color.BLUE);
            } else if (producto.getStock() == 0) {
                component.setBackground(Color.ORANGE);
            } else {
                component.setBackground(table.getBackground());
            }

            return component;
        }
    };

    private DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Cambiar el color de fondo de la fila si está eliminada
            if (isSelected) {
                component.setBackground(Color.BLUE);
            } else {
                component.setBackground(table.getBackground());
            }

            return component;
        }
    };

    /**
     * Creates new form main
     *
     * @throws java.sql.SQLException
     */
    public Aplicacion() throws SQLException {
        initComponents();
        getCiudades();
        mostrarTablas();
        eventos();

    }

    public void actualizarTablas() {
        dtm_productos = new NonEditableModel();
        dtm_clientes = new NonEditableModel();
        dtm_detallespedido = new NonEditableModel();
        dtm_empleados = new NonEditableModel();
        dtm_lineasproductos = new NonEditableModel();
        dtm_pedidos = new NonEditableModel();
        dtm_proveedores = new NonEditableModel();

        mostrarTablas();
    }

    private void mostrarTablas() {
        mostrarProductos();
        mostrarProveedores();
        mostrarLineasProductos();
        mostrarClientes();
        mostrarEmpleados();
        mostrarPedidos();
        estilosTablas();

        comboboxModelos();
    }

    public void estilosTablas() {

        tabla_productos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_proveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_clientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_empleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_lineasproductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_pedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (int i = 0; i < tabla_productos.getColumnCount(); i++) {
            tabla_productos.getColumnModel().getColumn(i).setCellRenderer(rowRendererProductos);
        }
        for (int i = 0; i < tabla_proveedores.getColumnCount(); i++) {
            tabla_proveedores.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
        for (int i = 0; i < tabla_clientes.getColumnCount(); i++) {
            tabla_clientes.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
        for (int i = 0; i < tabla_empleados.getColumnCount(); i++) {
            tabla_empleados.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
        for (int i = 0; i < tabla_lineasproductos.getColumnCount(); i++) {
            tabla_lineasproductos.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
        for (int i = 0; i < tabla_pedidos.getColumnCount(); i++) {
            tabla_pedidos.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
    }

    private void eventos() {
        dobleClickTablaProductos();
    }

    private void dobleClickTablaProductos() {

        tabla_productos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int selectedRow = tabla_productos.getSelectedRow();
                    if (selectedRow != -1) {
                        // Obtener los datos de la fila seleccionada
                        int id = (int) dtm_productos.getValueAt(selectedRow, 0);
                        String nombre = (String) dtm_productos.getValueAt(selectedRow, 1);
                        String linea = (String) dtm_productos.getValueAt(selectedRow, 2);
                        String descripcion = (String) dtm_productos.getValueAt(selectedRow, 3);
                        int stock = (int) dtm_productos.getValueAt(selectedRow, 4);
                        float pvp = (float) dtm_productos.getValueAt(selectedRow, 5);
                        String proveedor = (String) dtm_productos.getValueAt(selectedRow, 6);
                        Object[] data = {id, nombre, linea, descripcion, stock, pvp, proveedor};
                        // Realizar la acción deseada
                        for (int i = 0; i < data.length; i++) {

                            System.out.print(data[i]);
                        }
                    }
                }
            }
        });
    }

    private void comboboxModelos() {

        jComboBox2.removeAllItems();
        DefaultComboBoxModel dcbm_proveedores = new DefaultComboBoxModel();
        jComboBox2.setModel(dcbm_proveedores);
        for (Proveedor proveedor : proveedores_data) {
            jComboBox2.addItem(proveedor.getNombreEmpresa());
        }

        jComboBox1.removeAllItems();
        DefaultComboBoxModel dcbm_lineas = new DefaultComboBoxModel();
        jComboBox1.setModel(dcbm_lineas);
        for (LineaProducto linea : lineasproductos_data) {
            jComboBox1.addItem(linea.getLinea());
        }

        
        
        DefaultComboBoxModel dcbm_comunidades = new DefaultComboBoxModel();
        dcbm_comunidades.removeAllElements();
        jComboBox3.setModel(dcbm_comunidades);
        jComboBox7.setModel(dcbm_comunidades);
        for (Ciudad comunidad : ciudades_data) {
            if (dcbm_comunidades.getIndexOf(comunidad.getNombreComunidad()) == -1) {
                System.out.println(comunidad.getNombreComunidad());
                dcbm_comunidades.addElement(comunidad.getNombreComunidad());
                
            }
        }

    }

    private void getCiudades() {
        try {
            conexion.conectar();
            ciudades_data = conexion.seleccionarCiudades();
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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
            boolean eliminado = producto.isEliminado();

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
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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

        jframe_modificar_productos = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        modificar_producto = new javax.swing.JButton();
        jframe_modificar_proveedores = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        modificar_proveedor = new javax.swing.JButton();
        jframe_modificar_lineas = new javax.swing.JFrame();
        jPanel3 = new javax.swing.JPanel();
        jTextField9 = new javax.swing.JTextField();
        modificar_linea = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jframe_modificar_clientes = new javax.swing.JFrame();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jComboBox7 = new javax.swing.JComboBox<>();
        jComboBox8 = new javax.swing.JComboBox<>();
        jTextField16 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        modificar_cliente = new javax.swing.JButton();
        jframe_modificar_empleados = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        modificar_empleado = new javax.swing.JButton();
        jframe_modificar_pedidos = new javax.swing.JFrame();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jComboBox11 = new javax.swing.JComboBox<>();
        jComboBox12 = new javax.swing.JComboBox<>();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        modificar_proveedor4 = new javax.swing.JButton();
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
        insertar = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        modificar = new javax.swing.JButton();

        jframe_modificar_productos.setSize(new java.awt.Dimension(500, 500));

        jLabel1.setText("ID: X");

        modificar_producto.setText("Modificar");
        modificar_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_productoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField1)
                                .addComponent(jComboBox1, 0, 191, Short.MAX_VALUE)
                                .addComponent(jTextField2)
                                .addComponent(jTextField3)
                                .addComponent(jTextField4)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(modificar_producto)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(modificar_producto)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jframe_modificar_productosLayout = new javax.swing.GroupLayout(jframe_modificar_productos.getContentPane());
        jframe_modificar_productos.getContentPane().setLayout(jframe_modificar_productosLayout);
        jframe_modificar_productosLayout.setHorizontalGroup(
            jframe_modificar_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_modificar_productosLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jframe_modificar_productosLayout.setVerticalGroup(
            jframe_modificar_productosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_modificar_productosLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jframe_modificar_proveedores.setSize(new java.awt.Dimension(500, 500));

        jLabel2.setText("ID: X");

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        modificar_proveedor.setText("Modificar");
        modificar_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_proveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jTextField5)
                            .addComponent(jComboBox3, 0, 269, Short.MAX_VALUE)
                            .addComponent(jTextField6)
                            .addComponent(jTextField7)
                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField8)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(modificar_proveedor)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(modificar_proveedor)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jframe_modificar_proveedoresLayout = new javax.swing.GroupLayout(jframe_modificar_proveedores.getContentPane());
        jframe_modificar_proveedores.getContentPane().setLayout(jframe_modificar_proveedoresLayout);
        jframe_modificar_proveedoresLayout.setHorizontalGroup(
            jframe_modificar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframe_modificar_proveedoresLayout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        jframe_modificar_proveedoresLayout.setVerticalGroup(
            jframe_modificar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframe_modificar_proveedoresLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );

        jframe_modificar_lineas.setSize(new java.awt.Dimension(500, 500));

        modificar_linea.setText("Modificar");
        modificar_linea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_lineaActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane7.setViewportView(jTextArea1);

        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(166, 166, 166)
                            .addComponent(modificar_linea))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(71, 71, 71)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(modificar_linea)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jframe_modificar_lineasLayout = new javax.swing.GroupLayout(jframe_modificar_lineas.getContentPane());
        jframe_modificar_lineas.getContentPane().setLayout(jframe_modificar_lineasLayout);
        jframe_modificar_lineasLayout.setHorizontalGroup(
            jframe_modificar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframe_modificar_lineasLayout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        jframe_modificar_lineasLayout.setVerticalGroup(
            jframe_modificar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframe_modificar_lineasLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );

        jframe_modificar_clientes.setSize(new java.awt.Dimension(500, 500));

        jLabel4.setText("ID: X");

        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        modificar_cliente.setText("Modificar");
        modificar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_clienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jComboBox8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4)
                                    .addComponent(jTextField13)
                                    .addComponent(jTextField14)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField10)
                                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(modificar_cliente)))
                .addContainerGap(165, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(modificar_cliente)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jframe_modificar_clientesLayout = new javax.swing.GroupLayout(jframe_modificar_clientes.getContentPane());
        jframe_modificar_clientes.getContentPane().setLayout(jframe_modificar_clientesLayout);
        jframe_modificar_clientesLayout.setHorizontalGroup(
            jframe_modificar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_modificar_clientesLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jframe_modificar_clientesLayout.setVerticalGroup(
            jframe_modificar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_modificar_clientesLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        jframe_modificar_empleados.setSize(new java.awt.Dimension(500, 500));

        jLabel5.setText("ID: X");

        modificar_empleado.setText("Modificar");
        modificar_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_empleadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                            .addComponent(jTextField18)
                            .addComponent(jTextField19)
                            .addComponent(jTextField20)
                            .addComponent(jTextField26)
                            .addComponent(jTextField25)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(modificar_empleado)))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(modificar_empleado)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jframe_modificar_empleadosLayout = new javax.swing.GroupLayout(jframe_modificar_empleados.getContentPane());
        jframe_modificar_empleados.getContentPane().setLayout(jframe_modificar_empleadosLayout);
        jframe_modificar_empleadosLayout.setHorizontalGroup(
            jframe_modificar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_modificar_empleadosLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(288, Short.MAX_VALUE))
        );
        jframe_modificar_empleadosLayout.setVerticalGroup(
            jframe_modificar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_modificar_empleadosLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        jframe_modificar_pedidos.setSize(new java.awt.Dimension(500, 500));

        jLabel6.setText("ID: X");

        jComboBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox11ActionPerformed(evt);
            }
        });

        jComboBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox12ActionPerformed(evt);
            }
        });

        modificar_proveedor4.setText("Modificar");
        modificar_proveedor4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_proveedor4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(jTextField21)
                            .addComponent(jComboBox11, 0, 269, Short.MAX_VALUE)
                            .addComponent(jTextField22)
                            .addComponent(jTextField23)
                            .addComponent(jComboBox12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField24)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(modificar_proveedor4)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(modificar_proveedor4)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jframe_modificar_pedidosLayout = new javax.swing.GroupLayout(jframe_modificar_pedidos.getContentPane());
        jframe_modificar_pedidos.getContentPane().setLayout(jframe_modificar_pedidosLayout);
        jframe_modificar_pedidosLayout.setHorizontalGroup(
            jframe_modificar_pedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframe_modificar_pedidosLayout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        jframe_modificar_pedidosLayout.setVerticalGroup(
            jframe_modificar_pedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframe_modificar_pedidosLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabla_productos.setAutoCreateRowSorter(true);
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

        insertar.setText("Insertar");

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        modificar.setText("Modificar");
        modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(124, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 898, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(insertar)
                        .addGap(182, 182, 182)
                        .addComponent(eliminar)
                        .addGap(212, 212, 212)
                        .addComponent(modificar)
                        .addGap(206, 206, 206))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertar)
                    .addComponent(eliminar)
                    .addComponent(modificar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 659, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarActionPerformed
        // TODO add your handling code here:
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Productos")) {
            Object[] datos = new Object[7];

            for (int i = 0; i < tabla_productos.getColumnCount(); i++) {
                datos[i] = dtm_productos.getValueAt(tabla_productos.getSelectedRow(), i);
            }
            if (datos[0] != null) {
                jLabel1.setText("ID : " + datos[0].toString());
            }
            if (datos[1] != null) {
                jTextField1.setText(datos[1].toString());
            }
            jComboBox1.setSelectedItem(datos[2].toString());
            if (datos[3] != null) {
                jTextField2.setText(datos[3].toString());
            }
            if (datos[4] != null) {
                jTextField3.setText(datos[4].toString());
            }
            if (datos[5] != null) {
                jTextField4.setText(datos[5].toString());
            }
            jComboBox2.setSelectedItem(datos[6].toString());

            jframe_modificar_productos.show();
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Proveedores")) {
            Object[] datos = new Object[7];

            for (int i = 0; i < tabla_proveedores.getColumnCount(); i++) {
                datos[i] = dtm_proveedores.getValueAt(tabla_proveedores.getSelectedRow(), i);
            }
            if (datos[0] != null) {
                jLabel2.setText("ID : " + datos[0].toString());
            }
            if (datos[1] != null) {
                jTextField5.setText(datos[1].toString());
            }
            if (datos[2] != null) {
                jTextField6.setText(datos[2].toString());
            }
            jComboBox3.setSelectedItem(datos[3].toString());
            jComboBox4.setSelectedItem(datos[4].toString());
            if (datos[5] != null) {
                jTextField7.setText(datos[5].toString());
            }
            if (datos[6] != null) {
                jTextField8.setText(datos[6].toString());
            }

            jframe_modificar_proveedores.show();
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Líneas productos")) {
            Object[] datos = new Object[2];
            for (int i = 0; i < tabla_lineasproductos.getColumnCount(); i++) {
                datos[i] = dtm_lineasproductos.getValueAt(tabla_lineasproductos.getSelectedRow(), i);
            }
            jLabel3.setText(datos[0].toString());
            if (datos[0] != null) {
                jTextField9.setText(datos[0].toString());
            }
            if (datos[1] != null) {
                jTextArea1.setText(datos[1].toString());
            }

            jframe_modificar_lineas.show();
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes")) {
            Object[] datos = new Object[8];

            for (int i = 0; i < tabla_clientes.getColumnCount(); i++) {
                datos[i] = dtm_clientes.getValueAt(tabla_clientes.getSelectedRow(), i);
            }
            if (datos[0] != null) {
                jLabel4.setText("ID : " + datos[0].toString());
            }
            if (datos[1] != null) {
                jTextField13.setText(datos[1].toString());
            }
            if (datos[2] != null) {
                jTextField14.setText(datos[2].toString());
            }
            if (datos[3] != null) {
                jTextField15.setText(datos[3].toString());
            }
            jComboBox7.setSelectedItem(datos[4].toString());
            jComboBox8.setSelectedItem(datos[5].toString());
            if (datos[6] != null) {
                jTextField16.setText(datos[6].toString());
            }
            if (datos[7] != null) {
                jTextField10.setText(datos[7].toString());
            }

            jframe_modificar_clientes.show();
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Empleados")) {

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Pedidos")) {

        }
    }//GEN-LAST:event_modificarActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        // TODO add your handling code here:

        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Productos")) {
            if (tabla_productos.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    int id = (int) dtm_productos.getValueAt(tabla_productos.getSelectedRow(), 0);
                    conexion.conectar();
                    conexion.eliminarProducto(id);
                    conexion.desconectar();
                    actualizarTablas();
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Proveedores")) {
            if (tabla_proveedores.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    int id = (int) dtm_proveedores.getValueAt(tabla_proveedores.getSelectedRow(), 0);
                    conexion.conectar();
                    conexion.eliminarProveedor(id);
                    conexion.desconectar();
                    actualizarTablas();
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Líneas productos")) {
            if (tabla_lineasproductos.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    String id = (String) dtm_lineasproductos.getValueAt(tabla_lineasproductos.getSelectedRow(), 0);
                    conexion.conectar();
                    conexion.eliminarLineaProducto(id);
                    conexion.desconectar();
                    actualizarTablas();
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        /*if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes")) {
            if (tabla_clientes.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    int id = (int) dtm_clientes.getValueAt(tabla_clientes.getSelectedRow(), 0);
                    conexion.conectar();
                    conexion.eliminarCliente(id);
                    conexion.desconectar();
                    actualizarTablas();
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }*/
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Empleados")) {
            if (tabla_empleados.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    int id = (int) dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), 0);
                    conexion.conectar();
                    conexion.eliminarEmpleado(id);
                    conexion.desconectar();
                    actualizarTablas();
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        /*if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Pedidos")) {
            if (tabla_pedidos.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    int id = (int) dtm_pedidos.getValueAt(tabla_pedidos.getSelectedRow(), 0);
                    conexion.conectar();
                    conexion.eliminarPedido(id);
                    conexion.desconectar();
                    actualizarTablas();
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }*/
    }//GEN-LAST:event_eliminarActionPerformed

    private void modificar_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_productoActionPerformed
        try {
            // TODO add your handling code here:

            Producto producto = new Producto(
                    Integer.parseInt(jLabel1.getText().substring(5)),
                    jTextField1.getText(),
                    lineasproductos_data.get(jComboBox1.getSelectedIndex()),
                    jTextField2.getText(), Integer.parseInt(jTextField3.getText()),
                    Float.parseFloat(jTextField4.getText()),
                    proveedores_data.get(jComboBox2.getSelectedIndex()),
                    false
            );
            conexion.conectar();
            conexion.actualizarProducto(producto);
            conexion.desconectar();
            actualizarTablas();
            jframe_modificar_productos.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_modificar_productoActionPerformed

    private void modificar_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_proveedorActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            System.out.println(dcbm_ciudades.getSelectedItem().toString());
            Ciudad ciudad = new Ciudad();
            ciudad.setNombreCiudad(dcbm_ciudades.getSelectedItem().toString());
            Proveedor proveedor = new Proveedor(
                    Integer.parseInt(jLabel2.getText().substring(5)),
                    jTextField5.getText(),
                    jTextField6.getText(),
                    ciudades_data.get(ciudades_data.indexOf(ciudad)),
                    jTextField7.getText(),
                    jTextField8.getText()
            );
            conexion.conectar();
            conexion.actualizarProveedor(proveedor);
            conexion.desconectar();
            actualizarTablas();
            jframe_modificar_proveedores.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_modificar_proveedorActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed

        // TODO add your handling code here:
        jComboBox4.removeAllItems();
        jComboBox4.setModel(dcbm_ciudades);
        System.out.println("rpueab");

        for (Ciudad ciudad : ciudades_data) {
            if (jComboBox3.getSelectedItem().equals(ciudad.getNombreComunidad())) {
                jComboBox4.addItem(ciudad.getNombreCiudad());
                System.out.println("xd");
            }
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void modificar_lineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_lineaActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            String id = jLabel3.getText();
            LineaProducto linea = new LineaProducto(
                    jTextField9.getText(),
                    jTextArea1.getText()
            );
            conexion.conectar();
            conexion.actualizarLineasProducto(linea, id);
            conexion.desconectar();
            actualizarTablas();
            jframe_modificar_lineas.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_modificar_lineaActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        // TODO add your handling code here:

        jComboBox8.removeAllItems();
        jComboBox8.setModel(dcbm_ciudades);

        for (Ciudad ciudad : ciudades_data) {
            if (jComboBox7.getSelectedItem().equals(ciudad.getNombreComunidad())) {
                jComboBox8.addItem(ciudad.getNombreCiudad());
            }
        }
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void modificar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_clienteActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            String id = jLabel4.getText();
            Ciudad ciudad = new Ciudad();
            ciudad.setNombreCiudad(dcbm_ciudades.getSelectedItem().toString());
            Cliente cliente = new Cliente(
                    Integer.parseInt(jLabel4.getText().substring(5)),
                    jTextField13.getText(),
                    jTextField14.getText(),
                    jTextField15.getText(),
                    ciudades_data.get(ciudades_data.indexOf(ciudad)),
                    jTextField16.getText(),
                    jTextField10.getText());
            conexion.conectar();
            conexion.actualizarCliente(cliente);
            conexion.desconectar();
            actualizarTablas();
            jframe_modificar_clientes.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_modificar_clienteActionPerformed

    private void modificar_empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_empleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modificar_empleadoActionPerformed

    private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11ActionPerformed

    private void jComboBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox12ActionPerformed

    private void modificar_proveedor4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_proveedor4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modificar_proveedor4ActionPerformed

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
            java.util.logging.Logger.getLogger(Aplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Aplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Aplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Aplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Aplicacion().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton eliminar;
    private javax.swing.JButton insertar;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JFrame jframe_modificar_clientes;
    private javax.swing.JFrame jframe_modificar_empleados;
    private javax.swing.JFrame jframe_modificar_lineas;
    private javax.swing.JFrame jframe_modificar_pedidos;
    private javax.swing.JFrame jframe_modificar_productos;
    private javax.swing.JFrame jframe_modificar_proveedores;
    private javax.swing.JButton modificar;
    private javax.swing.JButton modificar_cliente;
    private javax.swing.JButton modificar_empleado;
    private javax.swing.JButton modificar_linea;
    private javax.swing.JButton modificar_producto;
    private javax.swing.JButton modificar_proveedor;
    private javax.swing.JButton modificar_proveedor4;
    private javax.swing.JTable tabla_clientes;
    private javax.swing.JTable tabla_empleados;
    private javax.swing.JTable tabla_lineasproductos;
    private javax.swing.JTable tabla_pedidos;
    private javax.swing.JTable tabla_productos;
    private javax.swing.JTable tabla_proveedores;
    // End of variables declaration//GEN-END:variables
}
