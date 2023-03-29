package com.software.tareasApp.domain.service.DTO;

import com.software.tareasApp.persistence.model.*;

public interface PolizaDTO {

    Producto getProducto();
    TipoProducto getTipoProducto();
    Integer getPremio();
    Integer getPrima();
    Cliente getCliente();
    Compania getCompania();
    Vendedor getVendedor();
    Integer getTotal();
    Moneda getMoneda();
    String getCerradoPor();
    Integer getComisionValor();
    Integer getComisionCobrada();
    EstadoPoliza getEstado();

}
