package BaseDatos;

import Modelo.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Sandra L. Guañarita F.
 */
public class BaseDatos {

    //Atributos de clase
    static Connection dbConnection; // clase especial en JDBC
    static Statement stSQL; // clase especial en JDBC
    static String nameDB ;
    static String user;
    static String pwd;
    
    
    /**
     * Constructor - Creates a new instance of BaseDatos
     */
    public BaseDatos() {
        
    }
    
    //Creando la conexión
    public boolean crearConexion() {
        
        nameDB = "DB_Prueba_ProyectoFI"; //modificar con el nombre de su base de datos
        user = "root";
        pwd = "benavides2302";

        try {
            // Cargar la clase Driver
            Class.forName("com.mysql.jdbc.Driver");

            //Definir el origen de los datos del driver
            String sourceURL = "jdbc:mysql:///" + nameDB;
            
            //Siguientes líneas cortesía José Emanuel Bolaños Salamanca
            //Para versión MySQL versión 8.0 desbloquear línea siguiente y bloquear línea anterior
            //String sourceURL =jdbc:mysql://localhost:3306/NombreBD?characterEncoding=latin1&useConfigs=maxPerformance
            //Dentro de MySQL ejecutar línea siguiente
            //ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
            
            // Crear una conexion a traves del DriverManager
            dbConnection
                    = DriverManager.getConnection(sourceURL, user, pwd);
            
            //Mensaje si la conexión con "nameDB" se establece
            JOptionPane.showMessageDialog(null,"!! Conexion con la base de datos " + nameDB + " establecida exitosamente !!");
            
        }catch(ClassNotFoundException | SQLException evt){
        	System.err.println(evt);
                System.out.println("!! Conexión con la base de datos " + nameDB + " fallida !!");
                return false;
                //Si la conexión no se puede establecer, arroja mensaje:
                    //nameDB no existe: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown database 'nameDB'
                    //user incorrecto: java.sql.SQLException: Access denied for user 'admon'@'localhost' (using password: YES)
                    //pwd incorrecto: java.sql.SQLException: Access denied for user 'root'@'localhost' (using password: YES)
        }
        return true;
    }//fin method
    
    //Cerrar la conexión
    public void cerrarConexion() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
                System.out.println("!!Cierre exitoso de la conexion con la base de datos " + nameDB + "!!");
                dbConnection = null;
            } catch (SQLException evt) {
                System.out.println("!!Cierre fallido de la conexion con la base de datos " + nameDB + "!!");
                System.err.println("\n SQLException -----------\n");
                System.err.println("SQLState: " + evt.getSQLState());
                System.err.println("Message: " + evt.getMessage());
            }
        }
    }}
   /*
   // Inserta una fila en la tabla PROFESORES de la base de datos "nameDB"
    public boolean insertarDependencia(DependenciaRepo dependencia) throws SQLException{     
        // Crear el objeto Statement que permita la ejecución del SQL
        stSQL = dbConnection.createStatement();
        
       //Proceso para insertar una fila en una tabla
        String nameTable="";
        try {
            //Configurando la sentencia a ejecutar
            nameTable = "Profesores";
            String sqlString = "INSERT INTO" + " " + nameTable
                    + "(cedula, tipo_cedula, nombres, apellidos, ultimo_nivel_formacion, titulo_ultimo_nivel_formacion) "
                    + "VALUES('" 
                    + profesor.getCedula() + "','" 
                    + profesor.getTipoCedula() + "','"
                    + profesor.getNombres() + "','" 
                    + profesor.getApellidos() + "','" 
                    + profesor.getUltimoNivelFormacion() + "','" 
                    + profesor.getTituloUltimoNivelFormacion() + "')";
            //Se ejecuta la sentencia SQL de actualización tipo "INSERT" 
            //Nota. Si desea ver el resultado, ingresar a la base de datos indicada en nameDB y consultar la tabla
            stSQL.executeUpdate(sqlString);
            
            //Mensaje si la sentencia "sqlString" tipo INSERT se ejecuta correctamente
            System.out.println("!! La tabla " + nameTable + " fue actualizada exitosamente, 1 fila creada !!"); 
            
        }catch(SQLException evt){
                System.out.println("!!Operación de inserción en la tabla" + nameTable + " fallida.!!");
        	System.err.println(evt);
                return false;
        }
        return true;       
    } //fin method
           
    // Consultar todos los PROFESORES de la base de datos "nameDB"
    public ArrayList consultarProfesoresAll() throws SQLException {
        ArrayList<DependenciaRepo> lista = new ArrayList(); //Crear la lista para almacenar resultados de la consulta
        int cedula;
        char tipoCedula; //C:ciudadanía, E: extranjería
        String nombres;
        String apellidos;
        String ultimoNivelFormacion; // PREGRADO (Default), ESPECIALIZACION, MAESTRIA, DOCTORADO
        String tituloUltimoNivelFormacion; //ej. Ingeniero Informático, Especialista en Gerencia de Proyectos
        Profesor objProfesor;  

        //Conformando sentencia SQL
        stSQL = dbConnection.createStatement();
        String nameTable = "Profesores";
        String sql = "SELECT * FROM " + nameTable;

        //Ejecutando sentencia
        ResultSet rs = stSQL.executeQuery(sql);
        
        // Mostrar los datos del ResultSet
        while (rs.next()) {
            //Extraer datos de rs
            cedula = rs.getInt("cedula");
            tipoCedula= rs.getString("tipo_cedula").charAt(0);
            nombres= rs.getString("nombres");
            apellidos= rs.getString("apellidos");
            ultimoNivelFormacion= rs.getString("ultimo_nivel_formacion");
            tituloUltimoNivelFormacion= rs.getString("titulo_ultimo_nivel_formacion");
            //Crear objeto para almacenar en ArrayList
            objProfesor = new Profesor(cedula,tipoCedula,nombres,apellidos,ultimoNivelFormacion,tituloUltimoNivelFormacion);
            //Agregando objeto a la lista
            lista.add(objProfesor);
        }
        
        return lista;
    }//fin method
    
    // Consultar con condición en tabla PROFESORES de la base de datos "nameDB"
    public ArrayList consultarProfesorCedula(int numeroCedula) throws SQLException {
        ArrayList<Profesor> lista = new ArrayList();
        int cedula;
        char tipoCedula; //C:ciudadanía, E: extranjería
        String nombres;
        String apellidos;
        String ultimoNivelFormacion; //PREGRADO (Default), ESPECIALIZACION, MAESTRIA, DOCTORADO
        String tituloUltimoNivelFormacion; //ej. Ingeniero Informático, Especialista en Gerencia de Proyectos
        Profesor objProfesor;  

        //Conformando sentencia SQL
        stSQL = dbConnection.createStatement();
        
        String nameTable = "Profesores";
        String sql = "SELECT * FROM" + " " + nameTable
                        + " " + "WHERE cedula="+ numeroCedula;
        //Ejecutando sentencia
        ResultSet rs = stSQL.executeQuery(sql);
        
        // Mostrar los datos del ResultSet
        while (rs.next()) {
            //Extraer datos de rs
            cedula = rs.getInt("cedula");
            tipoCedula= rs.getString("tipo_cedula").charAt(0);
            nombres= rs.getString("nombres");
            apellidos= rs.getString("apellidos");
            ultimoNivelFormacion= rs.getString("ultimo_nivel_formacion");
            tituloUltimoNivelFormacion= rs.getString("titulo_ultimo_nivel_formacion");
            //Crear objeto para almacenar en ArrayList
            objProfesor = new Profesor(cedula,tipoCedula,nombres,apellidos,ultimoNivelFormacion,tituloUltimoNivelFormacion);
            //Agregando objeto a la lista
            lista.add(objProfesor);
        }
        
        return lista;
    }
    
    // Eliminar fila(s) de la tabla PROFESORES de la base de datos "nameDB"
    public void eliminarProfesorCedula(int numeroCedula) throws SQLException{        
        stSQL = dbConnection.createStatement();
        String nameTable="";
        try {
            //Configurando la sentencia a ejecutar
            nameTable = "Profesores";
            String sqlString = "DELETE FROM " + nameTable
                                + " WHERE cedula="+ numeroCedula;
            //Ejecutando sentencia 
            stSQL.executeUpdate(sqlString);
            
            //Mensaje de operación exitosa
            System.out.println("!!Operación de borrado de una fila en la tabla " + nameTable + " exitosa.!!");
        }catch(SQLException evt){
                System.out.println("!!Operación de borrado de una fila en la tabla " + nameTable + " fallida.!!");
                System.err.println(evt);
        }    
    } //fin method
    
    // Eliminar fila(s) de la tabla PROFESORES de la base de datos "nameDB"
    public void modificarProfesorCedula(int numeroCedula, char columna, String nuevoValor) throws SQLException{  
        
        String setColumnaValor="SET ";
        //Nota: //si el dato es de naturaleza numérica se omiten las comillas del valor
        switch(columna){
            case '1': 
                setColumnaValor+= "TIPO_CEDULA=" + "'" + nuevoValor + "'";
                break;
            case '2': 
                setColumnaValor+= "NOMBRES=" + "'" + nuevoValor + "'"; 
                break;
            case '3': 
                setColumnaValor+= "APELLIDOS=" + "'" + nuevoValor + "'"; 
                break;
            case '4': 
                setColumnaValor+= "ULTIMO_NIVEL_FORMACION=" + "'" + nuevoValor + "'"; 
                break;
            case '5': 
                setColumnaValor+= "TITULO_ULTIMO_NIVEL_FORMACION=" + "'" + nuevoValor + "'"; 
                break;            
        }
        
        stSQL = dbConnection.createStatement();
        String nameTable="";
        try {
            //Configurando la sentencia a ejecutar
            nameTable = "Profesores";
            String sqlString = "UPDATE" + " " + nameTable + " " + setColumnaValor
                                + " " + "WHERE cedula="+ numeroCedula;
            
            //Se ejecuta la sentencia SQL de actualización tipo "UPDATE". 
            //Nota. Si desea ver el resultado, ingresar a la base de datos "nameDB" y consultar la tabla "nameTable"
            stSQL.executeUpdate(sqlString);
            
            //Mensaje si la sentencia "sqlString" tipo UPDATE se ejecuta correctamente
            System.out.println("!! La tabla " + nameTable + " fue actualizada exitosamente, 1 fila actualizada !!");  
            
        }catch(SQLException evt){
                System.out.println("!!Operación de modificación de una fila en la tabla " + nameTable + " fallida.!!");
        	System.err.println(evt);
        }    
    } //fin method
    
    
} //fin class

*/