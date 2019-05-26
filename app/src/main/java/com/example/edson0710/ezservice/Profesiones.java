package com.example.edson0710.ezservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.edson0710.ezservice.adapters.RecyclerViewAdapterProfesion;
import com.example.edson0710.ezservice.models.Profesion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Profesiones extends AppCompatActivity {
    int id;
    String id_uc;
    private String JSON_URL = "http://ezservice.tech/profesiones.php?cat=" + id;
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    private List<Profesion> lista2;
    private RecyclerView recycler2;
    double latitud, longitud;
    int distancia;
    float calificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesiones);

        //Recieve data
        id = getIntent().getExtras().getInt("id");
        id_uc = getIntent().getExtras().getString("id_uc");
        latitud = getIntent().getExtras().getDouble("latitud");
        longitud = getIntent().getExtras().getDouble("longitud");
        distancia = getIntent().getExtras().getInt("distancia");
        calificacion = getIntent().getExtras().getFloat("calificacion");
        Toast.makeText(Profesiones.this, "c: " + calificacion, Toast.LENGTH_SHORT).show();

        JSON_URL = "http://ezservice.tech/profesiones.php?cat=" + id;
        //ini views
        //TextView tv_prueba = findViewById(R.id.textoprueba);

        //setting values
        //tv_prueba.setText(""+id);
        lista2 = new ArrayList<Profesion>();

        jsoncall();
    }

    public void jsoncall() {

        ArrayRequest = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        Profesion profesion = new Profesion();
                        profesion.setId_profesion2(jsonObject.getInt("id"));
                        profesion.setNombre2(jsonObject.getString("nombre"));
                        //profesion.setImagen_url2(jsonObject.getString("imagen"));

                        lista2.add(profesion);


                    } catch (JSONException e) {
                        e.getCause();
                    }
                }

                setuprecyclerview2(lista2);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profesiones.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(Profesiones.this);
        requestQueue.add(ArrayRequest);

    }

    public void setuprecyclerview2(List<Profesion> lista2) {
        recycler2 = (RecyclerView) findViewById(R.id.recyclerview_profesiones);
        recycler2.addItemDecoration(new DivideRecycler(getResources()));
        RecyclerViewAdapterProfesion myadapter2 = new RecyclerViewAdapterProfesion(this, lista2, id_uc, latitud, longitud, distancia, calificacion);
        LinearLayoutManager mLayouyManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler2.setLayoutManager(mLayouyManager2);
        recycler2.setAdapter(myadapter2);
    }
}
