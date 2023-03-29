package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.EstadoPoliza;
import com.software.tareasApp.persistence.repository.EstadoPolizaRepository;
import com.software.tareasApp.view.constantes.Constantes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EstadoPolizaDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final EstadoPolizaRepository repository;
    
    public EstadoPolizaDAO(EstadoPolizaRepository repository){
        this.repository = repository;
    }

    public List<EstadoPoliza> getEstadoPolizas() {
        List<EstadoPoliza> estadoPolizas = new ArrayList<>();
        this.repository.findAll().forEach(estadoPoliza -> estadoPolizas.add(estadoPoliza));
        log.info(TareaAppApplication.usuario, "getEstadoPolizas");
        return estadoPolizas;
    }

    public EstadoPoliza getEstadoPolizaById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getEstadoPolizaById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("El id %s EstadoPoliza no existe", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public EstadoPoliza saveEstadoPoliza(EstadoPoliza estadoPoliza) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveEstadoPoliza " + estadoPoliza.toStringLog());
        return this.repository.save(estadoPoliza);
    }

    public List<EstadoPoliza> saveEstadoPoliza(List<EstadoPoliza> policies) throws TareasAppException {
        List<EstadoPoliza> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteEstadoPoliza(EstadoPoliza estadoPoliza) {
        log.info(TareaAppApplication.usuario, "deleteEstadoPoliza " + estadoPoliza.toStringLog());
        this.repository.delete(estadoPoliza);
    }

    public EstadoPoliza updateEstadoPoliza(EstadoPoliza estadoPoliza) throws TareasAppException {
        if (estadoPoliza.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateEstadoPoliza " + estadoPoliza.toStringLog());
            return this.repository.save(estadoPoliza);
        } else {
            String msg = String.format("Cannot update a EstadoPoliza without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public Integer countByNombre(String nombre){
        return this.repository.countByNombre(nombre);
    }
}
