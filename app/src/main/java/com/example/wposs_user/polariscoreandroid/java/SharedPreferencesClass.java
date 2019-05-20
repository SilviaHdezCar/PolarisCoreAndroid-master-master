package com.example.wposs_user.polariscoreandroid.java;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wposs_user.polariscoreandroid.Actividades.Activity_login;
import com.example.wposs_user.polariscoreandroid.Comun.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesClass {

    protected final static String PREFS_KEY = "Mis preferencias";
    protected final static String PREFS_KEY2 = "android.polaris.core.access";

    static android.content.SharedPreferences settings;
    static android.content.SharedPreferences.Editor editor;


    /**
     * Metodo utilizado para guardar la sesión
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveValueStrPreferenceLogueoHuella(Context context, String key, String value) {

        try {

            settings = context.getSharedPreferences(PREFS_KEY2, MODE_PRIVATE);

            editor = settings.edit();
            editor.putString(key, value);


            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean eliminarValuesLogueoHuella(Context context) {
        //saveValueStrPreference2();
        try {
            settings = context.getSharedPreferences(PREFS_KEY2, Context.MODE_PRIVATE);
            settings.edit().clear().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String getValueStrPreferenceLogueoHuella(Context context, String key) {
        try {
            preferences = context.getSharedPreferences(PREFS_KEY2, MODE_PRIVATE);
            return preferences.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Metodo utilizado para guardar la sesión
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean saveValueStrPreference(Context context, String key, String value) {

        try {

            settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);

            editor = settings.edit();
            editor.putString(key, value);


            editor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarValues(Context context) {
        //saveValueStrPreference2();
        try {
            settings = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
            settings.edit().clear().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static android.content.SharedPreferences preferences;


    public static String getValueStrPreference(Context context, String key) {
        try {
            preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
            return preferences.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getValueIntPreference(Context context, String key) {
        preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    public static void saveValueBooleanPreference(Context context, String key, Boolean value) {
        settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getValueBooleanPreference(Context context, String key) {
        preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);

        return preferences.getBoolean(key, false);
    }


}
