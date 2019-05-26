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
import com.example.edson0710.ezservice.models.Categoria;
import com.example.edson0710.ezservice.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapterCategoria extends RecyclerView.Adapter<RecyclerViewAdapterCategoria.MyViewHolder> {

    private Context mContext;
    private List<Categoria> mData;
    private RequestOptions option;
    String id_uc;
    double latitud, longitud;
    int distancia;
    float calificacion;

    public RecyclerViewAdapterCategoria(Context mContext, List<Categoria> mData, String id_uc, double latitud, double longitud, int distancia, float calificacion) {
        this.mContext = mContext;
        this.mData = mData;
        this.id_uc = id_uc;
        this.latitud = latitud;
        this.longitud = longitud;
        this.distancia = distancia;
        this.calificacion = calificacion;

        //Request option for Glide

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view;
        view = View.inflate(mContext, R.layout.categoria_row_item, null);


        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Profesiones.class);
                i.putExtra("id", mData.get(viewHolder.getAdapterPosition()).getId_categoria());
                i.putExtra("id_uc", id_uc);
                i.putExtra("latitud", latitud);
                i.putExtra("longitud", longitud);
                i.putExtra("distancia", distancia);
                i.putExtra("calificacion", calificacion);
                mContext.startActivity(i);
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_id.setText("" + mData.get(position).getId_categoria());
        holder.tv_nombre.setText(mData.get(position).getNombre());

        //Load image from Internet

        Glide.with(mContext).load(mData.get(position).getImagen_url()).apply(option.circleCropTransform()).into(holder.iv_imagen);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id;
        TextView tv_nombre;
        CircleImageView iv_imagen;
        LinearLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_id = itemView.findViewById(R.id.categoria_id);
            tv_nombre = itemView.findViewById(R.id.categoria_nombre);
            iv_imagen = itemView.findViewById(R.id.categoria_imagen);
            container = itemView.findViewById(R.id.container);

        }

    }


}

