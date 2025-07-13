package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Categoria; // Asegúrate de tener esta entidad creada

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO para la entidad Categoria.
 * Contiene los métodos para las operaciones CRUD de las categorías.
 */
public class CategoriaDAO {

    /**
     * Guarda una nueva categoría en la base de datos.
     * @param categoria El objeto Categoria a guardar.
     */
    public void guardar(Categoria categoria) {
        String sql = "INSERT INTO categorias (id, nombre, descripcion, categoria_padre_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getId());
            pstmt.setString(2, categoria.getNombre());
            pstmt.setString(3, categoria.getDescripcion());
            pstmt.setString(4, categoria.getCategoriaPadreId()); // Puede ser null

            pstmt.executeUpdate();
            System.out.println("Categoría guardada con éxito en la BD.");

        } catch (SQLException e) {
            System.err.println("Error al guardar la categoría: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Aquí podrías añadir otros métodos como:
    // - public Categoria buscarPorId(String id)
    // - public List<Categoria> listarTodas()
    // - public void actualizar(Categoria categoria)
    // - public void eliminar(String id)
}
