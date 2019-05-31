package com.example.edson0710.ezservice;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class Solicitar extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    Button solicitar, mas, menos, prueba;
    TextView texto, ubicacion;
    String id_uc;
    int distancia = 10, n_servicios;
    private LocationManager locationManager;
    private Location location;
    double lat = 1, lon = 1 ;
    RatingBar ratingBar;
    float calificacion = 1;
    private final static String CHANNEL_ID = "NOTIFICATION";
    private final static int NOTIFICATION_ID = 0;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference mNotificationDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_solicitar, container, false);
        id_uc = getArguments().getString("id");
        n_servicios = obtenerServicios();

        solicitar = rootView.findViewById(R.id.btn_solicitar);
        texto = rootView.findViewById(R.id.tv_calificacion);
        ubicacion = rootView.findViewById(R.id.tv_distancia);
        mas = rootView.findViewById(R.id.btn_mas);
        menos = rootView.findViewById(R.id.btn_menos);
        ratingBar = rootView.findViewById(R.id.raitingbar2);
        prueba = rootView.findViewById(R.id.prueba);
        ratingBar.setRating(calificacion);

        if (n_servicios < 5){
            ratingBar.setVisibility(View.INVISIBLE);
            texto.setVisibility(View.INVISIBLE);
        }


       if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(rootView.getContext(), "No hay permiso de ubicación", Toast.LENGTH_SHORT).show();
        } else {
            //Ubicación
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            lat = location.getLatitude();
            lon = location.getLongitude();
            Toast.makeText(getContext(), "lat"+lat, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "lon"+lon, Toast.LENGTH_SHORT).show();
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

        prueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*createNotificationChannel();
                createNotification();*/
        }
        });

        return rootView;
    }

    public int obtenerServicios() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int type_preference = preferences.getInt("servicios", 1);
        return type_preference;
    }

    public void  createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.icono3);
        builder.setContentTitle("Prueba");
        builder.setContentText("Notificacion prueba");
        //builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }


}
