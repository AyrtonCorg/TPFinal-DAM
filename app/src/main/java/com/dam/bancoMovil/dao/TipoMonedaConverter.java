package com.dam.bancoMovil.dao;

import android.arch.persistence.room.TypeConverter;

import com.dam.bancoMovil.modelo.TipoMoneda;
import com.dam.bancoMovil.modelo.TipoTramite;

public class TipoMonedaConverter {
    @TypeConverter
    public static TipoMoneda toEstado(String status) {
        return TipoMoneda.valueOf(status);
    }
    @TypeConverter
    public static String toString(TipoMoneda status) {
        return status.toString();
    }
}
