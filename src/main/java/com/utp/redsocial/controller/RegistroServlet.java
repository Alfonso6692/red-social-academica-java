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

    private ServicioUsuarios servicioUsuarios;

    @Override
    public void init() throws ServletException {
        // Obtener la instancia del servicio desde el contexto de la aplicación
        this.servicioUsuarios = (ServicioUsuarios) getServletContext().getAttribute("servicioUsuarios");

        if (servicioUsuarios == null) {
            throw new ServletException("ServicioUsuarios no está disponible en el contexto de la aplicación");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir a la página de registro
        request.getRequestDispatcher("/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            String confirmarContrasena = request.getParameter("confirmarContrasena");
            String carrera = request.getParameter("carrera");
            String ciclo = request.getParameter("ciclo");

            // 2. Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty()) {
                request.setAttribute("mensajeError", "El nombre es obligatorio");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
                return;
            }

            if (apellido == null || apellido.trim().isEmpty()) {
                request.setAttribute("mensajeError", "El apellido es obligatorio");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
                return;
            }

            if (correo == null || correo.trim().isEmpty()) {
                request.setAttribute("mensajeError", "El correo es obligatorio");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
                return;
            }

            if (contrasena == null || contrasena.trim().isEmpty()) {
                request.setAttribute("mensajeError", "La contraseña es obligatoria");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
                return;
            }

            if (contrasena.length() < 6) {
                request.setAttribute("mensajeError", "La contraseña debe tener al menos 6 caracteres");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
                return;
            }

            if (!contrasena.equals(confirmarContrasena)) {
                request.setAttribute("mensajeError", "Las contraseñas no coinciden");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
                return;
            }

            // 3. Verificar que no exista un usuario con el mismo correo
            Usuario usuarioExistente = servicioUsuarios.buscarPorCorreo(correo.trim());
            if (usuarioExistente != null) {
                request.setAttribute("mensajeError", "Ya existe una cuenta con este correo electrónico");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
                return;
            }

            // 4. Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setId(GeneradorID.generar());
            nuevoUsuario.setNombre(nombre.trim());
            nuevoUsuario.setApellido(apellido.trim());
            nuevoUsuario.setCorreo(correo.trim().toLowerCase());
            nuevoUsuario.setContrasena(contrasena); // En producción, usar hash de la contraseña

            // Campos opcionales
            if (carrera != null && !carrera.trim().isEmpty()) {
                nuevoUsuario.setCarrera(carrera.trim());
            }
            if (ciclo != null && !ciclo.trim().isEmpty()) {
                nuevoUsuario.setCiclo(ciclo.trim());
            }

            // 5. Registrar el usuario
            boolean registroExitoso = servicioUsuarios.registrarUsuario(nuevoUsuario);

            if (registroExitoso) {
                // Registro exitoso - redirigir al login con mensaje de éxito
                request.setAttribute("mensajeExito", "¡Cuenta creada exitosamente! Ya puedes iniciar sesión.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                // Error en el registro
                request.setAttribute("mensajeError", "Error al crear la cuenta. Intenta de nuevo.");
                request.getRequestDispatcher("/registro.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error en RegistroServlet: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("mensajeError", "Error interno del servidor. Intenta de nuevo más tarde.");
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
        }
    }
}