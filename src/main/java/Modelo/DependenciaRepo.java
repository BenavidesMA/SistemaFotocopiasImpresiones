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
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Miguel
 */
public class DependenciaRepo {

    private List<Dependencia> dependencias;

    public DependenciaRepo() {
        this.dependencias = new ArrayList<>();
    }

    public Dependencia buscarPorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (Dependencia d : dependencias) {
            if (d.getNombre().equalsIgnoreCase(nombre.trim())) {
                return d;
            }
        }
        return null;
    }

    public boolean existe(String nombre) {
        return buscarPorNombre(nombre) != null;
    }

    public int cantidad() {
        return dependencias.size();
    }

    public boolean dbRegistrar(Dependencia d) {
        if (d == null || !d.esValido()) {
            JOptionPane.showMessageDialog(null,
                    "Datos inválidos:\n" + (d != null ? d.getMensajeValidacion() : "Objeto nulo"),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String sql = "INSERT INTO dependencias (nombre, centro_costo) VALUES (?, ?)";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getCentroCosto());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null,
                    "Dependencia '" + d.getNombre() + "' registrada exitosamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al registrar dependencia:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public ArrayList<Dependencia> dbConsultarTodas() {
        ArrayList<Dependencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM dependencias";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Dependencia(
                        rs.getString("nombre"),
                        rs.getString("centro_costo")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al consultar dependencias:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public Dependencia dbConsultarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT * FROM dependencias WHERE nombre = ?";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Dependencia dep = new Dependencia(
                        rs.getString("nombre"),
                        rs.getString("centro_costo")
                );
                rs.close();
                return dep;
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al buscar dependencia:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public boolean dbEliminar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        String sql = "DELETE FROM dependencias WHERE nombre = ?";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null,
                        "Dependencia '" + nombre + "' eliminada exitosamente.",
                        "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se encontró la dependencia: " + nombre,
                        "No encontrada", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar dependencia:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

}
