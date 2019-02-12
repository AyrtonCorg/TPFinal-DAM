package com.dam.bancoMovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dam.bancoMovil.dao.CuentaDAO;
import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.Cuenta;
import com.dam.bancoMovil.modelo.TipoMoneda;
import com.dam.bancoMovil.modelo.Usuario;

public class CrearCuenta extends AppCompatActivity {
    private Button btnCrearCuenta;
    private EditText usuario;
    private EditText password;
    private EditText reingresoPassword;
    private EditText email;
    private EditText nroTarjeta;
    private EditText domicilio;
    private boolean nombreValido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);
        this.btnCrearCuenta = (Button) findViewById(R.id.crear_cuenta);
        this.usuario = (EditText) findViewById(R.id.usuario_crearCuenta);
        this.password = (EditText) findViewById(R.id.password_crearCuenta);
        this.reingresoPassword = (EditText) findViewById(R.id.reingresePassword_crearCuenta);
        this.email = (EditText) findViewById(R.id.email);
        this.nroTarjeta = (EditText) findViewById(R.id.numeroTarjeta);
        this.domicilio = (EditText) findViewById(R.id.domicilio);

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = usuario.getText().toString();
                String pass = password.getText().toString();
                String re_pass = reingresoPassword.getText().toString();
                String mail = email.getText().toString();
                Long nroTarj = Long.parseLong(nroTarjeta.getText().toString());
                String domi = domicilio.getText().toString();

                switch (validarDatos(user, pass, re_pass, mail, nroTarj, domi)){
                    case 0:
                        //Creo la cuenta, el usuario y los relaciono
                        final Cuenta cuenta = new Cuenta(nroTarj, (float) 0.0, TipoMoneda.ARS);
                        final Usuario u = new Usuario(user,pass,mail,domi,cuenta);
                        cuenta.setUsuario(u);
                        //Guardo en la BD
                        Runnable r1 = new Runnable() {
                            @Override
                            public void run() {
                                UsuarioDAO usuarioDAO = MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO();
                                usuarioDAO.insertOne(u);
                            }
                        };
                        Thread hiloGuardarUsuario = new Thread(r1);
                        hiloGuardarUsuario.start();

                        Runnable r2 = new Runnable() {
                            @Override
                            public void run() {
                                CuentaDAO cuentaDAO = MyDatabase.getInstance(getApplicationContext()).getCuentaDAO();
                                cuentaDAO.insertOne(cuenta);
                            }
                        };
                        Thread hiloGuardarCuenta = new Thread(r2);
                        hiloGuardarCuenta.start();

                        //Espero que se guarden las dos cosas
                        try {
                            hiloGuardarCuenta.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            hiloGuardarUsuario.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(v.getContext() , MainActivity.class);
                        startActivity(i);

                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Hay campos vacios", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                        //Seteo los editText
                        password.setText("");
                        reingresoPassword.setText("");
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "Número de tarjeta incorrecto", Toast.LENGTH_SHORT).show();
                        //Seteo los editText
                        nroTarjeta.setText("");
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
                        //Seteo los editText
                        usuario.setText("");
                        break;
                }
            }
        });


    }

    private int validarDatos(String user, String pass, String re_pass, String mail, Long nroTarj, String domi) {
        if (user.isEmpty() || pass.isEmpty() || re_pass.isEmpty() || mail.isEmpty() || nroTarj == 0 || domi.isEmpty()) {
            //Si algún campo está vacío
            return 1;
        } else {
            if (!pass.equals(re_pass)) {
                //Si no coinciden las contraseñas
                return 2;
            } else {
                if (nroTarj.toString().length() != 16) {
                    //Número de tarjeta inválido
                    return 3;
                } else {

                    if (!nombreUsuarioUnico(user)) {
                        //Si ya existe ese usuario
                        return 4;
                    } else {
                        //Está todo OK
                        return 0;
                    }
                }
            }
        }
    }


    private boolean nombreUsuarioUnico(final String user) {
        nombreValido = false;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                UsuarioDAO usuarioDAO = MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO();
                if (usuarioDAO.getUser(user) == null){
                    nombreValido = true;
                }else{
                    nombreValido = false;
                }
            }
        };
        Thread verificarNombreUsuario = new Thread(r);
        verificarNombreUsuario.start();
        //Espero a que termine para devolver el resultado
        try {
            verificarNombreUsuario.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return nombreValido;
    }

}
