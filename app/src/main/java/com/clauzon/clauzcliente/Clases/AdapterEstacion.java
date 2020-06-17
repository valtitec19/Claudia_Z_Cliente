package com.clauzon.clauzcliente.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzcliente.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterEstacion extends RecyclerView.Adapter<HolderEstacion> implements View.OnClickListener {
    private List<Estacion> lista = new ArrayList();
    private List<Estacion> rutas = new ArrayList();
    private Context c;
    private View.OnClickListener listener;


    public AdapterEstacion(Context c) {
        this.c = c;
    }

    public void add_producto(Estacion estacion) {
        lista.add(estacion);
        notifyItemInserted(lista.size());
    }

    public List<Estacion> get_lista(){

        return lista;
    }

    @NonNull
    @Override
    public HolderEstacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.recycler_estaciones, parent, false);
        view.setOnClickListener(this);
        return new HolderEstacion(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEstacion holder, int position) {

        for(int i=0;i<lista.size();i++){
            for(int j=i+1;j<lista.size();j++){
                if((lista.get(i).getNombre().equals(lista.get(j).getNombre()) && lista.get(i).getLinea().equals(lista.get(j).getLinea()))){
                    lista.remove(lista.get(j));
                }
            }
        }
        holder.getLinea().setText(lista.get(position).getLinea());
        holder.getNombre().setText(lista.get(position).getNombre());
        Glide.with(c).load(lista.get(position).getFoto()).centerCrop().override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getImagen());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }
}
