package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.CotizacionVendedor;
import com.software.tareasApp.persistence.model.Producto;
import com.software.tareasApp.persistence.model.Vendedor;
import com.software.tareasApp.persistence.repository.CotizacionVendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CotizacionVendedorDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final CotizacionVendedorRepository repository;
    
    @Autowired
    public CotizacionVendedorDAO(CotizacionVendedorRepository repository){
        this.repository = repository;
    }

    public List<CotizacionVendedor> getCotizacionVendedores() {
        List<CotizacionVendedor> cotizacionVendedores = new ArrayList<>();
        this.repository.findAll().forEach(cotizacionVendedor -> cotizacionVendedores.add(cotizacionVendedor));
        log.info(TareaAppApplication.usuario, "getCotizacionVendedores");
        return cotizacionVendedores;
    }

    public CotizacionVendedor getCotizacionVendedorById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getCotizacionVendedorById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("La cotizacion con este Id no existe", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public List<CotizacionVendedor> findByVendedor(Vendedor vendedor){
        log.info(TareaAppApplication.usuario, "findByVendedor " + vendedor.toStringLog());
        return this.repository.findByVendedor(vendedor);
    }

    public List<CotizacionVendedor> findByProducto(Producto producto){
        log.info(TareaAppApplication.usuario, "findByProducto " + producto.toStringLog());
        return this.repository.findByProducto(producto);
    }

    public CotizacionVendedor saveCotizacionVendedor(CotizacionVendedor cotizacionVendedor) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "Guardar cotizacion " + cotizacionVendedor.toStringLog());
        return this.repository.save(cotizacionVendedor);
    }
    
    public List<CotizacionVendedor> saveCotizacionVendedores(List<CotizacionVendedor> cotizacionVendedores) throws TareasAppException {
        List<CotizacionVendedor> finalList = new ArrayList<>();
        this.repository
                .saveAll(cotizacionVendedores)
                .forEach(
                        cotizacionVendedor -> {
                            finalList.add(cotizacionVendedor);
                        });
        return finalList;
    }

    public void deleteCotizacionVendedor(CotizacionVendedor cotizacionVendedor) {
        log.info(TareaAppApplication.usuario, "Borrar cotizacion " + cotizacionVendedor.toStringLog());
        this.repository.delete(cotizacionVendedor);
    }

    public CotizacionVendedor updateCotizacionVendedor(CotizacionVendedor cotizacionVendedor) throws TareasAppException {
        if (cotizacionVendedor.getId() != null) {
            log.info(TareaAppApplication.usuario, "Actualizar cotizacion " + cotizacionVendedor.toStringLog());
            return this.repository.save(cotizacionVendedor);
        } else {
            String msg = String.format("No se puede actualizar cotizacion sin Id asociado");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }
}
