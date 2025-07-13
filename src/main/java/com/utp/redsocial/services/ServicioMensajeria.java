package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.entidades.Mensaje;
import com.utp.redsocial.persistencia.MensajeDAO;
import com.utp.redsocial.util.GeneradorID;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para manejar el sistema de mensajería entre usuarios
 */
public class ServicioMensajeria {

    private final Map<String, Boolean> usuariosEnLinea; // Simulación de usuarios en línea
    private final MensajeDAO mensajeDAO;
    private final ServicioUsuarios servicioUsuarios;

    public ServicioMensajeria(ServicioUsuarios servicioUsuarios) {
        this.usuariosEnLinea = new HashMap<>();
        this.mensajeDAO = new MensajeDAO();
        this.servicioUsuarios = servicioUsuarios;
        System.out.println("ServicioMensajeria: Inicializado correctamente");
    }

    /**
     * Envía un mensaje de un usuario a otro.
     * Si el receptor está desconectado, el mensaje se encola.
     * @param idEmisor El ID del usuario que envía el mensaje.
     * @param idReceptor El ID del usuario que recibe el mensaje.
     * @param texto El contenido del mensaje.
     */
    public void enviarMensaje(String idEmisor, String idReceptor, String texto) {
        try {
            // 1. Validar que ambos usuarios existan
            Usuario emisor = servicioUsuarios.buscarPorId(idEmisor);
            Usuario receptor = servicioUsuarios.buscarPorId(idReceptor);

            if (emisor == null || receptor == null) {
                throw new IllegalArgumentException("El emisor o el receptor del mensaje no existen.");
            }

            // 2. Crear el objeto Mensaje
            String idMensaje = GeneradorID.generar();
            Mensaje mensaje = new Mensaje(idMensaje, texto, idEmisor, idReceptor, LocalDateTime.now(), false);

            // 3. Persistir el mensaje inmediatamente en la BD
            mensajeDAO.guardar(mensaje);

            // 4. Verificar si el receptor está en línea para decidir si notificar
            if (isUsuarioEnLinea(idReceptor)) {
                // El receptor está en línea, podríamos enviar notificación inmediata
                System.out.println("ServicioMensajeria: Mensaje enviado inmediatamente a " + receptor.getNombreCompleto());
            } else {
                // El receptor está desconectado, el mensaje queda guardado para cuando se conecte
                System.out.println("ServicioMensajeria: Mensaje guardado para " + receptor.getNombreCompleto() + " (desconectado)");
            }

        } catch (Exception e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al enviar mensaje", e);
        }
    }

    /**
     * Obtiene todos los mensajes entre dos usuarios.
     * @param idUsuario1 ID del primer usuario.
     * @param idUsuario2 ID del segundo usuario.
     * @return Lista de mensajes entre ambos usuarios.
     */
    public List<Mensaje> obtenerConversacion(String idUsuario1, String idUsuario2) {
        try {
            return mensajeDAO.obtenerConversacion(idUsuario1, idUsuario2);
        } catch (Exception e) {
            System.err.println("Error al obtener conversación: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Obtiene todos los mensajes recibidos por un usuario.
     * @param idUsuario ID del usuario.
     * @return Lista de mensajes recibidos.
     */
    public List<Mensaje> obtenerMensajesRecibidos(String idUsuario) {
        try {
            return mensajeDAO.obtenerMensajesPorReceptor(idUsuario);
        } catch (Exception e) {
            System.err.println("Error al obtener mensajes recibidos: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Obtiene todos los mensajes enviados por un usuario.
     * @param idUsuario ID del usuario.
     * @return Lista de mensajes enviados.
     */
    public List<Mensaje> obtenerMensajesEnviados(String idUsuario) {
        try {
            return mensajeDAO.obtenerMensajesPorEmisor(idUsuario);
        } catch (Exception e) {
            System.err.println("Error al obtener mensajes enviados: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Marca un mensaje como leído.
     * @param idMensaje ID del mensaje.
     */
    public void marcarComoLeido(String idMensaje) {
        try {
            mensajeDAO.marcarComoLeido(idMensaje);
            System.out.println("ServicioMensajeria: Mensaje marcado como leído - ID: " + idMensaje);
        } catch (Exception e) {
            System.err.println("Error al marcar mensaje como leído: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cuenta los mensajes no leídos de un usuario.
     * @param idUsuario ID del usuario.
     * @return Número de mensajes no leídos.
     */
    public int contarMensajesNoLeidos(String idUsuario) {
        try {
            return mensajeDAO.contarMensajesNoLeidos(idUsuario);
        } catch (Exception e) {
            System.err.println("Error al contar mensajes no leídos: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Marca a un usuario como en línea.
     * @param idUsuario ID del usuario.
     */
    public void marcarUsuarioEnLinea(String idUsuario) {
        usuariosEnLinea.put(idUsuario, true);

        // También actualizar el campo enLinea en la entidad Usuario
        Usuario usuario = servicioUsuarios.buscarPorId(idUsuario);
        if (usuario != null) {
            usuario.setEnLinea(true);
            servicioUsuarios.actualizarUsuario(usuario);
        }

        System.out.println("ServicioMensajeria: Usuario marcado como en línea - ID: " + idUsuario);
    }

    /**
     * Marca a un usuario como desconectado.
     * @param idUsuario ID del usuario.
     */
    public void marcarUsuarioDesconectado(String idUsuario) {
        usuariosEnLinea.put(idUsuario, false);

        // También actualizar el campo enLinea en la entidad Usuario
        Usuario usuario = servicioUsuarios.buscarPorId(idUsuario);
        if (usuario != null) {
            usuario.setEnLinea(false);
            servicioUsuarios.actualizarUsuario(usuario);
        }

        System.out.println("ServicioMensajeria: Usuario marcado como desconectado - ID: " + idUsuario);
    }

    /**
     * Verifica si un usuario está en línea.
     * @param idUsuario ID del usuario.
     * @return true si el usuario está en línea, false en caso contrario.
     */
    public boolean isUsuarioEnLinea(String idUsuario) {
        // Primero verificar en el mapa en memoria
        Boolean enLinea = usuariosEnLinea.get(idUsuario);
        if (enLinea != null) {
            return enLinea;
        }

        // Si no está en el mapa, verificar en la base de datos
        Usuario usuario = servicioUsuarios.buscarPorId(idUsuario);
        return usuario != null && usuario.isEnLinea();
    }

    /**
     * Obtiene la lista de usuarios actualmente en línea.
     * @return Lista de IDs de usuarios en línea.
     */
    public List<String> obtenerUsuariosEnLinea() {
        return usuariosEnLinea.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Elimina un mensaje (soft delete).
     * @param idMensaje ID del mensaje a eliminar.
     * @return true si se eliminó exitosamente, false en caso contrario.
     */
    public boolean eliminarMensaje(String idMensaje) {
        try {
            return mensajeDAO.eliminar(idMensaje);
        } catch (Exception e) {
            System.err.println("Error al eliminar mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}