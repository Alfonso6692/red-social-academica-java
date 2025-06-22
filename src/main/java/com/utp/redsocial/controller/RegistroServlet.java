package com.utp.redsocial.controller;

import com.utp.redsocial.services.ServicioUsuarios;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet para manejar el proceso de registro de nuevos usuarios.
 * Recibe los datos del formulario, los valida a través del servicio
 * y crea una nueva cuenta de usuario.
 */
@WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
public class RegistroServlet extends HttpServlet {

    // Se instancia el servicio de usuarios que contiene la lógica de negocio.
    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();

    /**
     * Procesa las peticiones HTTP para el método POST.
     * Es llamado cuando el usuario envía el formulario de registro.
     *
     * @param request  objeto que contiene la petición del cliente al servlet.
     * @param response objeto que contiene la respuesta que el servlet envía al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ServletException {

        // 1. Obtener los parámetros del formulario de registro.jsp
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        String carrera = request.getParameter("carrera");
        // Convertir el ciclo a entero, manejando posibles errores
        int ciclo = 0;
        try {
            ciclo = Integer.parseInt(request.getParameter("ciclo"));
        } catch (NumberFormatException e) {
            // Manejar el caso en que el ciclo no es un número válido
            request.setAttribute("mensajeError", "El ciclo ingresado no es válido.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        try {
            // 2. Llamar al servicio para registrar al nuevo usuario
            servicioUsuarios.registrarUsuario(nombre, apellido, correo, contrasena, carrera, ciclo);

            // Si el registro es exitoso:
            // 1. Establecer un mensaje de éxito para mostrar en la página de login
            request.setAttribute("mensajeExito", "¡Registro exitoso! Ahora puedes iniciar sesión.");

            // 2. Reenviar al usuario a la página de login
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // Si el servicio lanza una excepción (ej. correo ya existe, datos inválidos):

            // 1. Establecer el mensaje de error que viene desde la capa de servicio
            request.setAttribute("mensajeError", e.getMessage());

            // 2. Reenviar de vuelta al formulario de registro para que el usuario corrija los datos
            request.getRequestDispatcher("registro.jsp").forward(request, response);

        } catch (Exception e) {
            // Manejo de cualquier otro error inesperado
            e.printStackTrace();
            request.setAttribute("mensajeError", "Ocurrió un error inesperado durante el registro.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
