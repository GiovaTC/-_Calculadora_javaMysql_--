package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/calculadora_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "Tapiero123";

    public static Connection obtenerConexion() {

        try {
            Connection conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    PASSWORD
            );

            System.out.println("conexion exitosa a MYSQL!");
            return conexion;

        } catch (SQLException e) {

            System.out.println("Error de conexion: "
                    + e.getMessage());

            return null;
        }
    }
}
