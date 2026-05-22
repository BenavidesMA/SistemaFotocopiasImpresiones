/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class Solicitante extends Persona implements Validable {

    private String extension;
    private String nombreDependencia;

    public Solicitante(String nombre, String apellido, String extension,
            String cargo, String nombreDependencia) {
        super(nombre, apellido, cargo, nombreDependencia);
        this.extension = extension;
        this.nombreDependencia = nombreDependencia;
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.SOLICITANTE;
    }

    @Override
    public String mostrarInfo() {
        return "=== SOLICITANTE ===\n"
                + "Nombre:      " + nombre + " " + apellido + "\n"
                + "Extensión:   " + extension + "\n"
                + "Cargo:       " + cargo + "\n"
                + "Dependencia: " + nombreDependencia + "\n"
                + "Tipo:        " + getTipoUsuario().getDescripcion();
    }

    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }

    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();

        if (nombre == null || nombre.trim().length() < 7 || nombre.trim().length() > 40) {
            errores.append("- Nombre debe tener entre 7 y 40 caracteres.\n");
        }

        if (apellido == null || apellido.trim().length() < 7 || apellido.trim().length() > 40) {
            errores.append("- Apellido debe tener entre 7 y 40 caracteres.\n");
        }

        if (extension == null || extension.trim().length() != 5) {
            errores.append("- Extensión debe tener exactamente 5 dígitos (ej: 00234).\n");
        } else {
            try {
                int ext = Integer.parseInt(extension.trim());
                if (ext < 100 || ext > 9999) {
                    errores.append("- Extensión debe estar entre 00100 y 09999.\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- Extensión debe ser un número de 5 dígitos.\n");
            }
        }

        if (cargo == null || cargo.trim().length() < 7 || cargo.trim().length() > 20) {
            errores.append("- Cargo debe tener entre 7 y 20 caracteres.\n");
        }

        if (nombreDependencia == null || nombreDependencia.trim().isEmpty()) {
            errores.append("- Debe seleccionar una dependencia válida.\n");
        }

        return errores.toString();
    }

    public String getExtension() {
        return extension;
    }

    public String getNombreDependencia() {
        return nombreDependencia;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setNombreDependencia(String nombreDependencia) {
        this.nombreDependencia = nombreDependencia;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " | Cargo: " + cargo + " | Ext: " + extension + " | " + nombreDependencia;
    }
}
