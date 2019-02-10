package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.bancoMovil.modelo.Sucursal;
import java.util.List;

@Dao
public interface SucursalDAO {
    @Query("SELECT * FROM Sucursal")
    List<Sucursal> getAll();

    @Insert
    void insertAll(List<Sucursal> sucursal);

    @Insert
    void insertOne(Sucursal sucursal);

    @Update
    void update(Sucursal sucursal);

    @Delete
    void delete(Sucursal sucursal);
}
