package com.software.tareasApp.view.constantes;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Daniel Nacher
 * 2022-04-21
 */
@PropertySource("classpath:fragments.properties")
@Configuration
public class ConstantesPaginas {

    @Value("${fragments.fila1..data.version}")
    public String ver;

    @Value("${fragments.fila1..data.version2}")
    public String ver2;

    @Value("${fragments.banco}")
    public String BANCO;

    @Value("${fragments.poliza}")
    public String POLIZA;

    @Value("${fragments.banco.primeraFila.primerElemento.element}")
    private String element;

    @Value("${fragments.poliza.fila1.elemento0}")
    private String poliza;

    @Value("${fragments.poliza.fila1.elemento1}")
    private String poliza1;

    @Value("${fragments.poliza.fila1.elemento2}")
    private String poliza2;

    @Value("${fragments.poliza.fila1.elemento3}")
    private String poliza3;

    @Value("${fragments.poliza.fila1.elemento4}")
    private String poliza4;

    @Value("${fragments.poliza.fila1.elemento5}")
    private String poliza5;

    @Value("${fragments.poliza.fila1.elemento6}")
    private String poliza6;

    @Value("${fragments.poliza.fila1.elemento7}")
    private String poliza7;

    @Value("${fragments.poliza.fila1.elemento8}")
    private String poliza8;

    @Value("${fragments.poliza.fila1.elemento9}")
    private String poliza9;

    @Value("${fragments.poliza.fila2.elemento0}")
    private String poliza20;

    @Value("${fragments.poliza.fila2.elemento1}")
    private String poliza21;

    @Value("${fragments.poliza.fila2.elemento2}")
    private String poliza22;

    @Value("${fragments.poliza.fila2.elemento3}")
    private String poliza23;

    @Value("${fragments.poliza.fila2.elemento4}")
    private String poliza24;

    @Value("${fragments.poliza.fila2.elemento5}")
    private String poliza25;

    @Value("${fragments.poliza.fila2.elemento6}")
    private String poliza26;

    @Value("${fragments.poliza.fila2.elemento7}")
    private String poliza27;

    @Value("${fragments.poliza.fila2.elemento8}")
    private String poliza28;

    @Value("${fragments.poliza.fila2.elemento9}")
    private String poliza29;

    @Value("${fragments.poliza.fila3.elemento0}")
    private String poliza30;

    @Value("${fragments.fila1.elemento0}")
    private String estado0;

    @Value("${fragments.fila1.elemento1}")
    private String estado1;

    @Value("${fragments.buttons}")
    private String buttons;

    public void setButtons(Button button1, Button button2, Button button3, Button button4, Button btnGuardar){
        String[] arrStr = this.buttons.split(",");
        if(button1!=null){
            button1.setLayoutX(Double.parseDouble(arrStr[0]));
            button1.setLayoutY(46);
        }
        if(button2!=null){
            button2.setLayoutX(Double.parseDouble(arrStr[1]));
            button2.setLayoutY(46);
        }
        if(button3!=null){
            button3.setLayoutX(Double.parseDouble(arrStr[2]));
            button3.setLayoutY(46);
        }
        if(button4!=null){
            button4.setLayoutX(Double.parseDouble(arrStr[3]));
            button4.setLayoutY(46);
        }
        if(btnGuardar!=null){
            btnGuardar.setLayoutX(80);
            btnGuardar.setLayoutY(18);
        }
    }

    private void setTextField(String valores, Label label, TextField textField){
        String[] arrStr = valores.split(",");
        label.setLayoutX(Double.parseDouble(arrStr[0]));
        label.setLayoutY(Double.parseDouble(arrStr[1]));
        textField.setLayoutX(Double.parseDouble(arrStr[2]));
        textField.setLayoutY(Double.parseDouble(arrStr[3]));
    }

    public void set2Valores(Label lblNombre, TextField txtNombre, Label lblDescripcion, TextField txtDescripcion){
        setTextField(estado0, lblNombre, txtNombre);
        setTextField(estado1, lblDescripcion, txtDescripcion);
    }
}
