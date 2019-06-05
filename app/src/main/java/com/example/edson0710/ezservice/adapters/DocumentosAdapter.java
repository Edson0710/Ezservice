package com.example.edson0710.ezservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edson0710.ezservice.R;
import com.example.edson0710.ezservice.models.Menu;

import java.util.ArrayList;
import java.util.List;

public class DocumentosAdapter extends RecyclerView.Adapter<DocumentosAdapter.MyViewHolder> {

    private Context mContext;
    private List<Menu> mData;
    private RequestOptions option2;
    private OnClickRecycler listener;

    public DocumentosAdapter( List<Menu> mData, OnClickRecycler listener) {
        this.listener = listener;
        this.mData = mData;
        option2 = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public DocumentosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.documento_item, parent, false);

        final DocumentosAdapter.MyViewHolder viewHolder = new DocumentosAdapter.MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DocumentosAdapter.MyViewHolder holder, int position) {


        Menu menu = mData.get(position);
        holder.bind(menu, listener);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnClickRecycler {
        void OnClickItemRecycler(Menu menu);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagen;
        LinearLayout container2;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagen = itemView.findViewById(R.id.imagen2_documento);
            //container2 = itemView.findViewById(R.id.container2);

        }

        public void bind(final Menu menu, final OnClickRecycler listener){
            Glide.with(imagen.getContext()).load(menu.getTitulo()).apply(option2).into(imagen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnClickItemRecycler(menu);
                }
            });

        }

    }


}