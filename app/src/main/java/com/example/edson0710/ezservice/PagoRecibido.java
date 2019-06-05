package com.example.edson0710.ezservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PagoRecibido extends AppCompatActivity {

    int id_us;
    RadioGroup radio;
    RadioButton radio1, radio2;
    int pendiente;
    Button aceptar;
    String url2, id_uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_recibido);

        id_uc = getIntent().getExtras().getString("id_uc");
        id_us = getIntent().getExtras().getInt("id_us");

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        aceptar = findViewById(R.id.btn_pago);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio1.isChecked()){
                    jsoncall();
                    finish();
                }
                if (radio2.isChecked()){
                    finish();
                }
            }
        });

    }

    public void jsoncall() {

        url2 = "http://ezservice.tech/update_pago.php?id_uc=" + id_us + "&id_us=" + id_uc;

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
                                                                                      break;
                                        case "NO":

                                            //Toast.makeText(PagoRecibido.this, "No se permiten groserias", Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(PagoRecibido.this, "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(PagoRecibido.this);
        x.add(peticion);

    }
}
