package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.ClienteDAO;
import com.software.tareasApp.persistence.model.Cliente;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ClienteService {
    
    private final ClienteDAO clienteDAO;
    
    public ClienteService(ClienteDAO clienteDAO){
        this.clienteDAO = clienteDAO;
    }

    public List<Cliente> getClientes(){
        return clienteDAO.getClientes();
    }

    public Cliente getClientById(Integer id){
        return clienteDAO.getClienteById(id);
    }

    public Cliente saveClient(Cliente cliente){
        return clienteDAO.saveClient(cliente);
    }

    public Cliente updateClient(Cliente cliente){
        return clienteDAO.updateClient(cliente);
    }

    public void deleteClient(Cliente cliente){
        clienteDAO.deleteClient(cliente);
    }

    public List<Cliente> findAllByFechaNacimientoBetween(Date fechaDesde, Date fechaHasta){ return clienteDAO.findAllByFechaNacimientoBetween(fechaDesde, fechaHasta); }

    public List<Cliente> getAniversary(int diaInicio, int diaFinal, int mes){
        return clienteDAO.getAniversary(diaInicio, diaFinal, mes);
    }

}