package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioUsuarios;
import com.utp.redsocial.util.GeneradorID; // Asumiendo que tienes esta clase

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet para manejar el registro de nuevos usuarios.
 */
@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {

    // 1. Declaramos la variable del servicio, pero NO la inicializamos aquí.
    private ServicioUsuarios servicioUsuarios;

    @Override
    public void init() throws ServletException {
        // 2. Usamos el método init() para obtener la instancia del servicio
        // que fue creada por el InicializadorAplicacion (el Listener).
        this.servicioUsuarios = (ServicioUsuarios) getServletContext().getAttribute("servicioUsuarios");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificación de seguridad
        if (servicioUsuarios == null) {
            throw new ServletException("El servicio de usuarios no está disponible. Revisa los logs del servidor.");
        }

        // Obtener los datos del formulario de registro.jsp
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        String carrera = request.getParameter("carrera");
        int ciclo = Integer.parseInt(request.getParameter("ciclo")); // Convertir a entero

        // Crear un nuevo objeto Usuario
        String id = GeneradorID.generar();
        Usuario nuevoUsuario = new Usuario(id, nombre, apellido, correo, contrasena, carrera, ciclo);

        try {
            // Llamar al servicio para registrar al usuario
            // Necesitarás un método "registrarUsuario" en tu ServicioUsuarios
            servicioUsuarios.registrarUsuario(nuevoUsuario);

            // Redirigir a la página de login con un mensaje de éxito
            response.sendRedirect("login.jsp?registro=exitoso");

        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, reenviar a la página de registro con un mensaje
            request.setAttribute("mensajeError", "No se pudo completar el registro: " + e.getMessage());
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
