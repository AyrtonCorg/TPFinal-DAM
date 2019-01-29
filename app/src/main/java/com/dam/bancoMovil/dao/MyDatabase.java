package com.dam.bancoMovil.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.dam.bancoMovil.modelo.PlazoFijo;
import com.dam.bancoMovil.modelo.Transferencia;

public class MyDatabase {

    // variable de clase privada que almacena una instancia unica de esta entidad
    private static MyDatabase _INSTANCIA_UNICA=null;

    // metodo static publico que retorna la unica instancia de esta clase
    // si no existe, cosa que ocurre la primera vez que se invoca
    // la crea, y si existe retorna la instancia existente.

    public static MyDatabase getInstance(Context ctx){
        if(_INSTANCIA_UNICA==null) _INSTANCIA_UNICA = new MyDatabase(ctx);
        return _INSTANCIA_UNICA;
    }

    private Database db;
    private CuentaDAO cuentaDAO;
    private PlazoFijoDAO plazoFijoDAO;
    private SucursalDAO sucursalDAO;
    private TransferenciaDAO transferenciaDAO;
    private TurnoDAO turnoDAO;
    private UsuarioDAO usuarioDAO;

    // constructor privado para poder implementar SINGLETON
    // al ser privado solo puede ser invocado dentro de esta clase
    // el único lugar donde se invoca es en la linea 16 de esta clase
    // y se invocará UNA Y SOLO UNA VEZ, cuando _INSTANCIA_UNICA sea null
    // luego ya no se invoca nunca más. Nos aseguramos de que haya una
    // sola instancia en toda la aplicacion
    private MyDatabase(Context ctx){
        db = Room.databaseBuilder(ctx,
                Database.class, "database-name")
                .fallbackToDestructiveMigration()
                .build();
        cuentaDAO = db.cuentaDAO();
        plazoFijoDAO = db.plazoFijoDAO();
        sucursalDAO = db.sucursalDAO();
        transferenciaDAO = db.transferenciaDAO();
        turnoDAO = db.turnoDAO();
        usuarioDAO = db.usuarioDAO();
    }

    public void borrarTodo(){
        this.db.clearAllTables();
    }

    public CuentaDAO getCuentaDAO() {
        return cuentaDAO;
    }

    public void setCuentaDAO(CuentaDAO cuentaDAO) {
        this.cuentaDAO = cuentaDAO;
    }

    public PlazoFijoDAO getPlazoFijoDAO() {
        return plazoFijoDAO;
    }

    public void setPlazoFijoDAO(PlazoFijoDAO plazoFijoDAO) {
        this.plazoFijoDAO = plazoFijoDAO;
    }

    public SucursalDAO getSucursalDAO() {
        return sucursalDAO;
    }

    public void setSucursalDAO(SucursalDAO sucursalDAO) {
        this.sucursalDAO = sucursalDAO;
    }

    public TransferenciaDAO getTransferenciaDAO() {
        return transferenciaDAO;
    }

    public void setTransferenciaDAO(TransferenciaDAO transferenciaDAO) {
        this.transferenciaDAO = transferenciaDAO;
    }

    public TurnoDAO getTurnoDAO() {
        return turnoDAO;
    }

    public void setTurnoDAO(TurnoDAO turnoDAO) {
        this.turnoDAO = turnoDAO;
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
}
