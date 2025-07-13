package com.utp.redsocial.persistencia;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.entidades.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Usuario que usa ConexionBD para conectarse a Supabase
 */
public class UsuarioDAO {

    // Consultas SQL
    private static final String INSERT_USUARIO =
            "INSERT INTO usuarios (id, nombre, apellido, correo, contrasena, carrera, ciclo) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT id, nombre, apellido, correo, contrasena, carrera, ciclo FROM usuarios WHERE id = ?";

    private static final String SELECT_BY_CORREO =
            "SELECT id, nombre, apellido, correo, contrasena, carrera, ciclo FROM usuarios WHERE correo = ?";

    private static final String UPDATE_USUARIO =
            "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, contrasena = ?, carrera = ?, ciclo = ? WHERE id = ?";

    private static final String DELETE_USUARIO =
            "DELETE FROM usuarios WHERE id = ?";

    private static final String SELECT_ALL =
            "SELECT id, nombre, apellido, correo, contrasena, carrera, ciclo FROM usuarios ORDER BY nombre, apellido";

    public UsuarioDAO() {
        System.out.println("UsuarioDAO: Inicializado para usar ConexionBD");
    }

    /**
     * Guarda un nuevo usuario en la base de datos
     */
    public void guardar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                throw new SQLException("No se pudo obtener conexión a la base de datos");
            }

            stmt = conn.prepareStatement(INSERT_USUARIO);
            stmt.setString(1, usuario.getId());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellido());
            stmt.setString(4, usuario.getCorreo());
            stmt.setString(5, usuario.getContrasena());
            stmt.setString(6, usuario.getCarrera());
            stmt.setString(7, usuario.getCiclo());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("UsuarioDAO: Usuario guardado exitosamente - " + usuario.getCorreo());
            } else {
                throw new SQLException("No se pudo guardar el usuario");
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al guardar usuario en la base de datos", e);
        } finally {
            // Cerrar statement (la conexión la mantiene ConexionBD)
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
     * Busca un usuario por su ID
     */
    public Usuario buscarPorId(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para buscar usuario por ID");
                return null;
            }

            stmt = conn.prepareStatement(SELECT_BY_ID);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUsuario(rs);
            }

            return null;

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }

    /**
     * Busca un usuario por su correo electrónico
     */
    public Usuario buscarPorCorreo(String correo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para buscar usuario por correo");
                return null;
            }

            stmt = conn.prepareStatement(SELECT_BY_CORREO);
            stmt.setString(1, correo.toLowerCase());
            rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = mapResultSetToUsuario(rs);
                System.out.println("UsuarioDAO: Usuario encontrado - " + usuario.getNombreCompleto());
                return usuario;
            }

            System.out.println("UsuarioDAO: Usuario no encontrado para correo - " + correo);
            return null;

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por correo: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            cerrarRecursos(rs, stmt);
        }
    }

    /**
     * Actualiza un usuario existente
     */
    public boolean actualizar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para actualizar usuario");
                return false;
            }

            stmt = conn.prepareStatement(UPDATE_USUARIO);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getContrasena());
            stmt.setString(5, usuario.getCarrera());
            stmt.setString(6, usuario.getCiclo());
            stmt.setString(7, usuario.getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("UsuarioDAO: Usuario actualizado - " + usuario.getCorreo());
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
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
     * Elimina un usuario de la base de datos
     */
    public boolean eliminar(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para eliminar usuario");
                return false;
            }

            stmt = conn.prepareStatement(DELETE_USUARIO);
            stmt.setString(1, id);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("UsuarioDAO: Usuario eliminado - ID: " + id);
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
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
     * Obtiene todos los usuarios
     */
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para listar usuarios");
                return usuarios;
            }

            stmt = conn.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }

            System.out.println("UsuarioDAO: Se encontraron " + usuarios.size() + " usuarios");

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return usuarios;
    }

    /**
     * Mapea un ResultSet a un objeto Usuario
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getString("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setCarrera(rs.getString("carrera"));
        usuario.setCiclo(rs.getString("ciclo"));
        return usuario;
    }

    /**
     * Verifica si existe un usuario con el correo dado
     */
    public boolean existeCorreo(String correo) {
        return buscarPorCorreo(correo) != null;
    }

    /**
     * Cuenta el total de usuarios registrados
     */
    public int contarUsuarios() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            if (conn == null) {
                return 0;
            }

            stmt = conn.prepareStatement("SELECT COUNT(*) FROM usuarios");
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar usuarios: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, stmt);
        }

        return 0;
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