package com.clauzon.clauzcliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FormaPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma_pago);
    }

    public void add_card(View view) {
        startActivity(new Intent(FormaPagoActivity.this,PagoActivity.class));
//        Toast.makeText(this, "Solo pagos en efectivo por el momento", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Espera proximas novedades!", Toast.LENGTH_SHORT).show();

    }
}
