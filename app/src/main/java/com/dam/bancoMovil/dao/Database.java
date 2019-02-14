package com.dam.bancoMovil.dao;

import android.arch.persistence.room.RoomDatabase;

import com.dam.bancoMovil.modelo.Cuenta;
import com.dam.bancoMovil.modelo.PlazoFijo;
import com.dam.bancoMovil.modelo.Sucursal;
import com.dam.bancoMovil.modelo.Transferencia;
import com.dam.bancoMovil.modelo.Turno;
import com.dam.bancoMovil.modelo.Usuario;

@android.arch.persistence.room.Database(entities = {Usuario.class, Cuenta.class, Sucursal.class, Turno.class,
                                                    Transferencia.class, PlazoFijo.class},version = 4,exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract UsuarioDAO usuarioDAO();
    public abstract CuentaDAO cuentaDAO();
    public abstract SucursalDAO sucursalDAO();
    public abstract TurnoDAO turnoDAO();
    public abstract TransferenciaDAO transferenciaDAO();
    public abstract PlazoFijoDAO plazoFijoDAO();
}
