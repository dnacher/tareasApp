package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Banco;
import com.software.tareasApp.persistence.model.Ingreso;
import com.software.tareasApp.persistence.repository.IngresoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IngresoDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final IngresoRepository repository;
    
    public IngresoDAO(IngresoRepository repository){
        this.repository = repository;
    }

    public List<Ingreso> getIngresos() {
        List<Ingreso> ingresos = new ArrayList<>();
        this.repository.findAll().forEach(ingreso -> ingresos.add(ingreso));
        log.info(TareaAppApplication.usuario, "getIngreso");
        return ingresos;
    }

    public Ingreso getIngresoById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getIngresoById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The income id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Ingreso saveIngreso(Ingreso ingreso) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveIngreso " + ingreso.toStringLog());
        return this.repository.save(ingreso);
    }

    public List<Ingreso> saveIngresos(List<Ingreso> ingresos) throws TareasAppException {
        List<Ingreso> finalList = new ArrayList<>();
        this.repository
                .saveAll(ingresos)
                .forEach(
                        ingreso -> {
                            finalList.add(ingreso);
                        });
        return finalList;
    }

    public void deleteIngreso(Ingreso ingreso) {
        log.info(TareaAppApplication.usuario, "deleteIngreso " + ingreso.toStringLog());
        this.repository.delete(ingreso);
    }

    public Ingreso updateIngreso(Ingreso ingreso) throws TareasAppException {
        if (ingreso.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateIngreso " + ingreso.toStringLog());
            return this.repository.save(ingreso);
        } else {
            String msg = String.format("Cannot update a income without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public Integer countByAnioAndMesAndBanco(Integer anio, Integer mes, Banco banco){
        log.info(TareaAppApplication.usuario, "countByAnioAndMesAndBanco " + anio + " " + mes  + " " + banco.getNombre());
        return repository.countByAnioAndMesAndBanco(anio, mes, banco);
    }
}
