package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainController implements Initializable {
    @FXML
    private Button maximize;
    @FXML
    private Button minimize;
    @FXML
    private Button resize;
    
    Stage stage;
    Rectangle2D rec2;
    Double w,h;
    @FXML
    private ListView<String> listMenu;

    private final LogManagerClass logger = new LogManagerClass(getClass());

    @Override
    public void initialize(URL url, ResourceBundle rb) {}


    @FXML
    private void maximize(ActionEvent event) {
        stage = (Stage) maximize.getScene().getWindow();
        if (stage.isMaximized()) {
            if (w == rec2.getWidth() && h == rec2.getHeight()) {
                setStage(stage, 600,800);
            }
            maximize.getStyleClass().remove(ConstantesEtiquetas.DECORATION_BUTTON_RESTORE);
            resize.setVisible(true);
            stage.setMaximized(false);
        }else{
            stage.setMaximized(true);
            stage.setHeight(rec2.getHeight());
            maximize.getStyleClass().add(ConstantesEtiquetas.DECORATION_BUTTON_RESTORE);
            resize.setVisible(false);
        }
    }

    private void setStage(Stage stage, double height, double width){
        stage.setHeight(height);
        stage.setWidth(width);
    }

    @FXML
    private void minimize(ActionEvent event) {
        stage = (Stage) minimize.getScene().getWindow();
        if (stage.isMaximized()) {
            w = rec2.getWidth();
            h = rec2.getHeight();
            stage.setMaximized(false);
            setStage(stage, h,w);
            Platform.runLater(() -> stage.setIconified(true));
        }else{
            stage.setIconified(true);
        }        
    }

    @FXML
    private void close(ActionEvent event) {
        logger.info(TareaAppApplication.usuario, "Cierra aplicacion");
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void logout(ActionEvent event) {
        logger.info(TareaAppApplication.usuario, "Cierra sesion");
        TareaAppApplication.usuario =null;
    }

}
