/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import BaseDatos.BaseDatos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

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
    public boolean dbRegistrarOrden(OrdenAutorizacion o) {
        if (o == null || !o.esValido()) {
            JOptionPane.showMessageDialog(null,
                "Datos inválidos:\n" + (o != null ? o.getMensajeValidacion() : "Objeto nulo"),
                "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String sql = "INSERT INTO ordenes_autorizacion "
            + "(num_orden, tipo_orden, fecha_solicitud, observaciones, "
            + "firma_autorizada, nombre_solicitante, apellido_solicitante) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql)) {
            ps.setInt   (1, o.getNumOrden());
            ps.setString(2, o.getTipoOrden().getDescripcion());
            ps.setString(3, o.getFechaSolicitud());
            ps.setString(4, o.getObservaciones());
            ps.setString(5, o.getFirmaAutorizada());
            ps.setString(6, o.getNombreSolicitante());
            ps.setString(7, o.getApellidoSolicitante());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null,
                "Orden N° " + o.getNumOrden() + " registrada exitosamente.",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error al registrar orden:\n" + e.getMessage(),
                "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public ArrayList<String> dbConsultarTodasSolicitudes() {
        ArrayList<String> lista = new ArrayList<>();
        String sql =
            "SELECT sol.titulo_trabajo, sol.num_orden, sol.original, sol.reproducciones, "
            + "soli.nombre, soli.apellido, soli.cargo, soli.nombre_dependencia, "
            + "ss.num_servicio "
            + "FROM solicitudes sol "
            + "INNER JOIN ordenes_autorizacion o   ON sol.num_orden = o.num_orden "
            + "INNER JOIN solicitantes soli          ON o.nombre_solicitante  = soli.nombre "
            + "                                     AND o.apellido_solicitante = soli.apellido "
            + "LEFT JOIN solicitudes_servicio ss    ON ss.num_orden      = sol.num_orden "
            + "                                     AND ss.titulo_trabajo = sol.titulo_trabajo";

        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String fila =
                    "Orden: "        + rs.getInt   ("num_orden")          + " | "
                    + "Trabajo: "    + rs.getString("titulo_trabajo")      + " | "
                    + "Original: "   + rs.getInt   ("original")           + " | "
                    + "Reprod: "     + rs.getInt   ("reproducciones")      + " | "
                    + "Solicitante: "+ rs.getString("nombre")              + " "
                    +                  rs.getString("apellido")            + " | "
                    + "Cargo: "      + rs.getString("cargo")               + " | "
                    + "Dep: "        + rs.getString("nombre_dependencia")  + " | "
                    + "Servicio N°: "+ rs.getInt   ("num_servicio");
                lista.add(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error al consultar solicitudes:\n" + e.getMessage(),
                "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public ArrayList<String> dbConsultarPorNumOrden(int numOrden) {
        ArrayList<String> lista = new ArrayList<>();
        String sql =
            "SELECT sol.titulo_trabajo, sol.num_orden, sol.original, sol.reproducciones, "
            + "soli.nombre, soli.apellido, soli.cargo, soli.nombre_dependencia, "
            + "ss.num_servicio "
            + "FROM solicitudes sol "
            + "INNER JOIN ordenes_autorizacion o   ON sol.num_orden = o.num_orden "
            + "INNER JOIN solicitantes soli          ON o.nombre_solicitante  = soli.nombre "
            + "                                     AND o.apellido_solicitante = soli.apellido "
            + "LEFT JOIN solicitudes_servicio ss    ON ss.num_orden      = sol.num_orden "
            + "                                     AND ss.titulo_trabajo = sol.titulo_trabajo "
            + "WHERE sol.num_orden = ?";

        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql)) {
            ps.setInt(1, numOrden);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String fila =
                    "Orden: "        + rs.getInt   ("num_orden")          + " | "
                    + "Trabajo: "    + rs.getString("titulo_trabajo")      + " | "
                    + "Original: "   + rs.getInt   ("original")           + " | "
                    + "Reprod: "     + rs.getInt   ("reproducciones")      + " | "
                    + "Solicitante: "+ rs.getString("nombre")              + " "
                    +                  rs.getString("apellido")            + " | "
                    + "Cargo: "      + rs.getString("cargo")               + " | "
                    + "Dep: "        + rs.getString("nombre_dependencia")  + " | "
                    + "Servicio N°: "+ rs.getInt   ("num_servicio");
                lista.add(fila);
            }
            rs.close();

            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "No se encontró ninguna solicitud con el número de orden: " + numOrden,
                    "No encontrada", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error al consultar por número de orden:\n" + e.getMessage(),
                "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

}
