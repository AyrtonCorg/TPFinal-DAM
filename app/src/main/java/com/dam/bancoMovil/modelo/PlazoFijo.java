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
}
