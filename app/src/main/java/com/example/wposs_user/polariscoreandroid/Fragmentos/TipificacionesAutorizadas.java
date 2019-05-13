package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEvidenciasAutorizadas;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TipificacionesAutorizadas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TipificacionesAutorizadas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TipificacionesAutorizadas extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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

    private Button btn_siguiente;

    private RecyclerView rv;


    public static ArrayList<Observacion> list_con_fotos;


    private List<Tipificacion> tipificacionesRecibidas;

    public TipificacionesAutorizadas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TipificacionesAutorizadas.
     */
    // TODO: Rename and change types and number of parameters
    public static TipificacionesAutorizadas newInstance(String param1, String param2) {
        TipificacionesAutorizadas fragment = new TipificacionesAutorizadas();
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

// Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/view/"+Global.ID+".jpg").error(R.mipmap.ic_profile).fit().centerInside().into(imageView);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tipificaciones_autorizadas, container, false);

        list_con_fotos = new ArrayList<Observacion>();
        serial = (TextView) v.findViewById(R.id.serial_ter_autorizada);
        marca = (TextView) v.findViewById(R.id.marca_ter_autorizada);
        modelo = (TextView) v.findViewById(R.id.modelo_ter_autorizada);
        tecnologia = (TextView) v.findViewById(R.id.tecno_ter_autorizada);
        estado = (TextView) v.findViewById(R.id.estado_ter_autorizada);
        fechaANS = (TextView) v.findViewById(R.id.fechaANS_ter_autorizada);
        btn_siguiente = (Button) v.findViewById(R.id.btn_siguiente_tipificaciones_autorizadas);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones_autorizadas);


        System.out.println("TERMINAL: " + Global.terminalVisualizar.getTerm_serial());
        serial.setText(Global.terminalVisualizar.getTerm_serial());
        marca.setText(Global.terminalVisualizar.getTerm_brand());
        modelo.setText(Global.terminalVisualizar.getTerm_model());
        tecnologia.setText(Global.terminalVisualizar.getTerm_technology());
        estado.setText(Global.terminalVisualizar.getTerm_status());
        fechaANS.setText(Global.terminalVisualizar.getTerm_date_register());


        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new RepuestosAutorizadasFragment()).addToBackStack(null).commit();
            }
        });
        recorrerTipificaciones();

        llenarRVFotos(Global.observaciones_con_fotos);

        return v;
    }


    //mostrar fotos
    public void llenarRVFotos(List<Observacion> obsRecibidas) {
        if (obsRecibidas == null || obsRecibidas.size() == 0) {
            Toast.makeText(objeto, " No tiene evidencias", Toast.LENGTH_SHORT).show();
        }

        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        ArrayList obs = new ArrayList<>();

        for (Observacion observa : obsRecibidas) {
            if (observa != null) {
                obs.add(observa);//  butons.add(new ButtonCard(nombre, "","",icon,idVenta));
            }
        }


        final AdapterEvidenciasAutorizadas adapter = new AdapterEvidenciasAutorizadas(obs, new AdapterEvidenciasAutorizadas.interfaceClick() {//seria termi asoc
            @Override
            public void onClick(List<Observacion> lisObs, int position) {


                //   consumirServicioEtapas();

                //muestra la foto en un fragmen


                //objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new EtapasTerminal()).addToBackStack(null).commit();

            }
        }, R.layout.panel_evidencias_autorizadas);

        rv.setAdapter(adapter);
    }


    public void recorrerTipificaciones() {


        if (!Global.tipificacionesAutorizadas.equals("[]")) {

            String tipificaciones[] = Global.tipificacionesAutorizadas.split(",");

            ArrayList<String> tipificacions = new ArrayList<>();

            if ((tipificaciones.length == 0) || tipificaciones == null) {
                Toast.makeText(objeto, "No tiene tipificaciones", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < tipificaciones.length; i++) {
                    tipificacions.add(tipificaciones[i]);
                }
                llenarRVTipificaciones(tipificacions);
            }

        }
    }

/*    public void ordenarObsFotos(){
        if(Global.observaciones_con_fotos!=null|| !(Global.observaciones_con_fotos.size()==0)){
            Observacion obs[]=arrayObservaciones();
            Arrays.sort(obs);
        }
    }

   public  Observacion[] arrayObservaciones(){
        Observacion observaciones[]=new Observacion[Global.observaciones_con_fotos.size()];
       for(int i=0; i<Global.observaciones_con_fotos.size();i++){
           observaciones[i]=Global.observaciones_con_fotos.get(i);
       }
       return observaciones;
   }*/


    /**
     * este metodo llena el recycler view con las tipificaciones obtenidas al consumir el
     * servicio que muestra el detalle de la terminal autorizada seleccionada
     **/
    public void llenarRVTipificaciones(List<String> tipificacionesRecibidas) {
        //************************SE MUESTRA LA LISTA DE TERMINALES ASOCIADAS
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final ArrayList tipificaciones = new ArrayList<>();

        for (String val : tipificacionesRecibidas) {
            if (val != null) {
                System.out.println("arreg tip: " + val);
                tipificaciones.add(val);
            }

            final AdapterTipificacionesAutorizadas adapter = new AdapterTipificacionesAutorizadas(tipificaciones, null, R.layout.panel_tipificaciones_autorizadas);

            rv.setAdapter(adapter);

        }
        System.out.println("Tamaño del arreglo " + tipificaciones.size());
    }


    /**
     * Este metodo se utiliza para revisar cuales observaciones tienen fotos
     *
     * @param
     **/
    public boolean revisarFotos() {
        if (Global.OBSERVACIONES.size() == 0 || Global.OBSERVACIONES == null) {
            return false;
        }

        for (int i = 0; i < Global.OBSERVACIONES.size(); i++) {

        }


        return false;
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
