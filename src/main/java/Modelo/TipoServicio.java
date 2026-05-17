/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public enum TipoServicio {
    FOTOCOPIA_CARTA("01 - Fotocopia Carta"),
    FOTOCOPIA_OFICIO("02 - Fotocopia Oficio"),
    FOTOCOPIA_LADO_POR_LADO("03 - Fotocopia Lado por Lado"),
    DUPLICACION_OFF_SET("04 - Duplicación Off Set"),
    AMPLIACION("05 - Ampliación"),
    REDUCCION("06 - Reducción"),
    QUEMA_ACETATO("07 - Quema de Acetato"),
    ACETATO_COMPLETO("08 - Acetato Completo"),
    HOJAS_BLANCO("09 - Hojas en Blanco"),
    ANILLADO_PLASTICO("10 - Anillado Plástico"),
    ARGOLLADO_DOBLE_O("11 - Argollado Doble O"),
    ENGOMADO("12 - Engomado"),
    PERFORADO("13 - Perforado"),
    CARTULINA("14 - Cartulina"),
    IMPRESIONES_BLANCO_NEGRO("15 - Impresiones Blanco y Negro"),
    IMPRESION_COLOR("16 - Impresión Color"),
    FOTOCOPIA_COLOR("17 - Fotocopia Color"),
    IMPRESION_LADO_X_LADO("18 - Impresión Lado x Lado"),
    ACETATO_BLANCO_NEGRO("19 - Acetato Blanco y Negro"),
    ACETATO_COLOR("20 - Acetato Color"),
    LABEL_CD("21 - Label de CD"),
    QUEMA_CD("22 - Quema de CD"),
    SCANNER("23 - Scanner"),
    PASAR_ARCHIVO_PDF("24 - Pasar Archivo PDF");
 
    private final String descripcion;
 
    TipoServicio(String descripcion) {
        this.descripcion = descripcion;
    }
 
    public String getDescripcion() {
        return descripcion;
    }
 
    @Override
    public String toString() {
        return descripcion;
    }
 
    /** Retorna el TipoServicio correspondiente a un número (1-24). Retorna null si no es válido. */
    public static TipoServicio porNumero(int numero) {
        TipoServicio[] valores = TipoServicio.values();
        if (numero >= 1 && numero <= valores.length) {
            return valores[numero - 1];
        }
        return null;
    }
 
    /** Genera un String numerado con todos los servicios para mostrarlo en JOptionPane. */
    public static String listarServicios() {
        StringBuilder sb = new StringBuilder("=== SERVICIOS DISPONIBLES ===\n");
        for (int i = 0; i < values().length; i++) {
            sb.append((i + 1)).append(". ").append(values()[i].getDescripcion()).append("\n");
        }
        return sb.toString();
    }
}
 