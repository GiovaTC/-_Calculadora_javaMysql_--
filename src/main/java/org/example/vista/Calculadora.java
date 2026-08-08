package org.example.vista;

import org.example.dao.OperacionDAO;
import org.example.modelo.Operacion;

import javax.swing.*;
import java.awt.*;

public class Calculadora extends  JFrame {

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

    private void limpiar() {

        txtNumero1.setText("");
        txtNumero2.setText("");
        txtResultado.setText("");
        txtNumero1.requestFocus();
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

    private void crearVentana() {
        
        setTitle("Calculadora Java + MySQL");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }
    
    


}
