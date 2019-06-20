package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterRepuestoStock;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminalStock;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal_asociada;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class StockFragment extends Fragment {


    private View v;
    private ImageView foto_perfil;
    private RequestQueue queue;
    private ArrayList<Terminal>terminales;
    private ArrayList<Repuesto>repuestos;
    Button term;
    Button rep;
    RecyclerView rv;
    private LinearLayout layoutTerminales, layoutRepuestos;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objeto.setTitulo("STOCK");
        terminales= new ArrayList<Terminal>();
        repuestos= new ArrayList<Repuesto>();
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_stock, container, false);
        queue = Volley.newRequestQueue(v.getContext());
        rv= (RecyclerView)v.findViewById(R.id.recycler_stock);
        term=(Button)v.findViewById(R.id.btn_terminales_stock);
        rep= (Button)v.findViewById(R.id.btn_repuesto_stock);
        layoutTerminales = (LinearLayout) v.findViewById(R.id.selectTerminales);
        layoutRepuestos = (LinearLayout) v.findViewById(R.id.selectRepuestos);
       servicioTerminalStock();

        layoutTerminales.setBackgroundColor(getResources().getColor(R.color.blanca_linea));
        layoutRepuestos.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminales= new ArrayList<Terminal>();
                repuestos= new ArrayList<Repuesto>();
                layoutTerminales.setBackgroundColor(getResources().getColor(R.color.blanca_linea));
                layoutRepuestos.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
                servicioTerminalStock();


            }
        });
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminales= new ArrayList<Terminal>();
                repuestos= new ArrayList<Repuesto>();
                layoutRepuestos.setBackgroundColor(getResources().getColor(R.color.blanca_linea));
                layoutTerminales.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
                servicioRpuestoStock();

            }
        });


        return v;

    }

    /********************************metodo usaddo para mostrar en stock las terminales asignadas a un tecnico****************/

    public void servicioTerminalStock(){
        final  ArrayList<Terminal>terminales= new ArrayList<>();
        final ArrayList<Repuesto>rep=new ArrayList<>();
        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/stock";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
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

                            if (Global.STATUS_SERVICE.equals("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "ERROR: "+Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray1 = response.getJSONArray("terminales");


                            if (jsonArray1.length() == 0) {
                                Global.mensaje = "No tiene terminales ";
                                Toast.makeText(v.getContext(),Global.mensaje,Toast.LENGTH_SHORT).show();

                            }

                            else {

                                String ter = null;

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    ter = jsonArray1.getString(i);

                                    Terminal t = gson.fromJson(ter, Terminal.class);
                                    if (t != null ) {
                                    }
                                    terminales.add(t);
                                }
                            }
                            rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
                            rv.setLayoutManager(llm);
                            AdapterTerminalStock adapter= new AdapterTerminalStock(v.getContext(),terminales);
                            rv.setAdapter(adapter);




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
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()){
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
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


    /***********metodo usaddo para mostrar en stock los repuestos asignados a un tecnico*****/

    private Repuesto r;

    public void servicioRpuestoStock(){
        final  ArrayList<Repuesto>repuestos= new ArrayList<>();
        final ArrayList<Repuesto>rep=new ArrayList<>();
        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/stock";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
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

                            if (Global.STATUS_SERVICE.equals("fail")) {
                                Global.mensaje = response.get("message").toString();

                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "ERROR: "+Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray1 = response.getJSONArray("repuestos");


                            if (jsonArray1.length() == 0) {
                                Global.mensaje = "No tiene repuestos ";
                                Toast.makeText(v.getContext(),Global.mensaje,Toast.LENGTH_SHORT).show();

                            }

                            else {

                                String rep = null;

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    rep = jsonArray1.getString(i);


                                    r = gson.fromJson(rep, Repuesto.class);


                                    Repuesto r = gson.fromJson(rep, Repuesto.class);
                                    if(r.getSpar_quantity().equals("NaN")){
                                        r.setSpar_quantity("0");

                                    }

                                    if (r != null && Integer.parseInt(r.getSpar_quantity())>0) {
                                        repuestos.add(r);
                                    }

                                }
                            }
                            

                            rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
                            rv.setLayoutManager(llm);
                            AdapterRepuestoStock adapter= new AdapterRepuestoStock(v.getContext(),repuestos);
                            rv.setAdapter(adapter);




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
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()){
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
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

}


