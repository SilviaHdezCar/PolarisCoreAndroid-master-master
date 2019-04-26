package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.util.List;
import java.util.Vector;

public class AdapterValidaciones extends RecyclerView.Adapter<AdapterValidaciones.ViewHolderValidaciones> {

    interfaceClick ic;
    private int layoutButton;
    private List<Validacion> listValidaciones;

    public AdapterValidaciones(List<Validacion> buttonCards){

        this.listValidaciones = buttonCards;
    }
    public AdapterValidaciones(List<Validacion> list, interfaceClick ic, int layoutButton){
        this.listValidaciones = list;
        this.ic=ic;
        this.layoutButton=layoutButton;
    }
    @NonNull
    @Override
    public ViewHolderValidaciones onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_validaciones, parent, false);

        return new ViewHolderValidaciones(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderValidaciones holder, final int position) {

        holder.txt_validaciones.setText(this.listValidaciones.get(position).getTeva_description());


            //inici los rdios fakse
        holder.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                ic.onClick(listValidaciones,position,1);
                listValidaciones.get(position).setOk(true);
                listValidaciones.get(position).setFalla(false);
                listValidaciones.get(position).setNo_aplica(false);
                listValidaciones.get(position).setEstado("OK");
            }

        });

        holder.falla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                ic.onClick(listValidaciones,position,2);
                listValidaciones.get(position).setOk(false);
                listValidaciones.get(position).setFalla(true);
                listValidaciones.get(position).setNo_aplica(false);
                listValidaciones.get(position).setEstado("Falla");
            }

        });

        holder.no_aplica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                ic.onClick(listValidaciones,position,3);
                listValidaciones.get(position).setOk(false);
                listValidaciones.get(position).setFalla(false);
                listValidaciones.get(position).setNo_aplica(true);
                listValidaciones.get(position).setEstado("No aplica");
            }

        });


    }

    @Override
    public int getItemCount() {

        return listValidaciones.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public interface interfaceClick {
        void onClick(List<Validacion>button,int position, int pos_radio);
    }

    public class ViewHolderValidaciones extends RecyclerView.ViewHolder {
        RadioGroup cv;//este sería el rag¿dio grpousd
        RadioButton ok;
        RadioButton falla;
        RadioButton no_aplica;
        TextView txt_validaciones;
        int id;

        ViewHolderValidaciones(View itemView) {
            super(itemView);
            txt_validaciones = (TextView) itemView.findViewById(R.id.txt_validaciones);
            cv = (RadioGroup)itemView.findViewById(R.id.radio_group_validaciones);
            ok = (RadioButton)itemView.findViewById(R.id.bton_ok);
            falla = (RadioButton)itemView.findViewById(R.id.bton_falla);
            no_aplica = (RadioButton)itemView.findViewById(R.id.bton_NA);

        }
    }




}
