package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.bancoMovil.modelo.PlazoFijo;

import java.util.List;

public interface PlazoFijoDAO {
    @Query("SELECT * FROM PlazoFijo")
    List<PlazoFijo> getAll();

    @Insert
    void insertAll(List<PlazoFijo> plazoFijo);

    @Insert
    void insertOne(PlazoFijo plazoFijo);

    @Update
    void update(PlazoFijo plazoFijo);

    @Delete
    void delete(PlazoFijo plazoFijo);
}
