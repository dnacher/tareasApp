package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.CuentaAhorro;
import com.software.tareasApp.persistence.model.Movimiento;
import com.software.tareasApp.persistence.repository.MovimientoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MovimientoDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final MovimientoRepository repository;
    
    public MovimientoDAO(MovimientoRepository repository){
        this.repository = repository;
    }

    public List<Movimiento> getMovimientos() {
        List<Movimiento> policies = new ArrayList<>();
        this.repository.findAll().forEach(userType -> policies.add(userType));
        log.info(TareaAppApplication.usuario,"getMovimientos");
        return policies;
    }

    public Movimiento saveMovimiento(Movimiento movimiento) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveMovimiento " + movimiento);
        return this.repository.save(movimiento);
    }

    public void deleteMovimiento(Movimiento movimiento) {
        log.info(TareaAppApplication.usuario, "deleteMovimiento " + movimiento.toStringLog());
        this.repository.delete(movimiento);
    }

    public Movimiento findByCuentaAhorroAndConcepto(CuentaAhorro cuentaAhorro, String concepto){
        log.info(TareaAppApplication.usuario, "findByCuentaAhorroAndConcepto " + cuentaAhorro.toStringLog());
        return this.repository.findByCuentaAhorroAndConcepto(cuentaAhorro, concepto);
    }

    public List<Movimiento> findByCuentaAhorro(CuentaAhorro cuentaAhorro){
        log.info(TareaAppApplication.usuario, "findByCuentaAhorro " + cuentaAhorro.toStringLog());
        return this.repository.findByCuentaAhorro(cuentaAhorro);
    }

    public List<Movimiento> findByCuentaAhorroAndFechaBetween(CuentaAhorro cuentaAhorro, Date fechaDesde, Date fechaHasta){
        log.info(TareaAppApplication.usuario, "findByCuentaAhorroAndFechaBetween " + cuentaAhorro.toStringLog());
        return this.repository.findByCuentaAhorroAndFechaBetween(cuentaAhorro, fechaDesde, fechaHasta);
    }
}
