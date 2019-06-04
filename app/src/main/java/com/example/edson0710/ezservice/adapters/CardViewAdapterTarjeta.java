package com.example.edson0710.ezservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edson0710.ezservice.Comentarios;
import com.example.edson0710.ezservice.Profesiones;
import com.example.edson0710.ezservice.R;
import com.example.edson0710.ezservice.TarjetasServidores;
import com.example.edson0710.ezservice.models.TarjetaUsuario;

import java.util.List;

import static com.example.edson0710.ezservice.R.id.imagen_cardview;

public class CardViewAdapterTarjeta extends PagerAdapter {

    private List<TarjetaUsuario> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private RequestOptions option;


    public CardViewAdapterTarjeta(List<TarjetaUsuario> models, Context context) {
        this.models = models;
        this.context = context;

        //Request option for Glide

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.item_cardview, container, false);

        ImageView imagen;
        TextView nombre, distancia, calificacion, descripcion, edad, ocupacion;
        Button comentarios;
        TextView contador;

        imagen = view.findViewById(imagen_cardview);
        nombre = view.findViewById(R.id.nombre_cardview);
        descripcion = view.findViewById(R.id.descripcion_cardview);
        distancia = view.findViewById(R.id.km_cardview);
        calificacion = view.findViewById(R.id.calificacion_cardview);
        comentarios = view.findViewById(R.id.btn_comentarios);
        contador = view.findViewById(R.id.contador);
        edad = view.findViewById(R.id.edad_cardview);
        ocupacion = view.findViewById(R.id.ocupacion_cardview);

        contador.setText(position + 1 + " de " + models.size());
        nombre.setText(models.get(position).getNombre());
        descripcion.setText(models.get(position).getDescripcion());
        edad.setText(""+models.get(position).getEdad() + " años");
        distancia.setText(models.get(position).getDistancia()+ " Km");
        calificacion.setText("" + models.get(position).getCalificacion());
        ocupacion.setText(models.get(position).getOcupacion());
        //Load image from Internet

        Glide.with(context).load(models.get(position).getImagen()).apply(option.circleCropTransform()).into(imagen);

        comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Comentarios.class);
                i.putExtra("id", models.get(position).getId());
                context.startActivity(i);


            }
        });

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
