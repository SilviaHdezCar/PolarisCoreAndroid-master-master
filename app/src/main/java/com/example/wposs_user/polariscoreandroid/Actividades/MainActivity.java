package com.example.wposs_user.polariscoreandroid.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogCancelarHuella;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ConsultaTerminalesSerial;
import com.example.wposs_user.polariscoreandroid.Fragmentos.InicialFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.PerfilFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ProductividadFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.StockFragment;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Etapas;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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

    private int contadorFragmentos;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        objeto = this;
        Global.REPUESTOS = new ArrayList<>();
        Global.TIPIFICACIONES_DIAGNOSTICO = new ArrayList<>();
        Global.VALIDACIONES_DIAGNOSTICO = new ArrayList<>();
        Global.REPUESTOS_DIAGONOSTICO = new ArrayList<>();

        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setTitle(null);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(objeto);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.btn_alert:
                fragmentManager.beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();//Buscar
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


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
                            if (!error.getMessage().isEmpty()){
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
}