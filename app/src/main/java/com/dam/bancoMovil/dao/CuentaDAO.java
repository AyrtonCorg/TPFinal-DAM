package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.bancoMovil.modelo.Cuenta;

import java.util.List;

public interface CuentaDAO {
    @Query("SELECT * FROM Cuenta")
    List<Cuenta> getAll();

    @Insert
    void insertAll(List<Cuenta> cuenta);

    @Insert
    void insertOne(Cuenta cuenta);

    @Update
    void update(Cuenta cuenta);

    @Delete
    void delete(Cuenta cuenta);
}
