package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProducto, Integer> {
    Integer countByNombre(String nombre);
    List<TipoProducto> findAllByOrderByNombre();
}
