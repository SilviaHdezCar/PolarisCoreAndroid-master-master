package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;

import java.util.List;
import java.util.Vector;

public class AdapterTipificaciones extends RecyclerView.Adapter<AdapterTipificaciones.ViewHolderTipificaciones> {
   private interfaceClick ic;
    private List<Tipificacion> listTipificaciones;
    private int layoutButton;

    public AdapterTipificaciones(List<Tipificacion> tipificacions) {

        this.listTipificaciones = tipificacions;
    }

    public AdapterTipificaciones(List<Tipificacion> list, interfaceClick ic, int layoutButton) {
        System.out.println("position: "+layoutButton);
        this.listTipificaciones = list;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }

    @NonNull
    @Override
    public ViewHolderTipificaciones onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_tipificaciones, parent, false);

        return new ViewHolderTipificaciones(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderTipificaciones holder, final int position) {

        holder.txt_tipificaciones.setText(this.listTipificaciones.get(position).getTetv_description());
        holder.btn_eliminar_tipificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ic.onClick(listTipificaciones,position);
            }

        });
    }

    @Override
    public int getItemCount() {

        return listTipificaciones.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public interface interfaceClick {

        void onClick(List<Tipificacion> listTipificaciones, int position);
    }

    public class ViewHolderTipificaciones extends RecyclerView.ViewHolder {

        private TextView txt_tipificaciones;
        private ImageButton btn_eliminar_tipificacion;


        ViewHolderTipificaciones(View itemView) {
            super(itemView);
            txt_tipificaciones = (TextView) itemView.findViewById(R.id.txt_tipificaciones);
            btn_eliminar_tipificacion = (ImageButton) itemView.findViewById(R.id.btn_eliminar_tipificacion);


        }
    }


}
