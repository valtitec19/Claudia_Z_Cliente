package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clauzon.clauzcliente.Clases.AdapterHoras;
import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.Clases.Producto;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HorarioEntregaActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ArrayList<Pedidos> lista = new ArrayList<>();
    private TextView productos, costo, descrcipcion;
    private Spinner spinner;
    private Button button;
    private RadioButton tarjeta, efectivo,reloj,torniquetes;
    private Spinner ubicacion;
    String product = "";
    float cost;
    String hora, ubicacion_entrega;
    Calendar calendar;
    String fecha;
    Date mañana;
    Date fecha_final;
    String amount;
    private RecyclerView recyclerView;
    private AdapterHoras adapterHoras;
    private String estacion,linea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_entrega);
        firebaseON();
        inicio_view();
        recuperda_pedidos();
        Intent iin = getIntent();
        estacion=iin.getStringExtra("estacion");
        linea=iin.getStringExtra("linea");
        inicio_recycler();
        Bundle b = iin.getExtras();
        amount = (String) b.get("amount");
//        Log.e("Primer recibido ", amount);
        String languageToLoad = "Spanish"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        calendar = Calendar.getInstance(locale);
        calendar.add(Calendar.DATE, 1);
        //fecha_final=(Date)DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        mañana = calendar.getTime();
        fecha = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());


    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void inicio_recycler(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_horarios_disponibles);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterHoras = new AdapterHoras(this,currentUser.getUid(),estacion,linea);
        databaseReference.child("Rutas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Ruta ruta=dataSnapshot.getValue(Ruta.class);

                if(ruta.getEstado() && !ruta.getRepartidor().isEmpty()){
                    for(int i = 0; i<ruta.getEstaciones().size(); i++){
                        if(ruta.getEstaciones().get(i).getNombre().equals(estacion)){
                            if(linea.equals(ruta.getEstaciones().get(i).getLinea())){
                                adapterHoras.add_lista(ruta.getEstaciones().get(i).getHora());

                            }

                        }
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

        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(adapterHoras);




//        databaseReference.child("Rutas").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    Ruta ruta=ds.getValue(Ruta.class);
//                    for(int i = 0; i<ruta.getEstaciones().size(); i++){
//                    if(ruta.getEstaciones().get(i).getNombre().equals(estacion)){
//                        Log.e("Estacion correcta", "" );
//                        if(linea.equals(ruta.getEstaciones().get(i).getLinea())){
//                            Log.e("Nueva hora", "" );
//                            adapterHoras.add_lista(ruta.getEstaciones().get(i).getHora());
//                        }
//
//                    }
//                }
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
    public void onBackPressed() {
        Intent intent=new Intent(HorarioEntregaActivity.this,CarritoActivity.class);
        intent.putExtra("estacion",estacion);
        intent.putExtra("linea",linea);
        startActivity(intent);
        finish();
    }

    //metodo para resetear repartidor y hora de entrega en caso de no conluir el pedido
//    public void reset_pedido(){
//        databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds : snapshot.getChildren()){
//                    Pedidos pedidos = ds.getValue(Pedidos.class);
//                    if(pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")){
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void inicio_view() {
        reloj=(RadioButton) findViewById(R.id.radio_button_reloj);
        torniquetes=(RadioButton) findViewById(R.id.radio_butos_torniquetes);
        productos = (TextView) findViewById(R.id.productos_final_entrega);
        costo = (TextView) findViewById(R.id.costo_final_entrefa);
        descrcipcion = (TextView) findViewById(R.id.descripcion_final);
        button = (Button) findViewById(R.id.btn_horario_entrega);
//        spinner = (Spinner) findViewById(R.id.spinner_hora_entrega_final);
        tarjeta = (RadioButton) findViewById(R.id.radio_button_tarjeta);
        efectivo = (RadioButton) findViewById(R.id.radio_butos_efectivo);
        tarjeta.setChecked(true);
//        ArrayAdapter<CharSequence> horario = ArrayAdapter.createFromResource(this, R.array.horario, android.R.layout.simple_spinner_item);
//        spinner.setAdapter(horario);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                hora = adapterView.getItemAtPosition(i).toString();
//                databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            final Pedidos pedidos = snapshot.getValue(Pedidos.class);
//                            if (pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")) {
//                                databaseReference.child("Usuarios/" + currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
//                                        DatabaseReference reference = database.getReference("Pedidos/" + pedidos.getId());
//                                        pedidos.setDireccion_entrega(usuario.getDireccion_envio());
//                                        pedidos.setHora_entrega(hora);
//                                        reference.setValue(pedidos);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                                lista.add(pedidos);
//                            }
//                        }
//                        descrcipcion.setText("Entrega programada para " + fecha + " " + lista.get(0).getDireccion_entrega() + " a las " + hora);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        button = (Button) findViewById(R.id.btn_horario_entrega);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (efectivo.isChecked()) {
//Pedido con pago en efectivo, entrega en el metro
                    databaseReference.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Usuario usuario=dataSnapshot.getValue(Usuario.class);
                            if(usuario.getMultas()>0){
                                //Si tiene multa no permite el pago en efectivo, solo tarjeta
                                AlertDialog.Builder builder = new AlertDialog.Builder(HorarioEntregaActivity.this);
                                builder.setTitle("Lo lamento tienes una multa por cancelación");
                                builder.setMessage("Debes cubrir la multa para habilitar pago en efectivo");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        tarjeta.setChecked(true);
                                        Toast.makeText(HorarioEntregaActivity.this, "Para mayor informacion leer Terminos y Condiciones", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        tarjeta.setChecked(true);
                                        Toast.makeText(HorarioEntregaActivity.this, "Para mayor informacion leer Terminos y Condiciones", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.create().show();
                            }else{
                                //si se permite el pago en efectivo se elige la hora de entrega
                                databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            final Pedidos pedidos = snapshot.getValue(Pedidos.class);
                                            if (pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")) {

                                                if(reloj.isChecked()){
                                                    pedidos.setDescripcion(reloj.getText().toString());
                                                }else {
                                                    pedidos.setDescripcion(torniquetes.getText().toString());
                                                }
                                                //pedidos.setHora_entrega("");
                                                pedidos.setFecha(fecha);

//                                                pedidos.setHora_entrega("00:00");
                                                final DatabaseReference databaseReference2 = database.getReference();
                                                databaseReference2.child("Pedidos/" + pedidos.getId()).setValue(pedidos);

                                                /*MENSAJE FINAL */

                                                if(pedidos.getHora_entrega().equals("00:00")){
                                                    Toast.makeText(HorarioEntregaActivity.this, "Elija un horario de entrega", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    pedidos.setEstado("Pago pendiente (En efectivo)");
                                                    databaseReference2.child("Pedidos/" + pedidos.getId()).setValue(pedidos);
                                                    databaseReference.child("Catalogo Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                                Producto producto=ds.getValue(Producto.class);
                                                                if(producto.getId_producto().equals(pedidos.getProducto_id())){
                                                                    String mensaje=producto.getNombre_producto()+", "+pedidos.getDireccion_entrega()+", "+pedidos.getFecha()+", "+pedidos.getHora_entrega()+"\n"
                                                                            +"Telefono de contacto: "+usuario.getTelefono()+"\n"+"Costo: "+costo.getText().toString()+", "+pedidos.getCantidad()+" Piezas";

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(HorarioEntregaActivity.this,R.style.AlertDialogStyle);
                                                                    builder.setTitle("PEDIDO REALIZADO (PAGO PENDIENTE)");
                                                                    builder.setMessage(mensaje);

                                                                    builder.setCancelable(false);
                                                                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            //Enviar notificacion

                                                                            startActivity(new Intent(HorarioEntregaActivity.this, MainActivity.class));
                                                                            finish();
                                                                        }
                                                                    });
                                                                    builder.create().show();
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
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else if (tarjeta.isChecked()) {
                    double temp= Float.parseFloat(amount)*0.97;
                    amount=String.valueOf(temp);
                    databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Pedidos pedidos = snapshot.getValue(Pedidos.class);
                                if (pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")) {
//                                    pedidos.setDescripcion("Punto de entrega " + descripcion_fisica.getText().toString());
//                                    if(pedidos.getCosto_envio()==0){
//                                        pedidos.setHora_entrega(hora);
//                                    }else{
////                                        pedidos.setHora_entrega("00:00");
//                                    }
                                    pedidos.setFecha(fecha);
                                    if(reloj.isChecked()){
                                        pedidos.setDescripcion(reloj.getText().toString());
                                    }else {
                                        pedidos.setDescripcion(torniquetes.getText().toString());
                                    }
                                    DatabaseReference databaseReference2 = database.getReference();
                                    databaseReference2.child("Pedidos/" + pedidos.getId()).setValue(pedidos);
                                    if(pedidos.getHora_entrega().equals("00:00")){
                                        Toast.makeText(HorarioEntregaActivity.this, "Elija una hora de entrega", Toast.LENGTH_SHORT).show();
                                    }else {
                                        startActivity(new Intent(HorarioEntregaActivity.this, PagoActivity.class).putExtra("amount", amount));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }

    public void recuperda_pedidos() {
        databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pedidos pedidos = snapshot.getValue(Pedidos.class);
                    if (pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")) {
                        lista.add(pedidos);
                    }
                }
                for (int i = 0; i < lista.size(); i++) {
                    product = product + lista.get(i).getNombre() + " ";
                    cost = cost + (lista.get(i).getCosto() * lista.get(i).getCantidad());
                }
                productos.setText(product);
                costo.setText("$" + amount);
                descrcipcion.setText("Entrega programada para el dia de mañana en " + lista.get(0).getDireccion_entrega() );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
