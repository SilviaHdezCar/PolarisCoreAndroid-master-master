package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEtapa;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogMostarFoto;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static java.util.Collections.sort;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Prediagnostico.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Prediagnostico#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Prediagnostico extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    private View v;

    private TextView serial;
    private Button btn_observaciones;
    private Button btn_prediagnostico;
    private Button btn_reparacion;
    private Button btn_qa;

    private LinearLayout ly_observaciones;
    private LinearLayout ly_prediagnostico;
    private LinearLayout ly_reparacion;
    private LinearLayout ly_qa;
    private TableLayout tablaRepuestos;
    private LinearLayout layout_observaciones;
    private LinearLayout layout_prediagnostico;
    private LinearLayout layout_evidencias;
    private LinearLayout layout_repuestos;
    private LinearLayout layout_validaciones;
    private LinearLayout layout_tipificaciones;

    private RecyclerView rv;
    private List<Repuesto> repuestos;

    private ImageView img_evidencia1;
    private ImageView img_evidencia2;
    private TextView txt_nomFoto1;
    private TextView txt_nomFoto2;
    private TextView txt_fechaFoto1;
    private TextView txt_fechaFoto2;

    private boolean tieneRepuestos;

    public Prediagnostico() {
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
    public static Prediagnostico newInstance(String param1, String param2) {
        Prediagnostico fragment = new Prediagnostico();
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
        v = inflater.inflate(R.layout.fragment_prediagnostico, container, false);
        objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("DIAGNÓSTICOS");
        this.repuestos = new ArrayList<Repuesto>();
        serial = (TextView) v.findViewById(R.id.serial_diagnosticos);

        btn_observaciones = (Button) v.findViewById(R.id.btn_observaciones);
        btn_prediagnostico = (Button) v.findViewById(R.id.btn_terminales_prediagnostico);//Sería DIAGNOSTICO
        btn_reparacion = (Button) v.findViewById(R.id.btn_terminales_reparacion);
        btn_qa = (Button) v.findViewById(R.id.btn_terminales_qa);
        ly_observaciones = (LinearLayout) v.findViewById(R.id.select_observaciones);
        ly_prediagnostico = (LinearLayout) v.findViewById(R.id.select_prediagnostico);
        ly_reparacion = (LinearLayout) v.findViewById(R.id.select_reparacion);
        ly_qa = (LinearLayout) v.findViewById(R.id.select_qa);

        tablaRepuestos = (TableLayout) v.findViewById(R.id.tabla_repuestos_prediagnostico);
        layout_observaciones = (LinearLayout) v.findViewById(R.id.layout_observaciones);
        layout_prediagnostico = (LinearLayout) v.findViewById(R.id.layout_prediagnostico);
        layout_evidencias = (LinearLayout) v.findViewById(R.id.layout_evidencias_prediagnostico);
        layout_repuestos = (LinearLayout) v.findViewById(R.id.layout_repuestos_prediagnostico);
        layout_validaciones = (LinearLayout) v.findViewById(R.id.layout_validaciones);
        layout_tipificaciones = (LinearLayout) v.findViewById(R.id.layout_tipificaciones);

        img_evidencia1 = (ImageView) v.findViewById(R.id.img_evidencia1_qa);
        img_evidencia2 = (ImageView) v.findViewById(R.id.img_evidencia2_qa);
        txt_nomFoto1 = (TextView) v.findViewById(R.id.txt_nomFoto1_qa);
        txt_nomFoto2 = (TextView) v.findViewById(R.id.txt_nomFoto2_qa);
        txt_fechaFoto1 = (TextView) v.findViewById(R.id.txt_fechaFoto1_qa);
        txt_fechaFoto2 = (TextView) v.findViewById(R.id.txt_fechaFoto2_qa);


        styleObservacioneso();
        System.out.println("***********************ANTES DEL SORT***********************");
        for (Observacion ob : Global.OBSERVACIONES) {
            System.out.println("----FECHA OB: " + ob.getTeob_fecha());
        }
        Collections.sort(Global.OBSERVACIONES);

        System.out.println("***********************DESPUES DEL SORT***********************");
        for (Observacion ob : Global.OBSERVACIONES) {
            System.out.println("----FECHA OB: " + ob.getTeob_fecha());
        }

        llenarRVEtapas(Global.OBSERVACIONES);
        layout_prediagnostico.setVisibility(View.GONE);

        //al iniciar se debe mostrar todo lo del prediagnostico


        btn_observaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleObservacioneso();
                System.out.println("Tamaño de las observaciones: " + Global.OBSERVACIONES.size());
                llenarRVEtapas(Global.OBSERVACIONES);
                layout_observaciones.setVisibility(View.VISIBLE);
                layout_prediagnostico.setVisibility(View.GONE);

            }
        });

        btn_prediagnostico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stylePrediagnostico();
                layout_prediagnostico.setVisibility(View.VISIBLE);
                layout_validaciones.setVisibility(View.VISIBLE);
                layout_tipificaciones.setVisibility(View.VISIBLE);
                layout_repuestos.setVisibility(View.VISIBLE);
                layout_observaciones.setVisibility(View.GONE);
                layout_evidencias.setVisibility(View.GONE);
                llenarDiagnostico("1");


            }
        });

        btn_reparacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleReparacion();
                layout_prediagnostico.setVisibility(View.VISIBLE);
                layout_validaciones.setVisibility(View.VISIBLE);
                layout_tipificaciones.setVisibility(View.VISIBLE);
                layout_repuestos.setVisibility(View.VISIBLE);
                layout_observaciones.setVisibility(View.GONE);
                layout_evidencias.setVisibility(View.GONE);
                llenarDiagnostico("2");

            }
        });

        btn_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleQA();
                layout_prediagnostico.setVisibility(View.VISIBLE);
                layout_validaciones.setVisibility(View.VISIBLE);
                layout_tipificaciones.setVisibility(View.GONE);
                layout_repuestos.setVisibility(View.GONE);
                layout_observaciones.setVisibility(View.GONE);
                layout_evidencias.setVisibility(View.VISIBLE);
                llenarDiagnostico("3");

            }
        });

        serial.setText("Serial: " + Global.terminalVisualizar.getTerm_serial());
        if (Global.terminalVisualizar.getTerm_status().equals("DADO DE BAJA")) {
            btn_reparacion.setVisibility(View.GONE);
            btn_qa.setVisibility(View.VISIBLE);
            btn_prediagnostico.setVisibility(View.GONE);
        }

        return v;
    }


    /**
     * Metodo utilizado para llenar el recycler view de las observaciones del terminal seleccionado
     *
     * @Params Recibe la lista  observaciones o etapas que van a ser mostradas
     **/
    public void llenarRVEtapas(List<Observacion> observaciones) {

        rv = (RecyclerView) v.findViewById(R.id.recycler_view_observaciones_prediagnosticoÒ);
        if (observaciones == null || observaciones.size() == 0) {
            Toast.makeText(objeto, "Sin observaciones", Toast.LENGTH_SHORT).show();
        }


        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        ArrayList observations = new ArrayList<>();

        for (int i = observaciones.size() - 1; i >= 0; i--) {
            if (observaciones.get(i) != null) {
                if (!observaciones.get(i).getTeob_description().trim().isEmpty()) {
                    observations.add(observaciones.get(i));
                }
            }
        }

        final AdapterEtapa adapter = new AdapterEtapa(observations, new AdapterEtapa.interfaceClick() {
            @Override
            public void onClick(List<Observacion> observaciones, int position) {

            }
        }, R.layout.panel_etapas);

        rv.setAdapter(adapter);

    }


    /**
     * @param tipo -->-->1. diagnostico, 2.repación. 3. qa
     */
    public void llenarDiagnostico(String tipo) {

        //dependiendo del tipo, ocultar paneles
        layout_observaciones.setVisibility(View.GONE);
        layout_prediagnostico.setVisibility(View.VISIBLE);
        ArrayList<Validacion> validacions = llenarListaValidaciones(tipo);
        if (tipo.equals("3")) {
            if (validacions == null || validacions.size() == 0) {
                layout_validaciones.setVisibility(View.GONE);
            }
            layout_evidencias.setVisibility(View.VISIBLE);
            mostrarEvidencias();
            return;
        }

        ArrayList<String> tipificaciones = llenarListaTipificaciones(tipo);
        ArrayList<Repuesto> repuestos = llenarListaRepuestos(tipo);
        if (validacions == null || validacions.size() == 0) {
            layout_validaciones.setVisibility(View.GONE);
        }
        if (tipificaciones == null || tipificaciones.size() == 0) {
            layout_tipificaciones.setVisibility(View.GONE);
        }
        if (!tieneRepuestos(repuestos)) {
            layout_repuestos.setVisibility(View.GONE);
        }


    }

    /**
     * este metodo se ultiliza para llenar la lista de validaciones
     *
     * @param tipodiagnostico-->1. prediagnostico, 2.repación. 3. qa
     * @return
     */
    public ArrayList<Validacion> llenarListaValidaciones(String tipodiagnostico) {
        ArrayList<Validacion> validacions = new ArrayList<>();
        if (Global.validaciones_consultas.get(tipodiagnostico) != null) {
            if (!Global.validaciones_consultas.get(tipodiagnostico).equalsIgnoreCase("")) {
                String array_validaciones[] = Global.validaciones_consultas.get(tipodiagnostico).split("%");

                String validaciones[] = array_validaciones[1].split(",");
                String vali_estado[];
                if (!validaciones[0].equals("[]")) {

                    for (int i = 0; i < validaciones.length; i++) {
                        if (!validaciones[i].equalsIgnoreCase("[]")) {
                            boolean ok = false, falla = false, no_aplica = false;
                            String estado = "";
                            vali_estado = validaciones[i].split("-");

                            if (vali_estado[1].equalsIgnoreCase("OK")) {
                                ok = true;
                                estado = "ok";
                            } else if (vali_estado[1].equalsIgnoreCase("Falla")) {
                                falla = true;
                                estado = "falla";
                            } else if (vali_estado[1].equalsIgnoreCase("na")) {
                                no_aplica = true;
                                estado = "no aplica";
                            }
                            if (validaciones[i].split("-")[1].equals("OK")) {
                                ok = true;
                            } else if (validaciones[i].split("-")[1].equals("Falla")) {
                                falla = true;
                            } else if (validaciones[i].split("-")[1].equals("No aplica")) {
                                no_aplica = true;
                            }

                            Validacion v = new Validacion(validaciones[i].split("-")[0], ok, falla, no_aplica, estado);
                            validacions.add(v);
                        }

                    }
                    llenarRVValidaciones(validacions);
                }
            }
        }
        return validacions;
    }


    /**
     * Metodo empleado para llenar el recycler view de validaciones
     *
     * @param validacionesRecibidas
     */
    public void llenarRVValidaciones(List<Validacion> validacionesRecibidas) {

        rv = (RecyclerView) v.findViewById(R.id.recycler_view_validaciones_pregiagnostico);


        if (validacionesRecibidas == null || validacionesRecibidas.size() == 0) {
            Toast.makeText(objeto, "No tiene validaciones", Toast.LENGTH_SHORT).show();
        }
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
    }


    /**
     * Recorre el arreglo global de tipificaciones, el cual se llenó al consumir el servicio que consulta la terminal
     *
     * @param estado -->1. prediagnostico, 2.repación. 3. qa
     */
    public ArrayList<String> llenarListaTipificaciones(String estado) {
        ArrayList<String> tipificacions = new ArrayList<>();
        if (Global.tipificaciones_consultas.get(estado) != null) {
            if (!Global.tipificaciones_consultas.get(estado).equalsIgnoreCase("")) {
                String arreglo_tipificaciones[] = Global.tipificaciones_consultas.get(estado).split("%");
                String tipificaciones[] = arreglo_tipificaciones[1].split(",");
                if (!tipificaciones[0].equals("[]")) {


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
        }
        return tipificacions;
    }

    /**
     * este metodo llena el recycler view con las tipificaciones obtenidas al consumir el
     * servicio que muestra el detalle de la terminal autorizada seleccionada
     **/
    public void llenarRVTipificaciones(List<String> tipificacionesRecibidas) {
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones_prediagnostico);

        if (tipificacionesRecibidas == null || tipificacionesRecibidas.size() == 0) {
            Toast.makeText(objeto, "No tiene tificaciones", Toast.LENGTH_SHORT).show();
        }
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

    }


    /**
     * Este metodo se utiliza para recorrer el arreglo de repuestos enviado por el servicio al seleccionar una autorizada
     * Split de los repuestos recibidos y los agrega al recycler view
     **/
    public ArrayList<Repuesto> llenarListaRepuestos(String estado) {
        ArrayList<Repuesto> repuests = new ArrayList<>();
        if (Global.repuestos_consultas.get(estado) != null) {
            if (!Global.repuestos_consultas.get(estado).equalsIgnoreCase("")) {
                String arrayRepuestos[] = Global.repuestos_consultas.get(estado).split("%");
                System.out.println("Array repuestos: " + arrayRepuestos.toString());
                System.out.println("Array repuestos pos 0: " + arrayRepuestos[0]);
                if (arrayRepuestos[1] != null || !arrayRepuestos[1].isEmpty()) {
                    String reps[] = arrayRepuestos[1].split(",");
                    this.repuestos = new ArrayList<>();
                    if (reps == null || reps.length == 0 || reps.equals("")) {
                        Toast.makeText(objeto, "No tiene repuestos", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!reps[0].equals("[]") || !reps[0].trim().isEmpty()) {
                            Repuesto repuesto = null;
                            String[] rep = null;
                            for (int i = 0; i < reps.length; i++) {
                                rep = reps[i].split("-");
                                if (rep != null || rep.length > 0) {
                                    //String spar_code,String spar_name, String quantity, String spar_warehouse
                                    repuesto = new Repuesto(rep[0], rep[1], rep[2], rep[3]);
                                    this.repuestos.add(repuesto);
                                    repuests.add(repuesto);
                                }
                            }

                        }
                    }
                }

            }
        }
        return repuests;
    }

    /**
     * metodo utilizado para validar si la terminal tiene repuestos
     *
     * @return true--> si tiene repuesto (llena la tabla y oculta el panel de evidencias) false--> no tiene repuestos
     */
    public boolean tieneRepuestos(List<Repuesto> repuestos) {
        if (repuestos != null) {
            if (repuestos.size() > 0) {
                llenarTabla(repuestos);
                layout_evidencias.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo utilizado para llenar la tabla de respuestos con la columna OK
     **/
    public void llenarTabla(List<Repuesto> repuestos) {
        tablaRepuestos.removeAllViews();
        for (int i = 0; i < repuestos.size(); i++) {
            TableRow fila = new TableRow(objeto);
            fila.setId(i);
            fila.setGravity(Gravity.CENTER_HORIZONTAL);
            fila.setBackgroundResource(R.drawable.borde_inferior_gris);
            fila.setPadding(1, 10, 1, 10);

            //celdas

            TextView nombre = new TextView(objeto);
            nombre.setId(100 + i);
            nombre.setText(repuestos.get(i).getSpar_name());
            nombre.setGravity(Gravity.CENTER_HORIZONTAL);
            nombre.setPadding(30, 10, 1, 10);

            fila.addView(nombre);
            tablaRepuestos.addView(fila);
        }
    }

    /**
     * Metodo utilizado al mostrar el diagnóstico realizado por QA
     */
    public void mostrarEvidencias() {

        String fechaFoto1 = "";
        String fechaFoto2 = "";
        System.out.println("Tamaño del arreglo observaciones con foto: " + Global.observaciones_con_fotos.size());
        System.out.println("Tamaño del arreglo observaciones: " + Global.OBSERVACIONES.size());
        Global.observaciones_con_fotos = null;
        Global.observaciones_con_fotos = new ArrayList<Observacion>();

        if (Global.OBSERVACIONES != null) {
            if (Global.OBSERVACIONES.size() > 0) {
                for (Observacion o : Global.OBSERVACIONES) {
                    if (o.getTeob_photo() != null || !o.getTeob_photo().equalsIgnoreCase("")) {

                        if (o.getTeob_description().isEmpty() || o.getTeob_description().equals("Observación Imagen QA")) {
                            System.out.println(o.toString());
                            Global.observaciones_con_fotos.add(o);
                        }
                    }
                }
            }

            System.out.println("Observaciones con foto: " + Global.observaciones_con_fotos.size());
            if (Global.observaciones_con_fotos != null) {
                if (Global.observaciones_con_fotos.size() == 1 || Global.observaciones_con_fotos.size() == 2) {

                    if (Global.observaciones_con_fotos.get(0) != null) {
                        if (Global.observaciones_con_fotos.get(0).getTeob_photo() != null || !Global.observaciones_con_fotos.get(0).getTeob_photo().isEmpty()) {
                            final String nombreFoto1 = Global.observaciones_con_fotos.get(0).getTeob_photo();
                            fechaFoto1 = Utils.darFormatoFechaObservaciones(Global.observaciones_con_fotos.get(0).getTeob_fecha());
                            txt_fechaFoto1.setText(fechaFoto1);
                            txt_nomFoto1.setText(nombreFoto1);
                            txt_nomFoto1.setMovementMethod(new ScrollingMovementMethod());
                            System.out.println("Información de la foto: " + nombreFoto1 + "");
                            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + nombreFoto1).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia1);
                            Global.foto=1;
                            Global.rutaFotoObservacion = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + nombreFoto1;
                            img_evidencia1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //inflar fragment evidencias y carga la foto

                                    cargarPanel();

                                }
                            });
                        }

                    }
                    if (Global.observaciones_con_fotos.get(1) != null) {
                        if (Global.observaciones_con_fotos.get(1).getTeob_photo() != null || !Global.observaciones_con_fotos.get(1).getTeob_photo().isEmpty()) {
                            final String nombreFoto2 = Global.observaciones_con_fotos.get(1).getTeob_photo();
                            System.out.println("fecha 2: " + Global.observaciones_con_fotos.get(1).getTeob_fecha());
                            fechaFoto2 = Utils.darFormatoFechaObservaciones(Global.observaciones_con_fotos.get(1).getTeob_fecha());
                            System.out.println("String fecha 2: " + fechaFoto2);
                            txt_fechaFoto2.setText(fechaFoto2);
                            txt_nomFoto2.setText(nombreFoto2);

                            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + nombreFoto2).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia2);
                            Global.foto=2;
                            Global.rutaFotoObservacion2 = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + nombreFoto2;
                            img_evidencia2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //inflar fragment evidencias y carga la foto
                                   cargarPanel();

                                }
                            });
                        }

                    }

                }

            }
        }
    }

    public void cargarPanel() {
        DialogMostarFoto dialog = new DialogMostarFoto();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }

    public void styleObservacioneso() {
        ly_observaciones.setBackgroundColor(getResources().getColor(R.color.blanca_linea));
        ly_prediagnostico.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_reparacion.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_qa.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));

        btn_observaciones.setTextSize(15);
        btn_prediagnostico.setTextSize(13);
        btn_reparacion.setTextSize(13);
        btn_qa.setTextSize(13);
        btn_observaciones.setTypeface(null, Typeface.BOLD);
        btn_prediagnostico.setTypeface(null, Typeface.NORMAL);
        btn_reparacion.setTypeface(null, Typeface.NORMAL);
        btn_qa.setTypeface(null, Typeface.NORMAL);


    }

    public void stylePrediagnostico() {
        ly_prediagnostico.setBackgroundColor(getResources().getColor(R.color.blanca_linea));
        ly_reparacion.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_qa.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_observaciones.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));

        btn_observaciones.setTextSize(13);
        btn_observaciones.setTypeface(null, Typeface.NORMAL);
        btn_prediagnostico.setTextSize(15);
        btn_reparacion.setTextSize(13);
        btn_qa.setTextSize(13);
        btn_prediagnostico.setTypeface(null, Typeface.BOLD);
        btn_reparacion.setTypeface(null, Typeface.NORMAL);
        btn_qa.setTypeface(null, Typeface.NORMAL);


    }

    public void styleReparacion() {
        ly_prediagnostico.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_reparacion.setBackgroundColor(getResources().getColor(R.color.blanca_linea));
        ly_qa.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_observaciones.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));

        btn_observaciones.setTextSize(13);
        btn_observaciones.setTypeface(null, Typeface.NORMAL);
        btn_prediagnostico.setTextSize(13);
        btn_reparacion.setTextSize(15);
        btn_qa.setTextSize(13);
        btn_prediagnostico.setTypeface(null, Typeface.NORMAL);
        btn_reparacion.setTypeface(null, Typeface.BOLD);
        btn_qa.setTypeface(null, Typeface.NORMAL);
    }

    public void styleQA() {
        ly_prediagnostico.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_qa.setBackgroundColor(getResources().getColor(R.color.blanca_linea));
        ly_reparacion.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_observaciones.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));

        btn_observaciones.setTextSize(13);
        btn_observaciones.setTypeface(null, Typeface.NORMAL);
        btn_prediagnostico.setTextSize(13);
        btn_reparacion.setTextSize(13);
        btn_qa.setTextSize(15);
        btn_prediagnostico.setTypeface(null, Typeface.NORMAL);
        btn_reparacion.setTypeface(null, Typeface.NORMAL);
        btn_qa.setTypeface(null, Typeface.BOLD);
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
