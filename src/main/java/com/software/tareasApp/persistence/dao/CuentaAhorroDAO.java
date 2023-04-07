package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.CuentaAhorro;
import com.software.tareasApp.persistence.model.Usuario;
import com.software.tareasApp.persistence.repository.CuentaAhorroRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Daniel Nacher
 * 2023-03-28
 */

@Component
public class CuentaAhorroDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final CuentaAhorroRepository repository;

    public CuentaAhorroDAO(CuentaAhorroRepository repository){
        this.repository = repository;
    }

    public List<CuentaAhorro> getCuentaAhorros() {
        List<CuentaAhorro> cuentaAhorros = new ArrayList<>();
        this.repository.findAll().forEach(cuentaAhorro -> cuentaAhorros.add(cuentaAhorro));
        log.info(TareaAppApplication.usuario, "getCuentaAhorros");
        return cuentaAhorros;
    }

    public CuentaAhorro findByUsuario(Usuario usuario){
        log.info(TareaAppApplication.usuario, "findByUsuario " + usuario.getNombre());
        return this.repository
                .findByUsuario(usuario);
    }

    public CuentaAhorro findByNumeroCuenta(String numeroCuenta){
        log.info(TareaAppApplication.usuario, "findByNumeroCuenta " + numeroCuenta);
        return this.repository
                .findByNumeroCuenta(numeroCuenta);
    }

    public CuentaAhorro saveCuentaAhorro(CuentaAhorro cuentaAhorro) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveCuentaAhorro " + cuentaAhorro.toStringLog());
        return this.repository.save(cuentaAhorro);
    }


    public void deleteCuentaAhorro(CuentaAhorro cuentaAhorro) {
        log.info(TareaAppApplication.usuario, "deleteCuentaAhorros " + cuentaAhorro.toStringLog());
        this.repository.delete(cuentaAhorro);
    }

    public CuentaAhorro updateCuentaAhorro(CuentaAhorro cuentaAhorro) throws TareasAppException {
        if (cuentaAhorro.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateCuentaAhorros " + cuentaAhorro.toStringLog());
            return this.repository.save(cuentaAhorro);
        } else {
            String msg = String.format("Cannot update a crash without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

}
