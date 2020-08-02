package com.clauzon.clauzcliente.Clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Repartidor implements Serializable {
    private String nombre,apellidos,correo,telefono,id,fecha,genero,foto,direccion,horario_inicio,horario_fin;
    private Boolean estado;
    private List<String> entregas=new ArrayList<>();
    private String token;

    public Repartidor() {
    }

    public Repartidor(String nombre, String apellidos, String correo, String telefono, String id, String fecha, String genero, Boolean estado,String foto,String direccion, String horario_inicio,String horario_fin, List<String> entregas,String token) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.id = id;
        this.fecha = fecha;
        this.genero = genero;
        this.estado = estado;
        this.foto=foto;
        this.horario_inicio=horario_inicio;
        this.horario_fin=horario_fin;
        this.direccion=direccion;
        this.entregas=entregas;
        this.token=token;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHorario_inicio() {
        return horario_inicio;
    }

    public void setHorario_inicio(String horario_inicio) {
        this.horario_inicio = horario_inicio;
    }

    public String getHorario_fin() {
        return horario_fin;
    }

    public void setHorario_fin(String horario_fin) {
        this.horario_fin = horario_fin;
    }

    public List<String> getEntregas() {
        return entregas;
    }

    public void setEntregas(List<String> entregas) {
        this.entregas = entregas;
    }

    public void addEntrega(String id_entrega){
        entregas.add(id_entrega);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
