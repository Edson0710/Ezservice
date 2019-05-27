package com.example.edson0710.ezservice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registro1 extends AppCompatActivity {
    Bitmap bitmap;
    private static final int COD_SELECCION = 10;
    private static final int COD_FOTO = 20;
    private String UPLOAD_URL = "http://ezservice.tech/registro1.php";
    Button registrar;
    MaterialEditText correo, nombre, apellido, telefono, pass1, pass2;
    RadioButton hombre, mujer;
    CircleImageView imagen;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro1);

        registrar = findViewById(R.id.registro1_registrar);
        correo = findViewById(R.id.registro1_correo);
        nombre = findViewById(R.id.registro1_nombre);
        apellido = findViewById(R.id.registro1_apellido);
        telefono = findViewById(R.id.registro1_telefono);
        pass1 = findViewById(R.id.registro1_password1);
        pass2 = findViewById(R.id.registro1_password2);
        hombre = findViewById(R.id.radio1);
        mujer = findViewById(R.id.radio2);
        imagen = findViewById(R.id.registro1_imagen);


        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obtenerDatos()) {
                    Intent intent = new Intent(Registro1.this, login1.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Registro1.this);
        builder.setTitle("Elige una opción: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (opciones[i].equals("Elegir de Galeria")) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCION);
                } else {
                    dialog.dismiss();
                }
            }


        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COD_SELECCION:
                Uri miPath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), miPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagen.setImageURI(miPath);
                break;
        }
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
            Toast.makeText(Registro1.this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();

        } else {
            if (pass1T.equals(pass2T)) {
                Toast.makeText(Registro1.this, "Las contraseñas coinciden", Toast.LENGTH_SHORT).show();
                uploadImage(nombreT, apellidoT, sexo, correoT, pass1T, telefonoT);
                return true;
            } else {
                Toast.makeText(Registro1.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }

    private void uploadImage(final String nombre, final String apellido, final String sexo, final String correo, final String password, final String telefono) {
        //Mostrar el diálogo de progreso
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(Registro1.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(Registro1.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);


                //Creación de parámetros
                Map<String, String> params = new Hashtable<String, String>();

                //Agregando de parámetros
                params.put("nom", nombre);
                params.put("ape", apellido);
                params.put("sex", sexo);
                params.put("cor", correo);
                params.put("pass", password);
                params.put("tel", telefono);
                params.put("imagen", imagen);
                //Parámetros de retorno
                return params;
            }
        };

        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }


    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
