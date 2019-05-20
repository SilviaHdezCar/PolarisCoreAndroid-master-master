package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
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
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal_asociada;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
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


public class ConsultaTerminalesSerial extends Fragment {


    private EditText serial;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView btn_buscar_terminales_serial;
    private Button btn_ser_rango_fechas;
    private View view;
    private static Observacion o;

    private RequestQueue queue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Global.soloConsulta = "si";
        view = inflater.inflate(R.layout.fragment_consultar_terminales_serial, container, false);
        objeto.setTitle("       BÚSQUEDA POR SERIAL");
        queue = Volley.newRequestQueue(objeto);
        Global.TODAS_TERMINALES = null;
        Global.TODAS_TERMINALES = new ArrayList<Terminal>();
        servicioTerminales();

        rv = (RecyclerView) view.findViewById(R.id.recycler_view_consultaTerminales_por_serial);
        serial = (EditText) view.findViewById(R.id.serial_buscar);


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

    //*******************************BUSCA POR SERIAL Y LLENA EL RV
    public void buscarTerminalesPorSerial() {

       /* Vector<Terminal> terminal = new Vector<>();
        terminal.removeAllElements();
*/
        if (serial.getText().toString().isEmpty()) {
            Toast.makeText(objeto, "Por favor ingrese el serial", Toast.LENGTH_SHORT).show();
            return;
        } else {
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
            rv.setLayoutManager(llm);

            ArrayList terminals = new ArrayList<>();

            for (Terminal ter : Global.TODAS_TERMINALES) {
                if (ter.getTerm_serial().equalsIgnoreCase(serial.getText().toString())) {
                    terminals.add(ter);//  butons.add(new ButtonCard(nombre, "","",icon,idVenta));
                }
            }
            if (terminals.size() == 0) {
                Toast.makeText(objeto, "No se encontraron terminales registradas con ese serial", Toast.LENGTH_SHORT).show();
            }


            final AdapterTerminal_asociada adapter = new AdapterTerminal_asociada(terminals, new AdapterTerminal_asociada.interfaceClick() {//seria termi asoc
                @Override
                public void onClick(List<Terminal> terminal, int position) {

                    Global.serial_ter = terminal.get(position).getTerm_serial();
                    Global.modelo = terminal.get(position).getTerm_model();

                    Global.terminalVisualizar = terminal.get(position);
                    //System.out.println("");

                  //  objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new EtapasTerminalAutorizada()).addToBackStack(null).commit();

                    consumirServicioEtapas();


                    //   objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();


                    //objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new EtapasTerminal()).addToBackStack(null).commit();
                    // objeto.listarObservacionesTerminal(serialObtenido);
                }
            }, R.layout.panel_terminal_asociada);

            rv.setAdapter(adapter);

            serial.setText("");


        }


    }


    /**
     * Metodo utilizados para consumir el servicio  para listar las observaciones de acuerdo a una terminal mediante una petición REST
     * En el encabezado va el token-> Authenticator
     * Se envía el serial de la terminal  Global.serial_ter
     **/
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
                            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            response = new JSONObject(response.get("data").toString());

                            JSONArray jsonArray = response.getJSONArray("observaciones");

                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No tiene obervaciones";
                                Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_SHORT).show();
                                //   consumirServicioValidaciones();
                                //  objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();
                                return;
                            } else {

                                String obser = null;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    obser = jsonArray.getString(i);

                                    o = gson.fromJson(obser, Observacion.class);

                                    Global.OBSERVACIONES.add(o);
                                }
                                // Toast.makeText(objeto, "Tiene observaciones", Toast.LENGTH_SHORT).show();
                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new EtapasTerminalAutorizada()).addToBackStack(null).commit();
                                //consumirServicioValidaciones();
                            }

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

    /********************************metodo usaddo para mostrar en stock las terminales asignadas a un tecnico****************/

    public void servicioTerminales() {

        Global.TODAS_TERMINALES = null;
        Global.TODAS_TERMINALES = new ArrayList<Terminal>();
        final ArrayList<Terminal> terminales = new ArrayList<>();
        final ArrayList<Repuesto> rep = new ArrayList<>();
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

                                if (response.get("message").toString().equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray1 = response.getJSONArray("terminales");


                            if (jsonArray1.length() == 0) {
                                Global.mensaje = "No hay terminales registradas";
                                Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                String ter = null;

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    ter = jsonArray1.getString(i);

                                    Terminal t = gson.fromJson(ter, Terminal.class);
                                /*if (t != null) {
                                }*/
                                    //terminales.add(t);
                                    Global.TODAS_TERMINALES.add(t);
                                }
                            }
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
