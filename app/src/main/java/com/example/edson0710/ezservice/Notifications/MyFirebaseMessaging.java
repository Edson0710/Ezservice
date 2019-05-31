package com.example.edson0710.ezservice.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edson0710.ezservice.EditarPerfil;
import com.example.edson0710.ezservice.MainActivity;
import com.example.edson0710.ezservice.MessageActivity;
import com.example.edson0710.ezservice.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessaging extends FirebaseMessagingService {

    //Solo lo uso para comprobar la primera notificacion
    String titleX = "Hola";
    String bodyX = "Test";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //createNotificationChannel();
                jsoncall();
                sendOreoNotification();

            } else {
                jsoncall();
                sendNotification();
            }
        }
    }

    private void sendOreoNotification() {
        jsoncall();
        Intent intent = new Intent(this, MessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(titleX, bodyX, pendingIntent);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0, builder.build());

    }

    private void sendNotification() {
        jsoncall();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icono3)
                .setContentTitle(titleX)
                .setContentText(bodyX)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //.setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0, builder.build());

    }

    public void jsoncall() {
        String id_user = obtenerId();
        int tipo = obtenerTipo();

        String url = "http://ezservice.tech/notificaciones_firebase.php?id_user=" + id_user + "&tipo=" + tipo;
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
                                            int id_noti = response.getInt("id_noti");
                                            if (id_noti == 1) {
                                                titleX = "Nueva Solicitud";
                                                bodyX = "Tienes una nueva solicitud de servicio";
                                            } else if (id_noti == 2) {
                                                titleX = "Aceptado";
                                                bodyX = "Tu solicitud ha sido aceptada";
                                            }
                                            break;
                                        case "NO":
                                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error php", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue x = Volley.newRequestQueue(getApplicationContext());
        x.add(peticion);
    }

    public String obtenerId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String id_preference = preferences.getString("ID", "1");
        return id_preference;
    }

    public int obtenerTipo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int type_preference = preferences.getInt("TIPO", 1);
        return type_preference;
    }
}
