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
 * Servlet para manejar el cierre de sesión de los usuarios
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    private ServicioMensajeria servicioMensajeria;

    @Override
    public void init() throws ServletException {
        // Obtener la instancia del servicio desde el contexto de la aplicación
        this.servicioMensajeria = (ServicioMensajeria) getServletContext().getAttribute("servicioMensajeria");

        // ServicioMensajeria es opcional
        if (servicioMensajeria == null) {
            System.out.println("LogoutServlet: ServicioMensajeria no está disponible (modo básico)");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarCierreSesion(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarCierreSesion(request, response);
    }

    /**
     * Procesa el cierre de sesión del usuario
     */
    private void procesarCierreSesion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            // Obtener la sesión actual
            HttpSession session = request.getSession(false);

            if (session != null) {
                // Obtener el usuario de la sesión antes de cerrarla
                Usuario usuario = (Usuario) session.getAttribute("usuario");

                if (usuario != null) {
                    // Marcar al usuario como desconectado (si el servicio está disponible)
                    if (servicioMensajeria != null) {
                        try {
                            servicioMensajeria.marcarUsuarioDesconectado(usuario.getId());
                            System.out.println("LogoutServlet: Usuario marcado como desconectado - " + usuario.getCorreo());
                        } catch (Exception e) {
                            // Error al marcar como desconectado, pero no debe impedir el logout
                            System.err.println("Error al marcar usuario como desconectado: " + e.getMessage());
                        }
                    }

                    System.out.println("LogoutServlet: Sesión cerrada para " + usuario.getCorreo());
                } else {
                    System.out.println("LogoutServlet: Sesión cerrada (sin usuario en sesión)");
                }

                // Invalidar la sesión
                session.invalidate();
            } else {
                System.out.println("LogoutServlet: No había sesión activa");
            }

            // Redirigir al login con mensaje de éxito
            response.sendRedirect("login.jsp?mensaje=Sesión cerrada exitosamente");

        } catch (Exception e) {
            System.err.println("Error en LogoutServlet: " + e.getMessage());
            e.printStackTrace();

            // En caso de error, aún así redirigir al login
            response.sendRedirect("login.jsp?error=Error al cerrar sesión");
        }
    }
}