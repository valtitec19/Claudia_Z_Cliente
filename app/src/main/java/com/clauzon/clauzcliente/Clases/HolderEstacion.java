package com.clauzon.clauzcliente.Clases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderEstacion extends RecyclerView.ViewHolder {
    private TextView nombre,linea;
    private ImageView imagen;
    public HolderEstacion(@NonNull View itemView) {
        super(itemView);
        nombre=(TextView)itemView.findViewById(R.id.nombre_estacion_recycler);
        imagen=(ImageView) itemView.findViewById(R.id.foto_estacion_recycler);
        linea=(TextView) itemView.findViewById(R.id.nombre_linea_recycler);
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(CircleImageView imagen) {
        this.imagen = imagen;
    }

    public TextView getLinea() {
        return linea;
    }

    public void setLinea(TextView linea) {
        this.linea = linea;
    }
}
