package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Grupo;
import com.utp.redsocial.services.ServicioGrupos;
import com.utp.redsocial.services.ServicioUsuarios;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet para manejar todas las acciones relacionadas con los grupos de estudio.
 */
@WebServlet(name = "GrupoServlet", urlPatterns = {"/GrupoServlet"})
public class GrupoServlet extends HttpServlet {

    // 1. Declaramos las variables de los servicios, pero NO las inicializamos aquí.
    private ServicioGrupos servicioGrupos;
    private ServicioUsuarios servicioUsuarios;

    @Override
    public void init() throws ServletException {
        // 2. Usamos el método init() para obtener las instancias de los servicios
        // que fueron creadas por el InicializadorAplicacion (el Listener).
        this.servicioGrupos = (ServicioGrupos) getServletContext().getAttribute("servicioGrupos");
        this.servicioUsuarios = (ServicioUsuarios) getServletContext().getAttribute("servicioUsuarios");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificación de seguridad
        if (servicioGrupos == null) {
            throw new ServletException("El servicio de grupos no está disponible. Revisa los logs del servidor.");
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; // Si no se especifica acción, por defecto listamos los grupos.
        }

        switch (accion) {
            case "listar":
                listarGrupos(request, response);
                break;
            // Aquí puedes añadir más casos para otras acciones GET (ej. "ver", "formularioCrear")
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lógica para manejar peticiones POST (ej. crear un grupo)
        String accion = request.getParameter("accion");
        if ("crear".equals(accion)) {
            // crearGrupo(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    /**
     * Obtiene la lista de grupos del servicio y la envía a la página JSP correspondiente.
     */
    private void listarGrupos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Llamar al servicio para obtener la lista de todos los grupos
        List<Grupo> listaDeGrupos = servicioGrupos.listarTodosLosGrupos();

        // 2. Guardar la lista en el 'request' para que la página JSP pueda acceder a ella
        request.setAttribute("listaDeGrupos", listaDeGrupos);

        // 3. Redirigir la petición a la página JSP que mostrará los grupos
        // Asegúrate de que la ruta a tu JSP sea correcta.
        request.getRequestDispatcher("/vistas/grupos/listar.jsp").forward(request, response);
    }
}
