package com.utp.redsocial.entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {

    private String id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String carrera;
    private int ciclo;
    private List<String> habilidades;
    private List<String> intereses;
    private boolean enLinea;

    /**
     * Constructor completo.
     * Útil para crear un nuevo usuario con todos sus detalles desde el inicio.
     */
    public Usuario(String id, String nombre, String apellido, String correo, String contrasena, String carrera, int ciclo, List<String> habilidades, List<String> intereses, boolean enLinea) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.carrera = carrera;
        this.ciclo = ciclo;
        this.habilidades = habilidades;
        this.intereses = intereses;
        this.enLinea = enLinea;
    }

    /**
     * Constructor para el DAO.
     * Este es el constructor que la clase UsuarioDAO utiliza para crear objetos Usuario
     * a partir de los datos recuperados de la base de datos.
     */
    public Usuario(String id, String nombre, String apellido, String correo, String contrasena, String carrera, int ciclo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.carrera = carrera;
        this.ciclo = ciclo;
        // Inicializamos los campos restantes con valores por defecto.
        this.habilidades = new ArrayList<>();
        this.intereses = new ArrayList<>();
        this.enLinea = false;
    }


    // --- Getters y Setters ---

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

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public List<String> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<String> habilidades) {
        this.habilidades = habilidades;
    }

    public List<String> getIntereses() {
        return intereses;
    }

    public void setIntereses(List<String> intereses) {
        this.intereses = intereses;
    }

    public boolean isEnLinea() {
        return enLinea;
    }

    public void setEnLinea(boolean enLinea) {
        this.enLinea = enLinea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
