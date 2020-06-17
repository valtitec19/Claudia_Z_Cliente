package com.clauzon.clauzcliente.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.CarritoActivity;
import com.clauzon.clauzcliente.Clases.AdapterPedidos;
import com.clauzon.clauzcliente.Clases.AdapterPedidos2;
import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.FavActivity;
import com.clauzon.clauzcliente.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PedidosFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DashboardViewModel dashboardViewModel;
    private String id;
    private ArrayList<Pedidos> lista = new ArrayList<>();
    private TextView no_pedidos;
    private RecyclerView recyclerView;
    private AdapterPedidos adapterPedidos,adapterPedidos4;
    private AdapterPedidos2 adapterPedidos2,adapterPedidos3;
    private Spinner spinner;
    private String seleccion;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pedidos, container, false);
        firebaseON();
        no_pedidos = (TextView) root.findViewById(R.id.no_pedidos);
        verifica_pedidos_general();
        if (currentUser != null) {
            id = currentUser.getUid();
        }

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_pedidos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterPedidos = new AdapterPedidos(getContext(),currentUser.getUid());
        adapterPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        adapterPedidos2 = new AdapterPedidos2(getContext());
        adapterPedidos3 = new AdapterPedidos2(getContext());
        adapterPedidos4 = new AdapterPedidos(getContext(),currentUser.getUid());
        recyclerView.setAdapter(adapterPedidos);
        recyclerView.setHasFixedSize(true);
        rellena_recycler();
        String[] opciones = {"Próximas entregas","Pedidos completados","Pedidos cancelados","Todos los pedidos"};
        final ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(getContext(),R.layout.spinner,opciones);
        spinner= (Spinner)root.findViewById(R.id.spinner_pedidos);
        spinner.setAdapter(adapter_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seleccion=adapterView.getItemAtPosition(i).toString();
                if(seleccion.equals("Próximas entregas")){
                    recyclerView.setAdapter(adapterPedidos);
                }else if(seleccion.equals("Pedidos completados")){
                    recyclerView.setAdapter(adapterPedidos2);
                }else if(seleccion.equals("Pedidos cancelados")){
                    recyclerView.setAdapter(adapterPedidos3);
                }else if(seleccion.equals("Todos los pedidos")){
                    recyclerView.setAdapter(adapterPedidos4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_pedidos, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.fav) {
            startActivity(new Intent(getActivity(), FavActivity.class));

        }
        if (id == R.id.car) {
            verifica_pedidos();
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifica_pedidos_general() {
        databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Pedidos pedidos = ds.getValue(Pedidos.class);
                    if (pedidos.getUsuario_id().equals(id)) {
                        lista.add(pedidos);
                    }
                }
                if (lista.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    no_pedidos.setVisibility(View.VISIBLE);
                } else {
                    no_pedidos.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verifica_pedidos() {
        databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Pedidos pedidos = ds.getValue(Pedidos.class);
                    if (pedidos.getUsuario_id().equals(id) && pedidos.getEstado().equals("Carrito")) {

                        lista.add(pedidos);

                    }
                }
                if (lista.size() == 0) {
                    Toast.makeText(getContext(), "No tienes productos en tu carrito", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), CarritoActivity.class));
                    getActivity().finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

    }

    public void rellena_recycler() {

        databaseReference.child("Pedidos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedidos pedidos = dataSnapshot.getValue(Pedidos.class);
                if (pedidos.getUsuario_id().equals(id)) {
                    if (pedidos.getEstado().equals("Pago pendiente (En efectivo)") || pedidos.getEstado().equals("Pago Realizado")) {
                        adapterPedidos.add_pedido(pedidos);
                        adapterPedidos4.add_pedido(pedidos);
                    }else if(pedidos.getEstado().equals("Completado")){
                        adapterPedidos4.add_pedido(pedidos);
                        adapterPedidos2.add_pedido(pedidos);
                    }else if(pedidos.getEstado().equals("Cancelado")){
                        adapterPedidos4.add_pedido(pedidos);
                        adapterPedidos3.add_pedido(pedidos);
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


//        databaseReference.child("Pedidos").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Pedidos pedidos = snapshot.getValue(Pedidos.class);
//                    if (pedidos.getUsuario_id().equals(id)) {
//                        if (pedidos.getEstado().equals("Pago pendiente (En efectivo)") || pedidos.getEstado().equals("Pago Realizado")) {
//                            adapterPedidos.add_pedido(pedidos);
//                            adapterPedidos4.add_pedido(pedidos);
//                        }else if(pedidos.getEstado().equals("Completado")){
//                            adapterPedidos4.add_pedido(pedidos);
//                            adapterPedidos2.add_pedido(pedidos);
//                        }else if(pedidos.getEstado().equals("Cancelado")){
//                            adapterPedidos4.add_pedido(pedidos);
//                            adapterPedidos3.add_pedido(pedidos);
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            id = currentUser.getUid();
        }
    }
}