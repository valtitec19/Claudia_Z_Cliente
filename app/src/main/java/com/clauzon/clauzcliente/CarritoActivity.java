package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.PostalCodeEditText;
import com.clauzon.clauzcliente.Clases.AdapterCarrito;
import com.clauzon.clauzcliente.Clases.Estacion;
import com.clauzon.clauzcliente.Clases.Metro;
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
import com.mercadopago.MercadoPago;
import com.mercadopago.resources.datastructures.customer.card.Issuer;
import com.mercadopago.resources.datastructures.customer.card.PaymentMethod;
import com.sun.jersey.core.header.Token;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.services.network.UrlUtils;

import static android.view.Window.FEATURE_INDETERMINATE_PROGRESS;

public class CarritoActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private RecyclerView recyclerView;
    private AdapterCarrito adapterCarrito, adapterCarrito2, adapterCarrito3;
    private List<Producto> lista = new ArrayList<>();
    private ArrayList<Pedidos> lista2=new ArrayList<>();
    private List<Pedidos> lista_pedidos = new ArrayList<>();
    private Usuario me = new Usuario();

    private Pedidos pedido_recibido;
    private int suma;
    private String id;
    private Toolbar toolbar;
    private List<String> lista_ids = new ArrayList<>();
    private float suma_costo, costo_envio, costo_final;
    private String direccion;
    private TextView txt1, txt2, txt3, txt4, txt5,multas,costo_multas;
    private LinearLayout linearLayout;
    private ImageView atras, refresh;
    private Usuario usuario;
    private String id_pedido;
    private ProgressBar progressBar;
    private Button pagar;
    private ArrayList<Estacion> esraciones = new ArrayList<>();
    private String[] lineas = {"Linea 1", "Linea 2", "Linea 3", "Linea 4", "Linea 5", "Linea 6", "Linea 7", "Linea 8", "Linea 9", "Linea 12", "Linea A", "Linea B"};
    private Boolean tipo_envio;
    float costo2=0;
    private String descripcion_final;
    CharSequence[] values = {"$150 (Un dia despues) "," $120 (Dos dia despues)"," $100 (Tres dia despues)"};
    AlertDialog alertDialog1;
    String linea="";
    String linea2="";
    String estacion,linea_;
    private ImageView back;
    private String[] nombres={"Linea 1","Linea 2","Linea 3","Linea 4","Linea 5","Linea 6","Linea 7","Linea 8","Linea 9","Linea 12","Linea A","Linea B"};
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        firebaseON();
        recuperar_datos();
        Intent intent=getIntent();
        if(intent.getStringExtra("estacion")!=null && intent.getStringExtra("linea")!=null){
            estacion=intent.getStringExtra("estacion");
            linea_=intent.getStringExtra("linea");
        }
//        try{
//
//        }catch (Exception e){
//            estacion="PANTITLÁN";
//            linea="Linea 9";
//        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_carrito);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterCarrito = new AdapterCarrito(this, currentUser.getUid());
        recyclerView.setAdapter(adapterCarrito);
        recyclerView.setHasFixedSize(true);
        pagar = (Button) findViewById(R.id.btnBuy_carrito);
        txt1 = (TextView) findViewById(R.id.cantidad_productos);
        txt2 = (TextView) findViewById(R.id.costo_suma);
        txt3 = (TextView) findViewById(R.id.direccion_envio);
        txt4 = (TextView) findViewById(R.id.costo_envio);
        txt5 = (TextView) findViewById(R.id.costo_final);
        back=(ImageView) findViewById(R.id.atras_crrito);
        refresh=(ImageView) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CarritoActivity.this,CarritoActivity.class));
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CarritoActivity.this,MainActivity.class));
                finish();
            }
        });
        multas=(TextView)findViewById(R.id.multas);
        costo_multas=(TextView)findViewById(R.id.costo_multas);
        linearLayout=(LinearLayout)findViewById(R.id.layout_mulstas);

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                builder.setTitle("Seleccione un método de envío");
                builder.setPositiveButton("Envío a domicilio", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (usuario.getDireccion_envio().equals("no definido")) {
                            startActivity(new Intent(CarritoActivity.this, EnvioDomicilioActivity.class));
                            finish();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                            builder.setTitle("¿Utilizar dirección de envío existente?");
                            builder.setMessage(usuario.getDireccion_envio());
                            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    txt3.setText(usuario.getDireccion_envio());
                                    databaseReference.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final Usuario usuario=dataSnapshot.getValue(Usuario.class);
                                            String direccion=usuario.getDireccion_envio();
                                            linea=direccion.substring((direccion.length()-8),direccion.length());
                                            linea2=direccion.substring((direccion.length()-7),direccion.length());
                                            Log.e("LÍNEA ", linea );
                                            for(int i =0;i<nombres.length;i++){
                                                if(linea.equals(nombres[i]) || linea2.equals(nombres[i])){
                                                    Log.i("PRUEBAS    ", "Encontrado");
                                                    databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                Pedidos pedidos=ds.getValue(Pedidos.class);
                                                                if(pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")){
                                                                    pedidos.setCosto_envio(0);
                                                                    pedidos.setDireccion_entrega(usuario.getDireccion_envio());
                                                                    databaseReference.child("Pedidos").child(pedidos.getId()).setValue(pedidos);
                                                                    startActivity(new Intent(CarritoActivity.this,CarritoActivity.class));
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    if(txt4.getText().toString().equals("$0")){

                                    }else{ CreateAlertDialogWithRadioButtonGroup();}
                                }
                            });
                            builder.setNegativeButton("Agregar nueva direcciÓn", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in = new Intent(CarritoActivity.this, EnvioDomicilioActivity.class);
                                    //in.putExtra("id_pedido",id_pedido);
                                    startActivity(in);
                                    finish();
                                }
                            });
                            builder.create().show();
                        }
                    }
                });
                builder.setNegativeButton("Entrega en metro ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent1=new Intent(CarritoActivity.this, EstacionesActivity.class);
                        intent1.putExtra("amount",txt5.getText().toString());

                        startActivity(intent1);
                        finish();
                    }
                });
                builder.create().show();


            }
        });

        id = currentUser.getUid();
        verifica_pedidos();
        refresh = (ImageView) findViewById(R.id.refresh);
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt4.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                    builder.setTitle("Debe seleccionar un método de envío valido");
                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(CarritoActivity.this,MainActivity.class));
                        }
                    });
                    builder.create().show();
                }else{
                    if(tipo_envio){
                        Log.e("Costo final: ", txt5.getText().toString() );
                        Intent intent= new Intent(CarritoActivity.this,HorarioEntregaActivity.class);
                        String es,lin;
                        String direccion[]=txt3.getText().toString().split(",");
                        es=direccion[0];
                        lin=direccion[1].substring(1,direccion[1].length());
                        intent.putExtra("amount",txt5.getText().toString());
                        intent.putExtra("estacion",es);
                        intent.putExtra("linea",lin);
                        startActivity(intent);
                    }else{
                        Intent intent= new Intent(CarritoActivity.this,EntregaDomiciolioFinal.class);
                        intent.putExtra("amount",txt5.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        });
    }


    public void CreateAlertDialogWithRadioButtonGroup(){

        databaseReference.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Usuario usuario=dataSnapshot.getValue(Usuario.class);
                databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Pedidos pedidos=ds.getValue(Pedidos.class);
                            if(pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")){
                                pedidos.setDireccion_entrega(usuario.getDireccion_envio());
                                databaseReference.child("Pedidos").child(pedidos.getId()).setValue(pedidos);

                            }
                        }
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


        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);

        builder.setTitle("Eliga tiempo de envío");

        builder.setCancelable(false);

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        asginar_tipo_endio(150);

                        break;
                    case 1:

                        asginar_tipo_endio(120);
                        break;
                    case 2:

                        asginar_tipo_endio(100);
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    public void asginar_tipo_endio(final float costo_envio){
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Pedidos pedidos=ds.getValue(Pedidos.class);
                    if(pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")){
                        pedidos.setCosto_envio(costo_envio);
                        databaseReference.child("Pedidos").child(pedidos.getId()).setValue(pedidos);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recuperar_datos();
        startActivity(new Intent(CarritoActivity.this,CarritoActivity.class));
    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void recuperar_datos() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            databaseReference.child("Usuarios/" + currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    if(usuario.getMultas()>0){
                        linearLayout.setVisibility(View.VISIBLE);
                        multas.setText(String.valueOf(usuario.getMultas())+" Multas por cancelación");
                        costo_multas.setText("$"+String.valueOf(usuario.getMultas()*50));
                    }else {
                        linearLayout.setVisibility(View.GONE);
                    }
                    databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            lista_pedidos.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final Pedidos pedidos = snapshot.getValue(Pedidos.class);
                                if (pedidos.getUsuario_id().equals(id) && pedidos.getEstado().equals("Carrito")) {
                                    adapterCarrito.add_carrito(pedidos);
                                    suma = suma + pedidos.getCantidad();
                                    suma_costo = suma_costo + (pedidos.getCosto() * pedidos.getCantidad());
                                    costo_final= suma_costo + pedidos.getCosto_envio()+ (usuario.getMultas()*50);
                                    txt1.setText("Productos (" + String.valueOf(suma) + ")");
                                    txt2.setText("$" + String.valueOf(suma_costo));
                                    if (usuario.getDireccion_envio().equals("no definido")) {
                                        txt3.setText("Añade una opción de envío");
                                    } else {
                                        txt3.setText(usuario.getDireccion_envio());
                                    }
                                    txt5.setText(String.valueOf(costo_final));
                                    if(pedidos.getCosto_envio()==0 && usuario.getDireccion_envio().equals("no definido")){
                                        txt4.setText("");
                                    }else if(pedidos.getDireccion_entrega().equals("dirección") && pedidos.getCosto_envio()==0){
                                        txt4.setText("");
                                        txt3.setText("Añade una opción de envio");
                                    }else if(pedidos.getCosto_envio()==0 && !usuario.getDireccion_envio().equals("no definido")){
                                        txt4.setText("$"+0);
                                        tipo_envio=true;
                                    }else{
                                        txt4.setText("$"+pedidos.getCosto_envio());
                                        tipo_envio=false;
                                    }
                                }
                            }
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

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void verifica_pedidos(){
        databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Pedidos pedidos=ds.getValue(Pedidos.class);
                    if(pedidos.getUsuario_id().equals(id)){
                        lista2.add(pedidos);
                    }
                }
                if(lista2.size()==0){
                    startActivity(new Intent(CarritoActivity.this,MainActivity.class));
                }else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
