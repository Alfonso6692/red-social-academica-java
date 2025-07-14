package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Recurso;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DAO para gestionar recursos en la base de datos
 */
public class RecursoDAO {

    /**
     * Lista todos los recursos de la base de datos
     */
    public List<Recurso> listarTodos() {
        List<Recurso> recursos = new ArrayList<>();
        String sql = "SELECT id, titulo, descripcion, url, tipo, usuario_id, etiquetas, fecha_publicacion FROM recursos ORDER BY fecha_publicacion DESC";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Recurso recurso = mapearResultSetARecurso(rs);
                recursos.add(recurso);
            }

            System.out.println("RecursoDAO: Se encontraron " + recursos.size() + " recursos");

        } catch (SQLException e) {
            System.err.println("Error al listar recursos: " + e.getMessage());
            e.printStackTrace();
        }

        return recursos;
    }

    /**
     * Obtiene un recurso por su ID
     */
    public Recurso obtenerPorId(String id) {
        String sql = "SELECT id, titulo, descripcion, url, tipo, usuario_id, etiquetas, fecha_publicacion FROM recursos WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetARecurso(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener recurso por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserta un nuevo recurso en la base de datos
     */
    public boolean insertar(Recurso recurso) {
        String sql = "INSERT INTO recursos (id, titulo, descripcion, url, tipo, usuario_id, etiquetas, fecha_publicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recurso.getId());
            stmt.setString(2, recurso.getTitulo());
            stmt.setString(3, recurso.getDescripcion());
            stmt.setString(4, recurso.getUrl());
            stmt.setString(5, recurso.getTipo());
            stmt.setString(6, recurso.getUsuarioId());

            // Convertir lista de etiquetas a texto separado por comas
            String etiquetasTexto = "";
            if (recurso.getEtiquetas() != null && !recurso.getEtiquetas().isEmpty()) {
                etiquetasTexto = String.join(",", recurso.getEtiquetas());
            }
            stmt.setString(7, etiquetasTexto);

            // Usar fecha actual
            stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Recurso insertado exitosamente: " + recurso.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar recurso: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Actualiza un recurso existente
     */
    public boolean actualizar(Recurso recurso) {
        String sql = "UPDATE recursos SET titulo = ?, descripcion = ?, url = ?, tipo = ?, etiquetas = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recurso.getTitulo());
            stmt.setString(2, recurso.getDescripcion());
            stmt.setString(3, recurso.getUrl());
            stmt.setString(4, recurso.getTipo());

            // Convertir lista de etiquetas a texto
            String etiquetasTexto = "";
            if (recurso.getEtiquetas() != null && !recurso.getEtiquetas().isEmpty()) {
                etiquetasTexto = String.join(",", recurso.getEtiquetas());
            }
            stmt.setString(5, etiquetasTexto);

            stmt.setString(6, recurso.getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Recurso actualizado exitosamente: " + recurso.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar recurso: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina un recurso por su ID
     */
    public boolean eliminar(String id) {
        String sql = "DELETE FROM recursos WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Recurso eliminado exitosamente: " + id);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar recurso: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Recurso
     */
    private Recurso mapearResultSetARecurso(ResultSet rs) throws SQLException {
        Recurso recurso = new Recurso();

        recurso.setId(rs.getString("id"));
        recurso.setTitulo(rs.getString("titulo"));
        recurso.setDescripcion(rs.getString("descripcion"));
        recurso.setUrl(rs.getString("url"));
        recurso.setTipo(rs.getString("tipo"));
        recurso.setUsuarioId(rs.getString("usuario_id"));

        // Convertir texto de etiquetas a lista
        String etiquetasTexto = rs.getString("etiquetas");
        if (etiquetasTexto != null && !etiquetasTexto.trim().isEmpty()) {
            List<String> etiquetas = new ArrayList<>();
            String[] etiquetasArray = etiquetasTexto.split(",");
            for (String etiqueta : etiquetasArray) {
                String etiquetaLimpia = etiqueta.trim();
                if (!etiquetaLimpia.isEmpty()) {
                    etiquetas.add(etiquetaLimpia);
                }
            }
            recurso.setEtiquetas(etiquetas);
        }

        // Usar fecha_publicacion como fecha_creacion
        Timestamp fechaPublicacion = rs.getTimestamp("fecha_publicacion");
        if (fechaPublicacion != null) {
            recurso.setFechaCreacion(fechaPublicacion);
            recurso.setFechaActualizacion(fechaPublicacion);
        }

        return recurso;
    }

    /**
     * Verifica si existe un recurso con el ID dado
     */
    public boolean existe(String id) {
        String sql = "SELECT COUNT(*) FROM recursos WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de recurso: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}