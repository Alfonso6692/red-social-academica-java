package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Mensaje;
import com.utp.redsocial.persistencia.MensajeDAO;
import com.utp.redsocial.util.GeneradorID; // Asegúrate de tener esta clase de utilidad

import java.util.List;

/**
 * Capa de servicio para la lógica de negocio de la Mensajería.
 */
public class ServicioMensajeria {

    // El servicio crea su propia instancia del DAO.
    private final MensajeDAO mensajeDAO = new MensajeDAO();

    /**
     * Constructor por defecto.
     */
    public ServicioMensajeria() {
    }

    /**
     * Procesa y guarda un nuevo mensaje.
     * Recibe los datos básicos, crea el objeto Mensaje y lo persiste.
     * @param idEmisor El ID del usuario que envía el mensaje.
     * @param idReceptor El ID del usuario que recibe el mensaje.
     * @param texto El contenido del mensaje.
     */
    public void enviarMensaje(String idEmisor, String idReceptor, String texto) {
        // 1. Validar que los datos no sean nulos o vacíos.
        if (idEmisor == null || idReceptor == null || texto == null || texto.trim().isEmpty()) {
            System.err.println("Error: No se puede enviar un mensaje con datos incompletos.");
            return; // No continúa si los datos son inválidos
        }

        // 2. Crear un nuevo objeto Mensaje.
        String idMensaje = GeneradorID.generar();
        Mensaje nuevoMensaje = new Mensaje();

        // 3. Llamar al DAO para persistir el mensaje en la base de datos.
        mensajeDAO.guardar(nuevoMensaje);
        System.out.println("Servicio: Mensaje de " + idEmisor + " a " + idReceptor + " guardado.");
    }

    /**
     * Obtiene la conversación completa entre dos usuarios.
     * @param idUsuario1 El ID del primer usuario.
     * @param idUsuario2 El ID del segundo usuario.
     * @return Una lista de objetos Mensaje.
     */
    public List<Mensaje> obtenerConversacion(String idUsuario1, String idUsuario2) {
        return mensajeDAO.obtenerConversacion(idUsuario1, idUsuario2);
    }

    public void marcarUsuarioEnLinea(String id) {
    }

    public void marcarUsuarioDesconectado(String id) {

    }
}
