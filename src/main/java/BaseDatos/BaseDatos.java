package BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class BaseDatos {

    // Atributos
    public static Connection dbConnection;
    static Statement stSQL;
    static String nameDB;
    static String user;
    static String pwd;

    // Constructor
    public BaseDatos() {

    }

    // Crear conexión
    public boolean crearConexion() {

        nameDB = "esquema_db_act";
        user = "root";
        pwd = "benavides2302";

        try {

            // Cargar Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // URL de conexión
            String sourceURL = "jdbc:mysql://localhost:3306/"
                    + nameDB;
                    //"?useSSL=false&serverTimezone=UTC";

            // Conectar
            dbConnection = DriverManager.getConnection(sourceURL, user, pwd);

            JOptionPane.showMessageDialog(null,
                    "¡¡ Conexión exitosa con el sistema !!");

        } catch (ClassNotFoundException | SQLException evt) {

            System.err.println(evt);

            JOptionPane.showMessageDialog(null,
                    "Error en la conexión");

            return false;
        }

        return true;
    }

    // Cerrar conexión
    public void cerrarConexion() {

        if (dbConnection != null) {

            try {

                dbConnection.close();

                System.out.println("Conexión cerrada");

                dbConnection = null;

            } catch (SQLException evt) {

                System.out.println("Error al cerrar conexión");

                System.err.println(evt);
            }
        }
    }
}