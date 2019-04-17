package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.util.Vector;

public class AdapterValidaciones extends RecyclerView.Adapter<AdapterValidaciones.ViewHolderValidaciones> {

    private Vector<Validacion> listValidaciones;
    private LayoutInflater inflador;

    public AdapterValidaciones(Context c, Vector<Validacion> list) {
        this.listValidaciones = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderValidaciones onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_validaciones_terminales_asociadas, null);

        return new ViewHolderValidaciones(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderValidaciones holder, int i) {
       Validacion ob = this.listValidaciones.elementAt(i);
        holder.txt_validaciones.setText(ob.getTevs_terminal_validation());

    }

    @Override
    public int getItemCount() {
        return listValidaciones.size();

    }

    public class ViewHolderValidaciones extends RecyclerView.ViewHolder {

        TextView txt_validaciones;


        public ViewHolderValidaciones(View v) {
            super(v);
            txt_validaciones = (TextView) v.findViewById(R.id.txt_validaciones);
        }

    }






}
