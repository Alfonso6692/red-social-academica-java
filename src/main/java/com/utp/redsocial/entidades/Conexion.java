package com.utp.redsocial.entidades;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad que representa la conexión (amistad) entre dos usuarios.
 */
public class Conexion {

    private String idUsuario1;
    private String idUsuario2;
    private LocalDate fechaConexion;

    public Conexion(String idUsuario1, String idUsuario2, LocalDate fechaConexion) {
        this.idUsuario1 = idUsuario1;
        this.idUsuario2 = idUsuario2;
        this.fechaConexion = fechaConexion;
    }

    // --- Getters y Setters ---

    public String getIdUsuario1() {
        return idUsuario1;
    }

    public void setIdUsuario1(String idUsuario1) {
        this.idUsuario1 = idUsuario1;
    }

    public String getIdUsuario2() {
        return idUsuario2;
    }

    public void setIdUsuario2(String idUsuario2) {
        this.idUsuario2 = idUsuario2;
    }

    public LocalDate getFechaConexion() {
        return fechaConexion;
    }

    public void setFechaConexion(LocalDate fechaConexion) {
        this.fechaConexion = fechaConexion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conexion conexion = (Conexion) o;
        return (Objects.equals(idUsuario1, conexion.idUsuario1) && Objects.equals(idUsuario2, conexion.idUsuario2)) ||
                (Objects.equals(idUsuario1, conexion.idUsuario2) && Objects.equals(idUsuario2, conexion.idUsuario1));
    }

    @Override
    public int hashCode() {
        // Ordena los IDs para que el hash sea el mismo sin importar el orden (A-B es igual a B-A)
        if (idUsuario1.compareTo(idUsuario2) > 0) {
            return Objects.hash(idUsuario2, idUsuario1);
        }
        return Objects.hash(idUsuario1, idUsuario2);
    }
}