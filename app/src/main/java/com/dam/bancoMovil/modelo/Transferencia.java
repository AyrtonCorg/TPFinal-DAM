package com.dam.bancoMovil.modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.dam.bancoMovil.dao.TipoTransferenciaConverter;

@Entity
public class Transferencia {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_Transferencia")
    private long id_transferencia;
    @ColumnInfo (name = "monto")
    private float monto;
    @Embedded(prefix = "cuentaOrigen_")
    private Cuenta cuentaOrigen;
    @Embedded(prefix = "cuentaDestino_")
    private Cuenta cuentaDestino;
    @TypeConverters(TipoTransferenciaConverter.class)
    private TipoTransferencia tipoTransferencia;

    public Transferencia(float monto, Cuenta cuentaOrigen, Cuenta cuentaDestino, TipoTransferencia tipoTransferencia) {
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.tipoTransferencia = tipoTransferencia;
    }

    public long getId_transferencia() {
        return id_transferencia;
    }

    public void setId_transferencia(long id_transferencia) {
        this.id_transferencia = id_transferencia;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public TipoTransferencia getTipoTransferencia() {
        return tipoTransferencia;
    }

    public void setTipoTransferencia(TipoTransferencia tipoTransferencia) {
        this.tipoTransferencia = tipoTransferencia;
    }
}
