package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
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

public class AdapterTerminal_asociada extends RecyclerView.Adapter<AdapterTerminal_asociada.ViewHolderTerminal> {

    private List<Terminal> listTerminal;
    private LayoutInflater inflador;
    interfaceClick ic;
    private int layoutButton;

    public AdapterTerminal_asociada(Context c, List<Terminal> list) {
        this.listTerminal = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AdapterTerminal_asociada(List<Terminal> terminales, interfaceClick ic, int layoutButton) {
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
        String fechaANS = "";
        Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewModel/"+this.listTerminal.get(i).getTerm_model().toUpperCase()+".jpg").error(R.drawable.img_no_disponible).fit().centerInside().into(holder.imagen);
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

       if (estado.equals("QA")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_verde);
        }

        if (estado.equals("REPARACIÓN")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_amarillo);
        }

        if (estado.equals("PREDIAGNÓSTICO")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_rojo);
        }

        if (estado.equals("COTIZACIÓN")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_marron);
        }

        if (estado.equals("NUEVO")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_azul);
        }

        if (estado.equals("ALISTAMIENTO")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_gris);
        }

        if (estado.equals("OPERATIVO")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_naranja);
        }

        if (estado.equals("EN TRANSITO")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_azul_oscuro);
        }

        if (estado.equals("DADO DE BAJA")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_negro);
        }

        if (estado.equals("DIAGNÓSTICO")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_rojo_oscuro);
        }

        if (estado.equals("GARANTÍA")) {
            holder.layout_terminal_asociada.setBackgroundResource(R.drawable.borde_verde_azul);
        }

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

        ImageView imagen;
        TextView serial;
        TextView marca;
        TextView modelo;
        TextView tecnologia;
        TextView estado;
        TextView fechaANS;

        LinearLayout layout_terminal_asociada;

        public ViewHolderTerminal(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen_asociada);
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
