package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.clauzon.clauzcliente.Clases.AdapterFavoritos;
import com.clauzon.clauzcliente.Clases.Producto;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterFavoritos adapterFavoritos;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private Usuario usuario;
    private String id_user;
    private List<Producto> ids=new ArrayList<>();
    private List<String> ids_fav=new ArrayList<>();
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        firebaseON();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            id_user=currentUser.getUid();
        }
        recyclerView = (RecyclerView) findViewById(R.id.Recycler_Fav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FavActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterFavoritos = new AdapterFavoritos(FavActivity.this,id_user);

        adapterFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavActivity.this,OrdenActivity.class)
                .putExtra("producto",adapterFavoritos.get_lista().get(recyclerView.getChildAdapterPosition(view))));
            }
        });
        recyclerView.setAdapter(adapterFavoritos);

        databaseReference.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                for(int i=0;i<usuario.getFavoritos().size();i++){
                    ids_fav.add(usuario.getFavoritos().get(i));
                    Log.e("IDS **** ", ids_fav.get(i) );
                }
                databaseReference.child("Catalogo Productos").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Producto producto=dataSnapshot.getValue(Producto.class);
                        //adapterFavoritos.add_producto(producto);
                        for(int i=0;i<ids_fav.size();i++){
                            if(producto.getId_producto().equals(ids_fav.get(i))){
                                adapterFavoritos.add_producto(producto);
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void firebaseON() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            id_user=currentUser.getUid();
        }
    }
}
