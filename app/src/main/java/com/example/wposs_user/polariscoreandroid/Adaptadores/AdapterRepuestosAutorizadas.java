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

public class AdapterValidaciones extends RecyclerView.Adapter<AdapterValidaciones.ViewHolderValidaciones> {

    private interfaceClick ic;
    private int layoutButton;
    private List<Validacion> listValidaciones;

    public AdapterValidaciones(List<Validacion> buttonCards) {

        this.listValidaciones = buttonCards;
    }

    public AdapterValidaciones(List<Validacion> list, interfaceClick ic, int layoutButton) {
        System.out.println("position: " + layoutButton);
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
        //Llena los campos
        holder.txt_validaciones.setText(this.listValidaciones.get(position).getTeva_description());

        System.out.println("Adapter validaciones"+listValidaciones.get(position).getTeva_description()+"-"+listValidaciones.get(position).getEstado());
        //Esperando clic
        holder.ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                System.out.println("Adapter validaciones inicio: "+position+":-"+listValidaciones.get(position).getTeva_description()+"-"+listValidaciones.get(position).getEstado());
                //ic.onClick(listValidaciones, position, 1);
                listValidaciones.get(position).setOk(true);
                listValidaciones.get(position).setFalla(false);
                listValidaciones.get(position).setNo_aplica(false);
                listValidaciones.get(position).setEstado("OK");
                System.out.println("Adapter validaciones fin: "+position+":-"+listValidaciones.get(position).getTeva_description()+"-"+listValidaciones.get(position).getEstado());
            }

        });

        holder.falla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                System.out.println("Adapter validaciones inicio: "+position+":-"+listValidaciones.get(position).getTeva_description()+"-"+listValidaciones.get(position).getEstado());
                ic.onClick(listValidaciones, position, 2);
                listValidaciones.get(position).setOk(false);
                listValidaciones.get(position).setFalla(true);
                listValidaciones.get(position).setNo_aplica(false);
                listValidaciones.get(position).setEstado("Falla");
                System.out.println("Adapter validaciones fin: "+position+":-"+listValidaciones.get(position).getTeva_description()+"-"+listValidaciones.get(position).getEstado());
            }

        });

        holder.no_aplica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                System.out.println("Adapter validaciones inicio: "+position+":-"+listValidaciones.get(position).getTeva_description()+"-"+listValidaciones.get(position).getEstado());
                ic.onClick(listValidaciones, position, 3);
                listValidaciones.get(position).setOk(false);
                listValidaciones.get(position).setFalla(false);
                listValidaciones.get(position).setNo_aplica(true);
                listValidaciones.get(position).setEstado("No aplica");
                System.out.println("Adapter validaciones fin: "+position+":-"+listValidaciones.get(position).getTeva_description()+"-"+listValidaciones.get(position).getEstado());
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
        void onClick(List<Validacion> button, int position, int pos_radio);
    }

    public class ViewHolderValidaciones extends RecyclerView.ViewHolder {
        private RadioGroup cv;//este sería el rag¿dio grpousd
        private RadioButton ok;
        private RadioButton falla;
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
            ok.setChecked(false);
            falla.setChecked(false);
            no_aplica.setChecked(false);

        }
    }


}
