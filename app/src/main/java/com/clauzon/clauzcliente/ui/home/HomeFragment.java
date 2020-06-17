package com.clauzon.clauzcliente.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.Adapters.AdapterItemGroup;
import com.clauzon.clauzcliente.Clases.Category;
import com.clauzon.clauzcliente.Clases.AdapterHome;
import com.clauzon.clauzcliente.Clases.Producto;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.clauzon.clauzcliente.Interface.IFirebaseLoadListener;
import com.clauzon.clauzcliente.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements IFirebaseLoadListener {


    private Button button;
    //private FloatingActionButton fab1, fab2;
    private SearchView searchView;
    private List<Producto> lista;
    private Spinner spinner;
    private Toolbar toolbar;
    String seleccion;
    private RecyclerView recyclerView, recyclerView2, recyclerView3, recyclerView4;
    private AdapterHome adapterHome, adapterHome2, adapterHome3, adapterHome4;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Usuario usuario;
    private TextView categoria1, categoria2, categoria3, categoria4;
    private HomeViewModel homeViewModel;

    //******************************************//
    private AlertDialog alertDialog;
    private IFirebaseLoadListener iFirebaseLoadListener;
    private RecyclerView my_recyclerView;
    private DatabaseReference myData;
    Category category;
    private ProgressBar progressBar;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//        toolbar = (Toolbar) root.findViewById(R.id.toolbar_home);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //firebaseON();
        Intent i = getActivity().getIntent();
        usuario = (Usuario) i.getSerializableExtra("user");
        firebaseON();
        progressBar=(ProgressBar)root.findViewById(R.id.progressBar);
        myData = FirebaseDatabase.getInstance().getReference("Catalogo Productos");
        iFirebaseLoadListener = this;

        getDataFirebase();

        my_recyclerView = (RecyclerView) root.findViewById(R.id.Recycler_Home);
        my_recyclerView.setHasFixedSize(true);
        my_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    public void getDataFirebase() {


        final ArrayList<Producto> list = new ArrayList<>();
        final ArrayList<Category> categories = new ArrayList<>();
        final ArrayList<String> nombres = new ArrayList<>();
        ArrayList<Producto> n_list;
        progressBar.setVisibility(View.VISIBLE);
        myData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if(producto.isEstado()){
                        list.add(producto);
                        nombres.add(producto.getCategoria());
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    category = new Category();
                    String c = list.get(i).getCategoria();
                    if (exist(list.get(i).getCategoria(), categories) == true) {
                        Log.e("ESIXTE", "   CHECK " + nombres.get(i));
                    } else if (exist(list.get(i).getCategoria(), categories) == false) {
                        category.setName(c);
                        category.setList(new_list(c, list));
                        categories.add(category);
                    }

                }
                iFirebaseLoadListener.OnFirebaseLoadSuccess(categories);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadListener.OnFirebaseFaield(databaseError.getMessage());
            }

        });
    }

    public Boolean exist(String nombre_catgoria, ArrayList<Category> categories) {
        Boolean estado = false;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(nombre_catgoria)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<Producto> new_list(String categoria, ArrayList<Producto> list) {
        ArrayList<Producto> nueva_lista = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (categoria.equals(list.get(i).getCategoria())) {
                nueva_lista.add(list.get(i));
            } else {

            }
        }
        return nueva_lista;
    }

    public void firebaseON() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Categorias");//Catalogo de los productos
    }

    @Override
    public void OnFirebaseLoadSuccess(List<Category> grupo_categorias) {
        final AdapterItemGroup adapterItemGroup = new AdapterItemGroup(getActivity(), grupo_categorias);
        my_recyclerView.setAdapter(adapterItemGroup);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void OnFirebaseFaield(String message) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.icono_logo);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.fav) {
//            startActivity(new Intent(getActivity(), FavActivity.class));
//
//        }
//        if (id == R.id.car) {
//            verifica_pedidos();
//        }
        return super.onOptionsItemSelected(item);
    }
}