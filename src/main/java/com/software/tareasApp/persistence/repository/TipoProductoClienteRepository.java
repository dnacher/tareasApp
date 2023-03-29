package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.TipoProductoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoProductoClienteRepository extends JpaRepository<TipoProductoCliente, Integer> {
    List<TipoProductoCliente> findByCliente(Cliente cliente);
}
