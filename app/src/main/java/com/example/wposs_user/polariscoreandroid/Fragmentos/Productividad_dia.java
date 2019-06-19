package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.MyValueFormatter;
import com.example.wposs_user.polariscoreandroid.java.Productividad;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Productividad_dia.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Productividad_dia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Productividad_dia extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private View v;
    private RequestQueue queue;
    private ArrayList<Productividad> productividad;
    private EditText f_inicio;
    private Button produc;
    private TextView titulo;
    private BarChart grafica;
    private LinearLayout linea;
    private TextView tituloG;


    public static String Fecha1, Fecha2;

    boolean isChanged = false;
    private static final String CERO = "0";
    private static final String BARRA = "-";

    FragmentManager fragmentManager;
    public final java.util.Calendar c = java.util.Calendar.getInstance();
    final int mes = c.get(java.util.Calendar.MONTH);
    final int dia = c.get(java.util.Calendar.DAY_OF_MONTH);
    final int anio = c.get(java.util.Calendar.YEAR);


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Productividad_dia() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Productividad_dia.
     */
    // TODO: Rename and change types and number of parameters
    public static Productividad_dia newInstance(String param1, String param2) {
        Productividad_dia fragment = new Productividad_dia();
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
        productividad = new ArrayList<>();
       // objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_productividad_dia, container, false);
        objeto.setTitulo("PRODUCTIVIDAD POR DÍA");


        queue = Volley.newRequestQueue(objeto);
        produc = (Button) v.findViewById(R.id.btn_productividad_dia);

        f_inicio = (EditText) v.findViewById(R.id.txt_fecha_inicio_produc);
        grafica = (BarChart) v.findViewById(R.id.grafica_dia);
        tituloG=(TextView)v.findViewById(R.id.titulo_prod);


        //carga los txt de las fechas al hacer la consulta establecida por fechas

        f_inicio.setInputType(InputType.TYPE_NULL);



        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        fecha = fecha.replace("-", "-");
        // f_inicio.setText(fecha);
        Fecha1 = f_inicio.getText().toString();
        //f_inicio.setText(Tools.dateDDMMYYYYStr2(f_inicio.getText().toString()));


        f_inicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Fecha1 = f_inicio.getText().toString();
                        f_inicio.setText(Tools.dateDDMMYYYYStr3(f_inicio.getText().toString()));
                        return;
                    }
                }, 70);
                isChanged = true;
            }
        });

        f_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = false;
                showDatePickerDialog(f_inicio);
            }
        });


        produc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicioProductividadDia();
            }
        });


        return v;


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


    private void showDatePickerDialog(final EditText etFecha) {
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }


    /********************Metodo usado para obtener la productividad en un dia dado*************************************/////////
    public void consumirServicioProductividadDia() {

        grafica.clear();

        productividad = new ArrayList<>();

        String data_inicio = f_inicio.getText().toString();


        if (data_inicio.isEmpty()) {
            Toast.makeText(v.getContext(), "Debe seleccionar el día", Toast.LENGTH_SHORT).show();
             grafica.clear();
            tituloG.setVisibility(INVISIBLE);
            grafica.setVisibility(INVISIBLE);
            return;

        }


        final String fecha_inicial = f_inicio.getText().toString();
        String[] fecha = fecha_inicial.split("-");

        final int dia_inicio = Integer.parseInt(fecha[0]);
        int mes_inicio = Integer.parseInt(fecha[1]);
        int año_inicio = Integer.parseInt(fecha[2]);

        if(!validarFechaActual(dia_inicio,mes_inicio,año_inicio)){

            Toast.makeText(v.getContext(), "La fecha selecionada debe ser anterior a la actual", Toast.LENGTH_SHORT).show();
            grafica.clear();
            tituloG.setVisibility(INVISIBLE);
            grafica.setVisibility(INVISIBLE);
            f_inicio.setText("");
            return;
        }

        grafica.clear();

        String fecha_inicio = mes_inicio + "/" + dia_inicio + "/" + año_inicio;




        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/productivity";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
            jsonObject.put("fechaInicial", fecha_inicio);
            jsonObject.put("fechaFinal", fecha_inicio);
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
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                return;
                            }


                            response = new JSONObject(response.get("data").toString());
                            System.out.println("REPUESTA DEL SERVICIO****************" + response.toString());


                            JSONArray jsonArray = response.getJSONArray("productividad");


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No existen registros para el día seleccionado";
                                Toast.makeText(v.getContext(), Global.mensaje, Toast.LENGTH_SHORT).show();
                                grafica.clear();
                                f_inicio.setText("");
                                tituloG.setVisibility(INVISIBLE);
                                grafica.setVisibility(INVISIBLE);

                                return;
                            }
                            Productividad pro;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String res = jsonArray.getString(i);

                                pro = gson.fromJson(res, Productividad.class);

                                if (pro != null) {
                                }
                                productividad.add(pro);
                            }

                            this.pintarGrafica();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }

                    private void pintarGrafica() {


                        Productividad pr = productividad.get(0);

                        ArrayList<BarEntry> diagnosticadas = new ArrayList<>();
                        ArrayList<BarEntry> reparadas = new ArrayList<>();
                        ArrayList<BarEntry> asignadas = new ArrayList<>();

                        if (pr.getUste_associated_terminals() == null || pr.getUste_associated_terminals().equalsIgnoreCase("NaN")) {
                            asignadas.add(new BarEntry(2, 0));
                        }

                        if (pr.getUste_diagnosed_terminals()== null || pr.getUste_diagnosed_terminals().equals("NaN")) {
                           diagnosticadas.add(new BarEntry(2, 0));
                        }

                        if (pr.getUste_repaired_terminals() == null || pr.getUste_repaired_terminals().equalsIgnoreCase("NaN")) {
                            reparadas.add(new BarEntry(2, 0));
                        }


                        if (pr.getUste_associated_terminals() != null ) {

                            if(!pr.getUste_associated_terminals().equalsIgnoreCase("NaN")){

                                asignadas.add(new BarEntry(2,Float.parseFloat(pr.getUste_associated_terminals())));

                            }

                        }

                        if (pr.getUste_diagnosed_terminals()!= null ) {

                            if(!pr.getUste_diagnosed_terminals().equals("NaN")){

                                diagnosticadas.add(new BarEntry(2, Float.parseFloat(pr.getUste_diagnosed_terminals())));


                            }


                        }

                        if (pr.getUste_repaired_terminals() != null ) {
                            if( !pr.getUste_repaired_terminals().equalsIgnoreCase("NaN")){

                                reparadas.add(new BarEntry(2, Float.parseFloat(pr.getUste_repaired_terminals())));
                            }

                        }

                        BarDataSet tAsignadas = new BarDataSet(asignadas, "Asignadas");
                        BarDataSet tDiagnosticadas = new BarDataSet(diagnosticadas, "Diagnosticadas");
                        BarDataSet tReparadas= new BarDataSet(reparadas,"Reparadas");
                        String y= "        "+fecha_inicial;
                        String[] fecha = new String[] {y};

                        tAsignadas.setColor(Color.parseColor("#038793"));
                        tDiagnosticadas.setColor(Color.parseColor("#BDBDBD"));
                        tReparadas.setColor(Color.parseColor("#45A5F3"));


                        tAsignadas.setDrawValues(true);
                        tReparadas.setDrawValues(true);
                        tDiagnosticadas.setDrawValues(true);

                        tAsignadas.setValueFormatter(new MyValueFormatter());
                        tReparadas.setValueFormatter(new MyValueFormatter());
                        tDiagnosticadas.setValueFormatter(new MyValueFormatter());
                        grafica.setFitBars(true);



                        BarData datosGrafica = new BarData(tAsignadas, tDiagnosticadas,tReparadas);
                        datosGrafica.setValueTextSize(10);
                        grafica.setData(datosGrafica);
                        // String []meses= new String[]{"enero", "febrero", "marzo", "abril", "mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
                        XAxis x = grafica.getXAxis();
                        //  x.setValueFormatter(new IndexAxisValueFormatter(meses));

                        x.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                        YAxis yAxis = grafica.getAxis(YAxis.AxisDependency.LEFT);
                        yAxis.setCenterAxisLabels(true);
                        yAxis.setAxisMinimum(0);
                        yAxis.setGranularity(1);

                        tituloG.setVisibility(VISIBLE);
                        tituloG.setText("Productividad del día: " + fecha_inicial);

                       grafica.getAxis(YAxis.AxisDependency.LEFT);
                        yAxis.setCenterAxisLabels(true);
                        yAxis.setAxisMinimum(0);
                        yAxis.setGranularity(1);



                        //  x.setValueFormatter(new IndexAxisValueFormatter(meses));


                        x.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

                        x.setCenterAxisLabels(true);
                        grafica.getAxisRight().setEnabled(false);
                        x.setLabelCount(1);
                        x.setDrawLabels(false);
                        x.setPosition(XAxis.XAxisPosition.BOTTOM);
                        x.setGranularity(1);
                        x.setGranularityEnabled(true);
                        grafica.setDragEnabled(true);
                        grafica.setVisibleXRangeMaximum(2);
                        float barSpace = 0.02f;
                        float groupSpace = 0.6f;
                        datosGrafica.setBarWidth(0.2f);
                        grafica.getXAxis().setAxisMinimum(0);
                        grafica.getXAxis().setAxisMaximum(1);
                        grafica.groupBars(0, groupSpace, barSpace);
                        grafica.invalidate();
                        grafica.getDescription().setEnabled(false);
                        grafica.getAxisRight().setGranularity(1);
                        grafica.setVisibility(VISIBLE);
                          grafica.setVisibility(VISIBLE);
                        f_inicio.setText("");


                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error Respuesta en JSON: " + error.getMessage());
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()){
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


    public boolean validarFechaActual(int dia, int mes, int anio){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fechaActual = dateFormat.format(date);
        String []fechaAct= fechaActual.split("-");
        int anioAct=Integer.parseInt( fechaAct[0]);
        int mesAct=Integer.parseInt( fechaAct[1]);
        int diaAct= Integer.parseInt(fechaAct[2]);

        if(anio>anioAct){ return false;}

        if(anio<anioAct){ return true;}


       if(anio==anioAct){

            if(mes>mesAct){return false;}
            if(mes<mesAct){return true;}
            if(mes==mesAct){
                if(dia>=diaAct){return false;}
                    }
                 }

       return true;


    }


}
