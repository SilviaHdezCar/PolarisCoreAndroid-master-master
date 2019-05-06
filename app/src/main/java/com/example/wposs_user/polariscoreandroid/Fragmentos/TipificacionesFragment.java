package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificaciones;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogEsRepable;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class TipificacionesFragment extends Fragment {

    private LinearLayout layout_tipificaciones;
    private AutoCompleteTextView autocomplete_tipificaciones;
    private RecyclerView rv;
    private ArrayList<Tipificacion> listTipificaciones;
    public String descripcionTipificaion;
    private static ArrayList tipificaciones;
    private Button btn_siguiente_Tipificaciones;
    private static Tipificacion t;
    private RequestQueue queue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tipificaciones, container, false);
        descripcionTipificaion = "";
        layout_tipificaciones = (LinearLayout) v.findViewById(R.id.layout_tipificaciones);
        autocomplete_tipificaciones = (AutoCompleteTextView) v.findViewById(R.id.autocomplete_tipificaciones);
        btn_siguiente_Tipificaciones = (Button) v.findViewById(R.id.btn_siguiente_Tipificaciones);

        queue = Volley.newRequestQueue(objeto);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones);

        this.listTipificaciones = new ArrayList<Tipificacion>();
        Global.TIPIFICACIONES_DIAGNOSTICO = new ArrayList<String>();




        btn_siguiente_Tipificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguienteTipificaciones();
            }
        });



        consumirServicioTipificaciones();
        return v;

    }

    public void consumirServicioTipificaciones(){
        t = null;
        Global.TIPIFICACIONES = null;
        Global.TIPIFICACIONES= new ArrayList<Tipificacion>();
        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/tipesValidatorTerminal";
        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,//cuerpo de la peticion
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


                            JSONArray jsonArray = response.getJSONArray("tipificaciones");


                            if (jsonArray.length() == 0) {
                                layout_tipificaciones.setVisibility(View.INVISIBLE);
                                Global.mensaje = "No tiene tipificaciones";
                                Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            layout_tipificaciones.setVisibility(View.VISIBLE);
                            String ter = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                ter = jsonArray.getString(i);

                                t = gson.fromJson(ter, Tipificacion.class);
                                if (t != null) {
                                }
                                Global.TIPIFICACIONES.add(t);
                            }
                            llenarAutocomplete();
                            //llenarRVAsociadas(Global.TERMINALES_ASOCIADAS);
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

    //Concierte el arreglode tipificaciones a un arreglo de String
    public String[] getTipificaciones() {

        String[] rep = new String[Global.TIPIFICACIONES.size()];


        for (int i = 0; i < Global.TIPIFICACIONES.size(); i++) {

            rep[i] = Global.TIPIFICACIONES.get(i).getTetv_description();

        }
        return rep;

    }


    public void llenarAutocomplete() {

        final String[] getTipificaciones = this.getTipificaciones();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(objeto, android.R.layout.simple_list_item_1, getTipificaciones);

        autocomplete_tipificaciones.setAdapter(adapter);
        autocomplete_tipificaciones.setThreshold(0);
        autocomplete_tipificaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                descripcionTipificaion = adapter.getItem(i);

                InputMethodManager in = (InputMethodManager) objeto.getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                agregarTipificacion();
            }
        });
        /*autocomplete_tipificaciones.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {

                    descripcionTipificaion = adapter.getItem(i);
                    InputMethodManager in = (InputMethodManager) objeto.getSystemService(INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                    agregarTipificacion();
                    return true;
                }
                return false;
            }
        });*/


    }

    /**
     * Al prsionar agregar tipificacion
     * **/
    private void agregarTipificacion() {

        if (descripcionTipificaion.isEmpty()) {
            Toast.makeText(objeto, "Debe seleccionar una tipificación", Toast.LENGTH_SHORT).show();
            return;
        } else {
            for (Tipificacion tip : Global.TIPIFICACIONES) {
                if (tip != null) {
                    if (tip.getTetv_description().equalsIgnoreCase(descripcionTipificaion)) {
                        if (!buscarArregloRV(tip, listTipificaciones)) {
                            listTipificaciones.add(tip);
                            llenarRVTipificaciones();
                            descripcionTipificaion = "";
                            autocomplete_tipificaciones.setText("");
                        } else {
                            Toast.makeText(objeto, "La tipificación ya fue agregada", Toast.LENGTH_SHORT).show();
                            descripcionTipificaion = "";
                            autocomplete_tipificaciones.setText("");
                            return;
                        }
                    }
                }
            }

        }
    }

    public boolean buscarArregloRV(Tipificacion tip, ArrayList<Tipificacion> list) {
        boolean retorno = false;
        for (Tipificacion t : list) {
            if (tip == t) {
                retorno = true;
            }
        }

        return retorno;
    }


    //Metodo utilizado para llenar el arreglo que se le pasa al recycler view de tipificaciones
    public void llenarRVTipificaciones() {

        tipificaciones = new ArrayList<>();

        for (Tipificacion ter : listTipificaciones) {
            if (ter != null) {
                tipificaciones.add(ter);
            }
        }
        llenarRv();

    }


    private void llenarRv() {
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final AdapterTipificaciones adapter = new AdapterTipificaciones(tipificaciones, new AdapterTipificaciones.interfaceClick() {//seria termi asoc
            @Override
            public void onClick(List<Tipificacion> terminal, int position) {

                listTipificaciones.remove(position);
                tipificaciones.remove(position);
                llenarRv();
            }
        }, R.layout.panel_tipificaciones);

        rv.setAdapter(adapter);

    }


    /**
     * Muestra el cuadro de dialogo para seleccionar si es reparable
     *           NO-->Llenar el panel de observaciones
     *           SI-->  Mostrar cuadro de dialogo que pregunta si es por USO o FABRICA-->Pasar a la selección de repuestos
     * **/
    public void siguienteTipificaciones() {

        if(llenarTipificacionesDiagnostico()){
            esReparable();
        }

    }

    //Armo el arraylist     que voy a enviar al consumir el servicio de registrar diagnostico
    public boolean llenarTipificacionesDiagnostico() {
        boolean retorno=false;
        String cadena = "";

        if(listTipificaciones.size()==0){
            AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
            alertDialog.setTitle("¡ATENCIÓN!");
            alertDialog.setMessage("Debe seleccionar al menos una tipificacion");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ACEPTAR",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return false;
        }else {
           int  cont=0;
            for (Tipificacion tipi:listTipificaciones){
                if(tipi!=null){
    //        "Tipificaciones":[{"tets_terminal_serial":"212","tets_terminal_type_validation":"sadasdasd","tets_status":"ok"}]
                    //cadena = "{\"tets_terminal_serial\":\"<SERIAL>\",\"tets_terminal_type_validation\":\"<TIPO>\",\"tets_status\":\"ok\"}";
                    //cadena = cadena.replace("<SERIAL>", tipi.getTetv_id());
                    //cadena = cadena.replace("<TIPO>", tipi.getTetv_description());
                    cadena= "{"+(char)34+ "tets_terminal_serial"+(char)34 +":"+(char)34+ Global.serial_ter +(char)34+","+(char)34+ "tets_terminal_type_validation"+(char)34 +":"+(char)34+ tipi.getTetv_description() +(char)34+","+(char)34+ "tets_status"+(char)34+":"+ (char)34+"ok"+(char)34+"}";

                    Global.TIPIFICACIONES_DIAGNOSTICO.add(cadena);
                }
                cont++;
            }
            retorno=true;
        }


        return retorno;
    }

    public void esReparable() {
        DialogEsRepable dialog = new DialogEsRepable();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }



}
