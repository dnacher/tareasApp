package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.CuentaAhorroDAO;
import com.software.tareasApp.persistence.model.CuentaAhorro;
import com.software.tareasApp.persistence.model.Usuario;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
@Transactional
public class CuentaAhorroService {
    
    private final CuentaAhorroDAO cuentaAhorroDAO;
    
    public CuentaAhorroService(CuentaAhorroDAO cuentaAhorroDAO){
        this.cuentaAhorroDAO = cuentaAhorroDAO;
    }

    public List<CuentaAhorro> getCuentaAhorros(){
        return cuentaAhorroDAO.getCuentaAhorros();
    }

    public CuentaAhorro findByUsuario(Usuario usuario){
        return cuentaAhorroDAO.findByUsuario(usuario);
    }

    public CuentaAhorro findByNumeroCuenta(String numeroCuenta){
        return this.cuentaAhorroDAO
                .findByNumeroCuenta(numeroCuenta);
    }

    public CuentaAhorro saveCuentaAhorro(CuentaAhorro cuentaAhorro){
        return cuentaAhorroDAO.saveCuentaAhorro(cuentaAhorro);
    }

    public void deleteCuentaAhorro(CuentaAhorro cuentaAhorro) throws SQLIntegrityConstraintViolationException {
        cuentaAhorroDAO.deleteCuentaAhorro(cuentaAhorro);
    }

}
