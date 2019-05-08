package com.example.wposs_user.polariscoreandroid.Adaptadores;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.ArrayList;
import java.util.Vector;


public class AdapterTerminalStock extends RecyclerView.Adapter<AdapterTerminalStock.ViewHolderTerminalStock> {

    private ArrayList<Terminal> listTerminal;
    private LayoutInflater inflador;
    private View v;

    public AdapterTerminalStock(Context c, ArrayList<Terminal> list) {
        this.listTerminal = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public AdapterTerminalStock.ViewHolderTerminalStock onCreateViewHolder(ViewGroup viewGroup, int i) {


        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_terminal_stock, null);

        LinearLayout ln=(LinearLayout)v.findViewById(R.id.cargar_terminales);



        return new AdapterTerminalStock.ViewHolderTerminalStock(v);
    }

    @Override
    public void onBindViewHolder(AdapterTerminalStock.ViewHolderTerminalStock holder, int i) {


        Terminal ter = this.listTerminal.get(i);
        holder.marca_modelo.setText( ter.getTerm_brand()+"    "+ter.getTerm_model());
        holder.serial.setText( ter.getTerm_serial());
        holder.tecnologia.setText( ter.getTerm_technology());
        holder.estado.setText(ter.getTerm_status());
        String estados = holder.estado.getText().toString();

        if(estados.equals("QA")){ holder.rl.setBackgroundResource(R.drawable.borde_verde);      }

      if(estados.equals("REPARACIÓN")) {  holder.rl.setBackgroundResource(R.drawable.borde_amarillo);   }

       if(estados.equals("PREDIAGNÓSTICO")){ holder.rl.setBackgroundResource(R.drawable.borde_rojo);}

       if(estados.equals("COTIZACIÓN")){ holder.rl.setBackgroundResource(R.drawable.borde_marron);}

         if(estados.equals("NUEVO")){ holder.rl.setBackgroundResource(R.drawable.borde_azul);}

         if(estados.equals("ALISTAMIENTO")){ holder.rl.setBackgroundResource(R.drawable.borde_gris);}

        if(estados.equals("OPERATIVO")){ holder.rl.setBackgroundResource(R.drawable.borde_naranja);}

         if(estados.equals("EN TRANSITO")){ holder.rl.setBackgroundResource(R.drawable.borde_azul_oscuro);}

        if(estados.equals("DADO DE BAJA")){ holder.rl.setBackgroundResource(R.drawable.borde_negro);}


    }

    @Override
    public int getItemCount() {
        return listTerminal.size();

    }

    public class ViewHolderTerminalStock extends RecyclerView.ViewHolder {

        TextView marca_modelo;
        TextView serial;
        TextView tecnologia;
        TextView estado;
        TextView color;
       LinearLayout rl;






        public ViewHolderTerminalStock(View v) {
            super(v);

            marca_modelo=(TextView)v.findViewById(R.id.marca_ter_stock);
            serial = (TextView) v.findViewById(R.id.txt_serial_terrminal_stock);
            tecnologia = (TextView) v.findViewById(R.id.tecnologia_terminal_stock);
            estado = (TextView) v.findViewById(R.id.estado_ter_stock);
            rl=(LinearLayout)v.findViewById(R.id.cargar_terminales);




        }


    }





}




