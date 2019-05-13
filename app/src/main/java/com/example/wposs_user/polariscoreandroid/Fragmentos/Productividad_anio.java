package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.wposs_user.polariscoreandroid.java.Productividad;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Productividad_anio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Productividad_anio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Productividad_anio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
     private String mParam2;

     private Spinner año;
     private View v;
     GraphView grafica;
     LinearLayout linea;
    private RequestQueue queue;
    private ArrayList<Productividad> productividad;
    private Button consulta_produc;
    int [] diagnosticoMes;
    int [] reparadasMes;

    int total_enero=0;
    int total_febrero=0;
    int total_marzo=0;
    int total_abril=0;
    int total_mayo=0;
    int total_junio=0;
    int total_julio=0;
    int total_agosto=0;
    int total_septiembre=0;
    int total_octubre=0;
    int total_noviembre=0;
    int total_diciembre=0;

    private OnFragmentInteractionListener mListener;

    public Productividad_anio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Productividad_anio.
     */
    // TODO: Rename and change types and number of parameters
    public static Productividad_anio newInstance(String param1, String param2) {
        Productividad_anio fragment = new Productividad_anio();
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
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_productividad_anio, container, false);
        año=(Spinner)v.findViewById(R.id.spiner_añoxaño);
        queue = Volley.newRequestQueue(objeto);
        grafica=(GraphView)v.findViewById(R.id.grafica_año);
        consulta_produc= (Button)v.findViewById(R.id.consultar_año);
        linea= (LinearLayout)v.findViewById(R.id.linea_titulo_año);
        int [] diagnosticoMes=new int[12];
        int [] reparadasMes=new int[12];

        consulta_produc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicioProductividadAño();

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


    /********************Metodo usado para mostrar la productividad en un mes y año dado**********************/

    public void consumirServicioProductividadAño() {




        grafica.removeAllSeries();

        String añoDado= año.getSelectedItem().toString();


        productividad=new ArrayList<>();




        if(año.getSelectedItem()==null){
            Toast.makeText(v.getContext(),"Debe selecccionar el año a consultar",Toast.LENGTH_SHORT).show();
            grafica.setVisibility(INVISIBLE);
            linea.setVisibility(INVISIBLE);
            return;

        }

        if(año.getSelectedItem().toString().equals("Seleccione")){
            Toast.makeText(v.getContext(),"Seleccione un año valido",Toast.LENGTH_SHORT).show();
            grafica.setVisibility(INVISIBLE);
            linea.setVisibility(INVISIBLE);
            return;

        }

        int año_selecionado=Integer.parseInt(añoDado);



        String fecha_inicio=1+"/"+1+"/"+ añoDado;
        String fecha_fin=12+"/"+31+"/"+ añoDado;



        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/productivity";
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
                            System.out.println("REPUESTA DEL SERVICIO****************"+response.toString().trim());


                            JSONArray jsonArray = response.getJSONArray("productividad");


                            if (jsonArray.length() == 0) {
                                Global.mensaje = "No se encontraron registros para el mes y año seleccionado";
                                Toast.makeText(v.getContext(),Global.mensaje,Toast.LENGTH_SHORT).show();
                                grafica.setVisibility(INVISIBLE);
                                linea.setVisibility(INVISIBLE);
                                grafica.removeAllSeries();
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

                        System.out.println(productividad.toString());


                       diagnosticoMes=getValoresDiagnosticoMes();
                       reparadasMes=getValoresReparadasMes();

                        System.out.println("mes de mayo**********"+"    "+diagnosticoMes[6]);

                        BarGraphSeries<DataPoint> diagnostico_enero= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(1, diagnosticoMes[0]) });
                        BarGraphSeries<DataPoint> diagnostico_febrero= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(2, diagnosticoMes[1]) });
                        BarGraphSeries<DataPoint> diagnostico_marzo=new BarGraphSeries<>(new DataPoint[]{  new DataPoint(3, diagnosticoMes[2]) });
                        BarGraphSeries<DataPoint> diagnostico_abril= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(4, diagnosticoMes[3]) });
                        BarGraphSeries<DataPoint> diagnostico_mayo= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(5, diagnosticoMes[4]) });
                        BarGraphSeries<DataPoint> diagnostico_junio= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(6, diagnosticoMes[5]) });
                        BarGraphSeries<DataPoint> diagnostico_julio= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(7, diagnosticoMes[6]) });
                        BarGraphSeries<DataPoint> diagnostico_agosto= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(8, diagnosticoMes[7]) });
                        BarGraphSeries<DataPoint> diagnostico_septiembre= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(9, diagnosticoMes[8]) });
                        BarGraphSeries<DataPoint> diagnostico_octubre= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(10, diagnosticoMes[9]) });
                        BarGraphSeries<DataPoint> diagnostico_noviembre= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(11, diagnosticoMes[10]) });
                        BarGraphSeries<DataPoint> diagnostico_diciembre= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(12, diagnosticoMes[11]) });

                        BarGraphSeries<DataPoint> reparadas_enero= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(1, reparadasMes[0]) });
                        BarGraphSeries<DataPoint> reparadas_febrero= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(2,reparadasMes[1]) });
                        BarGraphSeries<DataPoint> reparadas_marzo=new BarGraphSeries<>(new DataPoint[]{  new DataPoint(3, reparadasMes[2]) });
                        BarGraphSeries<DataPoint> reparadas_abril= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(4, reparadasMes[3]) });
                        BarGraphSeries<DataPoint> reparadas_mayo= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(5, reparadasMes[4]) });
                        BarGraphSeries<DataPoint> reparadas_junio= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(6, reparadasMes[5]) });
                        BarGraphSeries<DataPoint> reparadas_julio= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(7, reparadasMes[6]) });
                        BarGraphSeries<DataPoint> reparadas_agosto= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(8, reparadasMes[7]) });
                        BarGraphSeries<DataPoint> reparadas_septiembre= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(9, reparadasMes[8]) });
                        BarGraphSeries<DataPoint> reparadas_octubre= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(10, reparadasMes[9]) });
                        BarGraphSeries<DataPoint> reparadas_noviembre= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(11, reparadasMes[10]) });
                        BarGraphSeries<DataPoint> reparadas_diciembre= new BarGraphSeries<>(new DataPoint[]{  new DataPoint(12, reparadasMes[11]) });


                        grafica.addSeries(diagnostico_enero);
                        reparadas_mayo.setDrawValuesOnTop(true);
                        reparadas_mayo.setValuesOnTopColor(Color.BLACK);
                         reparadas_mayo.setValuesOnTopSize(20);


                        reparadas_mayo.setColor(Color.GREEN);
                        diagnostico_mayo.setDrawValuesOnTop(true);
                        diagnostico_mayo.setValuesOnTopSize(20);
                        diagnostico_mayo.setValuesOnTopColor(Color.BLACK);
                        diagnostico_mayo.setColor(Color.BLUE);


                        grafica.addSeries(reparadas_enero);
                        grafica.addSeries(diagnostico_febrero);
                        grafica.addSeries(reparadas_febrero);
                        grafica.addSeries(diagnostico_marzo);
                        grafica.addSeries(reparadas_marzo);
                        grafica.addSeries(diagnostico_abril);
                        grafica.addSeries(reparadas_abril);
                        grafica.addSeries(diagnostico_mayo);
                        grafica.addSeries(reparadas_mayo);
                        grafica.addSeries(diagnostico_junio);
                        grafica.addSeries(reparadas_junio);
                        grafica.addSeries(diagnostico_julio);
                        grafica.addSeries(reparadas_julio);
                        grafica.addSeries(diagnostico_agosto);
                        grafica.addSeries(reparadas_agosto);
                        grafica.addSeries(diagnostico_septiembre);
                        grafica.addSeries(reparadas_septiembre);
                        grafica.addSeries(diagnostico_octubre);
                        grafica.addSeries(reparadas_octubre);
                        grafica.addSeries(diagnostico_noviembre);
                        grafica.addSeries(reparadas_noviembre);
                        grafica.addSeries(diagnostico_diciembre);
                        grafica.addSeries(reparadas_diciembre);




                        grafica.setMinimumHeight(10000);
                        grafica.setMinimumWidth(10000);
                        grafica.animate();
                        grafica.getGridLabelRenderer().setHorizontalAxisTitle("meses");
                        grafica.getGridLabelRenderer().setVerticalAxisTitle("Terminales");
                        grafica.setVisibility(VISIBLE);
                        linea.setVisibility(VISIBLE);





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

/********************Metodo para calcular la productividad de terminales asociadas en cada mes***************************/

public int[] getValoresDiagnosticoMes(){


       int []total= new int[12];

    total [0]=0;
    total [2]=0;
    total [3]=0;
    total [4]=0;
    total [5]=0;
    total [6]=0;
    total [7]=0;
    total [8]=0;
    total [9]=0;
    total [10]=0;
    total [11]=0;



        for(int i=0;i<productividad.size();i++){
            String  fecha=productividad.get(i).getUste_date();
            String[] mes_s= fecha.split("/");


            int mes= Integer.parseInt(mes_s[1]);

            if(mes==1){
              total_enero = total_enero +productividad.get(i).getUste_associated_terminals();
            }

            if(mes==2){
                total_febrero=total_febrero +productividad.get(i).getUste_associated_terminals();
            }

            if(mes==3){
                total_marzo=total_marzo+productividad.get(i).getUste_associated_terminals();
            }

            if(mes==4){
                total_abril=total_abril +productividad.get(i).getUste_associated_terminals();
            }

            if(mes==5){
                total_mayo=total_mayo +productividad.get(i).getUste_associated_terminals();
            }

            if(mes==6){
                total_junio=total_junio +productividad.get(i).getUste_associated_terminals();
            }

            if(mes==7){
                total_julio=total_julio +productividad.get(i).getUste_associated_terminals();
            }
            if(mes==8){
                total_agosto=total_agosto+productividad.get(i).getUste_associated_terminals();
            }

            if(mes==9){
                total_septiembre=total_septiembre +productividad.get(i).getUste_associated_terminals();
            }

            if(mes==10){
                total_octubre=total_octubre +productividad.get(i).getUste_associated_terminals();
            }
            if(mes==11){
                total_enero=total_noviembre +productividad.get(i).getUste_associated_terminals();
            }
            if(mes==12){
                total_enero=total_diciembre +productividad.get(i).getUste_associated_terminals();
            }

               }

    total [0]=total_enero;
    total [1]=total_febrero;
    total [2]=total_marzo;
    total [3]=total_abril;
    total [4]=total_mayo;
    total [5]=total_junio;
    total [6]=total_julio;
    total [7]=total_agosto;
    total [8]=total_septiembre;
    total [9]=total_octubre;
    total [10]=total_noviembre;
    total [11]=total_diciembre;




        return total;

}

    /********************Metodo para calcular la productividad de terminales Reparadas en cada mes******************/

    public int[] getValoresReparadasMes(){

        int []total= new int[12];
        total [0]=0;
        total [2]=0;
        total [3]=0;
        total [4]=0;
        total [5]=0;
        total [6]=0;
        total [7]=0;
        total [8]=0;
        total [9]=0;
        total [10]=0;
        total [11]=0;




        for(int i=0;i<productividad.size();i++){
            String  fecha=productividad.get(i).getUste_date();
            String[] mes_s= fecha.split("/");
            int mes= Integer.parseInt(mes_s[1]);

            if(mes==1){
                total_enero=total_enero +productividad.get(i).getUste_completed_terminals();
            }

            if(mes==2){
                total_febrero=total_febrero +productividad.get(i).getUste_completed_terminals();
            }

            if(mes==3){
                total_marzo=total_marzo+productividad.get(i).getUste_completed_terminals();
            }

            if(mes==4){
                total_abril=total_abril +productividad.get(i).getUste_completed_terminals();
            }

            if(mes==5){
                total_mayo=total_mayo +productividad.get(i).getUste_completed_terminals();
            }

            if(mes==6){
                total_junio=total_junio +productividad.get(i).getUste_completed_terminals();
            }

            if(mes==7){
                total_julio=total_julio +productividad.get(i).getUste_completed_terminals();
            }
            if(mes==8){
                total_agosto=total_agosto+productividad.get(i).getUste_completed_terminals();
            }

            if(mes==9){
                total_septiembre=total_septiembre +productividad.get(i).getUste_completed_terminals();
            }

            if(mes==10){
                total_octubre=total_octubre +productividad.get(i).getUste_completed_terminals();
            }
            if(mes==11){
                total_enero=total_noviembre +productividad.get(i).getUste_completed_terminals();
            }
            if(mes==12){
                total_enero=total_diciembre +productividad.get(i).getUste_completed_terminals();
            }

        }


        total [0]=total_enero;
        total [1]=total_febrero;
        total [2]=total_marzo;
        total [3]=total_abril;
        total [4]=total_mayo;
        total [5]=total_junio;
        total [6]=total_julio;
        total [7]=total_agosto;
        total [8]=total_septiembre;
        total [9]=total_octubre;
        total [10]=total_noviembre;
        total [1]=total_diciembre;



        return total;

    }



}
