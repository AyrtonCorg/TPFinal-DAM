package com.dam.bancoMovil.modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {
    @ColumnInfo(name="username")
    @PrimaryKey
    @NonNull
    private String username;
    @ColumnInfo(name= "password")
    private String password;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo (name="domicilio")
    private String domicilio;
    @Embedded(prefix = "cuenta_")
    private Cuenta cuenta;
    @Ignore
    private List<Turno> turnos;
    @Ignore
    private List<PlazoFijo> plazosFijos;

    public Usuario(String username, String password, String email, String domicilio, Cuenta cuenta) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.domicilio = domicilio;
        this.cuenta = cuenta;
        this.turnos = new ArrayList<Turno>();
        this.plazosFijos = new ArrayList<PlazoFijo>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
