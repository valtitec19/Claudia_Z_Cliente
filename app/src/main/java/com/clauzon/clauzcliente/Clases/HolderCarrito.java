package com.clauzon.clauzcliente.Clases;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.R;

public class HolderCarrito extends RecyclerView.ViewHolder {
    private TextView producto,color,costo,unidades;
    private ImageView foto_producto,borrar;
    private Button add,remove;

    public HolderCarrito(@NonNull View itemView) {
        super(itemView);
        producto=(TextView)itemView.findViewById(R.id.producto_recycler_carrito);
        color=(TextView)itemView.findViewById(R.id.color_Recycler_carrito);
        costo=(TextView)itemView.findViewById(R.id.costo_recycler_carrito);
        foto_producto=(ImageView)itemView.findViewById(R.id.imagen_carrito_recycler);
        unidades=(TextView)itemView.findViewById(R.id.unidades);
        borrar=(ImageView)itemView.findViewById(R.id.borrar);
        add=(Button)itemView.findViewById(R.id.add_unidad);
        remove=(Button)itemView.findViewById(R.id.remove_unidad);
    }

    public TextView getProducto() {
        return producto;
    }

    public void setProducto(TextView producto) {
        this.producto = producto;
    }

    public TextView getColor() {
        return color;
    }

    public void setColor(TextView color) {
        this.color = color;
    }

    public TextView getCosto() {
        return costo;
    }

    public void setCosto(TextView costo) {
        this.costo = costo;
    }

    public ImageView getFoto_producto() {
        return foto_producto;
    }

    public void setFoto_producto(ImageView foto_producto) {
        this.foto_producto = foto_producto;
    }

    public TextView getUnidades() {
        return unidades;
    }

    public void setUnidades(TextView unidades) {
        this.unidades = unidades;
    }

    public ImageView getBorrar() {
        return borrar;
    }

    public void setBorrar(ImageView borrar) {
        this.borrar = borrar;
    }

    public Button getAdd() {
        return add;
    }

    public void setAdd(Button add) {
        this.add = add;
    }

    public Button getRemove() {
        return remove;
    }

    public void setRemove(Button remove) {
        this.remove = remove;
    }
}
