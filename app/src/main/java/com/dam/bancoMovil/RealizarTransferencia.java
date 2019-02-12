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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.bancoMovil.dao.CuentaDAO;
import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.TransferenciaDAO;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.Cuenta;
import com.dam.bancoMovil.modelo.TipoTransferencia;
import com.dam.bancoMovil.modelo.Transferencia;
import com.dam.bancoMovil.modelo.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RealizarTransferencia extends AppCompatActivity {
    private static final int EVENTO_NUEVA_TRANSFERENCIA = 100;

    private TextView cuentaOrigen;
    private EditText cuentaDestino;
    private EditText montoATransferir;
    private TextView fecha;
    private EditText observaciones;
    private Button cancelar;
    private Button realizarTransferencia;
    private NavigationView navView;

    private Usuario usuarioO;
    private Usuario usuarioD;
    //private Cuenta cuentaBuscada;
    private Usuario usuarioBuscado;
    private UsuarioDAO usuarioDAO;
    private CuentaDAO cuentaDAO;
    private TransferenciaDAO transferenciaDAO;
    Boolean agregaSaldo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realizar_transferencia);

        this.navView = (NavigationView) findViewById(R.id.navview);
        this.cancelar = (Button) findViewById(R.id.cancelar);
        this.realizarTransferencia = (Button) findViewById(R.id.realizarTransferencia);
        this.cuentaDestino = (EditText) findViewById(R.id.cuentaDestino);
        this.cuentaOrigen = (TextView) findViewById(R.id.cuentaOrigen);
        this.fecha = (TextView) findViewById(R.id.fecha);
        this.montoATransferir = (EditText) findViewById(R.id.montoTransferencia);
        this.observaciones = (EditText) findViewById(R.id.observaciones);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String username = extras.getString("username");
            agregaSaldo = extras.getBoolean("agregaSaldo");

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    usuarioDAO = MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO();
                    usuarioO = usuarioDAO.getUser(username);
                }
            };
            Thread hiloCargarUsuario = new Thread(r);
            hiloCargarUsuario.start();

            try {
                hiloCargarUsuario.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.cuentaOrigen.setText(String.valueOf(usuarioO.getCuenta().getNumeroCuenta()));

            //Seteo la fecha actual-----------
            Date hoyCalendar = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
            String hoy = dateFormat.format(hoyCalendar);

            this.fecha.setText(hoy);
            //-------------------------------

            if (agregaSaldo) {
                this.cuentaDestino.setText(String.valueOf(usuarioO.getCuenta().getNumeroCuenta()));
                this.cuentaDestino.setEnabled(false);
            }
        }

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , MenuDeCuenta.class);
                i.putExtra("username", usuarioO.getUsername());
                startActivity(i);
            }
        });

        realizarTransferencia.setOnClickListener(transfer);


        navView.setNavigationItemSelectedListener(menu);
    }

    View.OnClickListener transfer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            transferenciaDAO = MyDatabase.getInstance(getApplicationContext()).getTransferenciaDAO();
            Boolean noAlcanza = false;

            final Cuenta cuentaO = usuarioO.getCuenta();
            final Cuenta cuentaD;

            if (agregaSaldo){
                usuarioD = usuarioO;
            }else{
                Long cuentaDest = Long.parseLong(cuentaDestino.getText().toString());
                //cuentaD = buscarCuenta(cuentaDest);
                usuarioD = buscarUsuarioDestino(cuentaDest);
            }

            final Float monto = Float.parseFloat(montoATransferir.getText().toString());
            final TipoTransferencia tipo = TipoTransferencia.TRANSFERENCIA_REALIZADAS;
            final String obser = observaciones.getText().toString();

            //Si no agrego saldo
            if(!agregaSaldo){
                //Verifico cuenta destino(usuario destino) que no sea null
                if(usuarioD != null){
                    //Verifico que el monto se pueda pagar
                    if(monto >= usuarioO.getCuenta().getSaldo()){
                        Toast.makeText(getApplicationContext(), "Saldo insuficiente para realizar la transferencia", Toast.LENGTH_SHORT).show();
                        montoATransferir.setText("");
                        noAlcanza = true;
                    }else{
                        noAlcanza = false;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "La cuenta destino no existe", Toast.LENGTH_SHORT).show();
                    cuentaDestino.setText("");
                    noAlcanza = true;
                }
            }

            if(!noAlcanza){
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        //Creo y guardo la transferencia
                        Transferencia transfer = new Transferencia(monto,cuentaO,usuarioD.getCuenta(),tipo,obser);
                        transfer.setId_transferencia(transferenciaDAO.insertOne(transfer));
                        /*Si agrego saldo a mi cuenta lo guardo una sola vez*/
                        //Agrego la transferencia a la cuenta -- No tengo que persistir datos asi que queda acá no más
                        cuentaO.getTransferencias().add(transfer);
                        //Agrego el saldo a la cuenta destino
                        usuarioD.getCuenta().setSaldo(usuarioD.getCuenta().getSaldo() + monto);
                        //Si no agrego saldo actualizo cuentaD
                        if (!agregaSaldo){
                            //Descuento el saldo de la cuenta origen
                            cuentaO.setSaldo(cuentaO.getSaldo()-monto);
                            usuarioO.setCuenta(cuentaO);
                            /*No hace falta esto porque ignoro la lista para guardar en la bd*/
                            //cuentaD.getTransferencias().add(transfer);
                        }else{
                            //Sino actualizo mi cuenta
                            usuarioO.setCuenta(usuarioD.getCuenta());
                        }
                        //Guardo los cambios
                        usuarioDAO.update(usuarioO);
                        usuarioDAO.update(usuarioD);
                        //cuentaDAO.update(usuarioD.getCuenta());
                        //cuentaDAO.update(cuentaO);

                        Message completeMessage = handler.obtainMessage(EVENTO_NUEVA_TRANSFERENCIA);
                        completeMessage.sendToTarget();
                    }
                };
                Thread hiloGuardarTransferencia = new Thread(r);
                hiloGuardarTransferencia.start();

                cuentaDestino.setText("");
                montoATransferir.setText("");
                observaciones.setText("");
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what){
                case EVENTO_NUEVA_TRANSFERENCIA:
                    Toast.makeText(getApplicationContext(), "Transferencia realizada con éxito", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    /*private Cuenta buscarCuenta(final Long nroCuenta){
        cuentaDAO = MyDatabase.getInstance(getApplicationContext()).getCuentaDAO();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                cuentaBuscada = cuentaDAO.getCuenta(nroCuenta);
            }
        };
        Thread hiloBuscarCuenta = new Thread(r);
        hiloBuscarCuenta.start();

        try {
            hiloBuscarCuenta.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return cuentaBuscada;
    }*/

    private Usuario buscarUsuarioDestino (final Long nroCuenta){
        usuarioDAO = MyDatabase.getInstance(getApplicationContext()).getUsuarioDAO();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                usuarioBuscado = usuarioDAO.getUsuarioDeCuenta(nroCuenta);
            }
        };
        Thread hiloBuscarCuenta = new Thread(r);
        hiloBuscarCuenta.start();

        try {
            hiloBuscarCuenta.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return usuarioBuscado;
    }

    //Manejo del Navigation View
    NavigationView.OnNavigationItemSelectedListener menu = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.agregarSaldo:
                    Intent i = new Intent(getApplicationContext() , RealizarTransferencia.class);
                    i.putExtra("username", usuarioO.getUsername());
                    i.putExtra("agregaSaldo",true);
                    startActivity(i);
                    break;
                case R.id.realizarTransferencia:
                    Intent j = new Intent(getApplicationContext() , RealizarTransferencia.class);
                    j.putExtra("username", usuarioO.getUsername());
                    j.putExtra("agregaSaldo",false);
                    startActivity(j);
                    break;
                case R.id.turnos:
                    Intent k = new Intent(getApplicationContext() , SolicitarTurno.class);
                    k.putExtra("username", usuarioO.getUsername());
                    startActivity(k);
                    break;
                case R.id.ultimosMovimientos:
                    Intent l = new Intent(getApplicationContext() , UltimosMovimientos.class);
                    l.putExtra("username", usuarioO.getUsername());
                    startActivity(l);
                    break;
                case R.id.administradorDeServicios:
                    Intent h = new Intent(getApplicationContext() , AdministradorDeServicios.class);
                    h.putExtra("username", usuarioO.getUsername());
                    startActivity(h);
                    break;
            }
            return true;
        }
    };

}
