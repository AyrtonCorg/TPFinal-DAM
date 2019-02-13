package com.dam.bancoMovil.CustomAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dam.bancoMovil.AdministradorServiciosHolder;
import com.dam.bancoMovil.R;
import com.dam.bancoMovil.modelo.PlazoFijo;
import com.dam.bancoMovil.modelo.Transferencia;

import java.text.SimpleDateFormat;
import java.util.List;


public class AdaptadorFilaAdministradorDeServicios extends ArrayAdapter<PlazoFijo> implements View.OnClickListener {

    private Context ctx;
    private List<PlazoFijo> datos;
    private int lastPosition = -1;

    public AdaptadorFilaAdministradorDeServicios(Context context,List<PlazoFijo> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final PlazoFijo dataModel = getItem(position);
        final AdministradorServiciosHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new AdministradorServiciosHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.custom_adaptador_fila_administrador_de_servicios, parent, false);
            viewHolder.fechaFin = (TextView) convertView.findViewById(R.id.textViewFechaFin);
            viewHolder.monto = (TextView) convertView.findViewById(R.id.textViewMonto);
            viewHolder.rendimiento = (TextView) convertView.findViewById(R.id.textViewRendimiento);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AdministradorServiciosHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        String hoy = dateFormat.format(dataModel.getFechaFin());

        viewHolder.fechaFin.setText("Fecha fin: " + hoy);
        viewHolder.monto.setText("Monto: " + dataModel.getMonto());
        viewHolder.rendimiento.setText("Rendimiento: "+dataModel.getRendimiento());

        viewHolder.monto.setTextColor(Color.BLACK);
        viewHolder.fechaFin.setTextColor(Color.BLACK);
        viewHolder.rendimiento.setTextColor(Color.BLACK);

        return convertView;
    }



    @Override
    public void onClick(View v) {

    }
}
