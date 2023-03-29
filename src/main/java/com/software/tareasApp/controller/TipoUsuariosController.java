package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.TipoUsuarioService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuSeguridad;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.TipoUsuario;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class TipoUsuariosController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<TipoUsuario> tableData;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtDescripcion;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnGuardar;

	@FXML
	private Button btnActualizar;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnBack;

	@FXML
	private Label lblNombre;

	@FXML
	private Label lblDescripcion;

	ObservableList<TipoUsuario> tipoUsuarios;

	@Autowired
	public TipoUsuariosController(TipoUsuarioService tipoUsuarioService, ConstantesPaginas constantesPaginas){
		this.constantesPaginas= constantesPaginas;
		this.tipoUsuarioService= tipoUsuarioService;
	}

	private final TipoUsuarioService tipoUsuarioService;
	private final ConstantesPaginas constantesPaginas;

	private TipoUsuario tipoUsuarioSelected;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			constantesPaginas.set2Valores(lblNombre, txtNombre, lblDescripcion, txtDescripcion);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuSeguridad.TipoUsuarios.getPagina());
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
	}

	private void btnAgregarOnAction() {
		btnAgregar.setOnAction((event) -> {
			clear();
			tipoUsuarioSelected = null;
			nuevoTipoUsuario();
		});
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

	private void updateButton(){
		if(tipoUsuarioSelected.getNombre()!=null){
			txtNombre.setText(tipoUsuarioSelected.getNombre());
		}
		if(tipoUsuarioSelected.getDescripcion()!=null){
			txtDescripcion.setText(tipoUsuarioSelected.getDescripcion());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, tipoUsuarioSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				tipoUsuarioService.deleteTipoUsuario(tipoUsuarioSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.COMPANIA_ASOCIADA_OTRO_REGISTRO);
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
				cargaTabla();
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
			cargaTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTabla() {
		tipoUsuarios = FXCollections.observableArrayList(tipoUsuarioService.getTipoUsuarios());
		UtilCargaTablas.cargaTablaTipoUsuario(tableData, tipoUsuarios);
		tableData.setOnMouseClicked(
			event -> {
		  		TipoUsuario ut = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && tableData.getSelectionModel().getSelectedItem() != null) {
					updateButton();
				}else {
					if (ut != null) {
						if (tipoUsuarioSelected != null) {
							if (tipoUsuarioSelected != ut) {
								tipoUsuarioSelected = ut;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								tipoUsuarioSelected = null;
								buttonDisable(true);
							}
						} else {
							tipoUsuarioSelected = ut;
							buttonDisable(false);
						}
					}
				}
        });
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuSeguridad.TipoUsuarios.getPagina(), btnActualizar, btnEliminar, null);
	}


	private void nuevoTipoUsuario() {
		paneTabel.setOpacity(0);
		new FadeInUpTransition(paneCrud).play();
		Platform.runLater(this::clear);
	}

	private void mostrarTabla() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		tableData.getSelectionModel().clearSelection();
		tipoUsuarioSelected = null;
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
		txtDescripcion.setText(ConstantesEtiquetas.VACIO);
	}

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(tipoUsuarioSelected ==null){
				tipoUsuarioSelected = new TipoUsuario();
			}
			boolean updateProcess = tipoUsuarioSelected.getId()!=null;
			tipoUsuarioSelected.setNombre(txtNombre.getText());
			tipoUsuarioSelected.setDescripcion(txtDescripcion.getText());
			tipoUsuarioService.saveTipoUsuario(tipoUsuarioSelected);
			if(updateProcess){
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
			tipoUsuarioSelected = null;
		}catch (TareasAppException ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().isEmpty()) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_TIPO_USUARIO);
		} else if((tipoUsuarioSelected==null || tipoUsuarioSelected.getId()!=null) &&
				tipoUsuarios.stream().anyMatch(tipoUsuario -> tipoUsuario.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.NOMBRE_TIPO_USUARIO_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
