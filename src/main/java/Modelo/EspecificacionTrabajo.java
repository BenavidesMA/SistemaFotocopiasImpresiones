/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class EspecificacionTrabajo implements Validable {
 
    private int     numOrden;     // PK parcial, FK
    private String  formatoPapel; // PK parcial: "CARTA" o "OFICIO"
    private boolean traePapel;    // true = marcó "SÍ"
    private int     cantidad;     // 1-500
    private String  tipoPapel;    // 4-31 caracteres
 
    public EspecificacionTrabajo(int numOrden, String formatoPapel, boolean traePapel,
                                  int cantidad, String tipoPapel) {
        this.numOrden    = numOrden;
        this.formatoPapel = formatoPapel.toUpperCase();
        this.traePapel   = traePapel;
        this.cantidad    = cantidad;
        this.tipoPapel   = tipoPapel;
    }
 
    // ── Validable ─────────────────────────────────────────────────────────────
 
    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }
 
    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();
 
        if (formatoPapel == null
                || (!formatoPapel.equalsIgnoreCase("CARTA")
                &&  !formatoPapel.equalsIgnoreCase("OFICIO")))
            errores.append("- Formato de papel debe ser CARTA u OFICIO.\n");
 
        if (cantidad < 1 || cantidad > 500)
            errores.append("- Cantidad debe estar entre 1 y 500.\n");
 
        if (tipoPapel == null || tipoPapel.trim().length() < 4 || tipoPapel.trim().length() > 31)
            errores.append("- Tipo de papel debe tener entre 4 y 31 caracteres.\n");
 
        return errores.toString();
    }
 
    // ── Getters ────────────────────────────────────────────────────────────────
 
    public int     getNumOrden()     { return numOrden; }
    public String  getFormatoPapel() { return formatoPapel; }
    public boolean isTraePapel()     { return traePapel; }
    public int     getCantidad()     { return cantidad; }
    public String  getTipoPapel()    { return tipoPapel; }
 
    @Override
    public String toString() {
        return "Formato: " + formatoPapel
             + "  Cantidad: " + cantidad
             + "  Tipo papel: " + tipoPapel
             + "  Trae papel: " + (traePapel ? "Sí" : "No");
    }
}
