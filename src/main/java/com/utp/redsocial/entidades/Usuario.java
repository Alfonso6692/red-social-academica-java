package com.utp.redsocial.entidades;

public class Usuario {

    private String id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String carrera;
    private int ciclo;

    /**
     * Constructor completo para crear un nuevo usuario.
     */
    public Usuario(String id, String nombre, String apellido, String correo, String contrasena, String carrera, int ciclo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.carrera = carrera;
        this.ciclo = ciclo;
    }

    /**
     * Constructor básico (opcional).
     * Ya no causa recursividad.
     */
    public Usuario(String id, String nombre, String apellido, String correo, String contrasena) {
        // Llama al constructor completo, pasando valores por defecto para los campos faltantes.
        this(id, nombre, apellido, correo, contrasena, "No especificada", 0);
    }

    public Usuario(String id, String nombre, String apellido, String correo, String contrasena, String carrera, String ciclo) {
    }

    // --- Getters y Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public int getCiclo() { return ciclo; }
    public void setCiclo(int ciclo) { this.ciclo = ciclo; }
}
