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
import com.example.edson0710.ezservice.adapters.RecyclerViewAdapterComentarios;
import com.example.edson0710.ezservice.adapters.RecyclerViewAdapterProfesion;
import com.example.edson0710.ezservice.models.Comentario;
import com.example.edson0710.ezservice.models.Profesion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Comentarios extends AppCompatActivity {

    int id;
    private String JSON_URL = "http://ezservice.tech/mostrar_comentarios.php?cat=" + id;
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    private List<Comentario> lista2;
    private RecyclerView recycler2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        //Recieve data
        id = getIntent().getExtras().getInt("id");
        JSON_URL = "http://ezservice.tech/mostrar_comentarios.php?cat=" + id;
        //ini views
        //TextView tv_prueba = findViewById(R.id.textoprueba);

        //setting values
        //tv_prueba.setText(""+id);
        lista2 = new ArrayList<Comentario>();

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
                        Comentario comentario = new Comentario();
                        comentario.setComentario(jsonObject.getString("comentario_servidor"));
                        comentario.setFecha(jsonObject.getString("fecha"));

                        lista2.add(comentario);


                    } catch (JSONException e) {
                        e.getCause();
                    }


                }

                setuprecyclerview2(lista2);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comentarios.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(Comentarios.this);
        requestQueue.add(ArrayRequest);

    }


    public void setuprecyclerview2(List<Comentario> lista2) {
        recycler2 = (RecyclerView) findViewById(R.id.recyclerview_comentarios);
        recycler2.addItemDecoration(new DivideRecycler(getResources()));
        RecyclerViewAdapterComentarios myadapter2 = new RecyclerViewAdapterComentarios(this, lista2);
        LinearLayoutManager mLayouyManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler2.setLayoutManager(mLayouyManager2);
        recycler2.setAdapter(myadapter2);


    }
}
