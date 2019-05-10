package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.security.acl.Group;
import java.util.ArrayList;

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
    private int id_fila;
    private int num_celda = 1;


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
        tabla = (TableLayout) v.findViewById(R.id.tabla_validaciones_autorizadas);
        btn_siguiente = (Button) v.findViewById(R.id.btn_siguiente_seleccionar_validaciones_autorizadas);

        llenarTabla();

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorrerTabla(tabla);
            }
        });


        return v;
    }


    public void recorrerTabla(TableLayout tabla) {
        for (int i = 1; i < Global.VALIDACIONES.size(); i++) {

        }

        for (int i = 0; i < tabla.getChildCount(); i++) {
            View child = tabla.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                for (int x = 0; x < row.getChildCount(); x++) {

                    View view = row.getChildAt(x);
                    view.setEnabled(false);
                }
            }
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
            //celdas

            TextView nombre = new TextView(objeto);
            nombre.setId(200 + i);
            nombre.setText(Global.VALIDACIONES.get(i).getTeva_description());
            nombre.setWidth(2);

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
