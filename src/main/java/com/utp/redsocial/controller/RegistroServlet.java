package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioUsuarios;
import com.utp.redsocial.util.GeneradorID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {

    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ... obtener parámetros del request ...
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        String carrera = request.getParameter("carrera");
        int ciclo = Integer.parseInt(request.getParameter("ciclo"));

        String id = GeneradorID.generar();
        Usuario nuevoUsuario = new Usuario(id, nombre, apellido, correo, contrasena, carrera, ciclo);

        try {
            // CORRECCIÓN: El método registrarUsuario no devuelve nada, solo se ejecuta.
            servicioUsuarios.registrarUsuario(nuevoUsuario);

            // Si no hubo excepción, el registro fue exitoso.
            response.sendRedirect("login.jsp?registro=exitoso");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "No se pudo completar el registro: " + e.getMessage());
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
