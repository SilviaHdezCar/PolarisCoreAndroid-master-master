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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEvidenciasAutorizadas;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.squareup.picasso.Picasso;

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

    private RecyclerView rv;/*
    private RecyclerView rvFotos;
    private RecyclerView rvRepuestos;*/
    private TableLayout tablaRepuestos;
    private LinearLayout layout_evidencias;
    private LinearLayout layout_repuestos;

    private ImageView img_evidencia1;
    private ImageView img_evidencia2;
    private TextView txt_nomFoto1;
    private TextView txt_nomFoto2;
    private TextView txt_fechaFoto1;
    private TextView txt_fechaFoto2;


    public static ArrayList<Observacion> list_con_fotos;

    private List<Repuesto> repuestos;
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
        objeto.setTitle("          TIPIFICACIONES");
        list_con_fotos = new ArrayList<Observacion>();
        this.repuestos = new ArrayList<Repuesto>();
        serial = (TextView) v.findViewById(R.id.serial_ter_autorizada);
        marca = (TextView) v.findViewById(R.id.marca_ter_autorizada);
        modelo = (TextView) v.findViewById(R.id.modelo_ter_autorizada);
        tecnologia = (TextView) v.findViewById(R.id.tecno_ter_autorizada);
        estado = (TextView) v.findViewById(R.id.estado_ter_autorizada);
        fechaANS = (TextView) v.findViewById(R.id.fechaANS_ter_autorizada);
        btn_siguiente = (Button) v.findViewById(R.id.btn_siguiente_tipificaciones_autorizadas);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones_autorizadas);
        layout_evidencias = (LinearLayout) v.findViewById(R.id.layout_evidencias);
        layout_repuestos = (LinearLayout) v.findViewById(R.id.layout_repuestos);
        tablaRepuestos = (TableLayout) v.findViewById(R.id.tabla_seleccionar_repuestos);

        img_evidencia1 = (ImageView) v.findViewById(R.id.img_evidencia1);
        img_evidencia2 = (ImageView) v.findViewById(R.id.img_evidencia2);
        txt_nomFoto1 = (TextView) v.findViewById(R.id.txt_nomFoto1);
        txt_nomFoto2 = (TextView) v.findViewById(R.id.txt_nomFoto2);
        txt_fechaFoto1 = (TextView) v.findViewById(R.id.txt_fechaFoto1);
        txt_fechaFoto2 = (TextView) v.findViewById(R.id.txt_fechaFoto2);

        layout_repuestos.setVisibility(View.VISIBLE);
        layout_evidencias.setVisibility(View.VISIBLE);


        System.out.println("TERMINAL: " + Global.terminalVisualizar.getTerm_serial());
        serial.setText(Global.terminalVisualizar.getTerm_serial());
        marca.setText(Global.terminalVisualizar.getTerm_brand());
        modelo.setText(Global.terminalVisualizar.getTerm_model());
        tecnologia.setText(Global.terminalVisualizar.getTerm_technology());
        estado.setText(Global.terminalVisualizar.getTerm_status());
        fechaANS.setText(Global.terminalVisualizar.getTerm_date_register());

        recorrerTipificaciones();
        llenarListaRepuestos();
        System.out.println("Tamaño lista rep=" + Global.repuestos_listar_autorizadas.size());
        System.out.println("Tamaño  rep DEFECTUOSOS=" + Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.size());
        System.out.println("Tamaño lista evidencias=" + Global.observaciones_con_fotos.size());

       if (this.repuestos != null || !(this.repuestos.size() == 0)) {//Tiene repuestos
            layout_evidencias.setVisibility(View.GONE);
            llenarTabla();
            //llenarTabla
        }else if (Global.observaciones_con_fotos != null || Global.observaciones_con_fotos.size() > 0) {
            layout_repuestos.setVisibility(View.GONE);

            Observacion obFoto1=Global.observaciones_con_fotos.get(Global.observaciones_con_fotos.size()-1);
            Observacion obFoto2=Global.observaciones_con_fotos.get(Global.observaciones_con_fotos.size()-2);


           String foto1=obFoto1.getTeob_photo();
           String foto2=obFoto2.getTeob_photo();

            txt_fechaFoto1.setText(foto1);
            txt_fechaFoto2.setText(foto2);
           Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/"+foto1+".jpg").error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia1);

           Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/"+foto1+".jpg").error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia2);

//            llenarRVFotos(Global.observaciones_con_fotos);

        }

        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Global.repuestos_listar_autorizadas != null || !(Global.repuestos_listar_autorizadas.size() == 0)) {
                    if (validarEstadosRepuestos()) {
                        //System.out.println();
                        objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesSeleccionarAutorizadas()).addToBackStack(null).commit();
                        return;

                    }
                } else {
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesSeleccionarAutorizadas()).addToBackStack(null).commit();
                    return;
                }
            }
        });


        return v;
    }

    /**
     * Este metodo se utiliza para verificar que todos los repuestos estén OK
     *
     * @return true-->si todos están oOK
     */
    public boolean validarEstadosRepuestos() {
        boolean retorno = false;
        recorrerTabla(tablaRepuestos);
        Repuesto rep = new Repuesto();
        for (int i = 0; i < this.repuestos.size(); i++) {
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
                    retorno = true;
                }
            }
        }
        return retorno;
    }

    /**
     * Este metodo se utiliza para recorrer la tabla mostrada de repuestos y cambia el estado
     * del repuesto al presionar el radio button     *
     *
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
                if (((RadioButton) view).isChecked()) {
                    this.repuestos.get(i - 1).setOk(true);
                }
            }
            System.out.println("Pos: " + i + "-->" + this.repuestos.get(i - 1).getSpar_name() + "-" + this.repuestos.get(i - 1).isOk());
        }
    }


    /**
     * Metodo utilizado para llenar la tabla de respuestos con la columna OK
     **/
    public void llenarTabla() {


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
            tablaRepuestos.addView(fila);


        }
    }


    /**
     * Este metodo se utiliza para recorrer el arreglo de repuestos enviado por el servicio al seleccionar una autorizada
     * Split de los repuestos recibidos y los agrega al recycler view
     **/
    public void llenarListaRepuestos() {
        System.out.println("va a llenar lista rep defectuosos");
        Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS = null;
        Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS = new ArrayList<Repuesto>();

        String reps[] = Global.repuestos_listar_autorizadas.get(Global.terminalVisualizar.getTerm_serial()).split(",");
        System.out.println("repuestos :::" + reps[0]);
        if (!reps[0].equals("[]")) {

            repuestos = new ArrayList<>();
            Repuesto repuesto = null;
            if ((reps.length == 0) || reps == null) {
                Toast.makeText(objeto, "No tiene repuestos", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < reps.length; i++) {
                    String[] rep = reps[i].split("-");
                    System.out.println("Repuesto" + rep[1]);
                    //String spar_code,String spar_name, String quantity, String spar_warehouse
                    repuesto = new Repuesto(rep[0], rep[1], rep[2], rep[3]);
                    this.repuestos.add(repuesto);
                    Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.add(repuesto);
                }
            }

        }
    }


  /*  //mostrar fotos
    public void llenarRVFotos(List<Observacion> obsRecibidas) {
        if (obsRecibidas == null || obsRecibidas.size() == 0) {
            Toast.makeText(objeto, " No tiene evidencias", Toast.LENGTH_SHORT).show();
            layout_evidencias.setVisibility(View.GONE);
            return;
        }

        rvFotos.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rvFotos.setLayoutManager(llm);

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

        rvFotos.setAdapter(adapter);
    }
*/

    public void recorrerTipificaciones() {

        String tipificaciones[] = Global.tipificaciones_listar_autorizadas.get(Global.terminalVisualizar.getTerm_serial()).split(",");
        System.out.println("Tipificacion pos 0====" + tipificaciones[0]);
        if (!tipificaciones[0].equals("[]")) {

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
        if (tipificacionesRecibidas == null || tipificacionesRecibidas.size() == 0) {
            Toast.makeText(objeto, "No tiene tificaciones", Toast.LENGTH_SHORT).show();
        }
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

    public void ordenarFechas() {

        if (Global.observaciones_con_fotos != null || Global.observaciones_con_fotos.size() > 0) {
            for (Observacion ob : Global.observaciones_con_fotos) {

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
