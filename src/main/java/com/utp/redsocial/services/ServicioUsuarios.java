package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.persistencia.UsuarioDAO;
import com.utp.redsocial.util.GeneradorID; // Suponiendo que tienes esta clase de utilidad
import com.utp.redsocial.util.ValidadorDatos; // Suponiendo que tienes esta clase de utilidad

import java.util.HashMap;
import java.util.Map;

/**
 * Capa de servicio para la lógica de negocio de los Usuarios.
 * Orquesta las operaciones llamando al DAO correspondiente y aplicando
 * las reglas de negocio.
 */
public class ServicioUsuarios {

    private final UsuarioDAO usuarioDAO;
    // Usamos un HashMap como caché para búsquedas rápidas de usuarios en memoria.
    private final Map<String, Usuario> usuariosEnMemoria;

    public ServicioUsuarios() {
        this.usuarioDAO = new UsuarioDAO();
        this.usuariosEnMemoria = new HashMap<>();
        // Opcional: Cargar algunos usuarios al iniciar el servicio
        // cargarUsuariosEnMemoria();
    }

    /**
     * Valida las credenciales de un usuario para el inicio de sesión.
     * @param correo El correo del usuario.
     * @param contrasena La contraseña ingresada.
     * @return El objeto Usuario si las credenciales son correctas, de lo contrario null.
     */
    public Usuario validarCredenciales(String correo, String contrasena) {
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo);

        // En un proyecto real, se compara una contraseña hasheada.
        // Aquí se hace una comparación de texto plano por simplicidad.
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            // Guardar en caché para acceso rápido futuro
            usuariosEnMemoria.put(usuario.getId(), usuario);
            return usuario;
        }
        return null;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @return El objeto Usuario creado si el registro es exitoso.
     * @throws IllegalArgumentException si los datos son inválidos o el correo ya existe.
     */
    public Usuario registrarUsuario(String nombre, String apellido, String correo, String contrasena, String carrera, int ciclo) throws IllegalArgumentException {
        // 1. Validar los datos de entrada
        if (!ValidadorDatos.esCorreoValido(correo)) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
        if (contrasena == null || contrasena.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }

        // 2. Verificar si el usuario ya existe
        if (usuarioDAO.buscarPorCorreo(correo) != null) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        // 3. Crear el usuario
        String id = GeneradorID.generar(); // Generar un ID único

        // En un proyecto real, la contraseña se "hashea" antes de guardarla.
        // String contrasenaHasheada = Hasher.hashPassword(contrasena);
        Usuario nuevoUsuario = new Usuario(id, nombre, apellido, correo, contrasena, carrera, ciclo);

        // 4. Persistir el usuario en la base de datos
        usuarioDAO.guardar(nuevoUsuario);
        System.out.println("Servicio: Usuario registrado con éxito.");

        // 5. Añadir a la caché en memoria
        usuariosEnMemoria.put(id, nuevoUsuario);

        return nuevoUsuario;
    }

    /**
     * Busca un usuario por su ID.
     * Primero busca en la caché en memoria y, si no lo encuentra, consulta la base de datos.
     * @param id El ID del usuario.
     * @return El objeto Usuario o null si no se encuentra.
     */
    public Usuario buscarPorId(String id) {
        // Búsqueda rápida en caché
        if (usuariosEnMemoria.containsKey(id)) {
            return usuariosEnMemoria.get(id);
        }

        // Si no está en caché, buscar en la BD
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario != null) {
            usuariosEnMemoria.put(id, usuario); // Guardar en caché para el futuro
        }
        return usuario;
    }

    /**
     * Actualiza la información de un usuario.
     * @param usuario El objeto Usuario con la información actualizada.
     */
    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
        // Actualizar también la caché
        usuariosEnMemoria.put(usuario.getId(), usuario);
        System.out.println("Servicio: Usuario actualizado.");
    }

    /**
     * Elimina un usuario del sistema.
     * @param id El ID del usuario a eliminar.
     */
    public void eliminarUsuario(String id) {
        usuarioDAO.eliminar(id);
        // Eliminar también de la caché
        usuariosEnMemoria.remove(id);
        System.out.println("Servicio: Usuario eliminado.");
    }
}
