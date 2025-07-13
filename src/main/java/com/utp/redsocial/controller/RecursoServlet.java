package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Recurso;
import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioRecursos;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Servlet para manejar todas las acciones relacionadas con los recursos,
 * como listar o compartir nuevos recursos.
 */
@WebServlet(name = "RecursoServlet", urlPatterns = {"/RecursoServlet"})
public class RecursoServlet extends HttpServlet {

    // Declarar la variable del servicio, pero NO inicializarla aquí
    private ServicioRecursos servicioRecursos;

    @Override
    public void init() throws ServletException {
        // Obtener la instancia del servicio desde el contexto de la aplicación
        this.servicioRecursos = (ServicioRecursos) getServletContext().getAttribute("servicioRecursos");

        if (servicioRecursos == null) {
            throw new ServletException("ServicioRecursos no está disponible en el contexto de la aplicación");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; // Acción por defecto si no se especifica ninguna.
        }

        switch (accion) {
            case "listar":
                listarRecursos(request, response);
                break;
            case "buscar":
                buscarRecursos(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("compartir".equals(accion)) {
            compartirRecurso(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    /**
     * Obtiene la lista de todos los recursos y la envía a la página JSP para mostrarla.
     */
    private void listarRecursos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Recurso> listaDeRecursos = servicioRecursos.listarTodos();
            request.setAttribute("listaDeRecursos", listaDeRecursos);

            // Enviar a la página JSP
            request.getRequestDispatcher("/vistas/recursos/listar.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar los recursos");
            request.getRequestDispatcher("/vistas/recursos/listar.jsp").forward(request, response);
        }
    }

    /**
     * Busca recursos por etiqueta
     */
    private void buscarRecursos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String etiqueta = request.getParameter("etiqueta");
            List<Recurso> recursos;

            if (etiqueta != null && !etiqueta.trim().isEmpty()) {
                recursos = servicioRecursos.buscarPorEtiqueta(etiqueta.trim());
                request.setAttribute("etiquetaBuscada", etiqueta);
            } else {
                recursos = servicioRecursos.listarTodos();
            }

            request.setAttribute("listaDeRecursos", recursos);
            request.getRequestDispatcher("/vistas/recursos/listar.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la búsqueda");
            request.getRequestDispatcher("/vistas/recursos/listar.jsp").forward(request, response);
        }
    }

    /**
     * Procesa el formulario para compartir un nuevo recurso.
     */
    private void compartirRecurso(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            // 1. Obtener los datos del formulario
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String url = request.getParameter("url");
            String tipo = request.getParameter("tipo");
            String etiquetasStr = request.getParameter("etiquetas");

            // 2. Validaciones básicas
            if (titulo == null || titulo.trim().isEmpty()) {
                response.sendRedirect("RecursoServlet?accion=listar&error=Título es requerido");
                return;
            }

            if (url == null || url.trim().isEmpty()) {
                response.sendRedirect("RecursoServlet?accion=listar&error=URL es requerida");
                return;
            }

            // 3. Procesar etiquetas
            List<String> etiquetas = new ArrayList<>();
            if (etiquetasStr != null && !etiquetasStr.trim().isEmpty()) {
                etiquetas = Arrays.asList(etiquetasStr.split(","));
                // Limpiar espacios en blanco
                etiquetas.replaceAll(String::trim);
                etiquetas.removeIf(String::isEmpty);
            }

            // 4. Obtener el usuario de la sesión
            HttpSession session = request.getSession(false);
            Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

            if (usuarioLogueado == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // 5. Llamar al servicio
            servicioRecursos.subirRecurso(titulo, descripcion, url, tipo, etiquetas, usuarioLogueado.getId());

            // 6. Redirigir con éxito
            response.sendRedirect("RecursoServlet?accion=listar&exito=true");

        } catch (IllegalArgumentException e) {
            String errorMsg = java.net.URLEncoder.encode(e.getMessage(), "UTF-8");
            response.sendRedirect("RecursoServlet?accion=listar&error=" + errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("RecursoServlet?accion=listar&error=Error interno del servidor");
        }
    }
}