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
public class SolicitanteRepo {

    private List<Solicitante> solicitantes;

    public SolicitanteRepo() {
        this.solicitantes = new ArrayList<>();
    }

    public boolean agregar(Solicitante s) {
        if (s == null || !s.esValido()) {
            return false;
        }
        if (existe(s.getExtension())) {
            return false;
        }
        return solicitantes.add(s);
    }

    public Solicitante buscarPorExtensionBD(String extension) {
        if (extension == null) {
            return null;
        }
        String sql = "SELECT nombre, apellido, extension, cargo, nombre_dependencia "
                + "FROM solicitantes WHERE extension = ?";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql)) {
            ps.setString(1, extension.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Solicitante s = new Solicitante(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("extension"),
                        rs.getString("cargo"),
                        rs.getString("nombre_dependencia")
                );
                rs.close();
                return s;
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al buscar solicitante:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public Solicitante buscarOperarioPorExtension(String extension) {
        if (extension == null) {
            return null;
        }
        for (Solicitante s : solicitantes) {
            if (s instanceof OperarioPublicaciones
                    && s.getExtension().equals(extension.trim())) {
                return s;
            }
        }
        return null;
    }

    public boolean existe(String extension) {
        return buscarPorExtensionBD(extension) != null
                || buscarOperarioPorExtension(extension) != null;
    }

    public Solicitante autenticar(String extension) {
        if (extension == null) {
            return null;
        }

        Solicitante s = buscarPorExtensionBD(extension);
        if (s != null) {
            return s;
        }

        return buscarOperarioPorExtension(extension);
    }

    public List<Solicitante> listarTodos() {
        return new ArrayList<>(solicitantes);
    }

    public int cantidad() {
        return solicitantes.size();
    }

    public boolean dbRegistrar(Solicitante s) {
        if (s == null || !s.esValido()) {
            JOptionPane.showMessageDialog(null,
                    "Datos inválidos:\n" + (s != null ? s.getMensajeValidacion() : "Objeto nulo"),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sqlVerificar = "SELECT COUNT(*) FROM dependencias WHERE nombre = ?";
        try (PreparedStatement psVerif = BaseDatos.dbConnection.prepareStatement(sqlVerificar)) {
            psVerif.setString(1, s.getNombreDependencia());
            ResultSet rs = psVerif.executeQuery();
            rs.next();
            int cantidad = rs.getInt(1);
            rs.close();

            if (cantidad == 0) {
                JOptionPane.showMessageDialog(null,
                        "La dependencia '" + s.getNombreDependencia() + "' no existe en el sistema.\n"
                        + "Por favor registre primero la dependencia o verifique el nombre ingresado.",
                        "Dependencia no encontrada", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al verificar dependencia:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sqlInsertar = "INSERT INTO solicitantes "
                + "(nombre, apellido, extension, cargo, nombre_dependencia) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sqlInsertar)) {
            ps.setString(1, s.getNombre());
            ps.setString(2, s.getApellido());
            ps.setString(3, s.getExtension());
            ps.setString(4, s.getCargo());
            ps.setString(5, s.getNombreDependencia());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Solicitante '" + s.getNombre() + " " + s.getApellido() + "' registrado exitosamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al registrar solicitante:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public ArrayList<Solicitante> dbConsultarBasicosTodos() {
        ArrayList<Solicitante> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitantes";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Solicitante(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("extension"),
                        rs.getString("cargo"),
                        rs.getString("nombre_dependencia")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al consultar solicitantes:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public ArrayList<String> dbConsultarConDependencia() {
        ArrayList<String> lista = new ArrayList<>();
        String sql
                = "SELECT s.extension, s.nombre, s.apellido, s.cargo,"
                + "s.nombre_dependencia, d.centro_costo "
                + "FROM solicitantes s "
                + "INNER JOIN dependencias d ON s.nombre_dependencia = d.nombre";

        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String fila
                        = "Ext: " + rs.getString("extension") + " | "
                        + "Nombre: " + rs.getString("nombre") + " "
                        + rs.getString("apellido") + " | "
                        + "Cargo: " + rs.getString("cargo") + " | "
                        + "Dep: " + rs.getString("nombre_dependencia") + " | "
                        + "CC: " + rs.getString("centro_costo");
                lista.add(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al consultar solicitantes con dependencia:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    public Solicitante dbConsultarPorNombre(String nombre, String apellido) {
        String sql = "SELECT nombre, apellido, extension, cargo, nombre_dependencia "
                + "FROM solicitantes WHERE nombre = ? AND apellido = ?";
        try (PreparedStatement ps = BaseDatos.dbConnection.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            ps.setString(2, apellido.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Solicitante s = new Solicitante(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("extension"),
                        rs.getString("cargo"),
                        rs.getString("nombre_dependencia")
                );
                rs.close();
                return s;
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al consultar solicitante:\n" + e.getMessage(),
                    "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
