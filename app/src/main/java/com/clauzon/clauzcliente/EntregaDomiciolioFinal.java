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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntregaDomiciolioFinal extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ArrayList<Pedidos> lista = new ArrayList<>();
    private TextView productos, costo, descrcipcion;
    private Spinner spinner;
    private Button button;
    private RadioButton tarjeta, efectivo;
    private EditText descripcion_fisica;
    String product = "";
    float cost;
    private String amount;
    private Pedidos pedidos_carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_domiciolio_final);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        amount = (String) b.get("amount");
        Log.e("Primer recibido ", amount);
        firebaseON();

        recuperda_pedidos();

        descripcion_fisica = (EditText) findViewById(R.id.descripcion_fachada_domicio_final);
        productos = (TextView) findViewById(R.id.productos_domicio_final);
        costo = (TextView) findViewById(R.id.costo_domicio_final);
        descrcipcion = (TextView) findViewById(R.id.descripcion_domicio_final);
        tarjeta = (RadioButton) findViewById(R.id.radio_button_tarjeta_domicio_final);
        efectivo = (RadioButton) findViewById(R.id.radio_butos_efectivo_domicio_final);
        button = (Button) findViewById(R.id.btn_entrega_domicio_final);
        inicio_view();
    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void inicio_view() {



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Direccion completa: ", pedidos_carrito.getDireccion_entrega() );
                int temporal = pedidos_carrito.getDireccion_entrega().length();
                String ciudad = pedidos_carrito.getDireccion_entrega().substring(temporal - 4, temporal);
                Log.e("Ciudad ", ciudad );
                if (efectivo.isChecked() && ciudad.equals("CDMX")) {

                    if (descripcion_fisica.getText().toString().equals("")) {
                        descripcion_fisica.setError("Campo obligatorio");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EntregaDomiciolioFinal.this);
                        builder.setTitle("Descripcion del pedido");
                        builder.setMessage(descrcipcion.getText().toString() + " Entrega: " + descripcion_fisica.getText().toString());
                        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            final Pedidos pedidos = snapshot.getValue(Pedidos.class);
                                            if (pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")) {
                                                pedidos.setEstado("Pago pendiente (En efectivo)");
                                                pedidos.setDireccion_entrega(pedidos.getDireccion_entrega()+" - "+descripcion_fisica.getText().toString());
                                                DatabaseReference databaseReference2 = database.getReference();
                                                databaseReference2.child("Pedidos/" + pedidos.getId()).setValue(pedidos);
                                                if (pedidos.getCosto_envio() == 120) {
                                                    recupera_info_notificacion("token", pedidos, "Correos de México");
                                                } else if (pedidos.getCosto_envio() == 150) {
                                                    recupera_info_notificacion("", pedidos, "CDMX");
                                                } else if (pedidos.getCosto_envio() == 250) {
                                                    recupera_info_notificacion("", pedidos, "FedEx");
                                                }

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                startActivity(new Intent(EntregaDomiciolioFinal.this, MainActivity.class));
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


                } else if (efectivo.isChecked() && !ciudad.equals("CDMX")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EntregaDomiciolioFinal.this);
                    builder.setTitle("El pago en efectivo solo aplica a CDMX");
                    builder.setMessage("Selecciona un método de pago diferente");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            efectivo.setChecked(false);
                            tarjeta.setChecked(true);

                        }
                    });
                    builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(EntregaDomiciolioFinal.this, MainActivity.class));
                            finish();
                        }
                    });
                    builder.create().show();
                } else {
                    double temp = Float.parseFloat(amount) * 0.97;
                    amount = String.valueOf(temp);
                    Intent intent = new Intent(EntregaDomiciolioFinal.this, PagoActivity.class);
                    intent.putExtra("amount", amount);
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
                        pedidos_carrito=pedidos;
                        lista.add(pedidos);
                    }
                }
                for (int i = 0; i < lista.size(); i++) {
                    product = product + lista.get(i).getNombre() + " ";
                    cost = cost + (lista.get(i).getCosto() * lista.get(i).getCantidad());
                }
                productos.setText(product);
                costo.setText("$" + amount);
                //descrcipcion.setText("Entrega: " + lista.get(0).getDireccion_entrega());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario =snapshot.getValue(Usuario.class);
                descrcipcion.setText("Entrega: "+usuario.getDireccion_envio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recupera_info_notificacion(final String token, final Pedidos pedidos, final String envio) {
        databaseReference.child("Usuarios/" + currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                enviar_notificacion(token, "Envío via " + envio + " para " + usuario.getNombre() + " " + usuario.getApellidos(), pedidos.getDireccion_entrega() + " " + usuario.getTelefono(), pedidos.getFoto());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void enviar_notificacion(String token, String titulo, String detalle, String imagen) {
        RequestQueue mRequestQue = Volley.newRequestQueue(this);

        JSONObject json = new JSONObject();
        try {

            json.put("to", "/topics/" + "domicilio");

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("titulo", titulo);
            notificationObj.put("detalle", detalle);
            notificationObj.put("imagen", imagen);


            //replace notification with data when went send data
            json.put("data", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json, null, null) {


                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAE3HNDFU:APA91bEmPKbwtdaQIrU9g2GmxBEwy7zqHzdwG-L3I7o6HzrKhJ5BupTBTqhN67ytbObOv_NUILcDMaG-HwCLi2tEFKDwOWShs14ZOGpWZOh2DJNhxwjAQIfPtWgn7sxWuDR9VfT4uPQW");
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
