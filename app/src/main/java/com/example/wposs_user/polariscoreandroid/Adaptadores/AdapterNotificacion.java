package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Notificacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotificacion extends RecyclerView.Adapter<AdapterNotificacion.ViewHolderNotificacion> {
    private interfaceClick ic;
    private ArrayList<Notificacion> notificaciones;
    private int layoutButton;

    public AdapterNotificacion(ArrayList<Notificacion> notificaciones, interfaceClick interfaceClick) {

        this.notificaciones = notificaciones;
    }

    public AdapterNotificacion(ArrayList<Notificacion> list, interfaceClick ic, int layoutButton) {
        System.out.println("position: " + layoutButton);
        this.notificaciones = list;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }


    @Override
    public ViewHolderNotificacion onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_notificaciones, parent, false);
        return new ViewHolderNotificacion(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderNotificacion holder, final int position) {

        holder.remitente.setText(this.notificaciones.get(position).getNoti_origen());
        holder.contenido.setText(notificaciones.get(position).getNoti_msg());
        holder.fecha.setText(notificaciones.get(position).getNoti_date_create());

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ic.onClick(notificaciones, position);
            }

        });
    }

    @Override
    public int getItemCount() {

        return notificaciones.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public interface interfaceClick {

        void onClick(List<Notificacion> notificaciones, int position);
    }

    public class ViewHolderNotificacion extends RecyclerView.ViewHolder {


        private TextView remitente;
        private TextView contenido;
        private TextView fecha;
        private ImageView eliminar;


        ViewHolderNotificacion(View itemView) {
            super(itemView);
            remitente = (TextView) itemView.findViewById(R.id.txt_origenNotificacion);
            contenido = (TextView) itemView.findViewById(R.id.mensaje_notificacion);
            fecha= (TextView) itemView.findViewById(R.id.fecha_notificacion);
            eliminar = (ImageView) itemView.findViewById(R.id.eliminar_notificacion);

        }
    }


}