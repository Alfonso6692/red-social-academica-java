// --- SolicitudConexion.java ---
package com.utp.redsocial.entidades;

import java.util.Date;

public class SolicitudConexion {

    private String idSolicitante;
    private String idDestinatario;
    private Date fecha;
    private String estado; // "pendiente", "aceptada", "rechazada"

    public SolicitudConexion(String idSolicitante, String idDestinatario, Date fecha) {
        this.idSolicitante = idSolicitante;
        this.idDestinatario = idDestinatario;
        this.fecha = fecha;
        this.estado = "pendiente";
    }

    // Getters y Setters

    public String getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(String idSolicitante) {
        this.idSolicitante = idSolicitante;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}