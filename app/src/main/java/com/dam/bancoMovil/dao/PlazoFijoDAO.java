package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.bancoMovil.modelo.PlazoFijo;

import java.util.List;

@Dao
public interface PlazoFijoDAO {
    @Query("SELECT * FROM PlazoFijo")
    List<PlazoFijo> getAll();

    @Query("SELECT * FROM PlazoFijo WHERE PlazoFijo.use_username = :username")
    List<PlazoFijo> getPlazosDeUsuario(String username);

    @Insert
    void insertAll(List<PlazoFijo> plazoFijo);

    @Insert
    long insertOne(PlazoFijo plazoFijo);

    @Update
    void update(PlazoFijo plazoFijo);

    @Delete
    void delete(PlazoFijo plazoFijo);
}
