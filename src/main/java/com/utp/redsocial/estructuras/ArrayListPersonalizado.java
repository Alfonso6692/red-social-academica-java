package com.utp.redsocial.estructuras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Implementación de un ArrayList personalizado con capacidad de expansión dinámica.
 * Permite operaciones de ordenamiento y filtrado.
 * @param <T> El tipo de dato que almacenará la lista.
 */
public class ArrayListPersonalizado<T> {
    private Object[] elementos;
    private int tamano;
    private static final int CAPACIDAD_INICIAL = 10;

    public ArrayListPersonalizado() {
        elementos = new Object[CAPACIDAD_INICIAL];
        tamano = 0;
    }

    public void agregar(T elemento) {
        if (tamano == elementos.length) {
            expandirCapacidad();
        }
        elementos[tamano++] = elemento;
    }

    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        return (T) elementos[indice];
    }

    public void eliminar(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        int numElementosAMover = tamano - indice - 1;
        if (numElementosAMover > 0) {
            System.arraycopy(elementos, indice + 1, elementos, indice, numElementosAMover);
        }
        elementos[--tamano] = null; // Limpiar para permitir al recolector de basura
    }

    @SuppressWarnings("unchecked")
    public void ordenar(Comparator<T> comparador) {
        Arrays.sort((T[]) elementos, 0, tamano, comparador);
    }

    /**
     * Filtra los elementos de la lista basados en un predicado.
     * Esta versión utiliza un bucle 'for' para máxima compatibilidad y
     * para evitar errores de compilación relacionados con la API de Streams en el IDE.
     * @param predicado La condición para filtrar los elementos.
     * @return Una nueva lista con los elementos que cumplen la condición.
     */
    public List<T> filtrar(Predicate<T> predicado) {
        List<T> resultado = new ArrayList<>();
        for (int i = 0; i < tamano; i++) {
            @SuppressWarnings("unchecked")
            T elemento = (T) elementos[i];
            if (predicado.test(elemento)) {
                resultado.add(elemento);
            }
        }
        return resultado;
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacio() {
        return tamano == 0;
    }

    private void expandirCapacidad() {
        int nuevaCapacidad = elementos.length * 2;
        elementos = Arrays.copyOf(elementos, nuevaCapacidad);
    }
}
