package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ClienteDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final ClienteRepository repository;
    
    @Autowired
    public ClienteDAO(ClienteRepository repository){
        this.repository= repository;
    }

    public List<Cliente> getClientes() {
        return repository.findAllByOrderByNombreAscApellidoAsc();
    }

    public Cliente getClienteById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getCliente " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("El cliente con esta id no existe ", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Cliente saveClient(Cliente cliente) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "Guardar cliente " + cliente.toStringLog());
        return this.repository.save(cliente);
    }
    
    public List<Cliente> saveClients(List<Cliente> clientes) throws TareasAppException {
        List<Cliente> finalList = new ArrayList<>();
        this.repository
                .saveAll(clientes)
                .forEach(
                        client -> {
                            finalList.add(client);
                        });
        return finalList;
    }

    public void deleteClient(Cliente cliente) {
        log.info(TareaAppApplication.usuario, "Borrar cliente " + cliente.toStringLog());
        this.repository.delete(cliente);
    }

    public Cliente updateClient(Cliente cliente) throws TareasAppException {
        if (cliente.getId() != null) {
            log.info(TareaAppApplication.usuario, "Actualizar cliente " + cliente.toStringLog());
            return this.repository.save(cliente);
        } else {
            String msg = String.format("No se puede actualizar un cliente sin Id asociada" + cliente.toStringLog());
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public List<Cliente> findAllByFechaNacimientoBetween(Date fechaDesde, Date fechaHasta){
        log.info(TareaAppApplication.usuario, "findAllByFechaNacimientoBetween" + fechaDesde + "-" + fechaHasta);
        return repository.findAllByFechaNacimientoBetween(fechaDesde, fechaHasta);
    }

    public List<Cliente> getAniversary(int diaInicio, int diaFinal, int mes){
        log.info(TareaAppApplication.usuario, "getAniversary");
        return repository.getAniversary(diaInicio, diaFinal, mes);
    }
}
