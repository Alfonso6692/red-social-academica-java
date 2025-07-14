package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.persistencia.UsuarioDAO;

public class ServicioUsuarios {

    // El servicio crea su propia instancia del DAO.
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // El constructor por defecto ahora es suficiente.
    public ServicioUsuarios() {}

    public Usuario validarCredenciales(String correo, String contrasena) {
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        return null;
    }

    public Usuario buscarPorId(String id) {
        return usuarioDAO.buscarPorId(id);
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioDAO.buscarPorCorreo(correo);
    }

    public void registrarUsuario(Usuario nuevoUsuario) throws Exception {
        if (usuarioDAO.buscarPorCorreo(nuevoUsuario.getCorreo()) != null) {
            throw new Exception("El correo electrónico ya está registrado.");
        }
        usuarioDAO.guardar(nuevoUsuario);
    }

    // Método para actualizar un usuario, que otros servicios podrían necesitar.
    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
    }
}
