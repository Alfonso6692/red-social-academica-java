package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Recurso;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Recurso que usa ConexionBD para conectarse a Supabase
 */
public class RecursoDAO {

    // Consultas SQL base
    private static final String INSERT_RECURSO =
            "INSERT INTO recursos (id, titulo, descripcion, url, tipo, fecha_publicacion) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT id, titulo, descripcion, url, tipo, fecha_publicacion FROM recursos WHERE id = ?";

    private static final String SELECT_ALL =
            "SELECT id, titulo, descripcion, url, tipo, fecha_publicacion FROM recursos ORDER BY fecha_publicacion DESC";

    private static final String UPDATE_RECURSO =
            "UPDATE recursos SET titulo = ?, descripcion = ?, url = ?, tipo = ?, fecha_publicacion = ? WHERE id = ?";

    private static final String DELETE_RECURSO =
            "DELETE FROM recursos WHERE id = ?";

    public RecursoDAO() {
        System.out.println("RecursoDAO: Inicializado para usar ConexionBD");
    }

    /**
     * Guarda un nuevo recurso en la base de datos
     */
    public void guardar(Recurso recurso) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                throw new SQLException("No se pudo obtener conexión a la base de datos");
            }

            stmt = conn.prepareStatement(INSERT_RECURSO);
            stmt.setString(1, recurso.getId());
            stmt.setString(2, recurso.getTitulo());
            stmt.setString(3, recurso.getDescripcion());
            stmt.setString(4, recurso.getUrl());
            stmt.setString(5, recurso.getTipo());
            stmt.setDate(6, Date.valueOf(recurso.getFechaPublicacion()));

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("RecursoDAO: Recurso guardado exitosamente - " + recurso.getTitulo());
            } else {
                throw new SQLException("No se pudo guardar el recurso");
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar recurso: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar recurso en la base de datos", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar statement: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Busca un recurso por su ID
     */
    public Recurso buscarPorId(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para buscar recurso por ID");
                return null;
            }

            stmt = conn.prepareStatement(SELECT_BY_ID);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Recurso recurso = mapResultSetToRecurso(rs);
                // TODO: Cargar etiquetas si tienes tabla separada
                return recurso;
            }

            return null;

        } catch (SQLException e) {
            System.err.println("Error al buscar recurso por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }

    /**
     * Obtiene todos los recursos
     */
    public List<Recurso> listarTodos() {
        List<Recurso> recursos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para listar recursos");
                return recursos;
            }

            stmt = conn.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Recurso recurso = mapResultSetToRecurso(rs);
                // TODO: Cargar etiquetas si tienes tabla separada
                recursos.add(recurso);
            }

            System.out.println("RecursoDAO: Se encontraron " + recursos.size() + " recursos");

        } catch (SQLException e) {
            System.err.println("Error al listar recursos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return recursos;
    }

    /**
     * Actualiza un recurso existente
     */
    public boolean actualizar(Recurso recurso) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para actualizar recurso");
                return false;
            }

            stmt = conn.prepareStatement(UPDATE_RECURSO);
            stmt.setString(1, recurso.getTitulo());
            stmt.setString(2, recurso.getDescripcion());
            stmt.setString(3, recurso.getUrl());
            stmt.setString(4, recurso.getTipo());
            stmt.setDate(5, Date.valueOf(recurso.getFechaPublicacion()));
            stmt.setString(6, recurso.getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("RecursoDAO: Recurso actualizado - " + recurso.getTitulo());
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("Error al actualizar recurso: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar statement: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Elimina un recurso de la base de datos
     */
    public boolean eliminar(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para eliminar recurso");
                return false;
            }

            stmt = conn.prepareStatement(DELETE_RECURSO);
            stmt.setString(1, id);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("RecursoDAO: Recurso eliminado - ID: " + id);
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("Error al eliminar recurso: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar statement: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Busca recursos por etiqueta (implementación temporal)
     * NOTA: Esto es temporal hasta que implementes el manejo de etiquetas adecuado
     */
    public List<Recurso> buscarPorEtiqueta(String etiqueta) {
        // Implementación temporal: buscar en título o descripción
        List<Recurso> recursosEncontrados = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                return recursosEncontrados;
            }

            String sql = "SELECT id, titulo, descripcion, url, tipo, fecha_publicacion " +
                    "FROM recursos " +
                    "WHERE LOWER(titulo) LIKE LOWER(?) OR LOWER(descripcion) LIKE LOWER(?) " +
                    "ORDER BY fecha_publicacion DESC";

            stmt = conn.prepareStatement(sql);
            String patron = "%" + etiqueta + "%";
            stmt.setString(1, patron);
            stmt.setString(2, patron);

            rs = stmt.executeQuery();

            while (rs.next()) {
                recursosEncontrados.add(mapResultSetToRecurso(rs));
            }

            System.out.println("RecursoDAO: Búsqueda por '" + etiqueta + "' encontró " + recursosEncontrados.size() + " recursos");

        } catch (SQLException e) {
            System.err.println("Error al buscar recursos por etiqueta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return recursosEncontrados;
    }

    /**
     * Mapea un ResultSet a un objeto Recurso
     */
    private Recurso mapResultSetToRecurso(ResultSet rs) throws SQLException {
        Recurso recurso = new Recurso();
        recurso.setId(rs.getString("id"));
        recurso.setTitulo(rs.getString("titulo"));
        recurso.setDescripcion(rs.getString("descripcion"));
        recurso.setUrl(rs.getString("url"));
        recurso.setTipo(rs.getString("tipo"));

        Date fecha = rs.getDate("fecha_publicacion");
        if (fecha != null) {
            recurso.setFechaPublicacion(fecha.toLocalDate());
        }

        return recurso;
    }

    /**
     * Cuenta el total de recursos
     */
    public int contarRecursos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                return 0;
            }

            stmt = conn.prepareStatement("SELECT COUNT(*) FROM recursos");
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar recursos: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return 0;
    }

    /**
     * Busca recursos por tipo
     */
    public List<Recurso> buscarPorTipo(String tipo) {
        List<Recurso> recursos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                return recursos;
            }

            String sql = "SELECT id, titulo, descripcion, url, tipo, fecha_publicacion " +
                    "FROM recursos WHERE tipo = ? ORDER BY fecha_publicacion DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tipo);
            rs = stmt.executeQuery();

            while (rs.next()) {
                recursos.add(mapResultSetToRecurso(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar recursos por tipo: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return recursos;
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