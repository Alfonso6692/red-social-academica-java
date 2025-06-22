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
 * Servlet para manejar la visualización de perfiles de usuario.
 */
@WebServlet(name = "PerfilServlet", urlPatterns = {"/PerfilServlet"})
public class PerfilServlet extends HttpServlet {

    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener el usuario que ha iniciado sesión
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        // Determinar qué perfil mostrar
        String idPerfilAMostrar = request.getParameter("id");

        Usuario usuarioDelPerfil;

        if (idPerfilAMostrar == null || idPerfilAMostrar.isEmpty() || idPerfilAMostrar.equals(usuarioLogueado.getId())) {
            // Si no se especifica un ID o es el del propio usuario, mostrar "Mi Perfil"
            usuarioDelPerfil = usuarioLogueado;
            request.setAttribute("esMiPerfil", true);
        } else {
            // Si se especifica un ID, buscar a ese usuario
            usuarioDelPerfil = servicioUsuarios.buscarPorId(idPerfilAMostrar);
            request.setAttribute("esMiPerfil", false);
        }

        // Enviar el objeto usuario a la página JSP
        request.setAttribute("usuarioPerfil", usuarioDelPerfil);
        request.setAttribute("paginaActual", "perfil"); // Para el menú
        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }
}