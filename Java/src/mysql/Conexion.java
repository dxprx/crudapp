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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class Conexion {

    private final String db = "jdbc:mysql://localhost:3306/crud_app";
    private final String db_user = "ogp";
    private final String db_password = "ogp";
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
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProducto);

            statement.executeUpdate();
        }
    }

    public void eliminarProveedor(int idProveedor) throws SQLException {
        String sql = "DELETE FROM proveedores WHERE idproveedor = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProveedor);

            statement.executeUpdate();
        }
    }

    public void eliminarLineaProducto(String linea) throws SQLException {
        String sql = "DELETE FROM lineasproducto WHERE lineaproducto = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, linea);

            statement.executeUpdate();
        }
    }

    public void eliminarCliente(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE idcliente = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCliente);

            statement.executeUpdate();
        }
    }

    public void eliminarEmpleado(int idEmpleado) throws SQLException {
        String sql = "DELETE FROM empleados WHERE idempleados = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idEmpleado);

            statement.executeUpdate();
        }
    }

    public void eliminarPedido(int idPedido) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE idpedido = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPedido);

            statement.executeUpdate();
        }
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombreproducto = ?, lineaproducto = ?, descripcion = ?, "
                + "cantidadEnStock = ?, pvp = ?, proveedor = ?, eliminado = ? WHERE idproducto = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, linea.getLinea());
            statement.setString(2, linea.getDescripcion());
            statement.setString(3, id);

            statement.executeUpdate();
        }
    }

    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, direccion = ?, ciudad = ?, codigoPostal = ?, email = ? WHERE idcliente = ?";;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
        String sql = "SELECT * FROM empleados INNER JOIN usuarios using(idusuario)";

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

    public List<DetallesPedido> seleccionarDetallesPedidos() throws SQLException {
        String sql = "SELECT  * FROM detallespedido INNER JOIN productos using(idproducto) INNER JOIN pedidos using(idpedido) INNER JOIN clientes using(idcliente) INNER JOIN ciudades_ccaa AS ciudades_ccaa_clientes ON clientes.ciudad = ciudades_ccaa_clientes.idciudad INNER JOIN ciudades_ccaa AS ciudades_ccaa_pedidos ON pedidos.ciudadPedido = ciudades_ccaa_pedidos.idciudad INNER JOIN usuarios ON idempleado=usuarios.idusuario INNER JOIN empleados using(idusuario) INNER JOIN lineasproducto using(lineaproducto) INNER JOIN proveedores ON proveedor=idproveedor INNER JOIN ciudades_ccaa AS ciudades_ccaa_proveedores ON proveedores.ciudad=ciudades_ccaa_proveedores.idciudad";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<DetallesPedido> pedidos = new ArrayList<>();
        try {

            while (resultSet.next()) {
                DetallesPedido pedido = new DetallesPedido();
                pedido.setIdpedido(new Pedido(resultSet.getInt("idpedido"), new Cliente(resultSet.getInt("idcliente"), resultSet.getString("clientes.nombre"), resultSet.getString("clientes.telefono"), resultSet.getString("clientes.direccion"), new Ciudad(resultSet.getInt("ciudades_ccaa_clientes.idciudad"), resultSet.getInt("ciudades_ccaa_clientes.idCCAA"), resultSet.getString("ciudades_ccaa_clientes.nombreCiudad"), resultSet.getString("ciudades_ccaa_clientes.nombreComunidad")), resultSet.getString("clientes.codigoPostal"), resultSet.getString("clientes.email")), new Empleado(resultSet.getInt("pedidos.idempleado"), resultSet.getString("empleados.nombre"), resultSet.getString("empleados.apellido1"), resultSet.getString("empleados.apellido2"), resultSet.getString("empleados.dni"), resultSet.getString("empleados.telefono"), resultSet.getString("empleados.email"), new Usuario(resultSet.getString("usuarios.usuario"), resultSet.getInt("usuarios.idusuario"), resultSet.getString("usuarios.privilegios"))), resultSet.getDate("fechaPedido"), resultSet.getString("codigopostalPedido"), new Ciudad(resultSet.getInt("ciudades_ccaa_pedidos.idciudad"), resultSet.getInt("ciudades_ccaa_pedidos.idCCAA"), resultSet.getString("ciudades_ccaa_pedidos.nombreCiudad"), resultSet.getString("ciudades_ccaa_pedidos.nombreComunidad")), resultSet.getString("codigopostalPedido")));
                pedido.setIdproducto(new Producto(resultSet.getInt("idproducto"), resultSet.getString("productos.nombreproducto"), new LineaProducto(resultSet.getString("lineaproducto"), resultSet.getString("lineasproducto.descripcion")), resultSet.getString("productos.descripcion"), resultSet.getInt("productos.cantidadEnStock"), resultSet.getFloat("productos.pvp"), new Proveedor(resultSet.getInt("idproveedor"), resultSet.getString("proveedores.nombreEmpresa"), resultSet.getString("proveedores.nombreContacto"), new Ciudad(resultSet.getInt("ciudades_ccaa_proveedores.idciudad"), resultSet.getInt("ciudades_ccaa_proveedores.idCCAA"), resultSet.getString("ciudades_ccaa_proveedores.nombreCiudad"), resultSet.getString("ciudades_ccaa_proveedores.nombreComunidad")), resultSet.getString("proveedores.telefono"), resultSet.getString("proveedores.paginaweb")), resultSet.getBoolean("productos.eliminado")));
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
        String sql = "SELECT * FROM pedidos INNER JOIN clientes using(idcliente)  INNER JOIN ciudades_ccaa AS ciudades_ccaa_clientes ON clientes.ciudad = ciudades_ccaa_clientes.idciudad INNER JOIN ciudades_ccaa AS ciudades_ccaa_pedidos ON pedidos.ciudadPedido = ciudades_ccaa_pedidos.idciudad INNER JOIN usuarios ON idempleado=usuarios.idusuario INNER JOIN empleados using(idusuario)";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Pedido> pedidos = new ArrayList<>();
        try {

            while (resultSet.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdpedido(resultSet.getInt("idpedido"));
                pedido.setCliente(new Cliente(resultSet.getInt("idcliente"), resultSet.getString("clientes.nombre"), resultSet.getString("clientes.telefono"), resultSet.getString("clientes.direccion"), new Ciudad(resultSet.getInt("ciudades_ccaa_clientes.idciudad"), resultSet.getInt("ciudades_ccaa_clientes.idCCAA"), resultSet.getString("ciudades_ccaa_clientes.nombreCiudad"), resultSet.getString("ciudades_ccaa_clientes.nombreComunidad")), resultSet.getString("clientes.codigoPostal"), resultSet.getString("clientes.email")));
                pedido.setEmpleado(new Empleado(resultSet.getInt("pedidos.idempleado"), resultSet.getString("empleados.nombre"), resultSet.getString("empleados.apellido1"), resultSet.getString("empleados.apellido2"), resultSet.getString("empleados.dni"), resultSet.getString("empleados.telefono"), resultSet.getString("empleados.email"), new Usuario(resultSet.getString("usuarios.usuario"), resultSet.getInt("usuarios.idusuario"), resultSet.getString("usuarios.privilegios"))));
                pedido.setFechaPedido(resultSet.getDate("fechaPedido"));
                pedido.setDireccionPedido(resultSet.getString("direccionPedido"));
                pedido.setCiudadPedido(new Ciudad(resultSet.getInt("ciudades_ccaa_pedidos.idciudad"), resultSet.getInt("ciudades_ccaa_pedidos.idCCAA"), resultSet.getString("ciudades_ccaa_pedidos.nombreCiudad"), resultSet.getString("ciudades_ccaa_pedidos.nombreComunidad")));
                pedido.setCodigopostalPedido(resultSet.getString("codigopostalPedido"));
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
        String sql = "INSERT INTO `crud_app`.`pedidos`(`idcliente`,`idempleado`,`fechaPedido`,`direccionPedido`,`ciudadPedido`,`codigopostalPedido`) VALUES (?,?,curdate(),?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pedido.getCliente().getIdClientes());
            statement.setInt(2, pedido.getEmpleado().getIdempleado());
            statement.setString(3, pedido.getDireccionPedido());
            statement.setInt(4, pedido.getCiudadPedido().getIdciudad());
            statement.setString(5, pedido.getCodigopostalPedido());

            statement.executeUpdate();
        }
        String sqlid = "SELECT idpedido FROM pedidos ORDER BY idpedido DESC LIMIT 1";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlid);

        int idpedido;
        if (resultSet.next()) {
            // process resultset
            idpedido = resultSet.getInt("idpedido");
        } else {
            // do something when no data arrived
            idpedido = -1;
        }

        String sqldp = "INSERT INTO `crud_app`.`detallespedido` (`idpedido`,`idproducto`,`cantidad`,`precio_venta`) VALUES (?,?,?,?);";
        for (DetallesPedido dp : detallesPedido) {
            try (PreparedStatement statement2 = connection.prepareStatement(sqldp)) {
                statement2.setInt(1, idpedido);
                statement2.setInt(2, dp.getIdproducto().getIdProducto());
                statement2.setInt(3, dp.getCantidad());
                statement2.setFloat(4, dp.getIdproducto().getPvp());

                statement2.executeUpdate();
            }
        }

    }

    public void insertarProveedor(Proveedor proveedor) throws SQLException {
        String sql = "INSERT INTO proveedores (nombreEmpresa, nombreContacto, ciudad, telefono, paginaweb) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
            sql = "INSERT INTO empleados (nombre, apellido1, apellido2, DNI, telefono,email,idusuario) VALUES (?, ?, ?, ?, ?,?,?)";

        } else {
            sql = "INSERT INTO empleados (nombre, apellido1, apellido2, DNI, telefono,email) VALUES (?, ?, ?, ?, ?,?)";

        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, empleado.getNombre());
            statement.setString(2, empleado.getApellido1());
            statement.setString(3, empleado.getApellido2());
            statement.setString(4, empleado.getDni());
            statement.setString(5, empleado.getTelefono());
            statement.setString(6, empleado.getEmail());
            if (empleado.getUsuario() instanceof Usuario) {
                statement.setInt(7, empleado.getUsuario().getIdUsuario());
            }
            statement.executeUpdate();
        }
    }
    
    public void insertarLineaproducto(LineaProducto linea) throws SQLException {

        String sql = "INSERT INTO lineasproducto(lineaproducto,descripcion) VALUES (?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, linea.getLinea());
            statement.setString(2, linea.getDescripcion());
            
            statement.executeUpdate();
        }
    }
}
