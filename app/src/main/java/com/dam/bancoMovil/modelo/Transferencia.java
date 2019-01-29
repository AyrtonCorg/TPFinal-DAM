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
}
