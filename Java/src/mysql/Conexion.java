/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysql;

/**
 *
 * @author oscar
 */
import clases.*;
import componentes.*;
import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.table.DefaultTableModel;
import java.lang.Runtime;

public class Conexion {

    private final String db = "jdbc:mysql://localhost:3306/crud_app";
    private final String db_user = "root";
    private final String db_password = "root";
    private Connection connection;
    private static Conexion instance = new Conexion();

    private Conexion() {

    }

    public static Conexion getInstance() {
        return instance;
    }

    public void conectar() throws SQLException {
        connection = DriverManager.getConnection(db, db_user, db_password);
        
    }

    public void desconectar() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public void insertarProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombreproducto, lineaproducto, descripcion, cantidadEnStock, pvp, proveedor) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getLineaProducto().toString());
            statement.setString(3, producto.getDescripcion());
            statement.setInt(4, producto.getStock());
            statement.setDouble(5, producto.getPvp());
            statement.setInt(6, producto.getProveedor().getIdproveedor());

            statement.executeUpdate();
        }
    }

    public void eliminarProducto(int idProducto) throws SQLException {
        String sql = "UPDATE productos SET eliminado = 1 WHERE idproducto = ?";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProducto);

            statement.executeUpdate();
        }
    }

    public void eliminarProveedor(int idProveedor) throws SQLException {
        String sql = "DELETE FROM proveedores WHERE idproveedor = ?";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProveedor);

            statement.executeUpdate();
        }
    }

    public void eliminarLineaProducto(String linea) throws SQLException {
        String sql = "DELETE FROM lineasproducto WHERE lineaproducto = ?";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, linea);

            statement.executeUpdate();
        }
    }

    public void eliminarCliente(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE idcliente = ?";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCliente);

            statement.executeUpdate();
        }
    }

    public void eliminarEmpleado(int idEmpleado) throws SQLException {
        String sql = "DELETE FROM empleados WHERE idempleados = ?";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idEmpleado);

            statement.executeUpdate();
        }
    }

    public void eliminarUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE idusuario = ?";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);

            statement.executeUpdate();
        }
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombreproducto = ?, lineaproducto = ?, descripcion = ?, "
                + "cantidadEnStock = ?, pvp = ?, proveedor = ?, eliminado = ? WHERE idproducto = ?";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getLineaProducto().getLinea());
            statement.setString(3, producto.getDescripcion());
            statement.setInt(4, producto.getStock());
            statement.setDouble(5, producto.getPvp());
            statement.setInt(6, producto.getProveedor().getIdproveedor());
            statement.setBoolean(7, producto.isEliminado());
            statement.setInt(8, producto.getIdProducto());

            statement.executeUpdate();
        }
    }

    public void actualizarProveedor(Proveedor proveedor) throws SQLException {
        String sql = "UPDATE proveedores SET nombreEmpresa = ?, nombreContacto = ?, ciudad = ?, telefono = ?, paginaweb = ? WHERE idproveedor = ?";;

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, proveedor.getNombreEmpresa());
            statement.setString(2, proveedor.getNombreContacto());
            statement.setInt(3, proveedor.getCiudad().getIdciudad());
            statement.setString(4, proveedor.getTelefono());
            statement.setString(5, proveedor.getPaginaweb());
            statement.setInt(6, proveedor.getIdproveedor());

            statement.executeUpdate();
        }
    }

    public void actualizarLineasProducto(LineaProducto linea, String id) throws SQLException {
        String sql = "UPDATE lineasproducto SET lineaproducto = ?, descripcion = ? WHERE lineaproducto = ?";;

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, linea.getLinea());
            statement.setString(2, linea.getDescripcion());
            statement.setString(3, id);

            statement.executeUpdate();
        }
    }

    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, direccion = ?, ciudad = ?, codigoPostal = ?, email = ? WHERE idcliente = ?";;

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getTelefono());
            statement.setString(3, cliente.getDireccion());
            statement.setInt(4, cliente.getCiudad().getIdciudad());
            statement.setString(5, cliente.getCodigoPostal());
            statement.setString(6, cliente.getEmail());
            statement.setInt(7, cliente.getIdClientes());

            statement.executeUpdate();
        }
    }

    public void actualizarEmpleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleados SET nombre = ?, apellido1 = ?, apellido2 = ?, DNI = ?, telefono = ?, email = ?, salario = ? WHERE idempleados = ?";;

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, empleado.getNombre());
            statement.setString(2, empleado.getApellido1());
            statement.setString(3, empleado.getApellido2());
            statement.setString(4, empleado.getDni());
            statement.setString(5, empleado.getTelefono());
            statement.setString(6, empleado.getEmail());
            statement.setFloat(7, empleado.getSalario());
            statement.setInt(8, empleado.getIdempleado());
            if (empleado.getUsuario() instanceof Usuario) {
                actualizarUsuario(empleado.getUsuario(), empleado.getIdempleado());
            }
            statement.executeUpdate();
        }
    }

    public void actualizarUsuario(Usuario usuario, int idEmpleado) throws SQLException {
        String select = "SELECT * FROM empleados WHERE idempleados = ?";
        int idusuario = 0;
        try ( PreparedStatement statement = connection.prepareStatement(select)) {
            statement.setInt(1, idEmpleado);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Procesar los datos del resultado
                idusuario = resultSet.getInt("idusuario");
                // ... obtener otros datos necesarios
            }

            resultSet.close();

        }

        String sql = "UPDATE usuarios SET usuario = ?, pass = ?, privilegios = ? WHERE idusuario = ?";;

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getUsuario());
            statement.setString(2, BCrypt.hashpw(usuario.getPass(), BCrypt.gensalt()));
            statement.setString(3, usuario.getRole());
            statement.setInt(4, idusuario);
            statement.executeUpdate();
        }
    }

    public List<Producto> seleccionarProductos() throws SQLException {
        String sql = "SELECT * FROM productos LEFT JOIN lineasproducto using(lineaproducto) LEFT JOIN proveedores ON productos.proveedor = proveedores.idproveedor LEFT JOIN ciudades_ccaa ON proveedores.ciudad = ciudades_ccaa.idciudad";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Producto> productos = new ArrayList<>();
        try {

            while (resultSet.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultSet.getInt("idproducto"));
                producto.setNombre(resultSet.getString("nombreproducto"));

                producto.setLineaProducto(new LineaProducto(resultSet.getString("lineaproducto"), resultSet.getString("lineasproducto.descripcion")));
                if (resultSet.wasNull()) {
                    producto.setLineaProducto(new LineaProducto(" ", " "));
                }

                producto.setDescripcion(resultSet.getString("descripcion"));
                producto.setStock(resultSet.getInt("cantidadEnStock"));
                producto.setPvp(resultSet.getFloat("pvp"));
                producto.setProveedor(new Proveedor(resultSet.getInt("proveedores.idproveedor"), resultSet.getString("proveedores.nombreEmpresa"), resultSet.getString("proveedores.nombreContacto"), new Ciudad(resultSet.getInt("idciudad"), resultSet.getInt("idCCAA"), resultSet.getString("nombreCiudad"), resultSet.getString("nombreComunidad")), resultSet.getString("proveedores.telefono"), resultSet.getString("proveedores.paginaweb")));
                if (resultSet.wasNull()) {
                    producto.setProveedor(new Proveedor(-1, " ", " ", new Ciudad(-1, -1, " ", " "), " ", " "));
                }
                producto.setEliminado(resultSet.getBoolean("eliminado"));
                productos.add(producto);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return productos;
    }

    public List<LineaProducto> seleccionarLineasProducto() throws SQLException {
        String sql = "SELECT * FROM lineasproducto";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<LineaProducto> lineas_productos = new ArrayList<>();
        try {

            while (resultSet.next()) {
                LineaProducto linea = new LineaProducto();
                linea.setLinea(resultSet.getString("lineaproducto"));
                linea.setDescripcion(resultSet.getString("descripcion"));
                lineas_productos.add(linea);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return lineas_productos;
    }

    public List<Proveedor> seleccionarProveedores() throws SQLException {
        String sql = "SELECT * FROM proveedores INNER JOIN ciudades_ccaa ON proveedores.ciudad = ciudades_ccaa.idciudad";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Proveedor> proveedores = new ArrayList<>();
        try {

            while (resultSet.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setIdproveedor(resultSet.getInt("idproveedor"));
                proveedor.setNombreEmpresa(resultSet.getString("nombreEmpresa"));
                proveedor.setNombreContacto(resultSet.getString("nombreContacto"));
                proveedor.setCiudad(new Ciudad(resultSet.getInt("idciudad"), resultSet.getInt("idCCAA"), resultSet.getString("nombreCiudad"), resultSet.getString("nombreComunidad")));
                proveedor.setTelefono(resultSet.getString("telefono"));
                proveedor.setPaginaweb(resultSet.getString("paginaweb"));
                proveedores.add(proveedor);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return proveedores;
    }

    public List<Cliente> seleccionarClientes() throws SQLException {
        String sql = "SELECT * FROM clientes INNER JOIN ciudades_ccaa ON clientes.ciudad = ciudades_ccaa.idciudad";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Cliente> clientes = new ArrayList<>();
        try {

            while (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdClientes(resultSet.getInt("idcliente"));
                cliente.setNombre(resultSet.getString("nombre"));
                cliente.setTelefono(resultSet.getString("telefono"));
                cliente.setDireccion(resultSet.getString("direccion"));
                cliente.setCiudad(new Ciudad(resultSet.getInt("idciudad"), resultSet.getInt("idCCAA"), resultSet.getString("nombreCiudad"), resultSet.getString("nombreComunidad")));
                cliente.setCodigoPostal(resultSet.getString("codigoPostal"));
                cliente.setEmail(resultSet.getString("email"));
                clientes.add(cliente);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return clientes;
    }

    public List<Empleado> seleccionarEmpleados() throws SQLException {
        String sql = "SELECT * FROM empleados LEFT JOIN usuarios using(idusuario)";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Empleado> empleados = new ArrayList<>();
        try {

            while (resultSet.next()) {
                Empleado empleado = new Empleado();
                empleado.setIdempleado(resultSet.getInt("idempleados"));
                empleado.setNombre(resultSet.getString("nombre"));
                empleado.setApellido1(resultSet.getString("apellido1"));
                empleado.setApellido2(resultSet.getString("apellido2"));
                empleado.setDni(resultSet.getString("DNI"));
                empleado.setTelefono(resultSet.getString("telefono"));
                empleado.setEmail(resultSet.getString("email"));
                empleado.setSalario(resultSet.getFloat("salario"));
                empleado.setUsuario(new Usuario(resultSet.getString("usuario"), resultSet.getInt("idusuario"), resultSet.getString("privilegios")));
                empleados.add(empleado);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return empleados;
    }

    public List<DetallesPedido> seleccionarDetallesPedidos(int idpedido) throws SQLException {
        String sql = "SELECT  * FROM detallespedido WHERE idpedido = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idpedido);
        ResultSet resultSet = statement.executeQuery();
        List<DetallesPedido> pedidos = new ArrayList<>();
        try {

            while (resultSet.next()) {
                DetallesPedido pedido = new DetallesPedido();
                pedido.setIdproducto(resultSet.getInt("idproducto"));
                pedido.setCantidad(resultSet.getInt("cantidad"));
                pedido.setPrecio_venta(resultSet.getFloat("precio_venta"));
                pedidos.add(pedido);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return pedidos;
    }

    public List<Pedido> seleccionarPedidos() throws SQLException {
        String sql = "SELECT * FROM pedidos";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Pedido> pedidos = new ArrayList<>();
        try {

            while (resultSet.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdpedido(resultSet.getInt("idpedido"));
                Cliente cliente = new Cliente();
                cliente.setIdClientes(resultSet.getInt("idcliente"));
                pedido.setCliente(cliente);
                Empleado empleado = new Empleado();
                empleado.setIdempleado(resultSet.getInt("idempleado"));
                pedido.setEmpleado(empleado);
                pedido.setFechaPedido(resultSet.getDate("fechaPedido"));
                pedidos.add(pedido);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return pedidos;
    }

    public List<Ciudad> seleccionarCiudades() throws SQLException {
        String sql = "SELECT * FROM ciudades_ccaa";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Ciudad> ciudades = new ArrayList<>();
        try {

            while (resultSet.next()) {
                Ciudad ciudad = new Ciudad();
                ciudad.setIdciudad(resultSet.getInt("idciudad"));
                ciudad.setIdccaa(resultSet.getInt("idCCAA"));
                ciudad.setNombreCiudad(resultSet.getString("nombreCiudad"));
                ciudad.setNombreComunidad(resultSet.getString("nombreComunidad"));
                ciudades.add(ciudad);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException exception) {
            System.out.println(exception);
        }
        return ciudades;
    }

    public void insertarPedido(Pedido pedido, List<DetallesPedido> detallesPedido) throws SQLException {
        String sql = "INSERT INTO `crud_app`.`pedidos`(`idcliente`,`idempleado`,`fechaPedido`) VALUES (?,?,curdate());";
        try ( PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, pedido.getCliente().getIdClientes());
            statement.setInt(2, pedido.getEmpleado().getIdempleado());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            int idpedido = -1;
            if (generatedKeys.next()) {
                idpedido = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve the generated ID for the pedido.");
            }

            String insertDetallesQuery = "INSERT INTO detallespedido (idpedido, idproducto, cantidad, precio_venta) VALUES (?, ?, ?, ?)";
            try ( PreparedStatement insertDetallesStatement = connection.prepareStatement(insertDetallesQuery)) {
                for (DetallesPedido detallePedido : detallesPedido) {
                    insertDetallesStatement.setInt(1, idpedido);
                    insertDetallesStatement.setInt(2, detallePedido.getIdproducto());
                    insertDetallesStatement.setInt(3, detallePedido.getCantidad());
                    float precio = 0;
                    try ( PreparedStatement selectPVP = connection.prepareStatement("SELECT * FROM productos WHERE idproducto = ?")) {
                        selectPVP.setInt(1, detallePedido.getIdproducto());
                        ResultSet resultSet = selectPVP.executeQuery();
                        if (resultSet.next()) {
                            precio = resultSet.getFloat("pvp");
                        }
                    }
                    insertDetallesStatement.setFloat(4, precio);
                    insertDetallesStatement.executeUpdate();
                }
            }
        }

    }

    public void insertarProveedor(Proveedor proveedor) throws SQLException {
        String sql = "INSERT INTO proveedores (nombreEmpresa, nombreContacto, ciudad, telefono, paginaweb) VALUES (?, ?, ?, ?, ?)";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, proveedor.getNombreEmpresa());
            statement.setString(2, proveedor.getNombreContacto());
            statement.setInt(3, proveedor.getCiudad().getIdciudad());
            statement.setString(4, proveedor.getTelefono());
            statement.setString(5, proveedor.getPaginaweb());

            statement.executeUpdate();
        }
    }

    public void insertarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, telefono, direccion, ciudad, codigoPostal,email) VALUES (?, ?, ?, ?, ?,?)";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getTelefono());
            statement.setString(3, cliente.getDireccion());
            statement.setInt(4, cliente.getCiudad().getIdciudad());
            statement.setString(5, cliente.getCodigoPostal());
            statement.setString(6, cliente.getEmail());

            statement.executeUpdate();
        }
    }

    public void insertarEmpleado(Empleado empleado) throws SQLException {

        String sql;
        if (empleado.getUsuario() instanceof Usuario) {
            sql = "INSERT INTO empleados (nombre, apellido1, apellido2, DNI, telefono,email,salario,idusuario) VALUES (?, ?, ?, ?, ?,?,?,?)";

        } else {
            sql = "INSERT INTO empleados (nombre, apellido1, apellido2, DNI, telefono,email,salario) VALUES (?, ?, ?, ?, ?,?,?)";

        }

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, empleado.getNombre());
            statement.setString(2, empleado.getApellido1());
            statement.setString(3, empleado.getApellido2());
            statement.setString(4, empleado.getDni());
            statement.setString(5, empleado.getTelefono());
            statement.setString(6, empleado.getEmail());
            statement.setFloat(7, empleado.getSalario());
            if (empleado.getUsuario() instanceof Usuario) {
                statement.setInt(8, insertarUsuario(empleado.getUsuario()));
            }
            statement.executeUpdate();
        }
    }

    private int insertarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (usuario,pass,privilegios) VALUES (?,?,?)";

        try ( PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, usuario.getUsuario());
            statement.setString(2, BCrypt.hashpw(usuario.getPass(), BCrypt.gensalt()));
            statement.setString(3, usuario.getRole());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();

            int idusuario = -1;
            if (generatedKeys.next()) {
                idusuario = generatedKeys.getInt(1);
                return idusuario;
            } else {
                throw new SQLException("Failed to retrieve the generated ID for the pedido.");
            }
        }
    }

    public void insertarLineaproducto(LineaProducto linea) throws SQLException {

        String sql = "INSERT INTO lineasproducto(lineaproducto,descripcion) VALUES (?,?)";

        try ( PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, linea.getLinea());
            statement.setString(2, linea.getDescripcion());

            statement.executeUpdate();
        }
    }

    public Empleado iniciarSesion(String usuario, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(db, db_user, db_password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM usuarios INNER JOIN empleados USING(idusuario) WHERE usuario = ?");
            statement.setString(1, usuario);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedUser = resultSet.getString("usuario");
                String storedPass = resultSet.getString("pass");
                int idUsuario = resultSet.getInt("idusuario");
                String role = resultSet.getString("privilegios");

                if (usuario.equals(storedUser) && BCrypt.checkpw(password, storedPass)) {
                    System.out.println("Éxito");
                    Empleado empleado = new Empleado();
                    empleado.setIdempleado(resultSet.getInt("idempleados"));
                    empleado.setNombre(resultSet.getString("nombre"));
                    empleado.setApellido1(resultSet.getString("apellido1"));
                    empleado.setApellido2(resultSet.getString("apellido2"));
                    empleado.setDni(resultSet.getString("DNI"));
                    empleado.setTelefono(resultSet.getString("telefono"));
                    empleado.setEmail(resultSet.getString("email"));
                    empleado.setSalario(resultSet.getFloat("salario"));
                    Usuario user = new Usuario(storedUser, idUsuario, role);
                    empleado.setUsuario(user);
                    resultSet.close();
                    statement.close();
                    connection.close();
                    return empleado;
                }
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }

        return null;
    }

}
