package com.utp.redsocial.estructuras;


import java.util.NoSuchElementException;
public class ListaDoblementeEnlazada<T> {
    private NodoDoble<T> cabeza;
    private NodoDoble<T> cola;
    private int tamanno;

    public ListaDoblementeEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamanno = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     * @param elemento El elemento a agregar.
     */
    public void agregar(T elemento) {
        NodoDoble<T> nuevoNodo = new NodoDoble<>(elemento);
        if (estaVacia()) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo;
        }
        tamanno++;
    }

    /**
     * Inserta un elemento en una posición específica.
     * @param elemento El elemento a insertar.
     * @param posicion La posición donde insertar el elemento.
     * @throws IndexOutOfBoundsException si la posición está fuera de rango.
     */
    public void insertar(T elemento, int posicion) {
        if (posicion < 0 || posicion > tamanno) {
            throw new IndexOutOfBoundsException("Posición fuera de rango: " + posicion);
        }
        if (posicion == tamanno) {
            agregar(elemento);
            return;
        }

        NodoDoble<T> nuevoNodo = new NodoDoble<>(elemento);
        if (posicion == 0) {
            nuevoNodo.siguiente = cabeza;
            cabeza.anterior = nuevoNodo;
            cabeza = nuevoNodo;
        } else {
            NodoDoble<T> actual = cabeza;
            for (int i = 0; i < posicion - 1; i++) {
                actual = actual.siguiente;
            }
            nuevoNodo.siguiente = actual.siguiente;
            nuevoNodo.anterior = actual;
            actual.siguiente.anterior = nuevoNodo;
            actual.siguiente = nuevoNodo;
        }
        tamanno++;
    }

    /**
     * Elimina y devuelve el elemento en una posición específica.
     * @param posicion La posición del elemento a eliminar.
     * @return El elemento eliminado.
     * @throws IndexOutOfBoundsException si la posición está fuera de rango.
     */
    public T eliminar(int posicion) {
        if (posicion < 0 || posicion >= tamanno) {
            throw new IndexOutOfBoundsException("Posición fuera de rango: " + posicion);
        }

        T elementoEliminado;
        if (posicion == 0) {
            elementoEliminado = cabeza.elemento;
            cabeza = cabeza.siguiente;
            if (cabeza != null) {
                cabeza.anterior = null;
            } else {
                cola = null;
            }
        } else if (posicion == tamanno - 1) {
            elementoEliminado = cola.elemento;
            cola = cola.anterior;
            cola.siguiente = null;
        } else {
            NodoDoble<T> actual = cabeza;
            for (int i = 0; i < posicion; i++) {
                actual = actual.siguiente;
            }
            elementoEliminado = actual.elemento;
            actual.anterior.siguiente = actual.siguiente;
            actual.siguiente.anterior = actual.anterior;
        }
        tamanno--;
        return elementoEliminado;
    }

    /**
     * Obtiene el elemento en una posición específica.
     * @param posicion La posición del elemento a obtener.
     * @return El elemento en la posición dada.
     * @throws IndexOutOfBoundsException si la posición está fuera de rango.
     */
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanno) {
            throw new IndexOutOfBoundsException("Posición fuera de rango: " + posicion);
        }
        NodoDoble<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }
        return actual.elemento;
    }

    /**
     * Busca un elemento en la lista y devuelve su índice.
     * @param elemento El elemento a buscar.
     * @return El índice de la primera ocurrencia del elemento, o -1 si no se encuentra.
     */
    public int buscar(T elemento) {
        NodoDoble<T> actual = cabeza;
        for (int i = 0; i < tamanno; i++) {
            if (actual.elemento.equals(elemento)) {
                return i;
            }
            actual = actual.siguiente;
        }
        return -1;
    }

    public int tamanno() {
        return tamanno;
    }

    public boolean estaVacia() {
        return tamanno == 0;
    }
}
