-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema crud_app
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `crud_app` ;

-- -----------------------------------------------------
-- Schema crud_app
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `crud_app` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `crud_app` ;

-- -----------------------------------------------------
-- Table `crud_app`.`ccaa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`ccaa` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`ccaa` (
  `idCCAA` INT NOT NULL AUTO_INCREMENT,
  `nombreComunidad` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idCCAA`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`ciudades`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`ciudades` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`ciudades` (
  `idciudad` INT NOT NULL AUTO_INCREMENT,
  `nombreCiudad` VARCHAR(100) NULL DEFAULT NULL,
  `idCCAA` INT NOT NULL,
  PRIMARY KEY (`idciudad`),
  INDEX `idCCAA_idx` (`idCCAA` ASC) VISIBLE,
  CONSTRAINT `idCCAA`
    FOREIGN KEY (`idCCAA`)
    REFERENCES `crud_app`.`ccaa` (`idCCAA`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`clientes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`clientes` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`clientes` (
  `idcliente` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `telefono` VARCHAR(9) NULL DEFAULT NULL,
  `direccion` VARCHAR(45) NULL DEFAULT NULL,
  `ciudad` INT NULL DEFAULT NULL,
  `codigoPostal` VARCHAR(10) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idcliente`),
  INDEX `ciudad_idx` (`ciudad` ASC) VISIBLE,
  CONSTRAINT `ciudadCliente`
    FOREIGN KEY (`ciudad`)
    REFERENCES `crud_app`.`ciudades` (`idciudad`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`usuarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`usuarios` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`usuarios` (
  `usuario` VARCHAR(45) NOT NULL,
  `pass` VARCHAR(500) NOT NULL,
  `idusuario` INT NOT NULL AUTO_INCREMENT,
  `privilegios` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`idusuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`pedidos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`pedidos` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`pedidos` (
  `idpedido` INT NOT NULL AUTO_INCREMENT,
  `idcliente` INT NOT NULL,
  `idempleado` INT NULL DEFAULT NULL,
  `fechaPedido` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`idpedido`, `idcliente`),
  INDEX `cliente_idx` (`idcliente` ASC) VISIBLE,
  INDEX `empleado_idx` (`idempleado` ASC) VISIBLE,
  CONSTRAINT `cliente`
    FOREIGN KEY (`idcliente`)
    REFERENCES `crud_app`.`clientes` (`idcliente`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `empleado`
    FOREIGN KEY (`idempleado`)
    REFERENCES `crud_app`.`usuarios` (`idusuario`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`lineasproducto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`lineasproducto` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`lineasproducto` (
  `lineaproducto` VARCHAR(45) NOT NULL,
  `descripcion` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`lineaproducto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`proveedores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`proveedores` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`proveedores` (
  `idproveedor` INT NOT NULL AUTO_INCREMENT,
  `nombreEmpresa` VARCHAR(45) NULL DEFAULT NULL,
  `nombreContacto` VARCHAR(45) NULL DEFAULT NULL,
  `ciudad` INT NULL DEFAULT NULL,
  `telefono` VARCHAR(9) NULL DEFAULT NULL,
  `paginaweb` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`idproveedor`),
  INDEX `ciudadProveedor_idx` (`ciudad` ASC) VISIBLE,
  CONSTRAINT `ciudadProveedor`
    FOREIGN KEY (`ciudad`)
    REFERENCES `crud_app`.`ciudades` (`idciudad`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`productos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`productos` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`productos` (
  `idproducto` INT NOT NULL AUTO_INCREMENT,
  `nombreproducto` VARCHAR(45) NOT NULL,
  `lineaproducto` VARCHAR(45) NULL DEFAULT NULL,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  `cantidadEnStock` INT NULL DEFAULT NULL,
  `pvp` FLOAT NOT NULL,
  `proveedor` INT NULL DEFAULT NULL,
  `eliminado` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`idproducto`),
  INDEX `lineaProducto_idx` (`lineaproducto` ASC) VISIBLE,
  INDEX `proveedor_idx` (`proveedor` ASC) VISIBLE,
  CONSTRAINT `lineaProducto`
    FOREIGN KEY (`lineaproducto`)
    REFERENCES `crud_app`.`lineasproducto` (`lineaproducto`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `proveedor`
    FOREIGN KEY (`proveedor`)
    REFERENCES `crud_app`.`proveedores` (`idproveedor`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`detallespedido`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`detallespedido` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`detallespedido` (
  `idpedido` INT NOT NULL,
  `idproducto` INT NOT NULL,
  `cantidad` INT NULL DEFAULT NULL,
  `precio_venta` FLOAT NULL DEFAULT NULL,
  PRIMARY KEY (`idpedido`, `idproducto`),
  INDEX `idproducto_idx` (`idproducto` ASC) VISIBLE,
  CONSTRAINT `idpedido`
    FOREIGN KEY (`idpedido`)
    REFERENCES `crud_app`.`pedidos` (`idpedido`)
    ON UPDATE CASCADE,
  CONSTRAINT `idproducto`
    FOREIGN KEY (`idproducto`)
    REFERENCES `crud_app`.`productos` (`idproducto`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`empleados`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`empleados` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`empleados` (
  `idempleados` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `apellido1` VARCHAR(45) NULL DEFAULT NULL,
  `apellido2` VARCHAR(45) NULL DEFAULT NULL,
  `DNI` VARCHAR(9) NULL DEFAULT NULL,
  `telefono` VARCHAR(9) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `idusuario` INT NULL DEFAULT NULL,
  `salario` FLOAT NULL DEFAULT NULL,
  PRIMARY KEY (`idempleados`),
  INDEX `usuario_idx` (`idusuario` ASC) VISIBLE,
  CONSTRAINT `usuario`
    FOREIGN KEY (`idusuario`)
    REFERENCES `crud_app`.`usuarios` (`idusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `crud_app` ;

-- -----------------------------------------------------
-- Placeholder table for view `crud_app`.`ciudades_ccaa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crud_app`.`ciudades_ccaa` (`idCCAA` INT, `idciudad` INT, `nombreCiudad` INT, `nombreComunidad` INT);

-- -----------------------------------------------------
-- Placeholder table for view `crud_app`.`numeropedidos_empleado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crud_app`.`numeropedidos_empleado` (`idempleado` INT, `numeropedidos` INT);

-- -----------------------------------------------------
-- Placeholder table for view `crud_app`.`vista_pedidos_productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `crud_app`.`vista_pedidos_productos` (`idproducto` INT, `idpedido` INT, `idcliente` INT, `idempleado` INT, `fechaPedido` INT, `cantidad` INT, `precio_venta` INT, `nombreproducto` INT, `lineaproducto` INT, `descripcion` INT, `cantidadEnStock` INT, `pvp` INT, `proveedor` INT, `eliminado` INT);

-- -----------------------------------------------------
-- View `crud_app`.`ciudades_ccaa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`ciudades_ccaa`;
DROP VIEW IF EXISTS `crud_app`.`ciudades_ccaa` ;
USE `crud_app`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crud_app`.`ciudades_ccaa` AS select `crud_app`.`ciudades`.`idCCAA` AS `idCCAA`,`crud_app`.`ciudades`.`idciudad` AS `idciudad`,`crud_app`.`ciudades`.`nombreCiudad` AS `nombreCiudad`,`crud_app`.`ccaa`.`nombreComunidad` AS `nombreComunidad` from (`crud_app`.`ciudades` join `crud_app`.`ccaa` on((`crud_app`.`ciudades`.`idCCAA` = `crud_app`.`ccaa`.`idCCAA`)));

-- -----------------------------------------------------
-- View `crud_app`.`numeropedidos_empleado`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`numeropedidos_empleado`;
DROP VIEW IF EXISTS `crud_app`.`numeropedidos_empleado` ;
USE `crud_app`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crud_app`.`numeropedidos_empleado` AS select `crud_app`.`pedidos`.`idempleado` AS `idempleado`,count(0) AS `numeropedidos` from (`crud_app`.`empleados` join `crud_app`.`pedidos` on((`crud_app`.`pedidos`.`idempleado` = `crud_app`.`empleados`.`idempleados`))) group by `crud_app`.`pedidos`.`idempleado`;

-- -----------------------------------------------------
-- View `crud_app`.`vista_pedidos_productos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`vista_pedidos_productos`;
DROP VIEW IF EXISTS `crud_app`.`vista_pedidos_productos` ;
USE `crud_app`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `crud_app`.`vista_pedidos_productos` AS select `crud_app`.`detallespedido`.`idproducto` AS `idproducto`,`crud_app`.`pedidos`.`idpedido` AS `idpedido`,`crud_app`.`pedidos`.`idcliente` AS `idcliente`,`crud_app`.`pedidos`.`idempleado` AS `idempleado`,`crud_app`.`pedidos`.`fechaPedido` AS `fechaPedido`,`crud_app`.`detallespedido`.`cantidad` AS `cantidad`,`crud_app`.`detallespedido`.`precio_venta` AS `precio_venta`,`crud_app`.`productos`.`nombreproducto` AS `nombreproducto`,`crud_app`.`productos`.`lineaproducto` AS `lineaproducto`,`crud_app`.`productos`.`descripcion` AS `descripcion`,`crud_app`.`productos`.`cantidadEnStock` AS `cantidadEnStock`,`crud_app`.`productos`.`pvp` AS `pvp`,`crud_app`.`productos`.`proveedor` AS `proveedor`,`crud_app`.`productos`.`eliminado` AS `eliminado` from ((`crud_app`.`pedidos` join `crud_app`.`detallespedido` on((`crud_app`.`pedidos`.`idpedido` = `crud_app`.`detallespedido`.`idpedido`))) join `crud_app`.`productos` on((`crud_app`.`detallespedido`.`idproducto` = `crud_app`.`productos`.`idproducto`)));
USE `crud_app`;

DELIMITER $$

USE `crud_app`$$
DROP TRIGGER IF EXISTS `crud_app`.`trg_proveedor_deleted` $$
USE `crud_app`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `crud_app`.`trg_proveedor_deleted`
BEFORE DELETE ON `crud_app`.`proveedores`
FOR EACH ROW
BEGIN
    UPDATE productos
    SET eliminado = 1
    WHERE proveedor = OLD.idproveedor;
END$$


USE `crud_app`$$
DROP TRIGGER IF EXISTS `crud_app`.`restar_stock` $$
USE `crud_app`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `crud_app`.`restar_stock`
AFTER INSERT ON `crud_app`.`detallespedido`
FOR EACH ROW
BEGIN
    UPDATE productos
    SET cantidadEnStock = cantidadEnStock - NEW.cantidad
    WHERE idproducto = NEW.idproducto;
END$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- INSERTS

INSERT INTO `crud_app`.`ccaa`
(`nombreComunidad`)
VALUES
("Andalucía"),
("Aragón"),
("Principado de Asturias"),
("Illes Balears"),
("Canarias"),
("Cantabria"),
("Castilla y León"),
("Castilla-La Mancha"),
("Cataluña"),
("Comunitat Valenciana"),
("Extremadura"),
("Galicia"),
("Comunidad de Madrid"),
("Región de Murcia"),
("Comunidad Foral de Navarra"),
("País Vasco"),
("La Rioja"),
("Ceuta"),
("Melilla");


INSERT INTO `crud_app`.`ciudades`
(`nombreCiudad`,
`idCCAA`)
VALUES
("Almería",1),
("Cádiz",1),
("Córdoba",1),
("Granada",1),
("Huelva",1),
("Jaén",1),
("Málaga",1),
("Sevilla",1),
("Huesca",2),
("Teruel",2),
("Zaragoza",2),
("Asturias",3),
("Illes Balears",4),
("Las Palmas",5),
("Santa Cruz de Tenerife",5),
("Cantabria",6),
("Ávila",7),
("Burgos",7),
("León",7),
("Palencia",7),
("Salamanca",7),
("Segovia",7),
("Soria",7),
("Valladolid",7),
("Zamora",7),
("Albacete",8),
("Ciudad Real",8),
("Cuenca",8),
("Guadalajara",8),
("Toledo",8),
("Barcelona",9),
("Girona",9),
("Lleida",9),
("Tarragona",9),
("Alicante",10),
("Castellón",10),
("Valencia",10),
("Badajoz",11),
("Cáceres",11),
("A Coruña",12),
("Lugo",12),
("Ourense",12),
("Pontevedra",12),
("Madrid",13),
("Murcia",14),
("Navarra",15),
("Álava",16),
("Bizkaia",16),
("Gipuzkoa",16),
("La Rioja",17),
("Ceuta",18),
("Melilla",19);




INSERT INTO `crud_app`.`usuarios`(`usuario`,`pass`,`privilegios`)VALUES("admin","$2a$10$H8u5uMrgixtOYIoi6XPfh.MrR9y65X8Q.6cnIuRUOw6ULxiMmkrX.","admin");
INSERT INTO `crud_app`.`usuarios`(`usuario`,`pass`,`privilegios`)VALUES("test","$2a$10$4Laa6FsWYMo6wZVE9OZLou91xW3xKtwaoQVHl3Ow9M2J8.NIOv.bG","user");

INSERT INTO `crud_app`.`empleados` (`nombre`, `apellido1`, `apellido2`, `DNI`, `telefono`, `email`, `idusuario`, `salario`)
VALUES ('Juan', 'Gómez', 'López', '12345678A', '123456789', 'juangomez@example.com', 1, 2500.00),
       ('María', 'Martínez', 'García', '87654321B', '987654321', 'mariamartinez@example.com', 2, 2800.00);
INSERT INTO `crud_app`.`empleados` (`nombre`, `apellido1`, `apellido2`, `DNI`, `telefono`, `email`, `salario`)
VALUES ('Pedro', 'Sánchez', 'Fernández', '98765432C', '654987321', 'pedrosanchez@example.com', 2300.00),
       ('Laura', 'López', 'Hernández', '76543210D', '321654987', 'lauralopez@example.com', 2700.00),
       ('Carlos', 'Rodríguez', 'Pérez', '01234567E', '789654321', 'carlosrodriguez@example.com', 3000.00);


-- Insertar datos en la tabla 'clientes'
INSERT INTO `crud_app`.`clientes` (`nombre`, `telefono`, `direccion`, `ciudad`, `codigoPostal`, `email`)
VALUES ('Juan Pérez', '123456789', 'Calle Mayor 123', 5, '12345', 'juan@example.com'),
       ('María López', '987654321', 'Avenida Libertad 456', 43, '54321', 'maria@example.com'),
       ('Pedro Rodríguez', '555555555', 'Plaza España 789', 25, '67890', 'pedro@example.com'),
       ('Laura García', '999999999', 'Callejón Oscuro 10', 25, '45678', 'laura@example.com'),
       ('Carlos Martínez', '111111111', 'Paseo del Parque 22', 36, '98765', 'carlos@example.com'),
       ('Ana Sánchez', '777777777', 'Ronda Sur 33', 38, '23456', 'ana@example.com'),
       ('Luisa Torres', '888888888', 'Avenida del Sol 44', 2, '34567', 'luisa@example.com'),
       ('Javier Vargas', '222222222', 'Callejuela 55', 23, '87654', 'javier@example.com'),
       ('Sofía Ramírez', '444444444', 'Calle Nueva 66', 42, '76543', 'sofia@example.com'),
       ('Eduardo Herrera', '666666666', 'Avenida Central 77', 8, '54321', 'eduardo@example.com');



-- Insertar datos en la tabla 'proveedores'
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Tabacos El Faro', 'Pedro Sánchez', 6, '987654321', 'www.tabacoselfaro.es');
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Tabacos La Estrella', 'Juan Martínez', 6, '654321987', 'www.tabacoslaestrella.es');
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Tabacos El Cigarro', 'Antonio López', 52, '123456789', 'www.tabacoselcigarro.es');
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Tabacos El Fumador', 'Manuel García', 3, '555555555', 'www.tabacoselfumador.es');
INSERT INTO `crud_app`.`proveedores` (`nombreEmpresa`, `nombreContacto`, `ciudad`, `telefono`, `paginaweb`) VALUES ('Tabacos El Humo', 'Luis Rodríguez', 35, '999999999', 'www.tabacoselhumo.es');

-- Insertar datos en la tabla 'lineasproducto'
INSERT INTO `crud_app`.`lineasproducto` (`lineaproducto`, `descripcion`)
VALUES ('Cigarros', 'Línea de cigarros de diferentes marcas y tamaños'),
       ('Tabaco de pipa', 'Variedades de tabaco de pipa para fumadores exigentes'),
       ('Puros', 'Selección de puros de alta calidad y sabores exquisitos'),
       ('Cigarrillos electrónicos', 'Productos para vapeo y cigarrillos electrónicos'),
       ('Accesorios para fumar', 'Artículos como encendedores, ceniceros y estuches');

-- Insertar datos en la tabla 'productos'

INSERT INTO `crud_app`.`productos` (`nombreproducto`, `lineaproducto`, `descripcion`, `cantidadEnStock`, `pvp`, `proveedor`, `eliminado`)
VALUES 
('Cigarro Clásico', 'Cigarros', 'Cigarro de tamaño clásico', 50, 5.99, 3, 0),
('Cigarro Premium', 'Cigarros', 'Cigarro de alta calidad con sabor exclusivo', 30, 8.99, 2, 0),
('Tabaco Aromático', 'Tabaco de pipa', 'Tabaco de pipa con notas aromáticas', 20, 12.99, 3, 0),
('Tabaco Natural', 'Tabaco de pipa', 'Tabaco de pipa natural sin aditivos', 15, 9.99, 4, 0),
('Puro Clásico', 'Puros', 'Puro de origen cubano con sabor intenso', 40, 15.99, 4, 0),
('Puro Premium', 'Puros', 'Puro premium con hojas selectas', 25, 19.99, 3, 0),
('Cigarrillo Electrónico Básico', 'Cigarrillos electrónicos', 'Cigarrillo electrónico básico recargable', 60, 24.99, 5, 0),
('Cigarrillo Electrónico Avanzado', 'Cigarrillos electrónicos', 'Cigarrillo electrónico avanzado', 35, 39.99, 5, 0),
('Encendedor de Mesa', 'Accesorios para fumar', 'Encendedor de mesa con llama ajustable', 10, 14.99, 4, 0),
('Estuche para Puros', 'Accesorios para fumar', 'Estuche de cuero para guardar puros', 5, 29.99, 4, 0);

-- Insertar datos en la tabla 'pedidos'
INSERT INTO `crud_app`.`pedidos` (`idcliente`, `idempleado`, `fechaPedido`)
VALUES (1, 1, '2023-06-01'),
       (2, 2, '2023-06-02'),
       (3, 1, '2023-06-02'),
       (4, 1, '2023-06-03'),
       (5, 1, '2023-06-03');

-- Insertar datos en la tabla 'detallespedido'
INSERT INTO `crud_app`.`detallespedido` (`idpedido`, `idproducto`, `cantidad`, `precio_venta`)
VALUES (1, 1, 2, 11.99),
       (1, 2, 1, 9.99),
       (2, 4, 3, 8.99),
       (2, 3, 2, 15.99),
       (2, 5, 1, 12.99),
       (3, 3, 3, 24.99),
       (3, 5, 2, 19.99),
       (4, 6, 1, 14.99),
       (5, 7, 3, 39.99),
       (5, 8, 2, 29.99);
