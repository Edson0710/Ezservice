package com.example.edson0710.ezservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class EditarPerfil extends AppCompatActivity {
    String id, nombre, apellido, correo;
    String telefono;
    Button aceptar;
    EditText et_nombre, et_apellido, et_correo, et_telefono;
    int type_obtener;
    String url1, url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        id = getIntent().getExtras().getString("id");
        et_nombre = (EditText) findViewById(R.id.perfil_nombre);
        et_apellido = (EditText) findViewById(R.id.perfil_apellido);
        et_correo = (EditText) findViewById(R.id.perfil_correo);
        et_telefono = (EditText) findViewById(R.id.perfil_telefono);

        type_obtener = obtenerTipo();

        if (type_obtener == 1) {
            url1 = "http://ezservice.tech/editarperfil.php?cat=" + id;

        }
        if (type_obtener == 2) {
            url1 = "http://ezservice.tech/editarperfil_server.php?cat=" + id;
        }


        jsoncall();


        aceptar = (Button) findViewById(R.id.perfil_aceptar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type_obtener == 1) {
                    url2 = "http://ezservice.tech/updateperfil.php?cat=" + id + "&nom=" + et_nombre.getText().toString()
                            + "&ape=" + et_apellido.getText().toString()
                            + "&cor=" + et_correo.getText().toString() + "&tel=" + et_telefono.getText().toString();

                }
                if (type_obtener == 2) {
                    url2 = "http://ezservice.tech/updateperfil_server.php?cat=" + id + "&nom=" + et_nombre.getText().toString()
                            + "&ape=" + et_apellido.getText().toString()
                            + "&cor=" + et_correo.getText().toString() + "&tel=" + et_telefono.getText().toString();
                }

                Handler handler = new Handler();

                jsoncall2();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(EditarPerfil.this, ""+nombre2, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 2000);


            }
        });

    }

    public void jsoncall() {
        JsonObjectRequest peticion = new JsonObjectRequest
                (
                        Request.Method.GET,
                        url1,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    nombre = response.getString("nombre");
                                    apellido = response.getString("apellido");
                                    correo = response.getString("correo");
                                    telefono = response.getString("telefono");

                                    et_nombre.setText(nombre);
                                    et_apellido.setText(apellido);
                                    et_correo.setText(correo);
                                    et_telefono.setText(telefono);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditarPerfil.this, "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(EditarPerfil.this);
        x.add(peticion);
    }

    public void jsoncall2() {

        JsonObjectRequest peticion = new JsonObjectRequest
                (
                        Request.Method.GET,
                        url2,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String valor = response.getString("Estado");
                                    switch (valor) {
                                        case "OK":
                                            //Toast.makeText(EditarPerfil.this, "Fallo al actualizar", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "NO":

                                            //Toast.makeText(EditarPerfil.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditarPerfil.this, "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(EditarPerfil.this);
        x.add(peticion);
    }

    public int obtenerTipo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditarPerfil.this);
        int type_preference = preferences.getInt("TIPO", 1);
        return type_preference;
    }
}
