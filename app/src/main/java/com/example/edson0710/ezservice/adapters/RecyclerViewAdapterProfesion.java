package com.example.edson0710.ezservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edson0710.ezservice.Profesiones;
import com.example.edson0710.ezservice.R;
import com.example.edson0710.ezservice.Registros;
import com.example.edson0710.ezservice.TarjetasServidores;
import com.example.edson0710.ezservice.models.Categoria;
import com.example.edson0710.ezservice.models.Profesion;

import java.util.List;

public class RecyclerViewAdapterProfesion extends RecyclerView.Adapter<RecyclerViewAdapterProfesion.MyViewHolder> {

    private Context mContext2;
    private List<Profesion> mData2;
    private RequestOptions option2;
    String id_uc;

    public RecyclerViewAdapterProfesion(Context mContext2, List<Profesion> mData2, String id_uc) {
        this.mContext2 = mContext2;
        this.mData2 = mData2;
        this.id_uc = id_uc;

        //Request option for Glide

        //option2 = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view;
        view = View.inflate(mContext2, R.layout.profesion_row_item, null);

        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.container2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext2, TarjetasServidores.class);
                i.putExtra("id", mData2.get(viewHolder.getAdapterPosition()).getId_profesion2());
                i.putExtra("id_uc", id_uc);

                mContext2.startActivity(i);
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_id.setText("" + mData2.get(position).getId_profesion2());
        holder.tv_nombre.setText(mData2.get(position).getNombre2());

        //Load image from Internet

        //Glide.with(mContext2).load(mData2.get(position).getImagen_url2()).apply(option2).into(holder.iv_imagen);

    }

    @Override
    public int getItemCount() {
        return mData2.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id;
        TextView tv_nombre;
        //ImageView iv_imagen;
        LinearLayout container2;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_id = itemView.findViewById(R.id.profesion_id);
            tv_nombre = itemView.findViewById(R.id.profesion_nombre);
            //iv_imagen = itemView.findViewById(R.id.profesion_imagen);
            container2 = itemView.findViewById(R.id.container2);

        }

    }


}