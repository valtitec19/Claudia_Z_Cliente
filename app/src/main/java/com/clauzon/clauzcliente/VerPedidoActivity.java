package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.Clases.Producto;
import com.clauzon.clauzcliente.Clases.Repartidor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerPedidoActivity extends AppCompatActivity {
    private TextView producto,descripcion,direccion,repartidor,txt_repartidor,fecha,hora,txt_hora,cantidad,costo,estado,no_seguimiento,txt_descripcion,txt_direccion;
    private CircleImageView circleImageView;
    private Pedidos recibido;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedido);
        Intent i = getIntent();
        recibido = (Pedidos) i.getSerializableExtra("pedido");
        inicio_views();
        cargar_pedido(recibido);
    }

    private void cargar_pedido(Pedidos recibido) {
        producto.setText(recibido.getNombre());
        if(recibido.getCosto_envio()==0){
            txt_descripcion.setText("Lugar de entrega");
            txt_direccion.setText("Estación: ");
            descripcion.setText(recibido.getDescripcion());
            if(recibido.getColor()!=null && !recibido.getColor().equals("")){
                producto.setText(producto.getText().toString()+ " - Color: "+recibido.getColor());
            }
            if(recibido.getTamano()!=null && !recibido.getTamano().equals("")){
                producto.setText(producto.getText().toString()+ " - Tamaño: "+recibido.getTamano());
            }
            if(recibido.getModelo()!=null && !recibido.getModelo().equals("")){
                producto.setText(producto.getText().toString()+" - Modelo: "+recibido.getModelo());
            }
        }else {
            descripcion.setText("");
            if(recibido.getColor()!=null && !recibido.getColor().equals("")){
                descripcion.setText(descripcion.getText().toString()+ "  - Color: "+recibido.getColor());
            }
            if(recibido.getTamano()!=null && !recibido.getTamano().equals("")){
                descripcion.setText(descripcion.getText().toString()+ " - Tamaño: "+recibido.getTamano());
            }
            if(recibido.getModelo()!=null && !recibido.getModelo().equals("")){
                descripcion.setText(descripcion.getText().toString()+" - Modelo: "+recibido.getModelo());
            }
            if(descripcion.getText().toString().isEmpty()){
                descripcion.setText(recibido.getDescripcion());
            }
        }




        direccion.setText(recibido.getDireccion_entrega());
        if(recibido.getRepartidor_id().equals("no asignado")){
            repartidor.setVisibility(View.GONE);
            txt_repartidor.setVisibility(View.GONE);
        }else {
            databaseReference.child("Repartidores/"+recibido.getRepartidor_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Repartidor rer=snapshot.getValue(Repartidor.class);
                    repartidor.setText(rer.getNombre());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        fecha.setText(recibido.getFecha());
        if(recibido.getHora_entrega().equals("00:00")){
            hora.setVisibility(View.GONE);
            txt_hora.setVisibility(View.GONE);
        }else {
            hora.setText(recibido.getHora_entrega());
        }
        if(recibido.getCantidad()==1){
            cantidad.setText(recibido.getCantidad() + " Producto");
        }else {
            cantidad.setText(recibido.getCantidad() + " Productos");
        }

        costo.setText("$" + String.valueOf(recibido.getCosto() * recibido.getCantidad()));

        estado.setText(recibido.getEstado());
        if(recibido.getId_compra()==null || recibido.getId_compra().isEmpty()){
            no_seguimiento.setVisibility(View.GONE);
        }else {
            no_seguimiento.setText("No seguimiento: "+recibido.getId_compra());
        }

        Glide.with(VerPedidoActivity.this).load(recibido.getFoto()).centerCrop().override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(circleImageView);
    }

    private void inicio_views() {
        producto=(TextView)findViewById(R.id.producto_pedido_final);
        descripcion=(TextView)findViewById(R.id.descripcion_pedido_final);
        direccion=(TextView)findViewById(R.id.direccion_envio_final);
        repartidor=(TextView)findViewById(R.id.repartidor_pedido_final);
        txt_repartidor=(TextView)findViewById(R.id.txt_repartidor);
        fecha=(TextView)findViewById(R.id.fecha_pedido_final);
        hora=(TextView)findViewById(R.id.hora_pedido_final);
        txt_hora=(TextView)findViewById(R.id.txt_hora_pedido_final);
        cantidad=(TextView)findViewById(R.id.cantidad_pedido_final);
        costo=(TextView)findViewById(R.id.costo_pedido_final);
        estado=(TextView)findViewById(R.id.estado_pedido_final);
        no_seguimiento=(TextView)findViewById(R.id.no_seguimiento);
        txt_descripcion=(TextView)findViewById(R.id.txt_descripcion);
        txt_direccion=(TextView)findViewById(R.id.txt_direccion);
        circleImageView=(CircleImageView)findViewById(R.id.imageView_pedido_final);
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
    }

    public void Aceptar(View view) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}