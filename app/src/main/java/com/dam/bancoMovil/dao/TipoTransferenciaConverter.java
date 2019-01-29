package com.dam.bancoMovil.dao;

import android.arch.persistence.room.TypeConverter;

import com.dam.bancoMovil.modelo.TipoTransferencia;

public class TipoTransferenciaConverter {
    @TypeConverter
    public static TipoTransferencia toEstado(String status) {
        return TipoTransferencia.valueOf(status);
    }
    @TypeConverter
    public static String toString(TipoTransferencia status) {
        return status.toString();
    }
}
