package com.clauzon.clauzcliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.clauzon.clauzcliente.Clases.Producto;

public class GarantiasActivity extends AppCompatActivity {
    private Producto p_recibido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garantias);
        Intent i = getIntent();
        p_recibido = (Producto) i.getSerializableExtra("producto");
    }

    public void garantias_back(View view) {
        Intent intent=new Intent(GarantiasActivity.this,OrdenActivity.class);
        intent.putExtra("producto",p_recibido);
        startActivity(intent);
        finish();
    }
}
