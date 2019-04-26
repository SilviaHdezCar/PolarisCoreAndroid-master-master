package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import java.util.ArrayList;
import java.util.Vector;

public class AdapterRepuestoDiag extends RecyclerView.Adapter<AdapterRepuestoDiag.ViewHolderRepuestoDiag> {


    private ArrayList<Repuesto> listRepuesto;
    private LayoutInflater inflador;
    private View.OnClickListener listener;

    public AdapterRepuestoDiag(Context c,ArrayList<Repuesto> list) {
        this.listRepuesto = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public AdapterRepuestoDiag.ViewHolderRepuestoDiag onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_agregarep, null);
        return new AdapterRepuestoDiag.ViewHolderRepuestoDiag(v);
    }



    public void onBindViewHolder(AdapterRepuestoDiag.ViewHolderRepuestoDiag holder, int i) {
        Repuesto rep = this.listRepuesto.get(i);
        holder.codigo.setText(rep.getSpar_code());
        holder.nombre.setText( rep.getSpar_name());
         holder.cantidad.setText(""+rep.getSpar_quantity());
    }


    public int getItemCount() {
        return listRepuesto.size();

    }




    public class ViewHolderRepuestoDiag extends RecyclerView.ViewHolder {

        TextView codigo;
        TextView nombre;
        TextView cantidad;


        public ViewHolderRepuestoDiag(View v) {
            super(v);
            codigo = (TextView) v.findViewById(R.id.txt_CodAgregarRep);
            nombre = (TextView) v.findViewById(R.id.txt_NomAgregarRep);
            cantidad = (TextView) v.findViewById(R.id.txt_CantAgregarRep);



        }


    }


}
