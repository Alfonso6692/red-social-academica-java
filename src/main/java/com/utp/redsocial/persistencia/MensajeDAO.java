package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Mensaje;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Mensaje que usa ConexionBD para conectarse a Supabase
 */
public class MensajeDAO {

    // Consultas SQL
    private static final String INSERT_MENSAJE =
            "INSERT INTO mensajes (id, texto, id_emisor, id_receptor, fecha, leido) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT id, texto, id_emisor, id_receptor, fecha, leido FROM mensajes WHERE id = ?";

    private static final String SELECT_CONVERSACION =
            "SELECT id, texto, id_emisor, id_receptor, fecha, leido FROM mensajes " +
                    "WHERE (id_emisor = ? AND id_receptor = ?) OR (id_emisor = ? AND id_receptor = ?) " +
                    "ORDER BY fecha ASC";

    private static final String SELECT_POR_RECEPTOR =
            "SELECT id, texto, id_emisor, id_receptor, fecha, leido FROM mensajes " +
                    "WHERE id_receptor = ? ORDER BY fecha DESC";

    private static final String SELECT_POR_EMISOR =
            "SELECT id, texto, id_emisor, id_receptor, fecha, leido FROM mensajes " +
                    "WHERE id_emisor = ? ORDER BY fecha DESC";

    private static final String UPDATE_MARCAR_LEIDO =
            "UPDATE mensajes SET leido = true WHERE id = ?";

    private static final String COUNT_NO_LEIDOS =
            "SELECT COUNT(*) FROM mensajes WHERE id_receptor = ? AND leido = false";

    private static final String DELETE_MENSAJE =
            "DELETE FROM mensajes WHERE id = ?";

    public MensajeDAO() {
        System.out.println("MensajeDAO: Inicializado para usar ConexionBD");
    }

    /**
     * Guarda un nuevo mensaje en la base de datos
     */
    public void guardar(Mensaje mensaje) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                throw new SQLException("No se pudo obtener conexión a la base de datos");
            }

            stmt = conn.prepareStatement(INSERT_MENSAJE);
            stmt.setString(1, mensaje.getId());
            stmt.setString(2, mensaje.getTexto());
            stmt.setString(3, mensaje.getIdEmisor());
            stmt.setString(4, mensaje.getIdReceptor());
            stmt.setTimestamp(5, Timestamp.valueOf(mensaje.getFecha()));
            stmt.setBoolean(6, mensaje.isLeido());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("MensajeDAO: Mensaje guardado exitosamente - De " +
                        mensaje.getIdEmisor() + " para " + mensaje.getIdReceptor());
            } else {
                throw new SQLException("No se pudo guardar el mensaje");
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar mensaje en la base de datos", e);
        } finally {
            cerrarRecursos(null, stmt);
        }
    }

    /**
     * Busca un mensaje por su ID
     */
    public Mensaje buscarPorId(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para buscar mensaje por ID");
                return null;
            }

            stmt = conn.prepareStatement(SELECT_BY_ID);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMensaje(rs);
            }

            return null;

        } catch (SQLException e) {
            System.err.println("Error al buscar mensaje por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }

    /**
     * Obtiene la conversación entre dos usuarios
     */
    public List<Mensaje> obtenerConversacion(String idUsuario1, String idUsuario2) {
        List<Mensaje> mensajes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para obtener conversación");
                return mensajes;
            }

            stmt = conn.prepareStatement(SELECT_CONVERSACION);
            stmt.setString(1, idUsuario1);
            stmt.setString(2, idUsuario2);
            stmt.setString(3, idUsuario2);
            stmt.setString(4, idUsuario1);
            rs = stmt.executeQuery();

            while (rs.next()) {
                mensajes.add(mapResultSetToMensaje(rs));
            }

            System.out.println("MensajeDAO: Se encontraron " + mensajes.size() +
                    " mensajes en la conversación entre " + idUsuario1 + " y " + idUsuario2);

        } catch (SQLException e) {
            System.err.println("Error al obtener conversación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return mensajes;
    }

    /**
     * Obtiene todos los mensajes recibidos por un usuario
     */
    public List<Mensaje> obtenerMensajesPorReceptor(String idReceptor) {
        List<Mensaje> mensajes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                return mensajes;
            }

            stmt = conn.prepareStatement(SELECT_POR_RECEPTOR);
            stmt.setString(1, idReceptor);
            rs = stmt.executeQuery();

            while (rs.next()) {
                mensajes.add(mapResultSetToMensaje(rs));
            }

            System.out.println("MensajeDAO: Se encontraron " + mensajes.size() +
                    " mensajes recibidos por " + idReceptor);

        } catch (SQLException e) {
            System.err.println("Error al obtener mensajes por receptor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return mensajes;
    }

    /**
     * Obtiene todos los mensajes enviados por un usuario
     */
    public List<Mensaje> obtenerMensajesPorEmisor(String idEmisor) {
        List<Mensaje> mensajes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                return mensajes;
            }

            stmt = conn.prepareStatement(SELECT_POR_EMISOR);
            stmt.setString(1, idEmisor);
            rs = stmt.executeQuery();

            while (rs.next()) {
                mensajes.add(mapResultSetToMensaje(rs));
            }

            System.out.println("MensajeDAO: Se encontraron " + mensajes.size() +
                    " mensajes enviados por " + idEmisor);

        } catch (SQLException e) {
            System.err.println("Error al obtener mensajes por emisor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return mensajes;
    }

    /**
     * Marca un mensaje como leído
     */
    public void marcarComoLeido(String idMensaje) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para marcar mensaje como leído");
                return;
            }

            stmt = conn.prepareStatement(UPDATE_MARCAR_LEIDO);
            stmt.setString(1, idMensaje);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("MensajeDAO: Mensaje marcado como leído - ID: " + idMensaje);
            }

        } catch (SQLException e) {
            System.err.println("Error al marcar mensaje como leído: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(null, stmt);
        }
    }

    /**
     * Cuenta los mensajes no leídos de un usuario
     */
    public int contarMensajesNoLeidos(String idReceptor) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                return 0;
            }

            stmt = conn.prepareStatement(COUNT_NO_LEIDOS);
            stmt.setString(1, idReceptor);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("MensajeDAO: Usuario " + idReceptor + " tiene " + count + " mensajes no leídos");
                return count;
            }

        } catch (SQLException e) {
            System.err.println("Error al contar mensajes no leídos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return 0;
    }

    /**
     * Elimina un mensaje de la base de datos
     */
    public boolean eliminar(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para eliminar mensaje");
                return false;
            }

            stmt = conn.prepareStatement(DELETE_MENSAJE);
            stmt.setString(1, id);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("MensajeDAO: Mensaje eliminado - ID: " + id);
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("Error al eliminar mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(null, stmt);
        }
    }

    /**
     * Mapea un ResultSet a un objeto Mensaje
     */
    private Mensaje mapResultSetToMensaje(ResultSet rs) throws SQLException {
        Mensaje mensaje = new Mensaje();
        mensaje.setId(rs.getString("id"));
        mensaje.setTexto(rs.getString("texto"));
        mensaje.setIdEmisor(rs.getString("id_emisor"));
        mensaje.setIdReceptor(rs.getString("id_receptor"));

        Timestamp timestamp = rs.getTimestamp("fecha");
        if (timestamp != null) {
            mensaje.setFecha(timestamp.toLocalDateTime());
        }

        mensaje.setLeido(rs.getBoolean("leido"));

        return mensaje;
    }

    /**
     * Método de utilidad para cerrar recursos
     */
    private void cerrarRecursos(ResultSet rs, PreparedStatement stmt) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
    }
}