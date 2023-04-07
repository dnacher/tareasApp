package com.software.tareasApp.persistence.model;

import com.google.gson.Gson;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.view.constantes.Constantes;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Daniel Nacher
 * 2023-03-28
 */

@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "tareas")
    private String tareas;

    @Column(name = "total")
    private Integer total;

    public Tarea(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date date) {
        this.fecha = date;
    }

    public String getTareas() {
        return tareas;
    }

    public void setTareas(String tareas) {
        this.tareas = tareas;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getNombre(){
        return usuario.getNombre();
    }

    public Map<String, Integer> getTotalXTarea(){
        Map<String, Integer> finalMap = new HashMap<>();
        Map<String, Boolean> map = new Gson().fromJson(getTareas(), Map.class);
        map.forEach((k,v) -> {
            if(v){
                switch (k){
                    case Constantes.AMABLE:
                        finalMap.put(k, 20);
                        break;
                    case Constantes.CAMA:
                    case Constantes.TAZA:
                    case Constantes.ESCRITORIO:
                        finalMap.put(k, 5);
                        break;
                    case Constantes.AGUA:
                    case Constantes.ROPA:
                        finalMap.put(k, 3);
                        break;
                    case Constantes.NOCHE:
                        finalMap.put(k, 15);
                        break;
                }
            }
        });
        return finalMap;
    }

    public String getFechaToString() {
        return UtilsGeneral.getFechaFormato(getFecha());
    }

    public String toStringLog() {
        return "Tarea{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", fecha=" + fecha +
                ", tareas='" + tareas + '\'' +
                ", total=" + total +
                '}';
    }
}
