package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEtapas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Etapas;
import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class EtapasTerminal extends Fragment {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    Vector<Etapas> etapas;
    Vector<Terminal> terminales;

    TextView serial_ter_seleccionada;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_etapas_terminal, container, false);
        serial_ter_seleccionada=(TextView)view.findViewById(R.id.serial_ter_seleccionada);
        if (Global.OBSERVACIONES == null) {
            serial_ter_seleccionada.setText(Global.serial_ter + " No tiene observaciones");
        }else {
            serial_ter_seleccionada.setText("Serial: "+Global.serial_ter );
            objeto.llenarRVEtapas(Global.OBSERVACIONES);
        }

        return view;

    }

}
