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

public class AdapterHome extends RecyclerView.Adapter<HolderHome> implements View.OnClickListener {
    private List<Producto> lista = new ArrayList();
    private Context c;
    private View.OnClickListener listener;

    public AdapterHome(Context c) {
        this.c = c;
    }

    public void add_producto(Producto producto) {
        lista.add(producto);
        notifyItemInserted(lista.size());
    }


    @NonNull
    @Override
    public HolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.recycler_item, parent, false);
        view.setOnClickListener(this);
        return new HolderHome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHome holder, int position) {
        holder.getCosto().setText(String.valueOf(lista.get(position).getVenta_producto()));
        Glide.with(c).load(lista.get(position).getFoto_producto()).centerCrop().override(450, 450)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto_item());

    }

    public List<Producto> getLista() {
        return lista;
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
