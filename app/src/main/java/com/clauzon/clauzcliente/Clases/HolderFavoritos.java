package com.clauzon.clauzcliente.Clases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderFavoritos extends RecyclerView.ViewHolder{
    private TextView nombre,descripcion,costo;
    private CircleImageView foto;
    private ImageView borrar;

    public HolderFavoritos(@NonNull View itemView) {
        super(itemView);
        nombre = (TextView) itemView.findViewById(R.id.nombre_recycler_fav);
        nombre.setSelected(true);
        descripcion = (TextView) itemView.findViewById(R.id.descripcion_recycler_fav);
        descripcion.setSelected(true);
        foto=(CircleImageView)itemView.findViewById(R.id.foto_recycler_fav);
        costo=(TextView) itemView.findViewById(R.id.costo_recycler_fav);
        borrar=(ImageView)itemView.findViewById(R.id.borrar_fav);
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(TextView descripcion) {
        this.descripcion = descripcion;
    }

    public CircleImageView getFoto() {
        return foto;
    }

    public void setFoto(CircleImageView foto) {
        this.foto = foto;
    }

    public TextView getCosto() {
        return costo;
    }

    public void setCosto(TextView costo) {
        this.costo = costo;
    }

    public ImageView getBorrar() {
        return borrar;
    }

    public void setBorrar(ImageView borrar) {
        this.borrar = borrar;
    }
}
