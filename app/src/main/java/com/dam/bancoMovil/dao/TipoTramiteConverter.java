package com.dam.bancoMovil.dao;

import android.arch.persistence.room.TypeConverter;

import com.dam.bancoMovil.modelo.TipoTramite;

public class TipoTramiteConverter {
    @TypeConverter
    public static TipoTramite toEstado(String status) {
        return TipoTramite.valueOf(status);
    }
    @TypeConverter
    public static String toString(TipoTramite status) {
        return status.toString();
    }
}
