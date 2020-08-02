package com.clauzon.clauzcliente.Clases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.clauzcliente.R;

public class HolderPedidos extends RecyclerView.ViewHolder {
    private TextView productos_pedido,lugar,fecha,estado,costo;
    private ImageView foto,cancelar;
    private TextView cliente;
    public HolderPedidos(@NonNull View itemView) {
        super(itemView);
        productos_pedido=(TextView)itemView.findViewById(R.id.productos_recycler_pedido);
        lugar=(TextView)itemView.findViewById(R.id.lugar_recycler_pedido);
        fecha=(TextView)itemView.findViewById(R.id.fecha_recycler_pedido);
        estado=(TextView)itemView.findViewById(R.id.estado_recycler_pedido);
        costo=(TextView)itemView.findViewById(R.id.costo_recycler_pedido);
        foto=(ImageView)itemView.findViewById(R.id.foto_recycler_pedido);
        cancelar=(ImageView)itemView.findViewById(R.id.cancelar);
        cliente=(TextView)itemView.findViewById(R.id.cliente_recycler_pedido);
        productos_pedido.setSelected(true);
        lugar.setSelected(true);
        fecha.setSelected(true);
        estado.setSelected(true);
        costo.setSelected(true);
        cliente.setSelected(true);

    }

    public TextView getProductos_pedido() {
        return productos_pedido;
    }

    public void setProductos_pedido(TextView productos_pedido) {
        this.productos_pedido = productos_pedido;
    }

    public TextView getLugar() {
        return lugar;
    }

    public void setLugar(TextView lugar) {
        this.lugar = lugar;
    }

    public TextView getFecha() {
        return fecha;
    }

    public void setFecha(TextView fecha) {
        this.fecha = fecha;
    }

    public TextView getEstado() {
        return estado;
    }

    public void setEstado(TextView estado) {
        this.estado = estado;
    }

    public TextView getCosto() {
        return costo;
    }

    public void setCosto(TextView costo) {
        this.costo = costo;
    }

    public ImageView getFoto() {
        return foto;
    }

    public void setFoto(ImageView foto) {
        this.foto = foto;
    }

    public ImageView getCancelar() {
        return cancelar;
    }

    public void setCancelar(ImageView cancelar) {
        this.cancelar = cancelar;
    }

    public TextView getCliente() {
        return cliente;
    }

    public void setCliente(TextView cliente) {
        this.cliente = cliente;
    }
}
