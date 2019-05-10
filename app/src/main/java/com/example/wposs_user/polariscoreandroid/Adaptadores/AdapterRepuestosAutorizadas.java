package com.example.wposs_user.polariscoreandroid.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import java.util.List;

public class AdapterRepuestosAutorizadas extends RecyclerView.Adapter<AdapterRepuestosAutorizadas.ViewHolderRepuestos> {

    private interfaceClick ic;
    private int layoutButton;
    private List<Repuesto> listRepuestos;

    public AdapterRepuestosAutorizadas(List<Repuesto> buttonCards) {

        this.listRepuestos = buttonCards;
    }

    public AdapterRepuestosAutorizadas(List<Repuesto> list, interfaceClick ic, int layoutButton) {
        System.out.println("position: " + layoutButton);
        this.listRepuestos = list;
        this.ic = ic;
        this.layoutButton = layoutButton;
    }

    @NonNull
    @Override
    public ViewHolderRepuestos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.panel_repuestos_autorizadas, parent, false);

        return new ViewHolderRepuestos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderRepuestos holder, final int position) {
        //Llena los campos
        holder.txt_validaciones.setText(this.listRepuestos.get(position).getSpar_name());

        // System.out.println("Adapter validaciones"+listValidaciones.get(position).getn()+"-"+listValidaciones.get(position).getEstado());
        //Esperando clic
        holder.ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Tools.toast("click: "+ buttonCards.get(position).text1);
                System.out.println("Adapter validaciones inicio: " + position + ":-" + listRepuestos.get(position).getSpar_name()+ "-" + listRepuestos.get(position).isOk());
                //ic.onClick(listValidaciones, position, 1);
                listRepuestos.get(position).setOk(true);

            }

        });


    }

    @Override
    public int getItemCount() {

        return listRepuestos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public interface interfaceClick {
        void onClick(List<Repuesto> button, int position, int pos_radio);
    }

    public class ViewHolderRepuestos extends RecyclerView.ViewHolder {

        private RadioButton ok;
        private TextView txt_validaciones;

        ViewHolderRepuestos(View itemView) {
            super(itemView);
            txt_validaciones = (TextView) itemView.findViewById(R.id.txt_repuesto_autorizada);
            ok = (RadioButton) itemView.findViewById(R.id.btn_ok_repuestos);
            ok.setChecked(false);

        }
    }


}
