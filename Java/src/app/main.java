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
    public main() throws SQLException {
        initComponents();
        mostrarTablas();
        getCiudades();
        eventos();
        comboboxModelos();

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

        for (Proveedor proveedor : proveedores_data) {
            jComboBox2.addItem(proveedor.getNombreEmpresa());
        }

        for (LineaProducto linea : lineasproductos_data) {
            jComboBox1.addItem(linea.getLinea());
        }
        DefaultComboBoxModel dcbm_comunidades = new DefaultComboBoxModel();
        jComboBox3.setModel(dcbm_comunidades);
        for (Ciudad comunidad : ciudades_data) {
            if (dcbm_comunidades.getIndexOf(comunidad.getNombreComunidad()) == -1) {

                jComboBox3.addItem(comunidad.getNombreComunidad());
            }
        }

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

        jFrame1 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        modificar_producto = new javax.swing.JButton();
        jFrame2 = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        modificar_proveedor = new javax.swing.JButton();
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

        jFrame1.setSize(new java.awt.Dimension(500, 500));

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

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jFrame2.setSize(new java.awt.Dimension(500, 500));

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

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame2Layout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame2Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

            jFrame1.show();
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
            jComboBox2.setSelectedItem(datos[4].toString());
            if (datos[5] != null) {
                jTextField7.setText(datos[5].toString());
            }
            if (datos[6] != null) {
                jTextField8.setText(datos[6].toString());
            }

            jFrame2.show();
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Líneas productos")) {

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes")) {

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
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
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
            jFrame1.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
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
            jFrame2.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_modificar_proveedorActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed

        // TODO add your handling code here:
        jComboBox4.removeAllItems();

        jComboBox4.setModel(dcbm_ciudades);

        for (Ciudad ciudad : ciudades_data) {
            if (jComboBox3.getSelectedItem().equals(ciudad.getNombreComunidad())) {
                jComboBox4.addItem(ciudad.getNombreCiudad());
            }
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
        System.out.println(dcbm_ciudades.getSelectedItem().toString());
    }//GEN-LAST:event_jComboBox4ActionPerformed

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
    private javax.swing.JButton eliminar;
    private javax.swing.JButton insertar;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JButton modificar;
    private javax.swing.JButton modificar_producto;
    private javax.swing.JButton modificar_proveedor;
    private javax.swing.JTable tabla_clientes;
    private javax.swing.JTable tabla_empleados;
    private javax.swing.JTable tabla_lineasproductos;
    private javax.swing.JTable tabla_pedidos;
    private javax.swing.JTable tabla_productos;
    private javax.swing.JTable tabla_proveedores;
    // End of variables declaration//GEN-END:variables
}
