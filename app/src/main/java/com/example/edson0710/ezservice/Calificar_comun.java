package com.example.edson0710.ezservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calificar_comun extends AppCompatActivity {

    RatingBar ratingBar;
    Button finalizar;
    EditText comentario;
    String url2, id_uc;
    int id_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_comun);

        id_uc = getIntent().getExtras().getString("id_uc");
        id_us = getIntent().getExtras().getInt("id_us");

        ratingBar = findViewById(R.id.raitingbar);
        finalizar = findViewById(R.id.btn_calificar);
        comentario = findViewById(R.id.editText2);

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                float calificacion = ratingBar.getRating();
                String comentarios = comentario.getText().toString();
                Toast.makeText(Calificar_comun.this, "Calificacion: " + calificacion, Toast.LENGTH_SHORT).show();
                Toast.makeText(Calificar_comun.this, "id_uc: " + id_uc, Toast.LENGTH_SHORT).show();
                Toast.makeText(Calificar_comun.this, "id_us: " + id_us, Toast.LENGTH_SHORT).show();
                jsoncall(comentarios, calificacion, date);
                Intent intent = new Intent(Calificar_comun.this, MainActivity.class);
                intent.putExtra("id", id_uc);
                startActivity(intent);
            }
        });

    }

    public void jsoncall(String comentario,  float calificacion, String date) {
        url2 = "http://ezservice.tech/add_historial_servidor.php?id_uc=" + id_us + "&id_us=" + id_uc
                + "&cali=" + calificacion + "&coment=" + comentario;

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
                        Toast.makeText(Calificar_comun.this, "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(Calificar_comun.this);
        x.add(peticion);

    }
}
