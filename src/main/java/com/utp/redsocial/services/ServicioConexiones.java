package com.utp.redsocial.services;

import com.utp.redsocial.entidades.SolicitudConexion;
import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.estructuras.Cola;
import com.utp.redsocial.estructuras.Grafo;
// Asumimos que existe un ConexionDAO para la persistencia
// import com.utp.redsocial.persistencia.ConexionDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Capa de servicio para la lógica de negocio de las Conexiones.
 * Utiliza un Grafo para representar la red social y una Cola para
 * gestionar las solicitudes pendientes.
 */
public class ServicioConexiones {

    private final Grafo<String> grafoConexiones; // El grafo almacena IDs de usuarios
    private final Cola<SolicitudConexion> solicitudesPendientes;
    // private final ConexionDAO conexionDAO; // DAO para persistencia
    private final ServicioUsuarios servicioUsuarios; // Para validar y obtener datos de usuarios

    public ServicioConexiones(ServicioUsuarios servicioUsuarios) {
        this.grafoConexiones = new Grafo<>();
        this.solicitudesPendientes = new Cola<>();
        // this.conexionDAO = new ConexionDAO();
        this.servicioUsuarios = servicioUsuarios;
        // Opcional: Cargar las conexiones existentes desde la BD al iniciar
        // cargarConexiones();
    }

    /**
     * Crea una nueva solicitud de conexión y la encola para ser procesada.
     * @param idSolicitante El ID del usuario que envía la solicitud.
     * @param idDestinatario El ID del usuario que recibe la solicitud.
     * @throws IllegalArgumentException si los usuarios no existen o ya están conectados.
     */
    public void solicitarConexion(String idSolicitante, String idDestinatario) throws IllegalArgumentException {
        // 1. Validar que ambos usuarios existan
        if (servicioUsuarios.buscarPorId(idSolicitante) == null || servicioUsuarios.buscarPorId(idDestinatario) == null) {
            throw new IllegalArgumentException("Uno o ambos usuarios no existen.");
        }

        // 2. Verificar que no sean ya amigos
        if (estanConectados(idSolicitante, idDestinatario)) {
            throw new IllegalArgumentException("Estos usuarios ya están conectados.");
        }

        // 3. Crear la solicitud y encolarla
        SolicitudConexion solicitud = new SolicitudConexion(idSolicitante, idDestinatario, new Date());
        solicitudesPendientes.encolar(solicitud);

        // 4. Persistir la solicitud en la base de datos
        // conexionDAO.crearSolicitud(solicitud);

        System.out.println("Servicio: Solicitud de conexión de " + idSolicitante + " a " + idDestinatario + " encolada.");
    }

    /**
     * Acepta una solicitud de conexión, creando la arista en el grafo.
     * @param idSolicitante El ID del usuario que envió la solicitud.
     * @param idDestinatario El ID del usuario que la acepta.
     */
    public void aceptarConexion(String idSolicitante, String idDestinatario) {
        // En una implementación real, se verificaría que la solicitud existe en la BD

        // 1. Agregar la conexión (arista) en el grafo en ambas direcciones
        grafoConexiones.agregarArista(idSolicitante, idDestinatario);

        // 2. Actualizar la base de datos
        // conexionDAO.aceptarSolicitud(idSolicitante, idDestinatario);

        System.out.println("Servicio: Conexión entre " + idSolicitante + " y " + idDestinatario + " aceptada.");
    }

    /**
     * Elimina una conexión existente del grafo.
     * @param idUsuario1 El ID de uno de los usuarios.
     * @param idUsuario2 El ID del otro usuario.
     */
    public void eliminarConexion(String idUsuario1, String idUsuario2) {
        // Lógica para eliminar la arista del grafo (necesitaría un método en la clase Grafo)
        // grafoConexiones.eliminarArista(idUsuario1, idUsuario2);

        // Eliminar de la base de datos
        // conexionDAO.eliminarConexion(idUsuario1, idUsuario2);

        System.out.println("Servicio: Conexión entre " + idUsuario1 + " y " + idUsuario2 + " eliminada.");
    }

    /**
     * Obtiene la lista de amigos (conexiones) de un usuario.
     * @param idUsuario El ID del usuario.
     * @return Una lista de objetos Usuario que son amigos del usuario dado.
     */
    public List<Usuario> obtenerConexiones(String idUsuario) {
        List<String> idsAmigos = grafoConexiones.obtenerVecinos(idUsuario);
        List<Usuario> amigos = new ArrayList<>();

        for (String idAmigo : idsAmigos) {
            Usuario amigo = servicioUsuarios.buscarPorId(idAmigo);
            if (amigo != null) {
                amigos.add(amigo);
            }
        }
        return amigos;
    }

    /**
     * Recomienda nuevas conexiones para un usuario (amigos de amigos).
     * @param idUsuario El ID del usuario para quien se generan las recomendaciones.
     * @return Una lista de objetos Usuario recomendados.
     */
    public List<Usuario> recomendarConexiones(String idUsuario) {
        Map<String, Integer> sugerencias = grafoConexiones.sugerirConexiones(idUsuario);
        List<Usuario> recomendaciones = new ArrayList<>();

        // Lógica para ordenar las sugerencias y devolver las mejores
        sugerencias.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    Usuario recomendado = servicioUsuarios.buscarPorId(entry.getKey());
                    if (recomendado != null) {
                        recomendaciones.add(recomendado);
                    }
                });

        return recomendaciones;
    }

    /**
     * Verifica si dos usuarios están directamente conectados en el grafo.
     * @param idUsuario1 ID del primer usuario.
     * @param idUsuario2 ID del segundo usuario.
     * @return true si están conectados, false en caso contrario.
     */
    public boolean estanConectados(String idUsuario1, String idUsuario2) {
        return grafoConexiones.obtenerVecinos(idUsuario1).contains(idUsuario2);
    }
}
