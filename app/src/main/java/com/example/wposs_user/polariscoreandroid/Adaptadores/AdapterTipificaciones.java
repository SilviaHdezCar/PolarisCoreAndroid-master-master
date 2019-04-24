package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.util.Vector;

public class AdapterTipificaciones extends RecyclerView.Adapter<AdapterTipificaciones.ViewHolderTipificaciones> {

    private Vector<Tipificacion> listTipificaciones;
    private LayoutInflater inflador;

    public AdapterTipificaciones(Context c, Vector<Tipificacion> list) {
        this.listTipificaciones = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderTipificaciones onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_validaciones, null);

        return new ViewHolderTipificaciones(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderTipificaciones holder, int i) {
       Tipificacion ob = this.listTipificaciones.elementAt(i);

      //  holder.txt_validaciones.setText(ob.getTeva_description());

    }

    @Override
    public int getItemCount() {
        return listTipificaciones.size();

    }

    public class ViewHolderTipificaciones extends RecyclerView.ViewHolder {

       // TextView txt_validaciones;


        public ViewHolderTipificaciones(View v) {
            super(v);
         //   txt_validaciones = (TextView) v.findViewById(R.id.txt_validaciones);
        }

    }






}
