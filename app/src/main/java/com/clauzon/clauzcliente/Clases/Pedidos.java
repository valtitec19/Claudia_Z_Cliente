package com.clauzon.clauzcliente.Clases;

import java.io.Serializable;

public class Pedidos implements Serializable {
    private String producto_id;
    private String estado;
    private int cantidad;
    private float costo;
    private String usuario_id;
    private String repartidor_id;
    private String direccion_entrega;
    private String hora_entrega,nombre,foto,descripcion,id;
    private float costo_envio;
    private String fecha;
    private String id_compra;
    private int tarjeta;
    private String color,tamano,modelo;

    public Pedidos() {
    }

//    public Pedidos(String producto_id, String estado, int cantidad, float costo, String usuario_id, String repartidor_id, String direccion_entrega, String hora_entrega, String nombre, String foto, String descripcion, String id, float costo_envio, String fecha, String id_compra, int tarjeta) {
//        this.producto_id = producto_id;
//        this.estado = estado;
//        this.cantidad = cantidad;
//        this.costo = costo;
//        this.usuario_id = usuario_id;
//        this.repartidor_id = repartidor_id;
//        this.direccion_entrega = direccion_entrega;
//        this.hora_entrega = hora_entrega;
//        this.nombre = nombre;
//        this.foto = foto;
//        this.descripcion = descripcion;
//        this.id = id;
//        this.costo_envio = costo_envio;
//        this.fecha = fecha;
//        this.id_compra = id_compra;
//        this.tarjeta = tarjeta;
//    }

    public Pedidos(String producto_id, String estado, int cantidad, float costo, String usuario_id, String repartidor_id, String direccion_entrega, String hora_entrega, String nombre, String foto, String descripcion, String id, float costo_envio, String fecha, String id_compra, int tarjeta, String color, String tamano, String modelo) {
        this.producto_id = producto_id;
        this.estado = estado;
        this.cantidad = cantidad;
        this.costo = costo;
        this.usuario_id = usuario_id;
        this.repartidor_id = repartidor_id;
        this.direccion_entrega = direccion_entrega;
        this.hora_entrega = hora_entrega;
        this.nombre = nombre;
        this.foto = foto;
        this.descripcion = descripcion;
        this.id = id;
        this.costo_envio = costo_envio;
        this.fecha = fecha;
        this.id_compra = id_compra;
        this.tarjeta = tarjeta;
        this.color = color;
        this.tamano = tamano;
        this.modelo = modelo;
    }



    public String getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(String producto_id) {
        this.producto_id = producto_id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getRepartidor_id() {
        return repartidor_id;
    }

    public void setRepartidor_id(String repartidor_id) {
        this.repartidor_id = repartidor_id;
    }

    public String getDireccion_entrega() {
        return direccion_entrega;
    }

    public void setDireccion_entrega(String direccion_entrega) {
        this.direccion_entrega = direccion_entrega;
    }

    public String getHora_entrega() {
        return hora_entrega;
    }

    public void setHora_entrega(String hora_entrega) {
        this.hora_entrega = hora_entrega;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getCosto_envio() {
        return costo_envio;
    }

    public void setCosto_envio(float costo_envio) {
        this.costo_envio = costo_envio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId_compra() {
        return id_compra;
    }

    public void setId_compra(String id_compra) {
        this.id_compra = id_compra;
    }

    public int getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(int tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
