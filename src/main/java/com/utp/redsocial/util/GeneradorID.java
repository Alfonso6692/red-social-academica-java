// --- GeneradorID.java ---
package com.utp.redsocial.util;

import java.util.UUID;

/**
 * Clase de utilidad para generar IDs únicos.
 */
public class GeneradorID {
    /**
     * Genera un ID único universal (UUID).
     * @return Un string que representa el ID único.
     */
    public static String generar() {
        return UUID.randomUUID().toString();
    }
}