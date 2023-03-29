package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Banco;
import com.software.tareasApp.persistence.repository.BancoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BancoDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final BancoRepository repository;
    
    public BancoDAO(BancoRepository repository){
        this.repository = repository;
    }

    public List<Banco> getBancos() {
        return repository.findAllByOrderByNombreAsc();
    }

    public Banco getBancoById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getBancoById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("Este banco no existe", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Banco saveBanco(Banco banco) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "Banco Guardado " + banco.toStringLog());
        return this.repository.save(banco);
    }

    public List<Banco> saveBancos(List<Banco> policies) throws TareasAppException {
        List<Banco> finalList = new ArrayList<>();
        this.repository
                .saveAll(policies)
                .forEach(
                        productType -> {
                            finalList.add(productType);
                        });
        return finalList;
    }

    public void deleteBanco(Banco banco) {
        log.info(TareaAppApplication.usuario, "banco borrado " + banco.toStringLog());
        this.repository.delete(banco);
    }

    public Banco updateBanco(Banco banco) throws TareasAppException {
        if (banco.getId() != null) {
            log.info(TareaAppApplication.usuario, "banco actualizado " + banco.toStringLog());
            return this.repository.save(banco);
        } else {
            String msg = String.format("No se puede actualizar banco sin id asocicado");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public Integer countBancoByNombre(String nombre){
        return this.repository.countBancoByNombre(nombre);
    }
}
