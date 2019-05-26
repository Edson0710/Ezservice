package com.example.edson0710.ezservice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registro2 extends AppCompatActivity {

    Bitmap bitmap;

    private String UPLOAD_URL = "http://ezservice.tech/";
    Button registrar;
    MaterialEditText correo, nombre, apellido, telefono, pass1, pass2;
    RadioButton hombre, mujer;
    String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);

        tipo = getIntent().getExtras().getString("tipo");
        if (tipo.equals("servidor")){
            UPLOAD_URL = "http://ezservice.tech/registro2.php";
        } else {
            UPLOAD_URL = "http://ezservice.tech/registro3.php";
        }

        registrar = findViewById(R.id.registro2_registrar);
        correo = findViewById(R.id.registro2_correo);
        nombre = findViewById(R.id.registro2_nombre);
        apellido = findViewById(R.id.registro2_apellido);
        telefono = findViewById(R.id.registro2_telefono);
        pass1 = findViewById(R.id.registro2_password1);
        pass2 = findViewById(R.id.registro2_password2);
        hombre = findViewById(R.id.radio1);
        mujer = findViewById(R.id.radio2);


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obtenerDatos()) {
                    Intent intent = new Intent(Registro2.this, login1.class);
                    startActivity(intent);
                }
            }
        });


    }



    public boolean obtenerDatos() {
        String correoT = correo.getText().toString().trim();
        String nombreT = nombre.getText().toString().trim();
        String apellidoT = apellido.getText().toString().trim();
        String telefonoT = telefono.getText().toString();
        String pass1T = pass1.getText().toString().trim();
        String pass2T = pass2.getText().toString().trim();
        String sexo = "1";
        if (hombre.isChecked()) {
            sexo = "1";
        } else {
            sexo = "0";
        }


        if (correoT.equals("") || nombreT.equals("") || apellidoT.equals("") || telefonoT.equals("") ||
                pass1T.equals("") || pass2T.equals("")) {
            Toast.makeText(Registro2.this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();

        } else {
            if (pass1T.equals(pass2T)) {
                Toast.makeText(Registro2.this, "Las contraseñas coinciden", Toast.LENGTH_SHORT).show();
                registrar(nombreT, apellidoT, sexo, correoT, pass1T, telefonoT);
                return true;
            } else {
                Toast.makeText(Registro2.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }

    private void registrar(final String nombre, final String apellido, final String sexo, final String correo, final String password, final String telefono) {
        //Mostrar el diálogo de progreso
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(Registro2.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(Registro2.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String, String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put("nom", nombre);
                params.put("ape", apellido);
                params.put("sex", sexo);
                params.put("cor", correo);
                params.put("pass", password);
                params.put("tel", telefono);
                //Parámetros de retorno
                return params;
            }
        };

        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }


}
