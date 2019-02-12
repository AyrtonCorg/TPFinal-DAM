package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dam.bancoMovil.modelo.Usuario;

import java.util.List;

@Dao
public interface UsuarioDAO {
    @Query("SELECT * FROM Usuario")
    List<Usuario> getAll();

    @Query("SELECT * FROM Usuario WHERE Usuario.username = :user")
    Usuario getUser(String user);

    @Query("SELECT * FROM Usuario WHERE Usuario.cuenta_nroCuenta = :nroCuenta")
    Usuario getUsuarioDeCuenta(Long nroCuenta);

    @Insert
    void insertAll(List<Usuario> usuario);

    @Insert
    void insertOne(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Delete
    void delete(Usuario usuario);
}
