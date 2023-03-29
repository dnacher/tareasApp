package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.CuotasPoliza;
import com.software.tareasApp.persistence.repository.CuotaPolizaRepository;
import com.software.tareasApp.persistence.repository.RegsitroCuotasRepository;
import com.software.tareasApp.view.constantes.Constantes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CuotasPolizaDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final CuotaPolizaRepository repository;
    
    public CuotasPolizaDAO(CuotaPolizaRepository repository){
        this.repository = repository;
    }

    public List<CuotasPoliza> getCuotasPolizas() {
        List<CuotasPoliza> cuotasPolizas = new ArrayList<>();
        this.repository.findAll().forEach(cuotasPoliza -> cuotasPolizas.add(cuotasPoliza));
        log.info(TareaAppApplication.usuario,  "getCuotasPoliza");
        return cuotasPolizas;
    }

    public CuotasPoliza getCuotasPolizaById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getCuotasPolizaById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("La cuota con este id no existe", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public CuotasPoliza saveCuotasPoliza(CuotasPoliza cuotasPoliza) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "Guardar cuota poliza " + cuotasPoliza.toStringLog());
        return this.repository.save(cuotasPoliza);
    }

    public List<CuotasPoliza> saveCuotasPolizas(List<CuotasPoliza> policies) throws TareasAppException {
        List<CuotasPoliza> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteCuotasPoliza(CuotasPoliza cuotasPoliza) {
        log.info(TareaAppApplication.usuario, "borrar CuotasPoliza " + cuotasPoliza.toStringLog());
        this.repository.delete(cuotasPoliza);
    }

    public CuotasPoliza updateCuotasPoliza(CuotasPoliza cuotasPoliza) throws TareasAppException {
        if (cuotasPoliza.getId() != null) {
            log.info(TareaAppApplication.usuario, "Actualizar CuotasPoliza " + cuotasPoliza.toStringLog());
            return this.repository.save(cuotasPoliza);
        } else {
            String msg = String.format("No se puede actualizar CuotasPoliza sin Id asociado");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }
    
}
