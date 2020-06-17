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
import com.clauzon.clauzcliente.FavActivity;
import com.clauzon.clauzcliente.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterFavoritos extends RecyclerView.Adapter<HolderFavoritos> implements View.OnClickListener  {
    private List<Producto> lista = new ArrayList();
    private Usuario usuario;
    private Context c;
    private String id_user;
    private View.OnClickListener listener;


    public AdapterFavoritos(Context c,String id_user) {
        this.c = c;
        this.id_user=id_user;
    }

    public void add_producto(Producto producto) {
        lista.add(producto);
        notifyItemInserted(lista.size());
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

    @NonNull
    @Override
    public HolderFavoritos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.recycler_favoritos, parent, false);
        view.setOnClickListener(this);
        return new HolderFavoritos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderFavoritos holder, final int position) {
        //fb();
        holder.getNombre().setText(lista.get(position).getNombre_producto());
        holder.getDescripcion().setText(lista.get(position).getDescripcion());
        holder.getCosto().setText(String.valueOf(lista.get(position).getVenta_producto()));
        holder.getBorrar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("¿Desea eliminar este producto de favoritos?");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Usuarios/"+id_user).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                                List<String> favs=usuario.getFavoritos();
                                for(int i=0;i<favs.size();i++){
                                    if(favs.get(i).equals(lista.get(position).getId_producto())){
                                        favs.remove(i);
                                        usuario.setFavoritos(favs);
                                        databaseReference.child("Usuarios/"+id_user).setValue(usuario);
                                        c.startActivity(new Intent(c, FavActivity.class));
                                        ((Activity) c).finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

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
        Glide.with(c).load(lista.get(position).getFoto_producto()).centerCrop().override(450, 450)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto());

    }

    public List<Producto> get_lista(){
        return lista;
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }

//    public void fb(){
//        final FirebaseDatabase database =FirebaseDatabase.getInstance();
//        DatabaseReference reference =database.getReference("Catalogo Productos");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Producto producto=dataSnapshot.getValue(Producto.class);
//                for(int i=0;i<usuario.getFavoritos().size();i++){
//                    if(usuario.getFavoritos().get(i).equals(producto.getId_producto())){
//                        lista.add(producto);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
