package com.dam.bancoMovil;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.modelo.Cuenta;
import com.dam.bancoMovil.modelo.TipoMoneda;
import com.dam.bancoMovil.modelo.TipoTransferencia;
import com.dam.bancoMovil.modelo.Transferencia;
import com.dam.bancoMovil.modelo.Usuario;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BancoMessagingService extends FirebaseMessagingService {
    private BroadcastReceiver br;
    private IntentFilter filtro;
    private Usuario usuario;

    public BancoMessagingService() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Extraigos los datos del mensaje remoto
        Object[] remoteData = remoteMessage.getData().values().toArray();
        final String username = remoteData[0].toString();
        final Long nroCuentaO = Long.parseLong(remoteData[1].toString());
        final Float monto = Float.parseFloat(remoteData[3].toString());

        //Seteo la fecha actual-----------
        Date hoyCalendar = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        final String hoy = dateFormat.format(hoyCalendar);

        //Busco en la bd el usuario, la cuenta destino y cre la transferencia
        Runnable user = new Runnable() {
            @Override
            public void run() {
                usuario = MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO().getUser(username);
                Cuenta cuentaO = MyDatabase.getInstance(getApplicationContext()).getCuentaDAO().getCuenta(nroCuentaO);
                //Creo una transferencia
                Transferencia transferencia = new Transferencia(monto,cuentaO,usuario.getCuenta(),
                        TipoTransferencia.TRANSFERENCIA_RECIBIDA,"",hoy);
                //Guardo la transferencia
                transferencia.setId_transferencia(MyDatabase.getInstance(getApplicationContext()).
                                                    getTransferenciaDAO().insertOne(transferencia));
                //Agrego el saldo a mi cuenta
                usuario.getCuenta().setSaldo(usuario.getCuenta().getSaldo()+monto);
                //Guardo los cambios de mi usuario
                MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO().update(usuario);
            }
        };
        Thread hiloRecibrTransferencia = new Thread(user);
        hiloRecibrTransferencia.start();

        try {
            hiloRecibrTransferencia.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Mando un mensaje al BroadcastReceiver
        br = new MyReceiver();
        filtro = new IntentFilter();
        filtro.addAction(MyReceiver.RECIBI_TRANSFERENCIA);
        getApplication().getApplicationContext().registerReceiver(br, filtro);
        final Intent i = new Intent();

        i.putExtra("monto", monto);
        i.putExtra("nroCuentaOrigen", nroCuentaO);
        i.putExtra("username", usuario.getUsername());
        sendBroadcast(i);

    }
    /*@Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
