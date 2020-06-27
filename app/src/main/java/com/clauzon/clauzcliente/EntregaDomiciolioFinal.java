package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.clauzon.clauzcliente.Clases.Pedidos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EntregaDomiciolioFinal extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ArrayList<Pedidos> lista = new ArrayList<>();
    private TextView productos, costo, descrcipcion;
    private Spinner spinner;
    private Button button;
    private RadioButton tarjeta,efectivo;
    private EditText descripcion_fisica;
    String product = "";
    float cost;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_domiciolio_final);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        amount = (String) b.get("amount");
        Log.e("Primer recibido ", amount);
        firebaseON();
        inicio_view();
        recuperda_pedidos();

        descripcion_fisica=(EditText)findViewById(R.id.descripcion_fachada_domicio_final);
        productos = (TextView) findViewById(R.id.productos_domicio_final);
        costo = (TextView) findViewById(R.id.costo_domicio_final);
        descrcipcion = (TextView) findViewById(R.id.descripcion_domicio_final);
        tarjeta=(RadioButton)findViewById(R.id.radio_button_tarjeta_domicio_final);
        efectivo=(RadioButton)findViewById(R.id.radio_butos_efectivo_domicio_final);

    }
    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }
    public void inicio_view() {



        button = (Button) findViewById(R.id.btn_entrega_domicio_final);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(efectivo.isChecked()){

                    if(descripcion_fisica.getText().toString().equals("")){
                        descripcion_fisica.setError("Campo obligatorio");
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(EntregaDomiciolioFinal.this);
                        builder.setTitle("Descripcion del pedido");
                        builder.setMessage(descrcipcion.getText().toString()+" Entrega: "+descripcion_fisica.getText().toString());
                        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Pedidos pedidos = snapshot.getValue(Pedidos.class);
                                            if (pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")) {
                                                pedidos.setEstado("Pago pendiente (En efectivo)");
                                                pedidos.setDescripcion("Punto de entrega: "+descripcion_fisica.getText().toString());
                                                DatabaseReference databaseReference2 = database.getReference();
                                                databaseReference2.child("Pedidos/" + pedidos.getId()).setValue(pedidos);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                startActivity(new Intent(EntregaDomiciolioFinal.this,MainActivity.class));
                                finish();
                            }
                        });
                        builder.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.create().show();
                    }



                }else{
                    double temp= Float.parseFloat(amount)*0.97;
                    amount=String.valueOf(temp);
                    Intent intent= new Intent(EntregaDomiciolioFinal.this,PagoActivity.class);
                    intent.putExtra("amount",amount);
                    startActivity(intent);
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
                descrcipcion.setText("Entrega: " + lista.get(0).getDireccion_entrega());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
