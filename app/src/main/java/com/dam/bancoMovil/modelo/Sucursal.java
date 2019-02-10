package com.dam.bancoMovil.modelo;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Sucursal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name ="id_sucursal")
    private int id_sucursal;
    @ColumnInfo(name = "nombre_sucursal")
    private String nombreSucursal;
    //Dirección de la sucursal
    @ColumnInfo(name="latitud")
    private double latitud;
    @ColumnInfo(name= "longitud")
    private double longitud;
    @Ignore
    private List<Turno> turnos;

    public Sucursal(int id_sucursal, String nombreSucursal, double latitud, double longitud) {
        this.id_sucursal = id_sucursal;
        this.nombreSucursal = nombreSucursal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.turnos = new ArrayList<Turno>();
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }
}
