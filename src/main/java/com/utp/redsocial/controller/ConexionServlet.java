package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioConexiones;
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
 * Servlet para manejar todas las acciones relacionadas con las Conexiones entre usuarios.
 * Utiliza un parámetro "accion" para determinar la operación a realizar
 * (solicitar, aceptar, listar, etc.).
 */
@WebServlet(name = "ConexionServlet", urlPatterns = {"/ConexionServlet"})
public class ConexionServlet extends HttpServlet {

    // Se instancian los servicios necesarios.
    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
    private final ServicioConexiones servicioConexiones = new ServicioConexiones(servicioUsuarios);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; // Acción por defecto
        }

        switch (accion) {
            case "listar":
                listarConexiones(request, response);
                break;
            case "aceptar":
                aceptarConexion(request, response);
                break;
            // Otros casos para GET
            default:
                listarConexiones(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            response.sendRedirect("conexiones.jsp");
            return;
        }

        switch (accion) {
            case "solicitar":
                solicitarConexion(request, response);
                break;
            // Otros casos para POST
            default:
                response.sendRedirect("conexiones.jsp");
        }
    }

    private void listarConexiones(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        List<Usuario> conexiones = servicioConexiones.obtenerConexiones(usuario.getId());

        request.setAttribute("listaConexiones", conexiones);
        request.getRequestDispatcher("conexiones.jsp").forward(request, response);
    }

    private void solicitarConexion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String idDestinatario = request.getParameter("idDestinatario");
            Usuario solicitante = (Usuario) session.getAttribute("usuarioLogueado");

            servicioConexiones.solicitarConexion(solicitante.getId(), idDestinatario);

            // Redirigir de vuelta al perfil del usuario al que se le envió la solicitud
            response.sendRedirect("perfil.jsp?id=" + idDestinatario + "&exito=Solicitud enviada");

        } catch (IllegalArgumentException e) {
            String idDestinatario = request.getParameter("idDestinatario");
            response.sendRedirect("perfil.jsp?id=" + idDestinatario + "&error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=Ocurrió un error inesperado");
        }
    }

    private void aceptarConexion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String idSolicitante = request.getParameter("idSolicitante");
            Usuario destinatario = (Usuario) session.getAttribute("usuarioLogueado");

            servicioConexiones.aceptarConexion(idSolicitante, destinatario.getId());

            // Redirigir a la página de notificaciones o conexiones
            response.sendRedirect("notificaciones.jsp?exito=Conexión aceptada");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("notificaciones.jsp?error=Error al aceptar la conexión");
        }
    }
}
