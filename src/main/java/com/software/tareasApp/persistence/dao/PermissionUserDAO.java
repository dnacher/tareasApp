package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.PermisoUsuario;
import com.software.tareasApp.persistence.model.TipoUsuario;
import com.software.tareasApp.persistence.repository.PermissionUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionUserDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final PermissionUserRepository repository;
    
    public PermissionUserDAO(PermissionUserRepository repository){
        this.repository = repository;
    }

    public List<PermisoUsuario> findAllByUserType(TipoUsuario tipoUsuario) {
        log.info(TareaAppApplication.usuario, "findAllByUserType: " + tipoUsuario.toStringLog());
        return this.repository.findAllByTipoUsuario(tipoUsuario);
    }

    public PermisoUsuario savePermissionUser(PermisoUsuario permisoUsuario) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "savePermissionUser: " + permisoUsuario.toStringLog());
        return this.repository.save(permisoUsuario);
    }

    public void deleteByTipoUsuario(TipoUsuario tu){
        repository.deleteByTipoUsuario(tu);
    }

}
