package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.CuentaAhorro;
import com.software.tareasApp.persistence.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Daniel Nacher
 * 2023-03-28
 */

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    Movimiento findByCuentaAhorroAndConcepto(CuentaAhorro cuentaAhorro,String concepto);
    List<Movimiento> findByCuentaAhorro(CuentaAhorro cuentaAhorro);
    List<Movimiento> findByCuentaAhorroAndFechaBetween(CuentaAhorro cuentaAhorro, Date dateDesde, Date dateHasta);

}
