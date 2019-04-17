package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import java.util.Vector;

public class AdapterRepuesto extends RecyclerView.Adapter<AdapterRepuesto.ViewHolderRepuesto>{

   private Vector<Repuesto> listRepuesto;
    private LayoutInflater inflador;

    public AdapterRepuesto(Context c, Vector<Repuesto> list) {
        this.listRepuesto = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public ViewHolderRepuesto onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_repuesto, null);

        return new ViewHolderRepuesto(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderRepuesto holder, int i) {
        Repuesto rep = this.listRepuesto.elementAt(i);
        holder.codigo.setText(rep.getCodigo());
        holder.nombre.setText( rep.getNombre());
        holder.estado.setText(rep.getEstado());
        holder.cantidad.setText(String.valueOf(rep.getCantidad()));
        }

    @Override
    public int getItemCount() {
        return listRepuesto.size();

    }

    public class ViewHolderRepuesto extends RecyclerView.ViewHolder {

        TextView codigo;
        TextView nombre;
        TextView estado;
        TextView cantidad;


        public ViewHolderRepuesto(View v) {
            super(v);
            codigo = (TextView) v.findViewById(R.id.cod_rep);
            nombre = (TextView) v.findViewById(R.id.nom_rep);
            estado = (TextView) v.findViewById(R.id.estado_rep);
            cantidad = (TextView) v.findViewById(R.id.cant_rep);



        }


    }


    }
