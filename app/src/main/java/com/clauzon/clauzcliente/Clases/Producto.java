package com.clauzon.clauzcliente.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Producto implements Serializable {
    private String nombre_producto, descripcion, id_producto, foto_producto, estado_producto, categoria;
    private boolean estado = false;
    private float venta_producto,oferta;
    private int cantidad_producto;
    private ArrayList<String> imagenes = new ArrayList<>();
    private ArrayList<String> colores = new ArrayList<>();
    private ArrayList<String> tamanos = new ArrayList<>();
    private ArrayList<String> modelos = new ArrayList<>();
    private String color_seleccionado,tamano_seleccionado,modelo_seleccionado;

    public Producto() {
    }

    public Producto(String nombre_producto, String descripcion, String id_producto, String foto_producto, boolean estado, float venta_producto, int cantidad_producto, String estado_producto, String categoria, ArrayList<String> imagenes) {
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.id_producto = id_producto;
        this.foto_producto = foto_producto;
        this.estado = estado;
        this.venta_producto = venta_producto;
        this.cantidad_producto = cantidad_producto;
        this.estado_producto = estado_producto;
        this.categoria = categoria;
        this.imagenes = imagenes;
    }

    public Producto(String nombre_producto, String descripcion, String id_producto, String foto_producto, boolean estado, float venta_producto,float oferta, int cantidad_producto, String estado_producto, String categoria, ArrayList<String> imagenes, ArrayList<String> colores, ArrayList<String> tamaños, ArrayList<String> modelos) {
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.id_producto = id_producto;
        this.foto_producto = foto_producto;
        this.estado = estado;
        this.venta_producto = venta_producto;
        this.oferta=oferta;
        this.cantidad_producto = cantidad_producto;
        this.estado_producto = estado_producto;
        this.categoria = categoria;
        this.imagenes = imagenes;
        this.colores = colores;
        this.tamanos = tamaños;
        this.modelos = modelos;
    }


    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getFoto_producto() {
        return foto_producto;
    }

    public void setFoto_producto(String foto_producto) {
        this.foto_producto = foto_producto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public float getVenta_producto() {
        return venta_producto;
    }

    public void setVenta_producto(float venta_producto) {
        this.venta_producto = venta_producto;
    }

    public int getCantidad_producto() {
        return cantidad_producto;
    }

    public void setCantidad_producto(int cantidad_producto) {
        this.cantidad_producto = cantidad_producto;
    }

    public String getEstado_producto() {
        return estado_producto;
    }

    public void setEstado_producto(String estado_producto) {
        this.estado_producto = estado_producto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }

    public ArrayList<String> getColores() {
        return colores;
    }

    public void setColores(ArrayList<String> colores) {
        this.colores = colores;
    }

    public ArrayList<String> getTamanos() {
        return tamanos;
    }

    public void setTamanos(ArrayList<String> tamanos) {
        this.tamanos = tamanos;
    }

    public ArrayList<String> getModelos() {
        return modelos;
    }

    public void setModelos(ArrayList<String> modelos) {
        this.modelos = modelos;
    }

    public float getOferta() {
        return oferta;
    }

    public void setOferta(float oferta) {
        this.oferta = oferta;
    }
}
