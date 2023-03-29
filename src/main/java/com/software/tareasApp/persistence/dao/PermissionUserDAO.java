package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.PermisoUsuario;
import com.software.tareasApp.persistence.model.TipoUsuario;
import com.software.tareasApp.persistence.repository.PermissionUserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissionUserDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final PermissionUserRepository repository;
    
    public PermissionUserDAO(PermissionUserRepository repository){
        this.repository = repository;
    }

    public List<PermisoUsuario> getPermissionUsers() {
        List<PermisoUsuario> permisoUsuarios = new ArrayList<>();
        this.repository.findAll().forEach(permisoUsuario -> permisoUsuarios.add(permisoUsuario));
        log.info(TareaAppApplication.usuario, "getPermissionUsers");
        return permisoUsuarios;
    }

    public PermisoUsuario getPermissionUserById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getPermissionUser: " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The permissionUser id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public List<PermisoUsuario> findAllByUserType(TipoUsuario tipoUsuario) {
        log.info(TareaAppApplication.usuario, "findAllByUserType: " + tipoUsuario.toStringLog());
        return this.repository.findAllByTipoUsuario(tipoUsuario);
    }

    public PermisoUsuario savePermissionUser(PermisoUsuario permisoUsuario) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "savePermissionUser: " + permisoUsuario.toStringLog());
        return this.repository.save(permisoUsuario);
    }

    public List<PermisoUsuario> savePermissionUsers(List<PermisoUsuario> permisoUsuarios) throws TareasAppException {
        List<PermisoUsuario> finalList = new ArrayList<>();
        this.repository
                .saveAll(permisoUsuarios)
                .forEach(
                        permisoUsuario -> {
                            finalList.add(permisoUsuario);
                        });
        return finalList;
    }

    public void deletePermissionUser(PermisoUsuario permisoUsuario) {
        log.info(TareaAppApplication.usuario, "deletePermissionUser " + permisoUsuario.toStringLog());
        this.repository.delete(permisoUsuario);
    }

    public void deleteByTipoUsuario(TipoUsuario tu){
        repository.deleteByTipoUsuario(tu);
    }

    public PermisoUsuario updatePermissionUser(PermisoUsuario permisoUsuario) throws TareasAppException {
        if (permisoUsuario.getId() != null) {
            log.info(TareaAppApplication.usuario, "updatePermissionUser " + permisoUsuario.toStringLog());
            return this.repository.save(permisoUsuario);
        } else {
            String msg = String.format("Cannot update a permission User without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }
}
