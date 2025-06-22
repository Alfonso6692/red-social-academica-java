// --- ValidadorDatos.java ---
package com.utp.redsocial.util;

import java.util.regex.Pattern;

/**
 * Clase de utilidad para validar diferentes tipos de datos.
 */
public class ValidadorDatos {

    // Expresión regular para validar correos electrónicos.
    private static final Pattern PATRON_CORREO = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    /**
     * Valida si un string tiene un formato de correo electrónico válido.
     * @param correo El correo a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean esCorreoValido(String correo) {
        if (correo == null) {
            return false;
        }
        return PATRON_CORREO.matcher(correo).matches();
    }
}
