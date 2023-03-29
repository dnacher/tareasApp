package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.TipoUsuario;
import com.software.tareasApp.persistence.repository.TipoUsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TipoUsuarioDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final TipoUsuarioRepository repository;
    
    public TipoUsuarioDAO(TipoUsuarioRepository repository){
        this.repository = repository;
    }

    public List<TipoUsuario> getTipoUsuarios() {
        List<TipoUsuario> policies = new ArrayList<>();
        this.repository.findAll().forEach(userType -> policies.add(userType));
        log.info(TareaAppApplication.usuario,"getTipoUsuarios");
        return policies;
    }

    public TipoUsuario getTipoUsuarioById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario,"getTipoUsuarioById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The userType id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public TipoUsuario saveTipoUsuario(TipoUsuario tipoUsuario) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveTipoUsuario " + tipoUsuario);
        return this.repository.save(tipoUsuario);
    }

    public List<TipoUsuario> saveTipoUsuarios(List<TipoUsuario> policies) throws TareasAppException {
        List<TipoUsuario> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        userType -> {
                            finalList.add(userType);
                        });
        return finalList;
    }

    public void deleteTipoUsuario(TipoUsuario tipoUsuario) {
        log.info(TareaAppApplication.usuario, "deleteTipoUsuario " + tipoUsuario.toStringLog());
        this.repository.delete(tipoUsuario);
    }

    public TipoUsuario updateTipoUsuario(TipoUsuario tipoUsuario) throws TareasAppException {
        if (tipoUsuario.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateTipoUsuario " + tipoUsuario.toStringLog());
            return this.repository.save(tipoUsuario);
        } else {
            String msg = String.format("Cannot update a userType without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public Integer countByNombre(String nombre){
        log.info(TareaAppApplication.usuario, "countByNombre " + nombre);
        return this.repository.countByNombre(nombre);
    }
}
