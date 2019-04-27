package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;


import java.util.List;
import java.util.Vector;

public class AdapterRepuestoDiag extends RecyclerView.Adapter<AdapterRepuestoDiag.ViewHolderRepuestoDiag> {


    private List<Repuesto> listRepuesto;
    private LayoutInflater inflador;
    private View.OnClickListener listener; AdapterRepuestoDiag.interfaceClick ic;


    public AdapterRepuestoDiag(Context c,List<Repuesto> list) {
        this.listRepuesto = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public AdapterRepuestoDiag.ViewHolderRepuestoDiag onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_agregarep, null);
        return new AdapterRepuestoDiag.ViewHolderRepuestoDiag(v);
    }



    public void onBindViewHolder(AdapterRepuestoDiag.ViewHolderRepuestoDiag holder, final int i) {
        Repuesto rep = this.listRepuesto.get(i);
        holder.codigo.setText(rep.getSpar_code());
        holder.nombre.setText( rep.getSpar_name());
         holder.cantidad.setText(""+rep.getSpar_quantity());
           }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public int getItemCount() {
        return listRepuesto.size();
    }

    public interface interfaceClick {
        void onClick(List<Repuesto> repuestos, int position);
    }




    public class ViewHolderRepuestoDiag extends RecyclerView.ViewHolder {

        TextView codigo;
        TextView nombre;
        TextView cantidad;
        ImageButton img;



        public ViewHolderRepuestoDiag(View v) {
            super(v);
            codigo = (TextView) v.findViewById(R.id.txt_CodAgregarRep);
            nombre = (TextView) v.findViewById(R.id.txt_NomAgregarRep);
            cantidad = (TextView) v.findViewById(R.id.txt_CantAgregarRep);
            ImageButton img= (ImageButton)v.findViewById(R.id.btn_delete_rep);

        }


    }


}
