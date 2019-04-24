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
import com.example.edson0710.ezservice.adapters.RecyclerViewAdapterCategoria;
import com.example.edson0710.ezservice.adapters.RecyclerViewAdapterHistorial;
import com.example.edson0710.ezservice.models.Categoria;
import com.example.edson0710.ezservice.models.HistorialPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Historial extends AppCompatActivity {
    String id_uc;
    private String JSON_URL = "http://ezservice.tech/mostrar_historial.php?cat=" + id_uc;
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    private List<HistorialPojo> lista;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_uc = getIntent().getExtras().getString("id");
        JSON_URL = "http://ezservice.tech/mostrar_historial.php?cat=" + id_uc;
        setContentView(R.layout.activity_historial);
        lista = new ArrayList<>();
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
                        HistorialPojo historial = new HistorialPojo();
                        historial.setId(jsonObject.getInt("id"));
                        historial.setNombre(jsonObject.getString("nombre"));
                        historial.setProfesion(jsonObject.getString("profesion"));
                        historial.setCosto(jsonObject.getString("costo"));
                        historial.setFecha(jsonObject.getString("fecha"));
                        historial.setImagen(jsonObject.getString("imagen"));
                        lista.add(historial);

                    } catch (JSONException e) {
                        e.getCause();
                    }
                }
                setuprecyclerview(lista);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Historial.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(Historial.this);
        requestQueue.add(ArrayRequest);

    }

    public void setuprecyclerview(List<HistorialPojo> lista) {
        recycler = (RecyclerView) findViewById(R.id.recyclerview_historial);
        recycler.addItemDecoration(new DivideRecycler(getResources()));
        RecyclerViewAdapterHistorial myadapter = new RecyclerViewAdapterHistorial(this, lista);
        LinearLayoutManager mLayouyManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recycler.setLayoutManager(mLayouyManager);
        recycler.setAdapter(myadapter);
    }
}
