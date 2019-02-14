package com.dam.bancoMovil;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class BancoFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public BancoFirebaseInstanceIdService() {
    }

    public void onTokenRefresh(){
        /*Ejercicio 5 del lab 3 parte 2*/

        //Obtengo el valor del token...
        String token = FirebaseInstanceId.getInstance().getToken();
        //Para después guardarlo
        guardartoken(token);

    }

    /*@Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/

    private void guardartoken(String _token){
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registratio_id", _token);
        editor.apply();
    }

    private String leerToken(){
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("registration_id", null);
    }

}
