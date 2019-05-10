package com.example.wposs_user.polariscoreandroid;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.java.MyValueFormatter;
import com.example.wposs_user.polariscoreandroid.java.Productividad;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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


    private BarChart grafica;
    private View v;
    private RequestQueue queue;
    ArrayList<Productividad> productividad;
    private EditText f_inicio;
    private EditText f_fin;
    TextView titulo_x;
    TextView titulo_y;
    Button produc;


    public static String Fecha1, Fecha2;

    boolean isChanged = false;
    private static final String CERO = "0";
    private static final String BARRA = "/";

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
        productividad= new ArrayList<>();
        // Inflate the layout for this fragment
        v =inflater.inflate(R.layout.fragment_productividad_dia, container, false);

        titulo_y=(TextView)v.findViewById(R.id.titulo_y);
        queue = Volley.newRequestQueue(objeto);
        titulo_x=(TextView)v.findViewById(R.id.titulo_x);
        produc=(Button)v.findViewById(R.id.btn_productividad);

        f_inicio = (EditText) v.findViewById(R.id.txt_fecha_inicio_prod);
        f_fin = (EditText) v.findViewById(R.id.txt_fecha_fin_prod);




        //carga los txt de las fechas al hacer la consulta establecida por fechas

        f_inicio.setInputType(InputType.TYPE_NULL);
        f_fin.setInputType(InputType.TYPE_NULL);
        f_inicio.setInputType(InputType.TYPE_NULL);


        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        fecha = fecha.replace("-", "/");
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
                        f_inicio.setText(Tools.dateDDMMYYYYStr2(f_inicio.getText().toString()));
                        return;
                    }
                }, 70);
                isChanged = true;
            }
        });
        f_fin.addTextChangedListener(new TextWatcher() {
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

                        Fecha2 = f_fin.getText().toString();
                        f_fin.setText(Tools.dateDDMMYYYYStr2(f_fin.getText().toString()));
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
        f_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = false;
                showDatePickerDialog(f_fin);
            }
        });






        grafica=(BarChart)v.findViewById(R.id.grafica_productividad);



        produc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicioProductividad();
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
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }









    /********************Metodo usado para obtener la productividad en un rango de fecha dada*************************************/////////

    public void consumirServicioProductividad() {
        productividad=new ArrayList<>();

        String data_inicio= f_inicio.getText().toString();
        String data_fin= f_fin.getText().toString();

        if(data_inicio.isEmpty()||data_fin.isEmpty()){
            Toast.makeText(v.getContext(),"Debe seleccionar la fecha de inicio y fin",Toast.LENGTH_SHORT).show();
            return;

        }

        boolean validar= this.validarFecha();

        if(validar==false){
            Toast.makeText(v.getContext(),"La fecha de fin debe ser posterior a la fecha de inicio",Toast.LENGTH_SHORT).show();

            grafica.setVisibility(View.INVISIBLE);
            titulo_y.setVisibility(View.INVISIBLE);
            titulo_x.setVisibility(View.INVISIBLE);
            return;
        }

        String fecha_inicial= f_inicio.getText().toString();
        String[] fecha=fecha_inicial.split("/");
        int dia_inicio=Integer.parseInt(fecha[0]);
        int mes_inicio=Integer.parseInt(fecha[1]);
        int año_inicio=Integer.parseInt(fecha[2]);
        String fecha_inicio=dia_inicio+"/"+ mes_inicio+"/"+año_inicio;

        String fecha_final= f_fin.getText().toString();
        String[] fechaFin=fecha_final.split("/");
        int dia_fin=Integer.parseInt(fechaFin[0]);
        int mes_fin=Integer.parseInt(fechaFin[1]);
        int año_fin=Integer.parseInt(fechaFin[2]);
        String fecha_fin= dia_fin+"/"+mes_fin+"/"+año_fin;

        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/productivity?Authenticator";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
            jsonObject.put("fechaInicial", fecha_inicio);
            jsonObject.put("fechaFinal",fecha_fin);
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
                            System.out.println("REPUESTA DEL SERVICIO****************"+response.toString());


                            JSONArray jsonArray = response.getJSONArray("productividad");


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No se encontraron registros para el rango de fechas seleccionado";
                                Toast.makeText(v.getContext(),Global.mensaje,Toast.LENGTH_SHORT).show();
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

                            System.out.println("TAMAÑO DE LA RESPUESTA ************************"+productividad.size());

                            this.pintarGrafica();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }

                    private void pintarGrafica() {

                        List<BarEntry> entradas= new ArrayList<>();

                        for(int i=0;i<productividad.size();i++){

                            entradas.add(new BarEntry(i+1,productividad.get(i).getUste_completed_terminals(),productividad.get(i).getUste_associated_terminals()));


                        }

                        System.out.println("VARIABLES DE LA grafica ************************"+entradas.size());

                        //Enviamos los datos para crear la grafica

                        BarDataSet datos = new BarDataSet(entradas,"");
                        datos.setValueFormatter( new MyValueFormatter());
                        Description des = grafica.getDescription();
                        des.setEnabled(false);

                        BarData data= new BarData(datos);

                        //Colocamos color a cada Barra

                        datos.setColors(ColorTemplate.COLORFUL_COLORS);

                        //Separacion entre barras
                        data.setBarWidth(0.9f);

                        grafica.setData(data);

                        //pone las barras centradas
                        grafica.setFitBars(true);

                        grafica.invalidate();//hacer refresh

                        grafica.setVisibility(View.VISIBLE);
                        titulo_y.setVisibility(View.VISIBLE);
                        titulo_x.setVisibility(View.VISIBLE);


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


    public boolean validarFecha(){

        String fecha_inicial= f_inicio.getText().toString();
        String[] fecha=fecha_inicial.split("/");
        int dia_inicio=Integer.parseInt(fecha[0]);
        int mes_inicio=Integer.parseInt(fecha[1]);
        int año_inicio=Integer.parseInt(fecha[2]);

        System.out.println("Fecha inicial:  "+"dia:"+dia_inicio+"mes:"+mes_inicio+"año:"+año_inicio);

        String fecha_final= f_fin.getText().toString();
        String[] fechaFin=fecha_final.split("/");
        int dia_fin=Integer.parseInt(fechaFin[0]);
        int mes_fin=Integer.parseInt(fechaFin[1]);
        int año_fin=Integer.parseInt(fechaFin[2]);

        System.out.println("Fecha final:  "+"dia:"+dia_fin+"mes:"+mes_fin+"año:"+año_fin);


        if(año_fin<año_inicio){ return false; }
        if(mes_fin<mes_inicio){ return false; }
        if(dia_fin<dia_inicio){ return false; }

        return true;

    }








}
