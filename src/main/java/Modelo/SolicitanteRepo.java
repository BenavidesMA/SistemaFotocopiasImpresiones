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
public class SolicitanteRepo{
 
    private List<Solicitante> solicitantes;
 
    public SolicitanteRepo() {
        this.solicitantes = new ArrayList<>();
    }
 
    /**
     * Agrega un solicitante al repositorio si no existe ya.
     * POLIMORFISMO: puede recibir un Solicitante o un OperarioPublicaciones.
     * @return true si se agregó, false si ya existía o no es válido.
     */
    public boolean agregar(Solicitante s) {
        if (s == null || !s.esValido()) {
            return false;
        }
        if (existe(s.getExtension())) {
            return false;
        }
        return solicitantes.add(s);
    }
 
    /**
     * Busca un solicitante por su extensión (PK).
     * POLIMORFISMO: puede retornar un Solicitante o un OperarioPublicaciones.
     * @return el Solicitante encontrado, o null si no existe.
     */
    public Solicitante buscarPorExtension(String extension) {
        if (extension == null) return null;
        for (Solicitante s : solicitantes) {
            if (s.getExtension().equals(extension.trim())) {
                return s;
            }
        }
        return null;
    }
 
    /**
     * Verifica si existe un solicitante con esa extensión.
     */
    public boolean existe(String extension) {
        return buscarPorExtension(extension) != null;
    }
 
    /**
     * Autentica un usuario por extensión y contraseña.
     * POLIMORFISMO: retorna un Solicitante que puede ser en realidad un OperarioPublicaciones.
     * El llamador puede verificar getTipoUsuario() para saber el rol real.
     *
     * @param extension extensión telefónica (usuario).
     * @param password  contraseña.
     * @return el Solicitante autenticado, o null si las credenciales son incorrectas.
     */
    public Solicitante autenticar(String extension, String password) {
        Solicitante s = buscarPorExtension(extension);
        if (s != null && s.getPassword().equals(password)) {
            return s;
        }
        return null;
    }
 
    /**
     * Retorna todos los solicitantes registrados.
     * POLIMORFISMO: la lista contiene objetos Solicitante y OperarioPublicaciones.
     */
    public List<Solicitante> listarTodos() {
        return new ArrayList<>(solicitantes);
    }
 
    /**
     * Retorna la cantidad de solicitantes registrados.
     */
    public int cantidad() {
        return solicitantes.size();
    }
}
