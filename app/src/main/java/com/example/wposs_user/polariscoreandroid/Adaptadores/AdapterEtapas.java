package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.List;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class AdapterEtapas extends RecyclerView.Adapter<AdapterEtapas.ViewHolderEtapas> {

    private Vector<Observacion> listEtapa;
    private LayoutInflater inflador;

    public AdapterEtapas(Context c, Vector<Observacion> list) {
        this.listEtapa = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderEtapas onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_etapas, null);

        return new ViewHolderEtapas(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderEtapas holder, int i) {

        Observacion ob = this.listEtapa.elementAt(i);

        holder.usuario_etapas.setText(ob.getTeob_id_user());
        holder.fecha_etapas.setText(ob.getTeob_fecha());
        holder.etapas_serial_terminal.setText("Serial: "+ob.getTeob_serial_terminal());
        holder.observaciones_etapas.setText(ob.getTeob_description());
    }

    @Override
    public int getItemCount() {
        return listEtapa.size();

    }

    public interface interfaceClick {
        void onClick(List<Observacion> etapa, int position);
    }

    public class ViewHolderEtapas extends RecyclerView.ViewHolder {

        TextView usuario_etapas;
        TextView fecha_etapas;
        TextView observaciones_etapas;
        TextView etapas_serial_terminal;

        public ViewHolderEtapas(View v) {
            super(v);
            usuario_etapas = (TextView) v.findViewById(R.id.usuario_etapas);
            etapas_serial_terminal = (TextView) v.findViewById(R.id.etapas_serial_terminal);
            fecha_etapas = (TextView) v.findViewById(R.id.fecha_etapas);
            observaciones_etapas = (TextView) v.findViewById(R.id.observaciones_etapas);
        }

    }



}
