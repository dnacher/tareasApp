package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Compania;
import com.software.tareasApp.persistence.model.RegistroCuotas;
import com.software.tareasApp.persistence.repository.RegsitroCuotasRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RegsitroCuotasDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final RegsitroCuotasRepository repository;
    
    public RegsitroCuotasDAO(RegsitroCuotasRepository repository){
        this.repository = repository;
    }

    public List<RegistroCuotas> getRegistroCuotas() {
        List<RegistroCuotas> registroCuotass = new ArrayList<>();
        this.repository.findAll().forEach(registroCuotas -> registroCuotass.add(registroCuotas));
        log.info(TareaAppApplication.usuario,  "getRegistroCuotas");
        return registroCuotass;
    }

    public RegistroCuotas getRegistroCuotasById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getRegsitroCuotasById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The productType id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public RegistroCuotas saveRegistroCuotas(RegistroCuotas registroCuotas) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveRegistroCuotas " + registroCuotas.toStringLog());
        return this.repository.save(registroCuotas);
    }

    public List<RegistroCuotas> saveRegistroCuotasList(List<RegistroCuotas> registroCuotasList) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveRegistroCuotasList " + registroCuotasList.toString());
        return this.repository.saveAll(registroCuotasList);
    }

    public List<RegistroCuotas> saveRegistroCuotas(List<RegistroCuotas> policies) throws TareasAppException {
        List<RegistroCuotas> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteRegistroCuotas(RegistroCuotas registroCuotas) {
        log.info(TareaAppApplication.usuario, "deleteRegistroCuotas " + registroCuotas.toStringLog());
        this.repository.delete(registroCuotas);
    }

    public RegistroCuotas updateRegistroCuotas(RegistroCuotas registroCuotas) throws TareasAppException {
        if (registroCuotas.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateRegistroCuotas " + registroCuotas.toStringLog());
            return this.repository.save(registroCuotas);
        } else {
            String msg = String.format("Cannot update a RegistroCuotas without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public List<RegistroCuotas> getRegistrosCuotasConCuotas(){
        log.info(TareaAppApplication.usuario, "getRegistrosCuotasConCuotas ");
        return this.repository.getRegistrosCuotasConCuotas();
    }

    public List<RegistroCuotas> getRegsitrosCuotasByAnioAndCompania(Compania compania, Integer anio){
        log.info(TareaAppApplication.usuario, "getRegsitrosCuotasByAnioAndCompania: Compañia: " + compania.getNombre() + " Año: " + anio);
        return this.repository.getRegsitrosCuotasByAnioAndCompania(compania, anio);
    }
}
