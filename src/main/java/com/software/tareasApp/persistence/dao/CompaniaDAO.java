package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Compania;
import com.software.tareasApp.persistence.repository.CompaniaRepository;
import com.software.tareasApp.view.constantes.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompaniaDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final CompaniaRepository repository;

    @Autowired
    public CompaniaDAO(CompaniaRepository repository){
        this.repository= repository;
    }

    public List<Compania> getCompanias() {
        log.info(TareaAppApplication.usuario, "getCompanias");
        return repository.findAllByOrderByNombreAsc();
    }

    public Compania getCompaniaById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getCompania " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("La compania con este id no existe ", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Compania saveCompania(Compania compania) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "guardar compania " + compania.toStringLog());
        return this.repository.save(compania);
    }

    public Integer countByNombre(String nombre){
        return this.repository.countByNombre(nombre);
    }

    public List<Compania> saveCompanias(List<Compania> companies) throws TareasAppException {
        List<Compania> finalList = new ArrayList<>();
        this.repository
                .saveAll(companies)
                .forEach(
                        company -> {
                            finalList.add(company);
                        });
        return finalList;
    }

    public void deleteCompania(Compania compania) {
        log.info(TareaAppApplication.usuario, "Borrar compania " + compania.toStringLog());
        this.repository.delete(compania);
    }

    public Compania updateCompania(Compania compania) throws TareasAppException {
        if (compania.getId() != null) {
            log.info(TareaAppApplication.usuario, "actualizar compania " + compania.toStringLog());
            return this.repository.save(compania);
        } else {
            String msg = String.format("No se puede actualizar sin Id asociada");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }
    
}
