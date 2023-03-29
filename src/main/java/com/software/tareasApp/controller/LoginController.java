package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.PermisoUsuarioService;
import com.software.tareasApp.domain.service.UsuarioService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.exceptions.UAuthException;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.view.animations.FadeInLeftTransition;
import com.software.tareasApp.view.animations.FadeInLeftTransition1;
import com.software.tareasApp.view.animations.FadeInRightTransition;
import com.software.tareasApp.view.constantes.Constantes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginController implements Initializable {

	@FXML
	private ComboBox<String> cmbUsuario;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Text lblWelcome;
	@FXML
	private Text lblUserLogin;
	@FXML
	private Button btnLogin;
	@FXML
	private Label lblClose;
	@FXML
	private Label lblVersion;

	@Autowired
	public LoginController(UsuarioService usuarioService, PermisoUsuarioService permisoUsuarioService, ConstantesPaginas constantesPaginas){
		this.usuarioService = usuarioService;
		this.permisoUsuarioService = permisoUsuarioService;
		this.constantesPaginas = constantesPaginas;
	}

	private final UsuarioService usuarioService;
	private final PermisoUsuarioService permisoUsuarioService;
	private final ConstantesPaginas constantesPaginas;

	@Value("${application.version}")
	private String version;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		Platform.runLater(() -> {
			cmbListUsuarios();
			new FadeInRightTransition(lblUserLogin).play();
			new FadeInLeftTransition(lblWelcome).play();
			new FadeInLeftTransition1(lblVersion).play();
			new FadeInLeftTransition1(cmbUsuario).play();
			new FadeInLeftTransition1(txtPassword).play();
			new FadeInRightTransition(btnLogin).play();
			btnLoginOnAction();
			lblVersion.setText("Versión: " + version);
			lblClose.setOnMouseClicked((MouseEvent event) -> {
				if(TareaAppApplication.usuario!=null){
					logger.info(TareaAppApplication.usuario, "Cierra aplicacion");
				}else{
					logger.info(null, "Cierra aplicacion");
				}
				Platform.exit();
				System.exit(0);
			});
		});
		txtPassword.setOnAction(this::login);
	}

	private void cmbListUsuarios(){
		cmbUsuario.setItems(FXCollections.observableArrayList(Constantes.USUARIOS));
		cmbUsuario.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if(newValue.equals("Mathias") || newValue.equals("Agustina")){
				txtPassword.setVisible(false);
			}else{
				txtPassword.setText("");
				txtPassword.setVisible(true);
			}
		});
	}

	private void btnLoginOnAction(){
		btnLogin.setOnAction(this::login);
	}

	private void login(ActionEvent event) {
		getVer();
		if(UtilsGeneral.isNotNullEmptyUserInfo(cmbUsuario.getValue(),txtPassword.getText())){
			try{
				TareaAppApplication.usuario = usuarioService.validarUsuario(cmbUsuario.getValue(),txtPassword.getText());
				if(TareaAppApplication.usuario !=null){
					TareaAppApplication.permisosUsuario = permisoUsuarioService.findAllByUserType(TareaAppApplication.usuario.getTipoUsuario());
					logger.info(TareaAppApplication.usuario, "Abre sesion");
					try{
						Stage stage = (Stage) cmbUsuario.getScene().getWindow();
						UtilsGeneral.openNewWindow(stage, getClass(),MenuController.class,Constantes.PAGINA_FORM_MENU);
					}catch (IOException ex) {
						errorLog(ex);
					}
				}else{
					UtilsGeneral.error(Errores.LOGUEO_ERROR);
					logger.error(TareaAppApplication.usuario, Errores.LOGUEO_ERROR.getCodigo() + " " + Errores.LOGUEO_ERROR.getError());
				}
			}catch(UAuthException ex){
				warnLog(ex);
			}
		}
	}

	private void getVer() {
		if(cmbUsuario.getValue().equals("Mathias") || cmbUsuario.getValue().equals("Agustina")){
			txtPassword.setText("asdasd");
		}
	}

	private void errorLog(Exception ex){
    	if (TareaAppApplication.usuario != null) {
		  logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		}else{
			logger.error(null, ex.getMessage(), ex);
		}
		UtilsGeneral.errorEx(ex);
	}

	private void warnLog(Exception ex){
		if (TareaAppApplication.usuario != null) {
			logger.warn(TareaAppApplication.usuario, ex.getMessage(), ex);
		}else{
			logger.warn(null, ex.getMessage(), ex);
		}
		UtilsGeneral.errorEx(ex);
	}

}
