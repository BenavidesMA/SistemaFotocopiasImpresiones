/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel
 */
public class AuxiliarRepo{
 
    private List<Auxiliar> auxiliares;
 
    public AuxiliarRepo() {
        this.auxiliares = new ArrayList<>();
    }
 
    /**
     * Agrega un auxiliar al repositorio si no existe ya.
     * @return true si se agregó, false si ya existía o no es válido.
     */
    public boolean agregar(Auxiliar a) {
        if (a == null || !a.esValido()) {
            return false;
        }
        if (existe(a.getFicha())) {
            return false;
        }
        return auxiliares.add(a);
    }
 
    /**
     * Busca un auxiliar por su ficha (PK).
     * @return el Auxiliar encontrado, o null si no existe.
     */
    public Auxiliar buscarPorFicha(String ficha) {
        if (ficha == null) return null;
        for (Auxiliar a : auxiliares) {
            if (a.getFicha().equalsIgnoreCase(ficha.trim())) {
                return a;
            }
        }
        return null;
    }
 
    /**
     * Verifica si existe un auxiliar con esa ficha.
     */
    public boolean existe(String ficha) {
        return buscarPorFicha(ficha) != null;
    }
 
    /**
     * Retorna todos los auxiliares registrados.
     */
    public List<Auxiliar> listarTodos() {
        return new ArrayList<>(auxiliares);
    }
 
    /**
     * Retorna la cantidad de auxiliares registrados.
     */
    public int cantidad() {
        return auxiliares.size();
    }
}
