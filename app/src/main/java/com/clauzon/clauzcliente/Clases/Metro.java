package com.clauzon.clauzcliente.Clases;

import java.util.ArrayList;

public class Metro {
    private ArrayList<Estacion> estaciones=new ArrayList<>();
    private String linea;

    public Metro() {
    }

    public Metro(ArrayList<Estacion> estaciones, String linea) {
        this.estaciones = estaciones;
        this.linea = linea;
    }

    public ArrayList<Estacion> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(ArrayList<Estacion> estaciones) {
        this.estaciones = estaciones;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }
    public void llena_array(ArrayList<Estacion> estaciones){
        this.estaciones.addAll(estaciones);
    }
}
