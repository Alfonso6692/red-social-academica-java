package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.persistencia.UsuarioDAO;

/**
 * Capa de servicio para la lógica de negocio de los Usuarios.
 * Actúa como intermediario entre los Servlets y los DAOs.
 */
public class ServicioUsuarios {

    // 1. La variable se declara 'final' para asegurar que se asigne en el constructor.
    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor del servicio.
     * Recibe una instancia del DAO (su dependencia) para poder funcionar.
     * Este constructor será llamado por el InicializadorObjetoGlobal.
     */
    public ServicioUsuarios() {
        // 2. Se asigna el DAO recibido a la variable de la clase.
        this.usuarioDAO = new UsuarioDAO(); // ✅ CORREGIDO: Crear nueva instancia
    }

    /**
     * Valida las credenciales de un usuario.
     * @param correo El correo electrónico ingresado.
     * @param contrasena La contraseña ingresada.
     * @return El objeto Usuario si las credenciales son correctas, de lo contrario null.
     */
    public Usuario validarCredenciales(String correo, String contrasena) {
        // Ahora, usuarioDAO no será nulo porque fue asignado en el constructor.
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        return null;
    }

    /**
     * Busca un usuario por su ID.
     * @param id El ID del usuario a buscar.
     * @return El usuario encontrado o null si no existe.
     */
    public Usuario buscarPorId(String id) {
        return usuarioDAO.buscarPorId(id);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuario El objeto Usuario a registrar.
     * @return true si se registró exitosamente, false en caso contrario.
     */
    public boolean registrarUsuario(Usuario usuario) {
        // Verificar que no exista un usuario con el mismo correo
        if (usuarioDAO.buscarPorCorreo(usuario.getCorreo()) != null) {
            return false; // Ya existe un usuario con ese correo
        }

        // Guardar el nuevo usuario
        usuarioDAO.guardar(usuario);
        return true;
    }

    /**
     * Actualiza la información de un usuario.
     * @param usuario El usuario con la información actualizada.
     * @return true si se actualizó exitosamente, false en caso contrario.
     */
    public boolean actualizarUsuario(Usuario usuario) {
        return usuarioDAO.actualizar(usuario);
    }
}