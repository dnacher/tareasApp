ALTER TABLE `tareas-app`.`poliza`
    CHANGE COLUMN `comision_porcentaje` `comision_porcentaje` DECIMAL(9,2) NULL DEFAULT NULL ,
    CHANGE COLUMN `comision_valor` `comision_valor` DECIMAL(9,2) NULL DEFAULT NULL ,
    CHANGE COLUMN `comision_vendedor_porcentaje` `comision_vendedor_porcentaje` DECIMAL(9,2) NULL DEFAULT NULL ,
    CHANGE COLUMN `comision_vendedor_valor` `comision_vendedor_valor` DECIMAL(9,2) NULL DEFAULT NULL ;
