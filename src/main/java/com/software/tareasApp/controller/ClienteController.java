package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.ClienteService;
import com.software.tareasApp.domain.service.PolizaService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.utils.ExportarExcel;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.Constantes;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ClienteController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private TableView<Cliente> tableCliente;

	@FXML
	private TextField txtClienteBusqueda;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtApellido;

	@FXML
	private TextField txtDireccion;

	@FXML
	private TextField txtCiudad;

	@FXML
	private TextField txtDepartamento;

	@FXML
	private TextField txtRut;

	@FXML
	private TextArea txtObservaciones;

	@FXML
	private DatePicker cmbFechaNacimiento;

	@FXML
	private TextField txtTelefono;

	@FXML
	private TextField txtCelular;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtCedulaIdentidad;

	@FXML
	private TextField txtLibretaPropiedad;

	@FXML
	private Button btnRecomendadoPor;

	@FXML
	private DatePicker cmbFechaInicio;

	@FXML
	private ProgressBar bar;

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
	private Label lblInfoObservaciones;

	@FXML
	private Label lblRecomendadoPorValor;

	@FXML
	private Label lblInfo;

	@FXML
	private CheckBox chkActivo;

	ObservableList<Cliente> clientes;
	private Cliente clienteSelected;
	private Cliente recomendadoPorSelected;
	private boolean recomendadoPorSeleccionando = false;
	private boolean recarga = false;

	private final ClienteService clienteService;
	private final PolizaService polizaService;
	private final ConstantesPaginas constantesPaginas;
	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Autowired
	public ClienteController(ClienteService clienteService, ConstantesPaginas constantesPaginas, PolizaService polizaService){
		this.constantesPaginas= constantesPaginas;
		this.clienteService = clienteService;
		this.polizaService = polizaService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuPrincipal.Clientes.getPagina());
			txtObservaciones.setWrapText(true);
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				textArea();
				cargaTabla();
				mostrarTabla();
				btnOnAction();
				buttonDisable(true);
				chkActivo.setSelected(true);
			} catch (Exception ex) {
				errorLog(ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void textArea(){
		UtilsGeneral.textArea(lblInfoObservaciones,txtObservaciones);
	}

	public void btnOnAction(){
		btnActualizarOnAction();
		btnEliminarOnAction();
		btnGuardarOnAction();
		btnBackOnAction();
		btnAgregarOnAction();
		btnRecomendadoPorOnAction();
	}

	private void btnRecomendadoPorOnAction(){
		btnRecomendadoPor.setOnAction((event) -> {
			mostrarTabla();
			recomendadoPorSeleccionando = true;
		});
	}

	private void btnAgregarOnAction() {
		btnAgregar.setOnAction((event) -> {
			clear();
			clienteSelected = null;
			nuevoCliente();
		});
	}

	private void btnBackOnAction() {
		btnBack.setOnAction((event) -> mostrarTabla());
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void updateButton(){
		recomendadoPorSelected = null;
		lblRecomendadoPorValor.setText(ConstantesEtiquetas.VACIO);
		if(clienteSelected.getNombre()!=null){
			txtNombre.setText(clienteSelected.getNombre());
		}
		if(clienteSelected.getApellido()!=null){
			txtApellido.setText(clienteSelected.getApellido());
		}
		if(clienteSelected.getDireccion()!=null){
			txtDireccion.setText(clienteSelected.getDireccion());
		}
		if(clienteSelected.getCiudad()!=null){
			txtCiudad.setText(clienteSelected.getCiudad());
		}
		if(clienteSelected.getDepartamento()!=null){
			txtDepartamento.setText(clienteSelected.getDepartamento());
		}
		if(clienteSelected.getFechaNacimiento()!=null){
			cmbFechaNacimiento.setValue(UtilsGeneral.getLocalDateFromDate(clienteSelected.getFechaNacimiento()));
		}
		if(clienteSelected.getTelefono()!=null){
			txtTelefono.setText(clienteSelected.getTelefono());
		}
		if(clienteSelected.getCelular()!=null){
			txtCelular.setText(clienteSelected.getCelular());
		}
		if(clienteSelected.getEmail()!=null){
			txtEmail.setText(clienteSelected.getEmail());
		}
		if(clienteSelected.getCedulaIdentidad()!=null){
			txtCedulaIdentidad.setText(clienteSelected.getCedulaIdentidad());
		}
		if(clienteSelected.getLibretaPropiedad()!=null){
			txtLibretaPropiedad.setText(clienteSelected.getLibretaPropiedad());
		}
		if(clienteSelected.getRecomendadoPor()!=null){
			lblRecomendadoPorValor.setText(clienteSelected.getRecomendadoPor().getNombreYApellido());
			recomendadoPorSelected = clienteSelected.getRecomendadoPor();
		}
		if(clienteSelected.getFechaComienzo()!=null){
			cmbFechaInicio.setValue(UtilsGeneral.getLocalDateFromDate(clienteSelected.getFechaComienzo()));
		}
		if(clienteSelected.getActivo()!=null){
			chkActivo.setSelected(clienteSelected.getActivo());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, clienteSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				clienteService.deleteClient(clienteSelected);
				txtClienteBusqueda.setText(ConstantesEtiquetas.VACIO);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.CLIENTE_ASOCIADO_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			}catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	public void recargarTabla() {
		try {
			buttonDisable(true);
			tableCliente.getColumns().clear();
			recarga = true;
			cargaTabla();
			mostrarTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void recomendadoPorProceso(Cliente c){
		recomendadoPorSelected = c;
		lblRecomendadoPorValor.setText(recomendadoPorSelected.getNombreYApellido());
		mostrarFormulario();
		recomendadoPorSeleccionando = false;
	}

	public void cargaTabla() {
		if(TareaAppApplication.clientes==null || recarga){
			TareaAppApplication.clientes = clienteService.getClientes();
			TareaAppApplication.polizas = polizaService.getPolizas();
			recarga = false;
		}
		clientes = FXCollections.observableArrayList(TareaAppApplication.clientes);
		UtilCargaTablas.cargaClienteTablaConFiltro(tableCliente, clientes, txtClienteBusqueda, lblInfo);
		UtilsGeneral.refreshBusqueda(txtClienteBusqueda);
		tableCliente.setOnMouseClicked(
				event -> {
					Cliente c = tableCliente.getSelectionModel().getSelectedItem();
					if (!recomendadoPorSeleccionando && event.getClickCount() == 2 && c != null) {
						clienteSelected = c;
						updateButton();
					}else {
						if (c != null) {
							if(recomendadoPorSeleccionando){
								if(clienteSelected!=null && clienteSelected.getId()!=null){
									if(Objects.equals(c.getId(), clienteSelected.getId())){
										UtilsGeneral.error(Errores.CLIENTE_IGUAL);
									}else{
										recomendadoPorProceso(c);
									}
								}else{
									recomendadoPorProceso(c);
								}
							}else{
								if (clienteSelected != null) {
									if (clienteSelected != c) {
										clienteSelected = c;
										buttonDisable(false);
									} else {
										tableCliente.getSelectionModel().clearSelection();
										clienteSelected = null;
										buttonDisable(true);
									}
								} else {
									clienteSelected = c;
									buttonDisable(false);
								}
							}
						}
					}
				});
	}

	public void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable,MenuPrincipal.Clientes.getPagina(), btnActualizar, btnEliminar,null);
	}

	public void nuevoCliente() {
		LocalDate now = Instant.ofEpochMilli(new Date().getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
		cmbFechaInicio.setValue(now);
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		mostrarFormulario();
	}

	private void mostrarFormulario(){
		buttonDisable(true);
		paneTabel.setOpacity(0);
		new FadeInUpTransition(paneCrud).play();
	}

	public void mostrarTabla() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		paneCrud.setOpacity(0);
		new FadeInUpTransition(paneTabel).play();
	}

	private void clear() {
		txtNombre.setText(ConstantesEtiquetas.VACIO);
		txtApellido.setText(ConstantesEtiquetas.VACIO);
		txtDireccion.setText(ConstantesEtiquetas.VACIO);
		txtCiudad.setText(ConstantesEtiquetas.VACIO);
		txtDepartamento.setText(ConstantesEtiquetas.VACIO);
		cmbFechaNacimiento.setValue(null);
		txtTelefono.setText(ConstantesEtiquetas.VACIO);
		txtCelular.setText(ConstantesEtiquetas.VACIO);
		txtEmail.setText(ConstantesEtiquetas.VACIO);
		txtCedulaIdentidad.setText(ConstantesEtiquetas.VACIO);
		txtLibretaPropiedad.setText(ConstantesEtiquetas.VACIO);
		lblRecomendadoPorValor.setText(ConstantesEtiquetas.VACIO);
		cmbFechaInicio.setValue(null);
	}

	public void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try {
			if (clienteSelected == null) {
				clienteSelected = new Cliente();
			}
			  boolean updateProcess = clienteSelected.getId() != null;
			  clienteSelected.setNombre(txtNombre.getText());
			  clienteSelected.setApellido(txtApellido.getText());
			  clienteSelected.setDireccion(txtDireccion.getText());
			  clienteSelected.setCiudad(txtCiudad.getText());
			  clienteSelected.setDepartamento(txtDepartamento.getText());
			  clienteSelected.setFechaNacimiento(UtilsGeneral.getDateFromLocalDate(cmbFechaNacimiento.getValue()));
			  clienteSelected.setTelefono(txtTelefono.getText());
			  clienteSelected.setCelular(txtCelular.getText());
			  clienteSelected.setEmail(txtEmail.getText());
			  clienteSelected.setCedulaIdentidad(txtCedulaIdentidad.getText());
			  clienteSelected.setLibretaPropiedad(txtLibretaPropiedad.getText());
			  clienteSelected.setRecomendadoPor(recomendadoPorSelected);
			  clienteSelected.setFechaComienzo(UtilsGeneral.getDateFromLocalDate(cmbFechaInicio.getValue()));
			  clienteSelected.setActivo(chkActivo.isSelected());
			  clienteService.saveClient(clienteSelected);
			  if (updateProcess) {
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			  } else {
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			  }
			  recargarTabla();
			  mostrarTabla();
			  clienteSelected = null;
		}catch(ObjectOptimisticLockingFailureException e){
			UtilsGeneral.error(Errores.CLIENTE_YA_ACTUALIZADO);
		}catch(Exception ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().isEmpty()) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_CLIENTE);
		}else if(txtCedulaIdentidad.getText().isEmpty()){
			txtCedulaIdentidad.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_CEDULA_CLIENTE);
		} else if((clienteSelected==null || clienteSelected.getId()==null) &&
				clientes.stream().anyMatch(cliente -> cliente.getCedulaIdentidad().equals(txtCedulaIdentidad.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.CLIENTE_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
