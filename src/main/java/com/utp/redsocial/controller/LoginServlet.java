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
 * Recibe las credenciales, las valida a través del servicio de usuarios
 * y gestiona la sesión.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    // Se instancia el servicio de usuarios que contiene la lógica de negocio.
    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();

    /**
     * Procesa las peticiones HTTP para el método POST.
     * Es llamado cuando el usuario envía el formulario de login.
     *
     * @param request  objeto que contiene la petición del cliente al servlet.
     * @param response objeto que contiene la respuesta que el servlet envía al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener los parámetros del formulario de login.jsp
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        try {
            // 2. Llamar al servicio para validar las credenciales
            Usuario usuarioValidado = servicioUsuarios.validarCredenciales(correo, contrasena);

            if (usuarioValidado != null) {
                // Si el usuario es válido:

                // 3. Crear una nueva sesión HTTP si no existe
                HttpSession session = request.getSession(true);

                // 4. Guardar el objeto Usuario en la sesión
                // Esto permite acceder a los datos del usuario en otras páginas (ej. dashboard.jsp)
                session.setAttribute("usuarioLogueado", usuarioValidado);

                // 5. Redirigir al usuario a la página principal o dashboard
                response.sendRedirect("dashboard.jsp");

            } else {
                // Si el usuario NO es válido:

                // 1. Establecer un mensaje de error
                request.setAttribute("mensajeError", "Correo o contraseña incorrectos. Por favor, intente de nuevo.");

                // 2. Reenviar la petición de vuelta a la página de login para mostrar el error
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            // Manejo de cualquier otro error inesperado durante el proceso
            e.printStackTrace();
            request.setAttribute("mensajeError", "Ocurrió un error inesperado. Contacte al administrador.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
