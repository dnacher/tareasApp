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

    public TipoUsuario saveTipoUsuario(TipoUsuario tipoUsuario) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveTipoUsuario " + tipoUsuario);
        return this.repository.save(tipoUsuario);
    }

    public void deleteTipoUsuario(TipoUsuario tipoUsuario) {
        log.info(TareaAppApplication.usuario, "deleteTipoUsuario " + tipoUsuario.toStringLog());
        this.repository.delete(tipoUsuario);
    }
}
