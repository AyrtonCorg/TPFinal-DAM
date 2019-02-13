package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.dam.bancoMovil.modelo.Cuenta;
import com.dam.bancoMovil.modelo.TipoTransferencia;
import com.dam.bancoMovil.modelo.Transferencia;

import java.util.List;

@Dao
public interface CuentaDAO {
    @Query("SELECT * FROM Cuenta")
    List<Cuenta> getAll();

    @Query("SELECT * FROM Cuenta WHERE Cuenta.nroCuenta = :nroCuenta")
    Cuenta getCuenta(Long nroCuenta);

    @Query("SELECT * FROM Transferencia WHERE Transferencia.cuentaOrigen_nroCuenta = :nroCuenta " +
                                            "OR Transferencia.cuentaDestino_nroCuenta = :nroCuenta")
    List<Transferencia> getTransferenciasDeCuenta(Long nroCuenta);

    @TypeConverters(TipoTransferenciaConverter.class)
    @Query("SELECT * FROM Transferencia WHERE Transferencia.cuentaOrigen_nroCuenta = :nroCuenta " +
            "AND Transferencia.tipoTransferencia = :tipo")
    List<Transferencia> getTransferenciasRealizadas(Long nroCuenta, TipoTransferencia tipo);

    @TypeConverters(TipoTransferenciaConverter.class)
    @Query("SELECT * FROM Transferencia WHERE Transferencia.cuentaDestino_nroCuenta = :nroCuenta " +
            "AND Transferencia.tipoTransferencia = :tipo")
    List<Transferencia> getTransferenciasRecibidas(Long nroCuenta, TipoTransferencia tipo );

    @Insert
    void insertAll(List<Cuenta> cuenta);

    @Insert
    void insertOne(Cuenta cuenta);

    @Update
    void update(Cuenta cuenta);

    @Delete
    void delete(Cuenta cuenta);
}
