package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Observacion obser;

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
        Global.REPUESTOS_DEFECTUOSOS_SOLICITAR = null;
        Global.REPUESTOS_DEFECTUOSOS_SOLICITAR = new ArrayList<Repuesto>();

        objeto.setTitle("          REPUESTOS DEFECTUOSOS");
        queue = Volley.newRequestQueue(objeto);

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
        fechaANS.setText(Global.terminalVisualizar.getTerm_date_register());

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
        System.out.println("serial "+Global.terminalVisualizar.getTerm_serial());
        observacion = txt_observacion.getText().toString().trim();
        if (observacion.isEmpty() || observacion == null) {
            Toast.makeText(objeto, "Por favor ingrese la observación", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.size() == 0 || Global.REPUESTOS_DEFECTUOSOS_SOLICITAR == null) {
            Toast.makeText(objeto, "Debe seleccionar al menos un repuesto", Toast.LENGTH_SHORT).show();
            return;
        } else {
            obser = new Observacion(observacion, Global.terminalVisualizar.getTerm_serial(), "");
            System.out.println("serial des else"+Global.terminalVisualizar.getTerm_serial());
            consumirServicioReparacionExitosa();
        }

    }


    /**
     * Este metodo se utiliza para verificar que todos los repuestos estén OK
     *
     * @return true-->si todos están oOK
     */
    public void validarEstadosRepuestos() {
        recorrerTabla(tabla);
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
                if (((RadioButton) view).isChecked()) {
                    Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i - 1).setOk(true);
                }
            }
            System.out.println("Pos: " + i + "-->" + Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i - 1).getSpar_name() + "-" + Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i - 1).isOk());
        }
    }

    /**
     * Metodo utilizado para llenar la tabla de respuestos con la columna OK
     **/
    public void llenarTabla() {
        System.out.println("Tamaño lista: " + Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.size());

        for (int i = 0; i < Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.size(); i++) {
            TableRow fila = new TableRow(objeto);
            fila.setId(i);
            fila.setGravity(View.TEXT_ALIGNMENT_CENTER);
            //celdas

            TextView nombre = new TextView(objeto);
            nombre.setId(100 + i);
            nombre.setText(Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.get(i).getSpar_name());

            RadioButton ok = new RadioButton(objeto);
            ok.setId(200 + i);
            ok.setChecked(false);

            fila.addView(nombre);
            fila.addView(ok);
            tabla.addView(fila);


        }
    }

    /**
     * Metodo utilizados para consumir el servicio  que permite registrar un diagnostico con observaciones mediante una petición REST
     * es utilizado cuando está todo ok en las validaciones
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioReparacionExitosa() {


        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/saveDiagnosisSpare";
        JSONObject jsonObject = new JSONObject();
        JSONObject obj2 = new JSONObject();
        try {

            JSONArray val=this.getValidaciones();
            jsonObject.put("validaciones",val);

            jsonObject.put("observacion",obser.getObjRep());

            JSONArray rep=this.getRepuestos();
            obj2.put("tesw_serial",Global.terminalVisualizar.getTerm_serial());
            obj2.put("tesw_repuestos",rep);
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
                            System.out.println("STATUS-->" + response.get("status").toString());
                            if (response.get("status").toString().equals("fail")) {
                                Global.mensaje=response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                    alertDialog.setTitle("Información");
                                    alertDialog.setMessage("Su sesión ha expirado, debe iniciar sesión nuevamente ");
                                    alertDialog.setCancelable(true);
                                    alertDialog.show();
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
                                AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                                alertDialog.setTitle("Informacion");
                                alertDialog.setMessage("Reparación finalizada");
                                alertDialog.setCancelable(true);
                                alertDialog.show();
                                eliminarPila();
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
            //CONSUMO LOS DOS SERVICIOD
            JSONObject ob = Global.REPUESTOS_DEFECTUOSOS_SOLICITAR.get(i).getObj();
            listas.put(ob);
        }

        return listas;
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
