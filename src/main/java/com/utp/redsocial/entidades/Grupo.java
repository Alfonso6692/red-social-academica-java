package com.utp.redsocial.entidades;

import java.util.List;
import java.util.Objects;

/**
 * Representa un grupo de estudio con sus miembros y temas.
 */
public class Grupo {

    private String id;
    private String nombre;
    private String descripcion;
    private String categoriaId;
    private String creadorId;
    private List<String> temas;

    /**
     * Constructor para la clase Grupo.
     * @param id El ID único del grupo.
     * @param nombre El nombre del grupo.
     * @param descripcion Una breve descripción del grupo.
     * @param categoriaId El ID de la categoría a la que pertenece.
     * @param creadorId El ID del usuario que creó el grupo.
     * @param temas Una lista de temas relacionados con el grupo.
     */
    public Grupo(String id, String nombre, String descripcion, String categoriaId, String creadorId, List<String> temas) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.creadorId = creadorId;
        this.temas = temas;
    }

    // --- Getters y Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(String creadorId) {
        this.creadorId = creadorId;
    }

    public List<String> getTemas() {
        return temas;
    }

    public void setTemas(List<String> temas) {
        this.temas = temas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grupo grupo = (Grupo) o;
        return Objects.equals(id, grupo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
