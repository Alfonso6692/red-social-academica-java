package com.utp.redsocial.entidades;

/**
 * Entidad Usuario que coincide con la tabla 'usuarios' en Supabase
 * Campos: id, nombre, apellido, correo, contrasena, carrera, ciclo
 */
public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String carrera;
    private String ciclo;
    private boolean enLinea; // Campo para estado de conexión

    // Constructor vacío
    public Usuario() {
        this.enLinea = false; // Por defecto, el usuario no está en línea
    }

    // Constructor con parámetros básicos
    public Usuario(String id, String nombre, String apellido, String correo, String contrasena) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Constructor completo
    public Usuario(String id, String nombre, String apellido, String correo,
                   String contrasena, String carrera, String ciclo) {
        this(id, nombre, apellido, correo, contrasena);
        this.carrera = carrera;
        this.ciclo = ciclo;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public boolean isEnLinea() {
        return enLinea;
    }

    public void setEnLinea(boolean enLinea) {
        this.enLinea = enLinea;
    }

    // Métodos de utilidad
    /**
     * Obtiene el nombre completo del usuario
     */
    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder();

        if (nombre != null && !nombre.trim().isEmpty()) {
            nombreCompleto.append(nombre.trim());
        }

        if (apellido != null && !apellido.trim().isEmpty()) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(apellido.trim());
        }

        return nombreCompleto.length() > 0 ? nombreCompleto.toString() : correo;
    }

    /**
     * Obtiene información académica completa
     */
    public String getInfoAcademica() {
        StringBuilder info = new StringBuilder();

        if (carrera != null && !carrera.trim().isEmpty()) {
            info.append(carrera);
        }

        if (ciclo != null && !ciclo.trim().isEmpty()) {
            if (info.length() > 0) {
                info.append(" - ");
            }
            info.append("Ciclo ").append(ciclo);
        }

        return info.length() > 0 ? info.toString() : "Información académica no especificada";
    }

    /**
     * Marca al usuario como conectado/en línea
     */
    public void conectar() {
        this.enLinea = true;
    }

    /**
     * Marca al usuario como desconectado/fuera de línea
     */
    public void desconectar() {
        this.enLinea = false;
    }

    /**
     * Obtiene el estado de conexión como texto
     */
    public String getEstadoConexion() {
        return enLinea ? "En línea" : "Desconectado";
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", correo='" + correo + '\'' +
                ", carrera='" + carrera + '\'' +
                ", ciclo='" + ciclo + '\'' +
                ", enLinea=" + enLinea +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return id != null ? id.equals(usuario.id) : usuario.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}