package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.VendedorDAO;
import com.software.tareasApp.persistence.model.Vendedor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class VendedorService {
    
    private final VendedorDAO vendedorDAO;
    
    public VendedorService(VendedorDAO vendedorDAO){
        this.vendedorDAO= vendedorDAO;
    }

    public List<Vendedor> getVendedores(){
        return vendedorDAO.getVendedores();
    }

    public Vendedor getVendedorById(Integer id){
        return vendedorDAO.getVendedorById(id);
    }

    public Vendedor saveVendedor(Vendedor vendedore){
        return vendedorDAO.saveVendedor(vendedore);
    }

    public Vendedor updateVendedor(Vendedor vendedore){
        return vendedorDAO.updateVendedor(vendedore);
    }

    public void deleteVendedor(Vendedor vendedore){
        vendedorDAO.deleteVendedor(vendedore);
    }

}
