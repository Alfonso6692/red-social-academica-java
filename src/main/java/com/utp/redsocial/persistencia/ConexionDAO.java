package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Conexion;
import com.utp.redsocial.entidades.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Conexion.
 * Gestiona la persistencia de las amistades entre usuarios.
 */
public class ConexionDAO {

    /**
     * Guarda una nueva conexión (amistad) en la base de datos.
     * @param conexion El objeto Conexion a guardar.
     */
    public void guardar(Conexion conexion) {
        String sql = "INSERT INTO conexiones (id_usuario1, id_usuario2, fecha_conexion) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, conexion.getIdUsuario1());
            pstmt.setString(2, conexion.getIdUsuario2());
            pstmt.setObject(3, conexion.getFechaConexion());

            pstmt.executeUpdate();
            System.out.println("Conexión guardada con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al guardar la conexión: " + e.getMessage());
        }
    }

    /**
     * Obtiene una lista de los IDs de todos los usuarios conectados a un usuario específico.
     * @param idUsuario El ID del usuario del que se quieren obtener las conexiones.
     * @return Una lista de Strings con los IDs de los amigos.
     */
    public List<String> obtenerIdsDeConexiones(String idUsuario) {
        List<String> idsConectados = new ArrayList<>();
        String sql = "SELECT id_usuario1, id_usuario2 FROM conexiones WHERE id_usuario1 = ? OR id_usuario2 = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idUsuario);
            pstmt.setString(2, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                String id1 = rs.getString("id_usuario1");
                String id2 = rs.getString("id_usuario2");

                // Añade a la lista el ID que NO es el del usuario que estamos buscando
                if (id1.equals(idUsuario)) {
                    idsConectados.add(id2);
                } else {
                    idsConectados.add(id1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las conexiones: " + e.getMessage());
        }
        return idsConectados;
    }

    /**
     * Elimina una conexión entre dos usuarios.
     * @param idUsuario1 ID del primer usuario.
     * @param idUsuario2 ID del segundo usuario.
     */
    public void eliminar(String idUsuario1, String idUsuario2) {
        String sql = "DELETE FROM conexiones WHERE (id_usuario1 = ? AND id_usuario2 = ?) OR (id_usuario1 = ? AND id_usuario2 = ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idUsuario1);
            pstmt.setString(2, idUsuario2);
            pstmt.setString(3, idUsuario2);
            pstmt.setString(4, idUsuario1);

            pstmt.executeUpdate();
            System.out.println("Conexión eliminada con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar la conexión: " + e.getMessage());
        }
    }

    public List<Usuario> obtenerAmigosDe(String idUsuario) {
        List<Usuario> amigos = new ArrayList<>();
        String sql = "SELECT u.* FROM usuarios u " +
                "JOIN amistades a ON u.id = a.usuario_b_id WHERE a.usuario_a_id = ? " +
                "UNION " +
                "SELECT u.* FROM usuarios u " +
                "JOIN amistades a ON u.id = a.usuario_a_id WHERE a.usuario_b_id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idUsuario);
            pstmt.setString(2, idUsuario);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Usuario amigo = new Usuario(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("carrera"),
                        rs.getString("ciclo")  // ✅ Correcto - ciclo es VARCHAR
                );
                amigos.add(amigo);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener amigos: " + e.getMessage());
            e.printStackTrace(); // Imprime el error completo para más detalles
        }
        return amigos;
    }
}
