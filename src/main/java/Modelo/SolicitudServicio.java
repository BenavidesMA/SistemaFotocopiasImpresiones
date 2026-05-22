/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class SolicitudServicio {

    private String tituloTrabajo;
    private int numOrden;
    private TipoServicio tipoServicio;

    public SolicitudServicio(String tituloTrabajo, int numOrden, TipoServicio tipoServicio) {
        this.tituloTrabajo = tituloTrabajo;
        this.numOrden = numOrden;
        this.tipoServicio = tipoServicio;
    }

    public String getTituloTrabajo() {
        return tituloTrabajo;
    }

    public int getNumOrden() {
        return numOrden;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    @Override
    public String toString() {
        return tipoServicio.getDescripcion() + "  (trabajo: \"" + tituloTrabajo + "\")";
    }
}
