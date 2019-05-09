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

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEtapa;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidaciones;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.util.ArrayList;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ValidacionTerminalesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ValidacionTerminalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ValidacionTerminalesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button etapaView;
    private Button validacionView;
    private Button btn_siguiente;
    private TextView tituloSerial;
    private View view;

    private RecyclerView rv;

    public ValidacionTerminalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ValidacionTerminalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ValidacionTerminalesFragment newInstance(String param1, String param2) {
        ValidacionTerminalesFragment fragment = new ValidacionTerminalesFragment();
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
        view = inflater.inflate(R.layout.fragment_validacion_terminales, container, false);
        etapaView = (Button) view.findViewById(R.id.btn_etapas);
        validacionView = (Button) view.findViewById(R.id.btn_validacion_terminales_autorizada);
        btn_siguiente = (Button) view.findViewById(R.id.btn_siguiente_validaciones_autorizadas);
        tituloSerial = (TextView) view.findViewById(R.id.tituloSerial);


        tituloSerial.setText(Global.terminalVisualizar.getTerm_serial());

        etapaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new EtapasTerminalAutorizada()).addToBackStack(null).commit();
            }
        });
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new TipificacionesAutorizadas()).addToBackStack(null).commit();
            }
        });
        validacionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionTerminalesFragment()).addToBackStack(null).commit();
            }
        });

        System.out.println("VALIDACIONES Y TIPIFICACIONES SOUT");

        System.out.println(Global.validacionesAutorizadas);
        System.out.println(Global.tipificacionesAutorizadas);

        String validaciones[] = Global.validacionesAutorizadas.split(",");
        ArrayList<Validacion> validacions = new ArrayList<>();
        for (int i = 0; i < validaciones.length; i++) {
            boolean ok = false, falla = false, no_aplica = false;
            if (validaciones[i].split("-")[1].equals("OK")) {
                ok = true;
            } else if (validaciones[i].split("-")[1].equals("Falla")) {
                falla = true;
            } else if (validaciones[i].split("-")[1].equals("No aplica")) {
                no_aplica = true;
            }

            Validacion v = new Validacion(validaciones[i].split("-")[0], ok, falla, no_aplica);
            validacions.add(v);
        }
        rv = (RecyclerView) view.findViewById(R.id.recycler_view_validaciones_autorizadas);
        llenarRVValidaciones(validacions);



        return view;
    }

    //este metodo llena el recycler view con las terminales obtenidas al consumir el servicio

    public void llenarRVValidaciones(List<Validacion> validacionesRecibidas) {
        //************************SE MUESTRA LA LISTA DE TERMINALES ASOCIADAS
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final ArrayList validaciones = new ArrayList<>();

        for (Validacion val : validacionesRecibidas) {
            if (val != null) {
                validaciones.add(val);
            }


            final AdapterValidacionesAutorizadas adapter = new AdapterValidacionesAutorizadas(validaciones, null, R.layout.panel_validaciones_terminales);

            rv.setAdapter(adapter);

        }
        System.out.println("Tamaño del arreglo " + validaciones.size());
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
