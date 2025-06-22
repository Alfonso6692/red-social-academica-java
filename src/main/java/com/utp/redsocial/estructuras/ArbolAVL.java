package com.utp.redsocial.estructuras;

public class ArbolAVL<T extends Comparable<T>> {

    private NodoAVL<T> raiz;

    private int altura(NodoAVL<T> nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    private int max(int a, int b) {
        return Math.max(a, b);
    }

    private NodoAVL<T> rotacionDerecha(NodoAVL<T> y) {
        NodoAVL<T> x = y.izquierdo;
        NodoAVL<T> T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        y.altura = max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = max(altura(x.izquierdo), altura(x.derecho)) + 1;

        return x;
    }

    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> x) {
        NodoAVL<T> y = x.derecho;
        NodoAVL<T> T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        x.altura = max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = max(altura(y.izquierdo), altura(y.derecho)) + 1;

        return y;
    }

    private int getFactorBalance(NodoAVL<T> nodo) {
        return (nodo == null) ? 0 : altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    public void insertar(T elemento) {
        raiz = insertar(raiz, elemento);
    }

    private NodoAVL<T> insertar(NodoAVL<T> nodo, T elemento) {
        if (nodo == null) {
            return (new NodoAVL<>(elemento));
        }

        if (elemento.compareTo(nodo.elemento) < 0) {
            nodo.izquierdo = insertar(nodo.izquierdo, elemento);
        } else if (elemento.compareTo(nodo.elemento) > 0) {
            nodo.derecho = insertar(nodo.derecho, elemento);
        } else { // Elementos duplicados no son permitidos
            return nodo;
        }

        nodo.altura = 1 + max(altura(nodo.izquierdo), altura(nodo.derecho));
        int balance = getFactorBalance(nodo);

        // Caso Izquierda Izquierda
        if (balance > 1 && elemento.compareTo(nodo.izquierdo.elemento) < 0) {
            return rotacionDerecha(nodo);
        }

        // Caso Derecha Derecha
        if (balance < -1 && elemento.compareTo(nodo.derecho.elemento) > 0) {
            return rotacionIzquierda(nodo);
        }

        // Caso Izquierda Derecha
        if (balance > 1 && elemento.compareTo(nodo.izquierdo.elemento) > 0) {
            nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }

        // Caso Derecha Izquierda
        if (balance < -1 && elemento.compareTo(nodo.derecho.elemento) < 0) {
            nodo.derecho = rotacionDerecha(nodo.derecho);
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    public boolean buscar(T elemento) {
        return buscar(raiz, elemento);
    }

    private boolean buscar(NodoAVL<T> nodo, T elemento) {
        if (nodo == null) {
            return false;
        }

        if (elemento.compareTo(nodo.elemento) == 0) {
            return true;
        }

        return elemento.compareTo(nodo.elemento) < 0
                ? buscar(nodo.izquierdo, elemento)
                : buscar(nodo.derecho, elemento);
    }
}
