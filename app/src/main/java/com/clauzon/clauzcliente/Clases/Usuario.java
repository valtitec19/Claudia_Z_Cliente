package com.clauzon.clauzcliente.Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {
    private String nombre,apellidos,correo,telefono,id,fecha,genero,foto,direccion_envio;
    private List<String> pedidos= new ArrayList<>();
    private List<String> favoritos= new ArrayList<>();
    private  int multas;
    private String token;
    public Usuario() {
    }

    public Usuario(String nombre, String apellidos, String correo, String telefono, String id, String fecha, String genero, String foto, List<String> pedidos,List<String> favoritos,String direccion_envio,int multas,String token) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.id = id;
        this.fecha = fecha;
        this.genero = genero;
        this.foto = foto;
        this.pedidos=pedidos;
        this.favoritos=favoritos;
        this.direccion_envio=direccion_envio;
        this.multas=multas;
        this.token=token;
    }

    public Usuario(String nombre, String apellidos, String correo, String telefono, String id, String fecha, String genero, String foto) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.id = id;
        this.fecha = fecha;
        this.genero = genero;
        this.foto = foto;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<String> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<String> pedidos) {
        this.pedidos = pedidos;
    }

    public void addPedido(String pedido){
        pedidos.add(pedido);
    }
    public void addFav(String id_fav){
        favoritos.add(id_fav);
    }

    public List<String> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<String> favoritos) {
        this.favoritos = favoritos;
    }


    public String getDireccion_envio() {
        return direccion_envio;
    }

    public void setDireccion_envio(String direccion_envio) {
        this.direccion_envio = direccion_envio;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", id='" + id + '\'' +
                ", fecha='" + fecha + '\'' +
                ", genero='" + genero + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    public int getMultas() {
        return multas;
    }

    public void setMultas(int multas) {
        this.multas = multas;
    }
}
