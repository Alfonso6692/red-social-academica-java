package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Notificacion;
import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioNotificaciones;
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
 * Servlet para manejar las acciones relacionadas con las Notificaciones.
 */
@WebServlet(name = "NotificacionServlet", urlPatterns = {"/NotificacionServlet"})
public class NotificacionServlet extends HttpServlet {

    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
    private final ServicioNotificaciones servicioNotificaciones = new ServicioNotificaciones(servicioUsuarios);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; // Acción por defecto
        }

        switch (accion) {
            case "listar":
                listarNotificaciones(request, response);
                break;
            case "marcarLeida":
                marcarComoLeida(request, response);
                break;
            default:
                listarNotificaciones(request, response);
        }
    }

    private void listarNotificaciones(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        List<Notificacion> notificaciones = servicioNotificaciones.obtenerNotificacionesRecientes(usuario.getId());

        request.setAttribute("listaNotificaciones", notificaciones);
        request.setAttribute("paginaActual", "notificaciones"); // Para el resaltado del menú
        request.getRequestDispatcher("notificaciones.jsp").forward(request, response);
    }

    private void marcarComoLeida(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idNotificacion = request.getParameter("idNotificacion");
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (idNotificacion != null && !idNotificacion.isEmpty()) {
            servicioNotificaciones.marcarComoLeida(idNotificacion, usuario.getId());
        }

        // Redirigir de vuelta a la lista de notificaciones
        response.sendRedirect(request.getContextPath() + "/NotificacionServlet?accion=listar");
    }
}
