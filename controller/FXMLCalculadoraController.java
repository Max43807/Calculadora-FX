/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadorafx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

/**
 *
 * @author ander
 */
public class FXMLCalculadoraController implements Initializable {

    @FXML
    private TextField txfPantalla;
    @FXML
    private Button btnIgual;
    @FXML
    private Button btnComa;
    @FXML
    private Button btnC;
    @FXML
    private Button btnCE;
    @FXML
    private Button btnMenos;
    @FXML
    private Button btnMas;
    @FXML
    private Button btnPor;
    @FXML
    private Button btnDiv;
    @FXML
    private Button btnPorC;
    @FXML
    private Button btn0;
    @FXML
    private Button btn1;

    

    //Tipo enumerado que define las posibles entrdas en la calculadora
    enum Entrada {
        NINGUNA,
        DIGITO,
        OPERADOR,
        CE
    }

    //Crear la variable del tipo Entrada:
    private Entrada ultimaEntrada;

    //Varible controla si ya se ingresó coma decimal
    private Boolean coma;

    //Variable que almacena el operador del botón
    private char operador;

    //Variables que memorizarán los operandos ingresados
    private byte numOperandos;
    private double ope1, ope2;

    //Error de división por cero:
    private String errorDiv;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarValores();
        errorDiv = "No se puede dividir entre cero";
    }

    private void inicializarValores() {

        // Inicializar los valores:
        ultimaEntrada = Entrada.NINGUNA;
        coma = false;
        operador = '\0';
        numOperandos = 0;
        ope1 = 0;
        ope2 = 0;
    }

    @FXML
    private void operadores_Action(ActionEvent event) {
        Button boton = (Button) event.getSource();

        if (ultimaEntrada == Entrada.DIGITO) {
            numOperandos += 1;
        }

        if (numOperandos == 1) {
            ope1 = Double.parseDouble(txfPantalla.getText().replace(',', '.'));
        } else if (numOperandos == 2) {
            ope2 = Double.parseDouble(txfPantalla.getText().replace(',', '.'));

            //Elegr la operación según el texto del botón:
            switch (operador) {
                case '+':
                    ope1 += ope2;
                    break;
                case '-':
                    ope1 -= ope2;
                    break;
                case 'X':
                case 'x':
                    ope1 *= ope2;
                    break;
                case '/':
                    if (ope2 == 0) {
                        txfPantalla.setText(errorDiv);
                        txfPantalla.setFont(Font.font("Consolas", 14));
                        break;
                    } else {
                        ope1 /= ope2;
                        break;
                    }
                case '=':
                    ope1 = ope2;
                    break;
            }

            if (ope2 != 0) {
                //Visualizar resultado en pantalla:
                txfPantalla.setText(String.valueOf(ope1).replace('.', ','));
            }
            numOperandos = 1;
        }
        operador = boton.getText().charAt(0);
        ultimaEntrada = Entrada.OPERADOR;
    }

    @FXML
    private void digitos_Action(ActionEvent event) {
        //Obtener el botón que se pulsó:
        Button boton = (Button) event.getSource();
        txfPantalla.setFont(Font.font("Consolas", 29)); //Volver a tamaño original de terxto

        if (ultimaEntrada != Entrada.DIGITO) {

            //Primero, limpiar la pantalla:
            txfPantalla.setText("");

            //Después, modificar el enumerable
            //a la espera de nuevos dígitos:
            ultimaEntrada = Entrada.DIGITO;
            coma = false;  //Permitir coma en segundo operando...
        }

        //Añadir el texto del botón pulsado a la pantalla
        if (txfPantalla.getText().equals("0")) {
            txfPantalla.setText(boton.getText());
        } else {
            txfPantalla.setText(txfPantalla.getText() + boton.getText());
        }
    }

    @FXML
    private void decimal_Action(ActionEvent event) {
        //asignar cero adelante si coma es primer operando
        if (txfPantalla.getText() == "0,") {
            ultimaEntrada = Entrada.DIGITO;
            coma = true;
        } else if (ultimaEntrada != Entrada.DIGITO) {
            txfPantalla.setText("0,");
            ultimaEntrada = Entrada.DIGITO;
        } else if (coma == false) {
            //Solo puede haber UNA coma:
            txfPantalla.setText(txfPantalla.getText() + ",");
            coma = true;
        }
    }

    @FXML
    private void clear_Action(ActionEvent event) {

        txfPantalla.setText("0,");
        inicializarValores();

    }

    @FXML
    private void clearEntry_Action(ActionEvent event) {

        txfPantalla.setText("0,");
        ultimaEntrada = Entrada.CE;
        coma = false;
    }

    @FXML
    private void porcentaje_Action(ActionEvent event) {
        double resultado;

        if (ultimaEntrada == Entrada.DIGITO) {
            //Obtener el %
            resultado = ope1 * Double.parseDouble(txfPantalla.getText().replace(',', '.')) / 100;

            //Visualizar el resultado
            txfPantalla.setText(String.valueOf(resultado).replace('.', ','));
        }
    }

    @FXML
    private void detectarTeclas_OnKeyTyped(KeyEvent event) {
        
       KeyCode codTecla = event.getCode();
       
       if(codTecla == event.getCode().DIGIT0 || codTecla == event.getCode().NUMPAD0) {
           btn0.fire();
       } else if (codTecla == event.getCode().DIGIT1 || codTecla == event.getCode().NUMPAD1) {
           btn1.fire();
       }
       
    }
}
