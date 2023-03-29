package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.TipoUsuarioService;
import com.software.tareasApp.domain.service.UsuarioService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuSeguridad;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.TipoUsuario;
import com.software.tareasApp.persistence.model.Usuario;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class UsuariosController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<Usuario> tableData;

	@FXML
	private TextField txtNombre;

	@FXML
	private PasswordField txtPass;

	@FXML
	private PasswordField txtPass2;

	@FXML
	private ComboBox<TipoUsuario> cmbTipoUsuario;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnGuardar;

	@FXML
	private Button btnActualizar;

	@FXML
	private Button btnResetearPass;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnBack;

	@FXML
	private Label lblPass;

	@FXML
	private Label lblPass2;

	ObservableList<Usuario> usuarios;

	@Autowired
	public UsuariosController(TipoUsuarioService tipoUsuarioService, UsuarioService usuarioService, ConstantesPaginas constantesPaginas){
		this.tipoUsuarioService = tipoUsuarioService;
		this.usuarioService = usuarioService;
		this.constantesPaginas = constantesPaginas;
	}

	private final ConstantesPaginas constantesPaginas;
	private final UsuarioService usuarioService;
	private final TipoUsuarioService tipoUsuarioService;

	private Usuario usuarioSelcted;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(btnAgregar, btnActualizar, btnEliminar,btnResetearPass, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuSeguridad.Usuarios.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void btnOnAction(){
		btnActualizarOnAction();
		btnEliminarOnAction();
		btnGuardarOnAction();
		btnBackOnAction();
		btnAgregarOnAction();
		btnResetearPassOnAction();
	}

	private void btnResetearPassOnAction(){
		btnResetearPass.setOnAction((event) -> resetearPass());
	}

	private void btnAgregarOnAction() {
		btnAgregar.setOnAction((event) -> nuevoUsuario());
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void btnBackOnAction() {
		btnBack.setOnAction((event) -> mostrarTabla());
	}

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void cargaFormulario(){
		if(usuarioSelcted.getNombre()!=null){
			txtNombre.setText(usuarioSelcted.getNombre());
		}
		if(usuarioSelcted.getTipoUsuario()!=null){
			cmbTipoUsuario.setValue(usuarioSelcted.getTipoUsuario());
		}
	}

	private void updateButton(){
		cargaFormulario();
		passVisible(false);
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void resetearPass(){
		cargaFormulario();
		passVisible(true);
		txtNombre.setDisable(true);
		cmbTipoUsuario.setDisable(true);
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void passVisible(boolean visible){
		lblPass.setVisible(visible);
		txtPass.setVisible(visible);
		lblPass2.setVisible(visible);
		txtPass2.setVisible(visible);
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, usuarioSelcted.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				usuarioService.deleteUsuario(usuarioSelcted);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.USUARIO_ASOCIADO_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				btnOnAction();
				buttonDisable(true);
				cargaTablaUsuario();
				cargarComboUserType();
				mostrarTabla();
			} catch (Exception ex) {
				errorLog(ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void recargarTabla() {
		try {
			buttonDisable(true);
			tableData.getColumns().clear();
			cargaTablaUsuario();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTablaUsuario() {
		usuarios = FXCollections.observableArrayList(usuarioService.getUsuarios());
		UtilCargaTablas.cargaTablaUsuario(tableData, usuarios);
		tableData.setOnMouseClicked(
			event -> {
				Usuario u = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && u != null) {
					usuarioSelcted = u;
					updateButton();
				}else {
					if (u != null) {
						if (usuarioSelcted != null) {
							if (usuarioSelcted != u) {
								usuarioSelcted = u;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								usuarioSelcted = null;
								buttonDisable(true);
							}
						} else {
							usuarioSelcted = u;
							buttonDisable(false);
						}
					}
				}
			});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuSeguridad.Usuarios.getPagina(), btnActualizar, btnEliminar, btnResetearPass);
	}

	private void cargarComboUserType() {
		ObservableList<TipoUsuario> options
				= FXCollections.observableArrayList(tipoUsuarioService.getTipoUsuarios());
		cmbTipoUsuario.setItems(options);
	}

	private void nuevoUsuario() {
		clear();
		passVisible(true);
		usuarioSelcted = null;
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		mostrarFormulario();
	}

	private void mostrarTabla() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		tableData.getSelectionModel().clearSelection();
		usuarioSelcted = null;
		paneCrud.setOpacity(0);
		new FadeInUpTransition(paneTabel).play();
	}

	private void mostrarFormulario(){
		buttonDisable(true);
		paneTabel.setOpacity(0);
		new FadeInUpTransition(paneCrud).play();
	}

	private void clear() {
		txtNombre.setText(ConstantesEtiquetas.VACIO);
		txtPass.setText(ConstantesEtiquetas.VACIO);
		txtPass2.setText(ConstantesEtiquetas.VACIO);
		cmbTipoUsuario.setValue(null);
	}

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(usuarioSelcted == null){
				usuarioSelcted = new Usuario();
			}
			boolean updateProcess = usuarioSelcted.getId()!=null;
			usuarioSelcted.setNombre(txtNombre.getText());
			usuarioSelcted.setTipoUsuario(cmbTipoUsuario.getValue());
			if(updateProcess){
				if(txtNombre.isDisable()){
					usuarioService.updateUsuario(usuarioSelcted);
					usuarioSelcted.setPassword(txtPass.getText());
					usuarioService.updateUsuario(usuarioSelcted);
					UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
				}else{
					usuarioService.updateSinPass(usuarioSelcted);
					UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
				}
			}else{
				usuarioSelcted.setPassword(txtPass.getText());
				usuarioService.saveUsuario(usuarioSelcted);
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
			usuarioSelcted = null;
		}catch (TareasAppException ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().isEmpty()) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_USUARIO);
		} else if((usuarioSelcted==null || usuarioSelcted.getId()==null) &&
				usuarios.stream().anyMatch(usuario -> usuario.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.USUARIO_EXISTE);
		}else if(txtPass.isVisible() && (txtPass.getText().isEmpty() || txtPass2.getText().isEmpty())){
			txtPass.requestFocus();
			return UtilsGeneral.error(Errores.PASS_USUARIO);
		}else if(txtPass.isVisible() && txtPass.getText().length()<4){
			txtPass.requestFocus();
			return UtilsGeneral.error(Errores.PASS_USUARIO_MENOR_4);
		}else if(txtPass.isVisible() && !txtPass.getText().equals(txtPass2.getText())){
			txtPass.requestFocus();
			return UtilsGeneral.error(Errores.PASS_NO_COINCIDE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
