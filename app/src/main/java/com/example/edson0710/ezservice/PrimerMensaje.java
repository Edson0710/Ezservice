package com.example.edson0710.ezservice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PrimerMensaje extends AppCompatActivity {
    Intent intent;
    Button enviar;
    static TextView fecha;
    EditText problema, ubicacion, presupuesto;
    static int dayX, monthX, yearX;
    String problemaX, ubicacionX, presupuestoX, fechaX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_mensaje);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        final String imagenURL = intent.getStringExtra("imagenURL");
        final double telefono = getIntent().getExtras().getDouble("telefono");
        final String id_uc = getIntent().getExtras().getString("id_uc");
        final int id_us = getIntent().getExtras().getInt("id_us");

        enviar = findViewById(R.id.btn_enviar);
        fecha = findViewById(R.id.msg_fecha);
        problema = findViewById(R.id.msg_problema);
        ubicacion = findViewById(R.id.msg_ubicacion);
        presupuesto = findViewById(R.id.msg_presupuesto);

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        fecha.setText(date);


        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                problemaX = problema.getText().toString().trim();
                ubicacionX = ubicacion.getText().toString().trim();
                presupuestoX = presupuesto.getText().toString().trim();
                fechaX = fecha.getText().toString();

                Intent intent = new Intent(PrimerMensaje.this, MessageActivity.class);
                intent.putExtra("userid", userid);
                intent.putExtra("imagenURL", imagenURL);
                intent.putExtra("telefono", telefono);
                intent.putExtra("id_uc", id_uc);
                intent.putExtra("id_us", id_us);
                intent.putExtra("problema", problemaX);
                intent.putExtra("ubicacion", ubicacionX);
                intent.putExtra("presupuesto", presupuestoX);
                intent.putExtra("fecha", fechaX);
                finish();
                startActivity(intent);
            }
        });

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "datePicker");
            }
        });


    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //Toast.makeText(getActivity(), "Año:"+year+" mes:" + month + " dia:" + dayOfMonth, Toast.LENGTH_SHORT).show();
            month += 1;
            if (month < 10) {
                fecha.setText(year + "-0" + month + "-" + dayOfMonth);
            } else {
                fecha.setText(year + "-" + month + "-" + dayOfMonth);

            }

        }

    }

}
