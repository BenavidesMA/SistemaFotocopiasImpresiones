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
public class Solicitud implements Validable {
 
    private String tituloTrabajo;  // PK parcial, 3-40 caracteres
    private int    numOrden;       // PK parcial, FK
    private int    original;       // 1-500
    private int    reproducciones; // 1-500 (copias o impresiones)
 
    private List<SolicitudServicio> solicitudServicios;
 
    public Solicitud(String tituloTrabajo, int numOrden, int original, int reproducciones) {
        this.tituloTrabajo      = tituloTrabajo;
        this.numOrden           = numOrden;
        this.original           = original;
        this.reproducciones     = reproducciones;
        this.solicitudServicios = new ArrayList<>();
    }
 
    // ── Validable ─────────────────────────────────────────────────────────────
 
    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }
 
    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();
 
        if (tituloTrabajo == null || tituloTrabajo.trim().length() < 3
                                  || tituloTrabajo.trim().length() > 40)
            errores.append("- Título del trabajo debe tener entre 3 y 40 caracteres.\n");
 
        if (original < 1 || original > 500)
            errores.append("- El número de originales debe estar entre 1 y 500.\n");
 
        if (reproducciones < 1 || reproducciones > 500)
            errores.append("- El número de reproducciones debe estar entre 1 y 500.\n");
 
        return errores.toString();
    }
 
    // ── Método para agregar servicios al trabajo ──────────────────────────────
 
    public void agregarSolicitudServicio(SolicitudServicio ss) {
        solicitudServicios.add(ss);
    }
 
    // ── Getters ────────────────────────────────────────────────────────────────
 
    public String                  getTituloTrabajo()      { return tituloTrabajo; }
    public int                     getNumOrden()           { return numOrden; }
    public int                     getOriginal()           { return original; }
    public int                     getReproducciones()     { return reproducciones; }
    public List<SolicitudServicio> getSolicitudServicios() { return solicitudServicios; }
 
    @Override
    public String toString() {
        return "\"" + tituloTrabajo + "\""
             + "  Original: " + original
             + "  Reproducciones: " + reproducciones;
    }
}