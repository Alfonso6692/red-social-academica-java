package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Recurso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DAO para la entidad Recurso.
 * Contiene todos los métodos para las operaciones CRUD de los recursos
 * educativos en la base de datos.
 */
public class RecursoDAO {

    /**
     * Guarda un nuevo recurso en la base de datos.
     * @param recurso El objeto Recurso a guardar.
     */
    public void guardar(Recurso recurso) {
        String sql = "INSERT INTO recursos (id, titulo, descripcion, url, tipo, fecha_publicacion, etiquetas) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, recurso.getId());
            pstmt.setString(2, recurso.getTitulo());
            pstmt.setString(3, recurso.getDescripcion());
            pstmt.setString(4, recurso.getUrl());
            pstmt.setString(5, recurso.getTipo());
            pstmt.setObject(6, recurso.getFechaPublicacion()); // Usar setObject para LocalDate

            // Convierte la lista de etiquetas a un string separado por comas
            String etiquetasComoString = String.join(",", recurso.getEtiquetas());
            pstmt.setString(7, etiquetasComoString);

            pstmt.executeUpdate();
            System.out.println("Recurso guardado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al guardar el recurso: " + e.getMessage());
        }
    }

    /**
     * Busca un recurso en la base de datos por su ID.
     * @param id El ID del recurso a buscar.
     * @return Un objeto Recurso si se encuentra, de lo contrario null.
     */
    public Recurso buscarPorId(String id) {
        String sql = "SELECT * FROM recursos WHERE id = ?";
        Recurso recurso = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String etiquetasComoString = rs.getString("etiquetas");
                List<String> etiquetas = new ArrayList<>(Arrays.asList(etiquetasComoString.split(",")));

                recurso = new Recurso(
                        rs.getString("id"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("url"),
                        rs.getString("tipo"),
                        rs.getObject("fecha_publicacion", LocalDate.class), // Usar getObject para LocalDate
                        etiquetas
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar recurso por ID: " + e.getMessage());
        }
        return recurso;
    }

    /**
     * Actualiza los datos de un recurso existente en la base de datos.
     * @param recurso El objeto Recurso con los datos actualizados.
     */
    public void actualizar(Recurso recurso) {
        String sql = "UPDATE recursos SET titulo = ?, descripcion = ?, url = ?, tipo = ?, fecha_publicacion = ?, etiquetas = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, recurso.getTitulo());
            pstmt.setString(2, recurso.getDescripcion());
            pstmt.setString(3, recurso.getUrl());
            pstmt.setString(4, recurso.getTipo());
            pstmt.setObject(5, recurso.getFechaPublicacion());

            String etiquetasComoString = String.join(",", recurso.getEtiquetas());
            pstmt.setString(6, etiquetasComoString);

            pstmt.setString(7, recurso.getId());

            pstmt.executeUpdate();
            System.out.println("Recurso actualizado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al actualizar el recurso: " + e.getMessage());
        }
    }

    /**
     * Elimina un recurso de la base de datos por su ID.
     * @param id El ID del recurso a eliminar.
     */
    public void eliminar(String id) {
        String sql = "DELETE FROM recursos WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("Recurso eliminado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al eliminar el recurso: " + e.getMessage());
        }
    }

    /**
     * Obtiene una lista de todos los recursos registrados.
     * @return Una lista de objetos Recurso.
     */
    public List<Recurso> listarTodos() {
        String sql = "SELECT * FROM recursos ORDER BY fecha_publicacion DESC";
        List<Recurso> recursos = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String etiquetasComoString = rs.getString("etiquetas");
                List<String> etiquetas = new ArrayList<>(Arrays.asList(etiquetasComoString.split(",")));

                Recurso recurso = new Recurso(
                        rs.getString("id"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("url"),
                        rs.getString("tipo"),
                        rs.getObject("fecha_publicacion", LocalDate.class),
                        etiquetas
                );
                recursos.add(recurso);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar los recursos: " + e.getMessage());
        }
        return recursos;
    }
}
