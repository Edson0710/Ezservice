package com.example.edson0710.ezservice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    Button login, register;
    int type_obtener;

    FirebaseUser firebaseUser;

    private static final String STRING_PREFERENCES = "preferencias";
    private static final String PREFERENCE_ESTADO_BUTTON = "estado.button";

    @Override
    protected void onStart() {
        super.onStart();

        ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id_obtener = obtenerId();
        if (firebaseUser != null) {
            if (obtenerEstado()) {
                type_obtener = obtenerTipo();
                if (type_obtener == 1) {
                    Intent iniciarAdmin = new Intent(StartActivity.this, MainActivity.class);
                    iniciarAdmin.putExtra("id", id_obtener);
                    startActivity(iniciarAdmin);
                    finish();
                }
                if (type_obtener == 2) {
                    Intent iniciarAdmin = new Intent(StartActivity.this, MainServidor.class);
                    iniciarAdmin.putExtra("id", id_obtener);
                    startActivity(iniciarAdmin);
                    finish();
                }
                if (type_obtener == 3) {
                    Intent iniciarAdmin = new Intent(StartActivity.this, MainInter.class);
                    iniciarAdmin.putExtra("id", id_obtener);
                    startActivity(iniciarAdmin);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, login1.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, Registros.class));
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    public int obtenerTipo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(StartActivity.this);
        int type_preference = preferences.getInt("TIPO", 1);
        return type_preference;
    }

    public String obtenerId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(StartActivity.this);
        String id_preference = preferences.getString("ID", "1");
        return id_preference;

    }

    public boolean obtenerEstado() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_BUTTON, false);
    }

}
