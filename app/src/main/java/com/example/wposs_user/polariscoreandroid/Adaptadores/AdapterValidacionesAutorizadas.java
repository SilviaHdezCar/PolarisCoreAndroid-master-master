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
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.util.List;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class AdapterValidacionesAutorizadas extends RecyclerView.Adapter<AdapterValidacionesAutorizadas.ViewHolderValidaciones> {

    private interfaceClick ic;
    private int layoutButton;
    private List<Validacion> listValidaciones;

    public AdapterValidacionesAutorizadas(List<Validacion> buttonCards) {

        this.listValidaciones = buttonCards;
    }

    public AdapterValidacionesAutorizadas(List<Validacion> list, interfaceClick ic, int layoutButton) {
        this.listValidaciones = list;
        this.ic = ic;
        this.layoutButton = layoutButton;
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
         if(listValidaciones.get(position).isOk()){
            holder.setOK(true);
        }else if(listValidaciones.get(position).isFalla()){
            holder.setFalla(true);
        }else if(listValidaciones.get(position).isNo_aplica()){
            holder.setNoAplica(true);
        }

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
        void onClick(List<Validacion> button, int position, int pos_radio);
    }

    public class ViewHolderValidaciones extends RecyclerView.ViewHolder {
        private RadioGroup cv;//este sería el rag¿dio grpousd
        private RadioButton ok;
        private RadioButton falla;
        private TextView titulo;
        private RadioButton no_aplica;
        private TextView txt_validaciones;
        private int id;

        ViewHolderValidaciones(View itemView) {
            super(itemView);
            txt_validaciones = (TextView) itemView.findViewById(R.id.txt_validaciones);
            cv = (RadioGroup) itemView.findViewById(R.id.radios_validaciones);
            ok = (RadioButton) itemView.findViewById(R.id.bton_ok);
            falla = (RadioButton) itemView.findViewById(R.id.bton_falla);
            no_aplica = (RadioButton) itemView.findViewById(R.id.bton_NA);
            titulo = (TextView) itemView.findViewById(R.id.tituloSerial);
            ok.setChecked(false);
            ok.setEnabled(false);
            falla.setChecked(false);
            falla.setEnabled(false);
            no_aplica.setChecked(false);
            no_aplica.setEnabled(false);

        }

        public void setOK(boolean estado){
            ok.setChecked(estado);
        }
        public void setFalla(boolean estado){
            falla.setChecked(estado);
        }
        public void setNoAplica(boolean estado){
            no_aplica.setChecked(estado);
        }
        public void setTitulo(String tituloN){
            titulo.setText(tituloN);
        }
    }


}