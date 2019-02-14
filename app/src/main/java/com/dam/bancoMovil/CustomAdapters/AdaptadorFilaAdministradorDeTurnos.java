package com.dam.bancoMovil.CustomAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dam.bancoMovil.AdministradorServiciosHolder;
import com.dam.bancoMovil.AdministradorTurnosHolder;
import com.dam.bancoMovil.R;
import com.dam.bancoMovil.modelo.PlazoFijo;
import com.dam.bancoMovil.modelo.Turno;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdaptadorFilaAdministradorDeTurnos extends ArrayAdapter<Turno> implements View.OnClickListener{
    private Context ctx;
    private List<Turno> datos;
    private int lastPosition = -1;

    public AdaptadorFilaAdministradorDeTurnos(Context context,List<Turno> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Turno dataModel = getItem(position);
        final AdministradorTurnosHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new AdministradorTurnosHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.custom_adaptador_fila_administrador_de_turnos, parent, false);
            viewHolder.idSucursal = (TextView) convertView.findViewById(R.id.textViewSucursalTurno);
            viewHolder.tipoTurno = (TextView) convertView.findViewById(R.id.textViewTipoTurno);
            viewHolder.fechaTurno = (TextView) convertView.findViewById(R.id.textViewFechaTurno);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AdministradorTurnosHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.fechaTurno.setText("Fecha: " + dataModel.getFechayHora());
        viewHolder.tipoTurno.setText("Tipo: " + dataModel.getTipoTramite());
        viewHolder.idSucursal.setText("Sucursal: "+dataModel.getSucursal().getNombreSucursal());

        viewHolder.fechaTurno.setTextColor(Color.BLACK);
        viewHolder.tipoTurno.setTextColor(Color.BLACK);
        viewHolder.idSucursal.setTextColor(Color.BLACK);

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
