package com.clauzon.clauzcliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class LineasActivity extends AppCompatActivity {

    private Button button;
    private CircleImageView imagen1,imagen2,imagen3,imagen4,imagen5,imagen6,imagen7,imagen8,imagen9,imagen12,imagenA,imagenB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineas);

        inicio_imagenes();
    }

    public void l1(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 1");
        startActivity(intent);
    }
    public void l2(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 2");
        startActivity(intent);
    }
    public void l3(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 3");
        startActivity(intent);
    }
    public void l4(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 4");
        startActivity(intent);
    }
    public void l5(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 5");
        startActivity(intent);
    }
    public void l6(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 6");
        startActivity(intent);
    }
    public void l7(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 7");
        startActivity(intent);
    }
    public void l8(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 8");
        startActivity(intent);
    }
    public void l9(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 9");
        startActivity(intent);
    }
    public void l12(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea 12");
        startActivity(intent);
    }
    public void lA(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea A");
        startActivity(intent);
    }
    public void lB(View view) {
        Intent intent=new Intent(LineasActivity.this,EstacionesActivity.class);
        intent.putExtra("linea","Linea B");
        startActivity(intent);
    }

    public void inicio_imagenes(){
        imagen1=(CircleImageView)findViewById(R.id.image1);
        imagen2=(CircleImageView)findViewById(R.id.image2);
        imagen3=(CircleImageView)findViewById(R.id.image3);
        imagen4=(CircleImageView)findViewById(R.id.image4);
        imagen5=(CircleImageView)findViewById(R.id.image5);
        imagen6=(CircleImageView)findViewById(R.id.image6);
        imagen7=(CircleImageView)findViewById(R.id.image7);
        imagen8=(CircleImageView)findViewById(R.id.image8);
        imagen9=(CircleImageView)findViewById(R.id.image9);
        imagen12=(CircleImageView)findViewById(R.id.image12);
        imagenA=(CircleImageView)findViewById(R.id.imageA);
        imagenB=(CircleImageView)findViewById(R.id.imageB);

        Glide.with(LineasActivity.this).load(getImage("linea1")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen1);
        Glide.with(LineasActivity.this).load(getImage("linea2")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen2);
        Glide.with(LineasActivity.this).load(getImage("linea3")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen3);
        Glide.with(LineasActivity.this).load(getImage("linea4")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen4);
        Glide.with(LineasActivity.this).load(getImage("linea5")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen5);
        Glide.with(LineasActivity.this).load(getImage("linea6")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen6);
        Glide.with(LineasActivity.this).load(getImage("linea7")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen7);
        Glide.with(LineasActivity.this).load(getImage("linea8")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen8);
        Glide.with(LineasActivity.this).load(getImage("linea9")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen9);
        Glide.with(LineasActivity.this).load(getImage("linea12")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen12);
        Glide.with(LineasActivity.this).load(getImage("lineaa")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagenA);
        Glide.with(LineasActivity.this).load(getImage("lineab")).centerCrop().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagenB);



    }
    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

}
