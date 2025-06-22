package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Grupo;
import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioGrupos;
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

@WebServlet(name = "GrupoServlet", urlPatterns = {"/GrupoServlet"})
public class GrupoServlet extends HttpServlet {

    private final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
    private final ServicioGrupos servicioGrupos = new ServicioGrupos(servicioUsuarios);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarGrupos(request, response);
                break;
            default:
                listarGrupos(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            response.sendRedirect("grupos.jsp");
            return;
        }

        switch (accion) {
            case "crear":
                crearGrupo(request, response);
                break;
            case "unirse":
                unirseAGrupo(request, response);
                break;
            default:
                response.sendRedirect("grupos.jsp");
        }
    }

    private void listarGrupos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- ESTA ES LA PARTE CORREGIDA ---
        // 1. Llama al servicio para obtener la lista de todos los grupos desde la BD.
        List<Grupo> listaGrupos = servicioGrupos.listarTodosLosGrupos();

        // 2. Adjunta la lista a la petición para que la página JSP pueda usarla.
        request.setAttribute("listaGrupos", listaGrupos);

        // 3. Reenvía a la página JSP.
        request.getRequestDispatcher("grupos.jsp").forward(request, response);
    }

    private void crearGrupo(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String categoriaId = request.getParameter("categoriaId");
            String temasInput = request.getParameter("temas");
            List<String> temas = Arrays.asList(temasInput.split("\\s*,\\s*"));

            Usuario creador = (Usuario) session.getAttribute("usuarioLogueado");

            servicioGrupos.crearGrupo(nombre, descripcion, temas, categoriaId, creador.getId());

            response.sendRedirect(request.getContextPath() + "/GrupoServlet?accion=listar&exito=Grupo%20creado%20correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/crear_grupo.jsp?error=Ocurrio%20un%20error");
        }
    }

    private void unirseAGrupo(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // ... (lógica para unirse a un grupo) ...
    }
}
