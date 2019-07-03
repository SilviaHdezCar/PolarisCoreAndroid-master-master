package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepuestoDefectuosoAutorizadas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepuestoDefectuosoAutorizadas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepuestoDefectuosoAutorizadas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private View v;
    private TextView serial;
    private TextView marca;
    private TextView modelo;
    private TextView tecnologia;
    private TextView estado;
    private TextView fechaANS;

    private TableLayout tabla;

    private TextView txt_observacion;
    private String observacion;

    private Button btn_siguiente;

    private RequestQueue queue;
    private RequestQueue queue2;
    private RequestQueue queue3;
    private Observacion obser;
    private String mensaje;
    private JSONArray jsonArrayHistorial;

    public RepuestoDefectuosoAutorizadas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepuestoDefectuosoAutorizadas.
     */
    // TODO: Rename and change types and number of parameters
    public static RepuestoDefectuosoAutorizadas newInstance(String param1, String param2) {
        RepuestoDefectuosoAutorizadas fragment = new RepuestoDefectuosoAutorizadas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_repuesto_defectuoso_autorizadas, container, false);
        // objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Global.REPUESTOS_DEFECTUOSOS_SOLICITAR = null;
        Global.REPUESTOS_DEFECTUOSOS_SOLICITAR = new ArrayList<Repuesto>();
        LinearLayout layout_terminales = (LinearLayout) v.findViewById(R.id.layout_terminales);
        layout_terminales.setBackgroundResource(R.drawable.borde_amarillo);

        objeto.setTitulo("REPUESTOS DEFECTUOSOS");
        queue = Volley.newRequestQueue(objeto);
        queue2 = Volley.newRequestQueue(objeto);
        queue3 = Volley.newRequestQueue(objeto);

        serial = (TextView) v.findViewById(R.id.serial_terminales);
        marca = (TextView) v.findViewById(R.id.marca_terminales);
        modelo = (TextView) v.findViewById(R.id.modelo_terminales);
        tecnologia = (TextView) v.findViewById(R.id.tecno_terminales);
        estado = (TextView) v.findViewById(R.id.estado_terminales);
        fechaANS = (TextView) v.findViewById(R.id.fechaANS_terminales);

        serial.setText(Global.terminalVisualizar.getTerm_serial());
        marca.setText(Global.terminalVisualizar.getTerm_brand());
        modelo.setText(Global.terminalVisualizar.getTerm_model());
        tecnologia.setText(Global.terminalVisualizar.getTerm_technology());
        estado.setText(Global.terminalVisualizar.getTerm_status());
        fechaANS.setText("");
        if (Global.terminalVisualizar.getTerm_date_ans() != null
                && !Global.terminalVisualizar.getTerm_date_ans().isEmpty()) {
            fechaANS.setText(Utils.darFormatoNewDate(Global.terminalVisualizar.getTerm_date_ans()));
        }


        btn_siguiente = (Button) v.findViewById(R.id.btn_siguiente_repuestos_defectuoso_autorizadas);
        txt_observacion = (TextView) v.findViewById(R.id.txt_observacion_repuesto);
        tabla = (TableLayout) v.findViewById(R.id.tabla_seleccionar_repuestos_defectuosos);


        llenarTabla();
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitar();
            }
        });

        return v;
    }


    public void solicitar() {
        validarEstadosRepuestos();
        observacion = txt_observacion.getText().toString().trim();
        if (Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.size() == 0 || Global.REPUESTOS_DEFECTUOSOS_SOLICITAR == null) {
            Toast.makeText(objeto, "Debe seleccionar al menos un repuesto", Toast.LENGTH_SHORT).show();
            return;
        }
        if (observacion.isEmpty() || observacion == null) {
            Toast.makeText(objeto, "Por favor ingrese la observación", Toast.LENGTH_SHORT).show();
            return;
        } else {
            obser = new Observacion(observacion, Global.terminalVisualizar.getTerm_serial(), "");
            consumirServicioObtenerHistorial();
        }

    }


    /**
     * Este metodo se utiliza para verificar que todos los repuestos estén OK
     *
     * @return true-->si todos están oOK
     */
    public void validarEstadosRepuestos() {
        //    recorrerTabla(tabla);
        Global.REPUESTOS_DEFECTUOSOS_SOLICITAR = new ArrayList<Repuesto>();
        Repuesto rep = new Repuesto();
        for (int i = 0; i < Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.size(); i++) {
            rep = Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i);
            if (rep != null) {
                if (rep.isOk())
                    Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.add(rep);
            }
        }
    }


    /**
     * Este metodo se utiliza para recorrer la tabla mostrada de repuestos y cambia el estado
     * del repuesto al presionar el radio button     *
     *
     * @param tabla
     */
    public void recorrerTabla(TableLayout tabla) {

        for (int i = 0; i < Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.size(); i++) {
            Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i).setOk(false);
        }
        int pos_fila;
        int pos_radio;

        for (int i = 1; i < tabla.getChildCount(); i++) {//filas
            View child = tabla.getChildAt(i);
            TableRow row = (TableRow) child;
            pos_fila = row.getId();
            View view = row.getChildAt(0);//celdas
           /* if (view instanceof TextView) {

                System.out.println("id: " + ((TextView) view).getText().toString());
                view.setEnabled(false);
            }*/
            view = row.getChildAt(1);//Celda en la posición 1

            if (view instanceof RadioButton) {
                final View finalView = view;
                final int finalI = i;
                ((RadioButton) view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (((RadioButton) finalView).isChecked()) {
                            if (((RadioButton) finalView).isSelected()) {
                                System.out.println("if selected true ");
                                ((RadioButton) finalView).setSelected(false);
                                ((RadioButton) finalView).setChecked(false);
                                Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(finalI - 1).setOk(false);
                                if (Global.REPUESTOS_DEFECTUOSOS_SOLICITAR != null) {
                                    if (Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.size() > 0) {
                                        System.out.println("tamaño reopuestos solicitar: " + Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.size());
                                        System.out.println("tamaño reopuestos autorizadas: " + Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.size());
                                        for (Repuesto rep : Global.REPUESTOS_DEFECTUOSOS_SOLICITAR) {
                                            if (rep.getSpar_code().equals(Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(finalI - 1).getSpar_code())) {
                                                Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.remove(rep);
                                                break;
                                            }
                                        }
                                    }
                                }

                            } else if (!((RadioButton) finalView).isSelected()) {
                                ((RadioButton) finalView).setSelected(true);
                                Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(finalI - 1).setOk(true);
                            }


                        }
                    }
                });

            }
            System.out.println("rep defec Pos: " + i + "-->" + Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i - 1).getSpar_name() + "-" + Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i - 1).isOk());
        }
    }

    /**
     * Metodo utilizado para llenar la tabla de respuestos con la columna OK
     **/
    public void llenarTabla() {

        for (int i = 0; i < Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.size(); i++) {
            TableRow fila = new TableRow(objeto);
            fila.setId(i);
            fila.setBackgroundResource(R.drawable.borde_inferior_gris);
            fila.setGravity(Gravity.CENTER_VERTICAL);

            //celdas

            TextView nombre = new TextView(objeto);
            nombre.setId(100 + i);
            nombre.setText(Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i).getSpar_name());
            nombre.setPadding(20, 0, 0, 0);

            RadioButton ok = new RadioButton(objeto);
            ok.setId(200 + i);
            ok.setChecked(false);

            fila.addView(nombre);
            fila.addView(ok);
            tabla.addView(fila);
        }
        recorrerTabla(tabla);
    }

    /**
     * Metodo utilizados para consumir el servicio  que permite obtener el historial de la terminal
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioObtenerHistorial() {
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/getHistoryTerminal";
        System.out.println("llegó a consumir servicio que obtiene el historial");
        jsonArrayHistorial = new JSONArray();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPUESTA OB His", response.toString());
                            if (!response.get("message").toString().equals("success")) {
                                mensaje = response.get("message").toString();
                                if (mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }

                            } else {

                                response = response.getJSONObject("data");
                                // jsonObject=response.getJSONArray("tehi_historial");
                                System.out.println("response-->" + response.toString());
                                String tehi_historial = response.getString("tehi_historial");
                                System.out.println("tehi_historial-->" + tehi_historial);
                                if (!tehi_historial.equals("")) {
                                    jsonArrayHistorial = new JSONArray(tehi_historial);
                                    System.out.println("jsonArray--->" + jsonArrayHistorial.length());
                                    if (jsonArrayHistorial != null && jsonArrayHistorial.length() > 0) {
                                        evaluarJsonArray();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("authenticator", Global.TOKEN);
                params.put("Id", Global.terminalVisualizar.getTerm_serial());

                return params;
            }
        };
        queue.add(jsArrayRequest);
    }


    /**
     * Este metodo se utiliza para revisar la respuesta obtenida luego de consumir el servicio en
     * le que se obtiene el historial de gestionadas de la terminal
     */
    public void evaluarJsonArray() {
        try {
            if (jsonArrayHistorial.length() == 1) {
                consumirServicioSumarGestion();

            } else {
                JSONObject jsonObject = jsonArrayHistorial.getJSONObject(jsonArrayHistorial.length() - 2);
                String estado = jsonObject.get("term_state").toString();
                String tecnico = jsonObject.get("term_location").toString();
                if ((estado.equalsIgnoreCase("REPARACIÓN")
                        || estado.equalsIgnoreCase("QA"))
                        && tecnico.equalsIgnoreCase(Global.CODE)) {
                    consumirServicioActualizarGestionadas();
                } else {
                    consumirServicioSumarGestion();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Metodo utilizados para consumir el servicio  que permite incrementar las terminales
     * gwartionadas por el técnico
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioSumarGestion() {
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/incrementarGestionadas";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
            jsonObject.put("tipo", "REPARACIÓN");
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
                            Log.d("RESPUESTA SUMAR", response.toString());
                            if (!response.get("status").toString().equals("ok")) {
                                mensaje = response.get("message").toString();
                                if (mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }

                            } else {
                                consumirServicioActualizarGestionadas();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()) {
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authenticator", Global.TOKEN);

                return params;
            }
        };
        queue.add(jsArrayRequest);
    }


    /**
     * Metodo utilizados para consumir el servicio  que permite actualizar las terminales gestionadas
     * gwartionadas por el técnico
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioActualizarGestionadas() {
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/updateHistoryTerminal";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("terminal_history", this.JObjectActualizarHistorial());
            jsonObject.put("tehi_serial", Global.terminalVisualizar.getTerm_serial());
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
                        try {
                            Log.d("RESPUESTA ACTUALIZAR", response.toString());
                            if (!response.get("status").toString().equals("ok")) {
                                mensaje = response.get("message").toString();
                                if (mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }

                            } else {
                                System.out.println("consumirServicioActualizarGestionadas()-->ok");
                                consumirServicioRepuestoDefectuoso();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()) {
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authenticator", Global.TOKEN);
                params.put("iD", Global.terminalVisualizar.getTerm_serial());

                return params;
            }
        };
        queue.add(jsArrayRequest);
    }

    public JSONObject JObjectActualizarHistorial() {
        JSONObject jsonObject = new JSONObject();
        try {
            Date date = new Date();
            jsonObject.put("term_location", Global.CODE);
            jsonObject.put("term_state", "DIAGNÓSTICO");
            jsonObject.put("date", date + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Metodo utilizados para consumir el servicio  que permite registrar un diagnostico cuando tiene repuestos defectusosos
     * es utilizado cuando está hay un afalla y es por repuesto defectusoso en las validaciones
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioRepuestoDefectuoso() {


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/saveDiagnosisSpare";
        JSONObject jsonObject = new JSONObject();
        JSONObject obj2 = new JSONObject();
        try {

            JSONArray val = this.getValidaciones();
            jsonObject.put("validaciones", val);

            jsonObject.put("observacion", obser.getObjRep());

            JSONArray rep = this.getRepuestos();
            obj2.put("tesw_serial", Global.terminalVisualizar.getTerm_serial());
            obj2.put("tesw_repuestos", rep);
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
                            if (response.get("status").toString().equals("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("Información");
                                alertDialog.setMessage("Error: " + response.get("message").toString());
                                alertDialog.setCancelable(true);
                                alertDialog.show();
                                return;

                            } else {
                                objeto.eliminarPila();
                                CustomAlertDialog(objeto, "Información", "Reparación finalizada", 3000, true);
                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA REG OBs", response.toString());
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
     * METODO QUE RECORRE LA LISTA DE VALIDACIONES Y LAS AGREGA AL ARREGLO
     **/
    public JSONArray getValidaciones() throws JSONException {
        JSONArray listas = new JSONArray();
        for (int i = 0; i < Global.VALIDACIONES.size(); i++) {
            JSONObject ob = Global.VALIDACIONES.get(i).getObjRep();
            listas.put(ob);
        }
        return listas;
    }


    /**
     * METODO QUE RECORRE LOS REPUESTOS Y LS AGREGA AL ARREGLO
     **/
    public JSONArray getRepuestos() throws JSONException {

        JSONArray listas = new JSONArray();

        for (int i = 0; i < Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.size(); i++) {
            //CONSUMO LOS DOS SERVICIOD-->PARA CAMBIAR EL ESTADO DE LOS REPUESTOS
            //consumo servio body 1
            String urlBody1 = "http://100.25.214.91:3000/PolarisCore/Spares/actualizarSpare";
            JSONObject jsonObject1 = new JSONObject();
            try {

                // Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getObj()
                jsonObject1.put("spar_code", Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getSpar_code());
                jsonObject1.put("spar_status", "NUEVO");
                jsonObject1.put("spar_warehouse", Global.CODE);//REvisar nombre llave
                jsonObject1.put("spar_quantity", Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getSpar_quantity());

                Log.d("RESPUESTA", jsonObject1.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsArrayRequest1 = new JsonObjectRequest(
                    Request.Method.POST,
                    urlBody1,
                    jsonObject1,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!response.get("message").toString().equals("success")) {
                                    Global.mensaje = response.get("message").toString();
                                    if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                        Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                        objeto.consumirSercivioCerrarSesion();
                                        return;
                                    }
                                    return;

                                }
                                return;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("RESPUESTA REG OBs", response.toString());
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

            queue2.add(jsArrayRequest1);
            //consumo servio body 2
            String urlBody2 = "http://100.25.214.91:3000/PolarisCore/Spares/actualizarSpare";
            JSONObject jsonObjectBody2 = new JSONObject();
            try {

                // Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getObj()
                jsonObjectBody2.put("spar_code", Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getSpar_code());
                jsonObjectBody2.put("spar_status", "DEFECTUOSO");
                jsonObjectBody2.put("spar_warehouse", Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getSpar_warehouse());//REvisar nombre llave
                jsonObjectBody2.put("spar_quantity", Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getSpar_quantity());
                jsonObjectBody2.put("spar_warehouse_new", Global.CODE);

                Log.d("RESPUESTA", jsonObjectBody2.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsArrayRequest2 = new JsonObjectRequest(
                    Request.Method.POST,
                    urlBody2,
                    jsonObjectBody2,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (!response.get("message").toString().equals("success")) {
                                    Global.mensaje = response.get("message").toString();
                                    if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                        Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                        objeto.consumirSercivioCerrarSesion();
                                        return;
                                    }
                                    return;

                                }
                                return;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("RESPUESTA REG OBs", response.toString());
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

            queue3.add(jsArrayRequest2);


            JSONObject rep = Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getObj();
            listas.put(rep);
        }

        return listas;
    }


    static AlertDialog dialog;

    public void CustomAlertDialog(Context c, String titulo, String msg, int tiempo, boolean cancelable) {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.DialogColor));
        builder.setTitle(titulo);
        builder.setMessage(msg);
        builder.setCancelable(cancelable);
        dialog = builder.create();
        if (((Activity) c).isFinishing()) {
            return;
        }
        //dialog.show();
        try {
            dialog.show();
        } catch (Exception e) {
            Log.i("exception", "Mensaje cerrado por la fuerza");
        }
        int titleDividerId = c.getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));
        }
        if (!cancelable)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, tiempo);
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
