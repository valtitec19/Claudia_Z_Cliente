package com.clauzon.clauzcliente.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzcliente.CarritoActivity;
import com.clauzon.clauzcliente.Clases.Pedidos;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.clauzon.clauzcliente.EditarPerfilActivity;
import com.clauzon.clauzcliente.FavActivity;
import com.clauzon.clauzcliente.FormaPagoActivity;
import com.clauzon.clauzcliente.LoginActivity;
import com.clauzon.clauzcliente.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    private Button boton_config,btn_carrito,btn_forma_pago,btn_fav;
    private Button boton,unete;
    private RecyclerView recyclerView;
    private CircleImageView foto;
    private TextView txt_nombre;
    private String nombre,url_foto;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private NotificationsViewModel notificationsViewModel;
    private CircleImageView foto_logo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        currentUser=mAuth.getCurrentUser();
        databaseReference=database.getReference();

        foto_logo=(CircleImageView)root.findViewById(R.id.imagen_claudia_logo);
        boton_config=(Button)root.findViewById(R.id.editar_perfil);
        cargar_datos();
        btn_forma_pago=(Button)root.findViewById(R.id.formas_de_pago);
        btn_carrito=(Button)root.findViewById(R.id.btn_carrito_perfil);
        btn_forma_pago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FormaPagoActivity.class));
            }
        });
        boton_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });
        foto=(CircleImageView)root.findViewById(R.id.foto_user);
        txt_nombre=(TextView)root.findViewById(R.id.user_name);
        btn_fav=(Button)root.findViewById(R.id.fav_btn);
        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuario usuario=dataSnapshot.getValue(Usuario.class);
                        if(usuario.getFavoritos().size()==0){
                            Toast.makeText(getContext(), "No tienes productos favoritos", Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(new Intent(getActivity(), FavActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Usuarios/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Usuario usuario=dataSnapshot.getValue(Usuario.class);
                        if(usuario.getPedidos().size()==0){
                            Toast.makeText(getContext(), "No tienes pedidos en tu carrito", Toast.LENGTH_SHORT).show();
                        }else{
                            databaseReference.child("Pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        Pedidos pedidos=ds.getValue(Pedidos.class);
                                        if(pedidos.getEstado().equals("Carrito") && pedidos.getUsuario_id().equals(usuario.getId())){
                                            startActivity(new Intent(getActivity(),CarritoActivity.class));
                                        }else {
                                            Toast.makeText(getContext(), "No tienes pedidos en tu carrito", Toast.LENGTH_SHORT).show();
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

            }
        });
        boton=(Button)root.findViewById(R.id.boton);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                borrar_token_dispositivo();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        unete= (Button) root.findViewById(R.id.unete_a_nosotros);
        unete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.clauzon.clauzentregas"));
                startActivity(i);
            }
        });
        return root;
    }

    private void borrar_token_dispositivo() {

        if(currentUser!=null && currentUser.isEmailVerified()){
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Usuarios/"+currentUser.getUid()).child("token");
                            reference.removeValue();

                        }
                    });
        }
    }

    public void cargar_datos(){
        Glide.with(getActivity()).load("https://firebasestorage.googleapis.com/v0/b/clauzon.appspot.com/o/RECURSOS%2FCZON-LogoApp-01.jpg?alt=media&token=95aec94d-854e-42bd-b7c3-d7411656b32b").centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(foto_logo);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            DatabaseReference referenceRepartidores= database.getReference("Usuarios/"+currentUser.getUid());
            referenceRepartidores.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario =dataSnapshot.getValue(Usuario.class);

                    try {
                        nombre=usuario.getNombre()+" "+usuario.getApellidos();
                        url_foto=usuario.getFoto();
                        txt_nombre.setText(nombre);
                        Glide.with(getActivity()).load(url_foto).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(foto);
                    }catch (Exception e){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            startActivity(new Intent(getActivity(),LoginActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cargar_datos();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargar_datos();

    }
}