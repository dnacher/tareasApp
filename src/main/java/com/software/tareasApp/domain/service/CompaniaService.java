package com.software.tareasApp.domain.service;

import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.dao.CompaniaDAO;
import com.software.tareasApp.persistence.model.Compania;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CompaniaService {
    
    private final CompaniaDAO companiaDAO;
    
    public CompaniaService(CompaniaDAO companiaDAO){
        this.companiaDAO = companiaDAO;
    }

    public List<Compania> getCompanias(){
        return companiaDAO.getCompanias();
    }

    public Compania getCompanyById(Integer id){
        return companiaDAO.getCompaniaById(id);
    }

    public Compania saveCompany(Compania compania) throws TareasAppException{
        return companiaDAO.saveCompania(compania);
    }

    public Compania updateCompany(Compania compania){
        return companiaDAO.updateCompania(compania);
    }

    public void deleteCompany(Compania compania){
        companiaDAO.deleteCompania(compania);
    }

}
