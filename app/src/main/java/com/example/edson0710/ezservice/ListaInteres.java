package com.example.edson0710.ezservice;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edson0710.ezservice.adapters.RecyclerViewAdapterLista;
import com.example.edson0710.ezservice.adapters.RecyclerViewAdapterProfesion;
import com.example.edson0710.ezservice.models.Lista;
import com.example.edson0710.ezservice.models.Profesion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaInteres extends Fragment {
    String id_uc;
    private String JSON_URL = "http://ezservice.tech/lista_interes.php?cat=" + id_uc;
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    RecyclerView recycler;
    ArrayList<Lista> listaInter;
    RecyclerViewAdapterLista myadapter = new RecyclerViewAdapterLista(getContext(), listaInter);
    int id_us;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_lista_interes, container, false);

        id_uc = getArguments().getString("id");
        myadapter.notifyDataSetChanged();
        listaInter = new ArrayList<>();
        recycler = (RecyclerView) rootView.findViewById(R.id.recyclerview_lista);

        //Recieve data
        //id = getIntent().getExtras().getInt("id");
        JSON_URL = "http://ezservice.tech/lista_interes.php?cat=" + id_uc;
        //ini views
        //TextView tv_prueba = findViewById(R.id.textoprueba);

        //setting values
        //tv_prueba.setText(""+id);

        jsoncall();

        return rootView;
    }

    public void jsoncall() {

        ArrayRequest = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        Lista lista = new Lista();
                        lista.setId(jsonObject.getInt("id"));
                        lista.setNombre(jsonObject.getString("nombre"));
                        lista.setImagen(jsonObject.getString("imagen"));
                        lista.setProfesion(jsonObject.getString("profesion"));
                        lista.setId_us(jsonObject.getInt("id_us"));
                        lista.setEstado(jsonObject.getString("estado"));

                        listaInter.add(lista);


                    } catch (JSONException e) {
                        e.getCause();
                    }


                }

                setuprecyclerview2(listaInter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(ArrayRequest);

    }


    public void setuprecyclerview2(List<Lista> listaInter) {
        recycler.addItemDecoration(new DivideRecycler(getResources()));
        LinearLayoutManager mLayouyManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerViewAdapterLista myadapter = new RecyclerViewAdapterLista(getContext(), listaInter);
        recycler.setLayoutManager(mLayouyManager2);
        recycler.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 121:
                id_us = listaInter.get(item.getGroupId()).getId_us();
                jsoncall2();
                Toast.makeText(getContext(), "Usuario eliminado...", Toast.LENGTH_SHORT).show();
                listaInter.remove(item.getGroupId());
                recycler.removeViewAt(item.getGroupId());
                myadapter.notifyItemRemoved(item.getGroupId());
                myadapter.notifyItemRangeChanged(item.getGroupId(), listaInter.size());
                myadapter.notifyDataSetChanged();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void jsoncall2() {
        String url = "http://ezservice.tech/delete_list.php?id_uc=" + id_uc + "&id_us=" + id_us;

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
                                            Toast.makeText(getContext(), "Usuario ya solicitado", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "NO":
                                            Toast.makeText(getContext(), "Añadido con éxito", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue x = Volley.newRequestQueue(getContext());
        x.add(peticion);
    }


}