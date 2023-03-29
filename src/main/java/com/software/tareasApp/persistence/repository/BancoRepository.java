package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BancoRepository extends JpaRepository<Banco, Integer> {
    Integer countBancoByNombre(String nombre);
    List<Banco> findAllByOrderByNombreAsc();
}
