package com.software.tareasApp.domain.service;

import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.dao.RegsitroCuotasDAO;
import com.software.tareasApp.persistence.model.Compania;
import com.software.tareasApp.persistence.model.RegistroCuotas;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RegistroCuotasService {
    
    private final RegsitroCuotasDAO registroCuotasDAO;
    
    public RegistroCuotasService(RegsitroCuotasDAO registroCuotasDAO){
        this.registroCuotasDAO = registroCuotasDAO;
    }

    public List<RegistroCuotas> getRegistroCuotas(){
        return registroCuotasDAO.getRegistroCuotas();
    }

    public RegistroCuotas getRegistroCuotasById(Integer id){
        return registroCuotasDAO.getRegistroCuotasById(id);
    }

    public RegistroCuotas saveRegistroCuotas(RegistroCuotas registroCuotas) throws TareasAppException{
        return registroCuotasDAO.saveRegistroCuotas(registroCuotas);
    }

    public List<RegistroCuotas> saveRegistroCuotasList(List<RegistroCuotas> registroCuotasList) throws TareasAppException {
        return this.registroCuotasDAO.saveRegistroCuotasList(registroCuotasList);
    }

    public RegistroCuotas updateRegistroCuotas(RegistroCuotas registroCuotas){
        return registroCuotasDAO.updateRegistroCuotas(registroCuotas);
    }

    public void deleteRegistroCuotas(RegistroCuotas registroCuotas){
        registroCuotasDAO.deleteRegistroCuotas(registroCuotas);
    }

    public List<RegistroCuotas> getRegistrosCuotasConCuotas(){
        return this.registroCuotasDAO.getRegistrosCuotasConCuotas();
    }

    public List<RegistroCuotas> getRegsitrosCuotasByAnioAndCompania(Compania compania, Integer anio){
        return this.registroCuotasDAO.getRegsitrosCuotasByAnioAndCompania(compania, anio);
    }

}
