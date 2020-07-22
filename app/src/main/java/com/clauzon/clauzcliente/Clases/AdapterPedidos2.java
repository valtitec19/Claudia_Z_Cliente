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
import com.clauzon.clauzcliente.MainActivity;
import com.clauzon.clauzcliente.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdapterPedidos2 extends RecyclerView.Adapter<HolderPedidos> implements  View.OnClickListener{
    private List<Pedidos> lista=new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    private String nombres="";

    public AdapterPedidos2(Context context) {
        this.context = context;
    }

    public void add_pedido(Pedidos pedidos){
        this.lista.add(pedidos);
        notifyItemInserted(this.lista.size());
    }

    @NonNull
    @Override
    public HolderPedidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_pedidos, parent, false);
        view.setOnClickListener(this);
        return new HolderPedidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPedidos holder, final int position) {

        // if(lista.get(position).getEstado().equals("Pago pendiente (En efectivo)") || lista.get(position).getEstado().equals("Pagado") ||lista.get(position).getEstado().equals("Completado") || lista.get(position).getEstado().equals("Cancelado")){
        holder.getProductos_pedido().setText(lista.get(position).getNombre()+ " "+lista.get(position).getColor()+" "+lista.get(position).getTamano()+" "+lista.get(position).getModelo());
        holder.getLugar().setText("Entrega: "+lista.get(position).getDireccion_entrega());
        if(lista.get(position).getHora_entrega().equals("00:00")){
            holder.getFecha().setVisibility(View.GONE);
        }else {
            holder.getFecha().setText("Entrega: "+lista.get(position).getFecha()+", "+lista.get(position).getHora_entrega());
        }
        holder.getEstado().setText(lista.get(position).getEstado());
        float cantidad = lista.get(position).getCantidad() * lista.get(position).getCosto();
        holder.getCosto().setText("Costo: $"+String.valueOf(cantidad)+" - "+lista.get(position).getCantidad()+" Unidades");
        Glide.with(context).load(lista.get(position).getFoto()).centerCrop().override(250,250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto());
        //  }
        holder.getCancelar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("¿Desea eliminar este pedido del historial?");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference().child("Pedidos").child(lista.get(position).getId());
                        databaseReference.removeValue();
                        DatabaseReference databaseReference2 = database.getReference().child("Usuarios").child(lista.get(position).getUsuario_id()).child("pedidos").child(lista.get(position).getId());
                        databaseReference2.removeValue();

                        context.startActivity(new Intent(context, MainActivity.class));
                        ((Activity) context).finish();

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

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
