package com.software.tareasApp.persistence.model;

import com.software.tareasApp.utils.UtilsGeneral;

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

/**
 * Daniel Nacher
 * 2023-04-06
 */

@Entity
@Table(name = "movimiento")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "concepto")
    private String concepto;

    @Column(name = "debito")
    private Integer debito;

    @Column(name = "credito")
    private Integer credito;

    @Column(name = "saldo")
    private Integer saldo;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "cuenta_ahorro")
    private CuentaAhorro cuentaAhorro;

    public Movimiento() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date date) {
        this.fecha = date;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Integer getDebito() {
        return debito;
    }

    public void setDebito(Integer debito) {
        this.debito = debito;
    }

    public Integer getCredito() {
        return credito;
    }

    public void setCredito(Integer credito) {
        this.credito = credito;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public CuentaAhorro getCuentaAhorro() {
        return cuentaAhorro;
    }

    public void setCuentaAhorro(CuentaAhorro cuentaAhorro) {
        this.cuentaAhorro = cuentaAhorro;
    }

    public String toStringLog() {
        return "Movimiento{" +
                ", concepto=" + concepto +
                '}';
    }

    public String getFechaToString() {
        return UtilsGeneral.getFechaFormato(getFecha());
    }
}
