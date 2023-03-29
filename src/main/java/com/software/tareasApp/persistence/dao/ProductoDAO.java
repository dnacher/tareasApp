package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Compania;
import com.software.tareasApp.persistence.model.Producto;
import com.software.tareasApp.persistence.model.TipoProducto;
import com.software.tareasApp.persistence.repository.ProductoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductoDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final ProductoRepository repository;
    
    public ProductoDAO(ProductoRepository repository){
        this.repository = repository;
    }

    public List<Producto> getProductos() {
        return repository.findAllByOrderByNombreAndTipoProductoAsc();
    }

    public Producto getProductoById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getProductoById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The product id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public List<Producto> findByCompania(Compania compania){
        log.info(TareaAppApplication.usuario, "findByCompania " + compania.toStringLog());
        return repository.findByCompania(compania);
    }

    public List<Producto> findByTipoProducto(TipoProducto tipoProducto){
        log.info(TareaAppApplication.usuario, "findByTipoProducto " + tipoProducto.toStringLog());
        return repository.findByTipoProducto(tipoProducto);
    }

    public Producto saveProducto(Producto producto) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveProducto " + producto.toStringLog());
        return repository.save(producto);
    }

    public List<Producto> saveProductos(List<Producto> policies) throws TareasAppException {
        List<Producto> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        product -> {
                            finalList.add(product);
                        });
        return finalList;
    }

    public void deleteProducto(Producto producto) {
        log.info(TareaAppApplication.usuario, "deleteProducto " + producto.toStringLog());
        this.repository.delete(producto);
    }

    public Producto updateProducto(Producto producto) throws TareasAppException {
        if (producto.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateProducto " + producto.toStringLog());
            return this.repository.save(producto);
        } else {
            String msg = String.format("Cannot update a product without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public Integer countByNombre(String nombre){
        log.info(TareaAppApplication.usuario, "countByNombre " + nombre);
        return this.repository.countByNombre(nombre);
    }
}
