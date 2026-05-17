/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Miguel
 */
public class OrdenRepo{
 
    private Map<Integer, OrdenAutorizacion> ordenes;
    private int siguienteNumOrden;
 
    public OrdenRepo() {
        this.ordenes           = new HashMap<>();
        this.siguienteNumOrden = 1;
    }
 
    /**
     * Agrega una orden al repositorio si no existe ya.
     * @return true si se agregó, false si ya existía o no es válida.
     */
    public boolean agregar(OrdenAutorizacion orden) {
        if (orden == null || !orden.esValido()) {
            return false;
        }
        if (existe(orden.getNumOrden())) {
            return false;
        }
        ordenes.put(orden.getNumOrden(), orden);
        
        // Actualiza el contador para el siguiente número de orden
        if (orden.getNumOrden() >= siguienteNumOrden) {
            siguienteNumOrden = orden.getNumOrden() + 1;
        }
        
        return true;
    }
 
    /**
     * Busca una orden por su número (PK).
     * @return la OrdenAutorizacion encontrada, o null si no existe.
     */
    public OrdenAutorizacion buscarPorNumero(int numOrden) {
        return ordenes.get(numOrden);
    }
 
    /**
     * Verifica si existe una orden con ese número.
     */
    public boolean existe(int numOrden) {
        return ordenes.containsKey(numOrden);
    }
 
    /**
     * Busca todas las órdenes de un solicitante específico.
     * @param nombreSolicitante  nombre del solicitante.
     * @param apellidoSolicitante apellido del solicitante.
     * @return lista de órdenes del solicitante (vacía si no tiene ninguna).
     */
    public List<OrdenAutorizacion> buscarPorSolicitante(String nombreSolicitante,
                                                          String apellidoSolicitante) {
        List<OrdenAutorizacion> resultado = new ArrayList<>();
        if (nombreSolicitante == null || apellidoSolicitante == null) {
            return resultado;
        }
        
        for (OrdenAutorizacion orden : ordenes.values()) {
            if (orden.getNombreSolicitante().equalsIgnoreCase(nombreSolicitante.trim())
                    && orden.getApellidoSolicitante().equalsIgnoreCase(apellidoSolicitante.trim())) {
                resultado.add(orden);
            }
        }
        return resultado;
    }
 
    /**
     * Retorna todas las órdenes registradas.
     */
    public List<OrdenAutorizacion> listarTodas() {
        return new ArrayList<>(ordenes.values());
    }
 
    /**
     * Genera el siguiente número de orden disponible.
     * @return número de orden único.
     */
    public int generarNumeroOrden() {
        return siguienteNumOrden++;
    }
 
    /**
     * Retorna la cantidad de órdenes registradas.
     */
    public int cantidad() {
        return ordenes.size();
    }
}
