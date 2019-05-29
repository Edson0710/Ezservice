package com.example.edson0710.ezservice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfil extends AppCompatActivity {
    Bitmap bitmap;
    private static final int COD_SELECCION = 10;
    private static final int COD_FOTO = 20;
    private String UPLOAD_URL = "http://ezservice.tech/updateperfil.php";


    String id, nombre, apellido, correo;
    String telefono;
    Button aceptar;
    EditText et_nombre, et_apellido, et_correo, et_telefono;
    int type_obtener;
    String url1, url2, url_imagen;
    CircleImageView imagen;
    String correoT, nombreT, apellidoT, telefonoT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        id = getIntent().getExtras().getString("id");
        et_nombre = findViewById(R.id.perfil_nombre);
        et_apellido = findViewById(R.id.perfil_apellido);
        et_correo = findViewById(R.id.perfil_correo);
        et_telefono = findViewById(R.id.perfil_telefono);
        imagen = findViewById(R.id.perfil_imagen);

        type_obtener = obtenerTipo();

        if (type_obtener == 1) {
            url1 = "http://ezservice.tech/editarperfil.php?cat=" + id;

        }
        if (type_obtener == 2) {
            url1 = "http://ezservice.tech/editarperfil_server.php?cat=" + id;
        }


        jsoncall();


        aceptar = findViewById(R.id.perfil_aceptar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type_obtener == 1) {
                    UPLOAD_URL = "http://ezservice.tech/updateperfil.php";
                    nombreT = et_nombre.getText().toString().trim();
                    apellidoT = et_apellido.getText().toString().trim();
                    correoT = et_correo.getText().toString().trim();
                    telefonoT = et_telefono.getText().toString().trim();

                }
                if (type_obtener == 2) {
                    UPLOAD_URL = "http://ezservice.tech/updateperfil_server.php";
                    nombreT = et_nombre.getText().toString().trim();
                    apellidoT = et_apellido.getText().toString().trim();
                    correoT = et_correo.getText().toString().trim();
                    telefonoT = et_telefono.getText().toString().trim();
                }

                Handler handler = new Handler();

                uploadImage(nombreT, apellidoT, correoT, telefonoT);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(EditarPerfil.this, ""+nombre2, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 2000);


            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });

    }

    public void jsoncall() {
        JsonObjectRequest peticion = new JsonObjectRequest
                (
                        Request.Method.GET,
                        url1,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    nombre = response.getString("nombre");
                                    apellido = response.getString("apellido");
                                    correo = response.getString("correo");
                                    telefono = response.getString("telefono");
                                    url_imagen = response.getString("imagen");

                                    et_nombre.setText(nombre);
                                    et_apellido.setText(apellido);
                                    et_correo.setText(correo);
                                    et_telefono.setText(telefono);
                                    //Glide.with(EditarPerfil.this).load(url_imagen).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imagen);
                                    Picasso.with(EditarPerfil.this).load(url_imagen).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imagen);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditarPerfil.this, "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(EditarPerfil.this);
        x.add(peticion);
    }

    public int obtenerTipo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditarPerfil.this);
        int type_preference = preferences.getInt("TIPO", 1);
        return type_preference;
    }

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones = {"Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditarPerfil.this);
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
        bitmap = redimensionarImagen(bitmap, 600, 800);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncha =  anchoNuevo/ancho;
            float escalaAlto = altoNuevo/alto;
            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncha, escalaAlto);
            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }

    }

    private void uploadImage(final String nombre, final String apellido, final String correo, final String telefono) {
        //Mostrar el diálogo de progreso
        final ProgressDialog loading = ProgressDialog.show(this, "Actualizando...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(EditarPerfil.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(EditarPerfil.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                params.put("cat", id);
                params.put("cor", correo);
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
