INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`,  `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (1,1,  '2023-05-11', 'Dirección 1',1);
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`, `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (2,1,  '2023-05-12', 'Dirección 2',2);
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`,  `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (3,2,  '2023-05-13', 'Dirección 3',3);
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`,  `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (4,2,  '2023-05-14', 'Dirección 4',4);
INSERT INTO `crud_app`.`pedidos` (`idcliente`,`idempleado`, `fechaPedido`, `direccionPedido`,`ciudadPedido`) VALUES (5,2,  '2023-05-16', 'Dirección 5',5);

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
