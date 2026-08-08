# -_Calculadora_javaMysql_--

🧮 Calculadora de Escritorio en Java + MySQL:

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/fa98e72c-6087-4bee-8c40-d1fa124f3cb7" />  

<img width="2551" height="1037" alt="image" src="https://github.com/user-attachments/assets/91a953b7-d9af-40fc-b233-f4fa852cc467" />    

```
Proyecto básico de una calculadora de escritorio desarrollada con Java 21, IntelliJ IDEA, Java Swing, MySQL 8 y JDBC .
Es una aplicación sencilla y apropiada para practicar programación orientada a objetos, interfaces gráficas y conexión de Java con MySQL.

Características:

La aplicación tendrá:
🖥️ Interfaz gráfica con Java Swing.
➕ Suma.
➖ Resta.
✖️ Multiplicación.
➗ División.
🧹 Botón limpiar.
💾 Guardado de cada operación en MySQL.
📋 Consulta del historial de operaciones mediante MySQL.
🔌 Conexión mediante JDBC.
📦 Separación en config, modelo, dao y vista.

1. Estructura del proyecto

CalculadoraJavaMySQL/
│
├── src/
│   ├── config/
│   │   └── ConexionBD.java
│   │
│   ├── modelo/
│   │   └── Operacion.java
│   │
│   ├── dao/
│   │   └── OperacionDAO.java
│   │
│   ├── vista/
│   │   └── Calculadora.java
│   │
│   └── Main.java
│
└── pom.xml

2. Base de datos MySQL

Primero podemos crear una base de datos llamada calculadora_db.

Crear la base de datos y la tabla

CREATE DATABASE calculadora_db;

USE calculadora_db;

CREATE TABLE operaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero1 DOUBLE NOT NULL,
    numero2 DOUBLE NOT NULL,
    operacion VARCHAR(20) NOT NULL,
    resultado DOUBLE NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Comprobar los datos

SELECT * FROM operaciones;

3. pom.xml

Si utilizas Maven en IntelliJ IDEA, utiliza el siguiente archivo:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.calculadora</groupId>
    <artifactId>CalculadoraJavaMySQL</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <!-- MySQL JDBC -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.4.0</version>
        </dependency>

    </dependencies>

</project>

4. ConexionBD.java

Esta clase será responsable de conectarse a MySQL.

package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/calculadora_db";

    private static final String USUARIO = "root";

    private static final String PASSWORD = "123456";

    public static Connection obtenerConexion() {

        try {

            Connection conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    PASSWORD
            );

            System.out.println("Conexión exitosa a MySQL.");

            return conexion;

        } catch (SQLException e) {

            System.out.println("Error de conexión: "
                    + e.getMessage());

            return null;
        }
    }
}

Importante: cambia 123456 por la contraseña de tu instalación de MySQL.

5. Operacion.java

Esta clase representa una operación matemática.

package modelo;

public class Operacion {

    private int id;
    private double numero1;
    private double numero2;
    private String operacion;
    private double resultado;

    public Operacion() {
    }

    public Operacion(double numero1,
                     double numero2,
                     String operacion,
                     double resultado) {

        this.numero1 = numero1;
        this.numero2 = numero2;
        this.operacion = operacion;
        this.resultado = resultado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNumero1() {
        return numero1;
    }

    public void setNumero1(double numero1) {
        this.numero1 = numero1;
    }

    public double getNumero2() {
        return numero2;
    }

    public void setNumero2(double numero2) {
        this.numero2 = numero2;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }
}

6. OperacionDAO.java

Aquí guardaremos las operaciones en MySQL.

package dao;

import config.ConexionBD;
import modelo.Operacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OperacionDAO {

    public boolean guardar(Operacion operacion) {

        String sql = """
                INSERT INTO operaciones
                (numero1, numero2, operacion, resultado)
                VALUES (?, ?, ?, ?)
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

7. Calculadora.java

Esta será nuestra interfaz gráfica desarrollada con Swing.

package vista;

import dao.OperacionDAO;
import modelo.Operacion;

import javax.swing.*;
import java.awt.*;

public class Calculadora extends JFrame {

    private JTextField txtNumero1;
    private JTextField txtNumero2;
    private JTextField txtResultado;

    private JButton btnSumar;
    private JButton btnRestar;
    private JButton btnMultiplicar;
    private JButton btnDividir;
    private JButton btnLimpiar;

    private final OperacionDAO operacionDAO;

    public Calculadora() {

        operacionDAO = new OperacionDAO();

        crearVentana();
        crearComponentes();
    }

    private void crearVentana() {

        setTitle("Calculadora Java + MySQL");

        setSize(450, 400);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));
    }

    private void crearComponentes() {

        JPanel panelDatos = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        panelDatos.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 10, 20
                )
        );

        JLabel lblNumero1 = new JLabel("Número 1:");
        JLabel lblNumero2 = new JLabel("Número 2:");
        JLabel lblResultado = new JLabel("Resultado:");

        txtNumero1 = new JTextField();
        txtNumero2 = new JTextField();
        txtResultado = new JTextField();

        txtResultado.setEditable(false);

        panelDatos.add(lblNumero1);
        panelDatos.add(txtNumero1);

        panelDatos.add(lblNumero2);
        panelDatos.add(txtNumero2);

        panelDatos.add(lblResultado);
        panelDatos.add(txtResultado);

        add(panelDatos, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(
                new GridLayout(2, 3, 10, 10)
        );

        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 20, 20, 20
                )
        );

        btnSumar = new JButton("+");
        btnRestar = new JButton("-");
        btnMultiplicar = new JButton("×");
        btnDividir = new JButton("÷");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnSumar);
        panelBotones.add(btnRestar);
        panelBotones.add(btnMultiplicar);

        panelBotones.add(btnDividir);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.CENTER);

        btnSumar.addActionListener(e ->
                calcular("+")
        );

        btnRestar.addActionListener(e ->
                calcular("-")
        );

        btnMultiplicar.addActionListener(e ->
                calcular("*")
        );

        btnDividir.addActionListener(e ->
                calcular("/")
        );

        btnLimpiar.addActionListener(e ->
                limpiar()
        );
    }

    private void calcular(String operacion) {

        try {

            double numero1 = Double.parseDouble(
                    txtNumero1.getText()
            );

            double numero2 = Double.parseDouble(
                    txtNumero2.getText()
            );

            double resultado;

            switch (operacion) {

                case "+" -> resultado = numero1 + numero2;

                case "-" -> resultado = numero1 - numero2;

                case "*" -> resultado = numero1 * numero2;

                case "/" -> {

                    if (numero2 == 0) {

                        JOptionPane.showMessageDialog(
                                this,
                                "No se puede dividir por cero.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    resultado = numero1 / numero2;
                }

                default -> {
                    return;
                }
            }

            txtResultado.setText(
                    String.valueOf(resultado)
            );

            Operacion nuevaOperacion =
                    new Operacion(
                            numero1,
                            numero2,
                            operacion,
                            resultado
                    );

            boolean guardado =
                    operacionDAO.guardar(nuevaOperacion);

            if (guardado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Operación realizada y guardada en MySQL."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Operación realizada, pero no fue posible guardarla.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese números válidos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiar() {

        txtNumero1.setText("");
        txtNumero2.setText("");
        txtResultado.setText("");

        txtNumero1.requestFocus();
    }
}

8. Main.java

Finalmente, este es el punto de entrada de la aplicación:

import vista.Calculadora;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Calculadora calculadora =
                    new Calculadora();

            calculadora.setVisible(true);
        });
    }
}

9. Funcionamiento

Al ejecutar Main, tendremos una ventana similar a:

┌─────────────────────────────────────────┐
│       Calculadora Java + MySQL          │
│                                         │
│ Número 1:    [                 ]        │
│ Número 2:    [                 ]        │
│ Resultado:   [                 ]        │
│                                         │
│       [+]       [-]       [×]           │
│       [÷]     [Limpiar]                 │
│                                         │
└─────────────────────────────────────────┘

Ejemplo

Ingresamos:

Número 1: 25
Número 2: 5

Presionamos:

÷

Resultado:

5.0

La operación se registra automáticamente en MySQL.

Por ejemplo:

id | numero1 | numero2 | operacion | resultado
------------------------------------------------
1  | 25      | 5       | /         | 5

10. Consultar el historial en MySQL

Podemos consultar las operaciones almacenadas mediante:

USE calculadora_db;

SELECT *
FROM operaciones
ORDER BY id DESC;

11. Flujo del programa

              ┌───────────────┐
              │     Main      │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │ Calculadora   │
              │    Swing      │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │   Operación   │
              │    + - * /    │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │ OperacionDAO  │
              └───────┬───────┘
                      │
                     JDBC
                      │
                      ▼
              ┌───────────────┐
              │     MySQL     │
              │ calculadora_db│
              └───────────────┘

12. Tecnologías utilizadas

Tecnología

Versión / Herramienta

Java

21

IntelliJ IDEA

IDE

Swing

Interfaz gráfica

MySQL

8

JDBC

Conexión a MySQL

Maven

Gestión del proyecto

MySQL Connector/J

9.4.0

13. Arquitectura básica

El proyecto utiliza una separación sencilla por responsabilidades:

config
   │
   └── ConexionBD.java
       Conexión con MySQL

modelo
   │
   └── Operacion.java
       Representa una operación

dao
   │
   └── OperacionDAO.java
       Guarda información en MySQL

vista
   │
   └── Calculadora.java
       Interfaz gráfica Swing

Main.java
   │
   └── Inicia la aplicación

14. Cómo ejecutar el proyecto

Paso 1. Crear la base de datos

Ejecutar en MySQL:

CREATE DATABASE calculadora_db;

USE calculadora_db;

CREATE TABLE operaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero1 DOUBLE NOT NULL,
    numero2 DOUBLE NOT NULL,
    operacion VARCHAR(20) NOT NULL,
    resultado DOUBLE NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Paso 2. Configurar MySQL

En ConexionBD.java, verificar:

private static final String URL =
        "jdbc:mysql://localhost:3306/calculadora_db";

private static final String USUARIO = "root";

private static final String PASSWORD = "123456";

Cambiar el usuario y contraseña según la configuración local de MySQL.

Paso 3. Abrir el proyecto

Abrir CalculadoraJavaMySQL en IntelliJ IDEA.

Paso 4. Descargar las dependencias

Si utilizas Maven, IntelliJ IDEA descargará automáticamente:

mysql-connector-j

Paso 5. Ejecutar

Ejecutar:

Main.java

Se abrirá la interfaz gráfica de la calculadora.

15. Prueba de funcionamiento

Realizar una prueba con los siguientes valores:

Número 1: 25
Número 2: 5
Operación: /

El resultado debe ser:

5.0

Después consultar MySQL:

SELECT *
FROM operaciones
ORDER BY id DESC;

Deberá aparecer una fila similar a:

id | numero1 | numero2 | operacion | resultado | fecha
-------------------------------------------------------
1  | 25      | 5       | /         | 5         | ...

16. Versión básica 1.0

Esta implementación corresponde a una versión básica 1.0 de una calculadora de escritorio con Java Swing y MySQL.

Como siguiente evolución, se puede agregar una tabla JTable dentro de la interfaz para mostrar el historial de operaciones almacenadas en MySQL.

También se pueden incorporar:
📋 Historial de operaciones.
🗑️ Eliminar operaciones.
✏️ Actualizar operaciones.
🔎 Consultar operaciones.
🚪 Botón Salir.
📊 JTable para visualizar los registros.
🧹 Limpieza de campos.
🧾 Mejor presentación de los resultados .
:. . / .  
