package com.software.tareasApp.domain.service;

import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.dao.CuotasPolizaDAO;
import com.software.tareasApp.persistence.model.CuotasPoliza;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CuotasPolizaService {
    
    private final CuotasPolizaDAO cuotasPolizaDAO;
    
    public CuotasPolizaService(CuotasPolizaDAO cuotasPolizaDAO){
        this.cuotasPolizaDAO = cuotasPolizaDAO;
    }

    public List<CuotasPoliza> getCuotasPolizas(){
        return cuotasPolizaDAO.getCuotasPolizas();
    }

    public CuotasPoliza getCuotasPolizaById(Integer id){
        return cuotasPolizaDAO.getCuotasPolizaById(id);
    }

    public CuotasPoliza saveCuotasPoliza(CuotasPoliza cuotasPoliza) throws TareasAppException{
        return cuotasPolizaDAO.saveCuotasPoliza(cuotasPoliza);
    }

    public CuotasPoliza updateCuotasPoliza(CuotasPoliza cuotasPoliza){
        return cuotasPolizaDAO.updateCuotasPoliza(cuotasPoliza);
    }

    public void deleteCuotasPoliza(CuotasPoliza cuotasPoliza){
        cuotasPolizaDAO.deleteCuotasPoliza(cuotasPoliza);
    }

}
