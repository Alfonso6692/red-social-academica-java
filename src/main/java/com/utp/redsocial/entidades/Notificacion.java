// --- Notificacion.java ---
package com.utp.redsocial.entidades;

import java.time.LocalDateTime;

public class Notificacion {

    private String id;
    private String idUsuario;
    private String tipo;
    private String mensaje;
    private String idReferencia;
    private LocalDateTime fecha;
    private boolean leida;

    public Notificacion(String id, String idUsuario, String tipo, String mensaje, String idReferencia, LocalDateTime fecha, boolean leida) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.idReferencia = idReferencia;
        this.fecha = fecha;
        this.leida = leida;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(String idReferencia) {
        this.idReferencia = idReferencia;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}