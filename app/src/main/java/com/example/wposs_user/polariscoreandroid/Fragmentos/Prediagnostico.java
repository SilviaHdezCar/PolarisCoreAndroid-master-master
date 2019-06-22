package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEtapa;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogMostarFoto;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Validacion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static java.util.Collections.sort;


public class Prediagnostico extends Fragment {


    private View v;

    private TextView serial;
    private Button btn_observaciones;
    private Button btn_prediagnostico;
    private Button btn_reparacion;
    private Button btn_qa;
    private Button btn_evidencias;

    private LinearLayout ly_observaciones;
    private LinearLayout ly_prediagnostico;
    private LinearLayout ly_reparacion;
    private LinearLayout ly_qa;
    private LinearLayout ly_evidencias;
    private TableLayout tablaRepuestos;
    private LinearLayout layout_observaciones;
    private LinearLayout layout_prediagnostico;
    private LinearLayout layout_evidencias;
    private LinearLayout layout_repuestos;
    private LinearLayout layout_validaciones;
    private LinearLayout layout_tipificaciones;
    private LinearLayout tab_prediagnostico;
    private LinearLayout tab_reparacion;
    private LinearLayout tab_qa;
    private LinearLayout tab_evidencias;
    private LinearLayout tab_observaciones;

    private RecyclerView rv;
    private List<Repuesto> repuestos;

    //evidencias
    private ImageView img_evidencia1;
    private ImageView img_evidencia2;
    private ImageView img_evidencia3;
    private ImageView img_evidencia4;

    private boolean tieneRepuestos;

    public Prediagnostico() {
        // Required empty public constructor
    }


    // Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/view/"+Global.ID+".jpg").error(R.mipmap.ic_profile).fit().centerInside().into(imageView);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_prediagnostico, container, false);
        this.inicializaciones();

        validarPestaniasMostrar();

        this.metodosOnCLick();
        return v;
    }

    public void inicializaciones() {
        //   objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("DIAGNÓSTICOS");
        this.repuestos = new ArrayList<Repuesto>();
        serial = (TextView) v.findViewById(R.id.serial_diagnosticos);
        serial.setText("Serial: " + Global.terminalVisualizar.getTerm_serial());
        Collections.sort(Global.OBSERVACIONES);

        btn_observaciones = (Button) v.findViewById(R.id.btn_observaciones);
        btn_prediagnostico = (Button) v.findViewById(R.id.btn_terminales_prediagnostico);//Sería DIAGNOSTICO
        btn_reparacion = (Button) v.findViewById(R.id.btn_terminales_reparacion);
        btn_qa = (Button) v.findViewById(R.id.btn_terminales_qa);
        btn_evidencias = (Button) v.findViewById(R.id.btn_terminales_evidencia);
        ly_observaciones = (LinearLayout) v.findViewById(R.id.select_observaciones);
        ly_prediagnostico = (LinearLayout) v.findViewById(R.id.select_prediagnostico);
        ly_reparacion = (LinearLayout) v.findViewById(R.id.select_reparacion);
        ly_qa = (LinearLayout) v.findViewById(R.id.select_qa);
        ly_evidencias = (LinearLayout) v.findViewById(R.id.select_evidencia);

        tab_prediagnostico = (LinearLayout) v.findViewById(R.id.tab_prediagnostico);
        tab_reparacion = (LinearLayout) v.findViewById(R.id.tab_reparacion);
        tab_qa = (LinearLayout) v.findViewById(R.id.tab_qa);
        tab_evidencias = (LinearLayout) v.findViewById(R.id.tab_evidencias);
        tab_observaciones = (LinearLayout) v.findViewById(R.id.tab_observaciones);

        tablaRepuestos = (TableLayout) v.findViewById(R.id.tabla_repuestos_prediagnostico);
        layout_observaciones = (LinearLayout) v.findViewById(R.id.layout_observaciones);
        layout_prediagnostico = (LinearLayout) v.findViewById(R.id.layout_prediagnostico);
        layout_evidencias = (LinearLayout) v.findViewById(R.id.layout_evidencias_prediagnostico);
        layout_repuestos = (LinearLayout) v.findViewById(R.id.layout_repuestos_prediagnostico);
        layout_validaciones = (LinearLayout) v.findViewById(R.id.layout_validaciones);
        layout_tipificaciones = (LinearLayout) v.findViewById(R.id.layout_tipificaciones);

        //Evidencias
        img_evidencia1 = (ImageView) v.findViewById(R.id.img_evidencia1_pred);
        img_evidencia2 = (ImageView) v.findViewById(R.id.img_evidencia2_pred);
        img_evidencia3 = (ImageView) v.findViewById(R.id.img_evidencia1_qa);
        img_evidencia4 = (ImageView) v.findViewById(R.id.img_evidencia2_qa);

    }


    public void metodosOnCLick() {
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
                layout_tipificaciones.setVisibility(View.GONE);
                layout_repuestos.setVisibility(View.GONE);
                layout_observaciones.setVisibility(View.GONE);
                layout_evidencias.setVisibility(View.GONE);
                llenarDiagnostico("3");
                layout_validaciones.setVisibility(View.VISIBLE);

            }
        });
        btn_evidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleEvidencias();
                layout_prediagnostico.setVisibility(View.VISIBLE);
                layout_tipificaciones.setVisibility(View.GONE);
                layout_repuestos.setVisibility(View.GONE);
                layout_observaciones.setVisibility(View.GONE);
                layout_evidencias.setVisibility(View.VISIBLE);
                mostrarEvidencias();
                layout_validaciones.setVisibility(View.GONE);

            }
        });


        if (Global.terminalVisualizar.getTerm_status().equals("DADO DE BAJA")) {
            btn_reparacion.setVisibility(View.GONE);
            btn_qa.setVisibility(View.VISIBLE);
            btn_prediagnostico.setVisibility(View.GONE);
        }
    }


    /**
     * Valida las pestañas que serán mostradas
     *
     * @return
     */
    public void validarPestaniasMostrar() {
        if (!validarObservaciones() && Global.validaciones_consultas.get("1") != null) {

            tab_prediagnostico.setVisibility(View.VISIBLE);

            stylePrediagnostico();
            layout_prediagnostico.setVisibility(View.VISIBLE);
            layout_validaciones.setVisibility(View.VISIBLE);
            layout_tipificaciones.setVisibility(View.VISIBLE);
            layout_repuestos.setVisibility(View.VISIBLE);
            layout_observaciones.setVisibility(View.GONE);
            layout_evidencias.setVisibility(View.GONE);
            llenarDiagnostico("1");
        }
        if (Global.validaciones_consultas.get("1") == null) {
            System.out.println("Global.validaciones_consultas.get(1)" + Global.validaciones_consultas.get("1"));
            tab_prediagnostico.setVisibility(View.GONE);
        }
        if (Global.validaciones_consultas.get("2") == null) {
            tab_reparacion.setVisibility(View.GONE);
        }
        if (Global.validaciones_consultas.get("3") == null) {
            tab_qa.setVisibility(View.GONE);
        }
        if (!observacionesConfoto()) {
            tab_evidencias.setVisibility(View.GONE);
        }
    }

    /**
     * Valida que tenga observaciones
     *
     * @return true--> si tiene observaciones y las muestra al inciiar. false--> si no tiene y oculta la pestaña
     */
    public boolean validarObservaciones() {
        boolean retorno = false;
        if (Global.OBSERVACIONES != null && Global.OBSERVACIONES.size() > 0) {
            styleObservacioneso();
            tab_observaciones.setVisibility(View.VISIBLE);
            llenarRVEtapas(Global.OBSERVACIONES);
            layout_prediagnostico.setVisibility(View.GONE);
            retorno = true;
        } else {
            tab_observaciones.setVisibility(View.GONE);
            retorno = false;
        }
        return retorno;
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
                    Collections.sort((ArrayList) validacions);
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
                        Collections.sort((ArrayList) tipificacions);
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
                            Collections.sort((ArrayList) this.repuestos);
                            Collections.sort((ArrayList) repuests);

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
     * Metodo utilizado para validar si la terminal ya pasó por QA y obtener los nombres de las fotos
     */
    public void mostrarEvidencias() {
        boolean obsFotos = observacionesConfoto();
        if (obsFotos) {

            String[] fotos;
            if ((!Global.validaciones_qa) && Global.observaciones_con_fotos.size() == 2) {//Valida que solo tenga fotos de prediagnóstico
                fotos = new String[2];
                fotos[0] = Global.observaciones_con_fotos.get(0).getTeob_photo();
                fotos[1] = Global.observaciones_con_fotos.get(1).getTeob_photo();
                System.out.println("foto pre 1: " + fotos[0]);
                System.out.println("foto pre 2: " + fotos[1]);
                llenarFotos(fotos, 1);
            } else if (Global.validaciones_qa && Global.observaciones_con_fotos.size() == 2) {//Valida que solo tenga fotos de QA
                fotos = new String[2];
                fotos[0] = Global.observaciones_con_fotos.get(0).getTeob_photo();
                fotos[1] = Global.observaciones_con_fotos.get(1).getTeob_photo();
                System.out.println("foto pre 1: " + fotos[0]);
                System.out.println("foto pre 2: " + fotos[1]);
                llenarFotos(fotos, 2);
            } else if (Global.validaciones_qa && Global.observaciones_con_fotos.size() == 4) {//Valida que solo tenga fotos de prediagnóstico y QA
                fotos = new String[4];
                fotos[0] = Global.observaciones_con_fotos.get(0).getTeob_photo();
                fotos[1] = Global.observaciones_con_fotos.get(1).getTeob_photo();
                fotos[2] = Global.observaciones_con_fotos.get(2).getTeob_photo();
                fotos[3] = Global.observaciones_con_fotos.get(3).getTeob_photo();
                System.out.println("foto pre 1: " + fotos[0]);
                System.out.println("foto pre 2: " + fotos[1]);
                System.out.println("foto pre 3: " + fotos[2]);
                System.out.println("foto pre 4: " + fotos[3]);
                llenarFotos(fotos, 3);
            }
        }
    }

    /**
     * @param fotos
     * @param estado 1.-->Prediagnóstico   2. -->Qa   3. -->Prediagnóstico y QA
     * @return
     */
    public boolean llenarFotos(final String[] fotos, int estado) {
        boolean retorno = false;
        LinearLayout linearLayout;
        if (fotos.length == 2 && estado == 1) {//no tiene fotos de  qa
            linearLayout = (LinearLayout) v.findViewById((R.id.ly_evidencias_qa));
            linearLayout.setVisibility(View.GONE);
            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[0]).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia1);
            img_evidencia1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.foto = 1;
                    Global.rutaFotoObservacion = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[0];
                    cargarPanel();
                }
            });
            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[1]).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia2);
            img_evidencia2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.foto = 2;
                    Global.rutaFotoObservacion2 = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[1];
                    cargarPanel();
                }
            });
        } else if (fotos.length == 2 && estado == 2) {// not iene fotos de prediagnostico
            linearLayout = (LinearLayout) v.findViewById((R.id.ly_evidencias_pred));
            linearLayout.setVisibility(View.GONE);
            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[0]).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia3);
            img_evidencia3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.foto = 1;
                    Global.rutaFotoObservacion = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[0];
                    cargarPanel();
                }
            });
            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[1]).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia4);
            img_evidencia4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.foto = 2;
                    Global.rutaFotoObservacion2 = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[1];
                    cargarPanel();
                }
            });
        } else if (fotos.length == 4 && estado == 3) { //tiene fotos de Qa
            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[0]).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia1);
            img_evidencia1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.foto = 1;
                    Global.rutaFotoObservacion = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[0];
                    cargarPanel();
                }
            });
            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[1]).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia2);
            img_evidencia2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.foto = 2;
                    Global.rutaFotoObservacion2 = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[1];
                    cargarPanel();
                }
            });
            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[2]).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia3);
            img_evidencia3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.foto = 1;
                    Global.rutaFotoObservacion = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[2];
                    cargarPanel();
                }
            });
            Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[3]).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia4);
            img_evidencia4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.foto = 2;
                    Global.rutaFotoObservacion2 = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + fotos[3];
                    cargarPanel();
                }
            });
        }


        return retorno;
    }


    /**
     * Recorre el arreglo de observaciones y revisa cuales tienen fotos y las asigna al arreglo de observaciones con foto
     *
     * @return ture--> tiene Foto false-->No tiene fotos
     */
    public boolean observacionesConfoto() {
        boolean retorno = false;
        Global.observaciones_con_fotos = null;
        Global.observaciones_con_fotos = new ArrayList<Observacion>();
        if (Global.OBSERVACIONES != null && Global.OBSERVACIONES.size() > 0) {
            for (Observacion o : Global.OBSERVACIONES) {
                if (o.getTeob_photo() != null || !o.getTeob_photo().equalsIgnoreCase("")) {
                    if (o.getTeob_description().isEmpty()) {
                        System.out.println(o.toString());
                        Global.observaciones_con_fotos.add(o);
                    }
                }
            }
            if (Global.observaciones_con_fotos != null && Global.observaciones_con_fotos.size() > 0) {
                Collections.sort(Global.observaciones_con_fotos);
                retorno = true;
            }
        }
        return retorno;
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

        btn_evidencias.setTypeface(null, Typeface.NORMAL);
        btn_evidencias.setTextSize(13);
        ly_evidencias.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
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

        btn_evidencias.setTypeface(null, Typeface.NORMAL);
        btn_evidencias.setTextSize(13);
        ly_evidencias.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
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

        btn_evidencias.setTypeface(null, Typeface.NORMAL);
        btn_evidencias.setTextSize(13);
        ly_evidencias.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
    }

    public void styleEvidencias() {
        ly_prediagnostico.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_qa.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_reparacion.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));
        ly_observaciones.setBackgroundColor(getResources().getColor(R.color.verde_pestanas));

        btn_observaciones.setTextSize(13);
        btn_observaciones.setTypeface(null, Typeface.NORMAL);
        btn_prediagnostico.setTextSize(13);
        btn_reparacion.setTextSize(13);
        btn_qa.setTextSize(13);
        btn_prediagnostico.setTypeface(null, Typeface.NORMAL);
        btn_reparacion.setTypeface(null, Typeface.NORMAL);
        btn_qa.setTypeface(null, Typeface.NORMAL);

        btn_evidencias.setTypeface(null, Typeface.BOLD);
        btn_evidencias.setTextSize(15);
        ly_evidencias.setBackgroundColor(getResources().getColor(R.color.blanca_linea));
    }


}
