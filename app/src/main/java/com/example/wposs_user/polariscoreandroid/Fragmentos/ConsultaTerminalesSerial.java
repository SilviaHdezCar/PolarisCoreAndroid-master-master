package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.wposs_user.polariscoreandroid.java.Validacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static java.util.Collections.sort;


public class ConsultaTerminalesSerial extends Fragment {


    private EditText serial;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView btn_buscar_terminales_serial;
    private Button btn_ser_rango_fechas;
    private View view;
    private static Observacion o;

    private RequestQueue queue;
    private String serial_terminal;

    private Terminal terminal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Global.soloConsulta = "si";
        view = inflater.inflate(R.layout.fragment_consultar_terminales_serial, container, false);
        // objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("BÚSQUEDA POR SERIAL");
        queue = Volley.newRequestQueue(objeto);
        //servicioTerminales();

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
                serial_terminal = serial.getText().toString().trim();
                if (serial_terminal.isEmpty()) {
                    Toast.makeText(objeto, "Por favor ingrese el serial", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    servicioBuscarTerminal();
                }
            }
        });


        return view;


    }


    /**
     * Metodo utilizado para consultar los diagnosticos de una terminal
     * devuelve:
     * {
     * "message": "success",
     * "status": "ok",
     * "terminal": [],    //contiene la información de la terminal
     * "validaciones": [],
     * "tipificaciones": [],
     * "repuestos": [],
     * "observaciones": []
     * }
     */
    public void servicioBuscarTerminal() {

        this.terminal = null;
        this.terminal = new Terminal();
        Global.OBSERVACIONES = null;
        Global.OBSERVACIONES = new ArrayList<Observacion>();
        Global.validaciones_qa = false;
        Global.validaciones_consultas = new HashMap<String, String>();
        Global.tipificaciones_consultas = new HashMap<String, String>();
        Global.repuestos_consultas = new HashMap<String, String>();

        final Gson gson = new GsonBuilder().create();
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/findserial";
        JSONObject jsonObject = new JSONObject();
        serial_terminal = serial.getText().toString().trim();
        try {
            jsonObject.put("serial", serial_terminal.toUpperCase());
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

                            System.out.println("REPUESTA SERVIDOR:  " + response.toString());
                            if (response.get("status").toString().equals("fail")) {
                                Global.mensaje = response.get("message").toString();

                                if (response.get("message").toString().equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String info = null;
                            Validacion validaciones = null;

                            //obtener el arreglo de la etiqueta Terminal
                            JSONArray jsonArray = response.getJSONArray("terminal");//
                            if (jsonArray.length() == 0) {
                                Global.mensaje = "Terminal no encontrada";
                                rv.setVisibility(View.GONE);
                                Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    info = jsonArray.getString(i);

                                    terminal = gson.fromJson(info, Terminal.class);
                                    System.out.println("TERMINAL<<<>>>>>>>" + terminal.toString());
                                }
                            }

                            //obtener arreglo de la etiqueta validaciones
                            jsonArray = response.getJSONArray("validaciones");//
                            String estado = "";
                            String arreglo = "";
                            String fecha = "";
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //  info = jsonArray.getString(i);
                                    estado = jsonArray.getJSONObject(i).getString("tevs_status").toString();
                                    arreglo = jsonArray.getJSONObject(i).getString("tevs_terminal_validation").toString();
                                    fecha = jsonArray.getJSONObject(i).getString("tevs_date").toString();

                                    Global.validaciones_consultas.put(estado, fecha + "%" + arreglo);
                                    System.out.println("Validaciones: " + estado + " - " + fecha + "%" + arreglo);
                                    if (estado.equals("3")) {
                                        Global.validaciones_qa = true;
                                    }
                                }
                            }

                            //Tipificaciones
                            jsonArray = response.getJSONArray("tipificaciones");//
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //  info = jsonArray.getString(i);
                                    estado = jsonArray.getJSONObject(i).getString("tets_status").toString();
                                    arreglo = jsonArray.getJSONObject(i).getString("tets_terminal_type_validation").toString();
                                    fecha = jsonArray.getJSONObject(i).getString("tets_date").toString();

                                    Global.tipificaciones_consultas.put(estado, fecha + "%" + arreglo);
                                    System.out.println("Tipificaciones:  " + estado + " - " + fecha + "%" + arreglo);
                                }
                            }

                            //Repuestos
                            jsonArray = response.getJSONArray("repuestos");//
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //  info = jsonArray.getString(i);
                                    estado = jsonArray.getJSONObject(i).getString("tesw_status").toString();
                                    arreglo = jsonArray.getJSONObject(i).getString("tesw_repuestos").toString();
                                    fecha = jsonArray.getJSONObject(i).getString("tesw_date").toString();

                                    if (arreglo != null || !arreglo.trim().isEmpty()) {
                                        if (arreglo != null) {
                                            if (!arreglo.isEmpty()) {
                                                Global.repuestos_consultas.put(estado, fecha + "%" + arreglo);
                                                System.out.println("Repuestos: " + estado + " - " + fecha + "%" + arreglo);
                                            }
                                        }
                                    }
                                }
                            }

                            //observaciones
                            jsonArray = response.getJSONArray("observaciones");//
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    info = jsonArray.getString(i);
                                    o = gson.fromJson(info, Observacion.class);
                                    Global.OBSERVACIONES.add(o);
                                }

                            }

                            llernarRV();
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
                            if (!error.getMessage().isEmpty()) {
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

    //*******************************BUSCA POR SERIAL Y LLENA EL RV
    public void llernarRV() {
        rv.setVisibility(View.VISIBLE);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final ArrayList terminals = new ArrayList<>();

     /*   for (Terminal ter : Global.TODAS_TERMINALES) {
            if (ter.getTerm_serial().equalsIgnoreCase(serial.getText().toString())) {
                terminals.add(ter);//  butons.add(new ButtonCard(nombre, "","",icon,idVenta));
            }
        }*/
        if (this.terminal != null) {
            terminals.add(this.terminal);
        }
        if (terminals.size() == 0) {
            Toast.makeText(objeto, "No se encontraron terminales registradas con ese serial", Toast.LENGTH_SHORT).show();

        }


        final AdapterTerminal_asociada adapter = new AdapterTerminal_asociada(terminals, new AdapterTerminal_asociada.interfaceClick() {//seria termi asoc
            @Override
            public void onClick(List<Terminal> terminal, int position) {
                if (Global.validaciones_consultas.size()==0
                        &&Global.tipificaciones_consultas.size()==0
                        &&Global.repuestos_consultas.size()==0
                        &&Global.OBSERVACIONES.size()==0){
                    return;
                }
                    if (!terminal.get(position).getTerm_status().equalsIgnoreCase("NUEVO")) {
                        Global.serial_ter = terminal.get(position).getTerm_serial();
                        Global.modelo = terminal.get(position).getTerm_model();
                        Global.terminalVisualizar = terminal.get(position);
                        Global.observaciones_con_fotos = null;
                        Global.observaciones_con_fotos = new ArrayList<>();
                        Global.fotos = new ArrayList<>();
                        objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new Prediagnostico()).addToBackStack(null).commit();
                    }


            }
        }, R.layout.panel_terminal_asociada);

        rv.setAdapter(adapter);

        serial.setText("");

    }
}
