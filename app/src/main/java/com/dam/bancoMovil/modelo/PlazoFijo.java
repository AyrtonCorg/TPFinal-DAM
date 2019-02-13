package com.dam.bancoMovil.modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.dam.bancoMovil.dao.FechaConverter;
import com.dam.bancoMovil.dao.TipoMonedaConverter;

import java.util.Date;

@Entity
public class PlazoFijo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_plazoFijo")
    private long idPlazoFijo;
    @ColumnInfo(name = "monto")
    private float monto;
    @ColumnInfo(name="tipoMoneda")
    @TypeConverters(TipoMonedaConverter.class)
    private TipoMoneda moneda;
    @ColumnInfo(name="fechaFin")
    @TypeConverters(FechaConverter.class)
    private Date fechaFin;
    @ColumnInfo(name = "rendimiento")
    private float rendimiento;
    //Si renovacionAutomatica = false -> acreditar en cuenta
    @ColumnInfo(name= "renovacionAutomatica")
    private boolean renovacionAutomatica;
    @Embedded(prefix =  "use_")
    private Usuario usuario;

    public PlazoFijo(float monto, TipoMoneda moneda, Date fechaFin, float rendimiento, boolean renovacionAutomatica, Usuario usuario) {
        this.monto = monto;
        this.moneda = moneda;
        this.fechaFin = fechaFin;
        this.rendimiento = rendimiento;
        this.renovacionAutomatica = renovacionAutomatica;
        this.usuario = usuario;
    }

    public long getIdPlazoFijo() {
        return idPlazoFijo;
    }

    public void setIdPlazoFijo(long idPlazoFijo) {
        this.idPlazoFijo = idPlazoFijo;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public float getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(float rendimiento) {
        this.rendimiento = rendimiento;
    }

    public boolean isRenovacionAutomatica() {
        return renovacionAutomatica;
    }

    public void setRenovacionAutomatica(boolean renovacionAutomatica) {
        this.renovacionAutomatica = renovacionAutomatica;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
