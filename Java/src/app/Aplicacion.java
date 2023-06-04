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
import componentes.TextField;

import java.io.*;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.UIManager;

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
                component.setBackground(new Color(253, 188, 60, 150));
            } else if (producto.isEliminado() && producto.getStock() == 0) {
                component.setBackground(new Color(135, 32, 49, 150)); //Rojo
            } else if (isSelected) {
                component.setBackground(new Color(0, 5, 255, 142)); //azulitoverdoso
            } else if (producto.getStock() == 0) {
                component.setBackground(new Color(253, 188, 60, 150)); // naranga
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
                component.setBackground(new Color(0, 5, 255, 142));
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

        } else {
            jTextField22.setVisible(false);
            jPasswordField2.setVisible(false);
            jComboBox12.setVisible(false);
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
        this.setSize(850, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        String imagePath = "src/img/compraventa-estancos.png"; // Reemplaza con la ruta correcta de tu imagen
        ImageIcon imageIcon = new ImageIcon(imagePath);
        this.setIconImage(imageIcon.getImage());

        String[] titulos_insertar_cliente = {"Nombre*", "Teléfono*", "Dirección*", "Código postal*", "Email*"};
        JTextField[] jtf_insertar_cliente = {jTextField34, jTextField35, jTextField36, jTextField37, jTextField38};
        for (int i = 0; i < jtf_insertar_cliente.length; i++) {
            TextField tf = (TextField) jtf_insertar_cliente[i];
            tf.setLabelText(titulos_insertar_cliente[i]);
        }
        jframe_insertar_clientes.setTitle("Insertar cliente");
        jframe_insertar_clientes.setSize(440, 575);
        jframe_insertar_clientes.setResizable(false);
        jframe_insertar_clientes.setLocationRelativeTo(null);
        jframe_insertar_clientes.setResizable(false);
        jframe_insertar_clientes.setIconImage(imageIcon.getImage());

        String[] titulos_insertar_empleado = {"Nombre*", "Primer apellido*", "Segundo apellido*", "DNI*", "Teléfono*", "Email*", "Salario*", "Usuario*"};
        JTextField[] jtf_insertar_empleado = {jTextField39, jTextField40, jTextField41, jTextField42, jTextField44, jTextField43, jTextField48, jTextField21};
        for (int i = 0; i < jtf_insertar_empleado.length; i++) {
            TextField tf = (TextField) jtf_insertar_empleado[i];
            tf.setLabelText(titulos_insertar_empleado[i]);
        }
        jframe_insertar_empleados.setTitle("Insertar empleado");
        jframe_insertar_empleados.setSize(440, 720);
        jframe_insertar_empleados.setResizable(false);
        jframe_insertar_empleados.setLocationRelativeTo(null);
        jframe_insertar_empleados.setResizable(false);
        jframe_insertar_empleados.setIconImage(imageIcon.getImage());

        String[] titulos_insertar_linea = {"Línea*"};
        JTextField[] jtf_insertar_linea = {jTextField33};
        for (int i = 0; i < jtf_insertar_linea.length; i++) {
            TextField tf = (TextField) jtf_insertar_linea[i];
            tf.setLabelText(titulos_insertar_linea[i]);
        }
        jframe_insertar_lineas.setTitle("Insertar línea de producto");
        jframe_insertar_lineas.setSize(440, 450);
        jframe_insertar_lineas.setResizable(false);
        jframe_insertar_lineas.setLocationRelativeTo(null);
        jframe_insertar_lineas.setResizable(false);
        jframe_insertar_lineas.setIconImage(imageIcon.getImage());

        String[] titulos_insertar_pedido = {"Cantidad"};
        JTextField[] jtf_insertar_pedido = {jTextField49};
        for (int i = 0; i < jtf_insertar_pedido.length; i++) {
            TextField tf = (TextField) jtf_insertar_pedido[i];
            tf.setLabelText(titulos_insertar_pedido[i]);
        }
        jframe_insertar_pedidos.setTitle("Insertar pedido");
        jframe_insertar_pedidos.setSize(1025, 475);
        jframe_insertar_pedidos.setResizable(false);
        jframe_insertar_pedidos.setLocationRelativeTo(null);
        jframe_insertar_pedidos.setResizable(false);
        jframe_insertar_pedidos.setIconImage(imageIcon.getImage());

        String[] titulos_insertar_producto = {"Producto*", "Descripción", "Stock*", "Precio*"};
        JTextField[] jtf_insertar_producto = {jTextField11, jTextField12, jTextField27, jTextField28};
        for (int i = 0; i < jtf_insertar_producto.length; i++) {
            TextField tf = (TextField) jtf_insertar_producto[i];
            tf.setLabelText(titulos_insertar_producto[i]);
        }
        jframe_insertar_productos.setTitle("Insertar producto");
        jframe_insertar_productos.setSize(440, 500);
        jframe_insertar_productos.setResizable(false);
        jframe_insertar_productos.setLocationRelativeTo(null);
        jframe_insertar_productos.setResizable(false);
        jframe_insertar_productos.setIconImage(imageIcon.getImage());

        String[] titulos_insertar_proveedor = {"Empresa*", "Contacto", "Teléfono*", "Sitio web*"};
        JTextField[] jtf_insertar_proveedor = {jTextField29, jTextField30, jTextField31, jTextField32};
        for (int i = 0; i < jtf_insertar_proveedor.length; i++) {
            TextField tf = (TextField) jtf_insertar_proveedor[i];
            tf.setLabelText(titulos_insertar_proveedor[i]);
        }
        jframe_insertar_proveedores.setTitle("Insertar proveedor");
        jframe_insertar_proveedores.setSize(440, 575);
        jframe_insertar_proveedores.setResizable(false);
        jframe_insertar_proveedores.setLocationRelativeTo(null);
        jframe_insertar_proveedores.setResizable(false);
        jframe_insertar_proveedores.setIconImage(imageIcon.getImage());

        String[] titulos_modificar_cliente = {"Nombre*", "Teléfono*", "Dirección*", "Código postal*", "Email*"};
        JTextField[] jtf_modificar_cliente = {jTextField13, jTextField14, jTextField15, jTextField16, jTextField10};
        for (int i = 0; i < jtf_modificar_cliente.length; i++) {
            TextField tf = (TextField) jtf_modificar_cliente[i];
            tf.setLabelText(titulos_modificar_cliente[i]);
        }
        jframe_modificar_clientes.setTitle("Modificar cliente");
        jframe_modificar_clientes.setSize(440, 575);
        jframe_modificar_clientes.setResizable(false);
        jframe_modificar_clientes.setLocationRelativeTo(null);
        jframe_modificar_clientes.setResizable(false);
        jframe_modificar_clientes.setIconImage(imageIcon.getImage());

        String[] titulos_modificar_empleado = {"Nombre*", "Primer apellido*", "Segundo apellido*", "DNI*", "Teléfono*", "Email*", "Salario*", "Usuario"};
        JTextField[] jtf_modificar_empleado = {jTextField17, jTextField18, jTextField19, jTextField20, jTextField25, jTextField26, jTextField47, jTextField22};
        for (int i = 0; i < jtf_modificar_empleado.length; i++) {
            TextField tf = (TextField) jtf_modificar_empleado[i];
            tf.setLabelText(titulos_modificar_empleado[i]);
        }
        jframe_modificar_empleados.setTitle("Modificar empleado");
        jframe_modificar_empleados.setSize(440, 720);
        jframe_modificar_empleados.setResizable(false);
        jframe_modificar_empleados.setLocationRelativeTo(null);
        jframe_modificar_empleados.setResizable(false);
        jframe_modificar_empleados.setIconImage(imageIcon.getImage());

        String[] titulos_modificar_linea = {"Línea*"};
        JTextField[] jtf_modificar_linea = {jTextField9};
        for (int i = 0; i < jtf_modificar_linea.length; i++) {
            TextField tf = (TextField) jtf_modificar_linea[i];
            tf.setLabelText(titulos_modificar_linea[i]);
        }
        jframe_modificar_lineas.setTitle("Modificar línea de producto");
        jframe_modificar_lineas.setSize(440, 450);
        jframe_modificar_lineas.setResizable(false);
        jframe_modificar_lineas.setLocationRelativeTo(null);
        jframe_modificar_lineas.setResizable(false);
        jframe_modificar_lineas.setIconImage(imageIcon.getImage());

        String[] titulos_modificar_productos = {"Producto*", "Descripción", "Stock*", "Precio*"};
        JTextField[] jtf_modificar_productos = {jTextField1, jTextField2, jTextField3, jTextField4};
        for (int i = 0; i < jtf_modificar_productos.length; i++) {
            TextField tf = (TextField) jtf_modificar_productos[i];
            tf.setLabelText(titulos_modificar_productos[i]);
        }
        jframe_modificar_productos.setTitle("Modificar producto");
        jframe_modificar_productos.setSize(440, 500);
        jframe_modificar_productos.setResizable(false);
        jframe_modificar_productos.setLocationRelativeTo(null);
        jframe_modificar_productos.setResizable(false);
        jframe_modificar_productos.setIconImage(imageIcon.getImage());

        String[] titulos_modificar_proveedor = {"Empresa*", "Contacto", "Teléfono*", "Sitio web*"};
        JTextField[] jtf_modificar_proveedor = {jTextField5, jTextField6, jTextField7, jTextField8};
        for (int i = 0; i < jtf_modificar_proveedor.length; i++) {
            TextField tf = (TextField) jtf_modificar_proveedor[i];
            tf.setLabelText(titulos_modificar_proveedor[i]);
        }
        jframe_modificar_proveedores.setTitle("Modificar proveedor");
        jframe_modificar_proveedores.setSize(440, 575);
        jframe_modificar_proveedores.setResizable(false);
        jframe_modificar_proveedores.setLocationRelativeTo(null);
        jframe_modificar_proveedores.setResizable(false);
        jframe_modificar_proveedores.setIconImage(imageIcon.getImage());

        String[] titulos_mostrar_detallespedido = {"ID pedido", "Cliente", "Empleado", "Fecha", "Total (€)"};
        JTextField[] jtf_mostrar_detallespedido = {jTextField45, jTextField23, jTextField24, jTextField46, jTextField50};
        for (int i = 0; i < jtf_mostrar_detallespedido.length; i++) {
            TextField tf = (TextField) jtf_mostrar_detallespedido[i];
            tf.setLabelText(titulos_mostrar_detallespedido[i]);
        }
        jframe_mostrar_detallespedido.setTitle("Detalles pedido");
        jframe_mostrar_detallespedido.setSize(850, 500);
        jframe_mostrar_detallespedido.setResizable(false);
        jframe_mostrar_detallespedido.setLocationRelativeTo(null);
        jframe_mostrar_detallespedido.setResizable(false);
        jframe_mostrar_detallespedido.setIconImage(imageIcon.getImage());
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

                String logoPath = "src/img/compraventa-estancos-200_x_200.png";
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

                PdfPTable tablaProductos = new PdfPTable(4);
                tablaProductos.setWidthPercentage(100);
                tablaProductos.addCell(new PdfPCell(new Phrase("ID")));
                tablaProductos.addCell(new PdfPCell(new Phrase("Producto")));
                tablaProductos.addCell(new PdfPCell(new Phrase("Cantidad")));
                tablaProductos.addCell(new PdfPCell(new Phrase("Precio/unidad")));

                LineSeparator lineSeparator2 = new LineSeparator();
                documento.add(espacioBlanco);
                documento.add(lineSeparator2);
                documento.add(espacioBlanco);
                for (Object[] producto : lista) {
                    tablaProductos.addCell(new PdfPCell(new Phrase(producto[0].toString())));
                    tablaProductos.addCell(new PdfPCell(new Phrase(producto[1].toString())));
                    tablaProductos.addCell(new PdfPCell(new Phrase(producto[2].toString())));
                    tablaProductos.addCell(new PdfPCell(new Phrase(producto[3].toString())));
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

        jframe_insertar_productos = new javax.swing.JFrame();
        jPanel7 = new javax.swing.JPanel();
        jTextField11 = new componentes.TextField();
        jComboBox5 = new componentes.Combobox<>("Línea de producto");
        jTextField12 = new componentes.TextField();
        jTextField27 = new componentes.TextField();
        jTextField28 = new componentes.TextField();
        jComboBox6 = new componentes.Combobox<>("Proveedor");
        insertar_producto = new componentes.ButtonGradient();
        jframe_modificar_productos = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new componentes.TextField();
        jComboBox1 = new componentes.Combobox<>("Línea de producto");
        jTextField2 = new componentes.TextField();
        jTextField3 = new componentes.TextField();
        jTextField4 = new componentes.TextField();
        jComboBox2 = new componentes.Combobox<>("Proveedor");
        modificar_producto = new componentes.ButtonGradient();
        jframe_insertar_proveedores = new javax.swing.JFrame();
        jPanel8 = new javax.swing.JPanel();
        jTextField29 = new componentes.TextField();
        jComboBox9 = new componentes.Combobox<>("Comunidad Autónoma");
        jComboBox10 = new componentes.Combobox<>("Ciudad");
        jTextField30 = new componentes.TextField();
        jTextField31 = new componentes.TextField();
        jTextField32 = new componentes.TextField();
        insertar_proveedor = new componentes.ButtonGradient();
        jframe_modificar_proveedores = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField5 = new componentes.TextField();
        jComboBox3 = new componentes.Combobox<>("Comunidad Autónoma");
        jComboBox4 = new componentes.Combobox<>("Ciudad");
        jTextField6 = new componentes.TextField();
        jTextField7 = new componentes.TextField();
        jTextField8 = new componentes.TextField();
        modificar_proveedor = new componentes.ButtonGradient();
        jframe_insertar_lineas = new javax.swing.JFrame();
        jPanel9 = new javax.swing.JPanel();
        jTextField33 = new componentes.TextField();
        jScrollPane8 = new componentes.TextAreaScroll();
        jTextArea2 = new componentes.TextArea();
        insertar_linea = new componentes.ButtonGradient();
        jframe_modificar_lineas = new javax.swing.JFrame();
        jPanel3 = new javax.swing.JPanel();
        jTextField9 = new componentes.TextField();
        modificar_linea = new componentes.ButtonGradient();
        jScrollPane7 = new componentes.TextAreaScroll();
        jTextArea1 = new componentes.TextArea();
        jLabel3 = new javax.swing.JLabel();
        jframe_insertar_clientes = new javax.swing.JFrame();
        jPanel10 = new javax.swing.JPanel();
        jTextField34 = new componentes.TextField();
        jTextField35 = new componentes.TextField();
        jTextField36 = new componentes.TextField();
        jComboBox13 = new componentes.Combobox<>("Comunidad Autónoma");
        jComboBox14 = new componentes.Combobox<>("Ciudad");
        jTextField37 = new componentes.TextField();
        jTextField38 = new componentes.TextField();
        insertar_cliente = new componentes.ButtonGradient();
        jframe_modificar_clientes = new javax.swing.JFrame();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField13 = new componentes.TextField();
        jTextField14 = new componentes.TextField();
        jTextField15 = new componentes.TextField();
        jComboBox7 = new componentes.Combobox<>("Comunidad Autónoma");
        jComboBox8 = new componentes.Combobox<>("Ciudad");
        jTextField16 = new componentes.TextField();
        jTextField10 = new componentes.TextField();
        modificar_cliente = new componentes.ButtonGradient();
        jframe_insertar_empleados = new javax.swing.JFrame();
        jPanel11 = new javax.swing.JPanel();
        jTextField39 = new componentes.TextField();
        jTextField40 = new componentes.TextField();
        jTextField41 = new componentes.TextField();
        jTextField42 = new componentes.TextField();
        jTextField44 = new componentes.TextField();
        jTextField43 = new componentes.TextField();
        jTextField48 = new componentes.TextField();
        insertar_empleado = new componentes.ButtonGradient();
        jCheckBox1 = new componentes.JCheckBoxCustom();
        jComboBox11 = new componentes.Combobox<>("Privilegios");
        jTextField21 = new componentes.TextField();
        jPasswordField1 = new componentes.PasswordField();
        jframe_modificar_empleados = new javax.swing.JFrame();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField17 = new componentes.TextField();
        jTextField18 = new componentes.TextField();
        jTextField19 = new componentes.TextField();
        jTextField20 = new componentes.TextField();
        jTextField25 = new componentes.TextField();
        jTextField26 = new componentes.TextField();
        jTextField47 = new componentes.TextField();
        modificar_empleado = new componentes.ButtonGradient();
        jComboBox12 = new componentes.Combobox<>("Privilegios");
        jTextField22 = new componentes.TextField();
        jPasswordField2 = new componentes.PasswordField();
        jframe_insertar_pedidos = new javax.swing.JFrame();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        insertar_pedido = new componentes.ButtonGradient();
        jComboBox15 = new componentes.Combobox<>("Cliente");
        jScrollPane11 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList<>();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList<>();
        jTextField49 = new componentes.TextField();
        jButton1 = new componentes.ButtonGradient();
        jButton2 = new componentes.ButtonGradient();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jframe_mostrar_detallespedido = new javax.swing.JFrame();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField45 = new componentes.TextField();
        jTextField23 = new componentes.TextField();
        jTextField24 = new componentes.TextField();
        jTextField46 = new componentes.TextField();
        jTextField50 = new componentes.TextField();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new componentes.ButtonGradient();
        jPanel13 = new javax.swing.JPanel();
        modificar = new componentes.ButtonGradient();
        eliminar = new componentes.ButtonGradient();
        insertar = new componentes.ButtonGradient();
        jTabbedPane1 = new componentes.TabbedPaneCustom();
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jm_usuario = new javax.swing.JMenu();
        jmi_cerrarSesion = new javax.swing.JMenuItem();

        jframe_insertar_productos.setSize(new java.awt.Dimension(500, 500));
        jframe_insertar_productos.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 240, 40));

        jPanel7.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 240, 40));
        jPanel7.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 240, 40));
        jPanel7.add(jTextField27, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 240, 40));
        jPanel7.add(jTextField28, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 240, 40));

        jPanel7.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, 240, 40));

        insertar_producto.setText("Insertar");
        insertar_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_productoActionPerformed(evt);
            }
        });
        jPanel7.add(insertar_producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 390, 160, 30));

        jframe_insertar_productos.getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 610));

        jframe_modificar_productos.setResizable(false);
        jframe_modificar_productos.setSize(new java.awt.Dimension(500, 500));
        jframe_modificar_productos.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("ID: X");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 50, 20));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 240, 50));

        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 240, 40));
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 240, 50));
        jPanel1.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 240, 50));
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 240, 50));

        jPanel1.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 240, 40));

        modificar_producto.setText("Modificar");
        modificar_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_productoActionPerformed(evt);
            }
        });
        jPanel1.add(modificar_producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 410, 160, 30));

        jframe_modificar_productos.getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 520));

        jframe_insertar_proveedores.setMinimumSize(new java.awt.Dimension(500, 500));
        jframe_insertar_proveedores.setSize(new java.awt.Dimension(500, 500));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel8.add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 240, 50));

        jComboBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });
        jPanel8.add(jComboBox9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 240, 40));

        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });
        jPanel8.add(jComboBox10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 240, 40));
        jPanel8.add(jTextField30, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 240, 50));
        jPanel8.add(jTextField31, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 240, 50));
        jPanel8.add(jTextField32, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, 240, 50));

        insertar_proveedor.setText("Insertar");
        insertar_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_proveedorActionPerformed(evt);
            }
        });
        jPanel8.add(insertar_proveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, 160, 30));

        javax.swing.GroupLayout jframe_insertar_proveedoresLayout = new javax.swing.GroupLayout(jframe_insertar_proveedores.getContentPane());
        jframe_insertar_proveedores.getContentPane().setLayout(jframe_insertar_proveedoresLayout);
        jframe_insertar_proveedoresLayout.setHorizontalGroup(
            jframe_insertar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
        );
        jframe_insertar_proveedoresLayout.setVerticalGroup(
            jframe_insertar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
        );

        jframe_modificar_proveedores.setSize(new java.awt.Dimension(500, 500));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("ID: X");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 60, 30));
        jPanel2.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 240, 50));

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 240, 40));

        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 240, 40));
        jPanel2.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 240, 50));
        jPanel2.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 240, 50));
        jPanel2.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 240, 50));

        modificar_proveedor.setText("Modificar");
        modificar_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_proveedorActionPerformed(evt);
            }
        });
        jPanel2.add(modificar_proveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 390, 160, 30));

        javax.swing.GroupLayout jframe_modificar_proveedoresLayout = new javax.swing.GroupLayout(jframe_modificar_proveedores.getContentPane());
        jframe_modificar_proveedores.getContentPane().setLayout(jframe_modificar_proveedoresLayout);
        jframe_modificar_proveedoresLayout.setHorizontalGroup(
            jframe_modificar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
        );
        jframe_modificar_proveedoresLayout.setVerticalGroup(
            jframe_modificar_proveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
        );

        jframe_insertar_lineas.setSize(new java.awt.Dimension(500, 500));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel9.add(jTextField33, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 240, 50));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane8.setViewportView(jTextArea2);

        jPanel9.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 260, 130));

        insertar_linea.setText("Insertar");
        insertar_linea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_lineaActionPerformed(evt);
            }
        });
        jPanel9.add(insertar_linea, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 160, 30));

        javax.swing.GroupLayout jframe_insertar_lineasLayout = new javax.swing.GroupLayout(jframe_insertar_lineas.getContentPane());
        jframe_insertar_lineas.getContentPane().setLayout(jframe_insertar_lineasLayout);
        jframe_insertar_lineasLayout.setHorizontalGroup(
            jframe_insertar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
        );
        jframe_insertar_lineasLayout.setVerticalGroup(
            jframe_insertar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );

        jframe_modificar_lineas.setSize(new java.awt.Dimension(500, 500));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 240, 50));

        modificar_linea.setText("Modificar");
        modificar_linea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_lineaActionPerformed(evt);
            }
        });
        jPanel3.add(modificar_linea, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 310, 160, 30));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane7.setViewportView(jTextArea1);

        jPanel3.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 260, 130));

        jLabel3.setText("jLabel3");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 90, 30));

        javax.swing.GroupLayout jframe_modificar_lineasLayout = new javax.swing.GroupLayout(jframe_modificar_lineas.getContentPane());
        jframe_modificar_lineas.getContentPane().setLayout(jframe_modificar_lineasLayout);
        jframe_modificar_lineasLayout.setHorizontalGroup(
            jframe_modificar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
        );
        jframe_modificar_lineasLayout.setVerticalGroup(
            jframe_modificar_lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );

        jframe_insertar_clientes.setSize(new java.awt.Dimension(500, 500));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jTextField34, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 240, 50));
        jPanel10.add(jTextField35, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 240, 50));
        jPanel10.add(jTextField36, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 240, 50));

        jComboBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox13ActionPerformed(evt);
            }
        });
        jPanel10.add(jComboBox13, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 220, 240, 40));

        jComboBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox14ActionPerformed(evt);
            }
        });
        jPanel10.add(jComboBox14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 240, 40));
        jPanel10.add(jTextField37, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 240, 50));
        jPanel10.add(jTextField38, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, 240, 50));

        insertar_cliente.setText("Insertar");
        insertar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_clienteActionPerformed(evt);
            }
        });
        jPanel10.add(insertar_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 420, 160, 30));

        javax.swing.GroupLayout jframe_insertar_clientesLayout = new javax.swing.GroupLayout(jframe_insertar_clientes.getContentPane());
        jframe_insertar_clientes.getContentPane().setLayout(jframe_insertar_clientesLayout);
        jframe_insertar_clientesLayout.setHorizontalGroup(
            jframe_insertar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
        );
        jframe_insertar_clientesLayout.setVerticalGroup(
            jframe_insertar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
        );

        jframe_modificar_clientes.setSize(new java.awt.Dimension(500, 500));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setText("ID: X");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 190, 40));
        jPanel4.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 240, 50));
        jPanel4.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 240, 50));
        jPanel4.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 240, 50));

        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });
        jPanel4.add(jComboBox7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 240, 40));

        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });
        jPanel4.add(jComboBox8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 240, 40));
        jPanel4.add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, 240, 50));
        jPanel4.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 400, 240, 50));

        modificar_cliente.setText("Modificar");
        modificar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_clienteActionPerformed(evt);
            }
        });
        jPanel4.add(modificar_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 470, 160, 30));

        javax.swing.GroupLayout jframe_modificar_clientesLayout = new javax.swing.GroupLayout(jframe_modificar_clientes.getContentPane());
        jframe_modificar_clientes.getContentPane().setLayout(jframe_modificar_clientesLayout);
        jframe_modificar_clientesLayout.setHorizontalGroup(
            jframe_modificar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );
        jframe_modificar_clientesLayout.setVerticalGroup(
            jframe_modificar_clientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
        );

        jframe_insertar_empleados.setSize(new java.awt.Dimension(500, 500));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel11.add(jTextField39, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 240, 50));
        jPanel11.add(jTextField40, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 240, 50));
        jPanel11.add(jTextField41, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 240, 50));
        jPanel11.add(jTextField42, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 220, 240, 50));
        jPanel11.add(jTextField44, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 240, 50));
        jPanel11.add(jTextField43, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, 240, 50));
        jPanel11.add(jTextField48, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 400, 240, 50));

        insertar_empleado.setText("Insertar");
        insertar_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertar_empleadoActionPerformed(evt);
            }
        });
        jPanel11.add(insertar_empleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 640, 160, 30));

        jCheckBox1.setText("Asignar usuario");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel11.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 450, -1, -1));

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "user", "admin" }));
        jPanel11.add(jComboBox11, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 590, 240, 40));
        jPanel11.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 480, 240, 50));
        jPanel11.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 540, 240, 40));

        javax.swing.GroupLayout jframe_insertar_empleadosLayout = new javax.swing.GroupLayout(jframe_insertar_empleados.getContentPane());
        jframe_insertar_empleados.getContentPane().setLayout(jframe_insertar_empleadosLayout);
        jframe_insertar_empleadosLayout.setHorizontalGroup(
            jframe_insertar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jframe_insertar_empleadosLayout.setVerticalGroup(
            jframe_insertar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        jframe_modificar_empleados.setSize(new java.awt.Dimension(500, 500));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("ID: X");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 40, -1));
        jPanel5.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 240, 50));
        jPanel5.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 240, 50));
        jPanel5.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 240, 50));
        jPanel5.add(jTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, 240, 50));
        jPanel5.add(jTextField25, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, 240, 50));
        jPanel5.add(jTextField26, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 370, 240, 50));
        jPanel5.add(jTextField47, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 430, 240, 50));

        modificar_empleado.setText("Modificar");
        modificar_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_empleadoActionPerformed(evt);
            }
        });
        jPanel5.add(modificar_empleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 650, 160, 30));

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "user", "admin" }));
        jPanel5.add(jComboBox12, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 600, 240, 40));
        jPanel5.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 490, 240, 50));
        jPanel5.add(jPasswordField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 550, 240, 40));

        javax.swing.GroupLayout jframe_modificar_empleadosLayout = new javax.swing.GroupLayout(jframe_modificar_empleados.getContentPane());
        jframe_modificar_empleados.getContentPane().setLayout(jframe_modificar_empleadosLayout);
        jframe_modificar_empleadosLayout.setHorizontalGroup(
            jframe_modificar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jframe_modificar_empleadosLayout.setVerticalGroup(
            jframe_modificar_empleadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        jframe_insertar_pedidos.setSize(new java.awt.Dimension(500, 500));
        jframe_insertar_pedidos.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
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
        jPanel12.add(insertar_pedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 380, 160, 30));

        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel12.add(jComboBox15, new org.netbeans.lib.awtextra.AbsoluteConstraints(722, 329, 240, 40));

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
        jPanel12.add(jTextField49, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, 190, 40));

        jButton1.setText("Añadir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 160, 160, 30));

        jButton2.setText("Eliminar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, 160, 30));

        jLabel58.setText("ID");
        jPanel12.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 11, -1, -1));

        jLabel59.setText("Producto");
        jPanel12.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 11, -1, -1));

        jframe_insertar_pedidos.getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1070, 490));

        jframe_mostrar_detallespedido.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
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

        jframe_mostrar_detallespedido.getContentPane().add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 458, 468));

        jTextField45.setEnabled(false);
        jframe_mostrar_detallespedido.getContentPane().add(jTextField45, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 38, 240, 50));

        jTextField23.setEnabled(false);
        jframe_mostrar_detallespedido.getContentPane().add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 108, 240, 50));

        jTextField24.setEnabled(false);
        jframe_mostrar_detallespedido.getContentPane().add(jTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 180, 240, 50));

        jTextField46.setEnabled(false);
        jframe_mostrar_detallespedido.getContentPane().add(jTextField46, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 261, 240, 50));

        jTextField50.setEnabled(false);
        jframe_mostrar_detallespedido.getContentPane().add(jTextField50, new org.netbeans.lib.awtextra.AbsoluteConstraints(494, 340, 240, 50));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jButton3.setText("Exportar factura");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(534, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(316, 316, 316))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(412, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        jframe_mostrar_detallespedido.getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 500));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        modificar.setText("Modificar");
        modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarActionPerformed(evt);
            }
        });

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        insertar.setText("Insertar");
        insertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertarActionPerformed(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tabla_productos.setAutoCreateRowSorter(true);
        tabla_productos.setBackground(new java.awt.Color(255, 255, 255));
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

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        tabla_proveedores.setAutoCreateRowSorter(true);
        tabla_proveedores.setBackground(new java.awt.Color(255, 255, 255));
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

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        tabla_lineasproductos.setAutoCreateRowSorter(true);
        tabla_lineasproductos.setBackground(new java.awt.Color(255, 255, 255));
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

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));

        tabla_clientes.setAutoCreateRowSorter(true);
        tabla_clientes.setBackground(new java.awt.Color(255, 255, 255));
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

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));

        tabla_empleados.setAutoCreateRowSorter(true);
        tabla_empleados.setBackground(new java.awt.Color(255, 255, 255));
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

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));

        tabla_pedidos.setAutoCreateRowSorter(true);
        tabla_pedidos.setBackground(new java.awt.Color(255, 255, 255));
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

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 858, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(insertar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(modificar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(eliminar)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(modificar)
                        .addComponent(eliminar)
                        .addComponent(insertar))
                    .addGap(575, 575, 575)))
        );

        getContentPane().add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 880, 600));

        jMenuBar1.setBackground(new java.awt.Color(135, 32, 49));
        jMenuBar1.setOpaque(true);

        jm_usuario.setText("Usuario");
        jMenuBar1.add(Box.createHorizontalGlue());

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

    private void modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarActionPerformed
        // TODO add your handling code here:
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Productos") && tabla_productos.getSelectedRow() != -1) {
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
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Proveedores") && tabla_proveedores.getSelectedRow() != -1) {
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
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Líneas productos") && tabla_lineasproductos.getSelectedRow() != -1) {
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
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes") && tabla_clientes.getSelectedRow() != -1) {
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
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Empleados") && tabla_empleados.getSelectedRow() != -1) {
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
    }//GEN-LAST:event_modificarActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        // TODO add your handling code here:

        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Productos")) {
            if (tabla_productos.getSelectedRow() != -1) {
                try {
                    // Obtener los datos de la fila seleccionada
                    if (JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar el producto?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        int id = (int) dtm_productos.getValueAt(tabla_productos.getSelectedRow(), 0);
                        conexion.conectar();
                        conexion.eliminarProducto(id);
                        conexion.desconectar();
                        actualizarTablas();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Proveedores")) {
            if (tabla_proveedores.getSelectedRow() != -1) {
                try {
                    if (JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar el proveedor?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        int id = (int) dtm_proveedores.getValueAt(tabla_proveedores.getSelectedRow(), 0);
                        conexion.conectar();
                        conexion.eliminarProveedor(id);
                        conexion.desconectar();
                        actualizarTablas();
                    }
                    // Obtener los datos de la fila seleccionada
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Líneas productos")) {
            if (tabla_lineasproductos.getSelectedRow() != -1) {
                try {
                    if (JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar la línea de productos?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        String id = (String) dtm_lineasproductos.getValueAt(tabla_lineasproductos.getSelectedRow(), 0);
                        conexion.conectar();
                        conexion.eliminarLineaProducto(id);
                        conexion.desconectar();
                        actualizarTablas();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes")) {
            if (tabla_clientes.getSelectedRow() != -1) {
                try {
                    if (JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar el cliente?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        int id = (int) dtm_clientes.getValueAt(tabla_clientes.getSelectedRow(), 0);
                        conexion.conectar();
                        conexion.eliminarCliente(id);
                        conexion.desconectar();
                        actualizarTablas();

                    }
                    // Obtener los datos de la fila seleccionada
                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Empleados")) {
            if (tabla_empleados.getSelectedRow() != -1) {
                try {
                    if (dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), 8).equals(usuario.getUsuario().getIdUsuario())) {
                        JOptionPane.showMessageDialog(null, "No puedes eliminarte a ti mismo", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar el empleado?", "Confirmación de eliminación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            int id = (int) dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), 0);
                            conexion.conectar();
                            conexion.eliminarEmpleado(id);
                            if (!dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), 8).equals(null)) {
                                int idusuario = (int) dtm_empleados.getValueAt(tabla_empleados.getSelectedRow(), 8);
                                conexion.eliminarUsuario(idusuario);
                            }
                            conexion.desconectar();
                            actualizarTablas();
                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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
            jTextField11.setText("");
            jTextField12.setText("");
            jTextField27.setText("");
            jTextField28.setText("");
            jComboBox5.setSelectedIndex(0);
            jComboBox6.setSelectedIndex(0);
            jframe_insertar_productos.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Proveedores")) {
            jTextField29.setText("");
            jTextField30.setText("");
            jTextField31.setText("");
            jTextField32.setText("");
            jComboBox9.setSelectedIndex(0);
            jComboBox10.setSelectedIndex(0);
            jframe_insertar_proveedores.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Líneas productos")) {
            jTextField33.setText("");
            jTextArea2.setText("");
            jframe_insertar_lineas.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Clientes")) {
            jTextField34.setText("");
            jTextField35.setText("");
            jTextField36.setText("");
            jTextField37.setText("");
            jTextField38.setText("");
            jComboBox13.setSelectedIndex(0);
            jComboBox14.setSelectedIndex(0);
            jframe_insertar_clientes.show();

        }
        if (jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()).equals("Empleados")) {
            jTextField39.setText("");
            jTextField40.setText("");
            jTextField41.setText("");
            jTextField42.setText("");
            jTextField44.setText("");
            jTextField43.setText("");
            jTextField48.setText("");
            jTextField21.setText("");
            jPasswordField1.setText("");
            jComboBox11.setSelectedIndex(0);
            jTextField21.setVisible(false);
            jPasswordField1.setVisible(false);
            jComboBox11.setVisible(false);
            if (jCheckBox1.isSelected()) {
                jCheckBox1.setSelected(false);
            }
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

        } else {
            jTextField21.setVisible(false);
            jPasswordField1.setVisible(false);
            jComboBox11.setVisible(false);

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

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
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
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
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
