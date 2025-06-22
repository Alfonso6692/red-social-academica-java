package com.utp.redsocial.estructuras;

import java.util.*;
/**
 * Implementación de un Grafo no dirigido usando listas de adyacencia.
 * Ideal para modelar redes sociales.
 * @param <T> El tipo de dato para los vértices del grafo.
 */

public class Grafo<T> {
    private final Map<T, List<T>> listaAdyacencia;

    public Grafo() {
        this.listaAdyacencia = new HashMap<>();
    }

    public void agregarVertice(T vertice) {
        listaAdyacencia.putIfAbsent(vertice, new LinkedList<>());
    }

    public void agregarArista(T origen, T destino) {
        // Asegurarse de que ambos vértices existan en el grafo
        agregarVertice(origen);
        agregarVertice(destino);

        // Agregar arista en ambas direcciones para un grafo no dirigido
        listaAdyacencia.get(origen).add(destino);
        listaAdyacencia.get(destino).add(origen);
    }

    public List<T> obtenerVecinos(T vertice) {
        return listaAdyacencia.getOrDefault(vertice, Collections.emptyList());
    }

    public Set<T> obtenerVertices() {
        return listaAdyacencia.keySet();
    }

    /**
     * Sugiere conexiones (amigos de amigos) para un usuario dado.
     * @param vertice El vértice (usuario) para el que se buscan sugerencias.
     * @return Un mapa con los vértices sugeridos y el número de amigos en común.
     */
    public Map<T, Integer> sugerirConexiones(T vertice) {
        Map<T, Integer> sugerencias = new HashMap<>();
        List<T> amigos = obtenerVecinos(vertice);

        for (T amigo : amigos) {
            List<T> amigosDelAmigo = obtenerVecinos(amigo);
            for (T amigoPotencial : amigosDelAmigo) {
                // No sugerir al mismo usuario ni a alguien que ya es amigo
                if (!amigoPotencial.equals(vertice) && !amigos.contains(amigoPotencial)) {
                    sugerencias.put(amigoPotencial, sugerencias.getOrDefault(amigoPotencial, 0) + 1);
                }
            }
        }
        return sugerencias;
    }
}