package com.utp.redsocial.services;

import com.utp.redsocial.entidades.Categoria;
import com.utp.redsocial.entidades.Grupo;
import com.utp.redsocial.estructuras.ArbolAVL;
import com.utp.redsocial.persistencia.CategoriaDAO;
import com.utp.redsocial.persistencia.GrupoDAO;
import com.utp.redsocial.util.GeneradorID;
import java.util.List;

/**
 * Capa de servicio para la lógica de negocio de los Grupos y Categorías.
 */
public class ServicioGrupos {

    private final GrupoDAO grupoDAO;
    private final CategoriaDAO categoriaDAO;
    private final ServicioUsuarios servicioUsuarios;
    private final ArbolAVL<Categoria> arbolCategorias;

    /**
     * Constructor del servicio.
     * Recibe las dependencias necesarias para funcionar, las cuales son
     * "inyectadas" por el InicializadorAplicacion.
     */
    public ServicioGrupos(GrupoDAO grupoDAO, CategoriaDAO categoriaDAO, ServicioUsuarios servicioUsuarios) {
        this.grupoDAO = grupoDAO;
        this.categoriaDAO = categoriaDAO;
        this.servicioUsuarios = servicioUsuarios;
        this.arbolCategorias = new ArbolAVL<>();
        // Opcional: Aquí podrías llamar a un método para cargar las categorías desde la BD
    }

    /**
     * Crea una nueva categoría y la persiste en la base de datos.
     */
    public Categoria agregarCategoria(String nombre, String descripcion, String categoriaPadreId) {
        String id = GeneradorID.generar();
        Categoria nuevaCategoria = new Categoria(id, nombre, descripcion, categoriaPadreId);

        // Inserta en la estructura en memoria (si la usas)
        arbolCategorias.insertar(nuevaCategoria);

        // Persiste en la base de datos usando el DAO correcto
        categoriaDAO.guardar(nuevaCategoria);

        System.out.println("Servicio: Categoría '" + nombre + "' creada con éxito.");
        return nuevaCategoria;
    }

    /**
     * Crea un nuevo grupo de estudio.
     */
    public Grupo crearGrupo(String nombre, String descripcion, List<String> temas, String categoriaId, String creadorId) throws IllegalArgumentException {
        if (servicioUsuarios.buscarPorId(creadorId) == null) {
            throw new IllegalArgumentException("El usuario creador no existe.");
        }

        String id = GeneradorID.generar();
        Grupo nuevoGrupo = new Grupo(id, nombre, descripcion, categoriaId, creadorId, temas);

        grupoDAO.guardar(nuevoGrupo);

        System.out.println("Servicio: Grupo '" + nombre + "' creado con éxito.");
        return nuevoGrupo;
    }

    /**
     * Obtiene una lista de todos los grupos registrados.
     */
    public List<Grupo> listarTodosLosGrupos() {
        return grupoDAO.listarTodos();
    }
}
