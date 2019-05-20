package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogFallaDetectada_autorizadas;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ValidacionesSeleccionarAutorizadas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ValidacionesSeleccionarAutorizadas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidacionesSeleccionarAutorizadas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View v;
    private TableLayout tabla;
    private Button btn_siguiente;


    private RequestQueue queue;

    private RequestQueue queue2;
    private RequestQueue queue3;
    private OnFragmentInteractionListener mListener;

    public ValidacionesSeleccionarAutorizadas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ValidacionesSeleccionarAutorizadas.
     */
    // TODO: Rename and change types and number of parameters
    public static ValidacionesSeleccionarAutorizadas newInstance(String param1, String param2) {
        ValidacionesSeleccionarAutorizadas fragment = new ValidacionesSeleccionarAutorizadas();
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
        v = inflater.inflate(R.layout.fragment_validaciones_seleccionar_autorizadas, container, false);
        objeto.setTitle("               VALIDACIONES");
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
                    System.out.println("verificación--> " + verificacionEstados);
                    if (verificacionEstados.equalsIgnoreCase("ok")) {
                        consumirServiciosCambiarEstadosRep();
                        consumirServicioReparacionExitosa();
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
     * @return true--> si el estado de la validacion es ok o Na(No aplica). false--> si el estado es Falla
     */
    public String validarEstadosValidaciones() {
        String retorno = "";
        recorrerTabla(tabla);
        int contFalla = 0;
        Validacion val = new Validacion();
        for (int i = 0; i < Global.VALIDACIONES.size(); i++) {
            val = Global.VALIDACIONES.get(i);
            if (val != null) {
                if (val.getEstado().isEmpty() || val.getEstado() == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                    alertDialog.setTitle("Información");
                    alertDialog.setMessage("Verifique el estado de la validación: " + val.getTeva_description());
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                    return "faltan";
                } else {
                    System.out.println(Global.VALIDACIONES.get(i).getTeva_description() + "-" + Global.VALIDACIONES.get(i).getEstado());
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
        System.out.println("Con falla: " + contFalla);
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
            System.out.println("fila: " + pos_fila);
            View view = row.getChildAt(0);//celdas
           /* if (view instanceof TextView) {

                System.out.println("id: " + ((TextView) view).getText().toString());
                view.setEnabled(false);
            }*/
            view = row.getChildAt(1);//celda en la pos 1
            if (view instanceof RadioGroup) {
                pos_radio = ((RadioGroup) view).getCheckedRadioButtonId();
                System.out.println("Pos_radio: " + pos_radio);

                if (pos_radio == (300 + pos_fila)) {
                    Global.VALIDACIONES.get(i - 1).setEstado("ok");
                    Global.VALIDACIONES.get(i - 1).setOk(true);
                    Global.VALIDACIONES.get(i - 1).setFalla(false);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(false);
                    System.out.println("isOK");
                }
                if (pos_radio == (400 + pos_fila)) {
                    Global.VALIDACIONES.get(i - 1).setEstado("falla");
                    Global.VALIDACIONES.get(i - 1).setOk(false);
                    Global.VALIDACIONES.get(i - 1).setFalla(true);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(false);
                    System.out.println("isFalla");
                }
                if (pos_radio == (500 + pos_fila)) {
                    Global.VALIDACIONES.get(i - 1).setEstado("na");
                    Global.VALIDACIONES.get(i - 1).setOk(false);
                    Global.VALIDACIONES.get(i - 1).setFalla(false);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(true);
                    System.out.println("isNa");
                }
            }
            System.out.println("Pos: " + i + "-->" + Global.VALIDACIONES.get(i - 1).getTeva_description() + "-" + Global.VALIDACIONES.get(i - 1).getEstado());
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
            nombre.setPadding(20,0,0,0);

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
        String validaciones[] = Global.validacionesAutorizadas.split(",");

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
        System.out.println("REPARACION EXITOSA ");

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
                            System.out.println("STATUS-->" + response.get("status").toString());
                            if (response.get("status").toString().equals("fail")) {
                                Global.mensaje=response.get("message").toString();
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
                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("Información");
                                alertDialog.setMessage("Reparación finalizada");
                                alertDialog.setCancelable(true);
                                alertDialog.show();
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
     * METODO QUE RECORRE LOS REPUESTOS Y LS AGREGA AL ARREGLO
     **/
    public void consumirServiciosCambiarEstadosRep(){

//cambia estado a nuevo
        for (int i = 0; i < Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.size(); i++) {
            //CONSUMO LOS DOS SERVICIOD
            //consumo servio body 1
            String urlBody1 = "http://100.25.214.91:3000/PolarisCore/Spares/actualizarSpare";
            JSONObject jsonObject1 = new JSONObject();
            try {

                // Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getObj()
                jsonObject1.put("spar_code", Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i).getSpar_code());
                jsonObject1.put("spar_status", "NUEVO");
                jsonObject1.put("spar_warehouse", Global.CODE);//REvisar nombre llave
                jsonObject1.put("spar_quantity", Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i).getSpar_quantity());

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
                                    System.out.println("!success servicio body 1");
                                    return;

                                }
                                System.out.println("cambiar estado dañado bien");
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

            queue2.add(jsArrayRequest1);

   //cambia estado a Dañado
            //consumo servio body 2
            String urlBody2 = "http://100.25.214.91:3000/PolarisCore/Spares/actualizarSpare";
            JSONObject jsonObjectBody2 = new JSONObject();
            try {

                // Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getObj()
                jsonObjectBody2.put("spar_code", Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i).getSpar_code());
                jsonObjectBody2.put("spar_status", "DAÑADO");
                jsonObjectBody2.put("spar_warehouse", Global.CODE);//REvisar nombre llave
                jsonObjectBody2.put("spar_quantity", Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i).getSpar_quantity());
                jsonObjectBody2.put("spar_warehouse_new", Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i).getSpar_warehouse());

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
                                    System.out.println("!success servicio body 1");
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

            queue3.add(jsArrayRequest2);

        }

    }

    /**
     * Este metodo elimina todos los fragmentos de la pila
     * ()
     */
    public void eliminarPila() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

    }

    /**
     * Este metodo infla el cuadro de dialogo donde permite seleccionar la razón de la falla detectada
     */
    public void fallaDetectada() {
        DialogFallaDetectada_autorizadas dialog = new DialogFallaDetectada_autorizadas();
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
