package com.clauzon.clauzcliente.Clases;

public class Estacion {

    private String nombre;
    private Boolean estado;
    private String hora;
    private String linea;
    private String foto;
    private String ruta;

    public Estacion() {
    }

    public Estacion(String nombre, Boolean estado, String hora, String linea, String foto, String ruta) {
        this.nombre = nombre;
        this.estado = estado;
        this.hora = hora;
        this.linea = linea;
        this.foto = foto;
        this.ruta = ruta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }


}
