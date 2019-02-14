package com.dam.bancoMovil;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.PlazoFijoDAO;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.PlazoFijo;
import com.dam.bancoMovil.modelo.TipoMoneda;
import com.dam.bancoMovil.modelo.Usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SolicitarPlazoFijo extends AppCompatActivity {
    private static final int EVENTO_NUEVO_PLAZO = 100;
    private RadioButton rbDolar;
    private RadioButton rbPeso;
    private EditText txtMonto;
    private SeekBar seekBar;
    private TextView lblDiasDePlazo;
    private TextView intereses;
    private TextView montoFinal;
    private ToggleButton toggle;
    private CheckBox chxTerminos;
    private Button btnHacerPlazoFijo;
    private NavigationView navigationView;
    private UsuarioDAO usuarioDAO;
    private Usuario usuario;
    private int cantDias = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitar_plazo_fijo);

        this.rbDolar = (RadioButton) findViewById(R.id.rbDolar);
        this.rbPeso = (RadioButton) findViewById(R.id.rbPeso);
        this.txtMonto = (EditText) findViewById(R.id.montoAInvertir);
        this.seekBar = (SeekBar) findViewById(R.id.seekBarDias);
        this.lblDiasDePlazo = (TextView) findViewById(R.id.lblDiasDePlazo);
        this.intereses = (TextView) findViewById(R.id.lblIntereses);
        this.montoFinal = (TextView) findViewById(R.id.lbl_MontoFinal);
        this.toggle = (ToggleButton) findViewById(R.id.toggleOption);
        this.chxTerminos = (CheckBox) findViewById(R.id.chxTerminosyCondiciones);
        this.btnHacerPlazoFijo = (Button) findViewById(R.id.btnHacerPlazoFijo);
        this.navigationView = (NavigationView) findViewById(R.id.navview);


        btnHacerPlazoFijo.setEnabled(false);
        seekBar.setMax(170);
        rbPeso.setChecked(true);

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

            //LISTENER SOBRE EL CHECHBOX DE TERMINOS Y CONDICIONES
            chxTerminos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        btnHacerPlazoFijo.setEnabled(isChecked);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Se debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
                        btnHacerPlazoFijo.setEnabled(isChecked);
                    }

                }
            });



            //COMPORTAMIENTO DEL SEEKBAR
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                Float monto = Float.valueOf(txtMonto.getText().toString());
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //Cada vez que se mueve el seekbar asigno el monto nuevamente
                    monto = Float.valueOf((txtMonto.getText().toString()));
                    //SETEO DEL LABEL DE DIAS EN PANTALLA
                    lblDiasDePlazo.setText(progress+10 + " dias de plazo");
                    //SETEO DE DIAS EN PLAZO FIJO
                    cantDias = progress+10;
                    //CALCULO DE INTERESES Y SETEO EN EL LBL
                    intereses.setText("Intereses:  $" + Double.toString(calcularRendimiento( monto, cantDias)));
                    montoFinal.setText("Monto final: $"+(monto+calcularRendimiento( monto, cantDias)));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            btnHacerPlazoFijo.setOnClickListener(hacerPlazo);
        }

        navigationView.setNavigationItemSelectedListener(menu);
    }

    View.OnClickListener hacerPlazo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final Float monto = Float.valueOf((txtMonto.getText().toString()));
            final Float rendimiento = calcularRendimiento(monto, cantDias);
            final Date fechaFin = sumarDiasAFecha(new Date(), cantDias);

            TipoMoneda tipoMoneda = TipoMoneda.ARS;

            if (rbPeso.isChecked()){
                tipoMoneda = TipoMoneda.ARS;
            }
            if (rbDolar.isChecked()){
                tipoMoneda = TipoMoneda.USD;
            }

            final Boolean renovacionAutomatica;

            if(toggle.isChecked()){
                renovacionAutomatica = true;
            }else{
                renovacionAutomatica = false;
            }


            final TipoMoneda finalTipoMoneda = tipoMoneda;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    //Verifico monto
                    if( monto > usuario.getCuenta().getSaldo()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "No se dispone de saldo suficiente", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{//Si tengo ese monto lo descuento
                        PlazoFijo plazoFijo = new PlazoFijo(monto, finalTipoMoneda,fechaFin,rendimiento,renovacionAutomatica,usuario);
                        usuario.getCuenta().setSaldo(usuario.getCuenta().getSaldo()-monto);
                        //Actualizo el usuario
                        usuarioDAO.update(usuario);
                        plazoFijo.setIdPlazoFijo(MyDatabase.getInstance(getApplicationContext()).getPlazoFijoDAO().insertOne(plazoFijo));

                        Message completeMessage = handler.obtainMessage(EVENTO_NUEVO_PLAZO);
                        completeMessage.sendToTarget();
                    }

                }
            };
            Thread hiloCrearPlazoFijo = new Thread(r);
            hiloCrearPlazoFijo.start();


        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what){
                case EVENTO_NUEVO_PLAZO:
                    Toast.makeText(getApplicationContext(), "Plazo fijo realizado con éxito", Toast.LENGTH_SHORT).show();
                    Intent h = new Intent(getApplicationContext() , AdministradorDeServicios.class);
                    h.putExtra("username", usuario.getUsername());
                    startActivity(h);
                    break;
            }
        }
    };

    public  Date sumarDiasAFecha(Date fecha, int dias){
        if (dias==0) return fecha;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    //METODO PARA RETORNAR LA TASA DE INTERES DEL PLAZO FIJO

    private Float tasa(Float monto, int dias){

        Float[] tasas = {Float.valueOf(25),
                            Float.valueOf(String.valueOf(27.5)),
                            Float.valueOf(30),
                            Float.valueOf(String.valueOf(32.3)),
                            Float.valueOf(35),
                            Float.valueOf(String.valueOf(38.5))};

        if(dias < 30 && monto <= 5000) return (tasas[0]);
        if(dias >= 30 && monto <= 5000) return (tasas[1]);
        if(dias < 30 && monto > 5000 && monto <= 99999) return (tasas[2]);
        if(dias >= 30 && monto > 5000 && monto <= 99999) return (tasas[3]);
        if(dias < 30 && monto > 99999) return (tasas[4]);
        if(dias >= 30 && monto > 99999) return (tasas[5]);

        return Float.valueOf(0);
    }

    //MÉTODO PARA EL CÁLCULO DE LOS INTERESES DEL PLAZO FIJO

    public Float calcularRendimiento(Float monto, int dias){

        Float dias1 = Float.parseFloat(String.valueOf(dias));
        Float potencia = Float.parseFloat(String.valueOf(Math.pow(1+ tasa(monto,dias)/100, dias1/360.0)));
        Float interes = monto * (potencia -1);

        return interes;
    }

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
