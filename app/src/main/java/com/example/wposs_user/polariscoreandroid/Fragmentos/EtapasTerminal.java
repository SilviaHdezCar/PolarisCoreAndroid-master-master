package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class EtapasTerminal extends Fragment {


    private TextView observacionesEtapas;
    private View view;
    private RecyclerView rv;
    private View v;
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
        observacionesEtapas = (TextView) view.findViewById(R.id.observaciones_etapas);
        objeto.setTitle("ETAPAS");


        rv = (RecyclerView) view.findViewById(R.id.recycler_view_etapas);


        //consumirServicioEtapas();
        llenarRVEtapas(Global.OBSERVACIONES);
        return view;

    }

    //boton atras de la calse etapas
    public void volverEtapas(View v) {
        objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
    }


    /* *//**
     * Metodo utilizados para consumir el servicio  para listar las observaciones de acuerdo a una terminal mediante una petición REST
     * En el encabezado va el token-> Authenticator
     * Se envía el serial de la terminal  Global.serial_ter
     **//*
    public void consumirServicioEtapas() {
        o = null;
        Global.OBSERVACIONES = null;
        Global.OBSERVACIONES = new ArrayList<Observacion>();
        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/observations";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("serial", Global.serial_ter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Global.STATUS_SERVICE = response.get("status").toString();
                            System.out.println("status:  " + Global.STATUS_SERVICE);

                            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                Toast.makeText(objeto, "Error al consultar las observaciones", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            response = new JSONObject(response.get("data").toString());

                           JSONArray jsonArray = response.getJSONArray("observaciones");


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No tiene obervaciones";
                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();

                               *//* objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas());
                                objeto.getSupportFragmentManager().beginTransaction().isAddToBackStackAllowed();
                                objeto.getSupportFragmentManager().beginTransaction().commit();*//*
                                return;
                            }

                            //objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new EtapasTerminal()).addToBackStack(null).commit();
                            String obser = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                obser = jsonArray.getString(i);

                                o = gson.fromJson(obser, Observacion.class);
                                if (o != null) {
                                }
                                Global.OBSERVACIONES.add(o);
                            }
                            llenarRVEtapas(Global.OBSERVACIONES);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error Respuesta en JSON: " + error.getMessage());
                        Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authenticator", Global.TOKEN);

                return params;
            }
        };

        queue.add(jsArrayRequest);

    }
*/

    /**
     * Metodo utilizado para llenar el recycler view de las observaciones del terminal seleccionado
     *
     * @Params Recibe la lista  observaciones o etapas que van a ser mostradas
     **/
    public void llenarRVEtapas(List<Observacion> observaciones) {
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        ArrayList observations = new ArrayList<>();

        for (Observacion observ : observaciones) {
            if (observ != null) {
                observations.add(observ);
            }
        }

        //Al dar clic en una observacion infla el panel de validaciones
        final AdapterEtapa adapter = new AdapterEtapa(observations, new AdapterEtapa.interfaceClick() {
            @Override
            public void onClick(List<Observacion> observaciones, int position) {
                //Validar fotos: SI--> Pasa al fragment que contiene las fotos. NO-->infla el fragment de validaciones
                String foto1 = observaciones.get(position).getTeob_photo();
                String foto2 = observaciones.get(position).getTeob_photo();//validar con la foto dos
                if (!foto1.isEmpty() || !foto2.isEmpty()) {
                    Global.foto1_etapa_ter = foto1;
                    Global.foto2_etapa_ter = foto2;
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new FotoObservacionFragment()).addToBackStack(null).commit();
                } else {
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();
                }
            }
        }, R.layout.panel_etapas);

        rv.setAdapter(adapter);

    }
}
