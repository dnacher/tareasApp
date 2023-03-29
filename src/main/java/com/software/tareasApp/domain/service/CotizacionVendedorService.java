package com.software.tareasApp.domain.service;

import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.dao.CotizacionVendedorDAO;
import com.software.tareasApp.persistence.model.CotizacionVendedor;
import com.software.tareasApp.persistence.model.Producto;
import com.software.tareasApp.persistence.model.Vendedor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CotizacionVendedorService {
    
    private final CotizacionVendedorDAO cotizacionVendedorDAO;
    
    public CotizacionVendedorService(CotizacionVendedorDAO cotizacionVendedorDAO){
        this.cotizacionVendedorDAO = cotizacionVendedorDAO;
    }

    public List<CotizacionVendedor> getCotizacionVendedores(){
        return cotizacionVendedorDAO.getCotizacionVendedores();
    }

    public CotizacionVendedor getCotizacionVendedorById(Integer id){
        return cotizacionVendedorDAO.getCotizacionVendedorById(id);
    }

    public CotizacionVendedor saveCotizacionVendedor(CotizacionVendedor cotizacionVendedor) throws TareasAppException{
        return cotizacionVendedorDAO.saveCotizacionVendedor(cotizacionVendedor);
    }

    public List<CotizacionVendedor> findByProducto(Producto producto){
        return cotizacionVendedorDAO.findByProducto(producto);
    }

    public CotizacionVendedor updateCotizacionVendedor(CotizacionVendedor cotizacionVendedor){
        return cotizacionVendedorDAO.updateCotizacionVendedor(cotizacionVendedor);
    }

    public void deleteCotizacionVendedor(CotizacionVendedor cotizacionVendedor){
        cotizacionVendedorDAO.deleteCotizacionVendedor(cotizacionVendedor);
    }

    public List<CotizacionVendedor> findByVendedor(Vendedor vendedor){
        return cotizacionVendedorDAO.findByVendedor(vendedor);
    }
}