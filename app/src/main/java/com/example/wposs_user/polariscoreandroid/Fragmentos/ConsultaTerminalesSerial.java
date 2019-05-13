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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminalStock;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ConsultaTerminalesSerial extends Fragment {


    private EditText serial;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView btn_buscar_terminales_serial;
    private Button btn_ser_rango_fechas;

    private RequestQueue queue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_consultar_terminales_serial, container, false);
        rv = (RecyclerView) view.findViewById(R.id.recycler_view_consultaTerminales_por_serial);
        serial = (EditText) view.findViewById(R.id.serial_buscar);
        queue = Volley.newRequestQueue(objeto);
        btn_buscar_terminales_serial = (ImageView) view.findViewById(R.id.btn_buscar_terminales_serial);
        btn_ser_rango_fechas = (Button) view.findViewById(R.id.btn_ser_rango_fechas);


        btn_ser_rango_fechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesFechas()).addToBackStack(null).commit();
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


        for (Terminal ter : Global.TODAS_TERMINALES) {
            if (ter.getTerm_serial().equalsIgnoreCase(serial.getText().toString())) {
                terminal.add(ter);
                rv.setAdapter(new AdapterTerminal(objeto, terminal));//le pasa los datos-> lista de usuarios
                layoutManager = new LinearLayoutManager(objeto);// en forma de lista
                rv.setLayoutManager(layoutManager);

            }
        }
        if (terminal.size() == 0) {
            Toast.makeText(objeto, "No se encontraron terminales registradas con ese serial", Toast.LENGTH_SHORT).show();
            rv.setAdapter(new AdapterTerminal(objeto, terminal));//le pasa los datos-> lista de usuarios
            layoutManager = new LinearLayoutManager(objeto);// en forma de lista
            rv.setLayoutManager(layoutManager);
        }
        serial.setText("");
    }



    /********************************metodo usaddo para mostrar en stock las terminales asignadas a un tecnico****************/

    public void servicioTerminales(){
        Global.TODAS_TERMINALES=null;
        Global.TODAS_TERMINALES=new ArrayList<Terminal>();
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
                            System.out.println("status:  " + Global.STATUS_SERVICE);

                            if (Global.STATUS_SERVICE.equals("fail")) {
                                Global.mensaje = response.get("message").toString();
                                Toast.makeText(objeto,"Error:  "+Global.mensaje,Toast.LENGTH_SHORT).show();
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray1 = response.getJSONArray("terminales");


                            if (jsonArray1.length() == 0) {
                                Global.mensaje = "No hay terminales ";
                                Toast.makeText(objeto,Global.mensaje,Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String ter = null;

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                ter = jsonArray1.getString(i);

                                Terminal t = gson.fromJson(ter, Terminal.class);
                                if (t != null) {
                                }
                                //terminales.add(t);
                                Global.TODAS_TERMINALES.add(t);
                            }


                           /* rv.setHasFixedSize(true);
                            LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
                            rv.setLayoutManager(llm);
                            AdapterTerminalStock adapter= new AdapterTerminalStock(v.getContext(),terminales);
                            rv.setAdapter(adapter);
*/



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
}
