package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clauzon.clauzcliente.Clases.AdapterEstacion;
import com.clauzon.clauzcliente.Clases.Estacion;
import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.Clases.Ruta;
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

public class EstacionesActivity extends AppCompatActivity {
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    //
    private ArrayList<Pedidos> lista_pedidos = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterEstacion adapterEstacion;
    private ArrayList<Estacion> estacions;
    private Usuario usuario;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        amount = (String) b.get("amount");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_estaciones);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterEstacion = new AdapterEstacion(EstacionesActivity.this);
        firebaseON();
        recyclerView.setAdapter(adapterEstacion);
        adapterEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                databaseReference=database.getReference("Usuarios/"+currentUser.getUid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usuario=dataSnapshot.getValue(Usuario.class);

                        final DatabaseReference databaseReference1=database.getReference("Pedidos");
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    final Pedidos pedidos=ds.getValue(Pedidos.class);
                                    if(pedidos.getEstado().equals("Carrito")){
                                        pedidos.setDireccion_entrega(adapterEstacion.get_lista().get(recyclerView.getChildAdapterPosition(view)).getNombre()+", "+adapterEstacion.get_lista().get(recyclerView.getChildAdapterPosition(view)).getLinea());
                                        pedidos.setCosto_envio(0);
                                        pedidos.setHora_entrega("00:00");
                                        //pedidos.setRepartidor_id(adapterEstacion.get_lista().get(recyclerView.getChildAdapterPosition(view)).getRuta());
                                        databaseReference1.child(pedidos.getId()).setValue(pedidos);
                                    }
                                }
                                DatabaseReference databaseReference2=database.getReference();
                                usuario.setDireccion_envio(adapterEstacion.get_lista().get(recyclerView.getChildAdapterPosition(view)).getNombre()+", "+adapterEstacion.get_lista().get(recyclerView.getChildAdapterPosition(view)).getLinea());
                                databaseReference2.child("Usuarios/"+currentUser.getUid()).setValue(usuario);
                                Intent intent1=new Intent(EstacionesActivity.this,HorarioEntregaActivity.class);
                                intent1.putExtra("estacion",adapterEstacion.get_lista().get(recyclerView.getChildAdapterPosition(view)).getNombre());
                                intent1.putExtra("linea",adapterEstacion.get_lista().get(recyclerView.getChildAdapterPosition(view)).getLinea());
                                intent1.putExtra("amount",amount);
                                startActivity(intent1);
                                finish();
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

        });
    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos

        databaseReference.child("Rutas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Ruta ruta = dataSnapshot.getValue(Ruta.class);
                try {
                    if(ruta.getEstado() && !ruta.getRepartidor().isEmpty()) {
                        for(int i=0;i<ruta.getEstaciones().size();i++){
                            ruta.getEstaciones().get(i).setRuta(ruta.getRepartidor());
                            adapterEstacion.add_producto(ruta.getEstaciones().get(i));
                        }
                    }
                }catch (Exception w){  }

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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EstacionesActivity.this,CarritoActivity.class));
    }
}
