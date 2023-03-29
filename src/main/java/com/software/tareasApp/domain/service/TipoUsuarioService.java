package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.TipoUsuarioDAO;
import com.software.tareasApp.persistence.model.TipoUsuario;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
@Transactional
public class TipoUsuarioService {
    
    private final TipoUsuarioDAO tipoUsuarioDAO;
    
    public TipoUsuarioService(TipoUsuarioDAO tipoUsuarioDAO){
        this.tipoUsuarioDAO = tipoUsuarioDAO;
    }

    public List<TipoUsuario> getTipoUsuarios(){
        return tipoUsuarioDAO.getTipoUsuarios();
    }

    public TipoUsuario saveTipoUsuario(TipoUsuario tipoUsuario){
        return tipoUsuarioDAO.saveTipoUsuario(tipoUsuario);
    }

    public void deleteTipoUsuario(TipoUsuario tipoUsuario) throws SQLIntegrityConstraintViolationException {
        tipoUsuarioDAO.deleteTipoUsuario(tipoUsuario);
    }

}
