package com.example.edson0710.ezservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edson0710.ezservice.MessageActivity;
import com.example.edson0710.ezservice.R;
import com.example.edson0710.ezservice.TarjetasServidores;
import com.example.edson0710.ezservice.models.Lista;
import com.example.edson0710.ezservice.models.Profesion;

import java.util.List;

public class RecyclerViewAdapterLista extends RecyclerView.Adapter<RecyclerViewAdapterLista.MyViewHolder> {

    private Context mContext2;
    private List<Lista> mData2;
    private RequestOptions option3;
    String id_uc;

    public RecyclerViewAdapterLista(Context mContext2, List<Lista> mData2, String id_uc) {
        this.mContext2 = mContext2;
        this.mData2 = mData2;
        this.id_uc = id_uc;

        //Request option for Glide

        option3 = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        final View view;
        view = View.inflate(mContext2, R.layout.lista_row_item, null);

        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.container3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prueba = mData2.get(viewHolder.getAdapterPosition()).getEstado();
                String id_firebase = mData2.get(viewHolder.getAdapterPosition()).getId_firebase();
                String imagenURL = mData2.get(viewHolder.getAdapterPosition()).getImagen();
                double telefono = mData2.get(viewHolder.getAdapterPosition()).getTelefono();
                int id_us = mData2.get(viewHolder.getAdapterPosition()).getId_us();
                Toast.makeText(mContext2, "Seleccionaste: "+prueba, Toast.LENGTH_SHORT).show();
                if (prueba.equals("Aceptado")){
                    Intent intent = new Intent(mContext2, MessageActivity.class);
                    intent.putExtra("userid", id_firebase);
                    intent.putExtra("imagenURL", imagenURL);
                    intent.putExtra("telefono", telefono);
                    intent.putExtra("id_uc", id_uc);
                    intent  .putExtra("id_us", id_us);
                    mContext2.startActivity(intent);
                }
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //holder.tv_id.setText(""+mData2.get(position).getId());
        holder.tv_nombre.setText(mData2.get(position).getNombre());
        holder.tv_profesion.setText(mData2.get(position).getProfesion());
        holder.tv_estado.setText(mData2.get(position).getEstado());

        //Load image from Internet

        //Glide.with(mContext2).load(mData2.get(position).getImagen()).apply(option3).into(holder.iv_imagen);
        Glide.with(mContext2).load(mData2.get(position).getImagen()).apply(option3.circleCropTransform()).into(holder.iv_imagen);

    }

    @Override
    public int getItemCount() {
        return mData2.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView tv_nombre;
        TextView tv_profesion;
        TextView tv_estado;
        ImageView iv_imagen;
        LinearLayout container3;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_nombre = itemView.findViewById(R.id.lista_nombre);
            tv_profesion = itemView.findViewById(R.id.lista_profesion);
            iv_imagen = itemView.findViewById(R.id.lista_imagen);
            tv_estado = itemView.findViewById(R.id.lista_estado2);
            container3 = itemView.findViewById(R.id.container_lista);
            container3.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 121, 0, "Delete");
        }

    }


}