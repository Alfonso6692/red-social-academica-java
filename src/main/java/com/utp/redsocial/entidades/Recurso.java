package com.utp.redsocial.entidades;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Recurso {
    private String id;
    private String titulo;
    private String descripcion;
    private String url;
    private String tipo;
    private String usuarioId;
    private List<String> etiquetas;
    private Date fechaCreacion;
    private Date fechaActualizacion;

    // Constructor vacío
    public Recurso() {
        this.etiquetas = new ArrayList<>();
        this.fechaCreacion = new Date();
        this.fechaActualizacion = new Date();
    }

    // Constructor con parámetros básicos
    public Recurso(String id, String titulo, String descripcion, String url, String tipo, String usuarioId) {
        this();
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
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

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas != null ? etiquetas : new ArrayList<>();
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // Métodos de utilidad
    public void agregarEtiqueta(String etiqueta) {
        if (etiqueta != null && !etiqueta.trim().isEmpty()) {
            if (this.etiquetas == null) {
                this.etiquetas = new ArrayList<>();
            }
            this.etiquetas.add(etiqueta.trim());
        }
    }

    public void removerEtiqueta(String etiqueta) {
        if (this.etiquetas != null && etiqueta != null) {
            this.etiquetas.remove(etiqueta.trim());
        }
    }

    public boolean tieneEtiqueta(String etiqueta) {
        if (this.etiquetas == null || etiqueta == null) {
            return false;
        }
        return this.etiquetas.contains(etiqueta.trim());
    }

    public String getEtiquetasComoTexto() {
        if (this.etiquetas == null || this.etiquetas.isEmpty()) {
            return "";
        }
        return String.join(", ", this.etiquetas);
    }

    public void setEtiquetasDesdeTexto(String etiquetasTexto) {
        this.etiquetas = new ArrayList<>();
        if (etiquetasTexto != null && !etiquetasTexto.trim().isEmpty()) {
            String[] etiquetasArray = etiquetasTexto.split(",");
            for (String etiqueta : etiquetasArray) {
                String etiquetaLimpia = etiqueta.trim();
                if (!etiquetaLimpia.isEmpty()) {
                    this.etiquetas.add(etiquetaLimpia);
                }
            }
        }
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Recurso{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", url='" + url + '\'' +
                ", tipo='" + tipo + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", etiquetas=" + etiquetas +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }

    // equals y hashCode basados en el ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recurso recurso = (Recurso) obj;
        return id != null ? id.equals(recurso.id) : recurso.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}