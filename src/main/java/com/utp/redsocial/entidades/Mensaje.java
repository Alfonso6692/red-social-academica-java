package com.utp.redsocial.entidades;

import java.time.LocalDateTime;

/**
 * Entidad Mensaje que coincide con la tabla 'mensajes' en Supabase
 * Campos: id, texto, id_emisor, id_receptor, fecha, leido
 */
public class Mensaje {
    private String id;
    private String texto;
    private String idEmisor;
    private String idReceptor;
    private LocalDateTime fecha;
    private boolean leido;

    // Constructor vacío
    public Mensaje() {
        this.fecha = LocalDateTime.now();
        this.leido = false;
    }

    // Constructor con parámetros básicos
    public Mensaje(String id, String texto, String idEmisor, String idReceptor) {
        this();
        this.id = id;
        this.texto = texto;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
    }

    // Constructor completo
    public Mensaje(String id, String texto, String idEmisor, String idReceptor,
                   LocalDateTime fecha, boolean leido) {
        this.id = id;
        this.texto = texto;
        this.idEmisor = idEmisor;
        this.idReceptor = idReceptor;
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
        this.leido = leido;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(String idEmisor) {
        this.idEmisor = idEmisor;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    // Métodos de utilidad
    /**
     * Marca el mensaje como leído
     */
    public void marcarComoLeido() {
        this.leido = true;
    }

    /**
     * Marca el mensaje como no leído
     */
    public void marcarComoNoLeido() {
        this.leido = false;
    }

    /**
     * Verifica si el mensaje es del emisor especificado
     * @param idUsuario ID del usuario a verificar
     * @return true si el usuario es el emisor, false en caso contrario
     */
    public boolean esDelEmisor(String idUsuario) {
        return idEmisor != null && idEmisor.equals(idUsuario);
    }

    /**
     * Verifica si el mensaje es para el receptor especificado
     * @param idUsuario ID del usuario a verificar
     * @return true si el usuario es el receptor, false en caso contrario
     */
    public boolean esParaReceptor(String idUsuario) {
        return idReceptor != null && idReceptor.equals(idUsuario);
    }

    /**
     * Verifica si el mensaje involucra al usuario especificado (como emisor o receptor)
     * @param idUsuario ID del usuario a verificar
     * @return true si el usuario está involucrado, false en caso contrario
     */
    public boolean involucraUsuario(String idUsuario) {
        return esDelEmisor(idUsuario) || esParaReceptor(idUsuario);
    }

    /**
     * Obtiene un resumen del mensaje para mostrar
     * @param longitudMaxima Longitud máxima del resumen
     * @return Resumen del texto del mensaje
     */
    public String getResumen(int longitudMaxima) {
        if (texto == null || texto.length() <= longitudMaxima) {
            return texto;
        }
        return texto.substring(0, longitudMaxima - 3) + "...";
    }

    /**
     * Obtiene un resumen corto del mensaje (50 caracteres)
     * @return Resumen corto del mensaje
     */
    public String getResumenCorto() {
        return getResumen(50);
    }

    /**
     * Obtiene la fecha formateada para mostrar
     * @return Fecha formateada como string
     */
    public String getFechaFormateada() {
        if (fecha == null) {
            return "Fecha desconocida";
        }

        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fecha.format(formatter);
    }

    /**
     * Obtiene el estado del mensaje como texto
     * @return "Leído" o "No leído"
     */
    public String getEstadoTexto() {
        return leido ? "Leído" : "No leído";
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "id='" + id + '\'' +
                ", texto='" + getResumenCorto() + '\'' +
                ", idEmisor='" + idEmisor + '\'' +
                ", idReceptor='" + idReceptor + '\'' +
                ", fecha=" + fecha +
                ", leido=" + leido +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Mensaje mensaje = (Mensaje) obj;
        return id != null ? id.equals(mensaje.id) : mensaje.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}