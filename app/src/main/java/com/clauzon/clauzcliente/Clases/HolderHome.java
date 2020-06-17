package com.clauzon.clauzcliente.Clases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.R;

public class HolderHome extends RecyclerView.ViewHolder{
    private TextView costo;
    private ImageView foto_item;

    public HolderHome(@NonNull View itemView) {
        super(itemView);
        costo=(TextView)itemView.findViewById(R.id.costo__recycler);
        foto_item= (ImageView) itemView.findViewById(R.id.foto_recycler);

    }

    public TextView getCosto() {
        return costo;
    }

    public void setCosto(TextView costo) {
        this.costo = costo;
    }

    public ImageView getFoto_item() {
        return foto_item;
    }

    public void setFoto_item(ImageView foto_item) {
        this.foto_item = foto_item;
    }
}
