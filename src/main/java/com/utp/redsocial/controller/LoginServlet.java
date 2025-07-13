package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioUsuarios;

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

    // 1. Declaramos la variable del servicio, pero NO la inicializamos aquí.
    private ServicioUsuarios servicioUsuarios;

    @Override
    public void init() throws ServletException {
        // 2. Usamos el método init() del servlet para obtener la instancia del servicio
        // que fue creada y guardada por el InicializadorAplicacion (el Listener).
        this.servicioUsuarios = (ServicioUsuarios) getServletContext().getAttribute("servicioUsuarios");
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
            Usuario usuarioValidado = servicioUsuarios.validarCredenciales(correo, contrasena);

            if (usuarioValidado != null) {
                // Si el usuario es válido:
                HttpSession session = request.getSession(true);

                // Guardamos el objeto Usuario en la sesión con la clave "usuario"
                session.setAttribute("usuario", usuarioValidado);

                // Redirigimos al usuario al dashboard
                response.sendRedirect("dashboard.jsp");

            } else {
                // Si el usuario NO es válido:
                request.setAttribute("mensajeError", "Correo o contraseña incorrectos. Por favor, intente de nuevo.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Ocurrió un error inesperado. Contacte al administrador.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
