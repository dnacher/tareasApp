package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonedaRepository extends JpaRepository<Moneda, Integer> {
    Integer countByNombre(String nombre);
}
