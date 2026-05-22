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
public class AuxiliarRepo {

    private List<Auxiliar> auxiliares;

    public AuxiliarRepo() {
        this.auxiliares = new ArrayList<>();
    }

    public boolean agregar(Auxiliar a) {
        if (a == null || !a.esValido()) {
            return false;
        }
        if (existe(a.getFicha())) {
            return false;
        }
        return auxiliares.add(a);
    }

    public Auxiliar buscarPorFicha(String ficha) {
        if (ficha == null) {
            return null;
        }
        for (Auxiliar a : auxiliares) {
            if (a.getFicha().equalsIgnoreCase(ficha.trim())) {
                return a;
            }
        }
        return null;
    }

    public boolean existe(String ficha) {
        return buscarPorFicha(ficha) != null;
    }

    public List<Auxiliar> listarTodos() {
        return new ArrayList<>(auxiliares);
    }

    public int cantidad() {
        return auxiliares.size();
    }
}
