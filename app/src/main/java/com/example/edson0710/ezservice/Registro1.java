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
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registro1 extends AppCompatActivity {
    Bitmap bitmap;
    private static final int COD_SELECCION = 10;
    private static final int COD_FOTO = 20;
    private String UPLOAD_URL = "http://ezservice.tech/registro1.php";
    Button registrar;
    MaterialEditText correo, nombre, apellido, telefono, pass1, pass2, dia, mes, ano, direccion;
    RadioButton hombre, mujer;
    CircleImageView imagen;
    String url;
    String correoT,nombreT,apellidoT,pass1T, pass2T, sexo, telefonoT, diaT, mesT, anoT, direccionT;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

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
        dia = findViewById(R.id.registro1_dia);
        mes = findViewById(R.id.registro1_mes);
        ano = findViewById(R.id.registro1_ano);
        direccion = findViewById(R.id.registro1_direccion);
        auth = FirebaseAuth.getInstance();

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatos();
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

    public void obtenerDatos() {
        correoT = correo.getText().toString().trim();
        nombreT = nombre.getText().toString().trim();
        apellidoT = apellido.getText().toString().trim();
        telefonoT = telefono.getText().toString();
        pass1T = pass1.getText().toString().trim();
        pass2T = pass2.getText().toString().trim();
        direccionT = direccion.getText().toString().trim();
        diaT = dia.getText().toString().trim();
        mesT = mes.getText().toString().trim();
        anoT = ano.getText().toString().trim();

        sexo = "1";
        if (hombre.isChecked()) {
            sexo = "1";
        } else {
            sexo = "0";
        }


        if (correoT.equals("") || nombreT.equals("") || apellidoT.equals("") || telefonoT.equals("") ||
                pass1T.equals("") || pass2T.equals("") || diaT.equals("") || mesT.equals("") || anoT.equals("") || direccionT.equals("")) {
            Toast.makeText(Registro1.this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();

        } else {
            if (pass1T.equals(pass2T)) {
                register(nombreT,correoT,pass1T);
            } else {
                Toast.makeText(Registro1.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadImage(final String nombre, final String apellido, final String sexo, final String correo, final String password, final String telefono,
                             final String direccion, final String date) {
        //Mostrar el diálogo de progreso
        final ProgressDialog loading = ProgressDialog.show(this, "Registrando...", "Espere por favor...", false, false);
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
                params.put("dire", direccion);
                params.put("date", date);
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
                                        String date = anoT+"-"+mesT+"-"+diaT;
                                        uploadImage(nombreT, apellidoT, sexo, correoT, pass1T, telefonoT, direccionT, date);
                                        Intent intent = new Intent(Registro1.this, login1.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Registro1.this, "Registro Fallido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
