package com.dam.bancoMovil;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnIniciarSesion;
    private Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Creo el canal de notificaciones
        createNotificationChannel();

        btnCrearCuenta = (Button) findViewById(R.id.btnCrearCuenta);
        btnIniciarSesion = (Button) findViewById(R.id.btn_iniciarSesion);

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext() , CrearCuenta.class);
                startActivity(i);
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext() , IniciarSesion.class);
                startActivity(i);
            }
        });
    }

    //Creación del canal de notificaciones
    private void createNotificationChannel() {
        // Crear el canal de notificaciones pero solo para API 26 io superior
        // dado que NotificationChannel es una clase nueva que no está incluida
        // en las librerías de soporte que brindan compatibilidad hacía atrás
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.canal_estado_nombre);
            String description = getString(R.string.canal_estado_descr);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CANAL01", name, importance);
            channel.setDescription(description);
            // Registrar el canal en el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
