package com.dam.bancoMovil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dam.bancoMovil.CustomAdapters.AdaptadorFilaActividadUltimosMovimientos;
import com.dam.bancoMovil.CustomAdapters.AdaptadorFilaAdministradorDeServicios;
import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.PlazoFijo;
import com.dam.bancoMovil.modelo.Transferencia;
import com.dam.bancoMovil.modelo.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdministradorDeServicios extends AppCompatActivity {
    private static final int EVENTO_COMPROBACION_TERMINADA = 100;
    private static final int EVENTO_SIN_SERVICIOS = 200;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private NavigationView navView;
    private ToggleButton toggleButton;
    private Button plazoFijo;
    private Button credito;
    private Button seguro;
    private ListView listView;

    private List<PlazoFijo> plazoFijoList = new ArrayList<PlazoFijo>();
    private AdaptadorFilaAdministradorDeServicios adapter_trans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrador_de_servicios);

        this.navView = (NavigationView) findViewById(R.id.navview);
        this.toggleButton = (ToggleButton) findViewById(R.id.buttonSolicitarOVerServicios);
        this.credito = (Button) findViewById(R.id.btn_Credito);
        this.plazoFijo = (Button) findViewById(R.id.btn_plazoFijo);
        this.seguro = (Button) findViewById(R.id.btn_Seguros);
        this.listView = (ListView) findViewById(R.id.listView);

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

            this.seguro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Si el toggle está en servicios suscriptos
                    if(toggleButton.isChecked()){
                        Message completeMessage = handler.obtainMessage(EVENTO_SIN_SERVICIOS);
                        completeMessage.sendToTarget();
                    }else{
                        //Si el toggle está en solicitar servicio
                        Intent i = new Intent(getApplicationContext() , SolicitarTurno.class);
                        i.putExtra("username", usuario.getUsername());
                        startActivity(i);
                    }
                }
            });

            this.credito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Si el toggle está en servicios suscriptos
                    if(toggleButton.isChecked()){
                        Message completeMessage = handler.obtainMessage(EVENTO_SIN_SERVICIOS);
                        completeMessage.sendToTarget();
                    }else{
                        //Si el toggle está en solicitar servicio
                        Intent i = new Intent(getApplicationContext() , SolicitarTurno.class);
                        i.putExtra("username", usuario.getUsername());
                        startActivity(i);
                    }
                }
            });

            this.plazoFijo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Si el toggle está en servicios suscriptos
                    if(toggleButton.isChecked()){
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                plazoFijoList = MyDatabase.getInstance(getApplicationContext()).getPlazoFijoDAO()
                                                                        .getPlazosDeUsuario(usuario.getUsername());

                                Message completeMessage = handler.obtainMessage(EVENTO_COMPROBACION_TERMINADA);
                                completeMessage.sendToTarget();
                            }
                        };
                        Thread hiloVerificarPlazoFijo = new Thread(r);
                        hiloVerificarPlazoFijo.start();


                    }else{
                        //Si el toggle está en solicitar servicio
                        Intent i = new Intent(getApplicationContext() , SolicitarPlazoFijo.class);
                        i.putExtra("username", usuario.getUsername());
                        startActivity(i);
                    }
                }
            });

        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlazoFijo plazo = (PlazoFijo) parent.getItemAtPosition(position);
                Date fechaFinalizacion = plazo.getFechaFin();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
                String fechaString = dateFormat.format(fechaFinalizacion);
                //Armo un cuadro de dialogo para mostrar los detalles del plazo seleccionado

                builder.setTitle("Datos del plazo fijo")
                        .setMessage("ID del plazo fijo: "+ plazo.getIdPlazoFijo()
                                        +"  Monto: "+ plazo.getMonto()+""
                                        +"  Moneda: "+plazo.getMoneda()+""
                                        +"  Rendimiento: "+plazo.getRendimiento()+""
                                        +"  Fecha fin: "+fechaString);
                builder.create().show();
            }
        });

        this.navView.setNavigationItemSelectedListener(menu);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what){
                case EVENTO_COMPROBACION_TERMINADA:
                    if (plazoFijoList != null && plazoFijoList.size() > 0 && !plazoFijoList.isEmpty()) {
                        ArrayList<PlazoFijo> plazosOrdenado = new ArrayList<>();
                        for(int i = plazoFijoList.size(); i>0; i--){
                            plazosOrdenado.add(plazoFijoList.get(i-1));
                        }
                        adapter_trans = new AdaptadorFilaAdministradorDeServicios(getApplicationContext(), plazosOrdenado);
                        adapter_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        listView.setAdapter(adapter_trans);
                    }else{                                                                                    //Un array vacio
                        adapter_trans = new AdaptadorFilaAdministradorDeServicios(getApplicationContext(), new ArrayList<PlazoFijo>());
                        adapter_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        listView.setAdapter(adapter_trans);
                        Toast.makeText(getApplicationContext(), "No se encuentra suscripto a este servicio", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case EVENTO_SIN_SERVICIOS:
                    adapter_trans = new AdaptadorFilaAdministradorDeServicios(getApplicationContext(), new ArrayList<PlazoFijo>());
                    adapter_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    listView.setAdapter(adapter_trans);
                    Toast.makeText(getApplicationContext(), "No se encuentra suscripto a este servicio", Toast.LENGTH_SHORT).show();
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
                    Intent k = new Intent(getApplicationContext() , SolicitarTurno.class);
                    k.putExtra("idSucursal",-1);
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
