package com.clauzon.clauzcliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AvisoPrivacidadActivity extends AppCompatActivity {

    String activity="registro";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso_privacidad);
        Intent intent=getIntent();
        activity=intent.getStringExtra("context");


    }

    public void regresar_registro(View view) {
        if(activity.equals("perfil")){
            startActivity(new Intent(AvisoPrivacidadActivity.this,EditarPerfilActivity.class));
            finish();

        }else if(activity.equals("registro")){
            startActivity(new Intent(AvisoPrivacidadActivity.this,RegistroActivity.class));
            finish();
        }

    }


}
