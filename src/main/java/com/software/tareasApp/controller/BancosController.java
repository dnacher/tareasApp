package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.BancoService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Banco;
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
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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
public class BancosController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private Label lblNombre;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<Banco> tableData;

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
	private Text textTitle;


	ObservableList<Banco> bancos;

	private final BancoService bancoService;
	private final ConstantesPaginas constantesPaginas;

	@Autowired
	public BancosController(BancoService bancoService, ConstantesPaginas constantesPaginas){
		this.bancoService = bancoService;
		this.constantesPaginas = constantesPaginas;
	}

	private Banco bancoSelected;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			textTitle.setText(constantesPaginas.BANCO);
			constantesPaginas.setLayouts(lblNombre, txtNombre);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuConfiguracion.Banco.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task longTask = UtilsGeneral.task();

		longTask.setOnSucceeded((EventHandler<WorkerStateEvent>) t -> {
			try {
				cargaTabla();
				mostrarTabla();
				btnOnAction();
				buttonDisable(true);
			} catch (Exception ex) {
				errorLog(ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void btnOnAction(){
		btnActualizarOnAction();
		btnEliminarOnAction();
		btnGuardarOnAction();
		btnBackOnAction();
		btnAgregarOnAction();
	}

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void updateButton(){
		if(!UtilsGeneral.isNullOrEmpty(bancoSelected.getNombre())){
			txtNombre.setText(bancoSelected.getNombre());
		}
		if(!UtilsGeneral.isNullOrEmpty(bancoSelected.getDescripcion())){
			txtDescripcion.setText(bancoSelected.getDescripcion());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, bancoSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				bancoService.deleteBanco(bancoSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.BANCO_ASOCIADO_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	private void btnAgregarOnAction(){
		clear();
		btnAgregar.setOnAction((event) -> {
			bancoSelected = null;
			agregarBanco();
			btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		});
	}

	private void btnBackOnAction(){
		btnBack.setOnAction((event) -> mostrarTabla());
	}

	private void agregarBanco() {
		mostrarFormulario();
		Platform.runLater(this::clear);
	}

	private void recargarTabla() {
		try {
			buttonDisable(true);
			tableData.getColumns().clear();
			cargaTabla();
			mostrarTabla();
			btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTabla() {
		bancos = FXCollections.observableArrayList(bancoService.getBancos());
		UtilCargaTablas.cargaBancoTabla(tableData, bancos);
		tableData.setOnMouseClicked(
			event -> {
				Banco b = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && b != null) {
					bancoSelected = b;
					updateButton();
				}else {
					if (b != null) {
						if (bancoSelected != null) {
							if (bancoSelected != b) {
								bancoSelected = b;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								bancoSelected = null;
								buttonDisable(true);
							}
						} else {
							bancoSelected = b;
							buttonDisable(false);
						}
					}
				}
			});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuConfiguracion.Banco.getPagina(), btnActualizar, btnEliminar, null);
	}

	private void mostrarTabla() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
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

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(bancoSelected ==null){
				bancoSelected = new Banco();
			}
			boolean updateProcess = bancoSelected.getId()!=null;
			bancoSelected.setNombre(txtNombre.getText());
			bancoSelected.setDescripcion(txtDescripcion.getText());
			if(updateProcess){
				bancoService.updateBanco(bancoSelected);
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
				bancoSelected = null;
			}else{
				bancoService.saveBanco(bancoSelected);
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
				bancoSelected = null;
			}
			mostrarTabla();
			recargarTabla();
		}catch (TareasAppException ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().isEmpty()) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_BANCO);
		} else if((bancoSelected==null || bancoSelected.getId()==null)
				&& bancos.stream().anyMatch(banco -> banco.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.NOMBRE_BANCO_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
