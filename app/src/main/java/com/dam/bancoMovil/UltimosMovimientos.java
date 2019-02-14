package com.dam.bancoMovil;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dam.bancoMovil.CustomAdapters.AdaptadorFilaActividadUltimosMovimientos;
import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.TipoTransferencia;
import com.dam.bancoMovil.modelo.Transferencia;
import com.dam.bancoMovil.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UltimosMovimientos extends AppCompatActivity {
    private static final int EVENTO_LISTA_CARGADA = 100;

    private Spinner spinnerTipoTransfer;
    private ListView ultimosMovimientos;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private NavigationView navView;
    private List<Transferencia> transferencias = new ArrayList<>();
    private AdaptadorFilaActividadUltimosMovimientos adapter_trans;
    private TipoTransferencia tipoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultimos_movimientos);

        this.spinnerTipoTransfer = (Spinner) findViewById(R.id.spinner);
        this.ultimosMovimientos = (ListView) findViewById(R.id.listView);
        this.navView = (NavigationView) findViewById(R.id.navview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String username = extras.getString("username");
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    usuarioDAO = MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO();
                    usuario = usuarioDAO.getUser(username);
                }
            };
            Thread hiloCargarUsuario = new Thread(r);
            hiloCargarUsuario.start();

            try {
                hiloCargarUsuario.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //---------Adapter lista de tipos de transferencias-------------------------------------------------------------------

            ArrayList<TipoTransferencia> tipos = new ArrayList<TipoTransferencia>();
            tipos.add(TipoTransferencia.TODOS);
            tipos.add(TipoTransferencia.TRANSFERENCIA_RECIBIDA);
            tipos.add(TipoTransferencia.TRANSFERENCIA_REALIZADAS);
            tipos.add(TipoTransferencia.DEPOSITO_AGREGAR_SALDO);
            tipos.add(TipoTransferencia.DEPOSITO_PLAZO_FIJO);

            ArrayAdapter<TipoTransferencia> tiposAdapter = new ArrayAdapter<TipoTransferencia>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tipos);

            spinnerTipoTransfer.setAdapter(tiposAdapter);
            spinnerTipoTransfer.setSelection(0);
            if(spinnerTipoTransfer.getItemAtPosition(0) != null){
                tipoSeleccionado = (TipoTransferencia) spinnerTipoTransfer.getItemAtPosition(0);
            }

            spinnerTipoTransfer.setOnItemSelectedListener(listenerDeListaTipo);

        }

        navView.setNavigationItemSelectedListener(menu);

    }

    AdapterView.OnItemSelectedListener listenerDeListaTipo = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            tipoSeleccionado = (TipoTransferencia) parent.getItemAtPosition(position);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    switch (tipoSeleccionado){
                        case TODOS:
                            transferencias = MyDatabase.getInstance(getApplicationContext()).getCuentaDAO().getTransferenciasDeCuenta(usuario.getCuenta().getNumeroCuenta());
                            break;
                        case TRANSFERENCIA_RECIBIDA:
                            transferencias = MyDatabase.getInstance(getApplicationContext()).getCuentaDAO().getTransferenciasRecibidas(usuario.getCuenta().getNumeroCuenta(),tipoSeleccionado);
                            break;
                        case DEPOSITO_PLAZO_FIJO:
                        case DEPOSITO_AGREGAR_SALDO:
                        case TRANSFERENCIA_REALIZADAS:
                            transferencias = MyDatabase.getInstance(getApplicationContext()).getCuentaDAO().getTransferenciasRealizadas(usuario.getCuenta().getNumeroCuenta(),tipoSeleccionado);
                            break;
                    }
                    Message completeMessage = handler.obtainMessage(EVENTO_LISTA_CARGADA);
                    completeMessage.sendToTarget();
                }
            };
            Thread hiloCargarTransferencia = new Thread(runnable);
            hiloCargarTransferencia.start();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what){
                case EVENTO_LISTA_CARGADA:
                    if (transferencias != null && transferencias.size() > 0 && !transferencias.isEmpty()) {
                        ArrayList<Transferencia> transferenciasOrdenada = new ArrayList<Transferencia>();
                        for(int i = transferencias.size(); i>0; i--){
                            transferenciasOrdenada.add(transferencias.get(i-1));
                        }
                        adapter_trans = new AdaptadorFilaActividadUltimosMovimientos(getApplicationContext(), transferenciasOrdenada);
                        adapter_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ultimosMovimientos.setAdapter(adapter_trans);
                    }else{                                                                                    //Un array vacio
                        adapter_trans = new AdaptadorFilaActividadUltimosMovimientos(getApplicationContext(), new ArrayList<Transferencia>());
                        adapter_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ultimosMovimientos.setAdapter(adapter_trans);
                        Toast.makeText(getApplicationContext(), "No se realizo ninguna transferencia del tipo seleccionado", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    //Manejo del Navigation View
    NavigationView.OnNavigationItemSelectedListener menu = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.agregarSaldo:
                    Intent i = new Intent(getApplicationContext() , RealizarTransferencia.class);
                    i.putExtra("username", usuario.getUsername());
                    i.putExtra("agregaSaldo",true);
                    startActivity(i);
                    break;
                case R.id.realizarTransferencia:
                    Intent j = new Intent(getApplicationContext() , RealizarTransferencia.class);
                    j.putExtra("username", usuario.getUsername());
                    j.putExtra("agregaSaldo",false);
                    startActivity(j);
                    break;
                case R.id.turnos:
                    Intent k = new Intent(getApplicationContext() , AdministradorDeTurnos.class);
                    k.putExtra("username", usuario.getUsername());
                    startActivity(k);
                    break;
                case R.id.ultimosMovimientos:
                    Intent l = new Intent(getApplicationContext() , UltimosMovimientos.class);
                    l.putExtra("username", usuario.getUsername());
                    startActivity(l);
                    break;
                case R.id.administradorDeServicios:
                    Intent h = new Intent(getApplicationContext() , AdministradorDeServicios.class);
                    h.putExtra("username", usuario.getUsername());
                    startActivity(h);
                    break;
                case R.id.cerrarSesion:
                    Intent r = new Intent(getApplicationContext() , MainActivity.class);
                    startActivity(r);
                    break;
            }
            return true;
        }
    };
}
