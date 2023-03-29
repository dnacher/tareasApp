package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Tarea;
import com.software.tareasApp.persistence.model.Usuario;
import com.software.tareasApp.persistence.repository.TareaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Daniel Nacher
 * 2023-03-28
 */

@Component
public class TareaDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final TareaRepository repository;

    public TareaDAO(TareaRepository repository){
        this.repository = repository;
    }

    public List<Tarea> getTareas() {
        List<Tarea> tareas = new ArrayList<>();
        this.repository.findAllByOrderByUsuarioAsc().forEach(tarea -> tareas.add(tarea));
        log.info(TareaAppApplication.usuario, "getTareas");
        return tareas;
    }

    public Tarea getTareasById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getTareaById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The crash id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public List<Tarea> findByUsuario(Usuario usuario){
        List<Tarea> tareas = new ArrayList<>();
        log.info(TareaAppApplication.usuario, "getTareaByUsuario " + usuario.getNombre());
        this.repository
                .findByUsuario(usuario)
                .forEach(tarea -> tareas.add(tarea));
        return tareas;
    }

    public Tarea saveTarea(Tarea tarea) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveTarea " + tarea.toStringLog());
        return this.repository.save(tarea);
    }


    public void deleteTarea(Tarea tarea) {
        log.info(TareaAppApplication.usuario, "deleteTareas " + tarea.toStringLog());
        this.repository.delete(tarea);
    }

    public Tarea updateTarea(Tarea tarea) throws TareasAppException {
        if (tarea.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateTareas " + tarea.toStringLog());
            return this.repository.save(tarea);
        } else {
            String msg = String.format("Cannot update a crash without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public List<Tarea> getTareasByFechaAndUsuario(Date desde, Date hasta, Usuario usuario){
        log.info(TareaAppApplication.usuario, "getTareasByFechaAndUsuario  desde:" + desde + " hasta:" + hasta + " usuario: " + usuario.getNombre());
        return this.repository.getTareasByFechaAndUsuario(desde, hasta, usuario);
    }

}
