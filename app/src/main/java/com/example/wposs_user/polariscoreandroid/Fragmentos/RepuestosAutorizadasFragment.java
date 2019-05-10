package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
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

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterRepuestosAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import java.util.ArrayList;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepuestosAutorizadasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepuestosAutorizadasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepuestosAutorizadasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;
    private TextView serial;
    private TextView marca;
    private TextView modelo;
    private TextView tecnologia;
    private TextView estado;
    private TextView fechaANS;


    private Button btn_siguiente;

    private RecyclerView rv;

    private List<Repuesto> repuestos;


    private OnFragmentInteractionListener mListener;

    public RepuestosAutorizadasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepuestosAutorizadasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepuestosAutorizadasFragment newInstance(String param1, String param2) {
        RepuestosAutorizadasFragment fragment = new RepuestosAutorizadasFragment();
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
        v = inflater.inflate(R.layout.fragment_repuestos_autorizadas, container, false);

        this.repuestos = new ArrayList<Repuesto>();

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


        btn_siguiente = (Button) v.findViewById(R.id.btn_siguiente_repuestos_autorizadas);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_repuestos_autorizadas);
        //
        recorrerRepuestos();

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarSeleccionRepuestos()){
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesSeleccionarAutorizadas()).addToBackStack(null).commit();
                    return;

                }
            }
        });
        return v;
    }


    /**
     * Este metodo se utiliza para recorrer el arreglo de repuestos enviado por el servicio al seleccionar una autorizada
     * Split de los repuestos recibidos y los agrega al recycler view
     **/
    public void recorrerRepuestos() {

        if (!Global.repuestosAutorizadas.equals("[]")) {

            String tipificaciones[] = Global.repuestosAutorizadas.split(",");

            ArrayList<Repuesto> repuestos = new ArrayList<>();
            Repuesto repuesto = null;
            if ((tipificaciones.length == 0) || tipificaciones == null) {
                Toast.makeText(objeto, "No tiene tipificaciones", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < tipificaciones.length; i++) {
                    String[] rep = tipificaciones[i].split("-");
                    System.out.println("Repuesto" + rep[1]);
                    //String spar_code,String spar_name, String quantity, String spar_warehouse
                    repuesto = new Repuesto(rep[0], rep[1], rep[2], rep[3]);
                    repuestos.add(repuesto);
                }
                llenarRVRepuestos(repuestos);
            }

        }
    }


    //este metodo llena el recycler view con los repuestos recibidos
    public void llenarRVRepuestos(List<Repuesto> repuestos) {
        final ArrayList tipificaciones = new ArrayList<>();


        if (repuestos.size() == 0 || repuestos == null) {
            Toast.makeText(objeto, "No tiene repuestos", Toast.LENGTH_SHORT).show();
        }
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);
        this.repuestos.clear();
        //final ArrayList validaciones = new ArrayList<>();

        for (Repuesto val : repuestos) {
            if (val != null) {
                this.repuestos.add(val);
            }

            //  System.out.println("LLENANDO RV.....");
            final AdapterRepuestosAutorizadas adapter = new AdapterRepuestosAutorizadas(this.repuestos, new AdapterRepuestosAutorizadas.interfaceClick() {
                @Override
                public void onClick(List<Repuesto> listRepuestos, int position, int pos_radio) {
                    //      System.out.println("al dar clic---- position " + position + "pos_radio " + pos_radio);
                    //    System.out.println("Fragment validaciones"+listValidaciones.get(position).getTeva_description()+"-"+listValidaciones.get(position).getEstado());
                    System.out.println("Adapter validaciones inicio: " + position + ":-" + listRepuestos.get(position).getSpar_name() + "-" + listRepuestos.get(position).isOk());
                }


            }, R.layout.panel_repuestos_autorizadas);

            rv.setAdapter(adapter);

        }


    }


    //Armo el arraylist     que voy a enviar al consumir el servicio de registrar diagnostico
    public boolean validarSeleccionRepuestos() {
        boolean retorno = false;
        for (Repuesto rep : this.repuestos) {
            if (rep != null) {
                if (!rep.isOk()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(objeto);
                    alertDialog.setTitle("INFORMACIÓN");
                    alertDialog.setMessage("Seleccione el repuesto: " + rep.getSpar_name());
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                    return false;
                }else{
                    retorno=true;
                }
            }
        }
        return retorno;
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