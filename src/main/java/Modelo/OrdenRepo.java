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
public class OrdenRepo {

    private Map<Integer, OrdenAutorizacion> ordenes;
    private int siguienteNumOrden;

    public OrdenRepo() {
        this.ordenes = new HashMap<>();
        this.siguienteNumOrden = 1;
    }

    public boolean agregar(OrdenAutorizacion orden) {
        if (orden == null || !orden.esValido()) {
            return false;
        }
        if (existe(orden.getNumOrden())) {
            return false;
        }
        ordenes.put(orden.getNumOrden(), orden);

        if (orden.getNumOrden() >= siguienteNumOrden) {
            siguienteNumOrden = orden.getNumOrden() + 1;
        }

        return true;
    }

    public OrdenAutorizacion buscarPorNumero(int numOrden) {
        return ordenes.get(numOrden);
    }

    public boolean existe(int numOrden) {
        return ordenes.containsKey(numOrden);
    }

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

    public List<OrdenAutorizacion> listarTodas() {
        return new ArrayList<>(ordenes.values());
    }

    public int generarNumeroOrden() {
        return siguienteNumOrden++;
    }

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
            ps.setInt(1, o.getNumOrden());
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

    public ArrayList<String> dbConsultarTodasOrdenes() {
        ArrayList<String> lista = new ArrayList<>();

        String sql
                = "SELECT o.num_orden, o.tipo_orden, o.fecha_solicitud, o.observaciones, o.firma_autorizada, "
                + "o.nombre_solicitante, o.apellido_solicitante, "
                + "s.nombre, s.apellido, s.extension, s.cargo, s.nombre_dependencia, "
                + "sv.servicio_seleccionado "
                + "FROM ordenes_autorizacion o "
                + "INNER JOIN solicitantes s "
                + "    ON o.nombre_solicitante = s.nombre "
                + "   AND o.apellido_solicitante = s.apellido "
                + "LEFT JOIN servicios sv "
                + "    ON sv.num_orden = o.num_orden "
                + "ORDER BY o.num_orden";

        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            int ordenActual = -1;
            StringBuilder fila = new StringBuilder();
            StringBuilder servicios = new StringBuilder();

            while (rs.next()) {
                int numOrden = rs.getInt("num_orden");
                String servicio = rs.getString("servicio_seleccionado");

                if (ordenActual != numOrden) {
                    if (fila.length() > 0) {
                        fila.append(" | Servicios: ")
                                .append(servicios.length() > 0 ? servicios.toString() : "N/A");
                        lista.add(fila.toString());
                    }

                    ordenActual = numOrden;
                    fila = new StringBuilder();
                    servicios = new StringBuilder();

                    fila.append("Orden: ").append(rs.getInt("num_orden")).append(" | ")
                            .append("Tipo solicitud: ").append(rs.getString("tipo_orden")).append(" | ")
                            .append("Fecha solicitud: ").append(rs.getString("fecha_solicitud")).append(" | ")
                            .append("Observaciones: ").append(rs.getString("observaciones")).append(" | ")
                            .append("Firma autorizada: ").append(rs.getString("firma_autorizada")).append(" | ")
                            .append("Solicitante: ").append(rs.getString("nombre_solicitante")).append(" ")
                            .append(rs.getString("apellido_solicitante")).append(" | ")
                            .append("Extensión: ").append(rs.getString("extension")).append(" | ")
                            .append("Cargo: ").append(rs.getString("cargo")).append(" | ")
                            .append("Dependencia: ").append(rs.getString("nombre_dependencia"));
                }

                if (servicio != null) {
                    if (servicios.length() > 0) {
                        servicios.append(", ");
                    }
                    servicios.append(servicio);
                }
            }

            if (fila.length() > 0) {
                fila.append(" | Servicios: ")
                        .append(servicios.length() > 0 ? servicios.toString() : "N/A");
                lista.add(fila.toString());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al consultar órdenes:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
        }

        return lista;
    }

    public ArrayList<String> dbConsultarPorNumOrden(int numOrden) {

        ArrayList<String> lista = new ArrayList<>();

        String sql
                = "SELECT o.num_orden, o.tipo_orden, o.fecha_solicitud, "
                + "o.observaciones, o.firma_autorizada, "
                + "o.nombre_solicitante, o.apellido_solicitante, "
                + "s.nombre, s.apellido, s.extension, s.cargo, s.nombre_dependencia, "
                + "sv.servicio_seleccionado "
                + "FROM ordenes_autorizacion o "
                + "INNER JOIN solicitantes s "
                + "ON o.nombre_solicitante = s.nombre "
                + "AND o.apellido_solicitante = s.apellido "
                + "LEFT JOIN servicios sv "
                + "ON sv.num_orden = o.num_orden "
                + "WHERE o.num_orden = ?";

        try (PreparedStatement ps
                = BaseDatos.dbConnection.prepareStatement(sql)) {

            ps.setInt(1, numOrden);

            ResultSet rs = ps.executeQuery();

            String servicios = "";
            boolean primeraFila = true;
            String fila = "";

            while (rs.next()) {

                String servicio = rs.getString("servicio_seleccionado");

                if (servicio != null) {

                    if (!servicios.isEmpty()) {
                        servicios += ", ";
                    }

                    servicios += servicio;
                }

                if (primeraFila) {

                    fila
                            = "Orden: " + rs.getInt("num_orden") + " | "
                            + "Tipo solicitud: " + rs.getString("tipo_orden") + " | "
                            + "Fecha solicitud: " + rs.getString("fecha_solicitud") + " | "
                            + "Observaciones: " + rs.getString("observaciones") + " | "
                            + "Firma autorizada: " + rs.getString("firma_autorizada") + " | "
                            + "Solicitante: " + rs.getString("nombre_solicitante") + " "
                            + rs.getString("apellido_solicitante") + " | "
                            + "Extensión: " + rs.getString("extension") + " | "
                            + "Cargo: " + rs.getString("cargo") + " | "
                            + "Dependencia: " + rs.getString("nombre_dependencia");

                    primeraFila = false;
                }

            }

            rs.close();

            if (!fila.isEmpty()) {

                fila += " | Servicios: "
                        + (servicios.isEmpty() ? "N/A" : servicios);

                lista.add(fila);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al consultar:\n" + e.getMessage(),
                    "Error SQL",
                    JOptionPane.ERROR_MESSAGE
            );

        }

        return lista;
    }

    public boolean dbRegistrarServicios(OrdenAutorizacion o) {

        if (o == null || o.getServicios().isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO servicios (num_orden, servicio_seleccionado) VALUES (?, ?)";

        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql)) {

            for (Servicio s : o.getServicios()) {
                ps.setInt(1, o.getNumOrden());
                ps.setString(2, s.getServicioSeleccionado().name());
                ps.addBatch();
            }

            ps.executeBatch();
            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al registrar servicios:\n" + e.getMessage(),
                    "Error SQL",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }

    public int dbObtenerSiguienteNumOrden() {

        String sql
                = "SELECT COALESCE(MAX(num_orden),0)+1 AS siguiente "
                + "FROM ordenes_autorizacion";

        try (
                PreparedStatement ps
                = BaseDatos.dbConnection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("siguiente");
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error obteniendo número de orden:\n"
                    + e.getMessage()
            );

        }

        return 1;
    }

}
