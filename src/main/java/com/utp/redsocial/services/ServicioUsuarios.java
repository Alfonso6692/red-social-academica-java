package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.persistencia.UsuarioDAO;

/**
 * Capa de servicio para la lógica de negocio de los Usuarios.
 * Actúa como intermediario entre los Servlets y los DAOs.
 */
public class ServicioUsuarios {

    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor del servicio.
     */
    public ServicioUsuarios() {
        this.usuarioDAO = new UsuarioDAO();
        System.out.println("ServicioUsuarios: Inicializado correctamente");
    }

    /**
     * Valida las credenciales de un usuario.
     * @param correo El correo electrónico ingresado.
     * @param contrasena La contraseña ingresada.
     * @return El objeto Usuario si las credenciales son correctas, de lo contrario null.
     */
    public Usuario validarCredenciales(String correo, String contrasena) {
        try {
            if (correo == null || correo.trim().isEmpty() ||
                    contrasena == null || contrasena.trim().isEmpty()) {
                return null;
            }

            Usuario usuario = usuarioDAO.buscarPorCorreo(correo.trim().toLowerCase());

            if (usuario != null && usuario.getContrasena().equals(contrasena)) {
                System.out.println("ServicioUsuarios: Credenciales válidas para " + correo);
                return usuario;
            }

            System.out.println("ServicioUsuarios: Credenciales inválidas para " + correo);
            return null;

        } catch (Exception e) {
            System.err.println("Error en validarCredenciales: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un usuario por su correo electrónico.
     * @param correo El correo del usuario a buscar.
     * @return El usuario encontrado o null si no existe.
     */
    public Usuario buscarPorCorreo(String correo) {
        try {
            if (correo == null || correo.trim().isEmpty()) {
                return null;
            }

            return usuarioDAO.buscarPorCorreo(correo.trim().toLowerCase());

        } catch (Exception e) {
            System.err.println("Error en buscarPorCorreo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un usuario por su ID.
     * @param id El ID del usuario a buscar.
     * @return El usuario encontrado o null si no existe.
     */
    public Usuario buscarPorId(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return null;
            }

            return usuarioDAO.buscarPorId(id.trim());

        } catch (Exception e) {
            System.err.println("Error en buscarPorId: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuario El objeto Usuario a registrar.
     * @return true si se registró exitosamente, false en caso contrario.
     */
    public boolean registrarUsuario(Usuario usuario) {
        try {
            // Validaciones básicas
            if (usuario == null) {
                System.err.println("ServicioUsuarios: Usuario es null");
                return false;
            }

            if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
                System.err.println("ServicioUsuarios: Correo es requerido");
                return false;
            }

            if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                System.err.println("ServicioUsuarios: Nombre es requerido");
                return false;
            }

            if (usuario.getContrasena() == null || usuario.getContrasena().length() < 6) {
                System.err.println("ServicioUsuarios: Contraseña debe tener al menos 6 caracteres");
                return false;
            }

            // Verificar que no exista un usuario con el mismo correo
            String correoNormalizado = usuario.getCorreo().trim().toLowerCase();
            if (usuarioDAO.buscarPorCorreo(correoNormalizado) != null) {
                System.err.println("ServicioUsuarios: Ya existe un usuario con el correo " + correoNormalizado);
                return false;
            }

            // Normalizar el correo antes de guardar
            usuario.setCorreo(correoNormalizado);

            // Guardar el nuevo usuario
            usuarioDAO.guardar(usuario);
            System.out.println("ServicioUsuarios: Usuario registrado exitosamente - " + usuario.getCorreo());
            return true;

        } catch (Exception e) {
            System.err.println("Error en registrarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza la información de un usuario.
     * @param usuario El usuario con la información actualizada.
     * @return true si se actualizó exitosamente, false en caso contrario.
     */
    public boolean actualizarUsuario(Usuario usuario) {
        try {
            if (usuario == null || usuario.getId() == null) {
                return false;
            }

            boolean resultado = usuarioDAO.actualizar(usuario);

            if (resultado) {
                System.out.println("ServicioUsuarios: Usuario actualizado - " + usuario.getCorreo());
            }

            return resultado;

        } catch (Exception e) {
            System.err.println("Error en actualizarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un usuario del sistema.
     * @param id El ID del usuario a eliminar.
     * @return true si se eliminó exitosamente, false en caso contrario.
     */
    public boolean eliminarUsuario(String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return false;
            }

            boolean resultado = usuarioDAO.eliminar(id.trim());

            if (resultado) {
                System.out.println("ServicioUsuarios: Usuario eliminado - ID: " + id);
            }

            return resultado;

        } catch (Exception e) {
            System.err.println("Error en eliminarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si un correo electrónico ya está registrado.
     * @param correo El correo a verificar.
     * @return true si el correo ya existe, false en caso contrario.
     */
    public boolean existeCorreo(String correo) {
        try {
            return buscarPorCorreo(correo) != null;
        } catch (Exception e) {
            System.err.println("Error en existeCorreo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el total de usuarios registrados.
     * @return El número total de usuarios.
     */
    public int contarUsuarios() {
        try {
            return usuarioDAO.contarUsuarios();
        } catch (Exception e) {
            System.err.println("Error en contarUsuarios: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Obtiene todos los usuarios del sistema.
     * @return Lista de todos los usuarios.
     */
    public java.util.List<Usuario> listarTodos() {
        try {
            return usuarioDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Error en listarTodos: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}