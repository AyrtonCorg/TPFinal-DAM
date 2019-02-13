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

import com.dam.bancoMovil.R;
import com.dam.bancoMovil.TransferenciaHolderDeMenuCuenta;
import com.dam.bancoMovil.modelo.Transferencia;


import java.util.List;

public class AdaptadorFilaUltimosMovimientosDeCuenta extends ArrayAdapter<Transferencia> implements View.OnClickListener {

    private Context ctx;
    private List<Transferencia> datos;
    private int lastPosition = -1;

    public AdaptadorFilaUltimosMovimientosDeCuenta(Context context,List<Transferencia> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        final Transferencia dataModel = getItem(position);
        final TransferenciaHolderDeMenuCuenta viewHolder;
        final View result;

        if(convertView == null){
            viewHolder = new TransferenciaHolderDeMenuCuenta();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.custom_adapters_adaptador_fila_ultimos_movimientos_de_cuenta,parent,false);
            viewHolder.fecha = (TextView) convertView.findViewById(R.id.textViewMenuCuentaFecha);
            viewHolder.monto = (TextView) convertView.findViewById(R.id.textViewMenuCuentaMonto);
            viewHolder.tipoTransferencia = (TextView) convertView.findViewById(R.id.textViewMenuCuentaTipoTransferencia);

            result=convertView;
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (TransferenciaHolderDeMenuCuenta) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.fecha.setText("Fecha: " + dataModel.getFecha());
        viewHolder.monto.setText("Monto: $" + dataModel.getMonto());

        switch (dataModel.getTipoTransferencia()){
            case TRANSFERENCIA_REALIZADAS:
                viewHolder.tipoTransferencia.setText("Tipo de transferencia: Transferencia realizada");
                break;
            case DEPOSITO_AGREGAR_SALDO:
                viewHolder.tipoTransferencia.setText("Tipo de transferencia: Agregar saldo");
                break;
            case DEPOSITO_PLAZO_FIJO:
                viewHolder.tipoTransferencia.setText("Tipo de transferencia: Depósito del plazo fijo");
                break;
            case TRANSFERENCIA_RECIBIDA:
                viewHolder.tipoTransferencia.setText("Tipo de transferencia: Transferencia recibida");
                break;
        }

        viewHolder.monto.setTextColor(Color.BLACK);
        viewHolder.fecha.setTextColor(Color.BLACK);
        viewHolder.tipoTransferencia.setTextColor(Color.BLACK);

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
