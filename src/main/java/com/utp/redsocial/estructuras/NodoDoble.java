package com.utp.redsocial.estructuras;
/**
 * Representa un nodo para ser usado en una ListaDoblementeEnlazada.
 * Contiene un elemento de datos y referencias al nodo anterior y siguiente.
 * @param <T> El tipo de dato que almacenará el nodo.
 */


class NodoDoble<T> {
    T elemento;
    NodoDoble<T> siguiente;
    NodoDoble<T> anterior;

    public NodoDoble(T elemento) {
        this.elemento = elemento;
        this.siguiente = null;
        this.anterior = null;
    }
}
