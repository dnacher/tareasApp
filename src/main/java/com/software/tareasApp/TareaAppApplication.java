package com.software.tareasApp;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.controller.LoginController;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.PermisoUsuario;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.persistence.model.Usuario;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.view.constantes.Constantes;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.util.List;

@SpringBootApplication
public class TareaAppApplication extends Application {

	public static Usuario usuario;
	public static List<PermisoUsuario> permisosUsuario;
	public static List<Cliente> clientes;
	public static List<Poliza> polizas;
	public static ConfigurableApplicationContext applicationContext;
	private LogManagerClass log = new LogManagerClass(getClass());

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		applicationContext = SpringApplication.run(TareaAppApplication.class);
		FXMLLoader loader  = new FXMLLoader(getClass().getResource(Constantes.PAGINA_INI));
		loader.setControllerFactory(applicationContext::getBean);
		Scene scene = new Scene(loader.load(), 493, 327, false, SceneAntialiasing.BALANCED);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream(Constantes.LOGO)));
		stage.show();
		Timeline timeline = new Timeline();
    timeline
        .getKeyFrames()
        .add(
            new KeyFrame(
                Duration.seconds(3),
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    try {
                      UtilsGeneral.openNewWindow(
                          stage, getClass(), LoginController.class, Constantes.PAGINA_LOGIN);
                    } catch (Exception ex) {
                      if (TareaAppApplication.usuario != null) {
                        log.error(TareaAppApplication.usuario, ex.getMessage(), ex);
                      } else {
                        log.error(TareaAppApplication.usuario, ex.getMessage(), ex);
                      }
                    }
                  }
                }));
		timeline.play();
	}
}
