-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`seguro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`seguro` (
  `idseguro` INT NOT NULL AUTO_INCREMENT,
  `empresa_aseguradora` VARCHAR(45) NULL,
  `cobertura_maxima` DOUBLE NULL,
  `tarifa` DOUBLE NULL,
  PRIMARY KEY (`idseguro`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`sede`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`sede` (
  `idsede` INT NOT NULL AUTO_INCREMENT,
  `distrito` VARCHAR(45) NOT NULL,
  `direccion` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`idsede`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`auto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`auto` (
  `idauto` INT NOT NULL AUTO_INCREMENT,
  `modelo` VARCHAR(45) NOT NULL,
  `color` VARCHAR(45) NOT NULL,
  `kilometraje` INT NOT NULL,
  `costo_por_dia` DOUBLE NOT NULL,
  `sede_idsede` INT NOT NULL,
  PRIMARY KEY (`idauto`),
  INDEX `fk_auto_sede_idx` (`sede_idsede` ASC) VISIBLE,
  CONSTRAINT `fk_auto_sede`
    FOREIGN KEY (`sede_idsede`)
    REFERENCES `mydb`.`sede` (`idsede`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

#Crear datos

INSERT INTO sede (idsede, distrito, direccion) VALUES
(1, 'Miraflores', 'Av. Larco 123'),
(2, 'San Isidro', 'Calle Los Laureles 456'),
(3, 'Surco', 'Av. Caminos del Inca 789');

INSERT INTO auto (idauto, modelo, color, kilometraje, costo_por_dia, sede_idsede) VALUES
(1, 'Toyota Corolla', 'Rojo', 35000, 120.50, 1),
(2, 'Hyundai Elantra', 'Negro', 22000, 110.00, 2),
(3, 'Kia Rio', 'Blanco', 27000, 105.75, 3),
(4, 'Chevrolet Spark', 'Azul', 18000, 90.00, 1),
(5, 'Mazda 3', 'Gris', 40000, 130.25, 2);

INSERT INTO seguro (idseguro, empresa_aseguradora, cobertura_maxima, tarifa) VALUES
(1, 'Rimac Seguros', 15000.00, 15.50),
(2, 'La Positiva', 20000.00, 18.75),
(3, 'Mapfre', 25000.00, 20.00);