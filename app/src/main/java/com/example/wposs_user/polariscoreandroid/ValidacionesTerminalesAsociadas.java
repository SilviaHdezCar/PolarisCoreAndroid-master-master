package com.example.wposs_user.polariscoreandroid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.java.Terminal;


public class ValidacionesTerminalesAsociadas extends Fragment {//CREO QUE ACA SE DEBE LLENAR EL RCV
    TextView marca_ter_validaciones;
    TextView modelo_ter_validaciones;
    TextView serial_ter_validaciones;
    TextView tecno_ter_validaciones;
    TextView estado_ter_validaciones;
    TextView garantia_ter_validaciones;
    TextView fecha_recepcion_ter_validaciones;
    TextView fechal_ans_ter_validaciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.fragment_validaciones_terminales_asociadas, container, false);

        marca_ter_validaciones = (TextView) v.findViewById(R.id.marca_ter_validaciones);
       modelo_ter_validaciones = (TextView) v.findViewById(R.id.modelo_ter_validaciones);
       serial_ter_validaciones = (TextView) v.findViewById(R.id.serial_ter_validaciones);
        tecno_ter_validaciones = (TextView) v.findViewById(R.id.tecno_ter_validaciones);
        estado_ter_validaciones = (TextView) v.findViewById(R.id.estado_ter_validaciones);
        garantia_ter_validaciones = (TextView) v.findViewById(R.id.garantia_ter_validaciones);
        fecha_recepcion_ter_validaciones = (TextView) v.findViewById(R.id.fecha_recepcion_ter_validaciones);
        fechal_ans_ter_validaciones = (TextView) v.findViewById(R.id.fechal_ans_ter_validaciones);

        //voy a recorrer el arreglo de terminales para que me liste la informacion de la terminal selecciona

        for (Terminal ter : Global.TERMINALES_ASOCIADAS) {
            if (ter.getTerm_serial().equalsIgnoreCase(Global.serial_ter)) {
                marca_ter_validaciones.setText(ter.getBrand());
                System.out.println("::::::::::::::::::::::::::Brand::::::::"+ter.getBrand());
                modelo_ter_validaciones.setText(ter.getTerm_model());
                System.out.println("::::::::::::::::::::::::::Modelo::::::::"+ter.getTerm_model());
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
        return v;

    }
}
