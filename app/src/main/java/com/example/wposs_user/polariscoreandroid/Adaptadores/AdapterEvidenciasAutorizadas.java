package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterEvidenciasAutorizadas extends RecyclerView.Adapter<AdapterEvidenciasAutorizadas.ViewHolderEvidencias> {

    private interfaceClick ic;
    private int layoutButton;
    private List<Observacion> listObservaciones;

    public AdapterEvidenciasAutorizadas(List<Observacion> buttonCards) {

        this.listObservaciones = buttonCards;
    }

    public AdapterEvidenciasAutorizadas(List<Observacion> list, interfaceClick ic, int layoutButton) {
        System.out.println("position: " + layoutButton);
        this.listObservaciones = list;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }

    @NonNull
    @Override
    public ViewHolderEvidencias onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_evidencias_autorizadas, parent, false);

        return new ViewHolderEvidencias(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderEvidencias holder, final int position) {
        System.out.println("Está en Adaptador tipifi va a llernar RV");
        String nomfotos=listObservaciones.get(position).getTeob_photo().trim();
        String foto1="";
        String foto2="";
        if(!nomfotos.isEmpty()|| nomfotos!=null){
            String fotos []=listObservaciones.get(position).getTeob_photo().split("/");
            for (int i=0; i<fotos.length;i++){
                foto1=fotos[0];
                foto2=fotos[0];
            }
          //  holder.img_evidencia.setImageDrawable(R.id.);
            holder.txt_nomFoto.setText(listObservaciones.get(position).getTeob_photo());
            holder.txt_fechaFoto.setText(listObservaciones.get(position).getTeob_fecha());
        }




    }

    @Override
    public int getItemCount() {

        return listObservaciones.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public interface interfaceClick {
        void onClick(List<Tipificacion> button, int position, int pos_radio);
    }

    public class ViewHolderEvidencias extends RecyclerView.ViewHolder {

        private ImageView img_evidencia;
        private TextView txt_nomFoto;
        private TextView txt_fechaFoto;

        ViewHolderEvidencias(View itemView) {
            super(itemView);
            img_evidencia = (ImageView) itemView.findViewById(R.id.img_evidencia);
            txt_nomFoto = (TextView) itemView.findViewById(R.id.txt_nomFoto);
            txt_fechaFoto = (TextView) itemView.findViewById(R.id.txt_fechaFoto);


        }

    }


}
