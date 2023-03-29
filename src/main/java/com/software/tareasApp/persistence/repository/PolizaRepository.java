package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.Moneda;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.domain.service.DTO.PolizaDTO;
import com.software.tareasApp.persistence.model.RegistroCuotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Integer> {
    List<Poliza> findByCliente(Cliente cliente);
    List<Poliza> findAllByOrderByIdDesc();
    List<Poliza> findByPolizaMadre(Poliza poliza);

    @Query("FROM Poliza p " +
            "WHERE p.cliente= ?1 " +
            "AND " +
            "(p.estado.nombre=?2 OR p.estado.nombre=?3)")
    List<Poliza> findByClienteAndEstado_NombreOrEstado_Nombre(Cliente cliente, String estadoNuevo, String estadoRenovacion);

    @Query("FROM Poliza p WHERE p.cliente= ?1 AND p.estado.nombre=?2")
    List<Poliza> findByClienteAndEstado_Nombre(Cliente cliente, String estadoNuevo);

    @Query( "SELECT SUM(p.premio) as premio, p.producto as producto, p.tipoProducto as tipoProducto, p.compania as compania, p.cliente as cliente, v as vendedor, p.moneda as moneda " +
            "FROM Poliza p " +
            "LEFT JOIN Vendedor v   ON p.vendedor=v " +
            "WHERE p.comienzo >=?1 " +
            "AND p.comienzo <= ?2 " +
            "GROUP BY p.producto, v")
    List<PolizaDTO> getTotalPremioByFechasGroupByProductos(Date desde, Date hasta);

    @Query( "SELECT SUM(p.prima) as prima, p.producto as producto, p.tipoProducto as tipoProducto, p.compania as compania, p.cliente as cliente, v as vendedor, p.moneda as moneda " +
            "FROM Poliza p " +
            "LEFT JOIN Vendedor v   ON p.vendedor=v " +
            "WHERE p.comienzo >=?1 " +
            "AND p.comienzo <= ?2 " +
            "GROUP BY p.producto, v")
    List<PolizaDTO> getTotalPrimaByFechasGroupByProductos(Date desde, Date hasta);

    @Query("SELECT COUNT(p.tipoProducto) as total, p.tipoProducto as tipoProducto, p.estado as estado " +
            "FROM Poliza p " +
            "WHERE p.comienzo >=?1 " +
            "AND p.comienzo <= ?2 " +
            "GROUP BY p.tipoProducto, p.estado " +
            "ORDER BY p.tipoProducto.nombre asc")
    List<PolizaDTO> getVentasPorTipoProducto(Date desde, Date hasta);


    @Query("SELECT p " +
            "FROM Poliza p " +
            "WHERE p.comienzo >=?1 " +
            "AND p.comienzo <= ?2")
    List<Poliza> getPolizasByFecha(Date desde, Date hasta);

    @Query("SELECT p " +
            "FROM Poliza p " +
            "WHERE p.vencimiento >=?1 " +
            "AND p.vencimiento <= ?2")
    List<Poliza> getPolizasVencimientoByFecha(Date desde, Date hasta);

  @Query("SELECT p.tipoProducto as tipoProducto, p.compania as compania, p.moneda as moneda, "
          + "case when p.moneda= ?3 then ((SUM(p.prima))* ?4) else SUM(p.prima) end AS total "
          + "FROM Poliza p "
          + "WHERE p.comienzo >=?1 "
          + "AND p.comienzo <= ?2 "
          + "GROUP BY p.tipoProducto, p.moneda.simbolo "
          + "HAVING SUM(p.prima)>=1 "
          + "ORDER BY p.compania.nombre asc")
  List<PolizaDTO> getSUMPrimaProductos(Date desde, Date hasta, Moneda moneda, Double comision);

  @Query(
      "SELECT p.compania as compania, p.cerradoPor as cerradoPor,p.tipoProducto as tipoProducto, p.moneda as moneda, "
          + "case when p.moneda= ?3 then ((SUM(p.comisionValor))* ?4) else SUM(p.comisionValor) end AS comisionValor, "
          + "case when p.moneda= ?3 then (SUM((p.comisionValor/p.cuotas)* rc.numeroCuotasPagas)* ?4) else SUM((p.comisionValor/p.cuotas)* rc.numeroCuotasPagas) end AS comisionCobrada "
          + "FROM Poliza p "
          + "JOIN RegistroCuotas rc on rc.poliza=p "
          + "WHERE p.comienzo >=?1 "
          + "AND p.comienzo <= ?2 "
          + "GROUP BY p.cerradoPor, p.compania, p.producto "
          + "HAVING SUM(p.comisionValor)>=1 "
          + "ORDER BY p.compania.nombre ASC")
  List<PolizaDTO> getPolizasComisionesByFecha(Date desde, Date hasta, Moneda moneda, Double comision);

  @Query("SELECT rc " +
          "FROM RegistroCuotas rc " +
          "WHERE YEAR(rc.poliza.comienzoCuota)=?1")
  List<RegistroCuotas> getRegistroCuotasByFecha(int anio);

}
