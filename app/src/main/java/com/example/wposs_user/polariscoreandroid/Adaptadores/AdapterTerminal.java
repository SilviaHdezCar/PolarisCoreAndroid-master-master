package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.Vector;

public class AdapterTerminal extends RecyclerView.Adapter<AdapterTerminal.ViewHolderTerminal> {

    private Vector<Terminal> listTerminal;
    private LayoutInflater inflador;

    public AdapterTerminal(Context c, Vector<Terminal> list) {
        this.listTerminal = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public ViewHolderTerminal onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_terminal, null);

        return new ViewHolderTerminal(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderTerminal holder, int i) {
        Terminal ter = this.listTerminal.elementAt(i);
        holder.serial.setText( ter.getTerm_serial());
        holder.marca.setText( ter.getTerm_brand());
        holder.modelo.setText( ter.getTerm_model());
        holder.tecnologia.setText( ter.getTerm_technology());
        holder.estado.setText(ter.getTerm_status());
        holder.fechaANS.setText(ter.getTerm_date_finish()+"");

    }

    @Override
    public int getItemCount() {
        return listTerminal.size();

    }

    public class ViewHolderTerminal extends RecyclerView.ViewHolder {

        TextView serial;
        TextView marca;
        TextView modelo;
        TextView tecnologia;
        TextView estado;
        TextView fechaANS;


        public ViewHolderTerminal(View v) {
            super(v);
            serial = (TextView) v.findViewById(R.id.serial_ter);
            marca = (TextView) v.findViewById(R.id.marca_ter);
            modelo = (TextView) v.findViewById(R.id.modelo_ter);
            tecnologia = (TextView) v.findViewById(R.id.tecno_ter);
            estado = (TextView) v.findViewById(R.id.estado_ter);
            fechaANS = (TextView) v.findViewById(R.id.fechal_ter);


        }


    }



}
