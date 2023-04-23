package com.software.tareasApp.persistence.model;

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
import java.io.Serializable;

@Entity
@Table(name = "permiso_usuario")
public class PermisoUsuario implements Serializable {

    public PermisoUsuario(){}

    public PermisoUsuario(TipoUsuario tipoUsuario, String pagina, Integer permiso) {
        this.tipoUsuario = tipoUsuario;
        this.pagina = pagina;
        this.permiso = permiso;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_usuario")
    private TipoUsuario tipoUsuario;

    @Column(name="pagina")
    private String pagina;

    @Column(name="permiso")
    private Integer permiso;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoUsuario getUserType() {
        return tipoUsuario;
    }

    public void setUserType(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String page) {
        this.pagina = page;
    }

    public Integer getPermiso() {
        return permiso;
    }

    public void setPermiso(Integer permission) {
        this.permiso = permission;
    }

    public String toStringLog() {
        return "PermisoUsuario{" +
                "id=" + id +
                ", tipoUsuario=" + tipoUsuario +
                ", pagina='" + pagina + '\'' +
                ", permiso=" + permiso +
                '}';
    }
}
