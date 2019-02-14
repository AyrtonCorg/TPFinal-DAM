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
    private String fechayHora;

    public Turno(Sucursal sucursal, Usuario usuario, TipoTramite tipoTramite, String fechayHora) {
        this.sucursal = sucursal;
        this.usuario = usuario;
        this.tipoTramite = tipoTramite;
        this.fechayHora = fechayHora;
    }

    public Long getId_turno() {
        return id_turno;
    }

    public void setId_turno(Long id_turno) {
        this.id_turno = id_turno;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getFechayHora() {
        return fechayHora;
    }

    public void setFechayHora(String fechayHora) {
        this.fechayHora = fechayHora;
    }
}
