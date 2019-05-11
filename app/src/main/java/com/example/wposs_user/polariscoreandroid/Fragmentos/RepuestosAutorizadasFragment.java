package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private TableLayout tabla;

    private Button btn_siguiente;


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
        tabla= (TableLayout)v.findViewById(R.id.tabla_seleccionar_repuestos);

        llenarTabla();

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarEstadosRepuestos()){
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesSeleccionarAutorizadas()).addToBackStack(null).commit();
                    return;

                }
            }
        });
        return v;
    }


    /**
     * Este metodo se utiliza para verificar que todos los repuestos estén OK
     * @return true-->si todos están oOK
     */
    public boolean validarEstadosRepuestos() {
        boolean retorno = false;
        recorrerTabla(tabla);
        Repuesto rep = new Repuesto();
        for (int i = 0; i <this.repuestos.size(); i++) {
            rep = this.repuestos.get(i);
            if (rep != null) {
                if (!rep.isOk()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                    alertDialog.setTitle("Información");
                    alertDialog.setMessage("Faltó seleccionar el repuesto: " + rep.getSpar_name());
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                    return false;
                } else {
                    retorno=true;
                }
            }
        }
        return retorno;
    }


    /**
     * Este metodo se utiliza para recorrer la tabla mostrada de repuestos y cambia el estado
     * del repuesto al presionar el radio button     *
     * @param tabla
     */
    public void recorrerTabla(TableLayout tabla) {

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
                if(((RadioButton) view).isChecked()){
                    this.repuestos.get(i-1).setOk(true);
                }
            }
            System.out.println("Pos: " + i + "-->" +this.repuestos.get(i - 1).getSpar_name() + "-" +  this.repuestos.get(i-1).isOk());
        }
    }

    /**
     * Metodo utilizado para llenar la tabla de respuestos con la columna OK
     **/
    public void llenarTabla() {
        llenarListaRepuestos();

        for (int i = 0; i < this.repuestos.size(); i++) {
            TableRow fila = new TableRow(objeto);
            fila.setId(i);
            fila.setGravity(View.TEXT_ALIGNMENT_CENTER);
            //celdas

            TextView nombre = new TextView(objeto);
            nombre.setId(100 + i);
            nombre.setText(this.repuestos.get(i).getSpar_name());

            RadioButton ok = new RadioButton(objeto);
            ok.setId(200 + i);
            ok.setChecked(false);

            fila.addView(nombre);
            fila.addView(ok);
            tabla.addView(fila);


        }
    }



    /**
     * Este metodo se utiliza para recorrer el arreglo de repuestos enviado por el servicio al seleccionar una autorizada
     * Split de los repuestos recibidos y los agrega al recycler view
     **/
    public void llenarListaRepuestos() {
        Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS=null;
        Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS=new ArrayList<Repuesto>();
        if (!Global.repuestosAutorizadas.equals("[]")) {

            String tipificaciones[] = Global.repuestosAutorizadas.split(",");

            repuestos = new ArrayList<>();
            Repuesto repuesto = null;
            if ((tipificaciones.length == 0) || tipificaciones == null) {
                Toast.makeText(objeto, "No tiene repuestos", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < tipificaciones.length; i++) {
                    String[] rep = tipificaciones[i].split("-");
                    System.out.println("Repuesto" + rep[1]);
                    //String spar_code,String spar_name, String quantity, String spar_warehouse
                    repuesto = new Repuesto(rep[0], rep[1], rep[2], rep[3]);
                    this.repuestos.add(repuesto);
                    Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.add(repuesto);
                }
            }

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