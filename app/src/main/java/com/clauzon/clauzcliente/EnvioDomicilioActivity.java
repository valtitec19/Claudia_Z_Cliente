package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EnvioDomicilioActivity extends AppCompatActivity {
    private EditText txt1, txt2, txt3, txt4, txt5, txt6, txt7;
    private Button aceptar;
    private String nombre, direccion1, direccion2, cpp, colonia, delegacion, ciudad, full_direccion;
    private String id_pedido = "";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Usuario usuario;
    private Pedidos pedidos;
    private String id_user, ciudad_seleccionada;
    private List<Pedidos> lista_pedidos = new ArrayList<>();
    private Spinner spinner, spinner_ciudades;
    private String costo_envio;
    int costo;
    Calendar calendar;
    String fecha;
    Date mañana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_domicilio);
        inicia_views();
        firebaseON();
        id_user = currentUser.getUid();
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        String languageToLoad = "Spanish"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        calendar = Calendar.getInstance(locale);

    }

    public void inicia_views() {
        txt1 = (EditText) findViewById(R.id.full_name);
        txt2 = (EditText) findViewById(R.id.direccion1);
        txt3 = (EditText) findViewById(R.id.direccion2);
        txt4 = (EditText) findViewById(R.id.cpp);
        txt5 = (EditText) findViewById(R.id.colonia);
        txt6 = (EditText) findViewById(R.id.delegacion);
//        txt7 = (EditText) findViewById(R.id.ciudad);
        aceptar = (Button) findViewById(R.id.btnAceptarDireccion);
        spinner_ciudades = (Spinner) findViewById(R.id.spinner_ciudades);
        ArrayAdapter<CharSequence> ciudades = ArrayAdapter.createFromResource(this, R.array.Ciudades, android.R.layout.simple_spinner_item);
        spinner_ciudades.setAdapter(ciudades);
        spinner_ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ciudad_seleccionada = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner = (Spinner) findViewById(R.id.spinner_tiempo_envio);
        ArrayAdapter<CharSequence> costos_envio = ArrayAdapter.createFromResource(this, R.array.costos_envio, android.R.layout.simple_spinner_item);
        spinner.setAdapter(costos_envio);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                costo_envio = adapterView.getItemAtPosition(i).toString();
                if (i == 0) {
                    calendar.add(Calendar.DATE, 1);
                    //fecha_final=(Date)DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
                    mañana = calendar.getTime();
                    fecha = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
                    costo = 150;
                } else if (i == 1) {
                    costo = 120;
                    calendar.add(Calendar.DATE, 2);
                    //fecha_final=(Date)DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
                    mañana = calendar.getTime();
                    fecha = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
                } else {
                    costo = 250;
                    calendar.add(Calendar.DATE, 3);
                    //fecha_final=(Date)DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
                    mañana = calendar.getTime();
                    fecha = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void recupera_datos() {
        nombre = txt1.getText().toString();
        direccion1 = txt2.getText().toString();
        direccion2 = txt3.getText().toString();
        cpp = txt4.getText().toString();
        colonia = txt5.getText().toString();
        delegacion = txt6.getText().toString();
//        ciudad = txt7.getText().toString();
    }


    public void firebaseON() {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void update() {
        recupera_datos();
        if (!nombre.isEmpty() && !direccion1.isEmpty() && !cpp.isEmpty() && !colonia.isEmpty() && !delegacion.isEmpty()) {
            if(!ciudad_seleccionada.equals("CDMX") && costo==150){
                AlertDialog.Builder builder = new AlertDialog.Builder(EnvioDomicilioActivity.this);
                builder.create();
                builder.setTitle("Opción de envío no valida");
                builder.setMessage("El envío al día siguiente solo está disponible en la CDMX");
                builder.setCancelable(false);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EnvioDomicilioActivity.this, "Modifica el la opción de envío", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }else {
                full_direccion = nombre + ", " + direccion1 + " " + direccion2 + " " + cpp + ", " + delegacion + ", " + ciudad_seleccionada;
                AlertDialog.Builder builder = new AlertDialog.Builder(EnvioDomicilioActivity.this);
                builder.create();
                builder.setTitle("Confirmar direccion de envio");
                builder.setMessage(full_direccion);
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final DatabaseReference databaseReference2 = database.getReference("Usuarios/" + currentUser.getUid());
                        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                usuario = dataSnapshot.getValue(Usuario.class);
                                usuario.setDireccion_envio(full_direccion);
                                databaseReference2.setValue(usuario);
                                final DatabaseReference databaseReference1 = database.getReference("Pedidos");
                                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            Pedidos pedidos = ds.getValue(Pedidos.class);
                                            if (pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")) {
                                                pedidos.setDireccion_entrega(full_direccion);
                                                pedidos.setCosto_envio(costo);
                                                pedidos.setFecha(fecha);
                                                databaseReference1.child(pedidos.getId()).setValue(pedidos);
                                            }

                                        }
                                        startActivity(new Intent(EnvioDomicilioActivity.this, CarritoActivity.class));
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
                builder.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }



        } else {
            Toast.makeText(EnvioDomicilioActivity.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
        }

    }
}
