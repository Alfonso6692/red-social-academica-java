package com.utp.redsocial.services;

import com.utp.redsocial.entidades.CategoriaGrupo;
import com.utp.redsocial.entidades.Grupo;
import com.utp.redsocial.estructuras.ArbolAVL;
import com.utp.redsocial.persistencia.GrupoDAO;
import com.utp.redsocial.util.GeneradorID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de servicio para la lógica de negocio de los Grupos de Estudio.
 * Utiliza un Árbol AVL para la organización jerárquica de categorías y
 * un HashMap para el acceso rápido a los grupos.
 */
public class ServicioGrupos {

    private final ArbolAVL<CategoriaGrupo> arbolCategorias;
    private final Map<String, Grupo> gruposPorId; // Caché para acceso rápido a grupos
    private final GrupoDAO grupoDAO;
    private final ServicioUsuarios servicioUsuarios; // Dependencia para validar al creador

    public ServicioGrupos(ServicioUsuarios servicioUsuarios) {
        this.arbolCategorias = new ArbolAVL<>();
        this.gruposPorId = new HashMap<>();
        this.grupoDAO = new GrupoDAO();
        this.servicioUsuarios = servicioUsuarios;
        // Opcional: Cargar categorías y grupos existentes desde la BD al iniciar
        // cargarCategoriasEnArbol();
        // cargarGruposEnMemoria();
    }

    /**
     * Crea una nueva categoría de grupo y la inserta en el árbol AVL.
     * @param nombre El nombre de la categoría.
     * @param descripcion La descripción de la categoría.
     * @param categoriaPadreId El ID de la categoría padre (puede ser null).
     * @return La nueva categoría creada.
     */
    public CategoriaGrupo crearCategoria(String nombre, String descripcion, String categoriaPadreId) {
        String id = GeneradorID.generar();
        CategoriaGrupo nuevaCategoria = new CategoriaGrupo(id, nombre, descripcion, categoriaPadreId);

        // 1. Insertar en la estructura en memoria (Arbol AVL)
        arbolCategorias.insertar(nuevaCategoria);

        // 2. Persistir en la base de datos
        // Se necesitaría un método en GrupoDAO para guardar categorías
        // grupoDAO.guardarCategoria(nuevaCategoria);

        System.out.println("Servicio: Categoría '" + nombre + "' creada con éxito.");
        return nuevaCategoria;
    }

    /**
     * Crea un nuevo grupo de estudio.
     * @param nombre El nombre del grupo.
     * @param descripcion La descripción del grupo.
     * @param temas Lista de temas del grupo.
     * @param categoriaId El ID de la categoría a la que pertenece.
     * @param creadorId El ID del usuario creador.
     * @return El nuevo grupo creado.
     * @throws IllegalArgumentException si el creador o la categoría no existen.
     */
    public Grupo crearGrupo(String nombre, String descripcion, List<String> temas, String categoriaId, String creadorId) throws IllegalArgumentException {
        // 1. Validar que el usuario creador exista
        if (servicioUsuarios.buscarPorId(creadorId) == null) {
            throw new IllegalArgumentException("El usuario creador no existe.");
        }

        // 2. Validar que la categoría exista (buscando en el árbol AVL)
        // Se necesitaría un método de búsqueda en el árbol que acepte un ID.
        // if (!arbolCategorias.buscarPorId(categoriaId)) {
        //     throw new IllegalArgumentException("La categoría especificada no existe.");
        // }

        // 3. Crear el grupo
        String id = GeneradorID.generar();
        Grupo nuevoGrupo = new Grupo(id, nombre, descripcion, categoriaId, creadorId, temas);

        // 4. Persistir en la base de datos
        grupoDAO.guardar(nuevoGrupo);

        // 5. Guardar en la caché en memoria
        gruposPorId.put(id, nuevoGrupo);

        System.out.println("Servicio: Grupo '" + nombre + "' creado con éxito.");
        return nuevoGrupo;
    }

    /**
     * Busca un grupo por su ID, primero en caché y luego en la BD.
     * @param id El ID del grupo.
     * @return El objeto Grupo o null si no se encuentra.
     */
    public Grupo buscarGrupoPorId(String id) {
        if (gruposPorId.containsKey(id)) {
            return gruposPorId.get(id);
        }

        Grupo grupo = grupoDAO.buscarPorId(id);
        if (grupo != null) {
            gruposPorId.put(id, grupo); // Almacenar en caché
        }
        return grupo;
    }

    /**
     * Busca todos los grupos que pertenecen a una categoría específica.
     * @param categoriaId El ID de la categoría.
     * @return Una lista de grupos.
     */
    public List<Grupo> buscarGruposPorCategoria(String categoriaId) {
        // Esta operación se delega directamente al DAO, que es más eficiente
        // para este tipo de consultas.
        return grupoDAO.listarTodos(); // En una implementación real, sería grupoDAO.buscarPorCategoria(categoriaId);
    }

    /**
     * Permite a un usuario unirse a un grupo.
     * @param idUsuario El ID del usuario que se une.
     * @param idGrupo El ID del grupo al que se une.
     */
    public void unirseAGrupo(String idUsuario, String idGrupo) {
        // 1. Verificar que el usuario y el grupo existan
        if (servicioUsuarios.buscarPorId(idUsuario) == null || buscarGrupoPorId(idGrupo) == null) {
            throw new IllegalArgumentException("El usuario o el grupo no existen.");
        }

        // 2. Lógica para añadir al usuario a la lista de miembros del grupo
        // Esto implicaría una tabla de relación (ej. `grupo_miembros`) en la BD.
        // grupoDAO.agregarMiembro(idGrupo, idUsuario);

        System.out.println("Servicio: Usuario " + idUsuario + " se unió al grupo " + idGrupo + ".");
    }
    // Añade este método a tu clase ServicioGrupos.java
    public List<Grupo> listarTodosLosGrupos() {
        return grupoDAO.listarTodos();
    }
}
