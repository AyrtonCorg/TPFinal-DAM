package com.dam.bancoMovil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dam.bancoMovil.dao.MyDatabase;
import com.dam.bancoMovil.dao.SucursalDAO;
import com.dam.bancoMovil.dao.UsuarioDAO;
import com.dam.bancoMovil.modelo.Sucursal;
import com.dam.bancoMovil.modelo.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.ArrayList;
import java.util.List;

public class BuscarSucursal extends FragmentActivity implements OnMapReadyCallback {
    private static final int EVENTO_UPDATE_LISTA = 100;
    private UsuarioDAO usuarioDAO;
    private Usuario usuario;
    private Sucursal sucursal;

    private GoogleMap miMapa;
    private List<Sucursal> sucursales = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_sucursal_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        miMapa = googleMap;
        miMapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Limpio el mapa cada vez que se inicia
        miMapa.clear();

        actualizarMapa();
        cargarMapaConSucursales();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final Long idSucursal = extras.getLong("idSucursal");
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

        miMapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Busco la sucursal seleccionada
                for(Sucursal s:sucursales){
                    if(s.getLatitud() == latLng.latitude && s.getLongitud() == latLng.longitude){
                        sucursal = s;
                    }
                }

                Intent i = new Intent(getApplicationContext(),SolicitarTurno.class);
                i.putExtra("idSucursal", sucursal.getId_sucursal());
                i.putExtra("username", usuario.getUsername());
                startActivity(i);
            }
        });
    }

    private void actualizarMapa() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},9999);
            return;
        }
        miMapa.setMyLocationEnabled(true);
        miMapa.getUiSettings().setZoomControlsEnabled(true);
    }

    private void cargarMapaConSucursales(){

        Runnable r = new Runnable() {
            @Override
            public void run() {
                sucursales.clear();
                sucursales = MyDatabase.getInstance(getApplicationContext()).getSucursalDAO().getAll();
                Message completeMessage = handler.obtainMessage(EVENTO_UPDATE_LISTA);
                completeMessage.sendToTarget();
            }
        };
        Thread hiloCargarSucursales = new Thread(r);
        hiloCargarSucursales.start();
    }

    private void agregarmarcadores() {
        for(Sucursal s: sucursales){
            miMapa.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLatitud(), s.getLongitud()))
                    .title(s.getNombreSucursal()));
        }
    }

    private void centrarSimple() {
        miMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sucursales.get(0).getLatitud(), sucursales.get(0).getLongitud()), 13));
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what){
                case EVENTO_UPDATE_LISTA:
                    agregarmarcadores();
                    centrarSimple();
                    break;
            }
        }
    };

}
