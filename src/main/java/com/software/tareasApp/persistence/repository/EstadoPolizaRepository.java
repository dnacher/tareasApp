package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.EstadoPoliza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoPolizaRepository extends JpaRepository<EstadoPoliza, Integer> {
    Integer countByNombre(String nombre);
}
