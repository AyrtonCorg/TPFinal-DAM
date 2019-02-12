package com.dam.bancoMovil.CustomAdapters;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import com.dam.bancoMovil.modelo.Transferencia;
import com.dam.bancoMovil.R;

import java.util.List;

public class AdaptadorFilaActividadUltimosMovimientos extends ArrayAdapter<Transferencia> implements View.OnClickListener {
    private Context ctx;
    private List<Transferencia> datos;
    private int lastPosition = -1;

    public AdaptadorFilaActividadUltimosMovimientos(Context context,List<Transferencia> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    @Override
    public void onClick(View v) {

    }
}
