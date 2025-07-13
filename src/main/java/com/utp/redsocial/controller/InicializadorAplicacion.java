package com.utp.redsocial.controller;

import com.utp.redsocial.services.ServicioUsuarios;
import com.utp.redsocial.services.ServicioRecursos;
import com.utp.redsocial.services.ServicioGrupos;
import com.utp.redsocial.services.ServicioMensajeria;
import com.utp.redsocial.services.ServicioNotificaciones;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener que se ejecuta al iniciar y destruir la aplicación web.
 * Se encarga de inicializar los servicios globales y ponerlos disponibles
 * en el contexto de la aplicación para que los servlets puedan acceder a ellos.
 */
@WebListener
public class InicializadorAplicacion implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== INICIANDO APLICACIÓN RED SOCIAL ACADÉMICA ===");

        try {
            // 1. Inicializar ServicioUsuarios (es independiente)
            ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
            sce.getServletContext().setAttribute("servicioUsuarios", servicioUsuarios);
            System.out.println("✓ ServicioUsuarios inicializado correctamente");

            // 2. Inicializar ServicioRecursos (depende de ServicioUsuarios)
            ServicioRecursos servicioRecursos = new ServicioRecursos(servicioUsuarios);
            sce.getServletContext().setAttribute("servicioRecursos", servicioRecursos);
            System.out.println("✓ ServicioRecursos inicializado correctamente");

            // 3. Inicializar otros servicios (añadir según vayas creándolos)
            /*
            ServicioGrupos servicioGrupos = new ServicioGrupos(servicioUsuarios);
            sce.getServletContext().setAttribute("servicioGrupos", servicioGrupos);
            System.out.println("✓ ServicioGrupos inicializado correctamente");

            ServicioMensajeria servicioMensajeria = new ServicioMensajeria(servicioUsuarios);
            sce.getServletContext().setAttribute("servicioMensajeria", servicioMensajeria);
            System.out.println("✓ ServicioMensajeria inicializado correctamente");

            ServicioNotificaciones servicioNotificaciones = new ServicioNotificaciones(servicioUsuarios);
            sce.getServletContext().setAttribute("servicioNotificaciones", servicioNotificaciones);
            System.out.println("✓ ServicioNotificaciones inicializado correctamente");
            */

            System.out.println("=== APLICACIÓN INICIADA EXITOSAMENTE ===");

        } catch (Exception e) {
            System.err.println("❌ ERROR CRÍTICO AL INICIALIZAR LA APLICACIÓN:");
            e.printStackTrace();

            // En un entorno de producción, podrías decidir no iniciar la app si falla
            throw new RuntimeException("No se pudo inicializar la aplicación", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== CERRANDO APLICACIÓN RED SOCIAL ACADÉMICA ===");

        try {
            // Limpiar recursos si es necesario
            // Por ejemplo, cerrar conexiones de base de datos, threads, etc.

            // Remover servicios del contexto
            sce.getServletContext().removeAttribute("servicioUsuarios");
            sce.getServletContext().removeAttribute("servicioRecursos");
            // sce.getServletContext().removeAttribute("servicioGrupos");
            // sce.getServletContext().removeAttribute("servicioMensajeria");
            // sce.getServletContext().removeAttribute("servicioNotificaciones");

            System.out.println("✓ Recursos liberados correctamente");
            System.out.println("=== APLICACIÓN CERRADA ===");

        } catch (Exception e) {
            System.err.println("❌ Error al cerrar la aplicación:");
            e.printStackTrace();
        }
    }
}