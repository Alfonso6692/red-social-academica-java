package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Notificacion;
import com.utp.redsocial.estructuras.Pila;
import com.utp.redsocial.persistencia.NotificacionDAO;
import com.utp.redsocial.util.GeneradorID;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de servicio para la lógica de negocio de las Notificaciones.
 * Utiliza una Pila para gestionar las notificaciones de cada usuario (LIFO).
 */
public class ServicioNotificaciones {

    private final Map<String, Pila<Notificacion>> notificacionesPorUsuario; // K: idUsuario, V: Pila de notificaciones
    private final NotificacionDAO notificacionDAO;
    private final ServicioUsuarios servicioUsuarios;

    public ServicioNotificaciones(ServicioUsuarios servicioUsuarios) {
        this.notificacionesPorUsuario = new HashMap<>();
        this.notificacionDAO = new NotificacionDAO();
        this.servicioUsuarios = servicioUsuarios;
    }

    /**
     * Crea y envía una nueva notificación a un usuario.
     * @param idUsuario El ID del usuario que recibirá la notificación.
     * @param tipo El tipo de notificación (ej. "NUEVA_CONEXION", "NUEVO_RECURSO").
     * @param mensaje El contenido de la notificación.
     * @param idReferencia El ID del objeto relacionado a la notificación (ej. ID del usuario que envió solicitud).
     */
    public void crearNotificacion(String idUsuario, String tipo, String mensaje, String idReferencia) {
        if (servicioUsuarios.buscarPorId(idUsuario) == null) {
            throw new IllegalArgumentException("El usuario para la notificación no existe.");
        }

        String idNotificacion = GeneradorID.generar();
        Notificacion notificacion = new Notificacion(idNotificacion, idUsuario, tipo, mensaje, idReferencia, LocalDateTime.now(), false);

        // 1. Guardar en la base de datos
        notificacionDAO.guardar(notificacion);

        // 2. Añadir a la Pila en memoria del usuario
        Pila<Notificacion> pilaUsuario = notificacionesPorUsuario.computeIfAbsent(idUsuario, k -> new Pila<>());
        pilaUsuario.push(notificacion);

        System.out.println("Servicio: Notificación creada para el usuario " + idUsuario);
    }

    /**
     * Obtiene las notificaciones más recientes de un usuario.
     * @param idUsuario El ID del usuario.
     * @return Una lista con las notificaciones no leídas más recientes.
     */
    public List<Notificacion> obtenerNotificacionesRecientes(String idUsuario) {
        // En una implementación real, se cargarían desde la BD si no están en memoria
        Pila<Notificacion> pilaUsuario = notificacionesPorUsuario.get(idUsuario);
        if (pilaUsuario == null || pilaUsuario.estaVacia()) {
            return new ArrayList<>();
        }

        // Se muestran las últimas sin sacarlas de la pila principal
        List<Notificacion> recientes = new ArrayList<>();
        Pila<Notificacion> pilaTemporal = new Pila<>();

        while (!pilaUsuario.estaVacia()) {
            Notificacion notif = pilaUsuario.pop();
            if (!notif.isLeida()) {
                recientes.add(notif);
            }
            pilaTemporal.push(notif);
        }

        // Restaurar la pila original
        while (!pilaTemporal.estaVacia()) {
            pilaUsuario.push(pilaTemporal.pop());
        }

        return recientes;
    }

    /**
     * Marca una notificación específica como leída.
     * @param idNotificacion El ID de la notificación.
     */
    public void marcarComoLeida(String idNotificacion, String idUsuario) {
        // 1. Actualizar en la base de datos
        notificacionDAO.actualizarEstadoLeida(idNotificacion, true);

        // 2. Actualizar en la pila en memoria (más complejo, requiere buscar y reconstruir la pila)
        Pila<Notificacion> pilaUsuario = notificacionesPorUsuario.get(idUsuario);
        if (pilaUsuario != null) {
            Pila<Notificacion> pilaAuxiliar = new Pila<>();
            boolean encontrada = false;
            while(!pilaUsuario.estaVacia()) {
                Notificacion notif = pilaUsuario.pop();
                if (notif.getId().equals(idNotificacion) && !encontrada) {
                    notif.setLeida(true);
                    encontrada = true;
                }
                pilaAuxiliar.push(notif);
            }
            // Restaurar pila
            while(!pilaAuxiliar.estaVacia()) {
                pilaUsuario.push(pilaAuxiliar.pop());
            }
        }
        System.out.println("Servicio: Notificación " + idNotificacion + " marcada como leída.");
    }
}
