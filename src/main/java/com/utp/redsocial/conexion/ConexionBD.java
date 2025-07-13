package com.utp.redsocial.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // --- DATOS OBTENIDOS DE TU PROYECTO SUPABASE (PESTAÑA "DIRECT CONNECTION") ---
    private static final String HOST = "aws-0-us-east-2.pooler.supabase.com"; // Tu host de la base de datos
    private static final String PORT = "6543";
    private static final String DBNAME = "postgres";
    private static final String USER = "postgres.injmmzhuemqgvmxpvklt"; // Para la conexión directa, el usuario es solo "postgres"

    // --- ¡IMPORTANTE! REEMPLAZA ESTA LÍNEA CON TU CONTRASEÑA ---
    private static final String PASSWORD = "Cienpies92!";

    // --- URL de conexión DIRECTA con SSL DESHABILITADO para que coincida con la configuración de Supabase ---
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DBNAME + "?sslmode=disable";

    private static Connection conn = null;

    /**
     * Establece y devuelve una conexión a la base de datos usando la Conexión Directa.
     * @return Un objeto Connection o null si la conexión falla.
     */
    public static Connection getConexion() {
        // Validación para asegurarse de que la contraseña ha sido cambiada.
        if ("AQUI_VA_TU_CONTRASEÑA_RESETEADA".equals(PASSWORD)) {
            System.err.println("*****************************************************************");
            System.err.println("FATAL: La contraseña de la base de datos no ha sido configurada.");
            System.err.println("Por favor, edita la clase ConexionBD.java y reemplaza la contraseña.");
            System.err.println("*****************************************************************");
            return null;
        }

        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("Cargando driver de PostgreSQL...");
                Class.forName("org.postgresql.Driver");

                System.out.println("Intentando conectar a la URL: " + URL);
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("¡Conexión a la base de datos (Directa) exitosa!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error fatal: No se encontró el driver de PostgreSQL.");
            System.err.println("Asegúrate de que el archivo JAR del driver esté en la carpeta 'lib' de tu proyecto.");
            e.printStackTrace();
            conn = null;
        } catch (SQLException e) {
            System.err.println("Error fatal al conectar a la base de datos: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());
            System.err.println("Verifica los siguientes puntos:");
            System.err.println("1. ¿La contraseña es correcta?");
            System.err.println("2. ¿El host '" + HOST + "' es correcto?");
            System.err.println("3. ¿Tienes conexión a internet y no hay un firewall bloqueando el puerto " + PORT + "?");
            e.printStackTrace();
            conn = null;
        }
        return conn;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static void cerrarConexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }


}
