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
public class MaquinaRepo{
 
    private List<Maquina> maquinas;
 
    public MaquinaRepo() {
        this.maquinas = new ArrayList<>();
    }
 
    /**
     * Agrega una máquina al repositorio si no existe ya.
     * @return true si se agregó, false si ya existía o no es válida.
     */
    public boolean agregar(Maquina m) {
        if (m == null || !m.esValido()) {
            return false;
        }
        if (existe(m.getMaquina())) {
            return false;
        }
        return maquinas.add(m);
    }
 
    /**
     * Busca una máquina por su código (PK).
     * @return la Maquina encontrada, o null si no existe.
     */
    public Maquina buscarPorCodigo(String maquina) {
        if (maquina == null) return null;
        for (Maquina m : maquinas) {
            if (m.getMaquina().equalsIgnoreCase(maquina.trim())) {
                return m;
            }
        }
        return null;
    }
 
    /**
     * Verifica si existe una máquina con ese código.
     */
    public boolean existe(String maquina) {
        return buscarPorCodigo(maquina) != null;
    }
 
    /**
     * Retorna todas las máquinas registradas.
     */
    public List<Maquina> listarTodas() {
        return new ArrayList<>(maquinas);
    }
 
    /**
     * Retorna la cantidad de máquinas registradas.
     */
    public int cantidad() {
        return maquinas.size();
    }
}
