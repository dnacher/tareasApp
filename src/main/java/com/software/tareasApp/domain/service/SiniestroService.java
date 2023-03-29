package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.SiniestroDAO;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.persistence.model.Siniestro;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SiniestroService {
    
    private final SiniestroDAO siniestroDAO;
    
    public SiniestroService(SiniestroDAO siniestroDAO){
        this.siniestroDAO = siniestroDAO;
    }

    public List<Siniestro> getSiniestros(){
        return siniestroDAO.getSiniestros();
    }

    public Siniestro getSiniestroById(Integer id){
        return siniestroDAO.getSiniestroById(id);
    }

    public Siniestro saveSiniestro(Siniestro siniestro){
        return siniestroDAO.saveSiniestro(siniestro);
    }

    public Siniestro updateSiniestro(Siniestro siniestro){
        return siniestroDAO.updateSiniestros(siniestro);
    }

    public void deleteSiniestro(Siniestro siniestro){
        siniestroDAO.deleteSiniestros(siniestro);
    }

    public List<Siniestro> findByPoliza(Poliza poliza){ return siniestroDAO.findByPoliza(poliza); }

    public List<Siniestro> findByCliente(Cliente cliente){
        return siniestroDAO.findByCliente(cliente);
    }

}