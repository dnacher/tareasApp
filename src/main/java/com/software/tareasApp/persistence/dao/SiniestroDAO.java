package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.persistence.model.Siniestro;
import com.software.tareasApp.persistence.repository.SiniestroRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SiniestroDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final SiniestroRepository repository;
    
    public SiniestroDAO(SiniestroRepository repository){
        this.repository = repository;
    }

    public List<Siniestro> getSiniestros() {
        List<Siniestro> siniestros = new ArrayList<>();
        this.repository.findAll().forEach(siniestro -> siniestros.add(siniestro));
        log.info(TareaAppApplication.usuario, "getSiniestros");
        return siniestros;
    }

    public Siniestro getSiniestroById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getSiniestroById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The crash id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Siniestro saveSiniestro(Siniestro siniestro) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveSiniestro " + siniestro.toStringLog());
        return this.repository.save(siniestro);
    }

    public List<Siniestro> saveSiniestros(List<Siniestro> siniestros) throws TareasAppException {
        List<Siniestro> finalList = new ArrayList<>();
        this.repository
                .saveAll(siniestros)
                .forEach(
                        siniestro -> {
                            finalList.add(siniestro);
                        });
        return finalList;
    }

    public void deleteSiniestros(Siniestro siniestro) {
        log.info(TareaAppApplication.usuario, "deleteSiniestros " + siniestro.toStringLog());
        this.repository.delete(siniestro);
    }

    public Siniestro updateSiniestros(Siniestro siniestro) throws TareasAppException {
        if (siniestro.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateSiniestros " + siniestro.toStringLog());
            return this.repository.save(siniestro);
        } else {
            String msg = String.format("Cannot update a crash without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public List<Siniestro> findByPoliza(Poliza poliza){
        log.info(TareaAppApplication.usuario, "findByPoliza: " + poliza.toStringLog());
        return this.repository.findByPoliza(poliza);
    }

    public List<Siniestro> findByCliente(Cliente cliente){
        log.info(TareaAppApplication.usuario, "findByCliente: " + cliente.getNombreYApellido() + " " + cliente.getCedulaIdentidad());
        return this.repository.findByCliente(cliente);
    }
}
