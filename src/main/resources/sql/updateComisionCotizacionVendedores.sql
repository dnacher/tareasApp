ALTER TABLE `tareas-app`.`cotizacion_vendedor`
    CHANGE COLUMN `comision_nueva` `comision_nueva` DECIMAL(9,2) NULL DEFAULT NULL ,
    CHANGE COLUMN `comision_renovacion` `comision_renovacion` DECIMAL(9,2) NULL DEFAULT NULL ;
