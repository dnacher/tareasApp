package com.software.tareasApp.persistence.model;

import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.view.constantes.Constantes;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "registro_cuotas")
public class RegistroCuotas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "poliza")
    private Poliza poliza;

    @Column(name = "numero_cuotas_pagas")
    private Integer numeroCuotasPagas;

    @Column(name = "ultima_fecha_actualizacion")
    private Date ultimaFechaActualizacion;

    @Transient
    private ComboBox pagoCuotas;

    public RegistroCuotas(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Poliza getPoliza() {
        return poliza;
    }

    public void setPoliza(Poliza poliza) {
        this.poliza = poliza;
    }

    public Integer getNumeroCuotasPagas() {
        return numeroCuotasPagas;
    }

    public void setNumeroCuotasPagas(Integer numeroCuotasPagas) {
        this.numeroCuotasPagas = numeroCuotasPagas;
    }

    public Date getUltimaFechaActualizacion() {
        return ultimaFechaActualizacion;
    }

    public String getUltimaFechaActualizacionToString() { return UtilsGeneral.getFechaFormato(ultimaFechaActualizacion); }

    public Cliente getCliente() { return poliza.getCliente(); }

    public Compania getCompania() { return poliza.getCompania(); }

    public void setUltimaFechaActualizacion(Date ultimaFechaActualizacion) {
        this.ultimaFechaActualizacion = ultimaFechaActualizacion;
    }

    public ComboBox getPagoCuotas() {
        if(pagoCuotas==null){
            pagoCuotas = new ComboBox();
            pagoCuotas.setItems(FXCollections.observableArrayList(Constantes.LISTA_CUOTAS));
            pagoCuotas.setMinWidth(100.0);
        }
        return pagoCuotas;
    }

    public void setPagoCuotas(ComboBox comboBox) {
        this.pagoCuotas = comboBox;
    }

    public String getNumeroPoliza(){
        return poliza.getNumeroPoliza();
    }

    public String getCuotas(){
        return poliza.getCuotas().toString();
    }

    public String toStringLog() {
        return "RegistroCuotas{" +
                "id=" + id +
                ", poliza=" + poliza +
                ", numeroCuotasPagas=" + numeroCuotasPagas +
                ", ultimaFechaActualizacion=" + ultimaFechaActualizacion +
                ", pagoCuotas=" + pagoCuotas +
                '}';
    }
}
