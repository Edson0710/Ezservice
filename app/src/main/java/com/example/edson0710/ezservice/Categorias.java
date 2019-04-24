package com.example.edson0710.ezservice;

import android.content.Intent;
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
import com.example.edson0710.ezservice.models.Categoria;

import com.example.edson0710.ezservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Categorias extends AppCompatActivity {

    private static String JSON_URL = "http://ezservice.tech/categorias.php";
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    private List<Categoria> lista;
    private RecyclerView recycler;
    String id_uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_uc = getIntent().getExtras().getString("id_uc");
        setContentView(R.layout.activity_categorias);
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
                        Categoria categoria = new Categoria();
                        categoria.setId_categoria(jsonObject.getInt("id"));
                        categoria.setNombre(jsonObject.getString("nombre"));
                        categoria.setImagen_url(jsonObject.getString("imagen"));
                        lista.add(categoria);

                    } catch (JSONException e) {
                        e.getCause();
                    }
                }
                setuprecyclerview(lista);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Categorias.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(Categorias.this);
        requestQueue.add(ArrayRequest);

    }

    public void setuprecyclerview(List<Categoria> lista) {
        recycler = (RecyclerView) findViewById(R.id.recyclerview_categorias);
        recycler.addItemDecoration(new DivideRecycler(getResources()));
        RecyclerViewAdapterCategoria myadapter = new RecyclerViewAdapterCategoria(this, lista, id_uc);
        LinearLayoutManager mLayouyManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recycler.setLayoutManager(mLayouyManager);
        recycler.setAdapter(myadapter);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Categorias.this, MainActivity.class);
        startActivity(i);
    }
}