/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class Servicio {

    private int numOrden;
    private TipoServicio servicioSeleccionado;

    public Servicio(int numOrden, TipoServicio servicioSeleccionado) {
        this.numOrden = numOrden;
        this.servicioSeleccionado = servicioSeleccionado;
    }

    public int getNumOrden() {
        return numOrden;
    }

    public TipoServicio getServicioSeleccionado() {
        return servicioSeleccionado;
    }

    @Override
    public String toString() {
        return servicioSeleccionado.getDescripcion();
    }
}
