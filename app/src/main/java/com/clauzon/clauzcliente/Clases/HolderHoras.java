package com.clauzon.clauzcliente.Clases;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.R;

public class HolderHoras extends RecyclerView.ViewHolder {
    private TextView hora_r;
    public HolderHoras(@NonNull View itemView) {
        super(itemView);
        hora_r=(TextView)itemView.findViewById(R.id.hora_recycler_hora);
    }

    public TextView getHora_r() {
        return hora_r;
    }

    public void setHora_r(TextView hora_r) {
        this.hora_r = hora_r;
    }
}
