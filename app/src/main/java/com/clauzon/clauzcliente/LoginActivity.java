package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clauzon.clauzcliente.Clases.Repartidor;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText txt_correo, txt_pass;
    private Button btn_iniciar, btn_registrar;
    private String email, password;
    private Usuario usuario;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;// ...
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseON();
        txt_correo = (EditText) findViewById(R.id.correo_login);
        txt_pass = (EditText) findViewById(R.id.pass_login);
        btn_iniciar = (Button) findViewById(R.id.iniciar_login);
        btn_registrar = (Button) findViewById(R.id.registro_login);
        Intent i = getIntent();
        usuario = (Usuario) i.getSerializableExtra("user");
        progressBar = (ProgressBar) findViewById(R.id.progress_circular_login);
        progressBar.setVisibility(View.GONE);
    }

    public void registro(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isEmailVerified()) {
                nextActivity();
            } else {
                // Toast.makeText(this, "Verifica tu correo para poder ingresar a tu cuenta", Toast.LENGTH_SHORT).show();
            }
        }
        //updateUI(currentUser);
    }

    private void nextActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("user", usuario);
        startActivity(intent);
        finish();
    }

    public void iniciar_sesion(View view) {
        progressBar.setVisibility(View.VISIBLE);
        //txt_pass.setFocusable(false);
        txt_correo.setEnabled(false);
        txt_pass.setEnabled(false);
        //txt_correo.setFocusable(false);
        email = txt_correo.getText().toString();
        password = txt_pass.getText().toString();
        if (isValidEmail(email) && validarPass()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {

                                    databaseReference.child("Repartidores").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                Repartidor repartidor = ds.getValue(Repartidor.class);
                                                if (repartidor.getCorreo().equals(txt_correo.getText().toString())) {
                                                    Toast.makeText(LoginActivity.this, "Este correo pertenece a una cuenta de Repartidor", Toast.LENGTH_SHORT).show();
                                                    FirebaseAuth.getInstance().signOut();
                                                    progressBar.setVisibility(View.GONE);
                                                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    progressBar.setVisibility(View.GONE);
                                    nextActivity();

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    // txt_pass.setFocusable(true);
                                    txt_correo.setEnabled(true);
                                    txt_pass.setEnabled(true);
                                    // txt_correo.setFocusable(true);
                                    Toast.makeText(LoginActivity.this, "Verifica tu correo para poder ingresar a tu cuenta", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                // txt_pass.setFocusable(true);
                                txt_correo.setEnabled(true);
                                txt_pass.setEnabled(true);
                                // txt_correo.setFocusable(true);
                                Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private Boolean validarPass() {
        String pass1;
        pass1 = txt_pass.getText().toString();
        if (pass1.length() > 6 && pass1.length() < 20) {
            return true;
        } else {
            return false;
        }
    }

    public void firebaseON() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }
}
