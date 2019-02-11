package com.dam.bancoMovil;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.Usuario;

public class IniciarSesion extends AppCompatActivity {
    private EditText usuario;
    private EditText password;
    private Button iniciarSesion;
    private Button olvideContrasenia;

    private boolean existeUser;
    private boolean inicioSesionValido;
    private BroadcastReceiver br;
    private IntentFilter filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciar_sesion);

        this.usuario = (EditText) findViewById(R.id.usuario);
        this.password = (EditText) findViewById(R.id.password);
        this.olvideContrasenia = (Button) findViewById(R.id.olvidasteContrasenia);
        this.iniciarSesion = (Button) findViewById(R.id.btn_iniciarSesion);

        br = new ContraseniaReceiver();
        filtro = new IntentFilter();
        filtro.addAction(ContraseniaReceiver.OLVIDE_CONTRASENIA);
        getApplication().getApplicationContext().registerReceiver(br,filtro);
        final Intent i = new Intent();

        olvideContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si ingreso el usuario
                if (usuario.getText().toString().length()!= 0){
                    String user = usuario.getText().toString();
                    //Verifico que exista el usuario
                    if (existeUsuario(user)){
                        //Me comunico con el broadcast receiver
                        i.putExtra("username",user);
                        i.setAction(ContraseniaReceiver.OLVIDE_CONTRASENIA);
                        sendBroadcast(i);
                    }else{
                        //Muestro mensaje de error
                        Toast.makeText(getApplicationContext(),"El usuario no existe",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //Pido que ingrese el usuario
                    Toast.makeText(getApplicationContext(),"Ingrese su usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifico campos no nulos
                if (usuario.getText().toString().length()!= 0 && password.getText().toString().length() != 0){
                    String user = usuario.getText().toString();
                    String pass = password.getText().toString();
                    //Verifico existencia
                    if (validoDatos(user,pass)){
                        Intent i = new Intent(v.getContext() , MenuDeCuenta.class);
                        i.putExtra("username",user);
                        startActivity(i);
                    }else{
                        //Muestro mensaje de error
                        Toast.makeText(getApplicationContext(),"El usuario y/o contraseña incorrectas",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //Pido que ingrese el usuario
                    Toast.makeText(getApplicationContext(),"Ingrese su usuario y/o contraseña",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validoDatos(final String nombreUser, final String pass) {
        inicioSesionValido = false;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                UsuarioDAO usuarioDAO = MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO();
                //Si existe el usuario
                if(existeUsuario(nombreUser)){
                    //Compruebo contraseña
                    Usuario usuario = usuarioDAO.getUser(nombreUser);
                    if (usuario.getPassword().equals(pass)){
                        inicioSesionValido = true;
                    }
                }
            }
        };
        Thread validarTupla = new Thread(r);
        validarTupla.start();

        try {
            validarTupla.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return  inicioSesionValido;

    }

    private boolean existeUsuario(final String user) {
        existeUser = false;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                UsuarioDAO usuarioDAO = MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO();
                if(usuarioDAO.getUser(user) != null){
                    existeUser = true;
                }else{
                    existeUser = false;
                }
            }
        };
        Thread buscarUsuario = new Thread(r);
        buscarUsuario.start();

        //Espero que termine el hilo
        try {
            buscarUsuario.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return existeUser;
    }
}
