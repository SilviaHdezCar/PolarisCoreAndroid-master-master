package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class Registro_diagnostico extends Fragment {

    private View v;
    private EditText cantidad_req;
    private AutoCompleteTextView aut_repuesto;
    private RecyclerView rv;
    private Button agregar;
    private EditText observ;
    private Button registroDiag;
    android.support.v4.app.FragmentManager fragmentManager;
    private RequestQueue queue;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_registro_diagnostico, container, false);
        objeto.setTitle("REGISTRAR DIAGNÓSTICO");
        Global.REPUESTOS_DIAGONOSTICO = new ArrayList<Repuesto>();
        aut_repuesto = (AutoCompleteTextView) v.findViewById(R.id.auto_repuesto);
        cantidad_req = v.findViewById(R.id.txt_cantReq);
        rv = (RecyclerView) v.findViewById(R.id.rv_repuestos_diag);
        agregar = (Button) v.findViewById(R.id.bton_agregarRepuesto);
        observ = (EditText) v.findViewById(R.id.txt_observaciones);
        registroDiag = (Button) v.findViewById(R.id.btn_registroDioagnostico);
        queue = Volley.newRequestQueue(objeto);
        this.consumirServicioRepuestos();

         cantidad_req.setEnabled(false);

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

        String[] rep = new String[Global.REPUESTOS.size()];


        for (int i = 0; i < Global.REPUESTOS.size(); i++) {

            rep[i] = Global.REPUESTOS.get(i).getSpar_code() + "   " + Global.REPUESTOS.get(i).getSpar_name();

        }
        return rep;

    }


    public void llenarAutocomplete() {

        final String[] rep = this.convertirRepuestos();
        aut_repuesto = (AutoCompleteTextView) v.findViewById(R.id.auto_repuesto);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), R.layout.spinner_sytle, rep);

        aut_repuesto.setAdapter(adapter);
        aut_repuesto.setThreshold(1);
        aut_repuesto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String code = adapter.getItem(i);
                String[] repuest = code.split(" ");
                Global.codigo_rep = repuest[0];
                cantidad_req.setEnabled(true);

                System.out.println(" Codigo del repuesto seleccionado;" + Global.codigo_rep);
                InputMethodManager in = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });
        aut_repuesto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {

                    String code = adapter.getItem(i);
                    String[] repuest = code.split(" ");
                    Global.codigo_rep = repuest[0];
                    InputMethodManager in = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                    cantidad_req.setEnabled(true);
                    return true;
                }
                return false;
            }
        });

    }


    public void agregarRepuesto() {

        String cant = cantidad_req.getText().toString();


        if (Global.REPUESTOS.size() == 0) {

            observ.setText("No hay repuestos para el modelo de terminal seleccionado, seleccione otra e intentelo de nuevo");
            agregar.setEnabled(false);
            registroDiag.setEnabled(false);
            return;
        }

        if (cant.isEmpty()) {
            Toast.makeText(objeto, "Debe ingresar una cantidad valida", Toast.LENGTH_SHORT).show();
            Global.codigo_rep="";
            return;
        }

        int cant_solicitada = Integer.parseInt(cant);

        if (cant_solicitada == 0) {

            Toast.makeText(objeto, "Debe solicitar como minimo 1 repuesto", Toast.LENGTH_SHORT).show();
            Global.codigo_rep="";
            return;

        }


        for (int i = 0; i < Global.REPUESTOS.size(); i++) {

            if (Global.REPUESTOS.get(i).getSpar_code().equals(Global.codigo_rep)) {
                if (Global.REPUESTOS.get(i).getSpar_quantity() < cant_solicitada) {
                    Toast.makeText(objeto, "El repuesto seleccionado no tiene disponible la cantidad solicitada", Toast.LENGTH_SHORT).show();
                    return;
                }
                int cantidad_disponible_repuesto = Global.REPUESTOS.get(i).getSpar_quantity();
                Repuesto r = new Repuesto(Global.REPUESTOS.get(i).getSpar_code(), Global.REPUESTOS.get(i).getSpar_name(), cant_solicitada, Global.REPUESTOS.get(i).getSpar_warehouse());

                for (Repuesto rt : Global.REPUESTOS_DIAGONOSTICO) {
                    if (rt.getSpar_code().equals(r.getSpar_code())) {

                        if (cantidad_disponible_repuesto < rt.getSpar_quantity() + cant_solicitada) {
                            cantidad_req.setText("");
                            Toast.makeText(objeto, "El repuesto no cuenta con la cantidad disponible solicitada", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        Global.REPUESTOS_DIAGONOSTICO.remove(rt);
                        r.setSpar_quantity(rt.getSpar_quantity()+cant_solicitada);
                         Global.REPUESTOS_DIAGONOSTICO.add(r);
                        this.llenarRv();
                        cantidad_req.setText("");
                        aut_repuesto.setText("");
                        this.cantidad_req.setEnabled(false);
                        return;

                    }


                }
                Global.REPUESTOS_DIAGONOSTICO.add(r);
                 this.llenarRv();
                cantidad_req.setText("");
                aut_repuesto.setText("");
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

            public void onClick(List<Repuesto> terminal, int position) {

                Global.REPUESTOS_DIAGONOSTICO.remove(position);
                llenarRv();
            }
        }, R.layout.panel_agregarep);

        rv.setAdapter(adapter);

    }


    /***********************Metodo utilizado para Registrar el Diagnostico de una terminal*********************/


    public void registrarDiagnostico() {

        String descripicionObserv = observ.getText().toString();


        if(Global.REPUESTOS_DIAGONOSTICO.size()==0 && descripicionObserv.trim().isEmpty()){

            Toast.makeText(objeto, "Debe agregar como minimo un repuesto o una observación", Toast.LENGTH_SHORT).show();
            return;

        }


        if(Global.REPUESTOS_DIAGONOSTICO.size()==0){

            if(descripicionObserv.isEmpty()){
                Toast.makeText(objeto, "Debe agregar un repuesto al menos", Toast.LENGTH_SHORT).show();
                return;
            }



            Observacion obser = new Observacion("", descripicionObserv, "", "", "", Global.serial_ter);
            Global.obs = obser;

            this.consumirServicioDiagnostico();
            return;

        }

        Observacion ob= new Observacion("","","","","",Global.serial_ter);
        Global.obs=ob;


        if (Global.diagnosticoTerminal.equalsIgnoreCase("autorizada")) {//consume el servicio: FINALIZAR REGISTRO DE REPARACIÓN POR NUEVO DIAGNÓSTICO:
            consumirServicioDiagnosticoAutorizada();


        } else {
            consumirServicioDiagnostico();//Consume el servicio cuando la terminal es asociada
        }


       /* Intent i = new Intent(v.getContext(), MainActivity.class); // inicio una nueva activiy
        getFragmentManager().beginTransaction().remove(this).commit(); /// remuevo el fragment usado
         startActivity(i);*/
    }


    /**
     * Metodo utilizados para consumir el servicio  de registrar un diagnostico de una terminal asociada
     * En el encabezado va el token-> Authenticator
     * lista de validaciones, tipificaciones, esReparable?, observacion, tipo de falla y una lista de repuestos
     **/
    public void consumirServicioDiagnosticoAutorizada() {


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/saveNewDiagnosis";
        JSONObject jsonObject = new JSONObject();
        JSONObject obj2 = new JSONObject();
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

                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("INFORMACIÓN");
                                alertDialog.setMessage("Error: " + response.get("message").toString() + "\n");
                                alertDialog.setCancelable(true);

                                alertDialog.show();

                                return;

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("Informacion");
                                alertDialog.setMessage("Diagnóstico registrado exitosamente");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",

                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();

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


    /**
     * Metodo utilizados para consumir el servicio  de registrar un diagnostico de una terminal asociada
     * En el encabezado va el token-> Authenticator
     * lista de validaciones, tipificaciones, esReparable?, observacion, tipo de falla y una lista de repuestos
     **/
     public void consumirServicioDiagnostico() {



        final Gson gson = new GsonBuilder().create();


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/savediagnosis";
        System.out.println("enviando...........");
        JSONObject jsonObject = new JSONObject();
        JSONObject obj2 = new JSONObject();
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

                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("INFORMACIÓN");
                                alertDialog.setMessage("Error: " + response.get("message").toString() + "\n");
                                alertDialog.setCancelable(true);

                                alertDialog.show();

                                return;

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("Informacion");
                                alertDialog.setMessage("Diagnóstico registrado exitosamente");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",

                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();

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
                                Toast.makeText(v.getContext(),Global.mensaje,Toast.LENGTH_SHORT).show();;
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());


                            JSONArray jsonArray = response.getJSONArray("repuestos");


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No existen repuestos disponibles para el modelo de serial seleccionado";
                                Toast.makeText(v.getContext(),Global.mensaje,Toast.LENGTH_SHORT).show();;
                                return;
                            }
                            String rep = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                rep = jsonArray.getString(i);

                                r = gson.fromJson(rep, Repuesto.class);
                                if (r != null) {
                                }
                                Global.REPUESTOS.add(r);
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


    public JSONArray getRepuestos() throws JSONException {

        JSONArray listas = new JSONArray();

        for(int i= 0;i<Global.REPUESTOS_DIAGONOSTICO.size();i++) {
            JSONObject ob = Global.REPUESTOS_DIAGONOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

    public JSONArray getValidaciones() throws JSONException {

        JSONArray listas= new JSONArray();

        for(int i= 0;i<Global.VALIDACIONES_DIAGNOSTICO.size();i++) {
            JSONObject ob = Global.VALIDACIONES_DIAGNOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

    public JSONArray getTipificaciones() throws JSONException {

        JSONArray listas= new JSONArray();

        for(int i= 0;i<Global.TIPIFICACIONES_DIAGNOSTICO.size();i++) {
            JSONObject ob = Global.TIPIFICACIONES_DIAGNOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }


}
