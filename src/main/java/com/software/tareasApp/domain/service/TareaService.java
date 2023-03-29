package com.software.tareasApp.domain.service;

import com.software.tareasApp.persistence.dao.TareaDAO;
import com.software.tareasApp.persistence.model.Tarea;
import com.software.tareasApp.persistence.model.Usuario;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TareaService {
    
    private final TareaDAO tareaDAO;
    
    public TareaService(TareaDAO tareaDAO){
        this.tareaDAO = tareaDAO;
    }

    public List<Tarea> getTareas(){
        return tareaDAO.getTareas();
    }

    public List<Tarea> findByUsuario(Usuario usuario){
        return tareaDAO.findByUsuario(usuario);
    }

    public Tarea saveTarea(Tarea tarea){
        return tareaDAO.saveTarea(tarea);
    }

    public Tarea updateTarea(Tarea tarea){
        return tareaDAO.updateTarea(tarea);
    }

    public void deleteTarea(Tarea tarea){
        tareaDAO.deleteTarea(tarea);
    }

    public List<Tarea> getTareasByFechaAndUsuario(Date desde, Date hasta, Usuario usuario){
        return tareaDAO.getTareasByFechaAndUsuario(desde, hasta, usuario);
    }
}
