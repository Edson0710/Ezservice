package com.example.edson0710.ezservice;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edson0710.ezservice.Notifications.APIService;
import com.example.edson0710.ezservice.Notifications.Client;
import com.example.edson0710.ezservice.Notifications.Data;
import com.example.edson0710.ezservice.Notifications.MyResponse;
import com.example.edson0710.ezservice.Notifications.Sender;
import com.example.edson0710.ezservice.Notifications.Token;
import com.example.edson0710.ezservice.adapters.CardViewAdapterTarjeta;
import com.example.edson0710.ezservice.models.TarjetaUsuario;
import com.example.edson0710.ezservice.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TarjetasServidores extends AppCompatActivity {

    int id;
    int id_us;
    String id_uc;
    private String JSON_URL = "http://ezservice.tech/mostrar_servidores.php?cat=" + id + "&id=" + id_uc;
    private JsonArrayRequest ArrayRequest;
    private RequestQueue requestQueue;
    ViewPager viewPager;
    CardViewAdapterTarjeta adapter;
    List<TarjetaUsuario> models;
    Button solicitar;
    FloatingActionButton home;
    double latitud, longitud;
    int distancia;
    float calificacion;
    DecimalFormat format1 = new DecimalFormat("#.##");
    APIService apiService;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    boolean mnotify = false;
    String url;
    String url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetas_servidores);
        id = getIntent().getExtras().getInt("id");
        id_uc = getIntent().getExtras().getString("id_uc");
        latitud = getIntent().getExtras().getDouble("latitud");
        longitud = getIntent().getExtras().getDouble("longitud");
        distancia = getIntent().getExtras().getInt("distancia");
        calificacion = getIntent().getExtras().getFloat("calificacion");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        JSON_URL = "http://ezservice.tech/mostrar_servidores.php?cat=" + id + "&id=" + id_uc;
        solicitar = findViewById(R.id.btn_contratar);
        home = findViewById(R.id.floating_btn);

        models = new ArrayList<>();
        jsoncall();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tipo = obtenerTipo();
                if (tipo == 1) {
                    Intent intent = new Intent(TarjetasServidores.this, MainActivity.class);
                    intent.putExtra("id", id_uc);
                    startActivity(intent);
                    finish();
                }
                if (tipo == 3) {
                    Intent intent = new Intent(TarjetasServidores.this, MainInter.class);
                    intent.putExtra("id", id_uc);
                    startActivity(intent);
                    finish();
                }

            }
        });


        solicitar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int cantidad = obtenerLista();
                if (cantidad < 5) {
                    int tipo = obtenerTipo();
                    if (tipo == 1) {
                        cantidad +=1;
                        guardarLista(cantidad);
                        Toast.makeText(TarjetasServidores.this, "lista:" + cantidad, Toast.LENGTH_SHORT).show();
                        id_us = models.get(viewPager.getCurrentItem()).getId();
                        url = "http://ezservice.tech/add_lista.php?id_uc=" + id_uc + "&id_us=" + id_us + "&est=" + 1;
                        jsoncall2();
                        url2 = "http://ezservice.tech/crear_notificacion.php?tipo=" + 2 + "&id_user=" + id_us + "&id_noti=" + 1;
                        jsoncall3();
                        mnotify = true;
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if (mnotify) {
                                    sendNotificaction(models.get(viewPager.getCurrentItem()).getId_firebase(), user.getUsername(), "hola");
                                }
                                mnotify = false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    if (tipo == 3) {
                        cantidad +=1;
                        guardarLista(cantidad);
                        id_us = models.get(viewPager.getCurrentItem()).getId();
                        url = "http://ezservice.tech/add_contratar.php?id_in=" + id_uc + "&id_us=" + id_us;
                        jsoncall2();
                        url2 = "http://ezservice.tech/crear_notificacion.php?tipo=" + 2 + "&id_user=" + id_us + "&id_noti=" + 1;
                        jsoncall3();
                        mnotify = true;
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if (mnotify) {
                                    sendNotificaction(models.get(viewPager.getCurrentItem()).getId_firebase(), user.getUsername(), "hola");
                                }
                                mnotify = false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                } else {
                    Toast.makeText(TarjetasServidores.this, "Tu lista esta llena", Toast.LENGTH_SHORT).show();
                }

                //removePage(viewPager.getCurrentItem());
                //viewPager.removeViewAt(viewPager.getCurrentItem());
                //models.remove(viewPager.getCurrentItem());
                //adapter.notifyDataSetChanged();
            }
        });
    }

    private void sendNotificaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver); //Ojo tambien
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    String bodys = username + ": " + message;
                    Data data = new Data(firebaseUser.getUid(), R.drawable.icono3, bodys, "Nuevo Mensaje",
                            models.get(viewPager.getCurrentItem()).getId_firebase());
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(TarjetasServidores.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void jsoncall() {

        ArrayRequest = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        TarjetaUsuario tarjetaUsuario = new TarjetaUsuario();
                        tarjetaUsuario.setId(jsonObject.getInt("id"));
                        tarjetaUsuario.setNombre(jsonObject.getString("nombre"));
                        tarjetaUsuario.setEdad(jsonObject.getInt("edad"));
                        tarjetaUsuario.setImagen(jsonObject.getString("imagen"));
                        tarjetaUsuario.setCalificacion(jsonObject.getDouble("calificacion"));
                        tarjetaUsuario.setDescripcion(jsonObject.getString("descripcion"));
                        tarjetaUsuario.setLatitud(jsonObject.getDouble("latitud"));
                        tarjetaUsuario.setLongitud(jsonObject.getDouble("longitud"));
                        tarjetaUsuario.setId_firebase(jsonObject.getString("id_firebase"));


                        Location locationA = new Location("punto A");
                        locationA.setLatitude(latitud);
                        locationA.setLongitude(longitud);
                        Location locationB = new Location("punto B");
                        locationB.setLatitude(jsonObject.getDouble("latitud"));
                        locationB.setLongitude(jsonObject.getDouble("longitud"));
                        float distance = locationA.distanceTo(locationB);
                        distance = distance / 1000;
                        if (tarjetaUsuario.getCalificacion() >= calificacion) {
                            if (distance <= distancia) {
                                tarjetaUsuario.setDistancia(format1.format(distance));
                                models.add(tarjetaUsuario);
                            }
                        }
                    } catch (JSONException e) {
                        e.getCause();
                    }


                }
                if (models == null || models.size() == 0) {
                    AlertDialog.Builder myBuild = new AlertDialog.Builder(TarjetasServidores.this);
                    myBuild.setMessage("No hay usuarios disponibles por el momento ó intenta con otros filtros");
                    myBuild.setTitle("Ezservice");
                    myBuild.setCancelable(false);
                    myBuild.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog dialog = myBuild.create();
                    dialog.show();
                }

                setupadapter(models);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TarjetasServidores.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(TarjetasServidores.this);
        requestQueue.add(ArrayRequest);

    }

    void jsoncall2() {
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
                                            Toast.makeText(TarjetasServidores.this, "Usuario ya solicitado", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "NO":
                                            Toast.makeText(TarjetasServidores.this, "Añadido con éxito", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TarjetasServidores.this, "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(TarjetasServidores.this);
        x.add(peticion);

    }


    public void setupadapter(List<TarjetaUsuario> models) {
        adapter = new CardViewAdapterTarjeta(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });
    }


    public void jsoncall3() {

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

                                            break;
                                        case "NO":
                                            //Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(getApplicationContext());
        x.add(peticion);
    }

    public int obtenerTipo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TarjetasServidores.this);
        int type_preference = preferences.getInt("TIPO", 1);
        return type_preference;
    }

    public void removePage(int position) {
        if ((position < 0) || (position >= models.size()) || (models.size() <= 1)) {

        } else {
            if (position == viewPager.getCurrentItem()) {
                if (position == (models.size() - 1)) {
                    viewPager.setCurrentItem(position - 1);
                } else if (position == 0) {
                    viewPager.setCurrentItem(1);
                }
            }
            viewPager.removeViewAt(viewPager.getCurrentItem());
            models.remove(viewPager.getCurrentItem());
            adapter.notifyDataSetChanged();
        }
    }

    public void guardarLista(int my_type) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TarjetasServidores.this);
        SharedPreferences.Editor myEditor = preferences.edit();
        myEditor.putInt("LISTA", my_type);
        myEditor.commit();
    }

    public int obtenerLista() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TarjetasServidores.this);
        int type_preference = preferences.getInt("LISTA", 0);
        return type_preference;
    }

}
