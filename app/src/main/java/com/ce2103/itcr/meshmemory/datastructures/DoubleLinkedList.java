package com.ce2103.itcr.meshmemory.datastructures;

import com.google.gson.JsonObject;

/**
 * @author estape11
 * Double linked list for any type of objects
 */

public class DoubleLinkedList {
    /**
     * Inner class of node, its for link the data
     */
    private class Nodo {
        Object dato;
        Nodo siguiente;

        public Nodo(Object elem) {
            dato = elem;
            siguiente = null;
        }
    }

    private Nodo cabeza;
    private Nodo cola;
    private int numElementos;

    /**
     * DLL constructor
     */
    public DoubleLinkedList() {
        cabeza = null;
        numElementos = 0;
    }

    /**
     * Adds elements to the top of the list
     * @param elem
     */
    public void add(Object elem) {
        if (numElementos == 0) {
            cabeza = new Nodo(elem);
        } else {
            obtenerNodo(numElementos - 1).siguiente = new Nodo(elem);
        }
        numElementos++;
    }

    /**
     * Gets the data container node by index
     * @param indice
     * @return Nodo
     */
    public Nodo obtenerNodo(int indice) {
        if (indice >= numElementos || indice < 0) {
            throw new IndexOutOfBoundsException("Indice incorrecto:" + indice);
        }

        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++)
            actual = actual.siguiente;
        return actual;
    }

    /**
     * Gets the specific index in the lista
     * @param elem
     * @return int of the index
     */
    public int indexOf(Object elem) {
        int indice;
        boolean encuentra = false;
        Nodo actual = cabeza;
        for (indice = 0; actual != null; indice++, actual = actual.siguiente) {
            if ((actual.dato != null && actual.dato.equals(elem)) || ((actual.dato == null) && (elem == null))) {
                encuentra = true;
                break;
            }
        }
        if (encuentra == false)
            indice = -1;
        return indice;
    }

    /**
     * Removes one of the nodes in the list by index
     * @param indice
     * @return Object(generic class)
     */
    public Object remove(int indice) {
        Nodo actual = null;
        Nodo anterior = null;
        if (indice > 0) {
            anterior = obtenerNodo(indice - 1);
            actual = anterior.siguiente;
            anterior.siguiente = actual.siguiente;
            numElementos--;
        }
        if (indice == 0) {
            actual = cabeza;
            cabeza = cabeza.siguiente;
            numElementos--;
        }
        if (actual != null)
            return actual.dato;
        else
            return null;
    }

    public int remove(Object elem) {
        int actual = indexOf(elem);
        if (actual != -1)
            remove(actual);

        return actual;
    }

    /**
     * Gets the data of the node by index
     * @param indice
     * @return generic object
     */
    public Object get(int indice) {
        return obtenerNodo(indice).dato;
    }

    public int size() {
        return numElementos;
    }

    /**
     * Search the Nodo by index and change the value of data
     * @param indice
     * @param nuevodato
     * @return
     */
    public Nodo swapData(int indice, JsonObject nuevodato) {
        if (indice >= numElementos || indice < 0) {
            throw new IndexOutOfBoundsException("Indice incorrecto:" + indice);
        }
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++)
            actual = actual.siguiente;
        actual.dato=nuevodato;
        return actual;
    }

}
