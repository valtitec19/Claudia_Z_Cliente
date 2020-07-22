package com.clauzon.clauzcliente.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.clauzon.clauzcliente.Clases.Producto;
import com.clauzon.clauzcliente.Interface.IItemClickListener;
import com.clauzon.clauzcliente.OrdenActivity;
import com.clauzon.clauzcliente.R;

import java.util.List;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyViewHolder> {

    private Context context;
    private List<Producto> list;

    public MyItemAdapter(Context context, List<Producto> list) {
        this.context = context;
        this.list = list;
    }

    public List<Producto> getLista() {
        return list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.costo.setText("$"+String.valueOf(list.get(position).getVenta_producto()));
        holder.nombre_item.setText(list.get(position).getNombre_producto());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(context).load(list.get(position).getFoto_producto()).transform(new CenterCrop(),new RoundedCorners(30))//.centerCrop()/*.override(800,800)*/
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.foto_item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p=position;
                Intent intent= new Intent(context, OrdenActivity.class);
                //intent.putExtra("user",usuario);
                intent.putExtra("producto",list.get(p));
                context.startActivity(intent);
//                Toast.makeText(context, "Funciona", Toast.LENGTH_SHORT).show();
            }
        });
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list!=null ? list.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView costo,nombre_item;
        private ImageView foto_item;

        private IItemClickListener iItemClickListener;

        public void setiItemClickListener(IItemClickListener iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            costo = (TextView) itemView.findViewById(R.id.costo__recycler);
            foto_item = (ImageView) itemView.findViewById(R.id.foto_recycler);
            nombre_item=(TextView)itemView.findViewById(R.id.nombre_recycler);
            nombre_item.setSelected(true);
            itemView.setOnClickListener(this);

        }

        public TextView getCosto() {
            return costo;
        }

        public void setCosto(TextView costo) {
            this.costo = costo;
        }

        public ImageView getFoto_item() {
            return foto_item;
        }

        public void setFoto_item(ImageView foto_item) {
            this.foto_item = foto_item;
        }

        public TextView getNombre_item() {
            return nombre_item;
        }

        public void setNombre_item(TextView nombre_item) {
            this.nombre_item = nombre_item;
        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onItemClickListener(view,getAdapterPosition());
        }
    }
}
