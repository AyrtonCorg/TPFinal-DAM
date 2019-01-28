package com.dam.bancoMovil.modelo;

public class Usuario {
    String username;
    String password;
    String email;
    String nombre;
    String apellido;
    Long cuit;
    String direccion;

    public Usuario(String username, String password, String email, String nombre, String apellido, Long cuit, String direccion) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuit = cuit;
        this.direccion = direccion;
    }
}
