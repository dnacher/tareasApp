package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Tarea;
import com.software.tareasApp.persistence.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Daniel Nacher
 * 2023-03-28
 */

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByUsuario(Usuario usuario);
    List<Tarea> findAllByOrderByUsuarioAsc();

    @Query(   "SELECT t "
            + "FROM Tarea t "
            + "WHERE t.fecha >= ?1 "
            + "AND t.fecha <= ?2 "
            + "AND t.usuario= ?3")
    List<Tarea> getTareasByFechaAndUsuario(Date desde, Date hasta, Usuario usuario);
}
