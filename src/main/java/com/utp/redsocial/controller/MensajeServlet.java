package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Mensaje;
import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioMensajeria;
import com.utp.redsocial.services.ServicioUsuarios;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet para manejar las acciones de la mensajería directa.
 */
@WebServlet(name = "MensajeServlet", urlPatterns = {"/MensajeServlet"})
public class MensajeServlet extends HttpServlet {

    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
    private final ServicioMensajeria servicioMensajeria = new ServicioMensajeria(servicioUsuarios);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idOtroUsuario = request.getParameter("idOtroUsuario");
        if (idOtroUsuario == null || idOtroUsuario.isEmpty()) {
            // Si no se especifica con quién chatear, redirigir al dashboard o a una página de inicio de mensajes.
            response.sendRedirect("dashboard.jsp");
            return;
        }

        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        Usuario otroUsuario = servicioUsuarios.buscarPorId(idOtroUsuario);

        if (otroUsuario == null) {
            // Manejar el caso de que el otro usuario no exista
            response.sendRedirect("dashboard.jsp?error=Usuario no encontrado");
            return;
        }

        // Obtener la conversación entre los dos usuarios
        List<Mensaje> conversacion = servicioMensajeria.obtenerConversacion(usuarioLogueado.getId(), idOtroUsuario);

        request.setAttribute("otroUsuario", otroUsuario);
        request.setAttribute("conversacion", conversacion);
        request.setAttribute("paginaActual", "mensajes");
        request.getRequestDispatcher("mensajes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String idReceptor = request.getParameter("idReceptor");
            String textoMensaje = request.getParameter("textoMensaje");
            Usuario emisor = (Usuario) session.getAttribute("usuarioLogueado");

            if (idReceptor != null && textoMensaje != null && !textoMensaje.trim().isEmpty()) {
                servicioMensajeria.enviarMensaje(emisor.getId(), idReceptor, textoMensaje);
            }

            // Redirigir de vuelta a la misma conversación
            response.sendRedirect(request.getContextPath() + "/MensajeServlet?idOtroUsuario=" + idReceptor);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp?error=Error al enviar el mensaje");
        }
    }
}