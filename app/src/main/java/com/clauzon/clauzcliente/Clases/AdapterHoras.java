package com.clauzon.clauzcliente.Clases;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterHoras extends RecyclerView.Adapter<HolderHoras> implements View.OnClickListener{
    private List<String> lista=new ArrayList<>();
    private Context c;
    private String id,estacion,linea;
    private View.OnClickListener listener;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public AdapterHoras(Context c,String id,String estacion, String linea) {
        this.c = c;
        this.id=id;
        this.estacion=estacion;
        this.linea=linea;
    }

    public void add_lista(String hora){
        lista.add(hora);
        notifyItemInserted(lista.size());
    }

    @NonNull
    @Override
    public HolderHoras onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.relleno_recycler_horarios, parent, false);
        view.setOnClickListener(this);
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference();

        return new HolderHoras(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderHoras holder, final int position) {
        holder.getHora_r().setText(lista.get(position));


        holder.getHora_r().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child("Rutas").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds :snapshot.getChildren()){
                            final Ruta ruta =ds.getValue(Ruta.class);
                            for(int i=0; i<ruta.getEstaciones().size();i++){
                                if(estacion.equals(ruta.getEstaciones().get(i).getNombre()) && linea.equals(ruta.getEstaciones().get(i).getLinea())  && holder.getHora_r().getText().toString().equals(ruta.getEstaciones().get(i).getHora())){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                                    builder.setTitle("Hora asignada");
                                    builder.setMessage(lista.get(position));
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            holder.getHora_r().setTextColor(Color.GREEN);
                                            databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        Pedidos pedidos = snapshot.getValue(Pedidos.class);
                                                        if (pedidos.getUsuario_id().equals(id) && pedidos.getEstado().equals("Carrito")) {
                                                            pedidos.setHora_entrega(lista.get(position));
                                                            pedidos.setRepartidor_id(ruta.getRepartidor());
                                                            databaseReference.child("Pedidos").child(pedidos.getId()).setValue(pedidos);
                                                            Toast.makeText(c, "Hora asignada: "+lista.get(position), Toast.LENGTH_SHORT).show();

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
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public List<String> get_lista(){

        return lista;
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
