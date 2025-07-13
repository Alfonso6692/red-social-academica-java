package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Categoria; // Importamos la entidad Categoria
import com.utp.redsocial.entidades.Grupo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DAO para la entidad Grupo.
 * Contiene todos los métodos para las operaciones CRUD de los grupos
 * y también maneja la creación de categorías.
 */
public class GrupoDAO {

    /**
     * Guarda un nuevo grupo en la base de datos.
     * @param grupo El objeto Grupo a guardar.
     */
    public void guardar(Grupo grupo) {
        // Los temas se guardan como un solo string separado por comas.
        String sql = "INSERT INTO grupos (id, nombre, descripcion, categoria_id, creador_id, temas) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, grupo.getId());
            pstmt.setString(2, grupo.getNombre());
            pstmt.setString(3, grupo.getDescripcion());
            pstmt.setString(4, grupo.getCategoriaId());
            pstmt.setString(5, grupo.getCreadorId());

            // Convierte la lista de temas a un string
            String temasComoString = String.join(",", grupo.getTemas());
            pstmt.setString(6, temasComoString);

            pstmt.executeUpdate();
            System.out.println("Grupo guardado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al guardar el grupo: " + e.getMessage());
        }
    }

    /**
     * Busca un grupo en la base de datos por su ID.
     * @param id El ID del grupo a buscar.
     * @return Un objeto Grupo si se encuentra, de lo contrario null.
     */
    public Grupo buscarPorId(String id) {
        String sql = "SELECT * FROM grupos WHERE id = ?";
        Grupo grupo = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Convierte el string de temas de vuelta a una lista
                String temasComoString = rs.getString("temas");
                List<String> temas = new ArrayList<>(Arrays.asList(temasComoString.split(",")));

                grupo = new Grupo(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("categoria_id"),
                        rs.getString("creador_id"),
                        temas
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar grupo por ID: " + e.getMessage());
        }
        return grupo;
    }

    /**
     * Actualiza los datos de un grupo existente en la base de datos.
     * @param grupo El objeto Grupo con los datos actualizados.
     */
    public void actualizar(Grupo grupo) {
        String sql = "UPDATE grupos SET nombre = ?, descripcion = ?, categoria_id = ?, temas = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, grupo.getNombre());
            pstmt.setString(2, grupo.getDescripcion());
            pstmt.setString(3, grupo.getCategoriaId());

            String temasComoString = String.join(",", grupo.getTemas());
            pstmt.setString(4, temasComoString);

            pstmt.setString(5, grupo.getId());

            pstmt.executeUpdate();
            System.out.println("Grupo actualizado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar el grupo: " + e.getMessage());
        }
    }

    /**
     * Elimina un grupo de la base de datos por su ID.
     * @param id El ID del grupo a eliminar.
     */
    public void eliminar(String id) {
        String sql = "DELETE FROM grupos WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("Grupo eliminado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar el grupo: " + e.getMessage());
        }
    }

    /**
     * Obtiene una lista de todos los grupos registrados.
     * @return Una lista de objetos Grupo.
     */
    public List<Grupo> listarTodos() {
        String sql = "SELECT * FROM grupos ORDER BY nombre";
        List<Grupo> grupos = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String temasComoString = rs.getString("temas");
                List<String> temas = new ArrayList<>(Arrays.asList(temasComoString.split(",")));

                Grupo grupo = new Grupo(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("categoria_id"),
                        rs.getString("creador_id"),
                        temas
                );
                grupos.add(grupo);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar los grupos: " + e.getMessage());
        }
        return grupos;
    }

    // =================================================================
    // MÉTODO AÑADIDO PARA MANEJAR CATEGORÍAS
    // =================================================================
    /**
     * Guarda una nueva categoría en la base de datos.
     * @param categoria El objeto Categoria a guardar.
     */
    public void guardarCategoria(Categoria categoria) {
        String sql = "INSERT INTO categorias (id, nombre, descripcion, categoria_padre_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getId());
            pstmt.setString(2, categoria.getNombre());
            pstmt.setString(3, categoria.getDescripcion());
            pstmt.setString(4, categoria.getCategoriaPadreId());

            pstmt.executeUpdate();
            System.out.println("Categoría guardada con éxito desde GrupoDAO.");

        } catch (SQLException e) {
            System.err.println("Error al guardar la categoría: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
