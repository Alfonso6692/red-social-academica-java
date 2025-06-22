// --- CategoriaGrupo.java ---
package com.utp.redsocial.entidades;

public class CategoriaGrupo implements Comparable<CategoriaGrupo> {

    private String id;
    private String nombre;
    private String descripcion;
    private String categoriaPadreId;

    public CategoriaGrupo(String id, String nombre, String descripcion, String categoriaPadreId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaPadreId = categoriaPadreId;
    }

    // Getters y Setters

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

    public String getCategoriaPadreId() {
        return categoriaPadreId;
    }

    public void setCategoriaPadreId(String categoriaPadreId) {
        this.categoriaPadreId = categoriaPadreId;
    }

    @Override
    public int compareTo(CategoriaGrupo otraCategoria) {
        // Compara las categorías por nombre para el ordenamiento en el árbol AVL
        return this.nombre.compareTo(otraCategoria.getNombre());
    }
}