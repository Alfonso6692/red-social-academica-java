package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Recurso;
import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioRecursos;
import com.utp.redsocial.services.ServicioUsuarios;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet para manejar todas las acciones relacionadas con los Recursos Educativos.
 * Utiliza un parámetro "accion" para determinar la operación a realizar
 * (subir, listar, buscar, etc.).
 */
@WebServlet(name = "RecursoServlet", urlPatterns = {"/RecursoServlet"})
public class RecursoServlet extends HttpServlet {

    // Se instancian los servicios necesarios.
    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
    private final ServicioRecursos servicioRecursos = new ServicioRecursos(servicioUsuarios);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; // Acción por defecto
        }

        switch (accion) {
            case "listar":
                listarRecursos(request, response);
                break;
            case "buscar":
                buscarRecursos(request, response);
                break;
            // Otros casos para GET, como ver un recurso específico
            default:
                listarRecursos(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            response.sendRedirect("recursos.jsp"); // Redirigir a la página principal de recursos
            return;
        }

        switch (accion) {
            case "subir":
                subirRecurso(request, response);
                break;
            // Otros casos para POST
            default:
                response.sendRedirect("recursos.jsp");
        }
    }

    private void listarRecursos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Recurso> listaRecursos = servicioRecursos.listarTodos();
        request.setAttribute("listaRecursos", listaRecursos);
        request.getRequestDispatcher("recursos.jsp").forward(request, response);
    }

    private void buscarRecursos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String etiqueta = request.getParameter("etiqueta");
        List<Recurso> recursosEncontrados = servicioRecursos.buscarPorEtiqueta(etiqueta);

        request.setAttribute("listaRecursos", recursosEncontrados);
        request.setAttribute("busquedaActual", etiqueta); // Para mostrar qué se buscó
        request.getRequestDispatcher("recursos.jsp").forward(request, response);
    }

    private void subirRecurso(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 1. Obtener los datos del formulario de subir recurso
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String url = request.getParameter("url");
            String tipo = request.getParameter("tipo");

            String etiquetasInput = request.getParameter("etiquetas");
            List<String> etiquetas = Arrays.asList(etiquetasInput.split("\\s*,\\s*"));

            // 2. Obtener el ID del usuario que sube el recurso
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
            String idUsuarioSube = usuario.getId();

            // 3. Llamar al servicio para crear el recurso
            servicioRecursos.subirRecurso(titulo, descripcion, url, tipo, etiquetas, idUsuarioSube);

            // 4. Redirigir a la lista de recursos con un mensaje de éxito
            response.sendRedirect("RecursoServlet?accion=listar&exito=Recurso subido correctamente");

        } catch (IllegalArgumentException e) {
            response.sendRedirect("subir_recurso.jsp?error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("subir_recurso.jsp?error=Ocurrió un error inesperado");
        }
    }
}
