package com.utp.redsocial.estructuras;

import java.util.NoSuchElementException;

public class Cola<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanno;

    public Cola() {
        this.cabeza = null;
        this.cola = null;
        this.tamanno = 0;
    }

    public void encolar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (estaVacia()) {
            cabeza = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
        }
        cola = nuevoNodo;
        tamanno++;
    }

    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        T elemento = cabeza.elemento;
        cabeza = cabeza.siguiente;
        if (cabeza == null) {
            cola = null;
        }
        tamanno--;
        return elemento;
    }

    public T primero() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return cabeza.elemento;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamanno() {
        return tamanno;
    }
}