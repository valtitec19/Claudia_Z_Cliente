package com.clauzon.clauzcliente.Clases;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imagenes=new ArrayList<>();

    @Override
    public int getCount() {
        return imagenes.size();
    }

    public ImageAdapter(Context context, ArrayList<String> imagenes) {
        this.context = context;
        this.imagenes = imagenes;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView=new ImageView(context);
        Glide.with(context).load(imagenes.get(position)).centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(100)))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        container.addView(imageView,0);

        return imageView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }
}
