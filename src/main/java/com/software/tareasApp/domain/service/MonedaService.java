package com.software.tareasApp.domain.service;

import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.dao.MonedaDAO;
import com.software.tareasApp.persistence.model.Moneda;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MonedaService {
    
    private final MonedaDAO monedaDAO;
    
    public MonedaService(MonedaDAO monedaDAO){
        this.monedaDAO= monedaDAO;
    }

    public List<Moneda> getMonedas(){
        return monedaDAO.getMonedas();
    }

    public Moneda getMonedaById(Integer id){
        return monedaDAO.getMonedaById(id);
    }

    public Moneda saveMoneda(Moneda moneda){
        return monedaDAO.saveMoneda(moneda);
    }

    public Moneda updateMoneda(Moneda moneda){
        return monedaDAO.updateMoneda(moneda);
    }

    public void deleteMoneda(Moneda moneda){
        monedaDAO.deleteMoneda(moneda);
    }

}
