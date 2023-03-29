package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.CotizacionVendedor;
import com.software.tareasApp.persistence.model.Producto;
import com.software.tareasApp.persistence.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionVendedorRepository extends JpaRepository<CotizacionVendedor, Integer> {
    List<CotizacionVendedor> findByProducto(Producto producto);
    List<CotizacionVendedor> findByVendedor(Vendedor vendedor);
}
