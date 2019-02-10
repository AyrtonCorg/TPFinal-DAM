package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.bancoMovil.modelo.Transferencia;

import java.util.List;

@Dao
public interface TransferenciaDAO {
    @Query("SELECT * FROM Transferencia")
    List<Transferencia> getAll();

    @Insert
    void insertAll(List<Transferencia> transferencia);

    @Insert
    void insertOne(Transferencia transferencia);

    @Update
    void update(Transferencia transferencia);

    @Delete
    void delete(Transferencia transferencia);
}
