package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.CuentaAhorro;
import com.software.tareasApp.persistence.model.Tarea;
import com.software.tareasApp.persistence.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Daniel Nacher
 * 2023-03-28
 */

@Repository
public interface CuentaAhorroRepository extends JpaRepository<CuentaAhorro, Integer> {
    CuentaAhorro findByUsuario(Usuario usuario);
    CuentaAhorro findByNumeroCuenta(String numeroCuenta);
}
