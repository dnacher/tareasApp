package com.software.tareasApp.domain.service;

import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.dao.BancoDAO;
import com.software.tareasApp.persistence.model.Banco;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BancoService {
    
    private final BancoDAO bancoDAO;
    
    public BancoService(BancoDAO bancoDAO){
        this.bancoDAO = bancoDAO;
    }

    public List<Banco> getBancos(){
        return bancoDAO.getBancos();
    }

    public Banco getBancoById(Integer id){
        return bancoDAO.getBancoById(id);
    }

    public Banco saveBanco(Banco banco) throws TareasAppException{
        return bancoDAO.saveBanco(banco);
    }

    public Banco updateBanco(Banco banco){
        return bancoDAO.updateBanco(banco);
    }

    public void deleteBanco(Banco banco){
        bancoDAO.deleteBanco(banco);
    }

    public Integer countBancoByNombre(String nombre){ return bancoDAO.countBancoByNombre(nombre); }

}
