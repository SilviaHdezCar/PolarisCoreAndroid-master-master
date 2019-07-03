package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class AdapterTerminal extends RecyclerView.Adapter<AdapterTerminal.ViewHolderTerminal> {

    private List<Terminal> listTerminal;
    private LayoutInflater inflador;

    AdapterTerminal_asociada.interfaceClick ic;
    private int layoutButton;

    public AdapterTerminal(Context c, List<Terminal> list) {
        this.listTerminal = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AdapterTerminal(List<Terminal> terminales, AdapterTerminal_asociada.interfaceClick ic, int layoutButton) {
        this.listTerminal = terminales;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }


    @Override
    public AdapterTerminal.ViewHolderTerminal onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_terminal_asociada, null);

        return new AdapterTerminal.ViewHolderTerminal(v);
    }


    public void onBindViewHolder(final ViewHolderTerminal holder, final int i) {
        String fechaANS = "";
        Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewModel/"+this.listTerminal.get(i).getTerm_model().toUpperCase()+".jpg").error(R.drawable.img_no_disponible).fit().centerInside().into(holder.imagen_terminal);
        holder.serial.setText(this.listTerminal.get(i).getTerm_serial());
        holder.marca.setText(this.listTerminal.get(i).getTerm_brand());
        holder.modelo.setText(this.listTerminal.get(i).getTerm_model());
        holder.tecnologia.setText(this.listTerminal.get(i).getTerm_technology());
        String estado = this.listTerminal.get(i).getTerm_status();
        holder.estado.setText(estado);
        holder.fechaANS.setText("");
        if (this.listTerminal.get(i).getTerm_date_ans() != null) {
            fechaANS = Utils.darFormatoFecha2(this.listTerminal.get(i).getTerm_date_ans());
            holder.fechaANS.setText(fechaANS);
        }


        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("ALISTAMIENTO")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_alistamiento);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("COTIZACIÓN")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_cotizacion);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("DADO DE BAJA")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_dado_baja);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("DIAGNÓSTICO")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_diagnostico);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("GARANTÍA")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_garantia);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("NUEVO")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_nuevo);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("PREDIAGNÓSTICO")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_prediagnostico);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("QA")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_qa);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("REPARACIÓN")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_reparacion);
        }

        if(this.listTerminal.get(i).getTerm_status().equalsIgnoreCase("TRANSITO")){
            holder.imagen_estado.setImageResource(R.mipmap.estado_transito);
        }


     holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_verde);


        /*Drawable icon = objeto.getResources().getDrawable(R.drawable.ic_advertencia);
        holder.imagen_estado.setImageDrawable(icon)*/;

        holder.layout_terminal_asociada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                ic.onClick(listTerminal, i);
            }

        });
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
        void onClick(List<Terminal> terminal, int position);
    }

    public class ViewHolderTerminal extends RecyclerView.ViewHolder {

        TextView serial;
        TextView marca;
        TextView modelo;
        TextView tecnologia;
        TextView estado;
        TextView fechaANS;
        ImageView imagen_terminal;
        ImageView imagen_estado;

        LinearLayout layout_terminal_asociada;

        public ViewHolderTerminal(View v) {
            super(v);
            imagen_terminal = (ImageView) v.findViewById(R.id.imagen_asociada);
            imagen_estado = (ImageView) v.findViewById(R.id.imagen_estado);
            serial = (TextView) v.findViewById(R.id.serial_ter_asociada);
            marca = (TextView) v.findViewById(R.id.marca_ter_asociada);
            modelo = (TextView) v.findViewById(R.id.modelo_ter_asociada);
            tecnologia = (TextView) v.findViewById(R.id.tecno_ter_asociada);
            estado = (TextView) v.findViewById(R.id.estado_ter_asociada);
            fechaANS = (TextView) v.findViewById(R.id.fechal_ter_asociada);
            layout_terminal_asociada = (LinearLayout) v.findViewById(R.id.layout_terminal_asociada);


        }


    }
}

