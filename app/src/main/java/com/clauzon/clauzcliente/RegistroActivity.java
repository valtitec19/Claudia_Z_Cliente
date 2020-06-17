package com.clauzon.clauzcliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.clauzcliente.Clases.Usuario;
import com.clauzon.clauzcliente.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private String url_foto = "";
    private Boolean val1=false,val2=false;
    private EditText txt_nombre, txt_apellido, txt_celular, txt_correo, txt_pass, txt_pass_confirm;
    private Spinner s_dias,s_meses,s_años,s_genero;
    private Button registrar;
    private CircleImageView circleImageView;
    private CheckBox checkBox;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private Usuario usuario;
    private String email, password,nombre,apellido,telefono,d,m,a,fecha,genero,id;
    private String direccion_envio="no definido";
    private List<String> pedidos= new ArrayList<>();
    private List<String> favoritos= new ArrayList<>();
    private ProgressBar progressBar;
    private int multas=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        requestMultiplePermissions();
        //Pedidos p=new Pedidos("hiuahiad","basd");
        //pedidos.add(p);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_registro);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        //Inicio_Spinners();
        checkBox=(CheckBox)findViewById(R.id.terminos_y_condiciones_registro);
        txt_nombre = (EditText) findViewById(R.id.nombre_registro);
        txt_apellido = (EditText) findViewById(R.id.apellido_registro);
        txt_celular = (EditText) findViewById(R.id.telefono_registro);
        txt_correo = (EditText) findViewById(R.id.correo_registro);
        txt_pass = (EditText) findViewById(R.id.contraseña_registro);
        txt_pass_confirm = (EditText) findViewById(R.id.contraseña_registro_confirmada);
        progressDialog = new ProgressDialog(this);
        circleImageView=(CircleImageView)findViewById(R.id.foto_registro);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
        registrar=(Button)findViewById(R.id.enviar_registro);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                telefono=txt_celular.getText().toString();
                apellido=txt_apellido.getText().toString();
                nombre=txt_nombre.getText().toString();
                email=txt_correo.getText().toString();
                //fecha=d+"/"+m+"/"+a;
                if(isValidEmail(email)){
                    if(validarPass()){
                        if(!nombre.isEmpty()&&!apellido.isEmpty()&&valida_numero(telefono)){
                            if(checkBox.isChecked()){
                                if (url_foto.isEmpty()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                                    builder.setTitle("Datos incompletos").setMessage("Por favor cargue una foto de perfil");
                                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(RegistroActivity.this, "No se creo el producto", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    builder.create().show();
                                } else {
                                    password=txt_pass_confirm.getText().toString();
                                    progressBar.setVisibility(View.VISIBLE);
                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(RegistroActivity.this, "Hemos enviado un correo de verificacion a tu banjeda de entrada", Toast.LENGTH_SHORT).show();
                                                                    progressBar.setVisibility(View.GONE);
                                                                    FirebaseUser currenUser=mAuth.getCurrentUser();
                                                                    id=currenUser.getUid();
                                                                    usuario= new Usuario(nombre,apellido,email,telefono,id,"","",url_foto,pedidos,favoritos,direccion_envio,multas);
                                                                    DatabaseReference referenceRepartidores= database.getReference("Usuarios/"+id);
                                                                    referenceRepartidores.setValue(usuario);
                                                                    Intent intent=getIntent();//new Intent(RegistroActivity.this, LoginActivity.class);
                                                                    intent.putExtra("user",usuario);
                                                                    startActivity(intent);
                                                                    nextActivity();
                                                                }
                                                            }
                                                        });

                                                    } else {
                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(RegistroActivity.this, "Error de registro", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }else{
                                Toast.makeText(RegistroActivity.this, "Debes aceptar los Terminos y Condiciones", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(RegistroActivity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegistroActivity.this, "Las contraseñas deben ser identicas", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegistroActivity.this, "correo electronico no valido", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private Boolean validarPass(){
        String pass1,pass2;
        pass1=txt_pass.getText().toString();
        pass2=txt_pass_confirm.getText().toString();
        if(pass1.equals(pass2)){
            if(pass1.length()>6 && pass1.length()<20){
                return true;
            }else {
                Toast.makeText(this, "La contraseña debe tener entre 7 y 20 caracteres", Toast.LENGTH_SHORT).show();
                return false;

            }
        }else {return false;}
    }

//    public void Inicio_Spinners(){
//        s_dias=(Spinner)findViewById(R.id.spinner_dia_registro);
//        s_meses=(Spinner)findViewById(R.id.spinner_mes_registro);
//        s_años=(Spinner)findViewById(R.id.spinner_año_registro);
//        s_genero=(Spinner)findViewById(R.id.spinner_genero_registro);
//        String[] generos={"Hombre","Mujer"};
//        final ArrayAdapter<String> adapter_spinner_genero = new ArrayAdapter<String>(this,R.layout.spinner,generos);
//        ArrayList<String> años=new ArrayList<>();
//        for(int i=1970;i<2001;i++){
//            String año=String.valueOf(i);
//            años.add(año);
//        }
//        final ArrayAdapter<String> adapter_años = new ArrayAdapter<String>(this,R.layout.spinner,años);
//        ArrayAdapter<CharSequence> dias=ArrayAdapter.createFromResource(this,R.array.dias,android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> meses=ArrayAdapter.createFromResource(this,R.array.meses,android.R.layout.simple_spinner_item);
//        s_dias.setAdapter(dias);
//        s_meses.setAdapter(meses);
//        s_años.setAdapter(adapter_años);
//        s_genero.setAdapter(adapter_spinner_genero);
//
//        s_dias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                d=adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        s_meses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                m=adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        s_años.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                a=adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        s_genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                genero=adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

    public Boolean valida_numero(String numero){
        if(numero.length()==10 ){
            return true;
        }else {return false;}
    }

    public void nextActivity(){
        Intent intent=new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //////////////////////*******************/////////////////////*****************
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Cargar Imagen de perfil");
        pictureDialog.setCancelable(false);
        String[] pictureDialogItems = {
                "Subir foto desde galeria",
                "Tomar nueva foto" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                progressDialog.setTitle("Subiendo foto");
                progressDialog.setMessage("Por favor espere");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Uri contentURI = data.getData();
                storageReference = firebaseStorage.getReference("IMAGENES CATALOGO PRODUCTOS");
                final StorageReference foto_subida = storageReference.child(contentURI.getLastPathSegment());
                foto_subida.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                url_foto = uri.toString();
                                progressDialog.dismiss();
                                Glide.with(RegistroActivity.this).load(url_foto).centerCrop().override(500,500).diskCacheStrategy(DiskCacheStrategy.ALL).into(circleImageView);
                                Toast.makeText(RegistroActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
//                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    imageButton.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            progressDialog.setTitle("Subiendo foto de perfil");
            progressDialog.setMessage("Por favor espere");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);
            Uri uri= getImageUri(this,thumbnail);
            storageReference = firebaseStorage.getReference("IMAGENES CATALOGO PRODUCTOS");
            final StorageReference foto_subida = storageReference.child(uri.getLastPathSegment());
            foto_subida.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            url_foto = uri.toString();
                            progressDialog.dismiss();
                            Glide.with(RegistroActivity.this).load(url_foto).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(circleImageView);
                            Toast.makeText(RegistroActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });


            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegistroActivity.this,LoginActivity.class));
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void aviso_privacidad(View view) {
        startActivity(new Intent(RegistroActivity.this,AvisoPrivacidadActivity.class)
        .putExtra("context","registro"));
    }
}
