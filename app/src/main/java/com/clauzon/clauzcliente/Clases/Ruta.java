package com.clauzon.clauzcliente.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Ruta implements Serializable {
    private String nombre,id;
    private Boolean estado;
    private ArrayList<Estacion> estaciones=new ArrayList<>();
    private String repartidor;

    public Ruta() {
    }

    public Ruta(String nombre, String id, Boolean estado, ArrayList<Estacion> estaciones, String repartidor) {
        this.nombre = nombre;
        this.id = id;
        this.estado = estado;
        this.estaciones = estaciones;
        this.repartidor = repartidor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public ArrayList<Estacion> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(ArrayList<Estacion> estaciones) {
        this.estaciones = estaciones;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }
}
