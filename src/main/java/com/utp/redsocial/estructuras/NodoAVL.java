package com.utp.redsocial.estructuras;

class NodoAVL<T extends Comparable<T>> {
    T elemento;
    int altura;
    NodoAVL<T> izquierdo;
    NodoAVL<T> derecho;

    NodoAVL(T elemento) {
        this.elemento = elemento;
        this.altura = 1; // La altura de un nuevo nodo es siempre 1
    }
}