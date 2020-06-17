package com.clauzon.clauzcliente.Clases;

public class Comprobante {
    private String cliente;
    private String concepto;
    private String monto;
    private String fecha;
    private String tarjeta;
    private String hora;
    private String status;

    public Comprobante() {
    }

    public Comprobante(String cliente, String concepto, String monto, String fecha, String tarjeta, String hora, String status) {
        this.cliente = cliente;
        this.concepto = concepto;
        this.monto = monto;
        this.fecha = fecha;
        this.tarjeta = tarjeta;
        this.hora = hora;
        this.status = status;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
