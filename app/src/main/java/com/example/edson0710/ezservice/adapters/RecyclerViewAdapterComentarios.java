package com.example.edson0710.ezservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.example.edson0710.ezservice.R;
import com.example.edson0710.ezservice.TarjetasServidores;
import com.example.edson0710.ezservice.models.Comentario;
import com.example.edson0710.ezservice.models.Profesion;

import java.util.List;

public class RecyclerViewAdapterComentarios extends RecyclerView.Adapter<RecyclerViewAdapterComentarios.MyViewHolder> {

    private Context mContext2;
    private List<Comentario> mData2;
    private RequestOptions option2;

    public RecyclerViewAdapterComentarios(Context mContext2, List<Comentario> mData2) {
        this.mContext2 = mContext2;
        this.mData2 = mData2;

        //Request option for Glide

        //option2 = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view;
        view = View.inflate(mContext2, R.layout.comentario_row_item, null);

        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.container3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_comentarios.setText(mData2.get(position).getComentario());
        holder.tv_fecha.setText(mData2.get(position).getFecha());

    }

    @Override
    public int getItemCount() {
        return mData2.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_comentarios;
        TextView tv_fecha;
        LinearLayout container3;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            tv_comentarios = itemView.findViewById(R.id.tv_comentario);
            container3 = itemView.findViewById(R.id.container3);

        }

    }


}