package com.utp.redsocial.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos PostgreSQL utilizando el patrón Singleton.
 * Esto asegura que solo se cree una única instancia de la conexión,
 * optimizando los recursos.
 */
public class ConexionBD {

    // --- Parámetros de la conexión para PostgreSQL ---
    // Reemplaza "nombre_de_tu_bd" con el nombre de tu base de datos.
    // Después (usando los datos de la URL de Neon):
    private static final String URL = "jdbc:postgresql://db.injmmzhuemqgvmxpvklt.supabase.co:5432/postgres";
    private static final String USUARIO = "postgres";
    private static final String CONTRASENA = "Alibabaylos40ladrones";

    // --- Instancia única de la conexión (Singleton) ---
    private static Connection conexion = null;

    /**
     * Constructor privado para evitar que se creen instancias de esta clase.
     */
    private ConexionBD() {
    }

    /**
     * Devuelve la única instancia de la conexión a la base de datos.
     * Si la conexión no existe o está cerrada, intenta crear una nueva.
     * El método es 'synchronized' para ser seguro en entornos con múltiples hilos (multi-threading).
     *
     * @return Objeto Connection a la base de datos.
     */
    public static synchronized Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Cargar el driver de la base de datos de PostgreSQL
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    System.err.println("Error: No se pudo encontrar el driver de PostgreSQL.");
                    e.printStackTrace();
                    return null;
                }

                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                System.out.println("Conexión a la base de datos PostgreSQL establecida con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos PostgreSQL.");
            e.printStackTrace();
            conexion = null; // Asegurarse de que la conexión sea nula si falla
        }
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static synchronized void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión.");
            e.printStackTrace();
        }
    }
}