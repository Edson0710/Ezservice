package com.example.edson0710.ezservice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Solicitar extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    Button solicitar, mas, menos;
    TextView texto, ubicacion;
    String id_uc;
    int distancia = 10, n_servicios;
    private LocationManager locationManager;
    private Location location;
    double lat = 20.702978, lon = -103.388983;
    RatingBar ratingBar;
    float calificacion = 1;
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_solicitar, container, false);
        id_uc = getArguments().getString("id");
        n_servicios = getArguments().getInt("n_servicios");

        solicitar = (Button) rootView.findViewById(R.id.btn_solicitar);
        texto = (TextView) rootView.findViewById(R.id.tv_calificacion);
        ubicacion = rootView.findViewById(R.id.tv_distancia);
        mas = rootView.findViewById(R.id.btn_mas);
        menos = rootView.findViewById(R.id.btn_menos);
        ratingBar = rootView.findViewById(R.id.raitingbar2);
        ratingBar.setRating(calificacion);
        Toast.makeText(getContext(), "n: " + n_servicios, Toast.LENGTH_SHORT).show();

        if (n_servicios < 5){
            ratingBar.setVisibility(View.INVISIBLE);
            texto.setVisibility(View.INVISIBLE);
        }


        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(rootView.getContext(), "No hay permisos", Toast.LENGTH_SHORT).show();
        } else {
            //Ubicación
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distancia += 1;
                ubicacion.setText(distancia+" Km");

            }
        });

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (distancia > 1) {
                    distancia -= 1;
                    ubicacion.setText(distancia + " Km");
                }

            }
        });


        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calificacion = ratingBar.getRating();
                Intent i = new Intent(rootView.getContext(), Categorias.class);
                i.putExtra("id_uc", id_uc);
                i.putExtra("latitud", lat);
                i.putExtra("longitud", lon);
                i.putExtra("distancia", distancia);
                i.putExtra("calificacion", calificacion);
                startActivity(i);
            }
        });

        return rootView;
    }

}
