package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal_asociada;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class InicialFragment extends Fragment {
    private RecyclerView rv;
    private String serialObtenido;
    private RequestQueue queue;
    private Button btn_asociadas;
    private Button btn_autorizadas;
    private LinearLayout tab_asociada;
    private LinearLayout tab_autorizada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicial, container, false);
        btn_asociadas = (Button) v.findViewById(R.id.btn_terminales_asociadas);
        btn_autorizadas = (Button) v.findViewById(R.id.btn_terminales_autorizadas);
        tab_asociada = (LinearLayout) v.findViewById(R.id.tab_asociada);
        tab_autorizada = (LinearLayout) v.findViewById(R.id.tab_autorizada);

        rv = (RecyclerView) v.findViewById(R.id.recycler_view_consultaTerminales_inicial);
        serialObtenido = "";
        Global.TERMINALES_ASOCIADAS = new ArrayList<Terminal>();
        queue = Volley.newRequestQueue(objeto);

        consumirServicioAsociadas();

        btn_asociadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //colocar que a lo que seleccione cierto botn, cambie el color de la linea de abajo
                tab_asociada.setBackgroundColor(R.drawable.borde_inferior_blanco);
                tab_autorizada.setBackgroundColor(R.drawable.borde_inferior_verde);
                Global.TERMINALES_ASOCIADAS = null;
                Global.TERMINALES_ASOCIADAS = new ArrayList<Terminal>();
                consumirServicioAsociadas();
            }
        });

        btn_autorizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_asociada.setBackgroundColor(R.drawable.borde_inferior_verde);
                tab_autorizada.setBackgroundColor(R.drawable.borde_inferior_blanco);

                consumirServicioAutorizadas();
            }
        });


        return v;

    }

    static Terminal t;

    /**
     * Metodo utilizados para consumir el servicio  de listar terminales asociadas mediante una petición REST
     * En el encabezado va el token-> Authenticator
     * Se envía el codigo del usuario  Global.CODE
     **/
    public void consumirServicioAsociadas() {
        t = null;
        Global.TERMINALES_ASOCIADAS = null;
        Global.TERMINALES_ASOCIADAS = new ArrayList<Terminal>();
        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals//associatedsWithDiagnosis";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", Global.CODE);
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
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray = response.getJSONArray("terminales");


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No tiene terminales asociadas";
                                return;
                            }
                            String ter = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                ter = jsonArray.getString(i);

                                t = gson.fromJson(ter, Terminal.class);
                                if (t != null) {
                                }
                                Global.TERMINALES_ASOCIADAS.add(t);
                            }
                            llenarRVAsociadas(Global.TERMINALES_ASOCIADAS);
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

    /**
     * Metodo utilizado para llenar el recycler view de las terminales asociadas
     * es invocado en el método que consume el servicio
     *
     * @Params Recibe la lista de terminales asociadas que van a ser mostradas
     **/
    public void llenarRVAsociadas(List<Terminal> terminalesRecibidas) {
        if (terminalesRecibidas == null || terminalesRecibidas.size() == 0) {
            Toast.makeText(objeto, Global.CODE + " No tiene terminales asociadas", Toast.LENGTH_SHORT).show();
            return;
        } else {

            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
            rv.setLayoutManager(llm);

            ArrayList terminals = new ArrayList<>();

            for (Terminal ter : terminalesRecibidas) {
                if (ter != null) {
                    terminals.add(ter);//  butons.add(new ButtonCard(nombre, "","",icon,idVenta));
                }
            }


            final AdapterTerminal_asociada adapter = new AdapterTerminal_asociada(terminals, new AdapterTerminal_asociada.interfaceClick() {//seria termi asoc
                @Override
                public void onClick(List<Terminal> terminal, int position) {


                    serialObtenido = terminal.get(position).getTerm_serial();
                    Global.modelo = terminal.get(position).getTerm_model();

                    objeto.listarObservacionesTerminal(serialObtenido);
                }
            }, R.layout.panel_terminal_asociada);

            rv.setAdapter(adapter);
        }
    }


    /**
     * Servicio para listar terminales autorizadas
     **/
    public void consumirServicioAutorizadas() {
        llenarRVAutorizada(Global.TERMINALES_AUTORIZADAS);

    }

    public void llenarRVAutorizada(List<Terminal> terminalesRecibidas) {
        if (terminalesRecibidas == null || terminalesRecibidas.size() == 0) {
            Toast.makeText(objeto, Global.CODE + " No tiene terminales autorizadas", Toast.LENGTH_SHORT).show();
            return;
        } //else {
            Vector<Terminal> terminales_aut = new Vector<>();
            for (Terminal ter : terminalesRecibidas) {
                if ((ter.getTerm_status()).equalsIgnoreCase("Autorizada")) {
                    terminales_aut.add(ter);
                }

            }
            rv.setAdapter(new AdapterTerminal(objeto, terminales_aut));//le pasa los datos-> lista de usuarios

            objeto.layoutManager = new LinearLayoutManager(objeto);// en forma de lista
           rv.setLayoutManager(objeto.layoutManager);
           /* rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
            rv.setLayoutManager(llm);

            ArrayList terminals = new ArrayList<>();

            for (Terminal ter : terminalesRecibidas) {
                if (ter != null) {
                    terminals.add(ter);
                }
            }

        //cambiar adaptador de autorizadas
            final AdapterTerminal_asociada adapter = new AdapterTerminal_asociada(terminals, new AdapterTerminal_asociada.interfaceClick() {//seria termi asoc
                @Override
                public void onClick(List<Terminal> terminal, int position) {


                    *//*serialObtenido = terminal.get(position).getTerm_serial();
                    Global.modelo = terminal.get(position).getTerm_model();

                    objeto.listarObservacionesTerminal(serialObtenido);*//*
                }
            }, R.layout.panel_terminal_asociada);//se debe crear un panel para las autorizadas, por el borde es de otro color

            rv.setAdapter(adapter);*/
        //}
    }


}
