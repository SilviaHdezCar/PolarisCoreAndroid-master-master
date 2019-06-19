package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogEsRepable;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class TipificacionesFragment extends Fragment {

    private LinearLayout layout_tipificaciones;
    private Spinner spinner_tipificaciones;
    // private RecyclerView rv;

    public String descripcionTipificaion;
    private static ArrayList tipificaciones;
    private Button btn_siguiente_Tipificaciones;
    private static Tipificacion t;
    private RequestQueue queue;

    private TableLayout tabla;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tipificaciones, container, false);
     //   objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("TIPIFICACIONES TERMINAL");
        descripcionTipificaion = "";
        layout_tipificaciones = (LinearLayout) v.findViewById(R.id.layout_tipificaciones);
        spinner_tipificaciones = (Spinner) v.findViewById(R.id.spinner_tipificaciones);
        btn_siguiente_Tipificaciones = (Button) v.findViewById(R.id.btn_siguiente_Tipificaciones);
        Global.panel_reparable = false;
        tabla = (TableLayout) v.findViewById(R.id.tabla_tipificaciones_asociadas);

        queue = Volley.newRequestQueue(objeto);
        //rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones);

        // Global.listTipificaciones = new ArrayList<Tipificacion>();
        Global.TIPIFICACIONES_DIAGNOSTICO = null;
        Global.TIPIFICACIONES_DIAGNOSTICO = new ArrayList<Tipificacion>();
        tabla.removeAllViews();
        llenarTabla();
        spinner_tipificaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] tipificaciones = obtenerTipificaciones();
                String t= tipificaciones[position];
                if(!t.equalsIgnoreCase("Seleccione")){
                    descripcionTipificaion = t;
                    agregarTipificacion();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_siguiente_Tipificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguienteTipificaciones();
            }
        });



        consumirServicioTipificaciones();
        return v;

    }


    /**
     * ESTE METODO SE UTILIZA PARA CONSUMIR EL SERVICIO QUE LISTA LAS TIPIFICACIONES +
     * INCOCA EL METODO QUE LLENA EL AUTOCOMPLETE
     **/
    public void consumirServicioTipificaciones() {
        t = null;
        Global.TIPIFICACIONES = null;
        Global.TIPIFICACIONES = new ArrayList<Tipificacion>();
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
                            Collections.sort((ArrayList)Global.TIPIFICACIONES);
                            llenarSpinner();
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

    //Concierte el arreglode tipificaciones a un arreglo de String
    public String[] obtenerTipificaciones() {
        String[] rep = new String[Global.TIPIFICACIONES.size()+1];
        rep[0] = "Seleccione";
        for (int i = 0; i < Global.TIPIFICACIONES.size(); i++) {
            rep[i+1] = Global.TIPIFICACIONES.get(i).getTetv_description();
        }
        return rep;

    }


    public void llenarSpinner() {

        final String[] getTipificaciones = this.obtenerTipificaciones();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(objeto, R.layout.spinner_tipificaciones_style, getTipificaciones);

        spinner_tipificaciones.setAdapter(adapter);
    }

    /**
     * Al prsionar la tipificación, la agrega a la tabla
     **/
    private void agregarTipificacion() {
        if (descripcionTipificaion.isEmpty()) {
            Toast.makeText(objeto, "Debe seleccionar una tipificación", Toast.LENGTH_SHORT).show();
            return;
        } else {
            for (Tipificacion tipif : Global.TIPIFICACIONES) {
                if (tipif.getTetv_description().equalsIgnoreCase(descripcionTipificaion)) {
                    boolean exite = buscarArreglo(descripcionTipificaion);
                    if (!exite) {
                        Global.listTipificaciones.add(tipif);
                        //llenarArregloTipificaciones();
                        tabla.removeAllViews();
                        llenarTabla();
                        descripcionTipificaion = "";
                        spinner_tipificaciones.setSelection(0);
                    } else {
                        Toast.makeText(objeto, "La tipificación ya fue agregada", Toast.LENGTH_SHORT).show();
                        descripcionTipificaion = "";
                        spinner_tipificaciones.setSelection(0);
                        return;
                    }
                }
            }
        }
    }

    public boolean buscarArreglo(String tip) {
//
        if (Global.listTipificaciones != null && Global.listTipificaciones.size() > 0) {
            for (Tipificacion t : Global.listTipificaciones) {
                if (tip.equals(t.getTetv_description())) {
                    return true;
                }
            }
        }


        return false;
    }


    //Metodo utilizado para llenar el arreglo que se le pasa para llenar de tipificaciones
    public void llenarArregloTipificaciones() {

        tipificaciones = new ArrayList<>();

        for (Tipificacion ter : Global.listTipificaciones) {
            if (ter != null) {
                tipificaciones.add(ter);
            }
        }
        // llenarRv();

    }



   /* private void llenarRv() {
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

    }*/


    /**
     * Este metodo se utiliza para recorrer la tabla mostrada de tipificaciones y elimina la tipificación
     * al presionar el radio button     *
     *
     * @param tabla
     */
    int pos_btn;

    public void recorrerTabla(final TableLayout tabla) {

        int pos_fila;

        for (int i = 0; i < tabla.getChildCount(); i++) {//filas
            View child = tabla.getChildAt(i);
            TableRow row = (TableRow) child;
            pos_fila = row.getId();
            View view = row.getChildAt(0);//celdas

            view = row.getChildAt(1);//Celda en la posición 1
            pos_btn = i;
            if (view instanceof ImageButton) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Global.listTipificaciones.remove(pos_btn);
                        tabla.removeAllViews();
                        llenarTabla();
                    }
                });

            }
        }
    }

    /**
     * Metodo utilizado para llenar la tabla tipificaciones con el botton eliminar
     **/
    public void llenarTabla() {

        if (Global.listTipificaciones != null && Global.listTipificaciones.size() > 0) {
            for (int i = 0; i < Global.listTipificaciones.size(); i++) {
                TableRow fila = new TableRow(objeto);
                fila.setId(i);
                fila.setBackgroundResource(R.drawable.borde_inferior_gris);
                fila.setGravity(Gravity.CENTER_VERTICAL);
                fila.setPadding(0, 0, 0, 0);

                //celdas
                TextView nombre = new TextView(objeto);
                nombre.setId(100 + i);
                nombre.setPadding(20, 0, 0, 0);
                nombre.setText(Global.listTipificaciones.get(i).getTetv_description());


                ImageButton btn_eliminar = new ImageButton(objeto);
                btn_eliminar.setId(200 + i);
                btn_eliminar.setMaxWidth(0);
                btn_eliminar.setMinimumWidth(0);
                btn_eliminar.setMaxHeight(0);
                btn_eliminar.setMinimumHeight(0);
                btn_eliminar.setPadding(0, 0, 0, 0);
                btn_eliminar.setImageResource(R.mipmap.ic_papelera_peq);
               // btn_eliminar.setColorFilter(Color.parseColor("#018793"));//para cambiar el color de la imagen
                btn_eliminar.setBackgroundColor(Color.parseColor("#00EEF3F3"));


                fila.addView(nombre);
                fila.addView(btn_eliminar);
                tabla.addView(fila);
            }
        }
        recorrerTabla(tabla);
    }


    /**
     * Muestra el cuadro de dialogo para seleccionar si es reparable
     * NO-->Llenar el panel de observaciones
     * SI-->  Mostrar cuadro de dialogo que pregunta si es por USO o FABRICA-->Pasar a la selección de repuestos
     **/
    public void siguienteTipificaciones() {
        if (llenarTipificacionesDiagnostico()) {
            if (Global.diagnosticoTerminal.equalsIgnoreCase("autorizada")) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new Registro_diagnostico()).addToBackStack(null).commit();
            } else {
                esReparable();
            }
        }

    }

    //Armo el arraylist     que voy a enviar al consumir el servicio de registrar diagnostico
    public boolean llenarTipificacionesDiagnostico() {
        boolean retorno = false;
        String cadena = "";
        if (Global.listTipificaciones.size() == 0) {
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
        } else {
            int cont = 0;
            for (Tipificacion tipi : Global.listTipificaciones) {
                if (tipi != null) {

                    Tipificacion tip = new Tipificacion(Global.serial_ter, tipi.getTetv_description(), "ok");
                    Global.TIPIFICACIONES_DIAGNOSTICO.add(tip);
                }
                cont++;
            }
            retorno = true;
        }


        return retorno;
    }

    public void esReparable() {
        Global.panel_reparable = true; //utilizado en el DialogFallaDetectada
        DialogEsRepable dialog = new DialogEsRepable();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }


}
