package com.software.tareasApp.domain.service;

import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.DTO.PolizaDTO;
import com.software.tareasApp.persistence.dao.PolizaDAO;
import com.software.tareasApp.persistence.dao.RegsitroCuotasDAO;
import com.software.tareasApp.persistence.dao.TipoProductoClienteDAO;
import com.software.tareasApp.persistence.model.*;
import com.software.tareasApp.utils.UtilsGeneral;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PolizaService {
    
    private final PolizaDAO polizaDAO;
    private final TipoProductoClienteDAO tipoProductoClienteDAO;
    private final RegsitroCuotasDAO registroCuotasDAO;

    public PolizaService(PolizaDAO polizaDAO, TipoProductoClienteDAO tipoProductoClienteDAO, RegsitroCuotasDAO registroCuotasDAO){
        this.polizaDAO = polizaDAO;
        this.tipoProductoClienteDAO = tipoProductoClienteDAO;
        this.registroCuotasDAO = registroCuotasDAO;
    }

    public List<Poliza> getPolizas(){
        return polizaDAO.getPolizas().stream()
                .map(poliza -> procesar(poliza))
                .collect(Collectors.toList());
    }

    private Poliza procesar(Poliza poliza){
        double comisionPorcentaje;
        if (poliza.getVendedor()!=null) {
            double comisionVendedorPorcentaje;
            comisionVendedorPorcentaje = valorVendedor(poliza);
            poliza.setComisionVendedorPorcentaje(comisionVendedorPorcentaje);
            poliza.setComisionVendedorValor( calculaPorcentaje (comisionVendedorPorcentaje,poliza.getPremio()));
            comisionPorcentaje = valorProducto(poliza) - valorVendedor(poliza);
            poliza.setComisionPorcentaje(comisionPorcentaje);
            if(poliza.getPrima()!=null){
                poliza.setComisionValor(calculaPorcentaje(comisionPorcentaje,poliza.getPrima()));
            }
        }else{
            comisionPorcentaje = valorProducto(poliza);
            poliza.setComisionPorcentaje(comisionPorcentaje);
            if(poliza.getPrima()!=null){
                poliza.setComisionValor(calculaPorcentaje(comisionPorcentaje,poliza.getPrima()));
            }
        }
        return poliza;
    }

    private double calculaPorcentaje(double porcentaje, double valor){
        double d = (porcentaje/100*(valor));
        return UtilsGeneral.round(d, 2);
    }

    private double valorProducto(Poliza poliza){
        if(poliza.getEstado().getNombre().equals("Nuevo")){
            if(poliza.getProducto()!=null){
                return poliza.getProducto().getComisionNueva();
            }else{
                return 1;
            }
        }else{
            if(poliza.getProducto()!=null){
                return poliza.getProducto().getComisionRenovacion();
            }else{
                return 1;
            }
        }
    }

    private double valorVendedor(Poliza poliza){
        if (poliza.getEstado().getNombre().equals("Nuevo")) {
            return poliza.getVendedor().getComisionNueva();
        }else{
            return poliza.getVendedor().getComisionRenovacion();
        }
    }

    public Poliza getPolicyById(Integer id){
        return polizaDAO.getPolizaById(id);
    }

    public Poliza savePolicy(Poliza poliza){
        boolean update = poliza.getId()!=null;
        poliza = polizaDAO.savePoliza(poliza);
        saveTipoProductoCliente(poliza);
        if(!update){
            RegistroCuotas registroCuotas = new RegistroCuotas();
            registroCuotas.setNumeroCuotasPagas(0);
            registroCuotas.setPoliza(poliza);
            registroCuotasDAO.saveRegistroCuotas(registroCuotas);
        }
        return poliza;
    }

    private void saveTipoProductoCliente(Poliza poliza){
        TipoProductoCliente tpc = new TipoProductoCliente();
        tpc.setNombre(poliza.getTipoProducto().getNombre() + "-" + poliza.getProducto().getNombre());
        tpc.setCliente(poliza.getCliente());
        tpc.setPoliza(poliza);
        tipoProductoClienteDAO.saveTipoProductoCliente(tpc);
    }

    public Poliza updatePolicy(Poliza poliza){
        return polizaDAO.updatePoliza(poliza);
    }

    public void deletePolicy(Poliza poliza){
        polizaDAO.deletePoliza(poliza);
    }

    public List<Poliza> findByCliente(Cliente cliente){
        return polizaDAO.findByCliente(cliente);
    }

    public List<Poliza> findByPolizaMadre(Poliza poliza){
        return polizaDAO.findByPolizaMadre(poliza);
    }

    public List<Poliza> findByClienteNuevoYRenovacion(Cliente cliente){
        return polizaDAO.findByClienteAndAndEstado_NombreOrEstado_Nombre(cliente, "Nuevo", "Renovacion");
    }

    public List<Poliza> findByClienteAndEstadoEndoso(Cliente cliente){
        return polizaDAO.findByClienteAndAndEstado_Nombre(cliente,"Endoso" );
    }

    public List<PolizaDTO> getTotalPremioByFechasGroupByProductos(Date desde, Date hasta){
        return polizaDAO.getTotalPremioByFechasGroupByProductos(desde, hasta);
    }

    public List<PolizaDTO> getTotalPrimaByFechasGroupByProductos(Date desde, Date hasta){
        return polizaDAO.getTotalPrimaByFechasGroupByProductos(desde, hasta);
    }

    public List<PolizaDTO> getVentasPorTipoProducto(Date desde, Date hasta){
        return polizaDAO.getVentasPorTipoProducto(desde, hasta);
    }

    public List<Poliza> getPolizasByFecha(Date desde, Date hasta){
        return polizaDAO.getPolizasByFecha(desde, hasta);
    }

    public List<Poliza> getPolizasVencimientoByFecha(Date desde, Date hasta){
        return polizaDAO.getPolizasVencimientoByFecha(desde, hasta);
    }

    public List<PolizaDTO> getSUMPrimaProductos(Date desde, Date hasta, Moneda moneda, Double comision){
        return polizaDAO.getSUMPrimaProductos(desde, hasta, moneda, comision);
    }

    public List<PolizaDTO> getPolizasComisionesByFecha(Date desde, Date hasta, Moneda moneda, Double tipoCambio){
        return polizaDAO.getPolizasComisionesByFecha(desde, hasta, moneda, tipoCambio);
    }

    public List<RegistroCuotas> getRegistroCuotasByFecha(int anio){
        return polizaDAO.getRegistroCuotasByFecha(anio);
    }
}
