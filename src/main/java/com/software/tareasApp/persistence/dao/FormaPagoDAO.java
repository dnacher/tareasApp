package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.FormaPago;
import com.software.tareasApp.persistence.repository.FormaPagoRepository;
import com.software.tareasApp.view.constantes.Constantes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FormaPagoDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final FormaPagoRepository repository;
    
    public FormaPagoDAO(FormaPagoRepository repository){
        this.repository = repository;
    }

    public List<FormaPago> getFormaPagos() {
        List<FormaPago> formaPagos = new ArrayList<>();
        this.repository.findAll().forEach(formaPago -> formaPagos.add(formaPago));
        log.info(TareaAppApplication.usuario,  "getProductTypes");
        return formaPagos;
    }

    public FormaPago getFormaPagoById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getProductTypeById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The productType id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public FormaPago saveFormaPago(FormaPago formaPago) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveProductType " + formaPago.toStringLog());
        return this.repository.save(formaPago);
    }

    public List<FormaPago> saveFormaPagos(List<FormaPago> policies) throws TareasAppException {
        List<FormaPago> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteFormaPago(FormaPago formaPago) {
        log.info(TareaAppApplication.usuario, "deleteProductType " + formaPago.toStringLog());
        this.repository.delete(formaPago);
    }

    public FormaPago updateFormaPago(FormaPago formaPago) throws TareasAppException {
        if (formaPago.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateProductType " + formaPago.toStringLog());
            return this.repository.save(formaPago);
        } else {
            String msg = String.format("Cannot update a productType without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

}
