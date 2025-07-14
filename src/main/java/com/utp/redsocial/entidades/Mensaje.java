package com.utp.redsocial.entidades;

import java.time.LocalDateTime;

public class Mensaje {

    private String id;
    private String texto;
    private String idEmisor;
    private String idReceptor;
    private LocalDateTime fecha;
    private boolean leido;

    public Mensaje() {
        this.id = id;
        this.texto = texto;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
        this.fecha = LocalDateTime.now();
        this.leido = false;
    }

    // Constructor completo por si se lee de la BD
    public Mensaje(String id, String texto, String idEmisor, String idReceptor, LocalDateTime fecha, boolean leido) {
        this.id = id;
        this.texto = texto;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
        this.fecha = fecha;
        this.leido = leido;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getIdEmisor() { return idEmisor; }
    public void setIdEmisor(String idEmisor) { this.idEmisor = idEmisor; }
    public String getIdReceptor() { return idReceptor; }
    public void setIdReceptor(String idReceptor) { this.idReceptor = idReceptor; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }
}
