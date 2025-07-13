package com.utp.redsocial.controller;

import com.utp.redsocial.entidades.Usuario;
import com.utp.redsocial.services.ServicioConexiones;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Este servlet maneja todas las acciones relacionadas con las conexiones de un usuario,
 * como listar, solicitar o eliminar amistades.
 */
// La anotación @WebServlet debe coincidir exactamente con la URL que se usa en el navegador.
@WebServlet("/ConexionServlet")
public class ConexionServlet extends HttpServlet {

    private ServicioConexiones servicioConexiones;

    @Override
    public void init() throws ServletException {
        // Obtenemos la instancia del servicio que fue creada por el Listener al iniciar la aplicación.
        // Es crucial que el Listener se ejecute primero.
        this.servicioConexiones = (ServicioConexiones) getServletContext().getAttribute("servicioConexiones");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("listar".equals(accion)) {
            listarConexiones(request, response);
        } else {
            // Si la acción no es "listar", se responde con un error.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida o no especificada.");
        }
    }

    private void listarConexiones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificación de seguridad: si el servicio no se inicializó, no podemos continuar.
        if (servicioConexiones == null) {
            throw new ServletException("El servicio de conexiones no está disponible. Revisa los logs del servidor para errores de inicialización.");
        }

        // Obtenemos el objeto Usuario de la sesión, que debió ser guardado durante el login.
        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");

        if (usuarioLogueado != null) {
            System.out.println("Servlet: Buscando conexiones para el usuario: " + usuarioLogueado.getNombre());

            // Usamos el servicio para obtener la lista de amigos.
            List<Usuario> conexiones = servicioConexiones.obtenerConexiones(usuarioLogueado.getId());

            System.out.println("Servlet: Se encontraron " + conexiones.size() + " conexiones.");

            // Guardamos la lista en el 'request' para que la página JSP pueda acceder a ella.
            request.setAttribute("listaDeConexiones", conexiones);
        } else {
            System.err.println("Error en ConexionServlet: No se encontró un usuario en la sesión. El usuario debe iniciar sesión primero.");
        }

        // Redirigimos la petición (junto con los datos del request) a la página JSP.
        // Asegúrate de que la ruta a tu archivo JSP sea correcta.
        request.getRequestDispatcher("/vistas/conexiones/listar.jsp").forward(request, response);
    }
}
