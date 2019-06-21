package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RadioGroup;
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
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogFallaDetectada;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ValidacionesSeleccionarAutorizadas extends Fragment {



    private View v;
    private TableLayout tabla;
    private Button btn_siguiente;


    private RequestQueue queue;

    private RequestQueue queue2;
    private RequestQueue queue3;
    private OnFragmentInteractionListener mListener;
    private JSONArray jsonArrayHistorial;
    private String mensaje;

    public ValidacionesSeleccionarAutorizadas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_validaciones_seleccionar_autorizadas, container, false);
        //objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("VALIDACIONES");
        queue = Volley.newRequestQueue(objeto);
        queue2 = Volley.newRequestQueue(objeto);
        queue3 = Volley.newRequestQueue(objeto);
        tabla = (TableLayout) v.findViewById(R.id.tabla_validaciones_autorizadas);
        btn_siguiente = (Button) v.findViewById(R.id.btn_siguiente_seleccionar_validaciones_autorizadas);

        llenarTabla();

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificacionEstados = validarEstadosValidaciones();
                if (verificacionEstados.equalsIgnoreCase("faltan")) {
                    return;
                } else {
                    if (verificacionEstados.equalsIgnoreCase("ok")) {
                        consumirServiciosCambiarEstadosRep();//CAMBIA EL ESTADO DE NUEVO A DAÑADO
                        //  consumirServicioReparacionExitosa();
                    } else if (verificacionEstados.equalsIgnoreCase("falla")) {
                        fallaDetectada();

                    }
                }

            }
        });


        return v;
    }

    /**
     * Este metodo Recorre el arreglo de validaciones y verifica que todos los estados esten ok o NA.     *
     *
     * @return faltan--> si el estado de la validacion es ok o Na(No aplica). falla--> si hay fallas en las validaciones. Ok--> si todo está ok o NA
     */
    public String validarEstadosValidaciones() {
        String retorno = "";
        recorrerTabla(tabla);
        int contFalla = 0;
        Validacion val = new Validacion();
        for (int i = 0; i < Global.VALIDACIONES.size(); i++) {
            val = Global.VALIDACIONES.get(i);
            if (val != null) {
                if (val.getEstado() == null || val.getEstado().isEmpty()) {
                   AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                    alertDialog.setTitle("Información");
                    alertDialog.setMessage("Verifique el estado de la validación: " + val.getTeva_description());
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                    return "faltan";

                } else {
                    if (val.getEstado().equalsIgnoreCase("falla")) {
                        contFalla++;
                    }
                }
            }
        }
        if (contFalla == 0) {
            retorno = "ok";
        } else {
            retorno = "falla";
        }
        return retorno;
    }


    /**
     * Este metodo se utiliza para recorrer la tabla mostrada de validaciones y cambia el estado
     * de la validacion al presionar un radio button
     *
     * @param tabla
     */
    public void recorrerTabla(TableLayout tabla) {

        int pos_fila;
        int pos_radio;

        for (int i = 1; i < tabla.getChildCount(); i++) {//filas
            View child = tabla.getChildAt(i);
            TableRow row = (TableRow) child;
            pos_fila = row.getId();
            View view = row.getChildAt(0);//celdas

            view = row.getChildAt(1);//celda en la pos 1
            if (view instanceof RadioGroup) {
                pos_radio = ((RadioGroup) view).getCheckedRadioButtonId();

                if (pos_radio == (300 + pos_fila)) {
                    Global.VALIDACIONES.get(i - 1).setEstado("ok");
                   Global.VALIDACIONES.get(i - 1).setOk(true);
                    Global.VALIDACIONES.get(i - 1).setFalla(false);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(false);
                }
                if (pos_radio == (400 + pos_fila)) {
                    Global.VALIDACIONES.get(i - 1).setEstado("falla");
                    Global.VALIDACIONES.get(i - 1).setOk(false);
                    Global.VALIDACIONES.get(i - 1).setFalla(true);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(false);
                }
                if (pos_radio == (500 + pos_fila)) {
                   Global.VALIDACIONES.get(i - 1).setEstado("na");
                    Global.VALIDACIONES.get(i - 1).setOk(false);
                    Global.VALIDACIONES.get(i - 1).setFalla(false);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(true);
                }
            }
        }
    }

    /**
     * Metodo utilizado para llenar la tabla de validaciones
     **/
    public void llenarTabla() {
        llenarListValidaciones();
        if (Global.VALIDACIONES.size() == 0) {
            Toast.makeText(objeto, "No tiene validaciones", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < Global.VALIDACIONES.size(); i++) {
            TableRow fila = new TableRow(objeto);
            fila.setId(i);
            fila.setBackgroundResource(R.drawable.borde_inferior_gris);
            fila.setGravity(Gravity.CENTER_VERTICAL);

            //celdas

            TextView nombre = new TextView(objeto);
            nombre.setId(200 + i);
            nombre.setText(Global.VALIDACIONES.get(i).getTeva_description());
            nombre.setWidth(2);
            nombre.setPadding(20, 0, 0, 0);

            RadioGroup rg = new RadioGroup(objeto);


            RadioButton ok = new RadioButton(objeto);
            ok.setId(300 + i);
            ok.setChecked(false);
            ok.setText("   ");

            RadioButton falla = new RadioButton(objeto);
            falla.setId(400 + i);
            falla.setChecked(false);
            falla.setText("   ");

            RadioButton na = new RadioButton(objeto);
            na.setId(500 + i);
            na.setChecked(false);
            na.setText("  ");

            rg.addView(ok);
            rg.addView(falla);
            rg.addView(na);
            rg.setOrientation(LinearLayout.HORIZONTAL);
            fila.addView(nombre);
            fila.addView(rg);
            tabla.addView(fila);
        }
    }

    /**
     * Este metodo llena el arreglo de validaciones  que va a ser mostrado en la tabla
     */
    public void llenarListValidaciones() {
        Global.VALIDACIONES = null;
        Global.VALIDACIONES = new ArrayList<>();
        String validaciones[] = Global.validaciones_listar_autorizadas.get(Global.terminalVisualizar.getTerm_serial()).split(",");

        for (int i = 0; i < validaciones.length; i++) {
            Validacion v = new Validacion(validaciones[i].split("-")[0], false, false, false);
            Global.VALIDACIONES.add(v);
        }
    }


    /**
     * Metodo utilizados para consumir el servicio  que permite registrar un diagnostico con observaciones mediante una petición REST
     * es utilizado cuando está todo ok en las validaciones
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioReparacionExitosa() {


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/saveDiagnosisQa";

        JSONObject jsonObject = new JSONObject();
        try {

            JSONArray val = this.getValidaciones();
            jsonObject.put("validaciones", val);
            // jsonObject.put("term_serial", Global.serial_ter);

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

                            } else {
                                eliminarPila();
                                objeto.CustomAlertDialog(objeto, "Información", "Reparación finalizada", 3000, false);
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
     * Metodo utilizados para consumir el servicio  que permite obtener el historial de la terminal
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioObtenerHistorial() {
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/getHistoryTerminal";
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
                                Toast.makeText(objeto, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                return;
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
                if (estado.equalsIgnoreCase("REPARACIÓN")&& tecnico.equalsIgnoreCase(Global.CODE)) {
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
            jsonObject.put("tipo", "QA");
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
                                Toast.makeText(objeto, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                return;
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
                                Toast.makeText(objeto, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                System.out.println("consumirServicioActualizarGestionadas()-->ok");
                                consumirServicioReparacionExitosa();
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
     * METODO QUE RECORRE LOS REPUESTOS Y LE CAMBIA EL ESTADO A DAÑADO
     **/
    public void consumirServiciosCambiarEstadosRep() {

//cambia estado a nuevo
        for (int i = 0; i < Global.REPUESTOS_CAMBIAR_ESTADO_DANADO.size(); i++) {
            //CONSUMO LOS DOS SERVICIOD-->PARA CAMBIAR EL ESTADO DE LOS REPUESTOS
            //consumo servio body 1
            String urlBody1 = "http://100.25.214.91:3000/PolarisCore/Spares/actualizarSpare";
            JSONObject jsonObject1 = new JSONObject();
            try {

                // Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getObj()
                jsonObject1.put("spar_code", Global.REPUESTOS_CAMBIAR_ESTADO_DANADO.get(i).getSpar_code());
                jsonObject1.put("spar_status", "NUEVO");
                jsonObject1.put("spar_warehouse", Global.CODE);//REvisar nombre llave
                jsonObject1.put("spar_quantity", Global.REPUESTOS_CAMBIAR_ESTADO_DANADO.get(i).getSpar_quantity());

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
                jsonObjectBody2.put("spar_code", Global.REPUESTOS_CAMBIAR_ESTADO_DANADO.get(i).getSpar_code());
                jsonObjectBody2.put("spar_status", "DAÑADO");
                jsonObjectBody2.put("spar_warehouse", Global.REPUESTOS_CAMBIAR_ESTADO_DANADO.get(i).getSpar_warehouse());//REvisar nombre llave
                jsonObjectBody2.put("spar_quantity", Global.REPUESTOS_CAMBIAR_ESTADO_DANADO.get(i).getSpar_quantity());
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
            consumirServicioObtenerHistorial();
        }

    }

    /**
     * Este metodo elimina todos los fragmentos de la pila
     */
    public void eliminarPila() {
        if (objeto.getSupportFragmentManager() != null) {
            FragmentManager fm = objeto.getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }


    }

    /**
     * Este metodo infla el cuadro de dialogo donde permite seleccionar la razón de la falla detectada
     */
    public void fallaDetectada() {
        DialogFallaDetectada dialog = new DialogFallaDetectada();
        dialog.show(objeto.getSupportFragmentManager(), "");
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
