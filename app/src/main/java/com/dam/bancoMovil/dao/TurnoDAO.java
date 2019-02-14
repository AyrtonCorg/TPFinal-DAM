package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.bancoMovil.modelo.Turno;

import java.util.List;

@Dao
public interface TurnoDAO {
    @Query("SELECT * FROM Turno")
    List<Turno> getAll();

    @Query("SELECT Turno.fechayHora FROM Turno WHERE Turno.suc_id_sucursal = :idSucursal")
    List<String> getAllFechayHoraDeTurnosTurnos(int idSucursal);

    @Insert
    void insertAll(List<Turno> turno);

    @Insert
    void insertOne(Turno turno);

    @Update
    void update(Turno turno);

    @Delete
    void delete(Turno turno);
}
