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
    REFERENCES `crud_app`.`ccaa` (`idCCAA`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
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
  `idempleado` INT NULL,
  `fechaPedido` DATE NULL,
  `direccionPedido` VARCHAR(100) NULL DEFAULT NULL,
  `ciudadPedido` INT NULL DEFAULT NULL,
  `codigopostalPedido` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`idpedido`, `idcliente`),
  INDEX `cliente_idx` (`idcliente` ASC) VISIBLE,
  INDEX `ciudad_idx` (`ciudadPedido` ASC) VISIBLE,
  INDEX `empleado_idx` (`idempleado` ASC) VISIBLE,
  CONSTRAINT `ciudadPedido`
    FOREIGN KEY (`ciudadPedido`)
    REFERENCES `crud_app`.`ciudades` (`idciudad`),
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
  `descripcion` VARCHAR(45) NULL,
  `cantidadEnStock` INT NULL,
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
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `crud_app`.`empleados`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `crud_app`.`empleados` ;

CREATE TABLE IF NOT EXISTS `crud_app`.`empleados` (
  `idempleados` INT NOT NULL,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  `apellido1` VARCHAR(45) NULL DEFAULT NULL,
  `apellido2` VARCHAR(45) NULL DEFAULT NULL,
  `DNI` VARCHAR(9) NULL DEFAULT NULL,
  `telefono` VARCHAR(9) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `idusuario` INT NULL DEFAULT NULL,
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

create view vista_pedidos_productos as select * from pedidos inner join detallespedido using(idpedido) inner join productos using(idproducto);
