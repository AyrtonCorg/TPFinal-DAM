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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.SucursalDAO;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.Sucursal;
import com.dam.bancoMovil.modelo.TipoTramite;
import com.dam.bancoMovil.modelo.Turno;
import com.dam.bancoMovil.modelo.Usuario;

import java.util.Calendar;
import java.util.List;

public class SolicitarTurno extends AppCompatActivity {
    private static final int EVENTO_NUEVO_TURNO = 100;

    private Button buscarSucursal;
    private NavigationView navigationView;
    private Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private Sucursal sucursal;
    private SucursalDAO sucursalDAO;
    private TextView coordSucursal;
    private Spinner diaTurno;
    private Spinner mesTurno;
    private Spinner anioTurno;
    private Spinner horarioTurno;
    private RadioButton caja;
    private RadioButton atencionPersonalizada;
    private Button solicitarTurno;
    private String horarioSeleccionado;
    private String diaSeleccionado;
    private String mesSeleccionado;
    private String anioSeleccionado;
    private boolean valido = true;

    private String[] dias30 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14",
            "15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
    private String[] dias31 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14",
            "15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    private String[] dias28 = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14",
            "15","16","17","18","19","20","21","22","23","24","25","26","27","28"};
    private String[] meses = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    private String[] anios = {"2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitar_turno);

        Calendar.getInstance();

        this.buscarSucursal = (Button) findViewById(R.id.btnBuscarSucursal);
        this.navigationView = (NavigationView) findViewById(R.id.navview);
        this.coordSucursal = (TextView) findViewById(R.id.sucursal_coord);
        this.diaTurno = (Spinner) findViewById(R.id.diaTurno);
        this.mesTurno = (Spinner) findViewById(R.id.mesTurno);
        this.anioTurno = (Spinner) findViewById(R.id.anioTurno);
        this.horarioTurno = (Spinner) findViewById(R.id.spinnerHorario);
        this.caja = (RadioButton) findViewById(R.id.radioButtonCaja);
        this.atencionPersonalizada = (RadioButton) findViewById(R.id.radioButtonAtencionPersonalizada);
        this.solicitarTurno = (Button) findViewById(R.id.buttonSolicitarTurno);


        atencionPersonalizada.setChecked(true);
        caja.setChecked(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final int idSucursal = extras.getInt("idSucursal");
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

            if(idSucursal != -1){
                //Cargar sucursal
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        sucursalDAO = MyDatabase.getInstance(getApplicationContext()).getSucursalDAO();
                        sucursal = sucursalDAO.getSucursal(idSucursal);
                    }
                };
                Thread hiloCargarSucursal = new Thread(r1);
                hiloCargarSucursal.start();

                try {
                    hiloCargarSucursal.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                coordSucursal.setText(sucursal.getNombreSucursal());
            }

            //---------Cargo los spinner-------------------------------------------------------------------

            String[] hora = {"09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30"};

            switch (Calendar.MONTH){
                //1 = Febrero -> Enero = 0
                case 2:
                    ArrayAdapter<String> diasAdapter1 = new ArrayAdapter<String>(getApplicationContext(),
                                                    android.R.layout.simple_spinner_dropdown_item, dias28);
                    diaTurno.setAdapter(diasAdapter1);
                    diaTurno.setSelection(0);
                    break;
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    ArrayAdapter<String> diasAdapter2 = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, dias31);
                    diaTurno.setAdapter(diasAdapter2);
                    diaTurno.setSelection(0);
                    break;
                default:
                    ArrayAdapter<String>  diasAdapter3 = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item, dias30);
                    diaTurno.setAdapter(diasAdapter3);
                    diaTurno.setSelection(0);
                    break;
            }

            ArrayAdapter<String> mesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, meses);
            mesTurno.setAdapter(mesAdapter);
            mesTurno.setSelection(0);
            if(mesTurno.getItemAtPosition(0) != null){
                mesSeleccionado = (String) mesTurno.getItemAtPosition(Calendar.MONTH);
            }

            ArrayAdapter<String> anioAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, anios);
            anioTurno.setAdapter(anioAdapter);
            anioTurno.setSelection(0);
            if(anioTurno.getItemAtPosition(0) != null){
                anioSeleccionado = (String) anioTurno.getItemAtPosition(0);
            }

            ArrayAdapter<String> horaAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, hora);
            horarioTurno.setAdapter(horaAdapter);
            horarioTurno.setSelection(0);
            if(horarioTurno.getItemAtPosition(0) != null){
                horarioSeleccionado = (String) horarioTurno.getItemAtPosition(0);
            }
        }

        diaTurno.setOnItemSelectedListener(guardarDia);
        mesTurno.setOnItemSelectedListener(cargarDias);
        anioTurno.setOnItemSelectedListener(guardarAnio);

        buscarSucursal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext() , BuscarSucursal.class);
                j.putExtra("username",usuario.getUsername());
                startActivity(j);
            }
        });

        solicitarTurno.setOnClickListener(solTurno);

        this.navigationView.setNavigationItemSelectedListener(menu);
    }

    AdapterView.OnItemSelectedListener guardarDia =new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            diaSeleccionado = diaTurno.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener cargarDias =new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mesSeleccionado = mesTurno.getItemAtPosition(position).toString();

            switch (position+1){
                case 2:
                    ArrayAdapter<String> diasAdapter1 = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, dias28);
                    diaTurno.setAdapter(diasAdapter1);
                    diaTurno.setSelection(0);
                    break;
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    ArrayAdapter<String> diasAdapter2 = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, dias31);
                    diaTurno.setAdapter(diasAdapter2);
                    diaTurno.setSelection(0);
                    break;
                default:
                    ArrayAdapter<String>  diasAdapter3 = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, dias30);
                    diaTurno.setAdapter(diasAdapter3);
                    diaTurno.setSelection(0);
                    break;
            }
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener guardarAnio =new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            anioSeleccionado = anioTurno.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener solTurno = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TipoTramite tipoTramite = TipoTramite.ATENCION_PERSONALIZADA;

            if(caja.isChecked()){
                tipoTramite = TipoTramite.CAJA;
            }
            if(atencionPersonalizada.isChecked()){
                tipoTramite = TipoTramite.ATENCION_PERSONALIZADA;
            }

            final TipoTramite finalTipoTramite = tipoTramite;

            final String fechaTurno = (diaSeleccionado).concat("/")
                                .concat(mesSeleccionado).concat("/")
                                .concat(anioSeleccionado).concat("  ")
                                .concat(horarioSeleccionado);

            if(turnoValido(fechaTurno, tipoTramite)){
                //Creo el turno
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Turno turno = new Turno(sucursal, usuario, finalTipoTramite, fechaTurno);
                        MyDatabase.getInstance(getApplicationContext()).getTurnoDAO().insertOne(turno);

                        Message completeMessage = handler.obtainMessage(EVENTO_NUEVO_TURNO);
                        completeMessage.sendToTarget();
                    }
                };
                Thread hiloGuardarTurno = new Thread(runnable);
                hiloGuardarTurno.start();

                Intent i = new Intent(getApplicationContext() , AdministradorDeTurnos.class);
                i.putExtra("username", usuario.getUsername());
                startActivity(i);

            }else{
                Toast.makeText(getApplicationContext(),"El turno seleccionado no está disponible",Toast.LENGTH_SHORT).show();
            }

        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what){
                case EVENTO_NUEVO_TURNO:
                    Toast.makeText(getApplicationContext(), "Turno realizado con éxito", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private boolean turnoValido(final String fechaTurno, final TipoTramite tipoTramite){
        //Obtengo todos los turnos de la sucursal selecionada
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<Turno> turnos = MyDatabase.getInstance(getApplicationContext()).getTurnoDAO().getAllTurnosDeSucursal(sucursal.getId_sucursal());

                for (Turno t : turnos){
                    if (t.getFechayHora().equals(fechaTurno) && t.getTipoTramite().equals(tipoTramite)){
                        valido = false;
                    }
                }
            }
        };
        Thread hiloCargarTurnos = new Thread(r);
        hiloCargarTurnos.start();

        try {
            hiloCargarTurnos.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return valido;
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
