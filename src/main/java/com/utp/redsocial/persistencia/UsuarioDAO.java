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
 * DAO para la entidad Usuario.
 * Contiene todos los métodos para las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * de los usuarios en la base de datos.
 */
public class UsuarioDAO {

    /**
     * Guarda un nuevo usuario en la base de datos.
     * @param usuario El objeto Usuario a guardar.
     */
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nombre, apellido, correo, contrasena, carrera, ciclo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getId());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellido());
            pstmt.setString(4, usuario.getCorreo());
            pstmt.setString(5, usuario.getContrasena()); // Nota: En un proyecto real, la contraseña debe ser hasheada.
            pstmt.setString(6, usuario.getCarrera());
            pstmt.setInt(7, usuario.getCiclo());

            pstmt.executeUpdate();
            System.out.println("Usuario guardado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al guardar el usuario: " + e.getMessage());
        }
    }

    /**
     * Busca un usuario en la base de datos por su correo electrónico.
     * @param correo El correo electrónico del usuario a buscar.
     * @return Un objeto Usuario si se encuentra, de lo contrario null.
     */
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        Usuario usuario = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("carrera"),
                        rs.getInt("ciclo")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por correo: " + e.getMessage());
        }
        return usuario;
    }

    /**
     * Busca un usuario en la base de datos por su ID.
     * @param id El ID único del usuario.
     * @return Un objeto Usuario si se encuentra, de lo contrario null.
     */
    public Usuario buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("carrera"),
                        rs.getInt("ciclo")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return usuario;
    }


    /**
     * Actualiza los datos de un usuario existente en la base de datos.
     * @param usuario El objeto Usuario con los datos actualizados.
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
            System.out.println("Usuario actualizado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    /**
     * Elimina un usuario de la base de datos por su ID.
     * @param id El ID del usuario a eliminar.
     */
    public void eliminar(String id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("Usuario eliminado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    /**
     * Obtiene una lista de todos los usuarios registrados.
     * @return Una lista de objetos Usuario.
     */
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY nombre, apellido";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("carrera"),
                        rs.getInt("ciclo")
                );
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
        }
        return usuarios;
    }
}
