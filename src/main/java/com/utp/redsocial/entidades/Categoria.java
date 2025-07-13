package com.utp.redsocial.entidades;

/**
 * Representa una categoría a la que pueden pertenecer los grupos.
 * Implementa la interfaz Comparable para poder ser ordenada en estructuras
 * de datos como un ArbolAVL.
 */
public class Categoria implements Comparable<Categoria> {

    private String id;
    private String nombre;
    private String descripcion;
    private String categoriaPadreId; // ID de la categoría padre, para jerarquías

    /**
     * Constructor para crear una nueva categoría.
     *
     * @param id               El ID único de la categoría.
     * @param nombre           El nombre de la categoría.
     * @param descripcion      Una breve descripción de la categoría.
     * @param categoriaPadreId El ID de la categoría padre (puede ser null).
     */
    public Categoria(String id, String nombre, String descripcion, String categoriaPadreId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaPadreId = categoriaPadreId;
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

    public String getCategoriaPadreId() {
        return categoriaPadreId;
    }

    public void setCategoriaPadreId(String categoriaPadreId) {
        this.categoriaPadreId = categoriaPadreId;
    }

    /**
     * Método de comparación requerido por la interfaz Comparable.
     * Permite ordenar las categorías alfabéticamente por su nombre.
     * @param otraCategoria La otra categoría con la que se va a comparar.
     * @return un valor negativo si esta categoría va antes, cero si son iguales,
     * o un valor positivo si esta categoría va después.
     */
    @Override
    public int compareTo(Categoria otraCategoria) {
        // Se utiliza el método compareTo de la clase String para comparar los nombres.
        return this.nombre.compareTo(otraCategoria.getNombre());
    }
}
