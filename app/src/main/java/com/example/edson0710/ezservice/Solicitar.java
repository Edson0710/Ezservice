package com.example.edson0710.ezservice;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Solicitar extends Fragment {

    Button solicitar;
    TextView texto;
    String id_uc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_solicitar, container, false);
        id_uc = getArguments().getString("id");

        solicitar = (Button) rootView.findViewById(R.id.btn_solicitar);
        texto = (TextView) rootView.findViewById(R.id.tv_solicitar);

        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(rootView.getContext(), Categorias.class);
                i.putExtra("id_uc", id_uc);

                startActivity(i);
            }
        });

        return rootView;
    }

}
