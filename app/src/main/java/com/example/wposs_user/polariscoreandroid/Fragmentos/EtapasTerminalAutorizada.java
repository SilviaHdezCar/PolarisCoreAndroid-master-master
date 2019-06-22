package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
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
import android.widget.LinearLayout;
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
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EtapasTerminalAutorizada.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EtapasTerminalAutorizada#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EtapasTerminalAutorizada extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;

    private Button etapaView, validacionView;

    private TextView serial;
    private TextView marca;
    private TextView modelo;
    private TextView tecnologia;
    private TextView estado;
    private TextView garantia;
    private TextView fechaANS;
    private EditText textArea_observacion;

    private Button btn_agregar_etapa_autorizada;
    private Button btn_siguiente_etapas_autorizada;

    private TableLayout tablaObservacion;

    private static Observacion o;


    private RecyclerView rv;


    private RequestQueue queue;

    private String observacion;

    public EtapasTerminalAutorizada() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EtapasTerminalAutorizada.
     */
    // TODO: Rename and change types and number of parameters
    public static EtapasTerminalAutorizada newInstance(String param1, String param2) {
        EtapasTerminalAutorizada fragment = new EtapasTerminalAutorizada();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(objeto);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Global.fotos = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_etapas_terminal_autorizada, container, false);
       // objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("ETAPAS");

        // muestro la terminal seleccionada con los valores que guarde en el obj terminal

        serial = (TextView) view.findViewById(R.id.serial_ter_asociada);
        marca = (TextView) view.findViewById(R.id.marca_ter_asociada);
        modelo = (TextView) view.findViewById(R.id.modelo_ter_asociada);
        tecnologia = (TextView) view.findViewById(R.id.tecno_ter_asociada);
        estado = (TextView) view.findViewById(R.id.estado_ter_asociada);
        garantia = (TextView) view.findViewById(R.id.txt_garantia);
        fechaANS = (TextView) view.findViewById(R.id.fechal_ter_asociada);
        rv = (RecyclerView) view.findViewById(R.id.recycler_view_observaciones_validacion);
        btn_agregar_etapa_autorizada = (Button) view.findViewById(R.id.btn_agregar_etapa_autorizada);
        btn_siguiente_etapas_autorizada = (Button) view.findViewById(R.id.btn_siguiente_etapas_autorizadas);
        textArea_observacion = (EditText) view.findViewById(R.id.textArea_information);

        tablaObservacion = (TableLayout) view.findViewById(R.id.tablaObservacion);

        ImageView imagen = (ImageView) view.findViewById(R.id.imagen_asociada_eta);
        Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewModel/"+
                Global.terminalVisualizar.getTerm_model().toUpperCase()+".jpg").error(R.drawable.img_no_disponible).fit().centerInside().into(imagen);
        serial.setText(Global.terminalVisualizar.getTerm_serial());
        marca.setText(Global.terminalVisualizar.getTerm_brand());
        modelo.setText(Global.terminalVisualizar.getTerm_model());
        tecnologia.setText(Global.terminalVisualizar.getTerm_technology());
        estado.setText(Global.terminalVisualizar.getTerm_status());
        System.out.println("garantía: "+Global.terminalVisualizar.getTerm_date_finish());
        System.out.println("estado garantía: "+this.estadoGarantía(Global.terminalVisualizar.getTerm_date_finish()));
        garantia.setText(this.estadoGarantía(Global.terminalVisualizar.getTerm_date_finish()));
        fechaANS.setText("");
        if (Global.terminalVisualizar.getTerm_date_reception() != null) {
            fechaANS.setText(Utils.darFormatoFecha2(Global.terminalVisualizar.getTerm_date_reception()) + " - ");
        }
        if (Global.terminalVisualizar.getTerm_date_ans() != null) {
            fechaANS.setText(fechaANS.getText().toString() + Utils.darFormatoFecha2(Global.terminalVisualizar.getTerm_date_ans()));
        }

        etapaView = (Button) view.findViewById(R.id.btn_etapas);
        validacionView = (Button) view.findViewById(R.id.btn_validacion_terminales);

        validacionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.setTitulo("VALIDACIONES");
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionTerminalesFragment()).addToBackStack(null).commit();
            }
        });


        if (Global.soloConsulta.equalsIgnoreCase("si")) {
            tablaObservacion.setVisibility(View.GONE);
        }
        consumirServicioEtapas();

        btn_agregar_etapa_autorizada.setOnClickListener(new View.OnClickListener() {
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

        btn_siguiente_etapas_autorizada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new TipificacionesAutorizadas()).addToBackStack(null).commit();
            }
        });

        return view;
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
    public void consumirServicioEtapas() {
        o = null;
        Global.OBSERVACIONES = null;
        Global.OBSERVACIONES = new ArrayList<Observacion>();
        Global.observaciones_con_fotos = null;
        Global.observaciones_con_fotos = new ArrayList<Observacion>();
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
                            Log.d("RESPUESTA OBSERVACIONES", response.toString());
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

                            JSONArray jsonArray = response.getJSONArray("observaciones");

                            String obser = null;
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    obser = jsonArray.getString(i);
                                    o = gson.fromJson(obser, Observacion.class);
                                    System.out.println(i+"-Ob llegó: "+o.toString());

                                    if (o.getTeob_photo() != null ) {
                                        if ( !o.getTeob_photo().equalsIgnoreCase("")) {
                                            if (o.getTeob_description()==null||o.getTeob_description().isEmpty()) {
                                                Global.observaciones_con_fotos.add(o);
                                            }
                                        }
                                    }
                                    Global.OBSERVACIONES.add(o);
                                }
                                System.out.println("tamaño ob fotos: "+ Global.observaciones_con_fotos.size());
                                if (Global.OBSERVACIONES != null) {
                                    sort(Global.OBSERVACIONES);
                                }

                            }
                            llenarRVEtapas(Global.OBSERVACIONES);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    public Observacion[] getObservacionesArray() {
        Observacion obs[] = new Observacion[Global.OBSERVACIONES.size()];
        for (int i = 0; i < Global.OBSERVACIONES.size(); i++) {
            obs[i] = Global.OBSERVACIONES.get(i);
        }

        return obs;
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
