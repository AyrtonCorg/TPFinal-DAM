package com.dam.bancoMovil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private List<Transferencia> transferencias = new ArrayList<>();
    private ArrayAdapter<Transferencia> adapter_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_de_cuenta);

        this.saldo = (TextView) findViewById(R.id.cantidadSaldo);
        this.ult_mov = (TextView) findViewById(R.id.textViewuUltimosMovientos);
        this.ultimosMovimientos = (ListView) findViewById(R.id.ultimosMovimientos);

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
                System.out.println("Transfe: "+MyDatabase.getInstance(getApplicationContext()).getTransferenciaDAO().getAll());

                transferencias = MyDatabase.getInstance(getApplicationContext()).getCuentaDAO().getTransferenciasDeCuenta(usuario.getCuenta().getNumeroCuenta());

                System.out.println("Transferencias: " +transferencias);
            }
        };
        Thread hiloCargarTransferencia = new Thread(runnable);
        hiloCargarTransferencia.start();

        try {
            hiloCargarTransferencia.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (transferencias != null && transferencias.size() > 0 && !transferencias.isEmpty()) {
            adapter_trans = new ArrayAdapter<Transferencia>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, transferencias);
            adapter_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ultimosMovimientos.setAdapter(adapter_trans);
            ultimosMovimientos.setItemChecked(0, true );
        } else {
            ult_mov.setText("Ultimos movimientos: No se realizo ninguna transferencia");
        }
    }
}
