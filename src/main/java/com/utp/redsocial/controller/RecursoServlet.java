package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.entidades.Recurso;
import com.utp.redsocial.services.ServicioUsuarios;
import com.utp.redsocial.services.ServicioRecursos;
import com.utp.redsocial.util.GeneradorID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet(name = "RecursoServlet", urlPatterns = {"/RecursoServlet"})
public class RecursoServlet extends HttpServlet {

    private ServicioRecursos servicioRecursos;

    @Override
    public void init() throws ServletException {
        // Obtener la instancia del servicio desde el contexto de la aplicación
        this.servicioRecursos = (ServicioRecursos) getServletContext().getAttribute("servicioRecursos");

        if (this.servicioRecursos == null) {
            throw new ServletException("ServicioRecursos no está disponible en el contexto de la aplicación");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        // Si no hay acción, por defecto listar
        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar";
        }

        try {
            switch (accion.toLowerCase()) {
                case "listar":
                    listarRecursos(request, response);
                    break;
                case "buscar":
                    buscarRecursos(request, response);
                    break;
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "eliminar":
                    eliminarRecurso(request, response);
                    break;
                default:
                    listarRecursos(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            manejarError(request, response, "Error interno del servidor: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            switch (accion != null ? accion.toLowerCase() : "") {
                case "crear":
                    crearRecurso(request, response);
                    break;
                case "actualizar":
                    actualizarRecurso(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            manejarError(request, response, "Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * Lista todos los recursos disponibles
     */
    private void listarRecursos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener todos los recursos
            List<Recurso> recursos = servicioRecursos.listarTodos();

            // El JSP espera "listaDeRecursos", no "recursos"
            request.setAttribute("listaDeRecursos", recursos);

            // Agregar información adicional
            request.setAttribute("totalRecursos", recursos.size());

            // Redirigir al JSP
            request.getRequestDispatcher("/vistas/recursos/listar.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            manejarError(request, response, "Error al cargar los recursos: " + e.getMessage());
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
                // Buscar por etiqueta específica
                recursos = servicioRecursos.buscarPorEtiqueta(etiqueta.trim());
                request.setAttribute("etiquetaBuscada", etiqueta.trim());
            } else {
                // Si no hay etiqueta, mostrar todos
                recursos = servicioRecursos.listarTodos();
            }

            // El JSP espera "listaDeRecursos"
            request.setAttribute("listaDeRecursos", recursos);
            request.setAttribute("totalRecursos", recursos.size());

            // Redirigir al mismo JSP
            request.getRequestDispatcher("/vistas/recursos/listar.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            manejarError(request, response, "Error al buscar recursos: " + e.getMessage());
        }
    }

    /**
     * Muestra el formulario para crear un nuevo recurso
     */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir al formulario de crear recurso
        request.getRequestDispatcher("/compartir-recurso.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para editar un recurso existente
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String recursoId = request.getParameter("id");

            if (recursoId == null || recursoId.trim().isEmpty()) {
                response.sendRedirect("RecursoServlet?accion=listar&error=ID de recurso requerido");
                return;
            }

            Recurso recurso = servicioRecursos.obtenerPorId(recursoId);

            if (recurso == null) {
                response.sendRedirect("RecursoServlet?accion=listar&error=Recurso no encontrado");
                return;
            }

            request.setAttribute("recurso", recurso);
            request.getRequestDispatcher("/editar-recurso.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            manejarError(request, response, "Error al cargar el recurso para editar: " + e.getMessage());
        }
    }

    /**
     * Crea un nuevo recurso
     */
    private void crearRecurso(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar sesión
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect("login.jsp?error=Sesión expirada");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");

            // Obtener parámetros del formulario
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String url = request.getParameter("url");
            String tipo = request.getParameter("tipo");
            String etiquetasStr = request.getParameter("etiquetas");

            // Validaciones básicas
            if (titulo == null || titulo.trim().isEmpty()) {
                response.sendRedirect("compartir-recurso.jsp?error=El título es requerido");
                return;
            }

            if (url == null || url.trim().isEmpty()) {
                response.sendRedirect("compartir-recurso.jsp?error=La URL es requerida");
                return;
            }

            // Crear el objeto Recurso
            Recurso recurso = new Recurso();
            recurso.setId(GeneradorID.generar());
            recurso.setTitulo(titulo.trim());
            recurso.setDescripcion(descripcion != null ? descripcion.trim() : "");
            recurso.setUrl(url.trim());
            recurso.setTipo(tipo != null ? tipo.trim() : "General");
            recurso.setUsuarioId(usuario.getId());

            // Procesar etiquetas
            if (etiquetasStr != null && !etiquetasStr.trim().isEmpty()) {
                List<String> etiquetas = new ArrayList<>();
                String[] etiquetasArray = etiquetasStr.split(",");
                for (String etiqueta : etiquetasArray) {
                    etiqueta = etiqueta.trim();
                    if (!etiqueta.isEmpty()) {
                        etiquetas.add(etiqueta);
                    }
                }
                recurso.setEtiquetas(etiquetas);
            }

            // Guardar el recurso
            boolean exito = servicioRecursos.crearRecurso(recurso);

            if (exito) {
                response.sendRedirect("RecursoServlet?accion=listar&exito=true");
            } else {
                response.sendRedirect("compartir-recurso.jsp?error=Error al guardar el recurso");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("compartir-recurso.jsp?error=Error interno: " + e.getMessage());
        }
    }

    /**
     * Actualiza un recurso existente
     */
    private void actualizarRecurso(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar sesión
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect("login.jsp?error=Sesión expirada");
                return;
            }

            String recursoId = request.getParameter("id");
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String url = request.getParameter("url");
            String tipo = request.getParameter("tipo");
            String etiquetasStr = request.getParameter("etiquetas");

            // Validaciones
            if (recursoId == null || recursoId.trim().isEmpty()) {
                response.sendRedirect("RecursoServlet?accion=listar&error=ID de recurso requerido");
                return;
            }

            // Obtener el recurso existente
            Recurso recurso = servicioRecursos.obtenerPorId(recursoId);
            if (recurso == null) {
                response.sendRedirect("RecursoServlet?accion=listar&error=Recurso no encontrado");
                return;
            }

            // Actualizar campos
            if (titulo != null && !titulo.trim().isEmpty()) {
                recurso.setTitulo(titulo.trim());
            }
            if (descripcion != null) {
                recurso.setDescripcion(descripcion.trim());
            }
            if (url != null && !url.trim().isEmpty()) {
                recurso.setUrl(url.trim());
            }
            if (tipo != null) {
                recurso.setTipo(tipo.trim());
            }

            // Procesar etiquetas
            if (etiquetasStr != null) {
                List<String> etiquetas = new ArrayList<>();
                String[] etiquetasArray = etiquetasStr.split(",");
                for (String etiqueta : etiquetasArray) {
                    etiqueta = etiqueta.trim();
                    if (!etiqueta.isEmpty()) {
                        etiquetas.add(etiqueta);
                    }
                }
                recurso.setEtiquetas(etiquetas);
            }

            // Actualizar el recurso
            boolean exito = servicioRecursos.actualizarRecurso(recurso);

            if (exito) {
                response.sendRedirect("RecursoServlet?accion=listar&exito=true");
            } else {
                response.sendRedirect("RecursoServlet?accion=editar&id=" + recursoId + "&error=Error al actualizar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            String recursoId = request.getParameter("id");
            response.sendRedirect("RecursoServlet?accion=editar&id=" + recursoId + "&error=Error interno: " + e.getMessage());
        }
    }

    /**
     * Elimina un recurso
     */
    private void eliminarRecurso(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar sesión
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect("login.jsp?error=Sesión expirada");
                return;
            }

            String recursoId = request.getParameter("id");

            if (recursoId == null || recursoId.trim().isEmpty()) {
                response.sendRedirect("RecursoServlet?accion=listar&error=ID de recurso requerido");
                return;
            }

            boolean exito = servicioRecursos.eliminarRecurso(recursoId);

            if (exito) {
                response.sendRedirect("RecursoServlet?accion=listar&exito=true");
            } else {
                response.sendRedirect("RecursoServlet?accion=listar&error=Error al eliminar el recurso");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("RecursoServlet?accion=listar&error=Error interno: " + e.getMessage());
        }
    }

    /**
     * Maneja errores y redirige a una página de error
     */
    private void manejarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
}