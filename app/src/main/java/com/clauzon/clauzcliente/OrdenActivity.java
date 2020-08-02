package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
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
    private TextView txt1, txt2, txt3, txt4,oferta,ahorro,txt_oferta,txt_ahorro,txt_precio_recomendado;
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
    private ImageAdapter imageAdapter;
    private ViewPager viewPager;
    private ArrayList<String> imagenes=new ArrayList<>();
    private LinearLayout linearLayout;
    private int position;
    private ImageView[] dots;
    private String fecha,id_compra;
    private int tarjeta;
    private Spinner spinner_colores,spinner_tamaños,spinner_modelos;
    private float costo_con_descuento;
    private String color_seleccionado,tamano_seleccionado,modelos_seleccionado;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        oferta=(TextView) findViewById(R.id.precio_oferta);
        ahorro=(TextView) findViewById(R.id.ahorro);
        txt_oferta=(TextView)findViewById(R.id.txt_oferta);
        txt_ahorro=(TextView)findViewById(R.id.txt_ahorro);
        txt_precio_recomendado=(TextView)findViewById(R.id.txt_precio_recomendado);
        spinner_colores=(Spinner) findViewById(R.id.spinne_color_orden);
        spinner_tamaños=(Spinner) findViewById(R.id.spinne_tamaño_orden);
        spinner_modelos=(Spinner) findViewById(R.id.spinne_modelo_orden);
        if(p_recibido.getColores().size()>0){
            ArrayList<String> array_colores=ordenar_array(p_recibido.getColores(),"Color");
            ArrayAdapter<String> adapter_colores = new ArrayAdapter<String>(OrdenActivity.this, R.layout.spinner_texto, array_colores);
            spinner_colores.setAdapter(adapter_colores);
            spinner_colores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    color_seleccionado = adapterView.getItemAtPosition(i).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            color_seleccionado="";
            spinner_colores.setVisibility(View.GONE);
        }
        if(p_recibido.getTamanos().size()>0){
            ArrayList<String> array_tamanos=ordenar_array(p_recibido.getTamanos(),"Tamaño");
            ArrayAdapter<String> adapter_tamanos = new ArrayAdapter<String>(OrdenActivity.this, R.layout.spinner_texto, array_tamanos);
            spinner_tamaños.setAdapter(adapter_tamanos);
            spinner_tamaños.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tamano_seleccionado = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            tamano_seleccionado="";
            spinner_tamaños.setVisibility(View.GONE);
        }
        if(p_recibido.getModelos().size()>0){
            ArrayList<String> array_modelos=ordenar_array(p_recibido.getModelos(),"Modelo");

            ArrayAdapter<String> adapter_modelos = new ArrayAdapter<String>(OrdenActivity.this, R.layout.spinner_texto, array_modelos);
            spinner_modelos.setAdapter(adapter_modelos);
            spinner_modelos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    modelos_seleccionado = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            modelos_seleccionado="";
            spinner_modelos.setVisibility(View.GONE);
        }

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

    private ArrayList<String> ordenar_array(ArrayList<String> arrayList, String valor){
        ArrayList<String> temp_array=new ArrayList<>();
        temp_array.addAll(arrayList);
        arrayList.clear();
        arrayList.add(valor);
        arrayList.addAll(temp_array);

        return arrayList;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cargar_producto(Producto p) {//carga los datos del producto en el activity para vizualizar
        txt1.setText(p.getNombre_producto());
        String descripcion = p.getDescripcion();
        txt3.setText("$"+String.valueOf(p.getVenta_producto()));
        txt2.setText(descripcion);
        oferta.setText("$"+p.getOferta());
        float descuento=p.getVenta_producto()-p.getOferta();
        costo_con_descuento=p.getVenta_producto()-descuento;
        float descuento_porcentaje=((p.getVenta_producto()-p.getOferta())/p.getVenta_producto())*100;
        String s = String.format("%.2f", descuento_porcentaje);

        if(descuento_porcentaje==0 || p.getOferta()==0){
            txt_precio_recomendado.setText("Costo: ");
            oferta.setVisibility(View.GONE);
            ahorro.setVisibility(View.GONE);
            ahorro.setText("0");
            txt_ahorro.setVisibility(View.GONE);
            txt_oferta.setVisibility(View.GONE);
            txt3.setBackground(this.getDrawable(R.drawable.fondo));
        }

        ahorro.setText("$"+descuento+"  "+"("+s+"%"+")");
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
            Log.e("Antes del if", String.valueOf(p_recibido.getVenta_producto()) );
            if(p_recibido.getVenta_producto()!= p_recibido.getOferta() && p_recibido.getOferta()!=0){
                p_recibido.setVenta_producto(costo_con_descuento);
                Log.e("Despues del if", String.valueOf(p_recibido.getVenta_producto()) );
            }
            if(color_seleccionado.equals("Color")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Seleccione un color valido");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Regresar al catalogo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(OrdenActivity.this,MainActivity.class));
                        finish();
                    }
                });
                builder.create().show();
            }else if(color_seleccionado.equals("Tamaño")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Seleccione un tamaño valido");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Regresar al catalogo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(OrdenActivity.this,MainActivity.class));
                        finish();
                    }
                });
                builder.create().show();
            }else if(modelos_seleccionado.equals("Modelo")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Seleccione un modelo valido");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Regresar al catalogo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(OrdenActivity.this,MainActivity.class));
                        finish();
                    }
                });
                builder.create().show();
            }else {
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
                        pedidos = new Pedidos(id, estado, cantidad,p_recibido.getVenta_producto(), currentUser.getUid(), "no asignado", "", "00:00",p_recibido.getNombre_producto(),p_recibido.getFoto_producto(),p_recibido.getDescripcion(),value,0,fecha,id_compra,tarjeta,color_seleccionado,tamano_seleccionado,modelos_seleccionado);
                        usuario.addPedido(value);
                        databaseReference.child("Pedidos").child(value).setValue(pedidos);
                        databaseReference.child("Usuarios").child(usuario.getId()).setValue(usuario);
                        startActivity(new Intent(OrdenActivity.this,MainActivity.class));
                        finish();
                    }
                });
                builder.create().show();
            }



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
        if(p_recibido.getVenta_producto()!= p_recibido.getOferta() && p_recibido.getOferta()!=0){
            p_recibido.setVenta_producto(costo_con_descuento);
            Log.e("Despues del if", String.valueOf(p_recibido.getVenta_producto()) );
        }
        value = UUID.randomUUID().toString();
        id = p_recibido.getId_producto();
        String estado = "Carrito";
        final int cantidad = Integer.parseInt(txt4.getText().toString());
        pedidos = new Pedidos(id, estado, cantidad,p_recibido.getVenta_producto(), currentUser.getUid(), "no asignado", "dirección", "00:00",p_recibido.getNombre_producto(),p_recibido.getFoto_producto(),p_recibido.getDescripcion(),value,0,fecha,id_compra,tarjeta,color_seleccionado,tamano_seleccionado,modelos_seleccionado);
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
                add_fav();

                Toast.makeText(OrdenActivity.this, "Producto añadido a favoritos", Toast.LENGTH_SHORT).show();
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
