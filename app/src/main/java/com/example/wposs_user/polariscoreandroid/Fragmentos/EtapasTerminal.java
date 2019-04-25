package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEtapas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Tools;
import com.example.wposs_user.polariscoreandroid.java.Etapas;
import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class EtapasTerminal extends Fragment {


    private TextView serial_ter_seleccionada;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_etapas_terminal, container, false);
        serial_ter_seleccionada = (TextView) view.findViewById(R.id.serial_ter_seleccionada);


        if (Global.OBSERVACIONES == null) {
            //serial_ter_seleccionada.setText(Global.serial_ter + " No tiene observaciones");
            serial_ter_seleccionada.setText(Global.serial_ter + " " + Global.mensaje+" no tiene observaciones");
        } else {
            serial_ter_seleccionada.setText("Serial: " + Global.serial_ter);
            llenarRVEtapas(Global.OBSERVACIONES);

        }

        return view;

    }


    public void llenarRVEtapas(List<Observacion> observaciones) {


        System.out.println("----+-+-+-+-+-+-+-+-+-+-+-Etapas Terminal: tamaño del areglo recibido= "+observaciones.size());

        Vector<Observacion> obs = new Vector<>();

        for (Observacion o : observaciones) {
            if (o != null) {
                obs.add(o);
            }

        }

        objeto.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_etapas);
        System.out.println("--------------ETAPAS TERMINAL- TAMAÑO DEL ARREGLO QUE SE ENVIA---"+obs.size());
        objeto.recyclerView.setAdapter(new AdapterEtapas(objeto, obs));//le pasa los datos-> lista de usuarios

        objeto.layoutManager = new LinearLayoutManager(objeto);// en forma de lista
        objeto.recyclerView.setLayoutManager(objeto.layoutManager);


    }
}
