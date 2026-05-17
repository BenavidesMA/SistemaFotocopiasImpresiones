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
                                  String cargo, String password, String nombreDependencia) {
        // Llama al constructor de Solicitante, que a su vez llama al de Persona
        super(nombre, apellido, extension, cargo, password, nombreDependencia);
    }
 
    // ── Sobreescritura de métodos de Persona/Solicitante ────────────────────
 
    /** SOBREESCRITURA: retorna OPERARIO_PUBLICACIONES — diferente a Solicitante. */
    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.OPERARIO_PUBLICACIONES;
    }
 
    /** SOBREESCRITURA: añade la línea de acceso a liquidaciones. */
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
 
    /** SOBREESCRITURA: añade "[OPERARIO]" al final del toString de Solicitante. */
    @Override
    public String toString() {
        return super.toString() + " [OPERARIO]";
    }
}