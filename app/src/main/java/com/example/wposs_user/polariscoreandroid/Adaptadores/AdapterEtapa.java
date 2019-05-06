package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.List;

public class AdapterEtapa extends RecyclerView.Adapter<AdapterEtapa.ViewHolderObservacion> {

    private List<Observacion> listObservacion;
    private LayoutInflater inflador;

    interfaceClick ic;
    private int layoutButton;

    public AdapterEtapa(Context c, List<Observacion> list) {
        this.listObservacion = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AdapterEtapa(List<Observacion> observacions, interfaceClick ic, int layoutButton) {
        this.listObservacion = observacions;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }


    @Override
    public ViewHolderObservacion onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_etapas, null);

        return new ViewHolderObservacion(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderObservacion holder, final int i) {


        holder.usuario_etapas.setText(this.listObservacion.get(i).getTeob_id_user());
        holder.fecha_etapas.setText(this.listObservacion.get(i).getTeob_fecha());
        holder.etapas_serial_terminal.setText("Serial: "+this.listObservacion.get(i).getTeob_serial_terminal());
        holder.observaciones_etapas.setText(this.listObservacion.get(i).getTeob_description());



        holder.layout_etapas_ter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic.onClick(listObservacion, i);
            }

        });
    }


    @Override
    public int getItemCount() {
        return listObservacion.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface interfaceClick {
        void onClick(List<Observacion> obs, int position);
    }

    public class ViewHolderObservacion extends RecyclerView.ViewHolder {

        TextView usuario_etapas;
        TextView fecha_etapas;
        TextView observaciones_etapas;
        TextView etapas_serial_terminal;

        LinearLayout layout_etapas_ter;

        public ViewHolderObservacion(View v) {
            super(v);
            usuario_etapas = (TextView) v.findViewById(R.id.usuario_etapas);
            etapas_serial_terminal = (TextView) v.findViewById(R.id.etapas_serial_terminal);
            fecha_etapas = (TextView) v.findViewById(R.id.fecha_etapas);
            observaciones_etapas = (TextView) v.findViewById(R.id.observaciones_etapas);
            layout_etapas_ter = (LinearLayout) v.findViewById(R.id.layout_etapas_ter);


        }


    }


}
