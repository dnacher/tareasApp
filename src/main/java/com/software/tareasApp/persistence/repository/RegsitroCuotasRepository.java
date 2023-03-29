package com.software.tareasApp.persistence.repository;

import com.software.tareasApp.persistence.model.Compania;
import com.software.tareasApp.persistence.model.RegistroCuotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegsitroCuotasRepository extends JpaRepository<RegistroCuotas, Integer> {
    @Query("FROM RegistroCuotas rc " +
            "WHERE rc.poliza.cuotas>rc.numeroCuotasPagas")
    List<RegistroCuotas> getRegistrosCuotasConCuotas();

    @Query("SELECT rc " +
            "FROM RegistroCuotas  rc " +
            "WHERE rc.poliza.cuotas > rc.numeroCuotasPagas " +
            "AND rc.poliza.compania=?1 " +
            "AND YEAR(rc.poliza.comienzo)=?2")
    List<RegistroCuotas> getRegsitrosCuotasByAnioAndCompania(Compania compania, Integer anio);
}
