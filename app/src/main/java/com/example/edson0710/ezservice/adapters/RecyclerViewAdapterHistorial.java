package com.example.edson0710.ezservice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edson0710.ezservice.R;
import com.example.edson0710.ezservice.models.HistorialPojo;

import java.util.List;

public class RecyclerViewAdapterHistorial extends RecyclerView.Adapter<RecyclerViewAdapterHistorial.MyViewHolder> {

    private Context mContext;
    private List<HistorialPojo> mData;
    private RequestOptions option;

    public RecyclerViewAdapterHistorial(Context mContext, List<HistorialPojo> mData) {
        this.mContext = mContext;
        this.mData = mData;

        //Request option for Glide

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view;
        view = View.inflate(mContext, R.layout.historial_row_item, null);


        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_profesion.setText(mData.get(position).getProfesion());
        holder.tv_nombre.setText(mData.get(position).getNombre());
        holder.tv_fecha.setText(mData.get(position).getFecha());
        holder.tv_costo.setText("Costo: $" + mData.get(position).getCosto());

        //Load image from Internet

        Glide.with(mContext).load(mData.get(position).getImagen()).apply(option).into(holder.iv_imagen);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_profesion;
        TextView tv_costo;
        TextView tv_nombre;
        TextView tv_fecha;
        ImageView iv_imagen;
        LinearLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_nombre = itemView.findViewById(R.id.historial_nombre);
            iv_imagen = itemView.findViewById(R.id.historial_imagen);
            tv_costo = itemView.findViewById(R.id.historial_costo);
            tv_fecha = itemView.findViewById(R.id.historial_fecha);
            tv_profesion = itemView.findViewById(R.id.historial_profesion);
            container = itemView.findViewById(R.id.container_historial);

        }

    }


}