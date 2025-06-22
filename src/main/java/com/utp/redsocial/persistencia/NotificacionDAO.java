package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Notificacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Notificacion.
 * Contiene los métodos para guardar, recuperar y actualizar las notificaciones
 * de los usuarios en la base de datos.
 */
public class NotificacionDAO {

    /**
     * Guarda una nueva notificación en la base de datos.
     * @param notificacion El objeto Notificacion a guardar.
     */
    public void guardar(Notificacion notificacion) {
        String sql = "INSERT INTO notificaciones (id, id_usuario, tipo, mensaje, id_referencia, fecha, leida) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, notificacion.getId());
            pstmt.setString(2, notificacion.getIdUsuario());
            pstmt.setString(3, notificacion.getTipo());
            pstmt.setString(4, notificacion.getMensaje());
            pstmt.setString(5, notificacion.getIdReferencia());
            pstmt.setObject(6, notificacion.getFecha()); // Usar setObject para LocalDateTime
            pstmt.setBoolean(7, notificacion.isLeida());

            pstmt.executeUpdate();
            System.out.println("Notificación guardada con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al guardar la notificación: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las notificaciones de un usuario específico.
     * @param idUsuario El ID del usuario cuyas notificaciones se quieren recuperar.
     * @return Una lista de objetos Notificacion, ordenada por fecha descendente.
     */
    public List<Notificacion> obtenerPorUsuario(String idUsuario) {
        String sql = "SELECT * FROM notificaciones WHERE id_usuario = ? ORDER BY fecha DESC";
        List<Notificacion> notificaciones = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Notificacion notificacion = new Notificacion(
                        rs.getString("id"),
                        rs.getString("id_usuario"),
                        rs.getString("tipo"),
                        rs.getString("mensaje"),
                        rs.getString("id_referencia"),
                        rs.getObject("fecha", LocalDateTime.class),
                        rs.getBoolean("leida")
                );
                notificaciones.add(notificacion);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las notificaciones: " + e.getMessage());
        }
        return notificaciones;
    }

    /**
     * Actualiza el estado 'leida' de una notificación.
     * @param idNotificacion El ID de la notificación a actualizar.
     * @param leida El nuevo estado (true o false).
     */
    public void actualizarEstadoLeida(String idNotificacion, boolean leida) {
        String sql = "UPDATE notificaciones SET leida = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, leida);
            pstmt.setString(2, idNotificacion);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado de la notificación: " + e.getMessage());
        }
    }

    /**
     * Elimina una notificación de la base de datos.
     * @param idNotificacion El ID de la notificación a eliminar.
     */
    public void eliminar(String idNotificacion) {
        String sql = "DELETE FROM notificaciones WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idNotificacion);
            pstmt.executeUpdate();
            System.out.println("Notificación eliminada con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar la notificación: " + e.getMessage());
        }
    }
}
