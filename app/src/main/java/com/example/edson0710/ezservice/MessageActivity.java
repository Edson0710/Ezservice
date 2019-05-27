package com.example.edson0710.ezservice;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.edson0710.ezservice.Notifications.APIService;
import com.example.edson0710.ezservice.Notifications.Client;
import com.example.edson0710.ezservice.Notifications.Data;
import com.example.edson0710.ezservice.Notifications.MyResponse;
import com.example.edson0710.ezservice.Notifications.Sender;
import com.example.edson0710.ezservice.Notifications.Token;
import com.example.edson0710.ezservice.adapters.MessageAdapter;
import com.example.edson0710.ezservice.models.Chat;
import com.example.edson0710.ezservice.models.User;
import com.google.android.gms.flags.IFlagProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    Button button, llamada, evento;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;

    String userid;

    ValueEventListener seenListener;

    APIService apiService;
    int tipo;

    boolean notify = false;

    @Override
    protected void onStart() {
        super.onStart();
        final String estado = getIntent().getExtras().getString("estado");
        tipo = obtenerTipo();
        if (tipo == 2){
            button.setVisibility(View.INVISIBLE);
            if (estado.equals("Finalizando")){
                button.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.profile_username);
        button = findViewById(R.id.back);
        llamada = findViewById(R.id.phone);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        evento = findViewById(R.id.event);

        //apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        final String imagenURL = intent.getStringExtra("imagenURL");
        final double telefono = getIntent().getExtras().getDouble("telefono");
        final String id_uc = getIntent().getExtras().getString("id_uc");
        final int id_us = getIntent().getExtras().getInt("id_us");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addEventToCalendar(MessageActivity.this);
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "datePicker");
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipo == 2){
                    Intent intent = new Intent(MessageActivity.this, Calificar_comun.class);
                    intent.putExtra("id_uc", id_uc);
                    intent.putExtra("id_us", id_us);
                    startActivity(intent);
                }
                if (tipo==1) {
                    Intent intent = new Intent(MessageActivity.this, Calificar.class);
                    intent.putExtra("id_uc", id_uc);
                    intent.putExtra("id_us", id_us);
                    startActivity(intent);
                }
            }
        });

        llamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+telefono));
                startActivity(intent);
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (imagenURL.equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(imagenURL).into(profile_image);
                }

                readMessage(firebaseUser.getUid(), userid, imagenURL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userid);
    }


    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);

    }


    private void readMessage(final String myid, final String userid, final String imageurl){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public int obtenerTipo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MessageActivity.this);
        int type_preference = preferences.getInt("TIPO", 1);
        return type_preference;
    }


    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }

    private void addEventToCalendar(Activity activity){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 5);
        cal.set(Calendar.YEAR, 2019);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");


        intent.putExtra(CalendarContract.Events.ALL_DAY, true);
        intent.putExtra(CalendarContract.Events.RRULE , "FREQ=ALL_DAY");
        intent.putExtra(CalendarContract.Events.TITLE, "Servicio de Ezservice");

        activity.startActivity(intent);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog( getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Toast.makeText(getActivity(), "Año:"+year+" mes:" + month + " dia:" + dayOfMonth, Toast.LENGTH_SHORT).show();
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);

            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);
            intent.putExtra(CalendarContract.Events.ALL_DAY, true);
            intent.putExtra(CalendarContract.Events.TITLE, "Servicio de Ezservice");
            startActivity(intent);
        }
    }

}
