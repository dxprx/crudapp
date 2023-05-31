/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import clases.*;

import com.sun.jdi.connect.spi.Connection;

import componentes.NonEditableModel;
import componentes.Validador;
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
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.*;
import javax.swing.JFileChooser;

/**
 *
 * @author oscar
 */
public class Aplicacion extends javax.swing.JFrame {

    private static Conexion conexion = Conexion.getInstance();

    private static Validador validador = Validador.getInstance();

    private Empleado usuario;

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
            if (producto.isEliminado() && producto.getStock() > 0) {
                component.setBackground(Color.ORANGE);
            } else if (producto.isEliminado() && producto.getStock() == 0) {
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

    private DefaultListModel<String> model_productoCompra = new DefaultListModel<>();
    private DefaultListModel<String> model_idproductoCompra = new DefaultListModel<>();
    private DefaultListModel<String> model_cantidadproductoCompra = new DefaultListModel<>();

    /**
     * Creates new form main
     *
     * @throws java.sql.SQLException
     */
    public Aplicacion(Empleado user) throws SQLException {
        usuario = user;
        initComponents();
        estilosJFrames();
        getCiudades();
        mostrarTablas();
        eventos();

        jm_usuario.setText(usuario.getNombre());
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

        if (!usuario.getUsuario().getRole().equals("admin")) {
            if (jTabbedPane1.indexOfTab("Empleados") != -1) {  // Verifica si el tab existe
                jTabbedPane1.removeTabAt(jTabbedPane1.indexOfTab("Empleados"));
            }
            insertar.setVisible(false);
            modificar.setVisible(false);
            eliminar.setVisible(false);
        }

        estilosTablas();

        comboboxModelos();

        listasModelos();
        
    }


    public void mostrarInsertarUsuario(boolean tieneUsuario) {
        if (tieneUsuario) {
            jTextField22.setVisible(true);
            jPasswordField2.setVisible(true);
            jComboBox12.setVisible(true);
            jLabel9.setVisible(true);
            jLabel10.setVisible(true);
            jLabel44.setVisible(true);

        } else {
            jTextField22.setVisible(false);
            jPasswordField2.setVisible(false);
            jComboBox12.setVisible(false);
            jLabel9.setVisible(false);
            jLabel10.setVisible(false);
            jLabel44.setVisible(false);
        }
    }

    public void estilosTablas() {

        tabla_productos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_proveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_clientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_empleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_lineasproductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla_pedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
    }

    public void estilosJFrames() {
        this.setTitle("Gestión de tienda");
        this.setSize(750, 500);
        this.setLocationRelativeTo(null);

        jframe_insertar_clientes.setTitle("Insertar cliente");
        jframe_insertar_clientes.setSize(440, 450);
        jframe_insertar_clientes.setResizable(false);
        jframe_insertar_clientes.setLocationRelativeTo(null);

        jframe_insertar_empleados.setTitle("Insertar empleado");
        jframe_insertar_empleados.setSize(440, 575);
        jframe_insertar_empleados.setResizable(false);
        jframe_insertar_empleados.setLocationRelativeTo(null);

        jframe_insertar_lineas.setTitle("Insertar línea de producto");
        jframe_insertar_lineas.setSize(440, 450);
        jframe_insertar_lineas.setResizable(false);
        jframe_insertar_lineas.setLocationRelativeTo(null);

        jframe_insertar_pedidos.setTitle("Insertar pedido");
        jframe_insertar_pedidos.setSize(1025, 475);
        jframe_insertar_pedidos.setResizable(false);
        jframe_insertar_pedidos.setLocationRelativeTo(null);

        jframe_insertar_productos.setTitle("Insertar producto");
        jframe_insertar_productos.setSize(440, 450);
        jframe_insertar_productos.setResizable(false);
        jframe_insertar_productos.setLocationRelativeTo(null);

        jframe_insertar_proveedores.setTitle("Insertar proveedor");
        jframe_insertar_proveedores.setSize(440, 450);
        jframe_insertar_proveedores.setResizable(false);
        jframe_insertar_proveedores.setLocationRelativeTo(null);

        jframe_modificar_clientes.setTitle("Modificar cliente");
        jframe_modificar_clientes.setSize(440, 450);
        jframe_modificar_clientes.setResizable(false);
        jframe_modificar_clientes.setLocationRelativeTo(null);

        jframe_modificar_empleados.setTitle("Modificar empleado");
        jframe_modificar_empleados.setSize(440, 575);
        jframe_modificar_empleados.setResizable(false);
        jframe_modificar_empleados.setLocationRelativeTo(null);

        jframe_modificar_lineas.setTitle("Modificar línea de producto");
        jframe_modificar_lineas.setSize(440, 450);
        jframe_modificar_lineas.setResizable(false);
        jframe_modificar_lineas.setLocationRelativeTo(null);

        jframe_modificar_productos.setTitle("Modificar producto");
        jframe_modificar_productos.setSize(440, 450);
        jframe_modificar_productos.setResizable(false);
        jframe_modificar_productos.setLocationRelativeTo(null);

        jframe_modificar_proveedores.setTitle("Modificar proveedor");
        jframe_modificar_proveedores.setSize(440, 450);
        jframe_modificar_proveedores.setResizable(false);
        jframe_modificar_proveedores.setLocationRelativeTo(null);

        jframe_mostrar_detallespedido.setTitle("Detalles pedido");
        jframe_mostrar_detallespedido.setSize(850, 500);
        jframe_mostrar_detallespedido.setResizable(false);
        jframe_mostrar_detallespedido.setLocationRelativeTo(null);
    }

    private void eventos() {
        dobleClickTablaPedidos();
        eventosSeleccionListasPedido();
    }

    private void eventosSeleccionListasPedido() {
        jList1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Obtener el índice seleccionado en jList1
                int indiceSeleccionado = jList1.getSelectedIndex();

                // Establecer la selección en jList2
                if (indiceSeleccionado >= 0) {
                    jList5.setSelectedIndex(indiceSeleccionado);
                }
            }
        });

        jList5.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Obtener el índice seleccionado en jList1
                int indiceSeleccionado = jList5.getSelectedIndex();

                // Establecer la selección en jList2
                if (indiceSeleccionado >= 0) {
                    jList1.setSelectedIndex(indiceSeleccionado);
                }
            }
        });

        jList2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Obtener el índice seleccionado en jList1
                int indiceSeleccionado = jList2.getSelectedIndex();

                // Establecer la selección en jList2
                if (indiceSeleccionado >= 0) {
                    jList3.setSelectedIndex(indiceSeleccionado);
                    jList4.setSelectedIndex(indiceSeleccionado);
                }
            }
        });

        jList3.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Obtener el índice seleccionado en jList1
                int indiceSeleccionado = jList3.getSelectedIndex();

                // Establecer la selección en jList2
                if (indiceSeleccionado >= 0) {
                    jList2.setSelectedIndex(indiceSeleccionado);
                    jList4.setSelectedIndex(indiceSeleccionado);
                }
            }
        });

        jList4.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Obtener el índice seleccionado en jList1
                int indiceSeleccionado = jList4.getSelectedIndex();

                // Establecer la selección en jList2
                if (indiceSeleccionado >= 0) {
                    jList2.setSelectedIndex(indiceSeleccionado);
                    jList3.setSelectedIndex(indiceSeleccionado);
                }
            }
        });
    }

    private void dobleClickTablaPedidos() {

        tabla_pedidos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int selectedRow = tabla_pedidos.getSelectedRow();
                    if (selectedRow != -1) {
                        // Obtener los datos de la fila seleccionada
                        int id = (int) dtm_pedidos.getValueAt(selectedRow, 0);
                        Pedido pedido = new Pedido();
                        pedido.setIdpedido(id);
                        pedido = pedidos_data.get(pedidos_data.indexOf(pedido));
                        jTextField45.setText(String.valueOf(pedido.getIdpedido()));
                        jTextField23.setText(clientes_data.get(clientes_data.indexOf(pedido.getCliente())).getNombre());
                        jTextField24.setText(empleados_data.get(empleados_data.indexOf(pedido.getEmpleado())).getNombre());
                        jTextField46.setText(pedido.getFechaPedido().toString());
                        mostrarDetallesPedidos();
                        jframe_mostrar_detallespedido.show();
                    }
                }
            }
        });
    }

    public static void generarPDFenDirectorio(List<Object[]> lista, String id, String cliente, String empleado, String fecha, String total) {
        // Mostrar el selector de directorio
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione un directorio");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int seleccion = fileChooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File directorioSeleccionado = fileChooser.getSelectedFile();

            // Crear el archivo PDF en el directorio seleccionado
            File archivoPDF = new File(directorioSeleccionado, "factura" + id + ".pdf");

            try {
                // Crear un nuevo documento PDF
                Document documento = new Document();
                PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));

                // Abrir el documento
                documento.open();
                documento.setMargins(50, 50, 50, 50);

                String logoPath = "media/logo.png";
                Image logo = Image.getInstance(logoPath);
                Paragraph paragraph = new Paragraph();
                logo.scaleToFit(100, 100);
                paragraph.add(logo);
                paragraph.setAlignment(Paragraph.ALIGN_LEFT);
                // Agregar el texto después de la imagen
                paragraph.add("Estanco TOBACCO'S");

                // Agregar el párrafo al documento
                documento.add(paragraph);
                LineSeparator lineSeparator1 = new LineSeparator();
                Paragraph espacioBlanco = new Paragraph(" ");
                documento.add(espacioBlanco);
                documento.add(lineSeparator1);
                documento.add(espacioBlanco);
                // Agregar el separador al documento
                // Agregar contenido al documento
                documento.add(new Paragraph("ID pedido: " + id));
                documento.add(new Paragraph("Cliente: " + cliente));
                documento.add(new Paragraph("Le atendió: " + empleado));
                documento.add(new Paragraph("Fecha de compra: " + fecha));

                PdfPTable tablaProductos = new PdfPTable(3);
                tablaProductos.setWidthPercentage(100);
                tablaProductos.addCell(new PdfPCell(new Phrase("ID")));
                tablaProductos.addCell(new PdfPCell(new Phrase("Producto")));
                tablaProductos.addCell(new PdfPCell(new Phrase("Precio/unidad")));

                LineSeparator lineSeparator2 = new LineSeparator();
                documento.add(espacioBlanco);
                documento.add(lineSeparator2);
                documento.add(espacioBlanco);
                for (Object[] producto : lista) {
                    tablaProductos.addCell(new PdfPCell(new Phrase(producto[0].toString())));
                    tablaProductos.addCell(new PdfPCell(new Phrase(producto[1].toString())));
                    tablaProductos.addCell(new PdfPCell(new Phrase(producto[2].toString())));
                }

                tablaProductos.setSpacingAfter(10);
                tablaProductos.setSpacingBefore(10);
                documento.add(tablaProductos);

                PdfPTable tablaTotal = new PdfPTable(1);
                tablaTotal.addCell(new PdfPCell(new Phrase("Total: " + total + "€")));
                tablaTotal.setSpacingAfter(10);
                tablaTotal.setSpacingBefore(10);
                documento.add(tablaTotal);

                // Cerrar el documento
                documento.close();

                JOptionPane.showMessageDialog(null, "Se ha generado el archivo PDF correctamente en: " + archivoPDF.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al generar el archivo PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void comboboxModelos() {

        jComboBox2.removeAllItems();
        jComboBox6.removeAllItems();
        DefaultComboBoxModel dcbm_proveedores = new DefaultComboBoxModel();
        jComboBox2.setModel(dcbm_proveedores);
        jComboBox6.setModel(dcbm_proveedores);
        for (Proveedor proveedor : proveedores_data) {
            dcbm_proveedores.addElement(proveedor.getNombreEmpresa());
        }

        jComboBox1.removeAllItems();
        jComboBox5.removeAllItems();
        DefaultComboBoxModel dcbm_lineas = new DefaultComboBoxModel();
        jComboBox1.setModel(dcbm_lineas);
        jComboBox5.setModel(dcbm_lineas);
        for (LineaProducto linea : lineasproductos_data) {
            dcbm_lineas.addElement(linea.getLinea());
        }

        DefaultComboBoxModel dcbm_comunidades = new DefaultComboBoxModel();
        dcbm_comunidades.removeAllElements();
        jComboBox3.setModel(dcbm_comunidades);
        jComboBox7.setModel(dcbm_comunidades);
        jComboBox9.setModel(dcbm_comunidades);
        jComboBox13.setModel(dcbm_comunidades);
        for (Ciudad comunidad : ciudades_data) {
            if (dcbm_comunidades.getIndexOf(comunidad.getNombreComunidad()) == -1) {
                dcbm_comunidades.addElement(comunidad.getNombreComunidad());

            }
        }

        jComboBox15.removeAllItems();
        DefaultComboBoxModel dcbm_clientes = new DefaultComboBoxModel();
        jComboBox15.setModel(dcbm_clientes);
        for (Cliente cliente : clientes_data) {
            dcbm_clientes.addElement(cliente.getIdClientes() + " " + cliente.getNombre());
        }

    }

    private void listasModelos() {

        jList1.removeAll();
        DefaultListModel<String> model_productosVenta = new DefaultListModel<>();
        jList1.setModel(model_productosVenta);

        for (Producto producto : productos_data) {
            if (producto.getStock() > 0) {
                model_productosVenta.addElement(producto.getNombre());
            }
        }

        jList5.removeAll();
        DefaultListModel<String> model_idproductosVenta = new DefaultListModel<>();
        jList5.setModel(model_idproductosVenta);

        for (Producto producto : productos_data) {
            if (producto.getStock() > 0) {
                model_idproductosVenta.addElement(String.valueOf(producto.getIdProducto()));
            }
        }

        model_productoCompra.removeAllElements();
        jList2.setModel(model_productoCompra);

        model_idproductoCompra.removeAllElements();
        jList3.setModel(model_idproductoCompra);

        model_cantidadproductoCompra.removeAllElements();
        jList4.setModel(model_cantidadproductoCompra);

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

        String titulos_empleados[] = {"ID", "Nombre", "Primer apellido", "Segundo apellido", "DNI", "Teléfono", "Email", "Salario", "ID Usuario", "Usuario", "Privilegios"};
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
                empleado.getSalario(),
                empleado.getUsuario().getIdUsuario(),
                empleado.getUsuario().getUsuario(),
                empleado.getUsuario().getRole()
            };
            dtm_empleados.addRow(resultado);
        }
    }

    private void mostrarDetallesPedidos() {
        dtm_detallespedido = new NonEditableModel();
        String titulos_pedidos[] = {"ID", "Producto", "Cantidad", "Precio"};
        dtm_detallespedido.setColumnIdentifiers(titulos_pedidos);
        jTable1.setModel(dtm_detallespedido);

        try {
            conexion.conectar();
            detallespedidos_data = conexion.seleccionarDetallesPedidos(Integer.parseInt(jTextField45.getText()));
            conexion.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (DetallesPedido pedido : detallespedidos_data) {
            Producto producto = new Producto();
            producto.setIdProducto(pedido.getIdproducto());
            Object[] resultado = {
                pedido.getIdproducto(),
                productos_data.get(productos_data.indexOf(producto)).getNombre(),
                pedido.getCantidad(),
                pedido.getPrecio_venta()
            };
            dtm_detallespedido.addRow(resultado);
        }
        float total = 0;
        int rowCount = dtm_detallespedido.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int cantidad = Integer.parseInt(dtm_detallespedido.getValueAt(i, 2).toString());
            float precio = Float.parseFloat(dtm_detallespedido.getValueAt(i, 3).toString());

            total += cantidad * precio;
        }
        jTextField50.setText(String.valueOf(total));
    }

    private void mostrarPedidos() {

        String titulos_pedidos[] = {"ID", "Cliente", "Empleado", "Fecha"};
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
                clientes_data.get(clientes_data.indexOf(pedido.getCliente())).getNombre(),
                empleados_data.get(empleados_data.indexOf(pedido.getEmpleado())).getNombre(),
                pedido.getFechaPedido()};
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
        jTextField47 = new javax.swing.JTextField();
        modificar_empleado = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox<>();
        jTextField22 = new javax.swing.JTextField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jframe_insertar_productos = new javax.swing.JFrame();
        jPanel7 = new javax.swing.JPanel();
        jTextField11 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox<>();
        jTextField12 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField28 = new javax.swing.JTextField();
        jComboBox6 = new javax.swing.JComboBox<>();
        insertar_producto = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jframe_insertar_proveedores = new javax.swing.JFrame();
        jPanel8 = new javax.swing.JPanel();
        jTextField29 = new javax.swing.JTextField();
        jComboBox9 = new javax.swing.JComboBox<>();
        jComboBox10 = new javax.swing.JComboBox<>();
        jTextField30 = new javax.swing.JTextField();
        jTextField31 = new javax.swing.JTextField();
        jTextField32 = new javax.swing.JTextField();
        insertar_proveedor = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jframe_insertar_lineas = new javax.swing.JFrame();
        jPanel9 = new javax.swing.JPanel();
        jTextField33 = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        insertar_linea = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jframe_insertar_clientes = new javax.swing.JFrame();
        jPanel10 = new javax.swing.JPanel();
        jTextField34 = new javax.swing.JTextField();
        jTextField35 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();
        jComboBox13 = new javax.swing.JComboBox<>();
        jComboBox14 = new javax.swing.JComboBox<>();
        jTextField37 = new javax.swing.JTextField();
        jTextField38 = new javax.swing.JTextField();
        insertar_cliente = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jframe_insertar_empleados = new javax.swing.JFrame();
        jPanel11 = new javax.swing.JPanel();
        jTextField39 = new javax.swing.JTextField();
        jTextField40 = new javax.swing.JTextField();
        jTextField41 = new javax.swing.JTextField();
        jTextField42 = new javax.swing.JTextField();
        jTextField44 = new javax.swing.JTextField();
        jTextField43 = new javax.swing.JTextField();
        jTextField48 = new javax.swing.JTextField();
        insertar_empleado = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox<>();
        jTextField21 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jframe_insertar_pedidos = new javax.swing.JFrame();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        insertar_pedido = new javax.swing.JButton();
        jComboBox15 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList<>();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList<>();
        jTextField49 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jframe_mostrar_detallespedido = new javax.swing.JFrame();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel52 = new javax.swing.JLabel();
        jTextField45 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField46 = new javax.swing.JTextField();
        jTextField50 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jm_usuario = new javax.swing.JMenu();
        jmi_perfil = new javax.swing.JMenuItem();
        jmi_cerrarSesion = new javax.swing.JMenuItem();

        jframe_modificar_productos.setResizable(false);
        jframe_modificar_productos.setSize(new java.awt.Dimension(500, 500));
        jframe_modificar_productos.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
                .addGap(123, 123, 123)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField1)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField2)
                        .addComponent(jTextField3)
                        .addComponent(jTextField4)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(modificar_producto)))
                .addContainerGap(250, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
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
                .addContainerGap(157, Short.MAX_VALUE))
        );

        jframe_modificar_productos.getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

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
                .addContainerGap(224, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                .addComponent(modificar_proveedor)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jframe_modificar_proveedoresLayout = new javax.swing.GroupLayout(jframe_modificar_proveedores.getContentPane());
        jframe_modificar_proveedores.getContentPane().setLayout(jframe_modificar_proveedoresLayout);
        jframe_modificar_proveedoresLayout.setHorizontalGroup(
            jframe_modificar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jframe_modificar_proveedoresLayout.setVerticalGroup(
            jframe_modificar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addContainerGap(224, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(modificar_linea)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jframe_modificar_lineasLayout = new javax.swing.GroupLayout(jframe_modificar_lineas.getContentPane());
        jframe_modificar_lineas.getContentPane().setLayout(jframe_modificar_lineasLayout);
        jframe_modificar_lineasLayout.setHorizontalGroup(
            jframe_modificar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jframe_modificar_lineasLayout.setVerticalGroup(
            jframe_modificar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addContainerGap(233, Short.MAX_VALUE))
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
                .addContainerGap(144, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jframe_modificar_clientesLayout = new javax.swing.GroupLayout(jframe_modificar_clientes.getContentPane());
        jframe_modificar_clientes.getContentPane().setLayout(jframe_modificar_clientesLayout);
        jframe_modificar_clientesLayout.setHorizontalGroup(
            jframe_modificar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jframe_modificar_clientesLayout.setVerticalGroup(
            jframe_modificar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jframe_modificar_empleados.setSize(new java.awt.Dimension(500, 500));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("ID: X");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 40, -1));
        jPanel5.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 270, -1));
        jPanel5.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 270, -1));
        jPanel5.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 270, -1));
        jPanel5.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 270, -1));
        jPanel5.add(jTextField25, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 270, -1));
        jPanel5.add(jTextField26, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 270, -1));
        jPanel5.add(jTextField47, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 270, -1));

        modificar_empleado.setText("Modificar");
        modificar_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_empleadoActionPerformed(evt);
            }
        });
        jPanel5.add(modificar_empleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 480, -1, -1));

        jLabel9.setText("Usuario");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        jLabel10.setText("Contraseña");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, -1, -1));

        jLabel44.setText("Privilegios");
        jPanel5.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, -1, -1));

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "user", "admin" }));
        jPanel5.add(jComboBox12, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 410, 269, -1));
        jPanel5.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 330, 269, -1));
        jPanel5.add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 370, 269, -1));

        jLabel45.setText("Nombre");
        jPanel5.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, 20));

        jLabel46.setText("Primer apellido");
        jPanel5.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 20));

        jLabel47.setText("Segundo apellido");
        jPanel5.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, 20));

        jLabel48.setText("DNI");
        jPanel5.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, 20));

        jLabel49.setText("Teléfono");
        jPanel5.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, 20));

        jLabel50.setText("Email");
        jPanel5.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, 20));

        jLabel51.setText("Salario");
        jPanel5.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, 20));

        javax.swing.GroupLayout jframe_modificar_empleadosLayout = new javax.swing.GroupLayout(jframe_modificar_empleados.getContentPane());
        jframe_modificar_empleados.getContentPane().setLayout(jframe_modificar_empleadosLayout);
        jframe_modificar_empleadosLayout.setHorizontalGroup(
            jframe_modificar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jframe_modificar_empleadosLayout.setVerticalGroup(
            jframe_modificar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_modificar_empleadosLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jframe_insertar_productos.setSize(new java.awt.Dimension(500, 500));
        jframe_insertar_productos.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 191, -1));

        jPanel7.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 191, -1));
        jPanel7.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 191, -1));
        jPanel7.add(jTextField27, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 191, -1));
        jPanel7.add(jTextField28, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 191, -1));

        jPanel7.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 270, 191, -1));

        insertar_producto.setText("Insertar");
        insertar_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_productoActionPerformed(evt);
            }
        });
        jPanel7.add(insertar_producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, -1, -1));

        jLabel12.setText("Producto");
        jPanel7.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, -1, -1));

        jLabel13.setText("Línea");
        jPanel7.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, -1, -1));

        jLabel14.setText("Descripción");
        jPanel7.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, -1, -1));

        jLabel15.setText("Stock");
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, -1, -1));

        jLabel16.setText("P.V.P.");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, -1, -1));

        jLabel17.setText("Proveedor");
        jPanel7.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, -1, -1));

        jframe_insertar_productos.getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jframe_insertar_proveedores.setSize(new java.awt.Dimension(500, 500));

        jComboBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });

        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });

        insertar_proveedor.setText("Insertar");
        insertar_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_proveedorActionPerformed(evt);
            }
        });

        jLabel18.setText("Empresa");

        jLabel19.setText("Contacto");

        jLabel20.setText("CA");

        jLabel21.setText("Ciudad");

        jLabel22.setText("Teléfono");

        jLabel23.setText("Web");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel23))))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField29)
                            .addComponent(jComboBox9, 0, 269, Short.MAX_VALUE)
                            .addComponent(jTextField30)
                            .addComponent(jTextField31)
                            .addComponent(jComboBox10, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField32)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(insertar_proveedor)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(insertar_proveedor)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jframe_insertar_proveedoresLayout = new javax.swing.GroupLayout(jframe_insertar_proveedores.getContentPane());
        jframe_insertar_proveedores.getContentPane().setLayout(jframe_insertar_proveedoresLayout);
        jframe_insertar_proveedoresLayout.setHorizontalGroup(
            jframe_insertar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jframe_insertar_proveedoresLayout.setVerticalGroup(
            jframe_insertar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jframe_insertar_lineas.setSize(new java.awt.Dimension(500, 500));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane8.setViewportView(jTextArea2);

        insertar_linea.setText("Insertar");
        insertar_linea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_lineaActionPerformed(evt);
            }
        });

        jLabel24.setText("Línea");

        jLabel25.setText("Descripción");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(166, 166, 166)
                            .addComponent(insertar_linea))
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel25)
                            .addGap(22, 22, 22)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(insertar_linea)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout jframe_insertar_lineasLayout = new javax.swing.GroupLayout(jframe_insertar_lineas.getContentPane());
        jframe_insertar_lineas.getContentPane().setLayout(jframe_insertar_lineasLayout);
        jframe_insertar_lineasLayout.setHorizontalGroup(
            jframe_insertar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jframe_insertar_lineasLayout.setVerticalGroup(
            jframe_insertar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jframe_insertar_clientes.setSize(new java.awt.Dimension(500, 500));

        jComboBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox13ActionPerformed(evt);
            }
        });

        jComboBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox14ActionPerformed(evt);
            }
        });

        insertar_cliente.setText("Insertar");
        insertar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_clienteActionPerformed(evt);
            }
        });

        jLabel26.setText("Nombre");

        jLabel27.setText("Teléfono");

        jLabel28.setText("Dirección");

        jLabel29.setText("CA");

        jLabel30.setText("Ciudad");

        jLabel31.setText("Código postal");

        jLabel32.setText("Email");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31)
                            .addComponent(jLabel32))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jComboBox14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField34)
                                    .addComponent(jTextField35)
                                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField38)
                                .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(insertar_cliente)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addComponent(insertar_cliente)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jframe_insertar_clientesLayout = new javax.swing.GroupLayout(jframe_insertar_clientes.getContentPane());
        jframe_insertar_clientes.getContentPane().setLayout(jframe_insertar_clientesLayout);
        jframe_insertar_clientesLayout.setHorizontalGroup(
            jframe_insertar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jframe_insertar_clientesLayout.setVerticalGroup(
            jframe_insertar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jframe_insertar_empleados.setSize(new java.awt.Dimension(500, 500));

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel11.add(jTextField39, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 269, -1));
        jPanel11.add(jTextField40, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 269, -1));
        jPanel11.add(jTextField41, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 269, -1));
        jPanel11.add(jTextField42, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 269, -1));
        jPanel11.add(jTextField44, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 269, -1));
        jPanel11.add(jTextField43, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 269, -1));
        jPanel11.add(jTextField48, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 269, -1));

        insertar_empleado.setText("Insertar");
        insertar_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_empleadoActionPerformed(evt);
            }
        });
        jPanel11.add(insertar_empleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 510, -1, -1));

        jLabel33.setText("Nombre");
        jPanel11.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel34.setText("Primer apellido");
        jPanel11.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jLabel35.setText("Segundo apellido");
        jPanel11.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel36.setText("DNI");
        jPanel11.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel37.setText("Teléfono");
        jPanel11.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        jLabel38.setText("Email");
        jPanel11.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jLabel39.setText("Salario");
        jPanel11.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jCheckBox1.setText("Asignar usuario");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel11.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, -1, -1));

        jLabel6.setText("Usuario");
        jPanel11.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, -1, -1));

        jLabel7.setText("Contraseña");
        jPanel11.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, -1, -1));

        jLabel8.setText("Privilegios");
        jPanel11.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, -1, -1));

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "user", "admin" }));
        jPanel11.add(jComboBox11, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 450, 269, -1));
        jPanel11.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 370, 269, -1));
        jPanel11.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 410, 269, -1));

        javax.swing.GroupLayout jframe_insertar_empleadosLayout = new javax.swing.GroupLayout(jframe_insertar_empleados.getContentPane());
        jframe_insertar_empleados.getContentPane().setLayout(jframe_insertar_empleadosLayout);
        jframe_insertar_empleadosLayout.setHorizontalGroup(
            jframe_insertar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jframe_insertar_empleadosLayout.setVerticalGroup(
            jframe_insertar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jframe_insertar_pedidos.setSize(new java.awt.Dimension(500, 500));
        jframe_insertar_pedidos.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane9.setViewportView(jList1);

        jPanel12.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 33, 220, 358));

        jScrollPane10.setViewportView(jList2);

        jPanel12.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(629, 34, 294, 260));

        insertar_pedido.setText("Insertar pedido");
        insertar_pedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_pedidoActionPerformed(evt);
            }
        });
        jPanel12.add(insertar_pedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 360, -1, -1));

        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel12.add(jComboBox15, new org.netbeans.lib.awtextra.AbsoluteConstraints(722, 329, 201, -1));

        jLabel11.setText("Cliente");
        jPanel12.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 332, -1, -1));

        jScrollPane11.setViewportView(jList3);

        jPanel12.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(585, 34, 38, 260));

        jScrollPane12.setViewportView(jList4);

        jPanel12.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(929, 34, 48, 260));

        jLabel40.setText("Cantidad");
        jPanel12.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(929, 6, -1, -1));

        jLabel41.setText("ID");
        jPanel12.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(585, 11, -1, -1));

        jLabel42.setText("Producto");
        jPanel12.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(629, 11, -1, -1));

        jList5.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane13.setViewportView(jList5);

        jPanel12.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 33, 41, 358));
        jPanel12.add(jTextField49, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 100, 159, -1));

        jLabel43.setText("Cantidad");
        jPanel12.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, -1, -1));

        jButton1.setText("Añadir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 150, 73, -1));

        jButton2.setText("Eliminar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 210, -1, -1));

        jLabel58.setText("ID");
        jPanel12.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 11, -1, -1));

        jLabel59.setText("Producto");
        jPanel12.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 11, -1, -1));

        jframe_insertar_pedidos.getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 420));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Producto", "Cantidad", "Precio"
            }
        ));
        jScrollPane14.setViewportView(jTable1);

        jLabel52.setText("Cliente");

        jTextField45.setEnabled(false);

        jTextField23.setEnabled(false);

        jTextField24.setEnabled(false);

        jTextField46.setEnabled(false);

        jTextField50.setEnabled(false);

        jLabel53.setText("Empleado");

        jLabel54.setText("ID pedido");

        jButton3.setText("Exportar factura");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel55.setText("Fecha");

        jLabel56.setText("Total");

        jLabel57.setText("€");

        javax.swing.GroupLayout jframe_mostrar_detallespedidoLayout = new javax.swing.GroupLayout(jframe_mostrar_detallespedido.getContentPane());
        jframe_mostrar_detallespedido.getContentPane().setLayout(jframe_mostrar_detallespedidoLayout);
        jframe_mostrar_detallespedidoLayout.setHorizontalGroup(
            jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_mostrar_detallespedidoLayout.createSequentialGroup()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jframe_mostrar_detallespedidoLayout.createSequentialGroup()
                        .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframe_mostrar_detallespedidoLayout.createSequentialGroup()
                                .addComponent(jLabel52)
                                .addGap(32, 32, 32))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframe_mostrar_detallespedidoLayout.createSequentialGroup()
                                .addComponent(jLabel54)
                                .addGap(34, 34, 34)))
                        .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField23)
                            .addComponent(jTextField45, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jframe_mostrar_detallespedidoLayout.createSequentialGroup()
                        .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel55)
                            .addComponent(jLabel53)
                            .addComponent(jLabel56))
                        .addGap(32, 32, 32)
                        .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField24, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                            .addComponent(jTextField46, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                            .addComponent(jTextField50))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jframe_mostrar_detallespedidoLayout.setVerticalGroup(
            jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframe_mostrar_detallespedidoLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jTextField45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addGap(18, 18, 18)
                .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53))
                .addGap(18, 18, 18)
                .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55))
                .addGap(18, 18, 18)
                .addGroup(jframe_mostrar_detallespedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jTextField50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(24, 24, 24))
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

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
        insertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertarActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(insertar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(modificar)
                    .addComponent(eliminar)
                    .addComponent(insertar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
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
            Object[] datos = new Object[11];

            for (int i = 0; i < tabla_empleados.getColumnCount(); i++) {
                datos[i] = dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), i);
            }
            if (datos[0] != null) {
                jLabel5.setText("ID : " + datos[0].toString());
            }
            if (datos[1] != null) {
                jTextField17.setText(datos[1].toString());
            }
            if (datos[2] != null) {
                jTextField18.setText(datos[2].toString());
            }
            if (datos[3] != null) {
                jTextField19.setText(datos[3].toString());
            }
            if (datos[4] != null) {
                jTextField20.setText(datos[4].toString());
            }
            if (datos[5] != null) {
                jTextField25.setText(datos[5].toString());
            }
            if (datos[6] != null) {
                jTextField26.setText(datos[6].toString());
            }
            if (datos[7] != null) {
                jTextField47.setText(datos[7].toString());
            }
            if (datos[8] == null || datos[9] == null || datos[10] == null) {
                mostrarInsertarUsuario(false);
                jframe_modificar_empleados.show();
            } else {
                if (datos[8] != null) {
                    if ((int) datos[8] != usuario.getUsuario().getIdUsuario()) {
                        mostrarInsertarUsuario(true);
                        jTextField22.setText(datos[9].toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "No puedes modificar tus datos", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (datos[10] != null) {
                    jComboBox12.setSelectedItem(datos[10].toString());
                }
                if ((int) datos[8] != usuario.getUsuario().getIdUsuario()) {
                    jframe_modificar_empleados.show();
                }

            }
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
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes")) {
            if (tabla_clientes.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    int id = (int) dtm_clientes.getValueAt(tabla_clientes.getSelectedRow(), 0);
                    conexion.conectar();
                    conexion.eliminarCliente(id);
                    conexion.desconectar();
                    actualizarTablas();
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Empleados")) {
            if (tabla_empleados.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    int id = (int) dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), 0);
                    conexion.conectar();
                    conexion.eliminarEmpleado(id);
                    if (!dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), 8).equals(null)) {
                        int idusuario = (int) dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), 8);
                        conexion.eliminarUsuario(idusuario);
                    }
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

            if (validador.validarNumeroPositivo(jTextField3.getText())
                    && validador.validarNumeroPositivo(jTextField4.getText())
                    && validador.noEstaVacio(jTextField1.getText())) {
                Producto producto = new Producto(
                        Integer.parseInt(jLabel1.getText().substring(5)),
                        jTextField1.getText(),
                        lineasproductos_data.get(jComboBox1.getSelectedIndex()),
                        jTextField2.getText(),
                        Integer.parseInt(jTextField3.getText()),
                        Float.parseFloat(jTextField4.getText()),
                        proveedores_data.get(jComboBox2.getSelectedIndex()),
                        false
                );
                conexion.conectar();
                conexion.actualizarProducto(producto);
                conexion.desconectar();
                actualizarTablas();
                jframe_modificar_productos.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_modificar_productoActionPerformed

    private void modificar_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_proveedorActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            if (validador.validarTelefono(jTextField7.getText())
                    && validador.validarPaginaWeb(jTextField8.getText())
                    && validador.noEstaVacio(jTextField5.getText())) {
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
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
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

    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void modificar_lineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_lineaActionPerformed
        // TODO add your handling code here:
        try {
            if (!lineasproductos_data.contains(jTextField9.getText())) {
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
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error: la línea de producto introducida ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            if (validador.validarTelefono(jTextField14.getText())
                    && validador.validarCodigoPostal(jTextField16.getText())
                    && validador.validarCorreoElectronico(jTextField10.getText())
                    && validador.noEstaVacio(jTextField13.getText())) {
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
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_modificar_clienteActionPerformed

    private void modificar_empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_empleadoActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            if (validador.validarDNI(jTextField20.getText())
                    && validador.validarTelefono(jTextField25.getText())
                    && validador.validarCorreoElectronico(jTextField26.getText())
                    && validador.validarNumeroPositivo(jTextField47.getText())
                    && validador.noEstaVacio(jTextField17.getText())
                    && validador.noEstaVacio(jTextField18.getText())
                    && validador.noEstaVacio(jTextField19.getText())) {

                String id = jLabel5.getText().substring(5);
                Empleado empleado = new Empleado();
                empleado.setIdempleado(Integer.parseInt(id));
                empleado.setNombre(jTextField17.getText());
                empleado.setApellido1(jTextField18.getText());
                empleado.setApellido2(jTextField19.getText());
                empleado.setDni(jTextField20.getText());
                empleado.setTelefono(jTextField25.getText());
                empleado.setEmail(jTextField26.getText());
                empleado.setSalario(Float.parseFloat(jTextField47.getText()));

                conexion.conectar();
                conexion.actualizarEmpleado(empleado);
                conexion.desconectar();
                actualizarTablas();
                jframe_modificar_empleados.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_modificar_empleadoActionPerformed

    private void insertar_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertar_productoActionPerformed
        try {
            // TODO add your handling code here:
            if (validador.validarNumeroPositivo(jTextField27.getText())
                    && validador.validarNumeroPositivo(jTextField28.getText())
                    && validador.noEstaVacio(jTextField11.getText())) {
                Producto producto = new Producto();
                producto.setNombre(jTextField11.getText());
                producto.setLineaProducto(lineasproductos_data.get(jComboBox5.getSelectedIndex()));
                producto.setDescripcion(jTextField12.getText());
                producto.setStock(Integer.parseInt(jTextField27.getText()));
                producto.setPvp(Float.parseFloat(jTextField28.getText()));
                producto.setProveedor(proveedores_data.get(jComboBox6.getSelectedIndex()));

                conexion.conectar();
                conexion.insertarProducto(producto);
                conexion.desconectar();
                actualizarTablas();
                jframe_insertar_productos.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_insertar_productoActionPerformed

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        // TODO add your handling code here:
        jComboBox10.removeAllItems();
        jComboBox10.setModel(dcbm_ciudades);

        for (Ciudad ciudad : ciudades_data) {
            if (jComboBox9.getSelectedItem().equals(ciudad.getNombreComunidad())) {
                jComboBox10.addItem(ciudad.getNombreCiudad());
            }
        }
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox10ActionPerformed

    private void insertar_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertar_proveedorActionPerformed
        // TODO add your handling code here:
        try {
            if (validador.validarTelefono(jTextField31.getText())
                    && validador.validarPaginaWeb(jTextField32.getText())
                    && validador.noEstaVacio(jTextField29.getText())) {
                Ciudad ciudad = new Ciudad();
                ciudad.setNombreCiudad(dcbm_ciudades.getSelectedItem().toString());

                // TODO add your handling code here:
                Proveedor proveedor = new Proveedor();
                proveedor.setNombreEmpresa(jTextField29.getText());
                proveedor.setNombreContacto(jTextField30.getText());
                proveedor.setCiudad(ciudades_data.get(ciudades_data.indexOf(ciudad)));
                proveedor.setTelefono(jTextField31.getText());
                proveedor.setPaginaweb(jTextField32.getText());

                conexion.conectar();
                conexion.insertarProveedor(proveedor);
                conexion.desconectar();
                actualizarTablas();
                jframe_insertar_proveedores.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_insertar_proveedorActionPerformed

    private void insertar_lineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertar_lineaActionPerformed
        // TODO add your handling code here:
        try {
            if (!lineasproductos_data.contains(jTextField33.getText())) {
                // TODO add your handling code here:
                LineaProducto linea = new LineaProducto();
                linea.setLinea(jTextField33.getText());
                linea.setDescripcion(jTextArea2.getText());

                conexion.conectar();
                conexion.insertarLineaproducto(linea);
                conexion.desconectar();
                actualizarTablas();
                jframe_insertar_lineas.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error: la línea de producto introducida ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_insertar_lineaActionPerformed

    private void jComboBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox13ActionPerformed
        // TODO add your handling code here:
        jComboBox14.removeAllItems();
        jComboBox14.setModel(dcbm_ciudades);

        for (Ciudad ciudad : ciudades_data) {
            if (jComboBox13.getSelectedItem().equals(ciudad.getNombreComunidad())) {
                jComboBox14.addItem(ciudad.getNombreCiudad());
            }
        }
    }//GEN-LAST:event_jComboBox13ActionPerformed

    private void jComboBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox14ActionPerformed

    private void insertar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertar_clienteActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            if (validador.validarTelefono(jTextField35.getText())
                    && validador.validarCodigoPostal(jTextField37.getText())
                    && validador.validarCorreoElectronico(jTextField38.getText())
                    && validador.noEstaVacio(jTextField34.getText())
                    && validador.noEstaVacio(jTextField36.getText())) {
                Ciudad ciudad = new Ciudad();
                ciudad.setNombreCiudad(dcbm_ciudades.getSelectedItem().toString());

                Cliente cliente = new Cliente();
                cliente.setNombre(jTextField34.getText());
                cliente.setTelefono(jTextField35.getText());
                cliente.setDireccion(jTextField36.getText());
                cliente.setCiudad(ciudades_data.get(ciudades_data.indexOf(ciudad)));
                cliente.setCodigoPostal(jTextField37.getText());
                cliente.setEmail(jTextField38.getText());

                conexion.conectar();
                conexion.insertarCliente(cliente);
                conexion.desconectar();
                actualizarTablas();
                jframe_insertar_clientes.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_insertar_clienteActionPerformed

    private void insertar_empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertar_empleadoActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            if (validador.validarDNI(jTextField42.getText())
                    && validador.validarTelefono(jTextField44.getText())
                    && validador.validarCorreoElectronico(jTextField43.getText())
                    && validador.validarNumeroPositivo(jTextField48.getText())
                    && validador.noEstaVacio(jTextField39.getText())
                    && validador.noEstaVacio(jTextField40.getText())
                    && validador.noEstaVacio(jTextField41.getText())) {
                Empleado empleado = new Empleado();
                empleado.setNombre(jTextField39.getText());
                empleado.setApellido1(jTextField40.getText());
                empleado.setApellido2(jTextField41.getText());
                empleado.setDni(jTextField42.getText());
                empleado.setTelefono(jTextField44.getText());
                empleado.setEmail(jTextField43.getText());
                empleado.setSalario(Float.parseFloat(jTextField48.getText()));

                if (jCheckBox1.isSelected()) {
                    Usuario usuario = new Usuario();
                    usuario.setUsuario(jTextField21.getText());
                    usuario.setPass(jPasswordField1.getText());
                    usuario.setRole(jComboBox11.getSelectedItem().toString());
                    empleado.setUsuario(usuario);
                }
                conexion.conectar();
                conexion.insertarEmpleado(empleado);
                conexion.desconectar();
                actualizarTablas();
                jframe_insertar_empleados.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Se produjo un error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_insertar_empleadoActionPerformed

    private void insertar_pedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertar_pedidoActionPerformed

        if (jList3.getModel().getSize() != 0) {
            try {
                // TODO add your handling code here:
                List<DetallesPedido> detallespedido = new ArrayList<>();
                int size = model_cantidadproductoCompra.getSize();
                for (int i = 0; i < size; i++) {
                    DetallesPedido detallepedido = new DetallesPedido();

                    Producto producto = new Producto();
                    producto.setIdProducto(Integer.parseInt(model_idproductoCompra.getElementAt(i)));

                    detallepedido.setCantidad(Integer.parseInt(model_cantidadproductoCompra.getElementAt(i)));
                    detallepedido.setIdproducto(productos_data.get(productos_data.indexOf(producto)).getIdProducto());
                    detallespedido.add(detallepedido);
                }

                Pedido pedido = new Pedido();
                Cliente cliente = new Cliente();
                cliente.setIdClientes(Integer.parseInt(jComboBox15.getSelectedItem().toString().substring(0, jComboBox15.getSelectedItem().toString().indexOf(" "))));
                pedido.setCliente(clientes_data.get(clientes_data.indexOf(cliente)));
                pedido.setEmpleado(empleados_data.get(empleados_data.indexOf(usuario)));
                conexion.conectar();
                conexion.insertarPedido(pedido, detallespedido);
                conexion.desconectar();
                actualizarTablas();
                jframe_insertar_pedidos.dispose();
            } catch (SQLException ex) {
                Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

        }
    }//GEN-LAST:event_insertar_pedidoActionPerformed

    private void insertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertarActionPerformed
        // TODO add your handling code here:
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Productos")) {
            jframe_insertar_productos.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Proveedores")) {
            jframe_insertar_proveedores.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Líneas productos")) {
            jframe_insertar_lineas.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes")) {
            jframe_insertar_clientes.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Empleados")) {
            jframe_insertar_empleados.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Pedidos")) {
            jframe_insertar_pedidos.show();
        }
    }//GEN-LAST:event_insertarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (validador.noEstaVacio(jTextField49.getText()) && validador.validarNumeroPositivo(jTextField49.getText()) && jList5.getSelectedIndex() != -1) {
            // Antes de insertar un producto comprueba si la cantidad es mayor al stock de ese producto, luego comprueba si la suma de mas productos es mayor que el stock y solo añade cantidad cuando el producto es el mismo
            Producto producto = new Producto();
            producto.setIdProducto(Integer.parseInt(jList5.getSelectedValue()));
            if (Integer.parseInt(jTextField49.getText()) > productos_data.get(productos_data.indexOf(producto)).getStock()) {
                JOptionPane.showMessageDialog(null, "La cantidad introducida es mayor que la cantidad en stock: " + productos_data.get(productos_data.indexOf(producto)).getStock(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (model_idproductoCompra.contains(jList5.getSelectedValue())) {
                    if (Integer.parseInt(jTextField49.getText()) + Integer.parseInt(model_cantidadproductoCompra.get(model_idproductoCompra.indexOf(jList5.getSelectedValue()))) > productos_data.get(productos_data.indexOf(producto)).getStock()) {
                        JOptionPane.showMessageDialog(null, "La cantidad introducida es mayor que la cantidad en stock: " + productos_data.get(productos_data.indexOf(producto)).getStock(), "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int cantidad = Integer.parseInt(model_cantidadproductoCompra.get(model_idproductoCompra.indexOf(jList5.getSelectedValue())));
                        cantidad += Integer.parseInt(jTextField49.getText());
                        model_cantidadproductoCompra.setElementAt(String.valueOf(cantidad), model_idproductoCompra.indexOf(jList5.getSelectedValue()));
                    }
                } else {
                    model_cantidadproductoCompra.addElement(jTextField49.getText());
                    model_idproductoCompra.addElement(jList5.getSelectedValue());
                    model_productoCompra.addElement(jList1.getSelectedValue());
                }
            }
        } else {
            if (!validador.noEstaVacio(jTextField49.getText())) {
                JOptionPane.showMessageDialog(null, "Por favor, introduzca una cantidad numérica válida", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            }
            if (!validador.validarNumeroPositivo(jTextField49.getText())) {
                JOptionPane.showMessageDialog(null, "Por favor, introduzca una cantidad numérica positiva", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            }
            if (jList5.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione un producto", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
        jTextField49.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        model_cantidadproductoCompra.removeElementAt(jList4.getSelectedIndex());
        model_idproductoCompra.removeElementAt(jList3.getSelectedIndex());
        model_productoCompra.removeElementAt(jList2.getSelectedIndex());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        if (!usuario.getUsuario().getRole().equals("admin")) {
            if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Pedidos")) {
                insertar.setVisible(true);
            } else {
                insertar.setVisible(false);
            }
        } else {
            if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Pedidos")) {
                eliminar.setVisible(false);
                modificar.setVisible(false);
            } else {
                eliminar.setVisible(true);
                modificar.setVisible(true);
            }
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()) {
            jTextField21.setVisible(true);
            jPasswordField1.setVisible(true);
            jComboBox11.setVisible(true);
            jLabel6.setVisible(true);
            jLabel7.setVisible(true);
            jLabel8.setVisible(true);
        } else {
            jTextField21.setVisible(false);
            jPasswordField1.setVisible(false);
            jComboBox11.setVisible(false);
            jLabel6.setVisible(false);
            jLabel7.setVisible(false);
            jLabel8.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jmi_cerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmi_cerrarSesionActionPerformed
        // TODO add your handling code here:
        this.dispose();
        JFrame inicio_sesion = new InicioSesion();
        inicio_sesion.show();
    }//GEN-LAST:event_jmi_cerrarSesionActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        List<Object[]> lista = new ArrayList<>();
        for (int i = 0; i < dtm_detallespedido.getRowCount(); i++) {

            Object[] producto = {
                dtm_detallespedido.getValueAt(i, 0),
                dtm_detallespedido.getValueAt(i, 1),
                dtm_detallespedido.getValueAt(i, 2),
                dtm_detallespedido.getValueAt(i, 3)
            };

            lista.add(producto);
        }

        generarPDFenDirectorio(lista, jTextField45.getText(), jTextField23.getText(), jTextField24.getText(), jTextField46.getText(), jTextField50.getText());
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
                    Empleado e = new Empleado();
                    Usuario u = new Usuario("Usuario", -1, "admin");
                    e.setUsuario(u);
                    new Aplicacion(e).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton eliminar;
    private javax.swing.JButton insertar;
    private javax.swing.JButton insertar_cliente;
    private javax.swing.JButton insertar_empleado;
    private javax.swing.JButton insertar_linea;
    private javax.swing.JButton insertar_pedido;
    private javax.swing.JButton insertar_producto;
    private javax.swing.JButton insertar_proveedor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JList<String> jList4;
    private javax.swing.JList<String> jList5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
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
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JFrame jframe_insertar_clientes;
    private javax.swing.JFrame jframe_insertar_empleados;
    private javax.swing.JFrame jframe_insertar_lineas;
    private javax.swing.JFrame jframe_insertar_pedidos;
    private javax.swing.JFrame jframe_insertar_productos;
    private javax.swing.JFrame jframe_insertar_proveedores;
    private javax.swing.JFrame jframe_modificar_clientes;
    private javax.swing.JFrame jframe_modificar_empleados;
    private javax.swing.JFrame jframe_modificar_lineas;
    private javax.swing.JFrame jframe_modificar_productos;
    private javax.swing.JFrame jframe_modificar_proveedores;
    private javax.swing.JFrame jframe_mostrar_detallespedido;
    private javax.swing.JMenu jm_usuario;
    private javax.swing.JMenuItem jmi_cerrarSesion;
    private javax.swing.JMenuItem jmi_perfil;
    private javax.swing.JButton modificar;
    private javax.swing.JButton modificar_cliente;
    private javax.swing.JButton modificar_empleado;
    private javax.swing.JButton modificar_linea;
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
