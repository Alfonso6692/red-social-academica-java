// --- Recurso.java ---
package com.utp.redsocial.entidades;

import java.time.LocalDate;
import java.util.List;

public class Recurso {

    private String id;
    private String titulo;
    private String descripcion;
    private String url;
    private String tipo;
    private LocalDate fechaPublicacion;
    private List<String> etiquetas;

    public Recurso(String id, String titulo, String descripcion, String url, String tipo, LocalDate fechaPublicacion, List<String> etiquetas) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.tipo = tipo;
        this.fechaPublicacion = fechaPublicacion;
        this.etiquetas = etiquetas;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }
}