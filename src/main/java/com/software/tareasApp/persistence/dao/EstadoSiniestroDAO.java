package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.EstadoSiniestro;
import com.software.tareasApp.persistence.repository.EstadoSiniestroRepository;
import com.software.tareasApp.view.constantes.Constantes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EstadoSiniestroDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final EstadoSiniestroRepository repository;
    
    public EstadoSiniestroDAO(EstadoSiniestroRepository repository){
        this.repository = repository;
    }

    public List<EstadoSiniestro> getEstadoSiniestros() {
        List<EstadoSiniestro> estadoSiniestros = new ArrayList<>();
        this.repository.findAll().forEach(estadoSiniestro -> estadoSiniestros.add(estadoSiniestro));
        log.info(TareaAppApplication.usuario,  "getEstadoSiniestros");
        return estadoSiniestros;
    }

    public EstadoSiniestro getEstadoSiniestroById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getEstadoSiniestroById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The EstadoSiniestro id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public EstadoSiniestro saveEstadoSiniestro(EstadoSiniestro estadoSiniestro) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveEstadoSiniestro " + estadoSiniestro.toStringLog());
        return this.repository.save(estadoSiniestro);
    }

    public List<EstadoSiniestro> saveEstadoSiniestro(List<EstadoSiniestro> policies) throws TareasAppException {
        List<EstadoSiniestro> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteEstadoSiniestro(EstadoSiniestro estadoSiniestro) {
        log.info(TareaAppApplication.usuario, "deleteEstadoSiniestro " + estadoSiniestro.toStringLog());
        this.repository.delete(estadoSiniestro);
    }

    public EstadoSiniestro updateEstadoSiniestro(EstadoSiniestro estadoSiniestro) throws TareasAppException {
        if (estadoSiniestro.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateEstadoSiniestro " + estadoSiniestro.toStringLog());
            return this.repository.save(estadoSiniestro);
        } else {
            String msg = String.format("Cannot update a EstadoSiniestro without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public Integer countByNombre(String nombre){
        return this.repository.countByNombre(nombre);
    }
}
