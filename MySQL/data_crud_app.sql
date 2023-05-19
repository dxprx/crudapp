-- COMUNIDADES AUTONOMAS 
INSERT INTO `crud_app`.`ccaa` (`nombreComunidad`)VALUES("Andalucía"),("Aragón"),("Principado de Asturias"),("Illes Balears"),("Canarias"),("Cantabria"),("Castilla y León"),("Castilla-La Mancha"),("Cataluña"),("Comunitat Valenciana"),("Extremadura"),("Galicia"),("Comunidad de Madrid"),("Región de Murcia"),("Comunidad Foral de Navarra"),("País Vasco"),("La Rioja"),("Ceuta"),("Melilla");
-- CIUDADES
INSERT INTO `crud_app`.`ciudades`(`nombreCiudad`,`idCCAA`)VALUES("Almería",1),("Cádiz",1),("Córdoba",1),("Granada",1),("Huelva",1),("Jaén",1),("Málaga",1),("Sevilla",1),("Huesca",2),("Teruel",2),("Zaragoza",2),("Asturias",3),("Illes Balears",4),("Las Palmas",5),("Santa Cruz de Tenerife",5),("Cantabria",6),("Ávila",7),("Burgos",7),("León",7),("Palencia",7),("Salamanca",7),("Segovia",7),("Soria",7),("Valladolid",7),("Zamora",7),("Albacete",8),("Ciudad Real",8),("Cuenca",8),("Guadalajara",8),("Toledo",8),("Barcelona",9),("Girona",9),("Lleida",9),("Tarragona",9),("Alicante",10),("Castellón",10),("Valencia",10),("Badajoz",11),("Cáceres",11),("A Coruña",12),("Lugo",12),("Ourense",12),("Pontevedra",12),("Madrid",13),("Murcia",14),("Navarra",15),("Álava",16),("Bizkaia",16),("Gipuzkoa",16),("La Rioja",17),("Ceuta",18),("Melilla",19);
-- USUARIOS 
INSERT INTO `crud_app`.`usuarios`(`usuario`,`pass`,`privilegios`)VALUES("admin","$2a$10$H8u5uMrgixtOYIoi6XPfh.MrR9y65X8Q.6cnIuRUOw6ULxiMmkrX.","admin");
INSERT INTO `crud_app`.`usuarios`(`usuario`,`pass`,`privilegios`)VALUES("test","$2a$10$4Laa6FsWYMo6wZVE9OZLou91xW3xKtwaoQVHl3Ow9M2J8.NIOv.bG","user");
-- CLIENTES 
INSERT INTO `crud_app`.`clientes` (`nombre`, `telefono`, `direccion`, `ciudad`, `codigoPostal`, `email`) VALUES ('Cliente 1', '123456789', 'Dirección 1', 1, '12345', 'cliente1@example.com');
INSERT INTO `crud_app`.`clientes` (`nombre`, `telefono`, `direccion`, `ciudad`, `codigoPostal`, `email`) VALUES ('Cliente 2', '987654321', 'Dirección 2', 1, '54321', 'cliente2@example.com');
INSERT INTO `crud_app`.`clientes` (`nombre`, `telefono`, `direccion`, `ciudad`, `codigoPostal`, `email`) VALUES ('Cliente 3', '111222333', 'Dirección 3', 2, '67890', 'cliente3@example.com');
INSERT INTO `crud_app`.`clientes` (`nombre`, `telefono`, `direccion`, `ciudad`, `codigoPostal`, `email`) VALUES ('Cliente 4', '444555666', 'Dirección 4', 2, '09876', 'cliente4@example.com');
INSERT INTO `crud_app`.`clientes` (`nombre`, `telefono`, `direccion`, `ciudad`, `codigoPostal`, `email`) VALUES ('Cliente 5', '777888999', 'Dirección 5', 3, '54321', 'cliente5@example.com');
-- LINEAS PRODUCTO 
INSERT INTO `crud_app`.`lineasproducto` (`lineaproducto`, `descripcion`) VALUES ('Línea 1', 'Descripción 1');
INSERT INTO `crud_app`.`lineasproducto` (`lineaproducto`, `descripcion`) VALUES ('Línea 2', 'Descripción 2');
INSERT INTO `crud_app`.`lineasproducto` (`lineaproducto`, `descripcion`) VALUES ('Línea 3', 'Descripción 3');
INSERT INTO `crud_app`.`lineasproducto` (`lineaproducto`, `descripcion`) VALUES ('Línea 4', 'Descripción 4');
INSERT INTO `crud_app`.`lineasproducto` (`lineaproducto`, `descripcion`) VALUES ('Línea 5', 'Descripción 5');
-- PROVEEDORES 
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Proveedor 1', 'Contacto 1', 1, '123456789', 'www.proveedor1.com');
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Proveedor 2', 'Contacto 2', 1, '987654321', 'www.proveedor2.com');
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Proveedor 3', 'Contacto 3', 2, '111222333', 'www.proveedor3.com');
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Proveedor 4', 'Contacto 4', 2, '444555666', 'www.proveedor4.com');
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Proveedor 5', 'Contacto 5', 3, '777888999', 'www.proveedor5.com');
-- PRODUCTOS 
INSERT INTO `crud_app`.`productos` (`nombreProducto`, `lineaproducto`, `proveedor`, `pvp`) VALUES ('Producto 1', 'Línea 1', 1, 10.99);
INSERT INTO `crud_app`.`productos` (`nombreProducto`, `lineaproducto`, `proveedor`, `pvp`) VALUES ('Producto 2', 'Línea 2', 2, 19.99);
INSERT INTO `crud_app`.`productos` (`nombreProducto`, `lineaproducto`, `proveedor`, `pvp`) VALUES ('Producto 3', 'Línea 3', 3, 5.99);
INSERT INTO `crud_app`.`productos` (`nombreProducto`, `lineaproducto`, `proveedor`, `pvp`) VALUES ('Producto 4', 'Línea 4', 4, 8.99);
INSERT INTO `crud_app`.`productos` (`nombreProducto`, `lineaproducto`, `proveedor`, `pvp`) VALUES ('Producto 5', 'Línea 5', 5, 15.99);
-- EMPLEADOS 
INSERT INTO `crud_app`.`empleados` (`idempleados`, `nombre`, `apellido1`, `apellido2`, `DNI`, `telefono`, `email`, `idusuario`) VALUES (1, 'Juan', 'Pérez', 'García', '12345678A', '123456789', 'juan@example.com', 1);
INSERT INTO `crud_app`.`empleados` (`idempleados`, `nombre`, `apellido1`, `apellido2`, `DNI`, `telefono`, `email`, `idusuario`) VALUES (2, 'María', 'López', 'Fernández', '87654321B', '987654321', 'maria@example.com', 2);
-- PEDIDOS 
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`,  `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (1,1,  '2023-05-11', 'Dirección 1',1);
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`, `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (2,1,  '2023-05-12', 'Dirección 2',2);
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`,  `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (3,2,  '2023-05-13', 'Dirección 3',3);
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`,  `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (4,2,  '2023-05-14', 'Dirección 4',4);
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`, `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (5,2,  '2023-05-16', 'Dirección 5',5);
-- DETALLES PEDIDOS 
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (1, 1, 2);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (1, 2, 1);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (2, 3, 3);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (2, 4, 2);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (3, 5, 1);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (4, 1, 2);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (4, 2, 1);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (5, 3, 3);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (5, 4, 2);
INSERT INTO `crud_app`.`detallespedido` (`idPedido`, `idProducto`, `cantidad`) VALUES (5, 5, 1);
