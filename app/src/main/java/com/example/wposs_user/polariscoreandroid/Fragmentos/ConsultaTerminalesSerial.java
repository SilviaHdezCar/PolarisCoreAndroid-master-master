package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ConsultaTerminalesSerial extends Fragment {


    private EditText serial;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView btn_buscar_terminales_serial;
    private Button btn_ser_rango_fechas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_consultar_terminales_serial, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_consultaTerminales_por_serial);
        serial = (EditText) view.findViewById(R.id.serial_buscar);
        btn_buscar_terminales_serial = (ImageView) view.findViewById(R.id.btn_buscar_terminales_serial);
        btn_ser_rango_fechas = (Button) view.findViewById(R.id.btn_ser_rango_fechas);

        btn_ser_rango_fechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesFechas()).commit();
            }
        });
        btn_buscar_terminales_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarTerminalesPorSerial();
            }
        });


        return view;


    }

    //*******************************BUSQUEDA POR SERIAL

    public void buscarTerminalesPorSerial() {

        Vector<Terminal> terminal = new Vector<>();
        terminal.removeAllElements();

        if (serial.getText().toString().isEmpty()) {
            Toast.makeText(objeto, "Por favor ingrese el serial", Toast.LENGTH_SHORT).show();
            return;
        }


        for (Terminal ter : Global.TERMINALES_ASOCIADAS) {
            if (ter.getTerm_serial().equalsIgnoreCase(serial.getText().toString())) {
                terminal.add(ter);
                recyclerView.setAdapter(new AdapterTerminal(objeto, terminal));//le pasa los datos-> lista de usuarios
                layoutManager = new LinearLayoutManager(objeto);// en forma de lista
                recyclerView.setLayoutManager(layoutManager);

            }
        }
        if (terminal.size() == 0) {
            Toast.makeText(objeto, "No se encontraron terminales registradas con ese serial", Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(new AdapterTerminal(objeto, terminal));//le pasa los datos-> lista de usuarios
            layoutManager = new LinearLayoutManager(objeto);// en forma de lista
            recyclerView.setLayoutManager(layoutManager);
        }
        serial.setText("");
    }
}
