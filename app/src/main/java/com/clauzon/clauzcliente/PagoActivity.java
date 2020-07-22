package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.Clases.Producto;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.clauzon.clauzcliente.PagoConTarjeta.RequestHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class PagoActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;
    private CardForm cardForm;
    private Button buy;
    private AlertDialog.Builder alertBuilder;

    //stripe

    private Stripe stripe;
    String amount;
    Card card;
    String tok;

    String tarjetaNumero;
    String tarjetaFecha;
    String tarjetaCvv;

    private String id_compra,nombre;
    private int tarjeta;
    private EditText nombre_pago;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);
        firebaseON();
        nombre_pago=(EditText)findViewById(R.id.nombre_pago);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        amount=(String) b.get("amount");
//        progressBar=(ProgressBar)findViewById(R.id.progressBarPago);
//        progressBar.setVisibility(View.GONE);
        Log.e("Primer recibido " , amount );
        //INSTANCIAMOS STRIPE PARA PODER LLARMARLO EN CUALQUIER PARTE DEL CODIGO
        try {
            stripe = new Stripe("pk_test_ztBjqcCSpW7gFjowluJbG9xl001xruX4Jm");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .setup(PagoActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buy.setClickable(false);
                if(cardForm.isValid() && !nombre_pago.getText().toString().isEmpty()){
                    OnClickProcesarPago();
                }else {
                    Toast.makeText(PagoActivity.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void OnClickProcesarPago() {
//        progressBar.setVisibility(View.VISIBLE);
        //OBTENEMOS LOS VALORES DE LA TARJETA
        if (cardForm.isValid()) {
            tarjetaNumero = cardForm.getCardNumber().toString();
            tarjetaFecha = cardForm.getExpirationDateEditText().getText().toString();
            tarjetaCvv = cardForm.getCvv().toString();
            String temp=String.valueOf(tarjetaNumero);
            String num=temp.substring(12,16);
            tarjeta=Integer.parseInt(num);

        } else {
            Toast.makeText(PagoActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
        }


//        //VALIDAMOS LOS CAMPOS DE TEXTO
//        if (tarjetaNumero.equals("") || tarjetaNumero.length() < 16) {
//            Toast.makeText(PagoActivity.this, "Ingresa un numero valido", Toast.LENGTH_SHORT).show();
//        } else if (tarjetaFecha.equals("")) {
//            Toast.makeText(PagoActivity.this, "Ingresa una fecha valida", Toast.LENGTH_SHORT).show();
//        } else if (tarjetaCvv.equals("") || tarjetaCvv.length() < 3) {
//            Toast.makeText(PagoActivity.this, "Ingresa un cvv valido", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(PagoActivity.this, "Tarjeta Agregada", Toast.LENGTH_SHORT).show();

        //SEPARAMOS EL MES Y LA FECHA
        Log.e("Fecha", tarjetaFecha.toString() );
        String string1 = tarjetaFecha.substring(0,2);
        String string2=tarjetaFecha.substring(2,6);
        Log.e("Mes: ", string1 );
        Log.e("Año: ", string2 );
        //CREAMOS NUESTRA TARJETA CON -> NUMERO DE TARJETA | MES | AÑO | CVV DE LA TARJETA
        card = new Card(
                tarjetaNumero,
                Integer.parseInt(string1),
                Integer.parseInt(string2),
                tarjetaCvv
        );

        //LE ASIGNAMOS LA MONEDA DE LA TARJETA
        card.setCurrency("mxn");

        //LE ASIGNAMOS EL NOMBRE DEL TITULAR DE LA TARJETA -> SE RECOMIENDA PEDIRLO EN UNA CAJA DE TEXTO
        card.setName(nombre_pago.getText().toString());

        //CREAMOS EL TOKEN DE STRIPE EN DONDE SE LE PASA LA TARJETA Y LA KEY PUBLICA
        stripe.createToken(card, "pk_test_ztBjqcCSpW7gFjowluJbG9xl001xruX4Jm", new TokenCallback() {

            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge

                //Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();

                //OBTENEMOS EL ID
                tok = token.getId();


                //OBTENEMOS EL MONTO EN TEXTO
                //amount = "120.00";

                //LO CONVERTIMOS EN DOUBLE PARA PODER MULTIPLICARLO POR 100
                double previo = Double.parseDouble(amount) * 100; //--> ESTO SE HACE PORQUE ESTAMOS USANDO PESOS MEXICANOS SI FUERAN EUROS SERIAN 1000 ETC ETC

                int entero = (int) previo;

                int inte = Integer.parseInt(String.valueOf(entero));

                //AGREGAMOS LA DESCRIPCION DEL PAGO
                String descripcion = "Productos Claudia Z Online News";

                //NUEVAMENTE LA MONEDA EN COMO SE COBRA EL EFECTIVO
                String moneda = "mxn";

                //LLAMAMOS PROCESAR PAGO Y LE PASAMOS LA --> DESCRIPCION - TOKEN QUE REALIZAMOS - MONTO Y MONEDA (CURRENCY)
                procesarPago(descripcion, tok, "" + inte, moneda);
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }


    public void procesarPago(String _description, String _token, String _amount, String moneda) {

        //SOLO LLAMAMOS A NUESTRA CLASE PAGO QUE ES DONDE NOS COMUNICAREMOS CON NUESTRO WEBSERVICE
        Pago pago = new Pago(_amount, _token, _description, moneda);
        pago.execute();

    }


    class Pago extends AsyncTask<Void, Void, String> {

        //DECLARAMOS LAS VARIABLES DE TRABAJO
        String _amount;
        String _token;
        String _description;
        String _currency;

        //DECLARAMOS EL CONSTRUCTOR PARA ASIGNAR A LAS VARIABLES
        public Pago(String _amount, String _token, String _description, String _currency) {
            this._amount = _amount;
            this._token = _token;
            this._description = _description;
            this._currency = _currency;
        }

        //CREAMOS UN PROGRESSDIALOG PARA EL USUARIO
        ProgressDialog pdLoading = new ProgressDialog(PagoActivity.this);

        //LO MOSTRAMOS ANTES DE EJECUCION Y DURANTE
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tProcesando Pago...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }


        //MIENTRAS ESTO OCURRE NOSOTROS OBTENEMOS LAS VARIABLES Y LAS MAPEAMOS CON AYUDA DE NUESTRO REQUEST HANDLER (CLASE)
        //Y LLAMAMOS AL METODO SENDPOSTREQUEST CON LOS PARAMETROS DE FUNCIONAMIENTO
        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("Content-Type", "application/json");
            params.put("method", "charge");
            params.put("amount", _amount);
            params.put("currency", _currency);
            params.put("source", _token);
            params.put("description", _description);

            //returing the response
            return requestHandler.sendPostRequest("https://claudiazon.com/TutoStripe/charge.php", params);
        }

        //CUANDO NOS RETORNA INFORMACION
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //OCULTAMOS EL PROGRESSDIALOG
            pdLoading.dismiss();
            buy.setClickable(true);
//            progressBar.setVisibility(View.GONE);
            try {
                //respuesta de conversión a objeto json
                JSONObject obj = new JSONObject(s);
                //RECORDEMOS QUE EN NUESTRO WEBSERVICES DEVOLVEMOS TRUE/FALSE EN ERROR SI ERROR ES FALO SIGNIFICA QUE EL PAGO SE PROCESO Y
                //MOSTRAMOS LA RESPUESTA EN UN TOAST
                if (!obj.getBoolean("error")) {
                    id_compra = UUID.randomUUID().toString();
//                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    final String mensaje=obj.getString("message").toString();
                    if(mensaje.equals("Pago Realizado")){

                        databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    final Pedidos pedidos = snapshot.getValue(Pedidos.class);
                                    if (pedidos.getUsuario_id().equals(currentUser.getUid()) && pedidos.getEstado().equals("Carrito")) {
                                        pedidos.setEstado(mensaje);
                                        pedidos.setId_compra(id_compra);
                                        pedidos.setTarjeta(tarjeta);
                                        final DatabaseReference databaseReference2 = database.getReference();
                                        databaseReference2.child("Pedidos/" + pedidos.getId()).setValue(pedidos);

                                        /*MENSAJE FINAL*/

                                        databaseReference.child("Catalogo Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                    final Producto producto=ds.getValue(Producto.class);
                                                    if(producto.getId_producto().equals(pedidos.getProducto_id())){
                                                        databaseReference2.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                                                                String hora=pedidos.getHora_entrega();
                                                                if(hora.equals("00:00")){
                                                                    hora="";
                                                                }
                                                                String mensaje=producto.getNombre_producto()+", "+pedidos.getDireccion_entrega()+", "+pedidos.getFecha()+", "+hora+"\n"
                                                                        +"Telefono de contacto: "+usuario.getTelefono()+"\n"+"Costo: "+((pedidos.getCantidad()*pedidos.getCosto())+pedidos.getCosto_envio())+", "+pedidos.getCantidad()+" Piezas";

                                                                AlertDialog.Builder builder = new AlertDialog.Builder(PagoActivity.this);
                                                                builder.setTitle("PEDIDO REALIZADO (PAGADO)");
                                                                builder.setMessage(mensaje);
                                                                builder.setCancelable(false);
                                                                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        startActivity(new Intent(PagoActivity.this, MainActivity.class));
                                                                        finish();
                                                                    }
                                                                });
                                                                builder.create().show();
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


                                        databaseReference2.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                                                usuario.setMultas(0);
                                                DatabaseReference ref=database.getReference();
                                                ref.child("Usuarios/"+currentUser.getUid()).setValue(usuario);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
//                                        startActivity(new Intent(PagoActivity.this,MainActivity.class));
//                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }else{

                    //SI EL PAGO NO SE EFECTUO TAMBIEN MOSTRAMOS EL ERROR
                    //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();




                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Exception: " + e, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

}
