package com.example.edson0710.ezservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class login1 extends AppCompatActivity {
    TextView tv_registrar;
    Button ingresar;
    EditText correo, password;
    RadioGroup radio;
    RadioButton radio1, radio2, radio_sesion;
    int type_obtener;

    private boolean isActivate;
    private static final String STRING_PREFERENCES = "preferencias";
    private static final String PREFERENCE_ESTADO_BUTTON = "estado.button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);


        ingresar = findViewById(R.id.login1_btn_iniciar);
        correo = findViewById(R.id.login1_et_correo);
        password = findViewById(R.id.login1_et_password);
        radio = findViewById(R.id.rgroup);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio_sesion = findViewById(R.id.radio_sesion);

        isActivate = radio_sesion.isChecked(); //DESACTIVADO

        radio_sesion.setOnClickListener(new View.OnClickListener() {
            //ACTIVADO
            @Override
            public void onClick(View v) {
                if (isActivate) {
                    radio_sesion.setChecked(false);
                }
                isActivate = radio_sesion.isChecked();

            }
        });


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEstado();
                if (radio1.isChecked()) {
                    //tv_registrar.setText("Usuario comun");
                    String url = "http://ezservice.tech/loginuser.php?usu=" + correo.getText().toString() + "&cont=" + password.getText().toString();
                    final Intent iniciarAdmin = new Intent(login1.this, MainActivity.class);
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
                                                        String id = response.getString("id");
                                                        guardarId(id);
                                                        type_obtener = 1;
                                                        guardarTipo(type_obtener);
                                                        iniciarAdmin.putExtra("id", id);
                                                        startActivity(iniciarAdmin);
                                                        finish();
                                                        break;
                                                    case "NO":
                                                        Toast.makeText(login1.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    , new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(login1.this, "Error php", Toast.LENGTH_SHORT).show();
                                }
                            });
                    RequestQueue x = Volley.newRequestQueue(login1.this);
                    x.add(peticion);
                }

                if (radio2.isChecked()) {
                    //tv_registrar.setText("Usuario Servidor");
                    String url = "http://ezservice.tech/loginserver.php?usu=" + correo.getText().toString() + "&cont=" + password.getText().toString();
                    final Intent iniciar = new Intent(login1.this, MainServidor.class);
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
                                                        String id = response.getString("id");
                                                        guardarId(id);
                                                        type_obtener = 2;
                                                        guardarTipo(type_obtener);
                                                        iniciar.putExtra("id", id);
                                                        startActivity(iniciar);
                                                        finish();
                                                        break;
                                                    case "NO":
                                                        Toast.makeText(login1.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    , new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(login1.this, "Error php", Toast.LENGTH_SHORT).show();
                                }
                            });
                    RequestQueue x = Volley.newRequestQueue(login1.this);
                    x.add(peticion);
                }
            }
        });


    }

    public static void changeEstado(Context c, boolean b) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON, b).apply();
    }

    public void guardarEstado() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON, radio_sesion.isChecked()).apply();
    }


    public void guardarId(String my_id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(login1.this);
        SharedPreferences.Editor myEditor = preferences.edit();
        myEditor.putString("ID", my_id);
        myEditor.commit();
    }


    public void guardarTipo(int my_type) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(login1.this);
        SharedPreferences.Editor myEditor = preferences.edit();
        myEditor.putInt("TIPO", my_type);
        myEditor.commit();
    }



    @Override
    public void onBackPressed() {

    }
}
