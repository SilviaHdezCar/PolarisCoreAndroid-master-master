package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal_asociada;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidaciones;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Tools;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.util.ArrayList;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ValidacionesTerminalesAsociadas extends Fragment {//CREO QUE ACA SE DEBE LLENAR EL RCV

    private TextView marca_ter_validaciones;
    private TextView modelo_ter_validaciones;
    private TextView serial_ter_validaciones;
    private TextView tecno_ter_validaciones;
    private TextView estado_ter_validaciones;
    private TextView garantia_ter_validaciones;
    private TextView fecha_recepcion_ter_validaciones;
    private TextView fechal_ans_ter_validaciones;
    private RecyclerView rv;
    private LinearLayout layout_encabezado_tipificaciones;
    private TextView lbl_msj_validaciones;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_validaciones_terminales_asociadas, container, false);

        lbl_msj_validaciones = (TextView) v.findViewById(R.id.lbl_msj_validaciones);
        marca_ter_validaciones = (TextView) v.findViewById(R.id.marca_ter_validaciones);
        modelo_ter_validaciones = (TextView) v.findViewById(R.id.modelo_ter_validaciones);
        serial_ter_validaciones = (TextView) v.findViewById(R.id.serial_ter_validaciones);
        tecno_ter_validaciones = (TextView) v.findViewById(R.id.tecno_ter_validaciones);
        estado_ter_validaciones = (TextView) v.findViewById(R.id.estado_ter_validaciones);
        garantia_ter_validaciones = (TextView) v.findViewById(R.id.garantia_ter_validaciones);
        fecha_recepcion_ter_validaciones = (TextView) v.findViewById(R.id.fecha_recepcion_ter_validaciones);
        fechal_ans_ter_validaciones = (TextView) v.findViewById(R.id.fechal_ans_ter_validaciones);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_validaciones);
        layout_encabezado_tipificaciones = (LinearLayout) v.findViewById(R.id.layout_encabezado_tipificaciones);

        //voy a recorrer el arreglo de terminales para que me liste la informacion de la terminal selecciona

        for (Terminal ter : Global.TERMINALES_ASOCIADAS) {
            if (ter.getTerm_serial().equalsIgnoreCase(Global.serial_ter)) {
                marca_ter_validaciones.setText(ter.getBrand());
                modelo_ter_validaciones.setText(ter.getTerm_model());
                serial_ter_validaciones.setText(ter.getTerm_serial());
                tecno_ter_validaciones.setText(ter.getTerm_technology());
                estado_ter_validaciones.setText(ter.getTerm_status());

                if (Integer.parseInt(ter.getTerm_warranty_time()) >= 0) {
                    garantia_ter_validaciones.setText("Con garantía");
                } else {
                    garantia_ter_validaciones.setText("Si garantía");
                }

                fecha_recepcion_ter_validaciones.setText(ter.getTerm_date_register());
                fechal_ans_ter_validaciones.setText(ter.getTerm_date_finish());

            }
        }
        if (Global.VALIDACIONES.size() == 0) {
            layout_encabezado_tipificaciones.setVisibility(View.INVISIBLE);
            lbl_msj_validaciones.setText("No hay validaciones registradas");
        } else {
            lbl_msj_validaciones.setVisibility(View.INVISIBLE);
            layout_encabezado_tipificaciones.setVisibility(View.VISIBLE);
            llenarRVValidaciones(Global.VALIDACIONES);
        }


        return v;

    }


    //este metodo llena el recycler view con las terminales obtenidas al consumir el servicio

    public void llenarRVValidaciones(List<Validacion> validacionesRecibidas) {
        //************************SE MUESTRA LA LISTA DE TERMINALES ASOCIADAS

        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final ArrayList validaciones = new ArrayList<>();

        //recorro la lista obtenida y la agg a la lista

        for (Validacion val : validacionesRecibidas) {
            if (val != null) {
                validaciones.add(val);//  butons.add(new ButtonCard(nombre, "","",icon,idVenta));
            }


            final AdapterValidaciones adapter = new AdapterValidaciones(validaciones, new AdapterValidaciones.interfaceClick() {
                @Override
                public void onClick(List<Validacion> listValidaciones, int position, int pos_radio) {
                    System.out.println("al dar clic---- position " + position + "pos_radio " + pos_radio);

                }


            }, R.layout.panel_validaciones);

            rv.setAdapter(adapter);

        }System.out.println("Tamaño del arreglo " + validaciones.size());
    }
}
