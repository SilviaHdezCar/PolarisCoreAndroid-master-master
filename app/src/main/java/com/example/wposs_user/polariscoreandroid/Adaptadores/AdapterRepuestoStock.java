package com.example.wposs_user.polariscoreandroid.Adaptadores;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.ArrayList;
import java.util.List;

public class AdapterRepuestoStock extends RecyclerView.Adapter<AdapterRepuestoStock.ViewHolderRepuestoStock> {

    private ArrayList<Repuesto> listRepuesto;
    private int layoutButton;
    private LayoutInflater inflador;

    public AdapterRepuestoStock(ArrayList<Repuesto> repuestos) {

        this.listRepuesto = repuestos;
    }

    public AdapterRepuestoStock(ArrayList<Repuesto> list, int layoutButton) {
        this.listRepuesto = list;
        this.layoutButton = layoutButton;
    }

    public AdapterRepuestoStock.ViewHolderRepuestoStock onCreateViewHolder(ViewGroup viewGroup, int i) {


        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_repuesto_stock, null);


        return new AdapterRepuestoStock.ViewHolderRepuestoStock(v);
    }



    public AdapterRepuestoStock(Context c, ArrayList<Repuesto> list) {
        this.listRepuesto = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public void onBindViewHolder( final ViewHolderRepuestoStock holder, final int position) {

        holder.cod_rep.setText(this.listRepuesto.get(position).getSpar_code());
        holder.nombre_rep.setText(this.listRepuesto.get(position).getSpar_name());
        holder.cant_rep.setText(""+this.listRepuesto.get(position).getSpar_quantity());
        holder.status.setText(this.listRepuesto.get(position).getSpar_status());
        String estados = holder.status.getText().toString();

        if(estados.equals("DEFECTUOSO")){ holder.rl.setBackgroundResource(R.drawable.borde_rojo);      }

        if(estados.equals("INSTALADO")) {  holder.rl.setBackgroundResource(R.drawable.borde_amarillo);   }

        if(estados.equals("DAÑADO")){ holder.rl.setBackgroundResource(R.drawable.borde_rojo);}

        if(estados.equals("EN TRANSITO")){ holder.rl.setBackgroundResource(R.drawable.borde_marron);}

        if(estados.equals("NUEVO")){ holder.rl.setBackgroundResource(R.drawable.borde_azul);}




    }

    @Override
    public int getItemCount() {

        return listRepuesto.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




    public class ViewHolderRepuestoStock extends RecyclerView.ViewHolder {


        private TextView nombre_rep;
        private TextView cod_rep;
        private TextView cant_rep;
        private TextView status;
        private RelativeLayout rl;


        ViewHolderRepuestoStock(View itemView) {
            super(itemView);
            cod_rep = (TextView) itemView.findViewById(R.id.txt_codigo_repuesto_stock);
            nombre_rep = (TextView) itemView.findViewById(R.id.txt_nombre_repuesto_stock);
            cant_rep = (TextView) itemView.findViewById(R.id.txt_cantidad_repuesto_stock);
            status=(TextView) itemView.findViewById(R.id.txt_estado_repuesto_stock);
            rl=(RelativeLayout)itemView.findViewById(R.id.cargar_repuestos);


        }
    }


}