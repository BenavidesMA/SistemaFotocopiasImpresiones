/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class OperarioPublicaciones extends Solicitante {

    public OperarioPublicaciones(String nombre, String apellido, String extension,
            String cargo, String nombreDependencia) {

        super(nombre, apellido, extension, cargo, nombreDependencia);
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.OPERARIO_PUBLICACIONES;
    }

    @Override
    public String mostrarInfo() {
        return "=== OPERARIO DE PUBLICACIONES ===\n"
                + "Nombre:      " + getNombre() + " " + getApellido() + "\n"
                + "Extensión:   " + getExtension() + "\n"
                + "Cargo:       " + getCargo() + "\n"
                + "Dependencia: " + getNombreDependencia() + "\n"
                + "Tipo:        " + getTipoUsuario().getDescripcion() + "\n"
                + "[Tiene acceso al módulo de Liquidaciones]";
    }

    @Override
    public String toString() {
        return super.toString() + " [OPERARIO]";
    }
}
