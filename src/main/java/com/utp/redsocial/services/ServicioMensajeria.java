// --- ServicioMensajeria.java ---
package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Mensaje;
import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.estructuras.Cola;
import com.utp.redsocial.persistencia.MensajeDAO;
import com.utp.redsocial.util.GeneradorID;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Capa de servicio para la lógica de negocio de la Mensajería.
 * Utiliza una Cola para gestionar los mensajes pendientes de usuarios desconectados.
 */
public class ServicioMensajeria {

    private final Map<String, Cola<Mensaje>> mensajesPendientesPorUsuario; // K: idReceptor, V: Cola de mensajes
    private final MensajeDAO mensajeDAO;
    private final ServicioUsuarios servicioUsuarios;

    public ServicioMensajeria(ServicioUsuarios servicioUsuarios) {
        this.mensajesPendientesPorUsuario = new HashMap<>();
        this.mensajeDAO = new MensajeDAO();
        this.servicioUsuarios = servicioUsuarios;
    }

    /**
     * Envía un mensaje de un usuario a otro.
     * Si el receptor está desconectado, el mensaje se encola.
     * @param idEmisor El ID del usuario que envía el mensaje.
     * @param idReceptor El ID del usuario que recibe el mensaje.
     * @param texto El contenido del mensaje.
     */
    public void enviarMensaje(String idEmisor, String idReceptor, String texto) {
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

        // 4. Verificar si el receptor está en línea para decidir si encolar
        if (!receptor.isEnLinea()) {
            // Obtiene la cola de mensajes del receptor, o crea una nueva si no existe
            Cola<Mensaje> colaReceptor = mensajesPendientesPorUsuario.computeIfAbsent(idReceptor, k -> new Cola<>());
            colaReceptor.encolar(mensaje);
            System.out.println("Servicio: Mensaje para " + idReceptor + " encolado (usuario desconectado).");
        } else {
            System.out.println("Servicio: Mensaje para " + idReceptor + " entregado directamente (usuario conectado).");
            // Aquí iría la lógica para enviar una notificación en tiempo real (ej. WebSockets)
        }
    }

    /**
     * Procesa y entrega los mensajes pendientes de un usuario cuando se conecta.
     * @param idUsuario El ID del usuario que se ha conectado.
     * @return Una lista de mensajes pendientes que fueron entregados.
     */
    public List<Mensaje> entregarMensajesPendientes(String idUsuario) {
        Cola<Mensaje> colaMensajes = mensajesPendientesPorUsuario.get(idUsuario);
        if (colaMensajes == null || colaMensajes.estaVacia()) {
            return new ArrayList<>(); // No hay mensajes pendientes
        }

        List<Mensaje> mensajesEntregados = new ArrayList<>();
        while (!colaMensajes.estaVacia()) {
            Mensaje mensaje = colaMensajes.desencolar();
            mensajeDAO.actualizarEstadoLeido(mensaje.getId(), true); // Marcar como leído en la BD
            mensajesEntregados.add(mensaje);
        }

        // Limpiar la cola de la memoria una vez entregados
        mensajesPendientesPorUsuario.remove(idUsuario);
        System.out.println("Servicio: Entregados " + mensajesEntregados.size() + " mensajes pendientes a " + idUsuario);
        return mensajesEntregados;
    }

    /**
     * Obtiene la conversación histórica entre dos usuarios.
     * @param idUsuario1 ID del primer usuario.
     * @param idUsuario2 ID del segundo usuario.
     * @return Lista de mensajes entre los dos usuarios.
     */
    public List<Mensaje> obtenerConversacion(String idUsuario1, String idUsuario2) {
        return mensajeDAO.obtenerConversacion(idUsuario1, idUsuario2);
    }
}