package com.example.wposs_user.polariscoreandroid.Adaptadores;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


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

        LinearLayout ln = (LinearLayout) v.findViewById(R.id.cargar_terminales);


        return new AdapterTerminalStock.ViewHolderTerminalStock(v);
    }

    @Override
    public void onBindViewHolder(AdapterTerminalStock.ViewHolderTerminalStock holder, int i) {

        Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewModel/"+this.listTerminal.get(i).getTerm_model().toUpperCase()+".jpg").error(R.drawable.img_no_disponible).fit().centerInside().into(holder.imagen);
        Terminal ter = this.listTerminal.get(i);
        holder.marca_modelo.setText(ter.getTerm_brand() + " " + ter.getTerm_model());
        holder.serial.setText(ter.getTerm_serial());
        holder.tecnologia.setText(ter.getTerm_technology().trim());
        holder.estado.setText(ter.getTerm_status());
        String estados = holder.estado.getText().toString();

         holder.rl.setBackgroundResource(R.drawable.borde_verde);



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
        ImageView imagen;


        public ViewHolderTerminalStock(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen_asociada);
            marca_modelo = (TextView) v.findViewById(R.id.marca_ter_stock);
            serial = (TextView) v.findViewById(R.id.txt_serial_terrminal_stock);
            tecnologia = (TextView) v.findViewById(R.id.tecnologia_terminal_stock);
            estado = (TextView) v.findViewById(R.id.estado_ter_stock);
            rl = (LinearLayout) v.findViewById(R.id.cargar_terminales);


        }


    }


}




