package com.example.edson0710.ezservice;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edson0710.ezservice.adapters.CardViewAdapterTarjeta;
import com.example.edson0710.ezservice.models.TarjetaUsuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TarjetasServidores extends AppCompatActivity {

    int id;
    int id_us;
    String id_uc;
    private String JSON_URL = "http://ezservice.tech/mostrar_servidores.php?cat=" + id;
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    ViewPager viewPager;
    CardViewAdapterTarjeta adapter;
    List<TarjetaUsuario> models;
    Button solicitar;
    FloatingActionButton home;
    double latitud, longitud;
    int distancia;
    float calificacion;
    DecimalFormat format1 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetas_servidores);
        id = getIntent().getExtras().getInt("id");
        id_uc = getIntent().getExtras().getString("id_uc");
        latitud = getIntent().getExtras().getDouble("latitud");
        longitud = getIntent().getExtras().getDouble("longitud");
        distancia = getIntent().getExtras().getInt("distancia");
        calificacion = getIntent().getExtras().getFloat("calificacion");
        Toast.makeText(TarjetasServidores.this, "c: " + calificacion, Toast.LENGTH_SHORT).show();


        JSON_URL = "http://ezservice.tech/mostrar_servidores.php?cat=" + id;
        solicitar = findViewById(R.id.btn_contratar);
        home = findViewById(R.id.floating_btn);

        models = new ArrayList<>();
        jsoncall();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TarjetasServidores.this, MainActivity.class);
                intent.putExtra("id", id_uc);
                startActivity(intent);
                finish();
            }
        });


        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*id_us = models.get(viewPager.getCurrentItem()).getId();
                jsoncall2();*/

            }
        });


    }

    public void jsoncall() {

        ArrayRequest = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        TarjetaUsuario tarjetaUsuario = new TarjetaUsuario();
                        tarjetaUsuario.setId(jsonObject.getInt("id"));
                        tarjetaUsuario.setNombre(jsonObject.getString("nombre"));
                        tarjetaUsuario.setEdad(jsonObject.getInt("edad"));
                        tarjetaUsuario.setImagen(jsonObject.getString("imagen"));
                        tarjetaUsuario.setCalificacion(jsonObject.getDouble("calificacion"));
                        tarjetaUsuario.setDescripcion(jsonObject.getString("descripcion"));
                        tarjetaUsuario.setLatitud(jsonObject.getDouble("latitud"));
                        tarjetaUsuario.setLongitud(jsonObject.getDouble("longitud"));

                        Location locationA = new Location("punto A");
                        locationA.setLatitude(latitud);
                        locationA.setLongitude(longitud);
                        Location locationB = new Location("punto B");
                        locationB.setLatitude(jsonObject.getDouble("latitud"));
                        locationB.setLongitude(jsonObject.getDouble("longitud"));
                        float distance = locationA.distanceTo(locationB);
                        distance = distance / 1000;
                        if (tarjetaUsuario.getCalificacion() >= calificacion) {
                            if (distance <= distancia) {
                                tarjetaUsuario.setDistancia(format1.format(distance));
                                models.add(tarjetaUsuario);
                            }
                        }
                    } catch (JSONException e) {
                        e.getCause();
                    }


                }
                if (models == null || models.size() == 0) {
                    AlertDialog.Builder myBuild = new AlertDialog.Builder(TarjetasServidores.this);
                    myBuild.setMessage("No hay usuarios disponibles por el momento ó intenta con otros filtros");
                    myBuild.setTitle("Ezservice");
                    myBuild.setCancelable(false);
                    myBuild.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog dialog = myBuild.create();
                    dialog.show();
                }

                setupadapter(models);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TarjetasServidores.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(TarjetasServidores.this);
        requestQueue.add(ArrayRequest);

    }

    void jsoncall2() {
        String url = "http://ezservice.tech/add_lista.php?id_uc=" + id_uc + "&id_us=" + id_us + "&est=" + 1;
        JsonObjectRequest peticion = new JsonObjectRequest
                (
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String valor = response.getString("Estado");
                                    switch (valor) {
                                        case "OK":
                                            Toast.makeText(TarjetasServidores.this, "Usuario ya solicitado", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "NO":
                                            Toast.makeText(TarjetasServidores.this, "Añadido con éxito", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TarjetasServidores.this, "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(TarjetasServidores.this);
        x.add(peticion);

    }


    public void setupadapter(List<TarjetaUsuario> models) {
        adapter = new CardViewAdapterTarjeta(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });
    }
}
