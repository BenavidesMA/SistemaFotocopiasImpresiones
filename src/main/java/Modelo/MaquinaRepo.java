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
public class MaquinaRepo {

    private List<Maquina> maquinas;

    public MaquinaRepo() {
        this.maquinas = new ArrayList<>();
    }

    public boolean agregar(Maquina m) {
        if (m == null || !m.esValido()) {
            return false;
        }
        if (existe(m.getMaquina())) {
            return false;
        }
        return maquinas.add(m);
    }

    public Maquina buscarPorCodigo(String maquina) {
        if (maquina == null) {
            return null;
        }
        for (Maquina m : maquinas) {
            if (m.getMaquina().equalsIgnoreCase(maquina.trim())) {
                return m;
            }
        }
        return null;
    }

    public boolean existe(String maquina) {
        return buscarPorCodigo(maquina) != null;
    }

    public List<Maquina> listarTodas() {
        return new ArrayList<>(maquinas);
    }

    public int cantidad() {
        return maquinas.size();
    }
}
