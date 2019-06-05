package com.example.edson0710.ezservice;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.edson0710.ezservice.adapters.DocumentosAdapter;
import com.example.edson0710.ezservice.adapters.RecyclerViewAdapterProfesion;
import com.example.edson0710.ezservice.models.Menu;
import com.example.edson0710.ezservice.models.Profesion;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Documentos extends AppCompatActivity {

    View view;
    TextView titulo;
    RecyclerView recyclerView;
    ImageView imagen;
    private List<Menu> lista;
    private String JSON_URL = "http://ezservice.tech/documentos.php?id_us=";
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_documentos);
        titulo = findViewById(R.id.titulo_documento);
        imagen = findViewById(R.id.imagen_documento);
        recyclerView = findViewById(R.id.recycler_documentos);
        id = getIntent().getExtras().getInt("id");
        lista = new ArrayList<Menu>();

        jsoncall();




    }

    public void jsoncall() {
        JSON_URL = "http://ezservice.tech/documentos.php?id_us="+id;

        ArrayRequest = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        Menu menu = new Menu();
                        menu.setTitulo(jsonObject.getString("url"));
                        //menu.setTitulo(jsonObject.getString("nombre"));
                        //profesion.setImagen_url2(jsonObject.getString("imagen"));

                        lista.add(menu);


                    } catch (JSONException e) {
                        e.getCause();
                    }
                }

                if (lista == null || lista.size() == 0) {
                    AlertDialog.Builder myBuild = new AlertDialog.Builder(Documentos.this);
                    myBuild.setMessage("Este usuario no tiene aún documentos");
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

                setuprecyclerview2(lista);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Documentos.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(Documentos.this);
        requestQueue.add(ArrayRequest);

    }

    public void setuprecyclerview2(List<Menu> lista) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_documentos);
        recyclerView.addItemDecoration(new DivideRecycler(getResources()));
        DocumentosAdapter myadapter2 = new DocumentosAdapter(lista, new DocumentosAdapter.OnClickRecycler() {
            @Override
            public void OnClickItemRecycler(Menu menu) {
                Glide.with(Documentos.this).load(menu.getTitulo()).into(imagen);
            }
        });
        LinearLayoutManager mLayouyManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayouyManager2);
        recyclerView.setAdapter(myadapter2);
    }

}
