package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioMensajeria;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet para manejar las acciones de la mensajería directa.
 */
@WebServlet(name = "MensajeServlet", urlPatterns = {"/MensajeServlet"})
public class MensajeServlet extends HttpServlet {

    // El servlet crea su propia instancia del servicio.
    private final ServicioMensajeria servicioMensajeria = new ServicioMensajeria();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        // Validar que haya un usuario en sesión
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener los datos del formulario de envío de mensaje
        String idReceptor = request.getParameter("idReceptor");
        String textoMensaje = request.getParameter("textoMensaje");
        Usuario emisor = (Usuario) session.getAttribute("usuario");

        try {
            // Validar que los datos necesarios no sean nulos
            if (idReceptor != null && !idReceptor.isEmpty() && textoMensaje != null && !textoMensaje.isEmpty()) {

                // CORRECCIÓN: Llamamos al método del servicio con los parámetros correctos.
                servicioMensajeria.enviarMensaje(emisor.getId(), idReceptor, textoMensaje);
            }

            // Redirigir de vuelta a la conversación para que se vea el nuevo mensaje.
            // Se añade el ID del receptor para saber qué conversación mostrar.
            response.sendRedirect(request.getContextPath() + "/MensajeServlet?accion=ver&idUsuario2=" + idReceptor);

        } catch (Exception e) {
            e.printStackTrace();
            // Manejar el error, quizás redirigiendo a una página de error.
            response.sendRedirect("dashboard.jsp?error=enviarMensaje");
        }
    }

    // Aquí iría el método doGet para manejar la visualización de conversaciones (accion="ver")
}
