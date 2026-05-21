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
public class OrdenAutorizacion implements Validable {
 
    // ── Datos básicos (secciones 1-2 del formulario) ─────────────────────────
    private int numOrden;
    private TipoOrden tipoOrden;
    private String fechaSolicitud;      // formato dd-mm-aaaa
    private String observaciones;       // opcional
    private String firmaAutorizada;
    private String nombreSolicitante;   // FK
    private String apellidoSolicitante; // FK
 
    // ── Entidades dependientes (agregación) ───────────────────────────────────
    private List<Servicio>             servicios;              // sección 2 - servicios marcados
    private List<Solicitud>            solicitudes;            // sección 3 - trabajos solicitados
    private List<EspecificacionTrabajo> especificaciones;      // sección 4 - specs de papel
    private List<LiquidacionOperativa>  liquidacionesOperativas;  // sección 6 - operativa
    private List<LiquidacionAdicional>  liquidacionesAdicionales; // sección 6 - servicios adicionales
    private LiquidacionFinal            liquidacionFinal;         // sección 6 - cierre
 
    public OrdenAutorizacion(int numOrden, TipoOrden tipoOrden, String fechaSolicitud,
                              String observaciones, String firmaAutorizada,
                              String nombreSolicitante, String apellidoSolicitante) {
        this.numOrden             = numOrden;
        this.tipoOrden            = tipoOrden;
        this.fechaSolicitud       = fechaSolicitud;
        this.observaciones        = observaciones;
        this.firmaAutorizada      = firmaAutorizada;
        this.nombreSolicitante    = nombreSolicitante;
        this.apellidoSolicitante  = apellidoSolicitante;
 
        this.servicios                = new ArrayList<>();
        this.solicitudes              = new ArrayList<>();
        this.especificaciones         = new ArrayList<>();
        this.liquidacionesOperativas  = new ArrayList<>();
        this.liquidacionesAdicionales = new ArrayList<>();
    }
 
    // ── Implementación de Validable ──────────────────────────────────────────
 
    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }
 
    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();
 
        if (numOrden < 1 || numOrden > 99999)
            errores.append("- Número de orden debe estar entre 1 y 99999.\n");
 
        if (tipoOrden == null)
            errores.append("- Debe seleccionar un tipo de orden (Fotocopias o Impresiones).\n");
 
        if (fechaSolicitud == null || !fechaSolicitud.matches("\\d{4}-\\d{2}-\\d{2}"))
            errores.append("- Fecha de solicitud debe tener formato dd-mm-aaaa (ej: 2026-03-22).\n");
 
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            if (observaciones.trim().length() < 10 || observaciones.trim().length() > 250)
                errores.append("- Observaciones debe tener entre 10 y 250 caracteres si se ingresa.\n");
        }
 
        if (firmaAutorizada == null || firmaAutorizada.trim().length() < 1
                                    || firmaAutorizada.trim().length() > 20)
            errores.append("- Firma autorizada debe tener entre 1 y 20 caracteres.\n");
 
        if (nombreSolicitante == null || nombreSolicitante.trim().isEmpty())
            errores.append("- Nombre del solicitante es obligatorio.\n");
 
        if (apellidoSolicitante == null || apellidoSolicitante.trim().isEmpty())
            errores.append("- Apellido del solicitante es obligatorio.\n");
 
        return errores.toString();
    }
 
    // ── Métodos para agregar entidades dependientes ───────────────────────────
    public void agregarServicio(Servicio s)                       { servicios.add(s); }
    public void agregarSolicitud(Solicitud s)                     { solicitudes.add(s); }
    public void agregarEspecificacion(EspecificacionTrabajo e)    { especificaciones.add(e); }
    public void agregarLiquidacionOperativa(LiquidacionOperativa lo)  { liquidacionesOperativas.add(lo); }
    public void agregarLiquidacionAdicional(LiquidacionAdicional la)  { liquidacionesAdicionales.add(la); }
    public void setLiquidacionFinal(LiquidacionFinal lf)              { this.liquidacionFinal = lf; }
 
    // ── Getters ─────────────────────────────────────────────────────────────
    public int getNumOrden()                              { return numOrden; }
    public TipoOrden getTipoOrden()                       { return tipoOrden; }
    public String getFechaSolicitud()                     { return fechaSolicitud; }
    public String getObservaciones()                      { return observaciones; }
    public String getFirmaAutorizada()                    { return firmaAutorizada; }
    public String getNombreSolicitante()                  { return nombreSolicitante; }
    public String getApellidoSolicitante()                { return apellidoSolicitante; }
    public List<Servicio> getServicios()                  { return servicios; }
    public List<Solicitud> getSolicitudes()               { return solicitudes; }
    public List<EspecificacionTrabajo> getEspecificaciones() { return especificaciones; }
    public List<LiquidacionOperativa> getLiquidacionesOperativas()   { return liquidacionesOperativas; }
    public List<LiquidacionAdicional> getLiquidacionesAdicionales()  { return liquidacionesAdicionales; }
    public LiquidacionFinal getLiquidacionFinal()         { return liquidacionFinal; }
 
    @Override
    public String toString() {
        return "Orden #" + String.format("%05d", numOrden)
             + " | " + tipoOrden.getDescripcion()
             + " | " + fechaSolicitud
             + " | " + nombreSolicitante + " " + apellidoSolicitante;
    }
}