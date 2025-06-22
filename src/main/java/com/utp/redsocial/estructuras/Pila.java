package com.utp.redsocial.estructuras;
import java.util.EmptyStackException;

public class Pila<T> {
    private Nodo<T> tope;
    private int tamanno;

    public Pila() {
        this.tope = null;
        this.tamanno = 0;
    }

    public void push(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.siguiente = tope;
        tope = nuevoNodo;
        tamanno++;
    }

    public T pop() {
        if (estaVacia()) {
            throw new EmptyStackException();
        }
        T elemento = tope.elemento;
        tope = tope.siguiente;
        tamanno--;
        return elemento;
    }

    public T peek() {
        if (estaVacia()) {
            throw new EmptyStackException();
        }
        return tope.elemento;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int tamanno() {
        return tamanno;
    }
}