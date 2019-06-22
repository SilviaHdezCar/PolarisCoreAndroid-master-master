package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
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
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static java.util.Collections.sort;

public class EtapasNuevoD_autorizadas extends Fragment {


    private View view;


    private TableLayout encabezado;


    private TextView serial;
    private TextView marca;
    private TextView modelo;
    private TextView tecnologia;
    private TextView estado;
    private TextView fechaANS;
    private TextView garantia;
    private EditText textArea_observacion;

    private Button btn_agregar_etapa;
    private Button btn_siguiente;

    private static Observacion o;


    private RecyclerView rv;


    private RequestQueue queue;

    private String observacion;


    public EtapasNuevoD_autorizadas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_etapas_terminal_autorizada, container, false);
       //objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

         //  <!-- android:windowSoftInputMode="adjustPan|adjustResize"-->VA EN EL MANIFEST
        objeto.setTitulo("ETAPAS");
        if (Global.diagnosticoTerminal.equalsIgnoreCase("autorizada")) {
            Global.lista_tipificaciones_tabla = new ArrayList<Tipificacion>();
            Global.listTipificaciones = new ArrayList<Tipificacion>();
        }

        // muestro la terminal seleccionada con los valores que guarde en el obj terminal

        queue = Volley.newRequestQueue(objeto);
        encabezado = (TableLayout) view.findViewById(R.id.tabla_encabezado);

        serial = (TextView) view.findViewById(R.id.serial_ter_asociada);
        marca = (TextView) view.findViewById(R.id.marca_ter_asociada);
        modelo = (TextView) view.findViewById(R.id.modelo_ter_asociada);
        tecnologia = (TextView) view.findViewById(R.id.tecno_ter_asociada);
        estado = (TextView) view.findViewById(R.id.estado_ter_asociada);
        garantia = (TextView) view.findViewById(R.id.txt_garantia);
        fechaANS = (TextView) view.findViewById(R.id.fechal_ter_asociada);
        rv = (RecyclerView) view.findViewById(R.id.recycler_view_observaciones_validacion);
        btn_agregar_etapa = (Button) view.findViewById(R.id.btn_agregar_etapa_autorizada);
        btn_siguiente = (Button) view.findViewById(R.id.btn_siguiente_etapas_autorizadas);
        textArea_observacion = (EditText) view.findViewById(R.id.textArea_information);


        ImageView imagen = (ImageView) view.findViewById(R.id.imagen_asociada_eta);
        Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewModel/"+
                Global.terminalVisualizar.getTerm_model().toUpperCase()+".jpg").error(R.drawable.img_no_disponible).fit().centerInside().into(imagen);

        btn_siguiente.bringToFront();

        serial.setText(Global.terminalVisualizar.getTerm_serial());
        marca.setText(Global.terminalVisualizar.getTerm_brand());
        modelo.setText(Global.terminalVisualizar.getTerm_model());
        tecnologia.setText(Global.terminalVisualizar.getTerm_technology());
        estado.setText(Global.terminalVisualizar.getTerm_status());
        garantia.setText(this.estadoGarantía(Global.terminalVisualizar.getTerm_date_finish()));
        fechaANS.setText("");
        if (Global.terminalVisualizar.getTerm_date_ans() == null) {
            fechaANS.setText(Global.terminalVisualizar.getTerm_date_ans());
        }


        encabezado.setVisibility(View.GONE);


        consumirServicioEtapas();
        btn_agregar_etapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observacion = textArea_observacion.getText().toString();
                if (observacion.isEmpty()) {
                    Toast.makeText(objeto, "Por favor ingrese la observación", Toast.LENGTH_SHORT).show();
                    return;
                }
                consumirServicioAgregarEtapa();
            }
        });

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main,
                        new ValidacionesTerminalesAsociadas()).addToBackStack(null).commit();
            }
        });
        textArea_observacion.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE
                        || i == EditorInfo.IME_FLAG_NO_ENTER_ACTION || i == EditorInfo.IME_ACTION_GO
                        || i == EditorInfo.IME_ACTION_NONE) {
                    InputMethodManager imm = (InputMethodManager) objeto.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });


        return view;
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
                                if (response.get("message").toString().equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "Error: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            response = new JSONObject(response.get("data").toString());

                            JSONArray jsonArray = response.getJSONArray("observaciones");
                            String obser = null;
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    obser = jsonArray.getString(i);
                                    o = gson.fromJson(obser, Observacion.class);
                                    Global.OBSERVACIONES.add(o);
                                }
                                if (Global.OBSERVACIONES != null) {
                                    sort(Global.OBSERVACIONES);
                                }
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
     * Metodo utilizado para llenar el recycler view de las observaciones del terminal seleccionado
     *
     * @Params Recibe la lista  observaciones o etapas que van a ser mostradas
     **/
    public void llenarRVEtapas(List<Observacion> observaciones) {
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        ArrayList observations = new ArrayList<>();

        for (int i = observaciones.size() - 1; i >= 0; i--) {
            if (observaciones.get(i) != null) {
                if (!observaciones.get(i).getTeob_description().trim().isEmpty()) {
                    observations.add(observaciones.get(i));
                }
            }
        }

        //Al dar clic en una observacion infla el panel de validaciones
        final AdapterEtapa adapter = new AdapterEtapa(observations, new AdapterEtapa.interfaceClick() {
            @Override
            public void onClick(List<Observacion> observaciones, int position) {

            }
        }, R.layout.panel_etapas);

        rv.setAdapter(adapter);

    }
    /**
     * Este metodo devuelve el estado dela garantía de la terminal
     *
     * @param garantia
     * @return Con garantía: Sí la garantía está vigente, No establecida: si la fecha de garantía no se estableció, Sin garantía: si ya no tiene garantía
     */
    public String estadoGarantía(String garantia) {
        String retorno = "";
        System.out.println("garantía--> +"+garantia);
        int comparacionFechas = tiempoGarantiaTerminal(garantia);
        System.out.println("comparacion-->" + comparacionFechas);
        if (comparacionFechas > 0) {
            retorno = "Con garantía";
        } else if (comparacionFechas == 0) {
            retorno = "No establecida";
        } else {
            retorno = "Sin garantía";
        }

        return retorno;
    }
    /**
     * Metodo que permite calcular el tiempo que duró la atención de la incidencia
     *
     * @return
     */
    public int tiempoGarantiaTerminal(String garantia) {
        int retorno =0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        Date dateEnd = new Date();
        Date fechaGarantia = null;
        try {
            if (garantia != null) {
                if (!garantia.trim().isEmpty() && !garantia.equals("Invalid Date")) {
                    fechaGarantia = dateFormat.parse(Utils.darFormatoNewDateDiferencia(garantia));
                    Date fechaActual = dateFormat.parse(Utils.darFormatoNewDateDiferencia2(dateEnd + ""));
                    int diferencia = (int) ((fechaGarantia.getTime() - fechaActual.getTime()) / 1000);
                    retorno =diferencia;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    /**
     * Metodo utilizados para consumir el servicio  para listar las observaciones de acuerdo a una terminal mediante una petición REST
     * En el encabezado va el token-> Authenticator
     * Se envía el serial de la terminal  Global.serial_ter
     **/
    public void consumirServicioAgregarEtapa() {
        o = null;
        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/saveObservations";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("teob_description", observacion);
            jsonObject.put("teob_serial_terminal", Global.serial_ter);
            jsonObject.put("teob_photo", " ");
            jsonObject.put("teob_id_user", Global.CODE);
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
                            if (response.get("status").toString().equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "Error al agregar la observación", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                textArea_observacion.setText("");
                                consumirServicioEtapas();
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


}
