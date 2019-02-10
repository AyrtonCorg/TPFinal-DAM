package com.dam.bancoMovil.modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.dam.bancoMovil.dao.TipoMonedaConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cuenta {
    @PrimaryKey
    @ColumnInfo(name= "nroCuenta")
    private Long numeroCuenta;
    @ColumnInfo(name ="saldo")
    private Float saldo;
    @ColumnInfo(name = "tipoMoneda")
    @TypeConverters(TipoMonedaConverter.class)
    private TipoMoneda tipoMoneda;
    @Ignore
    private Usuario usuario;
    @Ignore
    List<Transferencia> transferencias;

    @Ignore
    public Cuenta(Long numeroCuenta, Float saldo, Usuario usuario, TipoMoneda tipoMoneda) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.usuario = usuario;
        this.transferencias = new ArrayList<Transferencia>();
        this.tipoMoneda = tipoMoneda;
    }

    public Cuenta(Long numeroCuenta, Float saldo, TipoMoneda tipoMoneda) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        //this.usuario = usuario;
        this.transferencias = new ArrayList<Transferencia>();
        this.tipoMoneda = tipoMoneda;
    }

    public Long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Transferencia> getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(List<Transferencia> transferencias) {
        this.transferencias = transferencias;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
}
