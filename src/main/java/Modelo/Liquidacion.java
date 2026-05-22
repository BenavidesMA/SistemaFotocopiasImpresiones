/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public abstract class Liquidacion {

    protected int numOrden;

    public Liquidacion(int numOrden) {
        this.numOrden = numOrden;
    }

    public abstract double calcularTotal();

    public abstract String mostrarResumen();

    public int getNumOrden() {
        return numOrden;
    }

    @Override
    public String toString() {
        return "Liquidación Orden #" + numOrden
                + "  |  Total: $" + String.format("%.2f", calcularTotal());
    }
}
