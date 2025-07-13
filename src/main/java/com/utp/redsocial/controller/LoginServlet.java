package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioUsuarios;
import com.utp.redsocial.services.ServicioMensajeria;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet para manejar el proceso de inicio de sesión de los usuarios.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    // Declarar las variables del servicio, pero NO inicializarlas aquí.
    private ServicioUsuarios servicioUsuarios;
    private ServicioMensajeria servicioMensajeria;

    @Override
    public void init() throws ServletException {
        // Obtener las instancias del servicio desde el contexto de la aplicación
        this.servicioUsuarios = (ServicioUsuarios) getServletContext().getAttribute("servicioUsuarios");
        this.servicioMensajeria = (ServicioMensajeria) getServletContext().getAttribute("servicioMensajeria");

        if (servicioUsuarios == null) {
            throw new ServletException("ServicioUsuarios no está disponible en el contexto de la aplicación");
        }

        // ServicioMensajeria es opcional por ahora
        if (servicioMensajeria == null) {
            System.out.println("LoginServlet: ServicioMensajeria no está disponible (modo básico)");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir a la página de login
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Procesa las peticiones HTTP para el método POST.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        // Verificación de seguridad: si el servicio no se inicializó, no podemos continuar.
        if (servicioUsuarios == null) {
            throw new ServletException("El servicio de usuarios no está disponible. Revisa los logs del servidor.");
        }

        try {
            // Validaciones básicas
            if (correo == null || correo.trim().isEmpty()) {
                request.setAttribute("mensajeError", "El correo es obligatorio");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            if (contrasena == null || contrasena.trim().isEmpty()) {
                request.setAttribute("mensajeError", "La contraseña es obligatoria");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            Usuario usuarioValidado = servicioUsuarios.validarCredenciales(correo, contrasena);

            if (usuarioValidado != null) {
                // Si el usuario es válido:
                HttpSession session = request.getSession(true);

                // Guardamos el objeto Usuario en la sesión con la clave "usuario"
                session.setAttribute("usuario", usuarioValidado);

                // Marcar al usuario como en línea (si el servicio está disponible)
                if (servicioMensajeria != null) {
                    try {
                        servicioMensajeria.marcarUsuarioEnLinea(usuarioValidado.getId());
                        System.out.println("LoginServlet: Usuario marcado como en línea - " + usuarioValidado.getCorreo());
                    } catch (Exception e) {
                        // Error al marcar como en línea, pero no debe impedir el login
                        System.err.println("Error al marcar usuario como en línea: " + e.getMessage());
                    }
                }

                System.out.println("LoginServlet: Login exitoso para " + usuarioValidado.getCorreo());

                // Redirigimos al usuario al dashboard
                response.sendRedirect("dashboard.jsp");

            } else {
                // Si el usuario NO es válido:
                request.setAttribute("mensajeError", "Correo o contraseña incorrectos. Por favor, intente de nuevo.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error en LoginServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensajeError", "Ocurrió un error inesperado. Contacte al administrador.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}