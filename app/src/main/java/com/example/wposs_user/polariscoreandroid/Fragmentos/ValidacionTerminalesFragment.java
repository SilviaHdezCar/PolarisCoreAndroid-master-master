package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class ValidacionTerminalesFragment extends Fragment {


    private ImageView btn_atras;
    private ImageView btn_siguiente;
    private TextView tituloSerial;
    private View view;

    private RecyclerView rv;

    public ValidacionTerminalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_validacion_terminales, container, false);
        //  objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("VALIDACIONES");
        Global.fotos = new ArrayList<>();

        btn_siguiente = (ImageView) view.findViewById(R.id.btn_siguiente_validaciones_autorizadas);
        tituloSerial = (TextView) view.findViewById(R.id.tituloSerial);
        btn_atras=(ImageView)view.findViewById(R.id.btn_atras_etapas);


        tituloSerial.setText(Global.terminalVisualizar.getTerm_serial());

          btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new TipificacionesAutorizadas()).addToBackStack(null).commit();
            }
        });
        /*validacionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionTerminalesFragment()).addToBackStack(null).commit();
            }
        });*/


        String validaciones[] = Global.validaciones_listar_autorizadas.get(Global.terminalVisualizar.getTerm_serial()).split(",");
        ArrayList<Validacion> validacions = new ArrayList<>();
        String vali_estado[];
        for (int i = 0; i < validaciones.length; i++) {
            if (!validaciones[i].equalsIgnoreCase("[]")) {
                boolean ok = false, falla = false, no_aplica = false;
                String estado = "";
                vali_estado = validaciones[i].split("-");

                if (vali_estado[1].equalsIgnoreCase("OK")) {
                    ok = true;
                    estado = "ok";
                } else if (vali_estado[1].equalsIgnoreCase("Falla")) {
                    falla = true;
                    estado = "falla";
                } else if (vali_estado[1].equalsIgnoreCase("na")) {
                    no_aplica = true;
                    estado = "no aplica";
                }
           /* if (validaciones[i].split("-")[1].equals("OK")) {
                ok = true;
            } else if (validaciones[i].split("-")[1].equals("Falla")) {
                falla = true;
            } else if (validaciones[i].split("-")[1].equals("No aplica")) {
                no_aplica = true;
            }
*/
                //  Validacion v = new Validacion(validaciones[i].split("-")[0], ok, falla, no_aplica );
                Validacion v = new Validacion(validaciones[i].split("-")[0], ok, falla, no_aplica, estado);
                validacions.add(v);
            }
        }
        rv = (RecyclerView) view.findViewById(R.id.recycler_view_validaciones_autorizadas);
        Collections.sort((ArrayList) validacions);
        llenarRVValidaciones(validacions);


        return view;
    }

    //este metodo llena el recycler view con las terminales obtenidas al consumir el servicio

    public void llenarRVValidaciones(List<Validacion> validacionesRecibidas) {
        //************************SE MUESTRA LA LISTA DE TERMINALES ASOCIADAS
        if (validacionesRecibidas == null || validacionesRecibidas.size() == 0) {
            Toast.makeText(objeto, "No tiene validaciones", Toast.LENGTH_SHORT).show();
        }
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final ArrayList validaciones = new ArrayList<>();

        for (Validacion val : validacionesRecibidas) {
            if (val != null) {
                validaciones.add(val);
            }


            final AdapterValidacionesAutorizadas adapter = new AdapterValidacionesAutorizadas(validaciones, null, R.layout.panel_validaciones_terminales);

            rv.setAdapter(adapter);

        }
    }
}
