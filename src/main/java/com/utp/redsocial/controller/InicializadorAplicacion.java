package com.utp.redsocial.controller;

import com.utp.redsocial.conexion.ConexionBD;
import com.utp.redsocial.services.ServicioUsuarios;
import com.utp.redsocial.services.ServicioRecursos;
import com.utp.redsocial.services.ServicioMensajeria;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;

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
            // 0. Probar conexión a la base de datos
            Connection testConnection = ConexionBD.getConexion();
            if (testConnection != null) {
                System.out.println("✅ Conexión a Supabase establecida exitosamente");
            } else {
                throw new RuntimeException("No se pudo establecer conexión a la base de datos");
            }

            // 1. Inicializar ServicioUsuarios (es independiente)
            ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
            sce.getServletContext().setAttribute("servicioUsuarios", servicioUsuarios);
            System.out.println("✓ ServicioUsuarios inicializado correctamente");

            // 2. Inicializar ServicioRecursos (depende de ServicioUsuarios)
            ServicioRecursos servicioRecursos = new ServicioRecursos(servicioUsuarios);
            sce.getServletContext().setAttribute("servicioRecursos", servicioRecursos);
            System.out.println("✓ ServicioRecursos inicializado correctamente");

            // 3. Inicializar ServicioMensajeria
            ServicioMensajeria servicioMensajeria = new ServicioMensajeria(servicioUsuarios);
            sce.getServletContext().setAttribute("servicioMensajeria", servicioMensajeria);
            System.out.println("✓ ServicioMensajeria inicializado correctamente");

            // 4. Inicializar otros servicios cuando los crees
            /*
            ServicioGrupos servicioGrupos = new ServicioGrupos(servicioUsuarios);
            sce.getServletContext().setAttribute("servicioGrupos", servicioGrupos);
            System.out.println("✓ ServicioGrupos inicializado correctamente");

            ServicioNotificaciones servicioNotificaciones = new ServicioNotificaciones(servicioUsuarios);
            sce.getServletContext().setAttribute("servicioNotificaciones", servicioNotificaciones);
            System.out.println("✓ ServicioNotificaciones inicializado correctamente");
            */

            System.out.println("=== APLICACIÓN INICIADA EXITOSAMENTE ===");

        } catch (Exception e) {
            System.err.println("❌ ERROR CRÍTICO AL INICIALIZAR LA APLICACIÓN:");
            e.printStackTrace();

            // Mostrar información de ayuda para debugging
            System.err.println("\n📋 INFORMACIÓN DE DEBUG:");
            System.err.println("- Verifica que tu base de datos en Supabase esté activa");
            System.err.println("- Verifica que las credenciales en ConexionBD sean correctas");
            System.err.println("- Verifica que las tablas 'usuarios' y 'recursos' existan");
            System.err.println("- Verifica que el driver PostgreSQL esté en el classpath");

            throw new RuntimeException("No se pudo inicializar la aplicación", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== CERRANDO APLICACIÓN RED SOCIAL ACADÉMICA ===");

        try {
            // Cerrar conexión a la base de datos
            ConexionBD.cerrarConexion();
            System.out.println("✓ Conexión a base de datos cerrada");

            // Remover servicios del contexto
            sce.getServletContext().removeAttribute("servicioUsuarios");
            sce.getServletContext().removeAttribute("servicioRecursos");
            sce.getServletContext().removeAttribute("servicioMensajeria");
            // sce.getServletContext().removeAttribute("servicioGrupos");
            // sce.getServletContext().removeAttribute("servicioNotificaciones");

            System.out.println("✓ Recursos liberados correctamente");
            System.out.println("=== APLICACIÓN CERRADA ===");

        } catch (Exception e) {
            System.err.println("❌ Error al cerrar la aplicación:");
            e.printStackTrace();
        }
    }
}