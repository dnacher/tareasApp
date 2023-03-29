package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.persistence.model.Siniestro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiniestroRepository extends JpaRepository<Siniestro, Integer> {
    List<Siniestro> findByPoliza(Poliza poliza);
    List<Siniestro> findByCliente(Cliente cliente);
}
