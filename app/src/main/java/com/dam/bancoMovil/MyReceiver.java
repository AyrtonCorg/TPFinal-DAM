package com.dam.bancoMovil;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.Usuario;

public class MyReceiver extends BroadcastReceiver {

    public static final String OLVIDE_CONTRASENIA = "com.dam.bancoMovil.EVENTO_01_MSG";
    public static final String RECIBI_TRANSFERENCIA = "com.dam.bancoMovil.EVENTO_02_MSG";


    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        final String username = extras.getString("username");
        final Float monto = extras.getFloat("monto");
        final Long nroCuentaO = extras.getLong("nroCuentaOrigen");

        /*if (intent.getAction().equals(OLVIDE_CONTRASENIA)) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    UsuarioDAO usuarioDAO = MyDatabase.getInstance(context).getUsuarioDAO();
                    Usuario usuario = usuarioDAO.getUser(username);

                    Intent i = new Intent(context, IniciarSesion.class);
                    PendingIntent penInt = PendingIntent.getActivity(context, 0, i, 0);
                    //Creo la notificación
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CANAL01")
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle("Se envio su contraseña")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .addLine("Su usuario es "+ usuario.getUsername())
                                    .addLine("Su contraseña es " + usuario.getPassword()))
                            .setContentIntent(penInt)
                            .setAutoCancel(true);

                    //Muestro la notificación
                    NotificationManagerCompat notManager = NotificationManagerCompat.from(context);
                    notManager.notify(99, mBuilder.build());
                }
            };
            Thread hiloMostrarNotificacion = new Thread(r);
            hiloMostrarNotificacion.start();
        }*/

        switch (intent.getAction()){
            case OLVIDE_CONTRASENIA:
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        UsuarioDAO usuarioDAO = MyDatabase.getInstance(context).getUsuarioDAO();
                        Usuario usuario = usuarioDAO.getUser(username);

                        Intent i = new Intent(context, IniciarSesion.class);
                        PendingIntent penInt = PendingIntent.getActivity(context, 0, i, 0);
                        //Creo la notificación
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CANAL01")
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle("Se envio su contraseña")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setStyle(new NotificationCompat.InboxStyle()
                                        .addLine("Su usuario es "+ usuario.getUsername())
                                        .addLine("Su contraseña es " + usuario.getPassword()))
                                .setContentIntent(penInt)
                                .setAutoCancel(true);

                        //Muestro la notificación
                        NotificationManagerCompat notManager = NotificationManagerCompat.from(context);
                        notManager.notify(99, mBuilder.build());
                    }
                };
                Thread hiloMostrarNotificacion = new Thread(r);
                hiloMostrarNotificacion.start();
                break;

            case RECIBI_TRANSFERENCIA:
                Runnable trans = new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, UltimosMovimientos.class);
                        i.putExtra("username", username);
                        PendingIntent penInt = PendingIntent.getActivity(context, 0, i, 0);
                        //Creo la notificación
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CANAL01")
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle("Tansferencia recibida")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setStyle(new NotificationCompat.InboxStyle()
                                        .addLine("Transferencia de: "+ nroCuentaO)
                                        .addLine("Monto:  " + monto))
                                .setContentIntent(penInt)
                                .setAutoCancel(true);

                        //Muestro la notificación
                        NotificationManagerCompat notManager = NotificationManagerCompat.from(context);
                        notManager.notify(99, mBuilder.build());
                    }
                };
                Thread hiloMostrarTransferencia = new Thread(trans);
                hiloMostrarTransferencia.start();
        }

    }
}
