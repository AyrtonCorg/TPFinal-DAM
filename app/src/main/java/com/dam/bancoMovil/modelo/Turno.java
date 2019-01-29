package com.dam.bancoMovil.modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.dam.bancoMovil.dao.FechaConverter;
import com.dam.bancoMovil.dao.TipoTramiteConverter;


import java.util.Date;

@Entity
public class Turno {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id_turno")
    private Long id_turno;
    @Embedded(prefix = "suc_")
    private Sucursal sucursal;
    @Embedded (prefix = "usuario_")
    private Usuario usuario;
    @ColumnInfo(name = "tipoTramite")
    @TypeConverters(TipoTramiteConverter.class)
    private TipoTramite tipoTramite;
    @ColumnInfo(name = "fechayHora")
    @TypeConverters(FechaConverter.class)
    private Date fechayHora;

    public Turno(Sucursal sucursal, Usuario usuario, TipoTramite tipoTramite, Date fechayHora) {
        this.sucursal = sucursal;
        this.usuario = usuario;
        this.tipoTramite = tipoTramite;
        this.fechayHora = fechayHora;
    }
}
