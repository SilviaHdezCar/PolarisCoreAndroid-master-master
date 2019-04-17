package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.java.Etapas;

import java.util.Vector;

public class AdapterEtapas extends RecyclerView.Adapter<AdapterEtapas.ViewHolderEtapas> {

    private Vector<Etapas> listEtapa;
    private LayoutInflater inflador;

    public AdapterEtapas(Context c, Vector<Etapas> list) {
        this.listEtapa = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderEtapas onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_etapas_terminal, null);

        return new ViewHolderEtapas(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderEtapas holder, int i) {
       Etapas eta = this.listEtapa.elementAt(i);
        holder.contador_etapas.setText("1");
        holder.usuario_etapas.setText(eta.getUsuario().getNombre()+"   "+eta.getUsuario().getApellido());
        holder.fecha_etapas.setText(eta.getFecha()+"");
        holder.observaciones_etapas.setText(eta.getObservacion());

    }

    @Override
    public int getItemCount() {
        return listEtapa.size();

    }

    public class ViewHolderEtapas extends RecyclerView.ViewHolder {

        TextView usuario_etapas;
        TextView fecha_etapas;
        TextView observaciones_etapas;
        TextView contador_etapas;


        public ViewHolderEtapas(View v) {
            super(v);
            usuario_etapas = (TextView) v.findViewById(R.id.usuario_etapas);
            fecha_etapas = (TextView) v.findViewById(R.id.fecha_etapas);
            observaciones_etapas = (TextView) v.findViewById(R.id.observaciones_etapas);
            contador_etapas = (TextView) v.findViewById(R.id.contador_et);
        }

    }






}
