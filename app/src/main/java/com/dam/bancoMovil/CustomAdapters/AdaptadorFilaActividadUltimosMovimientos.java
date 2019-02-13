package com.dam.bancoMovil.CustomAdapters;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.bancoMovil.TransferenciaHolderDeActUltMov;
import com.dam.bancoMovil.modelo.TipoTransferencia;
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

    public View getView(final int position, View convertView, ViewGroup parent){
        final Transferencia dataModel = getItem(position);
        final TransferenciaHolderDeActUltMov viewHolder;
        final View result;

        if(convertView == null){
            viewHolder = new TransferenciaHolderDeActUltMov();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.custom_adapters_adaptador_fila_actividad_ultimos_movimientos,parent,false);
            viewHolder.idTransferencia = (TextView) convertView.findViewById(R.id.textViewUltMovAct_id);
            viewHolder.cuentaOrigen = (TextView) convertView.findViewById(R.id.textViewUltMovAct_cuentaOrigen);
            viewHolder.cuentaDestino = (TextView) convertView.findViewById(R.id.textViewUltMovAct_cuentaDestino);
            viewHolder.fecha = (TextView) convertView.findViewById(R.id.textViewUltMovAct_fecha);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView2);
            viewHolder.monto = (TextView) convertView.findViewById(R.id.textViewUltMovAct_monto);
            viewHolder.tipoTransferencia = (TextView) convertView.findViewById(R.id.textViewUltMovAct_tipoTransferencia);

            result=convertView;
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (TransferenciaHolderDeActUltMov) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.fecha.setText("Fecha: " + dataModel.getFecha());
        viewHolder.monto.setText("Monto: $" + dataModel.getMonto());
        viewHolder.idTransferencia.setText("Código: "+dataModel.getId_transferencia());
        viewHolder.cuentaOrigen.setText("Cuenta origen: "+dataModel.getCuentaOrigen().getNumeroCuenta());
        viewHolder.cuentaDestino.setText("Cuenta destino: "+dataModel.getCuentaDestino().getNumeroCuenta());
        viewHolder.imageView.setTag(position);
        if(dataModel.getTipoTransferencia() == TipoTransferencia.TRANSFERENCIA_REALIZADAS){
            viewHolder.imageView.setImageResource(R.drawable.pagos_realizados);
        }else{
            viewHolder.imageView.setImageResource(R.drawable.pagos_recibidos);
        }

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
        viewHolder.idTransferencia.setTextColor(Color.BLACK);
        viewHolder.cuentaOrigen.setTextColor(Color.BLACK);
        viewHolder.cuentaDestino.setTextColor(Color.BLACK);


        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
