package org.example.dao;

import org.example.config.ConexionBD;
import org.example.modelo.Operacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OperacionDAO {

    public boolean guardar(Operacion operacion) {

        String sql = """
                INSERT INTO operaciones
                (numero1, numero2, operacion, resultado)
                VALUES (?,?,?,?)
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, operacion.getNumero1());
            ps.setDouble(2, operacion.getNumero2());
            ps.setString(3, operacion.getOperacion());
            ps.setDouble(4, operacion.getResultado());

            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error guardando operación: "
                    + e.getMessage());

            return false;
        }
    }
}
