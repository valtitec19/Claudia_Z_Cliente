package com.clauzon.clauzcliente.Clases;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzcliente.CarritoActivity;
import com.clauzon.clauzcliente.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdapterCarrito extends RecyclerView.Adapter<HolderCarrito> implements View.OnClickListener {
    private float cantidad_;
    private String seleccion;
    private Pedidos p;
    private List<Pedidos> list = new ArrayList<>();
    private Context c;
    private View.OnClickListener listener;
    private String id_user;
    private int pos;

    public AdapterCarrito(Context c, String id_user) {
        this.c = c;
        this.id_user = id_user;
    }

    public void add_carrito(Pedidos pedidos) {
        this.list.add(pedidos);
        notifyItemInserted(this.list.size());
    }

    @NonNull
    @Override
    public HolderCarrito onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.recycler_carrito, parent, false);
        view.setOnClickListener(this);
        return new HolderCarrito(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderCarrito holder, final int position) {
        p = (list.get(position));
        holder.getProducto().setText(list.get(position).getNombre());
        holder.getColor().setText(list.get(position).getDescripcion());
        pos = position;
        holder.getUnidades().setText(String.valueOf(list.get(position).getCantidad()) + " Piezas");
        float cantidad = list.get(position).getCantidad() * list.get(position).getCosto();
        holder.getCosto().setText(String.valueOf(cantidad));
        holder.getAdd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pedidos pedidos=list.get(position);
                pedidos.setCantidad(pedidos.getCantidad()+1);
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference1 = database1.getReference();
                databaseReference1.child("Pedidos").child(list.get(position).getId()).setValue(pedidos);
                c.startActivity(new Intent(c, CarritoActivity.class));
                ((Activity) c).finish();
            }
        });

        holder.getRemove().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Pedidos pedidos=(list.get(position));
                if(pedidos.getCantidad()>1){
                    pedidos.setCantidad(pedidos.getCantidad()-1);
                    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference2 = database2.getReference();
                    databaseReference2.child("Pedidos").child(list.get(position).getId()).setValue(pedidos);
                    c.startActivity(new Intent(c, CarritoActivity.class));
                    ((Activity) c).finish();
                }else if(pedidos.getCantidad()==1){
                    final FirebaseDatabase database3 = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference3 = database3.getReference().child("Pedidos").child(pedidos.getId());
                    databaseReference3.removeValue();

                    DatabaseReference databaseReference4 = database3.getReference().child("Usuarios").child(pedidos.getUsuario_id()).child("pedidos").child(pedidos.getId());
                    databaseReference4.removeValue();

                    c.startActivity(new Intent(c, CarritoActivity.class));
                    ((Activity) c).finish();
                }
            }
        });

        holder.getBorrar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("¿Desea eliminar este pedido del carrito?");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference().child("Pedidos").child(list.get(position).getId());
                        databaseReference.removeValue();
                        DatabaseReference databaseReference2 = database.getReference().child("Usuarios").child(list.get(position).getUsuario_id()).child("pedidos").child(list.get(position).getId());
                        databaseReference2.removeValue();

                        c.startActivity(new Intent(c, CarritoActivity.class));
                        ((Activity) c).finish();

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });
        Glide.with(c).load(list.get(position).getFoto()).centerCrop().override(450, 450)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto_producto());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Pedidos> getLista() {
        return list;
    }

    @Override
    public void onClick(View view) {

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

}
