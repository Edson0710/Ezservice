package com.example.edson0710.ezservice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edson0710.ezservice.R;
import com.example.edson0710.ezservice.models.Lista;

import java.util.List;

public class RecyclerViewAdapterListaServidor extends RecyclerView.Adapter<RecyclerViewAdapterListaServidor.MyViewHolder> {

    private Context mContext2;
    private List<Lista> mData2;
    private RequestOptions option3;

    public RecyclerViewAdapterListaServidor(Context mContext2, List<Lista> mData2) {
        this.mContext2 = mContext2;
        this.mData2 = mData2;

        //Request option for Glide

        option3 = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view;
        view = View.inflate(mContext2, R.layout.lista_server_row_item, null);

        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.container3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(mContext2, TarjetasServidores.class);
                //i.putExtra("id",mData2.get(viewHolder.getAdapterPosition()).getId_profesion2());

                //mContext2.startActivity(i);
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //holder.tv_id.setText(""+mData2.get(position).getId());
        holder.tv_nombre.setText(mData2.get(position).getNombre());
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
        TextView tv_estado;
        ImageView iv_imagen;
        LinearLayout container3;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_nombre = itemView.findViewById(R.id.lista_nombre2);
            tv_estado = itemView.findViewById(R.id.lista_estado4);
            iv_imagen = itemView.findViewById(R.id.lista_imagen2);
            container3 = itemView.findViewById(R.id.container_lista2);
            container3.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 121, 0, "Eliminar");
            menu.add(this.getAdapterPosition(), 122, 1, "Aceptar");

        }

    }


}