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
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;

import java.util.ArrayList;
import java.util.List;

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


    private RecyclerView rv;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tipificaciones_autorizadas, container, false);
        serial = (TextView) v.findViewById(R.id.serial_ter_autorizada);
        marca = (TextView) v.findViewById(R.id.marca_ter_autorizada);
        modelo = (TextView) v.findViewById(R.id.modelo_ter_autorizada);
        tecnologia = (TextView) v.findViewById(R.id.tecno_ter_autorizada);
        estado = (TextView) v.findViewById(R.id.estado_ter_autorizada);
        fechaANS = (TextView) v.findViewById(R.id.fechaANS_ter_autorizada);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones_autorizadas);

        System.out.println("TERMINAL: " + Global.terminalVisualizar.getTerm_serial());
        serial.setText(Global.terminalVisualizar.getTerm_serial());
        marca.setText(Global.terminalVisualizar.getTerm_brand());
        modelo.setText(Global.terminalVisualizar.getTerm_model());
        tecnologia.setText(Global.terminalVisualizar.getTerm_technology());
        estado.setText(Global.terminalVisualizar.getTerm_status());
        fechaANS.setText(Global.terminalVisualizar.getTerm_date_register());



        recorrerTipificaciones();

        return v;
    }


    public void recorrerTipificaciones() {

        String tipificaciones[] = Global.tipificacionesAutorizadas.split(",");

        ArrayList<String> tipificacions = new ArrayList<>();

        if (!(tipificaciones.length == 0) || !(tipificaciones == null)) {
            for (int i = 0; i < tipificaciones.length; i++) {
                tipificacions.add(tipificaciones[i]);
            }
            llenarRVValidaciones(tipificacions);
        }


    }

    /**
     * este metodo llena el recycler view con las tipificaciones obtenidas al consumir el
     * servicio que muestra el detalle de la terminal autorizada seleccionada
     **/
    public void llenarRVValidaciones(List<String> tipificacionesRecibidas) {
        //************************SE MUESTRA LA LISTA DE TERMINALES ASOCIADAS
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final ArrayList tipificaciones = new ArrayList<>();

        for (String val : tipificacionesRecibidas) {
            if (val != null) {
                tipificaciones.add(val);
            }

            final AdapterTipificacionesAutorizadas adapter = new AdapterTipificacionesAutorizadas(tipificaciones, null, R.layout.panel_tipificaciones_autorizadas);

            rv.setAdapter(adapter);

        }
        System.out.println("Tamaño del arreglo " + tipificaciones.size());
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
