/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public enum ServicioLiquidacion {
    ANILLADO("01 - Anillado"),
    ARGOLLADO("02 - Argollado"),
    CARATULA("03 - Carátula"),
    PERFORADO("04 - Perforado"),
    ENGOMADO("05 - Engomado"),
    SCANNER("06 - Scanner"),
    QUEMA_CD("07 - Quema de CD"),
    PASAR_ARCHIVO_PDF("08 - Pasar Archivo PDF");
 
    private final String descripcion;
 
    ServicioLiquidacion(String descripcion) {
        this.descripcion = descripcion;
    }
 
    public String getDescripcion() {
        return descripcion;
    }
 
    @Override
    public String toString() {
        return descripcion;
    }
 
    /** Retorna el ServicioLiquidacion correspondiente a un número (1-8). Retorna null si no es válido. */
    public static ServicioLiquidacion porNumero(int numero) {
        ServicioLiquidacion[] valores = ServicioLiquidacion.values();
        if (numero >= 1 && numero <= valores.length) {
            return valores[numero - 1];
        }
        return null;
    }
 
    /** Genera un String numerado con todos los servicios adicionales. */
    public static String listarServicios() {
        StringBuilder sb = new StringBuilder("=== SERVICIOS DE LIQUIDACIÓN ===\n");
        for (int i = 0; i < values().length; i++) {
            sb.append((i + 1)).append(". ").append(values()[i].getDescripcion()).append("\n");
        }
        return sb.toString();
    }
}