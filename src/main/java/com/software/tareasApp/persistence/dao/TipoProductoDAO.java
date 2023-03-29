package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.TipoProducto;
import com.software.tareasApp.persistence.repository.TipoProductoRepository;
import com.software.tareasApp.view.constantes.Constantes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TipoProductoDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final TipoProductoRepository repository;
    
    public TipoProductoDAO(TipoProductoRepository repository){
        this.repository = repository;
    }

    public List<TipoProducto> getTipoProductos() {
        return repository.findAllByOrderByNombre();
    }

    public TipoProducto getTipoProductosById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getTipoProductosById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The productType id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public TipoProducto saveTipoProductos(TipoProducto tipoProducto) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveTipoProductos " + tipoProducto.toStringLog());
        return this.repository.save(tipoProducto);
    }

    public List<TipoProducto> saveTipoProductos(List<TipoProducto> policies) throws TareasAppException {
        List<TipoProducto> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteTipoProducto(TipoProducto tipoProducto) {
        log.info(TareaAppApplication.usuario, "deleteTipoProducto " + tipoProducto.toStringLog());
        this.repository.delete(tipoProducto);
    }

    public TipoProducto updateTipoProducto(TipoProducto tipoProducto) throws TareasAppException {
        if (tipoProducto.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateTipoProducto " + tipoProducto.toStringLog());
            return this.repository.save(tipoProducto);
        } else {
            String msg = String.format("Cannot update a productType without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public Integer countByNombre(String nombre){
        log.info(TareaAppApplication.usuario, "countByNombre " + nombre);
        return this.repository.countByNombre(nombre);
    }
}
