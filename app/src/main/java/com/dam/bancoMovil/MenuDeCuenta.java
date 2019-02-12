package com.dam.bancoMovil;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.Transferencia;
import com.dam.bancoMovil.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MenuDeCuenta extends AppCompatActivity {
    private TextView saldo;
    private TextView ult_mov;
    private ListView ultimosMovimientos;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private NavigationView navView;

    private List<Transferencia> transferencias = new ArrayList<>();
    private ArrayAdapter<Transferencia> adapter_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_de_cuenta);

        this.saldo = (TextView) findViewById(R.id.cantidadSaldo);
        this.ult_mov = (TextView) findViewById(R.id.textViewuUltimosMovientos);
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
            saldo.setText(String.valueOf(usuario.getCuenta().getSaldo()));
        }else{
            saldo.setText("0.0");
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                transferencias = MyDatabase.getInstance(getApplicationContext()).getCuentaDAO().getTransferenciasDeCuenta(usuario.getCuenta().getNumeroCuenta());
            }
        };
        Thread hiloCargarTransferencia = new Thread(runnable);
        hiloCargarTransferencia.start();

        try {
            hiloCargarTransferencia.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Saldo: "+usuario.getCuenta().getSaldo());
        for(Transferencia t : transferencias){
            System.out.println(t.getCuentaOrigen());
            System.out.println(t.getCuentaDestino());
            System.out.println(t.getMonto());
        }

        if (transferencias != null && transferencias.size() > 0 && !transferencias.isEmpty()) {
            adapter_trans = new ArrayAdapter<Transferencia>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, transferencias);
            adapter_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ultimosMovimientos.setAdapter(adapter_trans);
        } else {
            ult_mov.setText("Ultimos movimientos: No se realizo ninguna transferencia");
        }

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.agregarSaldo:
                                Intent i = new Intent(getApplicationContext() , RealizarTransferencia.class);
                                i.putExtra("username",usuario.getUsername());
                                i.putExtra("agregaSaldo",true);
                                startActivity(i);
                                break;
                            case R.id.realizarTransferencia:
                                Intent j = new Intent(getApplicationContext() , RealizarTransferencia.class);
                                j.putExtra("username",usuario.getUsername());
                                j.putExtra("agregaSaldo",false);
                                startActivity(j);
                                break;
                            case R.id.turnos:
                                Intent k = new Intent(getApplicationContext() , SolicitarTurno.class);
                                k.putExtra("username",usuario.getUsername());
                                startActivity(k);
                                break;
                            case R.id.ultimosMovimientos:
                                Intent l = new Intent(getApplicationContext() , UltimosMovimientos.class);
                                l.putExtra("username",usuario.getUsername());
                                startActivity(l);
                                break;
                            case R.id.administradorDeServicios:
                                Intent h = new Intent(getApplicationContext() , AdministradorDeServicios.class);
                                h.putExtra("username",usuario.getUsername());
                                startActivity(h);
                                break;
                        }
                        return true;
                    }
        });
    }
}
