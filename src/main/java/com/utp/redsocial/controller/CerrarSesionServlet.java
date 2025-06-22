package com.utp.redsocial.controller;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet para manejar el proceso de cerrar la sesión de un usuario.
 * Invalida la sesión actual y redirige al usuario a la página de login.
 */
@WebServlet(name = "CerrarSesionServlet", urlPatterns = {"/CerrarSesionServlet"})
public class CerrarSesionServlet extends HttpServlet {

    /**
     * Procesa las peticiones HTTP para el método GET.
     * Es llamado cuando un usuario hace clic en un enlace de "Cerrar Sesión".
     *
     * @param request  objeto que contiene la petición del cliente al servlet.
     * @param response objeto que contiene la respuesta que el servlet envía al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener la sesión actual. El parámetro 'false' es importante
        // para no crear una nueva sesión si no existe una.
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 2. Si la sesión existe, invalidarla.
            // Esto elimina todos los atributos guardados en ella (ej. "usuarioLogueado").
            session.invalidate();
        }

        // 3. Redirigir al usuario a la página de inicio de sesión.
        response.sendRedirect("login.jsp");
    }

    /**
     * El método doPost llama a doGet para manejar la petición de la misma manera.
     * Esto permite que el servlet funcione tanto si se le llama por GET como por POST.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
