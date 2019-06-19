package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEtapa;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.Fragmentos.InicialFragment.inicial;


public class EtapasTerminal extends Fragment {


    private TextView observacionesEtapas;
    private View view;
    private Button btn_siguiente;
    private RecyclerView rv;
    private static Observacion o;
    private RequestQueue queue;

    public EtapasTerminal() {
        queue = Volley.newRequestQueue(objeto);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_etapas_terminal, container, false);
       // objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        observacionesEtapas = (TextView) view.findViewById(R.id.observaciones_etapas);
        objeto.setTitulo("ETAPAS");


        rv = (RecyclerView) view.findViewById(R.id.recycler_view_etapas);
        btn_siguiente = (Button) view.findViewById(R.id.btn_siguiente_etapas);
//ordenar observaciones por fechas
        llenarRVEtapas(Global.OBSERVACIONES);
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();
            }
        });
        return view;

    }


    /**
     * Metodo utilizado para llenar el recycler view de las observaciones del terminal seleccionado
     *
     * @Params Recibe la lista  observaciones o etapas que van a ser mostradas
     **/
    public void llenarRVEtapas(List<Observacion> observaciones) {

        if (observaciones == null || observaciones.size() == 0) {
            //para que elimine este fragmento de la pila
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
            objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();
            return;
        }


        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        ArrayList observations = new ArrayList<>();

        for (int i = observaciones.size()-1;i>=0;i--){
            System.out.println("i: "+i);
            if(observaciones.get(i)!=null){
                if(!observaciones.get(i).getTeob_description().trim().isEmpty()){
                    observations.add(observaciones.get(i));
                }
            }
        }

        //Al dar clic en una observacion infla el panel de validaciones
        final AdapterEtapa adapter = new AdapterEtapa(observations, new AdapterEtapa.interfaceClick() {
            @Override
            public void onClick(List<Observacion> observaciones, int position) {
               // objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();


            }
        }, R.layout.panel_etapas);

        rv.setAdapter(adapter);

        if (observaciones == null || observaciones.size() == 0) {
            //para que elimine este fragmento de la pila
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack();
            objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();

        }

    }


}
