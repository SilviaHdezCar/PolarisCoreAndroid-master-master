package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterRepuesto;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.TerminalHistory;
import com.example.wposs_user.polariscoreandroid.java.Validacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class Registro_diagnostico extends Fragment {

    private View v;
    private EditText cantidad_req;
    private Spinner aut_repuesto;
    private RecyclerView rv;
    private Button agregar;
    private EditText observ;
    private Button registroDiag;
    android.support.v4.app.FragmentManager fragmentManager;
    private RequestQueue queue;
    private LinearLayout linea;
    private int fallas = 0;
    private ArrayList<TerminalHistory>thistory;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_registro_diagnostico, container, false);
       // objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("REGISTRAR DIAGNÓSTICO");
        Global.REPUESTOS = new ArrayList<>();
        Global.REPUESTOS_DIAGONOSTICO = new ArrayList<Repuesto>();
        Global.obs = new Observacion("", "", "", "", "  ", Global.serial_ter);
        aut_repuesto = (Spinner) v.findViewById(R.id.auto_repuesto);
        cantidad_req = v.findViewById(R.id.txt_cantReq);
        rv = (RecyclerView) v.findViewById(R.id.rv_repuestos_diag);
        agregar = (Button) v.findViewById(R.id.bton_agregarRepuesto);
        observ = (EditText) v.findViewById(R.id.txt_observaciones);
        registroDiag = (Button) v.findViewById(R.id.btn_registroDioagnostico);
        linea = (LinearLayout) v.findViewById(R.id.panel_repuestos);
        queue = Volley.newRequestQueue(objeto);
        boolean b = this.validarDanio();
        if (b) {
            linea.setVisibility(View.GONE);
        } else {
            linea.setVisibility(View.VISIBLE);
        }


        this.consumirServicioRepuestos();

        cantidad_req.setEnabled(false);

        aut_repuesto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] spares = convertirRepuestos();
                String r= spares[position];

                   if(!r.equals("Seleccione")){
                    cantidad_req.setEnabled(true);
                    String [] selecionado =r.split(" - ");
                    Global.codigo_rep= selecionado[0].trim();
                   }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registroDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarDiagnostico();

            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarRepuesto();
            }
        });


        return v;
    }


    public String[] convertirRepuestos() {

        String[] rep = new String[Global.REPUESTOS.size()+1];

        rep[0] = "Seleccione";

        for (int i = 0; i < Global.REPUESTOS.size(); i++) {

            rep[i+1] = Global.REPUESTOS.get(i).getSpar_code() + "  -  " + Global.REPUESTOS.get(i).getSpar_name();

        }
        return rep;

    }


    public void llenarAutocomplete() {

        final String[] rep = this.convertirRepuestos();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), R.layout.spinner_sytle, rep);

        aut_repuesto.setAdapter(adapter);
          }


    public void agregarRepuesto() {

        String cant = cantidad_req.getText().toString();


        if (Global.REPUESTOS.size() == 1) {

        Toast.makeText(objeto,"No existen repuestos seleccionados para el modelo de terminal seleccionado",Toast.LENGTH_SHORT).show();
            agregar.setEnabled(false);
            registroDiag.setEnabled(false);
            return;
        }

        if (cant.isEmpty()) {
            Toast.makeText(objeto, "Debe ingresar una cantidad válida", Toast.LENGTH_SHORT).show();
            Global.codigo_rep = "";
            return;
        }

        int cant_solicitada = Integer.parseInt(cant);

        if (cant_solicitada == 0) {

            Toast.makeText(objeto, "Debe solicitar como minimo 1 repuesto", Toast.LENGTH_SHORT).show();
            cantidad_req.setText("");
            Global.codigo_rep = "";

            return;

        }



        if (cant_solicitada >3) {

            Toast.makeText(objeto, "Debe solicitar como máximo 3 repuestos", Toast.LENGTH_SHORT).show();
            cantidad_req.setText("");
            return;


        }



        for (int i = 0; i < Global.REPUESTOS.size(); i++) {
            System.out.println("codigo del repuesto selecionado**"+Global.codigo_rep);
            if (Global.REPUESTOS.get(i).getSpar_code().equals(Global.codigo_rep)) {

                Repuesto reps = Global.REPUESTOS.get(i);
                System.out.println("");

                for (Repuesto rt : Global.REPUESTOS_DIAGONOSTICO) {
                    if (reps.getSpar_code().equals(rt.getSpar_code())) {


                        String total= String.valueOf(Integer.parseInt(rt.getSpar_quantity())+cant_solicitada);

                        if (Integer.parseInt(total)>3) {

                            Toast.makeText(objeto, "Solo puede solicitar como máximo 3 repuestos", Toast.LENGTH_SHORT).show();
                            Global.codigo_rep = "";
                            cantidad_req.setText("");
                            aut_repuesto.setSelection(0);
                             this.cantidad_req.setEnabled(false);
                            return;

                        }


                        Global.REPUESTOS_DIAGONOSTICO.remove(rt);
                        reps.setSpar_quantity(total);
                        Global.REPUESTOS_DIAGONOSTICO.add(reps);
                        this.llenarRv();
                        cantidad_req.setText("");
                        this.cantidad_req.setEnabled(false);
                        aut_repuesto.setSelection(0);
                        return;

                    }


                }
                reps.setSpar_quantity(String.valueOf(cant_solicitada));
                Global.REPUESTOS_DIAGONOSTICO.add(reps);
                this.llenarRv();
                cantidad_req.setText("");
                aut_repuesto.setSelection(0);
                this.cantidad_req.setEnabled(false);
                return;

            }

        }

    }


    private void llenarRv() {
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final AdapterRepuesto adapter = new AdapterRepuesto(Global.REPUESTOS_DIAGONOSTICO, new AdapterRepuesto.interfaceClick() {//seria termi asoc

            public void onClick(List<Repuesto> repuesto, int position) {

                Global.REPUESTOS_DIAGONOSTICO.remove(position);
                llenarRv();
            }
        }, R.layout.panel_agregarep);

        rv.setAdapter(adapter);

    }


    /***********************Metodo utilizado para Registrar el Diagnostico de una terminal*********************/


    public void registrarDiagnostico() {

        String descripicionObserv = observ.getText().toString();


        if (Global.REPUESTOS_DIAGONOSTICO.size() == 0 && descripicionObserv.trim().isEmpty()) {

            Toast.makeText(objeto, "Debe agregar como mínimo un repuesto o una observación", Toast.LENGTH_SHORT).show();
            return;

        }


        if (Global.REPUESTOS_DIAGONOSTICO.size() == 0) {

            if (descripicionObserv.isEmpty()) {
                Toast.makeText(objeto, "Debe agregar al menos un repuesto", Toast.LENGTH_SHORT).show();
                return;
            }


            Observacion obser = new Observacion("", descripicionObserv, "", "", " ", Global.serial_ter);
            Global.obs = obser;

            this.obtenerHistorialTerminal("asociada");
            return;

        }

        Observacion ob = new Observacion("", descripicionObserv, " ", " ", " ", Global.serial_ter);
        Global.obs = ob;


        if (Global.diagnosticoTerminal.equalsIgnoreCase("autorizada")) {//consume el servicio: FINALIZAR REGISTRO DE REPARACIÓN POR NUEVO DIAGNÓSTICO:

           obtenerHistorialTerminal("autorizada");


        } else {
            obtenerHistorialTerminal("asociada");;//Consume el servicio cuando la terminal es asociada
        }


       /* Intent i = new Intent(v.getContext(), MainActivity.class); // inicio una nueva activiy
        getFragmentManager().beginTransaction().remove(this).commit(); /// remuevo el fragment usado
         startActivity(i);*/
    }


    /**
     * Metodo utilizados para consumir el servicio  de registrar un diagnostico de una terminal autorizada
     * En el encabezado va el token-> Authenticator
     * lista de validaciones, tipificaciones, esReparable?, observacion, tipo de falla y una lista de repuestos
     **/
    public void consumirServicioDiagnosticoAutorizada() {


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/saveNewDiagnosis";
        JSONObject jsonObject = new JSONObject();
        final JSONObject obj2 = new JSONObject();
        try {

            JSONArray val = this.getValidaciones();
            jsonObject.put("validaciones", val);
            JSONArray tip = this.getTipificaciones();
            jsonObject.put("tipificaciones", tip);
            jsonObject.put("observacion", Global.obs.getObjRep());
            obj2.put("tesw_serial", Global.serial_ter);
            JSONArray o = this.getRepuestos();
            obj2.put("tesw_repuestos", o);
            jsonObject.put("repuestos", obj2);


            Log.d("RESPUESTA", jsonObject.toString());


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
                            Log.d("RESPUESTA", response.get("message").toString());

                            if (Global.STATUS_SERVICE.equals("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "Error: " + response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                eliminarPila();
                                objeto.CustomAlertDialog(objeto, "Información", "Diagnóstico registrado exitosamente", 3000, false);
                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
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


    /**
     * Metodo utilizados para consumir el servicio  de registrar un diagnostico de una terminal asociada
     * En el encabezado va el token-> Authenticator
     * lista de validaciones, tipificaciones, esReparable?, observacion, tipo de falla y una lista de repuestos
     **/
    public void consumirServicioDiagnostico() {


        final Gson gson = new GsonBuilder().create();


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/savediagnosis";
        JSONObject jsonObject = new JSONObject();
        final JSONObject obj2 = new JSONObject();
        try {

            JSONArray val = this.getValidaciones();
            jsonObject.put("validaciones", val);
            JSONArray tip = this.getTipificaciones();
            jsonObject.put("tipificaciones", tip);
            jsonObject.put("reparable", Global.reparable);
            jsonObject.put("observacion", Global.obs.getObj());
            jsonObject.put("falla", Global.fallaDetectada);
            obj2.put("tesw_serial", Global.serial_ter);
            JSONArray o = this.getRepuestos();
            obj2.put("tesw_repuestos", o);
            jsonObject.put("repuestos", obj2);


            Log.d("RESPUESTA", jsonObject.toString());


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
                            Log.d("RESPUESTA", response.get("message").toString());

                            if (Global.STATUS_SERVICE.equals("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }

                                Toast.makeText(objeto, "Error: " + response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                return;

                            } else {

                                eliminarPila();
                                objeto.CustomAlertDialog(objeto,"Información", "Diagnóstico registrado exitosamente",3000,true);
                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
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




    /**
     * servicio para actualizar el historial de una terminal
     **/
    public void servicioActualizarTerminal(final String est, final String tipo) {


        String estado="";

        Date date = new Date();

        if(Global.fallaDetectada.equals("USO") && tipo.equals("asociada")){

            estado= "COTIZACIÓN";

        }

        if(Global.fallaDetectada.equals("FABRICA")&& tipo.equals("asociada")){

            estado="GARANTÍA";

        }

        if(tipo.equals("autorizada")){

            estado="REPARACIÓN";

        }


        JSONObject jsonObject3= new JSONObject();

        try {
            jsonObject3.put("term_location",Global.CODE);
            jsonObject3.put("term_state",estado);
            jsonObject3.put("date",date.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Gson gson = new GsonBuilder().create();


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/updateHistoryTerminal";
        JSONObject jsonObject = new JSONObject();

        try {


            jsonObject.put("terminal_history", jsonObject3);
            jsonObject.put("tehi_serial",Global.serial_ter);



            Log.d("RESPUESTA", jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        System.out.println("RESPUESTA DEL SERVICIO ACTUALIZAR HISTORIAL**"+response.toString());

                        try {
                            Global.STATUS_SERVICE = response.get("status").toString();
                            Log.d("RESPUESTA", response.get("message").toString());

                            if (Global.STATUS_SERVICE.equals("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }

                                Toast.makeText(objeto, "Error: " + response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                return;

                            } else {

                                if(est.equals("no")&&tipo.equals("asociada")){

                                    consumirServicioDiagnostico();
                                    return;

                                }


                                if(est.equals("si")&&tipo.equals("asociada")){

                                    consumirServicioGestionarTerminal("asociada");
                                    return;

                                }

                                if(est.equals("no")&&tipo.equals("autorizada")){

                                    consumirServicioDiagnosticoAutorizada();
                                    return;

                                }


                                if(est.equals("si")&&tipo.equals("autorizada")){

                                    consumirServicioGestionarTerminal("autorizada");
                                    return;

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
                params.put("Id",Global.serial_ter);

                return params;
            }
        };

        queue.add(jsArrayRequest);

    }



    /**
     * servicio para obtener el historial de una terminal
     **/
    public void obtenerHistorialTerminal(final String tipoDiag) {


        thistory= new ArrayList<>();


        final Gson gson = new GsonBuilder().create();


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/getHistoryTerminal";
        JSONObject jsonObject = new JSONObject();


        Log.d("RESPUESTA", jsonObject.toString());


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("REPUESTA DEL SERVICIO OBTENER TERMINAL**"+response.toString());
                        try {

                            Log.d("RESPUESTA", response.get("message").toString());

                            if (response.get("message").equals("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }

                                Toast.makeText(objeto, "Error: " + response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                return;

                            } else {

                                response= response.getJSONObject("data");

                                if(response.toString().equals("")){


                                    if(tipoDiag.equals("asociada")){
                                        servicioActualizarTerminal("si","asociada");
                                        return;
                                    }


                                    if(tipoDiag.equals("autorizada")){
                                        servicioActualizarTerminal("si","autorizada");
                                        return;
                                    }

                                }
                               String rta = response.getString("tehi_historial");



                                JSONArray jsonArray= new JSONArray(rta);


                                if (jsonArray.length() == 1) {




                                    if(tipoDiag.equals("asociada")){
                                        servicioActualizarTerminal("si","asociada");
                                        return;
                                    }


                                    if(tipoDiag.equals("autorizada")){
                                        servicioActualizarTerminal("si","autorizada");
                                        return;
                                    }

                               }


                                String rep = null;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    rep = jsonArray.getString(i);

                                    String mensj= eliminarComillas(rep);
                                    String[]history= mensj.split(",");

                                    TerminalHistory t= new TerminalHistory(history[0],history[1],history[2]);

                                    thistory.add(t);

                               }

                                TerminalHistory term= thistory.get(thistory.size()-2);

                                String codTec= term.getTerm_location();
                                String statusF= term.getTerm_state();

                                if(codTec.equals(Global.CODE)){

                                    if(tipoDiag.equals("asociada")) {

                                        if (statusF.equals("DIAGNÓSTICO") || statusF.equals("COTIZACIÓN") || statusF.equals("GARANTÍA")) {
                                            servicioActualizarTerminal("no","asociada");
                                            return;
                                        }
                                    }

                                    if(tipoDiag.equals("autorizada")) {

                                        if (statusF.equals("REPARACIÓN")) {
                                            servicioActualizarTerminal("no","autorizada");
                                            return;
                                        }
                                    }

                                }


                                servicioActualizarTerminal("si",tipoDiag);



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
                params.put("id",Global.serial_ter);

                return params;
            }
        };

        queue.add(jsArrayRequest);

    }


    /**
     * servicio para actualizar el historial de una terminal
     **/
    public void consumirServicioGestionarTerminal(final String tipo) {


        final Gson gson = new GsonBuilder().create();


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/incrementarGestionadas";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user",Global.CODE);
            if(tipo.equalsIgnoreCase("asociada")){
                jsonObject.put("tipo", "DIAGNÓSTICO");
            }else{
                jsonObject.put("tipo", "REPARACIÓN");
            }

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

                        System.out.println("REPUESTA DEL SERVICIO GESTIONAR TERMINAL**"+response.toString());
                        try {
                            Global.STATUS_SERVICE = response.get("status").toString();
                            Log.d("RESPUESTA", response.get("message").toString());

                            if (Global.STATUS_SERVICE.equals("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }

                                Toast.makeText(objeto, "Error: " + response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                return;

                            } else {


                                if(tipo.equals("asociada")) {
                                    consumirServicioDiagnostico();
                                }

                                if(tipo.equals("autorizada")) {
                                    consumirServicioDiagnosticoAutorizada();
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






    /**
     * Metodo utilizados para consumir el servicio  de listar los respuestos asociados aun modelo de terminal
     * En el encabezado va el token-> Authenticator
     * Se envía el codigo del usuario  Global.CODE
     **/

    private static Repuesto r;

    public void consumirServicioRepuestos() {
        Global.REPUESTOS_DIAGONOSTICO = new ArrayList<Repuesto>();
        r = null;
        Global.REPUESTOS = null;
        Global.REPUESTOS = new ArrayList<Repuesto>();
        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/spares ";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
            jsonObject.put("model", Global.modelo);
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
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray = response.getJSONArray("repuestos");


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No existen repuestos disponibles para el modelo de serial seleccionado";
                                Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String rep = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                rep = jsonArray.getString(i);


                                r = gson.fromJson(rep, Repuesto.class);

                                if (r != null) {

                                    if (r.getSpar_quantity().equals("NaN")) {
                                        r.setSpar_quantity("0");

                                    }

                                    if (Integer.parseInt(r.getSpar_quantity()) > 0) {
                                        Global.REPUESTOS.add(r);
                                    }
                                }
                            }
                            llenarAutocomplete();
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


    public JSONArray getRepuestos() throws JSONException {

        JSONArray listas = new JSONArray();

        for (int i = 0; i < Global.REPUESTOS_DIAGONOSTICO.size(); i++) {
            JSONObject ob = Global.REPUESTOS_DIAGONOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }


    public JSONArray getValidaciones() throws JSONException {

        JSONArray listas = new JSONArray();

        for (int i = 0; i < Global.VALIDACIONES_DIAGNOSTICO.size(); i++) {
            JSONObject ob = Global.VALIDACIONES_DIAGNOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

    public JSONArray getTipificaciones() throws JSONException {
        JSONArray listas = new JSONArray();

        for (int i = 0; i < Global.TIPIFICACIONES_DIAGNOSTICO.size(); i++) {
            JSONObject ob = Global.TIPIFICACIONES_DIAGNOSTICO.get(i).getObj();
            listas.put(ob);
        }
        return listas;
    }

    public void eliminarPila() {


        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

    }

    public boolean validarDanio() {


        for (Validacion va : Global.VALIDACIONES_DIAGNOSTICO) {
            System.out.println(va.getTeva_description() + "-" + va.getEstado());
            if (va.getEstado().equals("falla")) {
                fallas++;
            }

        }

        System.out.println("TOTAL FALLAS             " + fallas);

        if (fallas == 1) {

            for (Validacion va : Global.VALIDACIONES_DIAGNOSTICO) {
                System.out.println(va.getTeva_description() + "-" + va.getEstado());
                if (va.getEstado().equals("falla") && va.getTeva_description().equals("SOFTWARE")) {
                    return true;
                } else if (!va.getEstado().equals("falla") && va.getTeva_description().equals("SOFTWARE")) {
                    return false;

                }
            }


        }


        return false;
    }



    public String eliminarComillas(String mens){

        char[] texto= mens.toCharArray();

            for(int i=0;i<texto.length;i++){

                if(texto[i] == '{'){

                    texto[i]= ' ';
                }

                if(texto[i] == '"'){

                    texto[i]= ' ';
                }

            }


            String x= "";


            for(Character c:texto){
                x+=c;

            }

            return x;



    }

}
