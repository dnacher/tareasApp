package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Vendedor;
import com.software.tareasApp.persistence.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VendedorDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final VendedorRepository repository;
    
    @Autowired
    public VendedorDAO(VendedorRepository repository){
        this.repository= repository;
    }

    public List<Vendedor> getVendedores() {
        List<Vendedor> vendedores = new ArrayList<>();
        this.repository.findAll().forEach(vendedor -> vendedores.add(vendedor));
        log.info(TareaAppApplication.usuario, "getVendedores");
        return vendedores;
    }

    public Vendedor getVendedorById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getVendedor " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The vendedor id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Vendedor saveVendedor(Vendedor vendedor) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveVendedor " + vendedor.toStringLog());
        return this.repository.save(vendedor);
    }
    
    public List<Vendedor> saveVendedores(List<Vendedor> vendedores) throws TareasAppException {
        List<Vendedor> finalList = new ArrayList<>();
        this.repository
                .saveAll(vendedores)
                .forEach(
                        vendedor -> {
                            finalList.add(vendedor);
                        });
        return finalList;
    }

    public void deleteVendedor(Vendedor vendedor) {
        log.info(TareaAppApplication.usuario, "deleteVendedor " + vendedor.toStringLog());
        this.repository.delete(vendedor);
    }

    public Vendedor updateVendedor(Vendedor vendedor) throws TareasAppException {
        if (vendedor.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateVendedor " + vendedor.toStringLog());
            return this.repository.save(vendedor);
        } else {
            String msg = String.format("Cannot update a vendedor without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }
}
