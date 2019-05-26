package com.example.edson0710.ezservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import com.bumptech.glide.Glide;
import com.example.edson0710.ezservice.models.Lista;
import com.example.edson0710.ezservice.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class login1 extends AppCompatActivity {
    Button ingresar;
    MaterialEditText correo, password;
    RadioGroup radio;
    RadioButton radio1, radio2, radio_sesion;
    int type_obtener;
    int estado_firebase;

    String url2;
    String id;
    String id_firebase = "null";

    String email, pass;

    private boolean isActivate;
    private static final String STRING_PREFERENCES = "preferencias";
    private static final String PREFERENCE_ESTADO_BUTTON = "estado.button";

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String username;

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

        auth = FirebaseAuth.getInstance();

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
                email = correo.getText().toString();
                pass = password.getText().toString();
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
                                                        type_obtener = 1;
                                                        id = response.getString("id");
                                                        estado_firebase = response.getInt("estado_firebase");
                                                        username = response.getString("nombre");
                                                        if(estado_firebase==0){
                                                            jsconcall_estado();
                                                            register(username, email, pass);
                                                        }

                                                        auth.signInWithEmailAndPassword(email, pass)
                                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Intent intent = new Intent(login1.this, MainActivity.class);
                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            guardarId(id);
                                                                            guardarTipo(type_obtener);
                                                                            intent.putExtra("id", id);
                                                                            Toast.makeText(login1.this, "All is fine", Toast.LENGTH_SHORT).show();
                                                                            startActivity(intent);
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(login1.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
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
                                                        type_obtener = 2;
                                                        id = response.getString("id");
                                                        estado_firebase = response.getInt("estado_firebase");
                                                        username = response.getString("nombre");
                                                        if(estado_firebase==0){
                                                            jsconcall_estado();
                                                            register(username, email, pass);
                                                        }
                                                        auth.signInWithEmailAndPassword(email, pass)
                                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Intent intent = new Intent(login1.this, MainServidor.class);
                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            guardarId(id);
                                                                            guardarTipo(type_obtener);
                                                                            intent.putExtra("id", id);
                                                                            Toast.makeText(login1.this, "All is fine", Toast.LENGTH_SHORT).show();
                                                                            startActivity(intent);
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(login1.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
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

    private void register(final String username, String email, String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username", username);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(login1.this, MainActivity.class);
                                        if (type_obtener==2) {
                                            intent = new Intent(login1.this, MainServidor.class);
                                        }

                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        guardarId(id);
                                        guardarTipo(type_obtener);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(login1.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(login1.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void jsconcall_estado(){


        if (type_obtener == 1) {
            url2 = "http://ezservice.tech/update_estado_firebase_comun.php?cat=" + id + "&est=" + 1 + "&idf=" + id_firebase;

        }
        if (type_obtener == 2) {
            url2 = "http://ezservice.tech/update_estado_firebase_servidor.php?cat=" + id + "&est=" + 1 + "&idf=" + id_firebase;
        }
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
                        Toast.makeText(login1.this, "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(login1.this);
        x.add(peticion);

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(login1.this, StartActivity.class);
        startActivity(intent);
    }
}
