package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import java.util.List;

public class AdapterTipificacionesAutorizadas extends RecyclerView.Adapter<AdapterTipificacionesAutorizadas.ViewHolderTipificaciones> {

    private interfaceClick ic;
    private int layoutButton;
    private List<String> listTipificaciones;

    public AdapterTipificacionesAutorizadas(List<String> buttonCards) {

        this.listTipificaciones = buttonCards;
    }

    public AdapterTipificacionesAutorizadas(List<String> list, interfaceClick ic, int layoutButton) {
        this.listTipificaciones = list;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }

    @NonNull
    @Override
    public ViewHolderTipificaciones onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_tipificaciones_autorizadas, parent, false);

        return new ViewHolderTipificaciones(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderTipificaciones holder, final int position) {
        holder.txt_tipificacion.setText(this.listTipificaciones.get(position));

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
        void onClick(List<Tipificacion> button, int position, int pos_radio);
    }

    public class ViewHolderTipificaciones extends RecyclerView.ViewHolder {

        private TextView txt_tipificacion;

        ViewHolderTipificaciones(View itemView) {
            super(itemView);
            txt_tipificacion = (TextView) itemView.findViewById(R.id.txt_tipificaciones_autorizadas);


        }

    }


}
