package com.example.edson0710.ezservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class Configuracion_Server extends Fragment {

    String id, nombre, apellido, imagen, calificacion;
    Button cerrarSesion;
    TextView tv_nombre, tv_calificacion, tv_historial, tv_editar;
    Switch disponibilidad;
    ImageView iv_imagen;
    private RequestOptions option;
    int estado;
    Boolean obtenerEstado;

    @Override
    public void onStart() {
        super.onStart();
        obtenerEstado = obtenerSwitch();
        if (obtenerEstado){
            disponibilidad.setChecked(true);
        } else {
            disponibilidad.setChecked(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_configuracion__server, container, false);

        id = getArguments().getString("id");
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);


        cerrarSesion = rootView.findViewById(R.id.btn_configuracion_server);
        tv_nombre = rootView.findViewById(R.id.tv1_configuracion_server);
        tv_historial = rootView.findViewById(R.id.tv3_historial_server);
        tv_calificacion = rootView.findViewById(R.id.tv2_configuracion_server);
        iv_imagen = rootView.findViewById(R.id.iv_configuracion_server);
        tv_editar = rootView.findViewById(R.id.tv4_editar_server);
        disponibilidad = rootView.findViewById(R.id.switch1);


        jsoncall();

        tv_historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), Historial.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        tv_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), EditarPerfil.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login1.changeEstado(rootView.getContext(), false);


                Intent i = new Intent(rootView.getContext(), login1.class);
                startActivity(i);

            }
        });

        obtenerEstado = obtenerSwitch();
        /*if (obtenerEstado){
            disponibilidad.setChecked(true);
        } else {
            disponibilidad.setChecked(false);
        }
*/

        disponibilidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (disponibilidad.isChecked()){
                    estado = 1;
                    jsoncall2();
                    guardarSwitch(true);
                }
                if (!disponibilidad.isChecked()){
                    estado = 0;
                    jsoncall2();
                    guardarSwitch(false);
                }
            }
        });



        return rootView;

    }

    void jsoncall() {
        String url = "http://ezservice.tech/miperfilServer.php?cat=" + id;
        JsonObjectRequest peticion = new JsonObjectRequest
                (
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    nombre = response.getString("nombre");
                                    apellido = response.getString("apellido");
                                    calificacion = response.getString("calificacion");
                                    imagen = response.getString("imagen");
                                    tv_nombre.setText(nombre + " " + apellido);
                                    tv_calificacion.setText(calificacion);
                                    Glide.with(getContext()).load(imagen).apply(option.circleCropTransform()).into(iv_imagen);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(getContext());
        x.add(peticion);
    }
    public void jsoncall2(){
        String url = "http://ezservice.tech/update_disponibilidad.php?id=" + id + "&est=" + estado;

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
                                            //Toast.makeText(getContext(), "Usuario ya solicitado", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "NO":
                                            //Toast.makeText(getContext(), "Añadido con éxito", Toast.LENGTH_SHORT).show();
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
                    }
                });
        RequestQueue x = Volley.newRequestQueue(getContext());
        x.add(peticion);
    }

    public void guardarSwitch(boolean my_switch) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor myEditor = preferences.edit();
        myEditor.putBoolean("switch", my_switch);
        myEditor.commit();
    }

    public boolean obtenerSwitch() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean id_preference = preferences.getBoolean("switch", true);
        return id_preference;

    }

}
