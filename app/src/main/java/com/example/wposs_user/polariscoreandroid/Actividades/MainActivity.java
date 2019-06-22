package com.example.wposs_user.polariscoreandroid.Actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogCancelarHuella;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogNotificacion;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ConsultaTerminalesSerial;
import com.example.wposs_user.polariscoreandroid.Fragmentos.InicialFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.PerfilFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ProductividadFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.StockFragment;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Etapas;
import com.example.wposs_user.polariscoreandroid.java.Notificacion;
import com.example.wposs_user.polariscoreandroid.java.NotificacionTecnico;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static com.example.wposs_user.polariscoreandroid.R.drawable.ic_notification;
import static com.example.wposs_user.polariscoreandroid.R.drawable.ic_notnotificacion;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValues;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private Vector<Repuesto> repuestos;
    private AutoCompleteTextView autocomplete_tipificaciones;
    private Vector<Etapas> etapas;
    private RecyclerView rv;


    private TextView serial;

    private Spinner spinner_estado_terminal;
    private EditText f_inicio;
    private EditText f_fin;
    FragmentManager fragmentManager;
    public static MainActivity objeto;
    private LinearLayout layout_terminal_etapas;
    private TextView usuario_drawer;
    private TextView codigo_drawer;
    private ImageView imageView_perfil;
    private DialogNotificacion dialogo;
    private ImageView verNotificaciones;
    private ArrayList<Notificacion> notificaciones;
    private NotificacionTecnico ntecnico;
    private String mensaje;


    private int contadorFragmentos;

    private RequestQueue queue;
    private int btn_alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        notificaciones = new ArrayList<>();
        objeto = this;

        Global.REPUESTOS = new ArrayList<>();
        Global.TIPIFICACIONES_DIAGNOSTICO = new ArrayList<>();
        Global.VALIDACIONES_DIAGNOSTICO = new ArrayList<>();
        Global.REPUESTOS_DIAGONOSTICO = new ArrayList<>();


        queue = Volley.newRequestQueue(MainActivity.this);


        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setTitle(null);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        usuario_drawer = (TextView) hView.findViewById(R.id.usuario_drawer);
        codigo_drawer = (TextView) hView.findViewById(R.id.codigo_drawer);
        imageView_perfil = (ImageView) hView.findViewById(R.id.imageView_perfil);
        verNotificaciones = (ImageView) findViewById(R.id.btn_notificaciones);

        Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/view/" + Global.ID + ".jpg").error(R.mipmap.ic_profile).fit().centerInside().into(imageView_perfil);


        usuario_drawer.setText(Global.NOMBRE);
        codigo_drawer.setText(Global.CODE);

        imageView_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).addToBackStack(null).commit();

            }
        });

        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();


        // Para las notificaciones
        verNotificaciones = (ImageView) findViewById(R.id.btn_notificaciones);

        ntecnico = new NotificacionTecnico();
        new Thread(ntecnico).start();//


        consumirServicioNotificaciones();


        verNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Global.notificaciones.isEmpty()) {
                    verNotificaciones.setImageResource(R.mipmap.ic_campanano);

                }

                if (Global.notificaciones.size() > 0) {

                    verNotificaciones.setImageResource(R.mipmap.ic_campanasi);
                    dialogo = new DialogNotificacion();
                    dialogo.show(MainActivity.this.getSupportFragmentManager(), "");


                }

            }
        });


    }

    public void setTitulo(String title) {
        TextView titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText(title);
    }

    @Override
    public void onBackPressed() {
        contadorFragmentos = getSupportFragmentManager().getBackStackEntryCount();

        if (contadorFragmentos == 1) {
            // super.onBackPressed();
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("¿Desea cerrar sesión?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    consumirSercivioCerrarSesion();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
            dialogo1.show();
        } else {
            getSupportFragmentManager().popBackStack();
        }


    }



  /* @Override
    protected void onStop() {
        super.onStop();
       // consumirSercivioCerrarSesion();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.btn_alert:
                fragmentManager.beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();//Buscar
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // AL SELECCIONAR ALGUUNA OPCION DEL MENU
        // FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_perfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).addToBackStack(null).commit();
            // cargarDatosPerfil();
        } else if (id == R.id.nav_stock) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new StockFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_consultar_terminales_reparadas) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesSerial()).addToBackStack(null).commit();

        } else if (id == R.id.nav_productividad) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ProductividadFragment()).addToBackStack(null).commit();

        } else if (id == R.id.nav_cerrar_sesion) {

            consumirSercivioCerrarSesion();
        } else if (id == R.id.nav_autenticacion_huella) {
            DialogCancelarHuella dialog = new DialogCancelarHuella();
            dialog.show(MainActivity.this.getSupportFragmentManager(), "");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    /*************************************************************************************
     *METODO QUE CONSUME EL SERVICIO PARA CERRAR SESIÓN
     *
     ***************************************************** **/

    public void consumirSercivioCerrarSesion() {
        String url = "http://100.25.214.91:3000/PolarisCore/Users/close";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.ID);
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

                            if (response.get("status").toString().equalsIgnoreCase("fail")) {
                                try {
                                    Global.mensaje = response.get("message").toString();
                                    Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            eliminarValues(objeto);
                            ntecnico.stop();
                            Global.notificaciones = new ArrayList<>();
                            Intent i = new Intent(objeto, Activity_login.class);
                            startActivity(i);
                            finish();

                            return;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error Respuesta en JSON: " + error.getMessage());
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()) {
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authenticator", Global.TOKEN);//QUITAR
                return params;
            }
        };

        queue.add(jsArrayRequest);
    }


    /**
     * Metodo que crea un Alert dialogo y se cierrar atomaticamente
     *
     * @param c contexto
     * @param titulo -->Titulo del cuadro de dialogo
     * @param msg -->Información del cuadro de dialogo
     * @param tiempo -->Tiempo en el cual permanece activo el cuadro de dialogo
     * @param cancelable -->True: si es cancelable, False: si no
     */
    static AlertDialog dialog;

    public void CustomAlertDialog(Context c, String titulo, String msg, int tiempo, boolean cancelable) {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.AppTheme));
        builder.setTitle(titulo);
        builder.setMessage(msg);
        builder.setCancelable(cancelable);
        dialog = builder.create();
        if (((Activity) c).isFinishing()) {
            return;
        }
        //dialog.show();
        try {
            dialog.show();
        } catch (Exception e) {
            Log.i("Error:", "Mensaje cerrado por la fuerza");
        }
        int titleDividerId = c.getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));
        }
        if (!cancelable)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, tiempo);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EditText getF_inicio() {
        return f_inicio;
    }

    public void setF_inicio(EditText f_inicio) {
        this.f_inicio = f_inicio;
    }

    public EditText getF_fin() {
        return f_fin;
    }

    public void setF_fin(EditText f_fin) {
        this.f_fin = f_fin;
    }

    public int getContadorFragmentos() {
        return contadorFragmentos;
    }

    public void setContadorFragmentos(int contadorFragmentos) {
        this.contadorFragmentos = contadorFragmentos;
    }


    /***************METODO PARA CONSUMIR EL SERVICIO DE NOTIFICACIONES***************************/

    private static Notificacion n;

    public void consumirServicioNotificaciones() {

        Global.notificaciones = new ArrayList<>();

        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Notifications/noti ";
        JSONObject jsonObject = new JSONObject();


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Global.STATUS_SERVICE = response.get("status").toString();

                            if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                                Global.mensaje = response.get("message").toString();
                                if (Global.mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "ERROR: " + Global.mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            }


                            response = new JSONObject(response.toString());

                            if (response.get("notificacion").equals("{}")) {

                                verNotificaciones.setImageResource(R.mipmap.ic_campanano);

                                return;
                            } else {

                                JSONArray jsonArray = response.getJSONArray("notificacion");


                                String not = null;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    not = ((JSONArray) jsonArray).getString(i);

                                    n = gson.fromJson(not, Notificacion.class);


                                    if (n != null && !n.getNoti_msg().contains("albarán")) {

                                        String[] ms = n.getNoti_msg().split(":");
                                        String tit = ms[0];
                                        String men = ms[1];
                                        String[] vacio = men.split(",");
                                        String h = eliminarCaracteres(men);

                                        if (h.contains("c") || h.contains("m")) {

                                            if (!contieNotificacion(n.getNoti_id())) {

                                                String[] fecha = n.getNoti_date_create().split(",");
                                                String[] diames = fecha[0].split(" ");
                                                String mes = Utils.obtenerMes(diames[0]);

                                                int dia = Integer.parseInt(diames[1]);

                                                String[] anioHora = fecha[1].split(" ");
                                                String anio = anioHora[0];

                                                String fecha_not = Utils.formatoDia(dia) + "-" + Utils.obtenerNumMes2(mes) + "-" + anioHora[1] + "   " + anioHora[2];
                                                n.setNoti_date_create(fecha_not);

                                                String msj = n.getNoti_msg();


                                                if (msj.contains("terminal") && !msj.contains("object")) {


                                                    if(!msj.contains("[")){

                                                        String mx= msj.substring(0,33);
                                                        String con= msj.substring(33,msj.length());

                                                        msj= mx+"["+con+"]";

                                                        System.out.println("MENSAJE ARREGLADO*** " +msj);

                                                    }

                                                    String nMensaje = eliminarCaracteres(msj);

                                                    System.out.println("NOTIFICACIONES RECIBIDAS** "+ nMensaje);
                                                    String[] mesagge = nMensaje.split(":");
                                                    String titulo = mesagge[0];

                                                    String mesag;


                                                      ArrayList<String>terminales= listarNotterminales(nMensaje)
;

                                                    String text = formatoNotificaciones(terminales.toString());

                                                    String msjFin = formatoNot(text);


                                                    n.setNoti_titulo(titulo);

                                                    n.setNoti_msg(msjFin);


                                                }

                                                if (msj.contains("repuesto") && !msj.contains("object")) {


                                                    String nMensaje = eliminarCaracteres(msj);
                                                    String[] mesagge = nMensaje.split("   ");
                                                    String titulo = mesagge[0];

                                                     String msjFin = eliminarCaracteres(mesagge[1]);


                                                    String msjFinal = formatoRep(msjFin);

                                                    n.setNoti_titulo(titulo);


                                                    n.setNoti_msg(msjFinal);


                                                }


                                                Global.notificaciones.add(n);


                                            }


                                        }
                                    }
                                }
                            }

                            if (Global.notificaciones.size() == 0) {
                                verNotificaciones.setImageResource(R.mipmap.ic_campanano);

                            }

                            if (Global.notificaciones.size() > 0) {

                                verNotificaciones.setImageResource(R.mipmap.ic_campanasi);
                                dialogo = new DialogNotificacion();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("id", Global.CODE);

                return params;
            }
        };

        queue.add(jsArrayRequest);

    }


    /***************SERVICIO PARA ELIMINAR UNA NOTIFICACION***************************/


    public void eliminarNotificacion(final String id) {

        Notificacion noti = null;

        final Gson gson = new GsonBuilder().create();

        String url = "http://100.25.214.91:3000/PolarisCore/Notifications";
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response = new JSONObject(response.toString());

                            if (!response.get("message").equals("success")) {

                                Toast.makeText(objeto, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
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
                params.put("id", id);

                return params;
            }
        };

        queue.add(jsArrayRequest);

    }

    public boolean contieNotificacion(String id) {

        for (Notificacion not : Global.notificaciones) {

            if (not.getNoti_id().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public String eliminarCaracteres(String msj) {

        char[] caracteres = msj.toCharArray();

        for (int i = 0; i < caracteres.length; i++) {

            if (caracteres[i] == '[') {

                caracteres[i] = ' ';
            }

            if (caracteres[i] == ']') {

                caracteres[i] = ' ';
            }

            if (caracteres[i] == '{') {

                caracteres[i] = ' ';
            }
            if (caracteres[i] == '}') {

                caracteres[i] = ' ';
            }

            if (caracteres[i] == '"') {

                caracteres[i] = ' ';
            }


        }

        String x = "";


        for (Character c : caracteres) {
            x += c;

        }

        return x;


    }


    public ArrayList<String> listarNotterminales(String ms) {
        ArrayList<String> ter = new ArrayList<>();
        String[] body = ms.split("  ");

        if (body.length == 2) {
            ter.add(ms);

        }


        if (body.length > 2) {
            String tit = body[0] + "\n";

            for (int i = 2; i < body.length; i = i + 2) {

                String x = "\n" + body[i] + "\n";
                ter.add(x);

            }


        }

        System.out.println("VALOR RETORNADO EN EL METODO**"+ ter.toString());

        return ter;



    }

    public String formatoNotificaciones(String mens) {

        char[] texto = mens.toCharArray();

        for (int i = 0; i < texto.length; i++) {

            if (texto[i] == '[') {

                texto[i] = ' ';
            }
            if (texto[i] == ']') {

                texto[i] = ' ';
            }
            if (texto[i] == ',') {

                texto[i] = ' ';
            }
        }


        String x = "";


        for (Character c : texto) {
            x += c;

        }

        return x;

    }


    public String formatoNot(String txt) {

       String rta = "";

        String[] tlista = txt.split("  ");


        for (int i = 0; i < tlista.length; i = i + 3) {

            String[] marca = tlista[i].split(":");
            String[] modelo = tlista[i + 1].split(":");
            String[] serial = tlista[i + 2].split(":");

            if (marca[0].trim().equals("marca")) {
                marca[0] = "Marca";
            }

            if (modelo[0].trim().equals("modelo")) {
                modelo[0] = "Modelo";
            }

            if (serial[0].trim().equals("serial")) {
                serial[0] = "Serial";
            }

            tlista[i] = marca[0] + ":" + marca[1];
            tlista[i + 1] = modelo[0] + ":" + modelo[1];
            tlista[i + 2] = serial[0] + ":" + serial[1];

            rta = rta + tlista[i] + "\n" + tlista[i + 1] + "\n" + tlista[i+2] + "\n";
        }

        return rta;

    }


    public String formatoRep(String txt) {



        String rta = "";

        String[] tlista = txt.split(",");


        for (int i = 0; i < tlista.length; i = i + 3) {

            String[] codigo = tlista[i].split(":");
            String[] nombre = tlista[i + 1].split(":");
            String[] cantidad = tlista[i + 2].split(":");

            if (codigo[0].trim().equals("codigo")) {
                codigo[0] = "Código";
            }

            if (nombre[0].trim().equals("nombre")) {
                nombre[0] = "Nombre";
            }

            if (cantidad[0].trim().equals("cantidad")) {
                cantidad[0] = "Cantidad";
            }


            tlista[i] = codigo[0] + ":" + codigo[1];
            tlista[i + 1] = nombre[0] + ":" + nombre[1];
            tlista[i + 2] = cantidad[0] + ":" + cantidad[1];

            rta =rta+ tlista[i] + "\n" + tlista[i + 1] + "\n" + tlista[i+2] + "\n" + "\n";


        }

        return rta;

    }


}