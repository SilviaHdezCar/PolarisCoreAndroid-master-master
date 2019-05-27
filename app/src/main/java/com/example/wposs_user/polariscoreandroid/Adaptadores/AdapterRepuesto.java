package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;

import java.util.List;
import java.util.Vector;

public class AdapterRepuesto extends RecyclerView.Adapter<AdapterRepuesto.ViewHolderRepuesto> {
    private interfaceClick ic;
    private List<Repuesto> listRepuesto;
    private int layoutButton;

    public AdapterRepuesto(List<Repuesto> repuestos) {

        this.listRepuesto = repuestos;
    }

    public AdapterRepuesto(List<Repuesto> list, interfaceClick ic, int layoutButton) {
       this.listRepuesto = list;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }


    @Override
    public ViewHolderRepuesto onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_agregarep, parent, false);

        return new ViewHolderRepuesto(v);
    }

    @Override
    public void onBindViewHolder( final ViewHolderRepuesto holder, final int position) {

        holder.cod_rep.setText(this.listRepuesto.get(position).getSpar_code());
        holder.nombre_rep.setText(this.listRepuesto.get(position).getSpar_name());
        holder.cant_rep.setText(""+this.listRepuesto.get(position).getSpar_quantity());

        holder.delete_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ic.onClick(listRepuesto,position);
            }

        });
    }

    @Override
    public int getItemCount() {

        return listRepuesto.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public interface interfaceClick {

        void onClick(List<Repuesto> listRepuesto, int position);
    }

    public class ViewHolderRepuesto extends RecyclerView.ViewHolder {

        private TextView cod_rep;
        private TextView nombre_rep;
        private TextView cant_rep;
        private ImageButton delete_rep;


        ViewHolderRepuesto(View itemView) {
            super(itemView);
            cod_rep = (TextView) itemView.findViewById(R.id.txt_CodAgregarRep);
            nombre_rep = (TextView) itemView.findViewById(R.id.txt_NomAgregarRep);
            cant_rep = (TextView) itemView.findViewById(R.id.txt_CantAgregarRep);
            delete_rep= (ImageButton) itemView.findViewById(R.id.btn_delete_rep);


        }
    }


}