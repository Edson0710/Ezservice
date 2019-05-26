package com.example.edson0710.ezservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registros extends AppCompatActivity {

    Button registro1, registro2, registro3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        registro1 = findViewById(R.id.btn_registro1);
        registro2 = findViewById(R.id.btn_registro2);
        registro3 = findViewById(R.id.btn_registro3);

        registro1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Registros.this, Registro1.class);
                Registros.this.startActivity(intent1);
            }
        });

        registro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registros.this, Registro2.class);
                intent.putExtra("tipo", "servidor");
                Registros.this.startActivity(intent);
            }
        });

        registro3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registros.this, Registro2.class);
                intent.putExtra("tipo", "inter");
                Registros.this.startActivity(intent);
            }
        });
    }
}
