package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Recurso;
import com.utp.redsocial.persistencia.RecursoDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar recursos educativos
 */
public class ServicioRecursos {

    private RecursoDAO recursoDAO;

    /**
     * Constructor que recibe el DAO
     */
    public ServicioRecursos(RecursoDAO recursoDAO) {
        this.recursoDAO = recursoDAO;
    }

    /**
     * Constructor por defecto
     */
    public ServicioRecursos() {
        this.recursoDAO = new RecursoDAO();
    }

    /**
     * Setter para el DAO
     */
    public void setRecursoDAO(RecursoDAO recursoDAO) {
        this.recursoDAO = recursoDAO;
    }

    /**
     * Lista todos los recursos
     */
    public List<Recurso> listarTodos() {
        try {
            if (recursoDAO == null) {
                System.err.println("RecursoDAO no está inicializado");
                return new ArrayList<>();
            }

            List<Recurso> recursos = recursoDAO.listarTodos();
            System.out.println("ServicioRecursos: Se encontraron " + recursos.size() + " recursos");
            return recursos != null ? recursos : new ArrayList<>();

        } catch (Exception e) {
            System.err.println("Error al listar recursos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene un recurso por ID
     */
    public Recurso obtenerPorId(String id) {
        try {
            if (recursoDAO == null || id == null || id.trim().isEmpty()) {
                return null;
            }

            return recursoDAO.obtenerPorId(id);

        } catch (Exception e) {
            System.err.println("Error al obtener recurso por ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea un nuevo recurso
     */
    public boolean crearRecurso(Recurso recurso) {
        try {
            if (recursoDAO == null || recurso == null) {
                return false;
            }

            // Validaciones básicas
            if (recurso.getTitulo() == null || recurso.getTitulo().trim().isEmpty()) {
                System.err.println("El título del recurso es requerido");
                return false;
            }

            if (recurso.getUrl() == null || recurso.getUrl().trim().isEmpty()) {
                System.err.println("La URL del recurso es requerida");
                return false;
            }

            boolean resultado = recursoDAO.insertar(recurso);

            if (resultado) {
                System.out.println("Recurso creado exitosamente: " + recurso.getId());
            }

            return resultado;

        } catch (Exception e) {
            System.err.println("Error al crear recurso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un recurso existente
     */
    public boolean actualizarRecurso(Recurso recurso) {
        try {
            if (recursoDAO == null || recurso == null || recurso.getId() == null) {
                return false;
            }

            // Verificar que el recurso existe
            Recurso existente = recursoDAO.obtenerPorId(recurso.getId());
            if (existente == null) {
                System.err.println("No se encontró el recurso: " + recurso.getId());
                return false;
            }

            boolean resultado = recursoDAO.actualizar(recurso);

            if (resultado) {
                System.out.println("Recurso actualizado exitosamente: " + recurso.getId());
            }

            return resultado;

        } catch (Exception e) {
            System.err.println("Error al actualizar recurso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un recurso
     */
    public boolean eliminarRecurso(String id) {
        try {
            if (recursoDAO == null || id == null || id.trim().isEmpty()) {
                return false;
            }

            // Verificar que el recurso existe
            Recurso existente = recursoDAO.obtenerPorId(id);
            if (existente == null) {
                System.err.println("No se encontró el recurso: " + id);
                return false;
            }

            boolean resultado = recursoDAO.eliminar(id);

            if (resultado) {
                System.out.println("Recurso eliminado exitosamente: " + id);
            }

            return resultado;

        } catch (Exception e) {
            System.err.println("Error al eliminar recurso: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca recursos por etiqueta
     */
    public List<Recurso> buscarPorEtiqueta(String etiqueta) {
        try {
            if (recursoDAO == null) {
                return new ArrayList<>();
            }

            if (etiqueta == null || etiqueta.trim().isEmpty()) {
                return listarTodos();
            }

            String etiquetaBusqueda = etiqueta.trim().toLowerCase();
            List<Recurso> todosLosRecursos = listarTodos();
            List<Recurso> resultados = new ArrayList<>();

            for (Recurso recurso : todosLosRecursos) {
                if (recursoContieneEtiqueta(recurso, etiquetaBusqueda)) {
                    resultados.add(recurso);
                }
            }

            System.out.println("Búsqueda por etiqueta '" + etiqueta + "': " + resultados.size() + " resultados");
            return resultados;

        } catch (Exception e) {
            System.err.println("Error al buscar por etiqueta: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Verifica si un recurso contiene una etiqueta específica
     */
    private boolean recursoContieneEtiqueta(Recurso recurso, String etiquetaBusqueda) {
        if (recurso.getEtiquetas() == null || recurso.getEtiquetas().isEmpty()) {
            return false;
        }

        for (String etiqueta : recurso.getEtiquetas()) {
            if (etiqueta != null && etiqueta.toLowerCase().contains(etiquetaBusqueda)) {
                return true;
            }
        }

        return false;
    }
}