package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Compania;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompaniaRepository extends JpaRepository<Compania, Integer> {
    List<Compania> findAllByOrderByNombreAsc();
    Integer countByNombre(String nombre);
}
