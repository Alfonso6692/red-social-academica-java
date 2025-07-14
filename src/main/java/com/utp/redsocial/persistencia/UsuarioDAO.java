package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the Usuario entity.
 * Contains all methods for CRUD operations (Create, Read, Update, Delete)
 * for users in the database.
 */
public class UsuarioDAO {

    /**
     * Saves a new user to the database.
     * @param usuario The Usuario object to save.
     */
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nombre, apellido, correo, contrasena, carrera, ciclo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getId());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellido());
            pstmt.setString(4, usuario.getCorreo());
            pstmt.setString(5, usuario.getContrasena());
            pstmt.setString(6, usuario.getCarrera());
            pstmt.setInt(7, usuario.getCiclo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Finds a user in the database by their email.
     * @param correo The user's email to search for.
     * @return A Usuario object if found, otherwise null.
     */
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Finds a user in the database by their ID.
     * @param id The unique ID of the user.
     * @return A Usuario object if found, otherwise null.
     */
    public Usuario buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing user's data in the database.
     * @param usuario The Usuario object with the updated data.
     */
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, contrasena = ?, carrera = ?, ciclo = ? WHERE id = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getCorreo());
            pstmt.setString(4, usuario.getContrasena());
            pstmt.setString(5, usuario.getCarrera());
            pstmt.setInt(6, usuario.getCiclo());
            pstmt.setString(7, usuario.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ... otros métodos como listarTodos, eliminar, etc.

    /**
     * Private utility method to map a row from a ResultSet to a Usuario object.
     * This avoids code duplication in the search methods.
     * @param rs The ResultSet positioned at the row to map.
     * @return A Usuario object with the data from the row.
     * @throws SQLException If an error occurs while accessing the ResultSet data.
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        // CORRECCIÓN: Se crea el objeto Usuario directamente con el constructor,
        // pasando los valores de cada columna del ResultSet.
        return new Usuario(
                rs.getString("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("correo"),
                rs.getString("contrasena"),
                rs.getString("carrera"),
                rs.getInt("ciclo")
        );
    }
}
