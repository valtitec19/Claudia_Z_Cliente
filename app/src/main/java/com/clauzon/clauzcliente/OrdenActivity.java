package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clauzon.clauzcliente.Clases.ImageAdapter;
import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.Clases.Producto;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrdenActivity extends AppCompatActivity {

    private Producto p_recibido;
    private TextView txt1, txt2, txt3, txt4;
    private Button btn1, btn2, btn3;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private int contador = 0;
    private FirebaseUser currentUser;
    private Usuario usuario;
    private List<Pedidos> lista_pedidos=new ArrayList<>();
    private String value;
    private Pedidos pedidos;
    private String id,id_user;
    private ViewPager viewPager;
    private ArrayList<String> imagenes=new ArrayList<>();
    private LinearLayout linearLayout;
    private int position;
    private ImageView[] dots;
    private ImageAdapter imageAdapter;
    private String fecha,id_compra;
    private int tarjeta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden);
        firebaseOn();
        Intent i = getIntent();
        p_recibido = (Producto) i.getSerializableExtra("producto");
        //usuario=(Usuario)i.getSerializableExtra("user");
        txt1 = (TextView) findViewById(R.id.nombre_orden);
        txt2 = (TextView) findViewById(R.id.descripcion_orden);
        txt3 = (TextView) findViewById(R.id.precio_orden);
        txt4 = (TextView) findViewById(R.id.contador_orden);
        btn1 = (Button) findViewById(R.id.add_orden);
        btn2 = (Button) findViewById(R.id.remove_orden);
        btn3 = (Button) findViewById(R.id.añadir_orden);
        recupera_imagenes(p_recibido);
        //Log.e("IMAGEN RECUPERADA", p_recibido.getImagenes().get(0) );

        //imageView = (ImageView) findViewById(R.id.foto_orden);
        cargar_producto(p_recibido);
//        viewPagerOn();
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        imageAdapter=new ImageAdapter(this,imagenes);
        viewPager.setAdapter(imageAdapter);

        //viewPagerOn();
    }

    public void remove_contador(View view) {

        if (contador > 0) {
            contador = contador - 1;
        }
        txt4.setText(String.valueOf(contador));
    }

    public void recupera_imagenes(Producto producto){
        for(int i=0;i<producto.getImagenes().size();i++){
            imagenes.add(producto.getImagenes().get(i));
        }
    }

    public void add_contador(View view) {
        //contador = Integer.parseInt(txt4.getText().toString());
        contador = contador + 1;
        txt4.setText(String.valueOf(contador));
    }

    public void cargar_producto(Producto p) {//carga los datos del producto en el activity para vizualizar
        txt1.setText(p.getNombre_producto());
        String descripcion = p.getDescripcion();
        txt3.setText("$"+String.valueOf(p.getVenta_producto()));
        txt2.setText(descripcion);
//        Glide.with(this).
//                load(p.getFoto_producto())
//                .centerCrop()
//                .override(450, 450)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageView);
        contador = Integer.parseInt(txt4.getText().toString());
    }

    public void aceptar_orden(View view) {//Cargar pedido al usuario
        if (Integer.parseInt(txt4.getText().toString()) > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Producto añadido al carrito");
            builder.setPositiveButton("Pagar ahora", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    actualizar_usuario();
                    Intent intent = new Intent(OrdenActivity.this, CarritoActivity.class);
                    //intent.putExtra("id_pedido",value);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Seguir Comprando", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    value = UUID.randomUUID().toString();
                    id = p_recibido.getId_producto();
                    String estado = "Carrito";
                    final int cantidad = Integer.parseInt(txt4.getText().toString());
                    pedidos = new Pedidos(id, estado, cantidad,p_recibido.getVenta_producto(), currentUser.getUid(), "no asignado", "", "00:00",p_recibido.getNombre_producto(),p_recibido.getFoto_producto(),p_recibido.getDescripcion(),value,0,fecha,id_compra,tarjeta);
                    usuario.addPedido(value);
                    databaseReference.child("Pedidos").child(value).setValue(pedidos);
                    databaseReference.child("Usuarios").child(usuario.getId()).setValue(usuario);
                    startActivity(new Intent(OrdenActivity.this,MainActivity.class));
                    finish();
                }
            });
            builder.create().show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cantidad no válida").setMessage("Por favor seleccione una cantidad válida");
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(OrdenActivity.this, "Se cancelo la orden", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OrdenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            builder.create().show();
        }
    }

    public void firebaseOn() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            id_user=currentUser.getUid();
        }
    }

    public void actualizar_usuario() {//generar nuevo usuario con el pedido seleccionado
        value = UUID.randomUUID().toString();
        id = p_recibido.getId_producto();
        String estado = "Carrito";
        final int cantidad = Integer.parseInt(txt4.getText().toString());
        pedidos = new Pedidos(id, estado, cantidad,p_recibido.getVenta_producto(), currentUser.getUid(), "no asignado", "dirección", "00:00",p_recibido.getNombre_producto(),p_recibido.getFoto_producto(),p_recibido.getDescripcion(),value,0,fecha,id_compra,tarjeta);
        usuario.addPedido(value);
        databaseReference.child("Pedidos").child(value).setValue(pedidos);
        databaseReference.child("Usuarios").child(usuario.getId()).setValue(usuario);


        regresar();
    }

    private void regresar() {
        startActivity(new Intent(OrdenActivity.this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            id_user=currentUser.getUid();
            DatabaseReference referenceRepartidores = database.getReference("Usuarios/" + currentUser.getUid());
            referenceRepartidores.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usuario = dataSnapshot.getValue(Usuario.class);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

            finish();
        }
    }//Recuperando al usuario de firebase

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.fav:

                AlertDialog.Builder builder = new AlertDialog.Builder(OrdenActivity.this);
                builder.setTitle("¿Desea añadir este producto de favoritos?");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       add_fav();
                        Toast.makeText(OrdenActivity.this, "Producto añadido a favoritos", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrdenActivity.this,MainActivity.class));
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void add_fav(){
        usuario.addFav(p_recibido.getId_producto());
        databaseReference.child("Usuarios").child(usuario.getId()).setValue(usuario);
    }

    public void garantias(View view) {
        Intent intent=new Intent(OrdenActivity.this,GarantiasActivity.class);
        intent.putExtra("producto",p_recibido);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OrdenActivity.this,MainActivity.class));
        finish();
    }
}
