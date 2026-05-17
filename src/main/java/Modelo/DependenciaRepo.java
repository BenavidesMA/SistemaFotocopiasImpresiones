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
public class DependenciaRepo{
 
    private List<Dependencia> dependencias;
 
    public DependenciaRepo() {
        this.dependencias = new ArrayList<>();
    }
 
    /**
     * Agrega una dependencia al repositorio si no existe ya.
     * @return true si se agregó, false si ya existía.
     */
    public boolean agregar(Dependencia d) {
        if (d == null || !d.esValido()) {
            return false;
        }
        if (existe(d.getNombre())) {
            return false;
        }
        return dependencias.add(d);
    }
 
    /**
     * Busca una dependencia por su nombre (PK).
     * @return la Dependencia encontrada, o null si no existe.
     */
    public Dependencia buscarPorNombre(String nombre) {
        if (nombre == null) return null;
        for (Dependencia d : dependencias) {
            if (d.getNombre().equalsIgnoreCase(nombre.trim())) {
                return d;
            }
        }
        return null;
    }
 
    /**
     * Verifica si existe una dependencia con ese nombre.
     */
    public boolean existe(String nombre) {
        return buscarPorNombre(nombre) != null;
    }
 
    /**
     * Retorna todas las dependencias registradas.
     */
    public List<Dependencia> listarTodas() {
        return new ArrayList<>(dependencias);
    }
    
    public boolean eliminar(String nombre) {
    for (Dependencia d : dependencias) {
        if (d.getNombre().equalsIgnoreCase(nombre)) {
            dependencias.remove(d);
            return true;
        }
    }
    return false;
}
 
    /**
     * Retorna la cantidad de dependencias registradas.
     */
    public int cantidad() {
        return dependencias.size();
    }
}
