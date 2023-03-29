package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.DTO.PolizaDTO;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.Moneda;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.persistence.model.RegistroCuotas;
import com.software.tareasApp.persistence.repository.PolizaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PolizaDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final PolizaRepository repository;
    
    public PolizaDAO(PolizaRepository repository){
        this.repository = repository;
    }

    public List<Poliza> getPolizas() {
        return repository.findAllByOrderByIdDesc();
    }

    public Poliza getPolizaById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getPolizaById " + id);
        return repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The policy id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Poliza savePoliza(Poliza poliza) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "savePoliza " + poliza.toStringLog());
        return repository.save(poliza);
    }

    public List<Poliza> savePolizas(List<Poliza> policies) throws TareasAppException {
        List<Poliza> finalList = new ArrayList<>();
        repository
                .saveAll(policies)
                .forEach(
                        poliza -> {
                            finalList.add(poliza);
                        });
        return finalList;
    }

    public void deletePoliza(Poliza poliza) {
        log.info(TareaAppApplication.usuario, "deletePoliza " + poliza.toStringLog());
        repository.delete(poliza);
    }

    public Poliza updatePoliza(Poliza poliza) throws TareasAppException {
        if (poliza.getId() != null) {
            log.info(TareaAppApplication.usuario, "updatePoliza " + poliza.toStringLog());
            return repository.save(poliza);
        } else {
            String msg = String.format("Cannot update a policy without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }
    public List<Poliza> findByCliente(Cliente cliente){
        log.info(TareaAppApplication.usuario, "findByCliente " + cliente.toStringLog());
        return repository.findByCliente(cliente);
    }

    public List<Poliza> findByPolizaMadre(Poliza poliza){
        log.info(TareaAppApplication.usuario, "findByPolizaMadre " + poliza.toStringLog());
        return repository.findByPolizaMadre(poliza);
    }

    public List<Poliza> findByClienteAndAndEstado_NombreOrEstado_Nombre(Cliente cliente, String estadoNuevo, String estadoRenovacion){
        log.info(TareaAppApplication.usuario, "findByClienteAndAndEstado_NombreOrEstado_Nombre " + cliente.toStringLog());
        return repository.findByClienteAndEstado_NombreOrEstado_Nombre(cliente, estadoNuevo, estadoRenovacion);
    }

    public List<Poliza> findByClienteAndAndEstado_Nombre(Cliente cliente, String estadoNuevo){
        log.info(TareaAppApplication.usuario, "findByClienteAndAndEstado_Nombre " + cliente.toStringLog());
        return repository.findByClienteAndEstado_Nombre(cliente, estadoNuevo);
    }

    public List<PolizaDTO> getTotalPrimaByFechasGroupByProductos(Date desde, Date hasta){
        log.info(TareaAppApplication.usuario, "getTotalPrimaByFechasGroupByProductos " + desde + " " + hasta);
        return repository.getTotalPrimaByFechasGroupByProductos(desde, hasta);
    }

    public List<PolizaDTO> getTotalPremioByFechasGroupByProductos(Date desde, Date hasta){
        log.info(TareaAppApplication.usuario, "getTotalPremioByFechasGroupByProductos " + desde + " " + hasta);
        return repository.getTotalPremioByFechasGroupByProductos(desde, hasta);
    }

    public List<PolizaDTO> getVentasPorTipoProducto(Date desde, Date hasta){
        log.info(TareaAppApplication.usuario, "getCountProductos " + desde + " " + hasta);
        return repository.getVentasPorTipoProducto(desde, hasta);
    }

    public List<Poliza> getPolizasByFecha(Date desde, Date hasta){
        log.info(TareaAppApplication.usuario, "getPolizasByFecha " + desde + " " + hasta);
        return repository.getPolizasByFecha(desde, hasta);
    }

    public List<Poliza> getPolizasVencimientoByFecha(Date desde, Date hasta){
        log.info(TareaAppApplication.usuario, "getPolizasVencimientoByFecha " + desde + " " + hasta);
        return repository.getPolizasVencimientoByFecha(desde, hasta);
    }

    public List<PolizaDTO> getSUMPrimaProductos(Date desde, Date hasta, Moneda moneda, Double comision){
        log.info(TareaAppApplication.usuario, "getSUMPrimaProductos " + desde + " " + hasta);
        return repository.getSUMPrimaProductos(desde, hasta, moneda, comision);
    }

    public List<PolizaDTO> getPolizasComisionesByFecha(Date desde, Date hasta, Moneda moneda, Double tipoCambio){
        log.info(TareaAppApplication.usuario, "getPolizasComisionesByFecha " + desde + " " + hasta);
        return repository.getPolizasComisionesByFecha(desde, hasta, moneda, tipoCambio);
    }

    public List<RegistroCuotas> getRegistroCuotasByFecha(int anio){
        log.info(TareaAppApplication.usuario, "getRegistroCuotasByFecha " + anio);
        return repository.getRegistroCuotasByFecha(anio);
    }

}
