package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Banco;
import com.software.tareasApp.persistence.model.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {
    Integer countByAnioAndMesAndBanco(Integer anio, Integer mes, Banco banco);
}