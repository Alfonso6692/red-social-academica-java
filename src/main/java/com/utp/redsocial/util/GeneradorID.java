package com.utp.redsocial.util;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.utp.redsocial.persistencia.NotificacionDAO;

/**
 * Utilidad para generar IDs únicos para las entidades del sistema.
 */
public class GeneradorID {

    private static long contador = 0;

    /**
     * Genera un ID único usando UUID.
     * @return String con un ID único
     */
    public static String generar() {
        return UUID.randomUUID().toString();
    }

    /**
     * Genera un ID único con prefijo personalizado.
     * @param prefijo El prefijo para el ID (ej: "USER", "RECURSO", "GRUPO")
     * @return String con un ID único con prefijo
     */
    public static String generar(String prefijo) {
        return prefijo + "_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Genera un ID numérico secuencial (para testing o casos simples).
     * @return String con un ID numérico
     */
    public static String generarNumerico() {
        synchronized (GeneradorID.class) {
            return String.valueOf(++contador);
        }
    }

    /**
     * Genera un ID basado en timestamp.
     * @return String con un ID basado en fecha y hora actual
     */
    public static String generarConTimestamp() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return timestamp + "_" + random;
    }
}