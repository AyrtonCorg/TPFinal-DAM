package com.dam.bancoMovil;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dam.bancoMovil.CustomAdapters.AdaptadorFilaAdministradorDeServicios;
import com.dam.bancoMovil.CustomAdapters.AdaptadorFilaAdministradorDeTurnos;
import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.PlazoFijo;
import com.dam.bancoMovil.modelo.Turno;
import com.dam.bancoMovil.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class AdministradorDeTurnos extends AppCompatActivity {
    private static final int EVENTO_LISTA_CARGADA = 100;
    private Button solicitarTurno;
    private Button verTurnos;
    private ListView listView;
    private NavigationView navigationView;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private List<Turno> turnoList = new ArrayList<>();
    private AdaptadorFilaAdministradorDeTurnos adapter_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrador_de_turnos);

        this.listView = (ListView) findViewById(R.id.listView);
        this.solicitarTurno = (Button) findViewById(R.id.btn_SolicitarTurno);
        this.verTurnos = (Button) findViewById(R.id.btn_VerTurnos);
        this.navigationView = (NavigationView) findViewById(R.id.navview);

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

        }

        verTurnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        turnoList = MyDatabase.getInstance(getApplicationContext()).getTurnoDAO()
                                .getTurnosDeUsuario(usuario.getUsername());

                        Message completeMessage = handler.obtainMessage(EVENTO_LISTA_CARGADA);
                        completeMessage.sendToTarget();
                    }
                };
                Thread hiloVerificarPlazoFijo = new Thread(r);
                hiloVerificarPlazoFijo.start();
            }
        });

        solicitarTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , SolicitarTurno.class);
                i.putExtra("idSucursal",-1);
                i.putExtra("username", usuario.getUsername());
                startActivity(i);
            }
        });

        navigationView.setNavigationItemSelectedListener(menu);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what){
                case EVENTO_LISTA_CARGADA:
                    ArrayList<Turno> turnosOrdenado = new ArrayList<>();
                    for(int i = turnoList.size(); i>0; i--){
                        turnosOrdenado.add(turnoList.get(i-1));
                    }
                    adapter_trans = new AdaptadorFilaAdministradorDeTurnos(getApplicationContext(), turnosOrdenado);
                    adapter_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    listView.setAdapter(adapter_trans);

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
