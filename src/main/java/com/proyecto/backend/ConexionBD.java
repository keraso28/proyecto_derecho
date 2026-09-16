package com.proyecto.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    // ¡Actualizado! Ahora apunta a proyecto_derecho
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_derecho?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // XAMPP viene sin contraseña por defecto

    public static Connection conectar() {
        Connection conexion = null;
        try {
            // Cargar el driver de MySQL que descargamos con Maven
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("¡Conexión exitosa a la base de datos proyecto_derecho!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL. " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
        return conexion;
    }

    // Método main temporal exclusivo para probar que la conexión funciona
    public static void main(String[] args) {
        conectar();
    }
}