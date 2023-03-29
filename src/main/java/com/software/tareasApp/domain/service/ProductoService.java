package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.ProductoDAO;
import com.software.tareasApp.persistence.model.Compania;
import com.software.tareasApp.persistence.model.Producto;
import com.software.tareasApp.persistence.model.TipoProducto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductoService {
    
    private final ProductoDAO productoDAO;
    
    public ProductoService(ProductoDAO productoDAO){
        this.productoDAO = productoDAO;
    }

    public List<Producto> getProductos(){
        return productoDAO.getProductos();
    }

    public Producto getProductoById(Integer id){
        return productoDAO.getProductoById(id);
    }

    public List<Producto> findByCompania(Compania compania){
        return productoDAO.findByCompania(compania);
    }

    public List<Producto> findByTipoProducto(TipoProducto tipoProducto){
        return productoDAO.findByTipoProducto(tipoProducto);
    }

    public Producto saveProducto(Producto producto){
        return productoDAO.saveProducto(producto);
    }

    public Producto updateProducto(Producto producto){
        return productoDAO.updateProducto(producto);
    }

    public void deleteProducto(Producto producto){
        productoDAO.deleteProducto(producto);
    }

}
