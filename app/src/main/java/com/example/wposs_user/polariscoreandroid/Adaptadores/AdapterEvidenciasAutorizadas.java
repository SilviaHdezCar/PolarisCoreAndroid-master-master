package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class AdapterEvidenciasAutorizadas extends RecyclerView.Adapter<AdapterEvidenciasAutorizadas.ViewHolderEvidencias> {

    private interfaceClick ic;
    private int layoutButton;
    private List<Observacion> listObservaciones;
    private LayoutInflater inflador;

    public AdapterEvidenciasAutorizadas(Context c, List<Observacion> list) {
        this.listObservaciones = list;
        this.inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AdapterEvidenciasAutorizadas(List<Observacion> list, interfaceClick ic, int layoutButton) {
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
        String nomfotos=listObservaciones.get(position).getTeob_photo().trim();
        String foto1="";
        String foto2="";

    //    Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/nombreImagen.extension"+Global.ID+".jpg").error(R.mipmap.ic_profile).fit().centerInside().into(imageView);
        if(!nomfotos.isEmpty()|| nomfotos!=null){
            String fotos []=listObservaciones.get(position).getTeob_photo().split("/");
            foto1=fotos[0];
            foto2=fotos[1];

          //  holder.img_evidencia.setImageDrawable(R.id.);
            holder.txt_nomFoto.setText(listObservaciones.get(position).getTeob_photo());
            holder.txt_fechaFoto.setText(listObservaciones.get(position).getTeob_fecha());
        }

        holder.panel_evidencias_autorizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                ic.onClick(listObservaciones, position);
            }

        });



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
        void onClick(List<Observacion> listObservaciones, int position);
    }

    public class ViewHolderEvidencias extends RecyclerView.ViewHolder {

        private ImageView img_evidencia;
        private TextView txt_nomFoto;
        private TextView txt_fechaFoto;
        private LinearLayout panel_evidencias_autorizadas;

        ViewHolderEvidencias(View itemView) {
            super(itemView);
            img_evidencia = (ImageView) itemView.findViewById(R.id.img_evidencia);
            txt_nomFoto = (TextView) itemView.findViewById(R.id.txt_nomFoto);
            txt_fechaFoto = (TextView) itemView.findViewById(R.id.txt_fechaFoto);
            panel_evidencias_autorizadas = (LinearLayout) itemView.findViewById(R.id.panel_evidencias_autorizadas);


        }

    }


}
