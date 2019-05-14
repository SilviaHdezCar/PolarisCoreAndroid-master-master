package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.wposs_user.polariscoreandroid.java.MyValueFormatter;
import com.example.wposs_user.polariscoreandroid.java.Productividad;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static java.util.Collections.sort;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Productividad_mes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Productividad_mes#newInstance} factory method to
 * create an instance of this fragment.
 */


public class Productividad_mes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private View v;
    private RequestQueue queue;
    private ArrayList<Productividad> productividad;
    private Spinner mes;
    private Spinner año;
    private Button produc;
    private BarChart grafica;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Productividad_mes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Productividad_mes.
     */
    // TODO: Rename and change types and number of parameters
    public static Productividad_mes newInstance(String param1, String param2) {
        Productividad_mes fragment = new Productividad_mes();
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
        v= inflater.inflate(R.layout.fragment_productividad_mes, container, false);
      mes = (Spinner)v.findViewById(R.id.spin_mesxmes);
      año= (Spinner)v.findViewById(R.id.spiner_añoxmes);
      produc=(Button)v.findViewById(R.id.produc_mes);
     grafica=(BarChart) v.findViewById(R.id.grafica_mes);
        queue = Volley.newRequestQueue(objeto);
         produc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumirServicioProductividadMes();

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

    public void consumirServicioProductividadMes() {




        grafica.clear();

        String mesDado= mes.getSelectedItem().toString();
        String añoDado= año.getSelectedItem().toString();


        productividad=new ArrayList<>();




        if(mes.getSelectedItem()==null||año.getSelectedItem()==null){
            Toast.makeText(v.getContext(),"Debe selecccionar el mes y año a consultar",Toast.LENGTH_SHORT).show();
            grafica.setVisibility(INVISIBLE);
            return;

        }

        if(mes.getSelectedItem().toString().equals("Selecccione")||año.getSelectedItem().equals("Seleccione")){
            Toast.makeText(v.getContext(),"Seleccione un mes y año valido",Toast.LENGTH_SHORT).show();
            grafica.setVisibility(INVISIBLE);
            return;

        }

        int mes_selecionado= this.getMes(mesDado);
        final int can_dias=this.getDiasMes(mes_selecionado);


        String fecha_inicio=mes_selecionado+"/"+1+"/"+ añoDado;
        String fecha_fin=mes_selecionado+"/"+can_dias+"/"+ añoDado;



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
                                grafica.clear();
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
          ArrayList<BarEntry>diagnosticadas= new ArrayList<>();
          ArrayList<BarEntry>reparadas= new ArrayList<>();





          /*    System.out.println(productividad.toString());

              BarGraphSeries<DataPoint> diagnostico = null;
              BarGraphSeries<DataPoint>  reparadas= null;
              DataPoint[] datos1= new DataPoint[productividad.size()];
              DataPoint[] datos2= new DataPoint[productividad.size()];
              diagnostico = new BarGraphSeries<>(new DataPoint[]{});
              reparadas = new BarGraphSeries<>(new DataPoint[]{ });*/
              Productividad[] pro= llenarSerie();
              Arrays.sort(pro);



              for(int i=0;i<pro.length-1;i++) {

                  String fecha_rta = pro[i].getUste_date();
                  String[] fechas = fecha_rta.split("/");
                  int diaDado = Integer.parseInt(fechas[0]);
                  diagnosticadas.add(new BarEntry(diaDado,productividad.get(i).getUste_associated_terminals()));
                  reparadas.add(new BarEntry(diaDado,productividad.get(i).getUste_completed_terminals()));

                           }


              BarDataSet datos=new BarDataSet(diagnosticadas,"Diagnosticadas");
              BarDataSet valores=new BarDataSet(reparadas,"Reparadas");
              datos.setColor(Color.RED);
              valores.setColor(Color.GREEN);
              datos.setDrawValues(true);

              valores.setDrawValues(true);

              datos.setValueFormatter(new MyValueFormatter());
              valores.setValueFormatter(new MyValueFormatter());


              BarData datosGrafica = new BarData(datos,valores);
              grafica.setData(datosGrafica);
              // String []meses= new String[]{"enero", "febrero", "marzo", "abril", "mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
              XAxis x=grafica.getXAxis();
              //  x.setValueFormatter(new IndexAxisValueFormatter(meses));
              x.setCenterAxisLabels(true);
              x.setLabelCount(0);
              x.setPosition(XAxis.XAxisPosition.BOTTOM);
              x.setGranularity(1);
              x.setGranularityEnabled(true);
              grafica.setDragEnabled(true);
              grafica.setVisibleXRangeMaximum(5);
              float barSpace=0.02f;
              float groupSpace= 0.8f;
              datosGrafica.setBarWidth(0.1f);
              grafica.getXAxis().setAxisMinimum(1);
              grafica.getXAxis().setAxisMaximum(can_dias);
              grafica.groupBars(0, groupSpace,barSpace);
              grafica.invalidate();
              grafica.getDescription().setEnabled(false);
              grafica.setVisibility(VISIBLE);



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

    /*****************metodo para convertir el mes a int*****************/

public int getMes(String meses){

        if(meses.equals("Enero")){ return 1;}
        if(meses.equals("Febrero")){ return 2;}
        if(meses.equals("Marzo")){ return 3;}
        if(meses.equals("Abril")) return 4;
        if(meses.equals("Mayo")){ return 5;}
        if(meses.equals("Junio")){ return 6;}
        if(meses.equals("Julio")){ return 7;}
        if(meses.equals("Agosto")){ return 8;}
        if(meses.equals("Septiembre")){ return 9;}
        if(meses.equals("Octubre")){ return 10;}
        if(meses.equals("noviembre")){ return 11;}
        if(meses.equals("Diviembre")){ return 12;}

        return 0;

}


    /****************metodo para obtener el numero de dias en cada mes*********************/

      public int getDiasMes(int mesdia){

          if(mesdia==1){return 31;}
          if(mesdia==2){return 28;}
          if(mesdia==3){return 31;}
          if(mesdia==4){return 30;}
          if(mesdia==5){return 31;}
          if(mesdia==6){return 30;}
          if(mesdia==7){return 31;}
          if(mesdia==8){return 31;}
          if(mesdia==9){return 30;}
          if(mesdia==10){return 31;}
          if(mesdia==11) { return 30;}
          if (mesdia == 12) { return 31; }

              return 0;

          }

/*************************Metodo para pasar el arrayList a vector para ordenarlo y graficarlo*******************/

 public Productividad[] llenarSerie(){

          Productividad[] p = new Productividad[productividad.size()];

          for(int i=0;i<productividad.size();i++){

              p[i]=productividad.get(i);

           }

          return p;

 }







}
