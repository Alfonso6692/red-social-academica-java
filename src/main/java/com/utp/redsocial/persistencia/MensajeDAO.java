package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Mensaje;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Mensaje.
 * Contiene los métodos para guardar y recuperar mensajes de la base de datos.
 */
public class MensajeDAO {

    /**
     * Guarda un nuevo mensaje en la base de datos.
     * @param mensaje El objeto Mensaje a guardar.
     */
    public void guardar(Mensaje mensaje) {
        String sql = "INSERT INTO mensajes (id, texto, id_emisor, id_receptor, fecha, leido) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mensaje.getId());
            pstmt.setString(2, mensaje.getTexto());
            pstmt.setString(3, mensaje.getIdEmisor());
            pstmt.setString(4, mensaje.getIdReceptor());
            pstmt.setObject(5, mensaje.getFecha()); // Usar setObject para LocalDateTime
            pstmt.setBoolean(6, mensaje.isLeido());

            pstmt.executeUpdate();
            System.out.println("Mensaje guardado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al guardar el mensaje: " + e.getMessage());
        }
    }

    /**
     * Obtiene la conversación completa entre dos usuarios.
     * Recupera todos los mensajes donde los dos usuarios son emisor o receptor.
     * @param idUsuario1 El ID del primer usuario.
     * @param idUsuario2 El ID del segundo usuario.
     * @return Una lista de objetos Mensaje, ordenada por fecha.
     */
    public List<Mensaje> obtenerConversacion(String idUsuario1, String idUsuario2) {
        String sql = "SELECT * FROM mensajes WHERE " +
                "(id_emisor = ? AND id_receptor = ?) OR " +
                "(id_emisor = ? AND id_receptor = ?) " +
                "ORDER BY fecha ASC";

        List<Mensaje> conversacion = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idUsuario1);
            pstmt.setString(2, idUsuario2);
            pstmt.setString(3, idUsuario2);
            pstmt.setString(4, idUsuario1);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Mensaje mensaje = new Mensaje(
                        rs.getString("id"),
                        rs.getString("texto"),
                        rs.getString("id_emisor"),
                        rs.getString("id_receptor"),
                        rs.getObject("fecha", LocalDateTime.class),
                        rs.getBoolean("leido")
                );
                conversacion.add(mensaje);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener la conversación: " + e.getMessage());
        }
        return conversacion;
    }

    /**
     * Actualiza el estado 'leido' de un mensaje.
     * @param idMensaje El ID del mensaje a actualizar.
     * @param leido El nuevo estado (true o false).
     */
    public void actualizarEstadoLeido(String idMensaje, boolean leido) {
        String sql = "UPDATE mensajes SET leido = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, leido);
            pstmt.setString(2, idMensaje);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del mensaje: " + e.getMessage());
        }
    }
}
