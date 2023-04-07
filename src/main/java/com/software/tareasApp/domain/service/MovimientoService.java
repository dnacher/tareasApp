package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.MovimientoDAO;
import com.software.tareasApp.persistence.model.CuentaAhorro;
import com.software.tareasApp.persistence.model.Movimiento;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MovimientoService {
    
    private final MovimientoDAO movimientoDAO;
    
    public MovimientoService(MovimientoDAO movimientoDAO){
        this.movimientoDAO = movimientoDAO;
    }

    public List<Movimiento> getMovimientos(){
        return movimientoDAO.getMovimientos();
    }

    public Movimiento saveMovimiento(Movimiento movimiento){
        return movimientoDAO.saveMovimiento(movimiento);
    }

    public void deleteMovimiento(Movimiento movimiento) throws SQLIntegrityConstraintViolationException {
        movimientoDAO.deleteMovimiento(movimiento);
    }

    public Movimiento findByCuentaAhorroAndConcepto(CuentaAhorro cuentaAhorro, String concepto){
        return this.movimientoDAO.findByCuentaAhorroAndConcepto(cuentaAhorro, concepto);
    }

    public List<Movimiento> findByCuentaAhorro(CuentaAhorro cuentaAhorro){
        return this.movimientoDAO.findByCuentaAhorro(cuentaAhorro);
    }

    public List<Movimiento> findByCuentaAhorroAndFechaBetween(CuentaAhorro cuentaAhorro, Date fechaDesde, Date fechaHasta){
        return this.movimientoDAO.findByCuentaAhorroAndFechaBetween(cuentaAhorro, fechaDesde, fechaHasta);
    }

}
