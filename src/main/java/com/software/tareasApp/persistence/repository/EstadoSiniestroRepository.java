package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.EstadoSiniestro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoSiniestroRepository extends JpaRepository<EstadoSiniestro, Integer> {
    Integer countByNombre(String nombre);
}
