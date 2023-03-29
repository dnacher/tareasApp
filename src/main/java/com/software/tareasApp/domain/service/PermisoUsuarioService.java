package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.PermissionUserDAO;
import com.software.tareasApp.persistence.model.PermisoUsuario;
import com.software.tareasApp.persistence.model.TipoUsuario;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PermisoUsuarioService {
    
    private final PermissionUserDAO permissionUserDAO;
    
    public PermisoUsuarioService(PermissionUserDAO permissionUserDAO){
        this.permissionUserDAO= permissionUserDAO;
    }

    public List<PermisoUsuario> findAllByUserType(TipoUsuario tipoUsuario){
        return permissionUserDAO.findAllByUserType(tipoUsuario);
    }

    public List<PermisoUsuario> savePermissionUserList(List<PermisoUsuario> listaPermisoUsuarios){
        List<PermisoUsuario> finalist = new ArrayList<>();
        listaPermisoUsuarios.stream().forEach(pu -> {
            finalist.add(permissionUserDAO.savePermissionUser(pu));
        });
        return finalist;
    }

    public void deleteByTipoUsuario(TipoUsuario tu){
        permissionUserDAO.deleteByTipoUsuario(tu);
    }

}
