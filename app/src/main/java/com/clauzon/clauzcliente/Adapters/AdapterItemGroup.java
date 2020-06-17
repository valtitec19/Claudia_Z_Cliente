package com.clauzon.clauzcliente.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.Clases.Category;
import com.clauzon.clauzcliente.Clases.Producto;
import com.clauzon.clauzcliente.OrdenActivity;
import com.clauzon.clauzcliente.R;

import java.util.List;

public class AdapterItemGroup extends RecyclerView.Adapter<AdapterItemGroup.MyViewHolder> {

    private Context context;
    private List<Category> list;

    public AdapterItemGroup(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }
    public List<Category> getLista() {
        return list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_group,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        List<Producto> itemData=list.get(position).getList();
        final MyItemAdapter myItemAdapter = new MyItemAdapter(context,itemData);
        holder.recyclerView_item.setHasFixedSize(true);
        holder.recyclerView_item.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView_item.setAdapter(myItemAdapter);
        holder.recyclerView_item.setNestedScrollingEnabled(false);
        holder.recyclerView_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Probando", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, OrdenActivity.class);
                //intent.putExtra("user",usuario);
                intent.putExtra("producto",myItemAdapter.getLista().get(holder.recyclerView_item.getChildAdapterPosition(view)));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (list!=null ? list.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        RecyclerView recyclerView_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name_recycler_vertical);
            recyclerView_item=(RecyclerView)itemView.findViewById(R.id.recycler_view_list);
        }
    }
}
