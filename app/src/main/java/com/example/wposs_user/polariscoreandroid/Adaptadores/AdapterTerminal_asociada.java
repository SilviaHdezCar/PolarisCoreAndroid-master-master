package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.List;

public class AdapterTerminal_asociada extends RecyclerView.Adapter<AdapterTerminal_asociada.ViewHolderTerminal> {

    private List<Terminal> listTerminal;
    private LayoutInflater inflador;

    interfaceClick ic;
    private int layoutButton;

    public AdapterTerminal_asociada(Context c, List<Terminal> list) {
        this.listTerminal = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AdapterTerminal_asociada(List<Terminal> terminales, interfaceClick ic,int layoutButton) {
        this.listTerminal = terminales;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }


    @Override
    public ViewHolderTerminal onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_terminal_asociada, null);

        return new ViewHolderTerminal(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderTerminal holder, final int i) {

        holder.serial.setText(this.listTerminal.get(i).getSerial());
        holder.marca.setText(this.listTerminal.get(i).getMarca());
        holder.modelo.setText(this.listTerminal.get(i).getModelo());
        holder.tecnologia.setText(this.listTerminal.get(i).getTecnologia());
        holder.estado.setText(this.listTerminal.get(i).getEstado());
        holder.fechaANS.setText(this.listTerminal.get(i).getFechaLimite() + "");


    }


    @Override
    public int getItemCount() {
        return listTerminal.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface interfaceClick {
        void onClick(List<Terminal> button, int position);
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
            serial = (TextView) v.findViewById(R.id.serial_ter_asociada);
            marca = (TextView) v.findViewById(R.id.marca_ter_asociada);
            modelo = (TextView) v.findViewById(R.id.modelo_ter_asociada);
            tecnologia = (TextView) v.findViewById(R.id.tecno_ter_asociada);
            estado = (TextView) v.findViewById(R.id.estado_ter_asociada);
            fechaANS = (TextView) v.findViewById(R.id.fechal_ter_asociada);


        }


    }


}
