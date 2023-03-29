package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Moneda;
import com.software.tareasApp.persistence.repository.MonedaRepository;
import com.software.tareasApp.view.constantes.Constantes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MonedaDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final MonedaRepository repository;
    
    public MonedaDAO(MonedaRepository repository){
        this.repository = repository;
    }

    public List<Moneda> getMonedas() {
        List<Moneda> monedas = new ArrayList<>();
        this.repository.findAll().forEach(moneda -> monedas.add(moneda));
        log.info(TareaAppApplication.usuario,  "getMonedas");
        return monedas;
    }

    public Moneda getMonedaById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getMonedaById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The Moneda id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Moneda saveMoneda(Moneda moneda) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveMoneda " + moneda.toStringLog());
        return this.repository.save(moneda);
    }

    public List<Moneda> saveMoneda(List<Moneda> policies) throws TareasAppException {
        List<Moneda> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteMoneda(Moneda moneda) {
        log.info(TareaAppApplication.usuario, "deleteMoneda " + moneda.toStringLog());
        this.repository.delete(moneda);
    }

    public Moneda updateMoneda(Moneda moneda) throws TareasAppException {
        if (moneda.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateMoneda " + moneda.toStringLog());
            return this.repository.save(moneda);
        } else {
            String msg = String.format("Cannot update a Moneda without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public Integer countByNombre(String nombre){
        return this.repository.countByNombre(nombre);
    }
}
