package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Recurso;
import com.utp.redsocial.estructuras.ArrayListPersonalizado;
import com.utp.redsocial.persistencia.RecursoDAO;
import com.utp.redsocial.util.GeneradorID;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de servicio para la lógica de negocio de los Recursos Educativos.
 * Utiliza un ArrayListPersonalizado para gestionar los recursos en memoria
 * y un HashMap como índice para búsquedas rápidas por etiquetas.
 */
public class ServicioRecursos {

    private final ArrayListPersonalizado<Recurso> listaDeRecursos;
    private final Map<String, List<Recurso>> indicePorEtiqueta; // Tabla Hash para búsqueda rápida
    private final RecursoDAO recursoDAO;
    private final ServicioUsuarios servicioUsuarios; // Para validar al usuario que sube el recurso

    // Asegúrate de que esté así:
    public ServicioRecursos(ServicioUsuarios servicioUsuarios) {
        this.listaDeRecursos = new ArrayListPersonalizado<>();
        this.indicePorEtiqueta = new HashMap<>();
        this.recursoDAO = new RecursoDAO();  // ✅ Con 'new'
        this.servicioUsuarios = servicioUsuarios;
    }

    /**
     * Sube un nuevo recurso educativo al sistema.
     * @param titulo El título del recurso.
     * @param descripcion La descripción del recurso.
     * @param url La URL para acceder al recurso.
     * @param tipo El tipo de recurso (ej. PDF, Video, Enlace).
     * @param etiquetas Una lista de etiquetas para categorizar el recurso.
     * @param idUsuarioSube El ID del usuario que sube el recurso.
     * @return El nuevo recurso creado.
     * @throws IllegalArgumentException si el usuario no existe.
     */
    public Recurso subirRecurso(String titulo, String descripcion, String url, String tipo, List<String> etiquetas, String idUsuarioSube) throws IllegalArgumentException {
        // 1. Validar que el usuario exista
        if (servicioUsuarios.buscarPorId(idUsuarioSube) == null) {
            throw new IllegalArgumentException("El usuario que intenta subir el recurso no existe.");
        }

        // 2. Crear el objeto Recurso
        String id = GeneradorID.generar();
        Recurso nuevoRecurso = new Recurso(id, titulo, descripcion, url, tipo, LocalDate.now(), etiquetas);

        // 3. Persistir en la base de datos
        recursoDAO.guardar(nuevoRecurso);

        // 4. Añadir a la estructura en memoria
        listaDeRecursos.agregar(nuevoRecurso);

        // 5. Actualizar el índice de búsqueda por etiquetas
        actualizarIndice(nuevoRecurso);

        System.out.println("Servicio: Recurso '" + titulo + "' subido con éxito.");
        return nuevoRecurso;
    }

    /**
     * Busca recursos que coincidan con una etiqueta específica.
     * La búsqueda es muy rápida gracias al índice (Tabla Hash).
     * @param etiqueta La etiqueta a buscar.
     * @return Una lista de recursos que contienen esa etiqueta.
     */
    public List<Recurso> buscarPorEtiqueta(String etiqueta) {
        return indicePorEtiqueta.getOrDefault(etiqueta.toLowerCase(), new ArrayList<>());
    }

    /**
     * Obtiene todos los recursos del sistema.
     * @return Una lista de todos los recursos.
     */
    public List<Recurso> listarTodos() {
        // En una implementación real, se podría implementar paginación
        return recursoDAO.listarTodos();
    }

    /**
     * Busca un recurso específico por su ID.
     * @param id El ID del recurso.
     * @return El objeto Recurso o null si no se encuentra.
     */
    public Recurso buscarPorId(String id) {
        // Primero busca en la lista en memoria (aunque no es lo más eficiente)
        for(int i = 0; i < listaDeRecursos.tamano(); i++) {
            if (listaDeRecursos.obtener(i).getId().equals(id)) {
                return listaDeRecursos.obtener(i);
            }
        }
        // Si no está en memoria, busca en la BD
        return recursoDAO.buscarPorId(id);
    }


    /**
     * Método privado para añadir un recurso al índice de búsqueda.
     * @param recurso El recurso a indexar.
     */
    private void actualizarIndice(Recurso recurso) {
        for (String etiqueta : recurso.getEtiquetas()) {
            String etiquetaNormalizada = etiqueta.toLowerCase().trim();
            // Si la etiqueta no existe en el mapa, crea una nueva lista
            indicePorEtiqueta.putIfAbsent(etiquetaNormalizada, new ArrayList<>());
            // Añade el recurso a la lista de esa etiqueta
            indicePorEtiqueta.get(etiquetaNormalizada).add(recurso);
        }
    }
}
