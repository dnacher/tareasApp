package com.software.tareasApp.view.constantes;

import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

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

    public void setLayouts(Label label, TextField textField){
        String[] arrStr = this.element.split(",");
        label.setLayoutX(Double.parseDouble(arrStr[0]));
        label.setLayoutY(Double.parseDouble(arrStr[1]));
        textField.setLayoutX(Double.parseDouble(arrStr[2]));
        textField.setLayoutY(Double.parseDouble(arrStr[3]));
    }

    private void setTextField(String valores, Label label, TextField textField){
        String[] arrStr = valores.split(",");
        label.setLayoutX(Double.parseDouble(arrStr[0]));
        label.setLayoutY(Double.parseDouble(arrStr[1]));
        textField.setLayoutX(Double.parseDouble(arrStr[2]));
        textField.setLayoutY(Double.parseDouble(arrStr[3]));
    }

    private void setTextText(String valores, Label label, Label labelValor){
        String[] arrStr = valores.split(",");
        label.setLayoutX(Double.parseDouble(arrStr[0]));
        label.setLayoutY(Double.parseDouble(arrStr[1]));
        labelValor.setLayoutX(Double.parseDouble(arrStr[2]));
        labelValor.setLayoutY(Double.parseDouble(arrStr[3]));
    }

    private void setDatePicker(String valores, Label label, DatePicker cmbDate){
        String[] arrStr = valores.split(",");
        label.setLayoutX(Double.parseDouble(arrStr[0]));
        label.setLayoutY(Double.parseDouble(arrStr[1]));
        cmbDate.setLayoutX(Double.parseDouble(arrStr[2]));
        cmbDate.setLayoutY(Double.parseDouble(arrStr[3]));
    }

    private void setCmb(String valores, Label label, ComboBox cmb){
        String[] arrStr = valores.split(",");
        label.setLayoutX(Double.parseDouble(arrStr[0]));
        label.setLayoutY(Double.parseDouble(arrStr[1]));
        cmb.setLayoutX(Double.parseDouble(arrStr[2]));
        cmb.setLayoutY(Double.parseDouble(arrStr[3]));
    }

    private void setChk(String valores, Label label, CheckBox chk){
        String[] arrStr = valores.split(",");
        label.setLayoutX(Double.parseDouble(arrStr[0]));
        label.setLayoutY(Double.parseDouble(arrStr[1]));
        chk.setLayoutX(Double.parseDouble(arrStr[2]));
        chk.setLayoutY(Double.parseDouble(arrStr[3]));
    }

    private void setChkCmb(String valores, CheckBox chk, ComboBox cmb){
        String[] arrStr = valores.split(",");
        chk.setLayoutX(Double.parseDouble(arrStr[0]));
        chk.setLayoutY(Double.parseDouble(arrStr[1]));
        cmb.setLayoutX(Double.parseDouble(arrStr[2]));
        cmb.setLayoutY(Double.parseDouble(arrStr[3]));
    }

    private void setBtnConIcono(String valores, Label lbl, Button btn, Label lblValor){
        String[] arrStr = valores.split(",");
        lbl.setLayoutX(Double.parseDouble(arrStr[0]));
        lbl.setLayoutY(Double.parseDouble(arrStr[1]));
        btn.setLayoutX(Double.parseDouble(arrStr[2]));
        btn.setLayoutY(Double.parseDouble(arrStr[3]));
        lblValor.setLayoutX(Double.parseDouble(arrStr[4]));
        lblValor.setLayoutY(Double.parseDouble(arrStr[5]));
    }

    private void setTextArea(String valores, Label lbl, TextArea txt, Label lblValor){
        String[] arrStr = valores.split(",");
        lbl.setLayoutX(Double.parseDouble(arrStr[0]));
        lbl.setLayoutY(Double.parseDouble(arrStr[1]));
        txt.setLayoutX(Double.parseDouble(arrStr[2]));
        txt.setLayoutY(Double.parseDouble(arrStr[3]));
        lblValor.setLayoutX(Double.parseDouble(arrStr[4]));
        lblValor.setLayoutY(Double.parseDouble(arrStr[5]));
    }

    public void setPoliza(Label lblCompania, Button btnCompania, Label lblCompaniaValor,
                          Label lblCliente, Button btnCliente, Label lblClienteValor,
                          Label lblNumeroPoliza, TextField txtNumeroPoliza,
                          Label lblFechaComienzo, DatePicker cmbFechaComienzo,
                          Label lblFechaVencimiento, DatePicker cmbFechaVencimiento,
                          Label lblProducto, Button btnProducto, Label lblProductoValor,
                          Label lblTipoProducto, Label lblTipoProductoValor,
                          Label lblPremio, TextField txtPremio,
                          Label lblPrima, TextField txtPrima,
                          Label lblMoneda, ComboBox cmbMoneda,
                          Label lblComision, Label lblComisionValor,
                          Label lblFormaPago, ComboBox cmbFormaPago,
                          Label lblCuotas, TextField txtCuotas,
                          Label lblComienzoCuota, DatePicker cmbComienzoCuota,
                          Label lblFinCuota, DatePicker cmbFinCuota,
                          Label lblImporteCuota, Label lblImporteCuotaValor,
                          Label lblCerradoPor, Label lblCerradoPorValor,
                          Label lblEsApp, CheckBox chkEsApp,
                          Label lblEstado, ComboBox cmbEstado,
                          CheckBox chkVendedor, ComboBox cmbVendedor,
                          Label lblObservaciones, TextArea txtObservaciones, Label lblInfoObservaciones){
        setBtnConIcono(poliza, lblCompania, btnCompania, lblCompaniaValor);
        setBtnConIcono(poliza1, lblCliente, btnCliente, lblClienteValor);
        setTextField(poliza2,lblNumeroPoliza, txtNumeroPoliza);
        setDatePicker(poliza3, lblFechaComienzo, cmbFechaComienzo);
        setDatePicker(poliza4, lblFechaVencimiento, cmbFechaVencimiento);
        setBtnConIcono(poliza5, lblProducto, btnProducto, lblProductoValor);
        setTextText(poliza6,lblTipoProducto, lblTipoProductoValor);
        setTextField(poliza7, lblPremio, txtPremio);
        setTextField(poliza8, lblPrima, txtPrima);
        setCmb(poliza9, lblMoneda, cmbMoneda);
        setTextText(poliza20,lblComision,lblComisionValor);
        setCmb(poliza21,lblFormaPago, cmbFormaPago);
        setTextField(poliza22, lblCuotas, txtCuotas);
        setDatePicker(poliza23, lblComienzoCuota, cmbComienzoCuota);
        setDatePicker(poliza24, lblFinCuota, cmbFinCuota);
        setTextText(poliza25, lblImporteCuota,lblImporteCuotaValor);
        setTextText(poliza26,lblCerradoPor,lblCerradoPorValor);
        setChk(poliza27,lblEsApp,chkEsApp);
        setCmb(poliza28, lblEstado,cmbEstado);
        setChkCmb(poliza29,chkVendedor,cmbVendedor);
        setTextArea(poliza30,lblObservaciones, txtObservaciones, lblInfoObservaciones);
    }

    public void set2Valores(Label lblNombre, TextField txtNombre, Label lblDescripcion, TextField txtDescripcion){
        setTextField(estado0, lblNombre, txtNombre);
        setTextField(estado1, lblDescripcion, txtDescripcion);
    }
}
