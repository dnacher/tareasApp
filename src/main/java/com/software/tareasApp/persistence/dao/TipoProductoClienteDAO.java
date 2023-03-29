package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.TipoProductoCliente;
import com.software.tareasApp.persistence.repository.TipoProductoClienteRepository;
import com.software.tareasApp.view.constantes.Constantes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TipoProductoClienteDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final TipoProductoClienteRepository repository;
    
    public TipoProductoClienteDAO(TipoProductoClienteRepository repository){
        this.repository = repository;
    }

    public List<TipoProductoCliente> getTipoProductoClientes() {
        List<TipoProductoCliente> tipoProductoClientes = new ArrayList<>();
        this.repository.findAll().forEach(tipoProductoCliente -> tipoProductoClientes.add(tipoProductoCliente));
        log.info(TareaAppApplication.usuario,  "getTipoProductoClientes");
        return tipoProductoClientes;
    }

    public TipoProductoCliente getTipoProductoClienteById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getTipoProductoClienteById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The productType id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public TipoProductoCliente saveTipoProductoCliente(TipoProductoCliente tipoProductoCliente) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveTipoProductoCliente " + tipoProductoCliente.toStringLog());
        return this.repository.save(tipoProductoCliente);
    }

    public List<TipoProductoCliente> saveTipoProductoClientes(List<TipoProductoCliente> policies) throws TareasAppException {
        List<TipoProductoCliente> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteTipoProductoCliente(TipoProductoCliente tipoProductoCliente) {
        log.info(TareaAppApplication.usuario, "deleteTipoProductoCliente " + tipoProductoCliente.toStringLog());
        this.repository.delete(tipoProductoCliente);
    }

    public TipoProductoCliente updateTipoProductoCliente(TipoProductoCliente tipoProductoCliente) throws TareasAppException {
        if (tipoProductoCliente.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateTipoProductoCliente " + tipoProductoCliente.toStringLog());
            return this.repository.save(tipoProductoCliente);
        } else {
            String msg = String.format("Cannot update a productType without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public List<TipoProductoCliente> findByCliente(Cliente cliente){
        log.info(TareaAppApplication.usuario, "findByCliente " + cliente.toStringLog());
        return repository.findByCliente(cliente);
    }
}
