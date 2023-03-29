package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.ClienteService;
import com.software.tareasApp.domain.service.EstadoSiniestroService;
import com.software.tareasApp.domain.service.PolizaService;
import com.software.tareasApp.domain.service.SiniestroService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.persistence.model.Cliente;
import com.software.tareasApp.persistence.model.EstadoSiniestro;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.persistence.model.Siniestro;
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
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class SiniestroController implements Initializable {

	@FXML
	private AnchorPane paneFormulario;

	@FXML
	private AnchorPane paneTableSiniestro;

	@FXML
	private AnchorPane paneTablePoliza;

	@FXML
	private AnchorPane paneTableCliente;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<Siniestro> tableSiniestros;

	@FXML
	private TableView<Cliente> tableClientes;

	@FXML
	private TableView<Poliza> tablePolizas;

	@FXML
	private Label lblCliente;

	@FXML
	private TextField txtNumeroSiniestro;

	@FXML
	private Label lblPoliza;

	@FXML
	private DatePicker cmbFecha;

	@FXML
	private CheckBox chkEsDeducible;

	@FXML
	private TextField txtImporteDeducible;

	@FXML
	private ComboBox<EstadoSiniestro> cmbEstado;

	@FXML
	private TextArea txtInformacion;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnGuardar;

	@FXML
	private Button btnActualizar;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnBackSiniestros;

	@FXML
	private Button btnBackCliente;

	@FXML
	private Button btnBackPoliza;

	@FXML
	private Button btnCliente;

	@FXML
	private Button btnPoliza;

	@FXML
	private TextField txtSiniestroBusqueda;

	@FXML
	private TextField txtPolizaBusqueda;

	@FXML
	private TextField txtClienteBusqueda;

	@FXML
	private Label lblInfoSiniestro;

	@FXML
	private Label lblInfoCliente;

	@FXML
	private Label lblInfoPoliza;

	private ObservableList<Siniestro> siniestros;
	private Poliza polizaSelected;
	private Cliente clienteSelected;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Autowired
	public SiniestroController(SiniestroService siniestroService, PolizaService polizaService, ClienteService clienteService,
							   EstadoSiniestroService estadoSiniestroService, ConstantesPaginas constantesPaginas){
		this.siniestroService = siniestroService;
		this.polizaService = polizaService;
		this.estadoSiniestroService = estadoSiniestroService;
		this.clienteService = clienteService;
		this.constantesPaginas = constantesPaginas;
	}

	private final ConstantesPaginas constantesPaginas;
	private final SiniestroService siniestroService;
	private final PolizaService polizaService;
	private final ClienteService clienteService;
	private final EstadoSiniestroService estadoSiniestroService;


	private Siniestro siniestroSelected;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuPrincipal.Siniestros.getPagina());
			txtInformacion.setWrapText(true);
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void task() {
		Task longTask = UtilsGeneral.task();

		longTask.setOnSucceeded((EventHandler<WorkerStateEvent>) t -> {
			try {
				txtImporteDeducible.setDisable(!chkEsDeducible.isDisable());
				cargaTabla();
				mostrarTablaSiniestro();
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
		btnPolizaOnAction();
		btnBackPolizaYClienteOnAction();
		btnClienteOnAction();
		chkOnListener();
	}

	private void chkOnListener(){
		chkEsDeducible.selectedProperty().addListener((observable, oldValue, newValue) -> txtImporteDeducible.setDisable(!newValue));
	}

	private void btnBackPolizaYClienteOnAction(){
		btnBackPoliza.setOnAction((event) -> mostrarFormulario());
		btnBackCliente.setOnAction((event) -> mostrarFormulario());
	}

	private void btnPolizaOnAction(){
		btnPoliza.setOnAction((event) -> mostrarTablaPoliza());
	}

	private void btnClienteOnAction(){
		btnCliente.setOnAction((event) -> mostrarTablaCliente());
	}

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void updateButton(){
		if(siniestroSelected.getCliente()!=null){
			lblCliente.setText(siniestroSelected.getCliente().getNombre() + " " + siniestroSelected.getCliente().getApellido());
			clienteSelected = siniestroSelected.getCliente();
		}
		if(siniestroSelected.getPoliza()!=null){
			lblPoliza.setText(siniestroSelected.getPoliza().getNumeroPoliza());
			polizaSelected = siniestroSelected.getPoliza();
		}
		if(siniestroSelected.getNumeroSiniestro()!=null){
			txtNumeroSiniestro.setText(siniestroSelected.getNumeroSiniestro());
		}
		if(siniestroSelected.getFecha()!=null){
			cmbFecha.setValue(UtilsGeneral.getLocalDateFromDate(siniestroSelected.getFecha()));
		}
		if(siniestroSelected.getEsDeducible()!=null){
			chkEsDeducible.setSelected(siniestroSelected.getEsDeducible());
		}
		if(siniestroSelected.getImporteDeducible()!=null){
			txtImporteDeducible.setText(siniestroSelected.getImporteDeducible().toString());
		}
		if(siniestroSelected.getEstadoSiniestro()!=null){
			cmbEstado.setValue(siniestroSelected.getEstadoSiniestro());
		}
		if(siniestroSelected.getInformacion()!=null){
			txtInformacion.setText(siniestroSelected.getInformacion());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneFormulario, siniestroSelected.getNumeroSiniestro(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				siniestroService.deleteSiniestro(siniestroSelected);
				txtSiniestroBusqueda.setText(ConstantesEtiquetas.VACIO);
				recargarTablaSiniestro();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.SINIESTRO_ASOCIADO_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	private void btnAgregarOnAction(){
		btnAgregar.setOnAction((event) ->{
			clear();
			siniestroSelected = null;
			nuevoSiniestro();
		});
	}

	private void btnBackOnAction(){
		btnBackSiniestros.setOnAction((event) -> mostrarTablaSiniestro());
	}

	private void recargarTablaSiniestro() {
		try {
			buttonDisable(true);
			tableSiniestros.getColumns().clear();
			cargaTabla();
			mostrarTablaSiniestro();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTabla() {
		cargaTablaSiniestro();
		cargaTablaPoliza();
		cargaTablaCliente();
		cargaComboEstado();
	}

	private void cargaTablaPoliza(){
		if(TareaAppApplication.polizas==null){
			TareaAppApplication.polizas = polizaService.getPolizas();
		}
		ObservableList<Poliza> polizas = FXCollections.observableArrayList(TareaAppApplication.polizas);
    	UtilCargaTablas.cargaTablaPoliza(
        tablePolizas,
        polizas,
        txtPolizaBusqueda,
        lblInfoPoliza,
        MenuPrincipal.Siniestros.getPagina());
		tablePolizas.setOnMouseClicked(
				event -> {
					Poliza p = tablePolizas.getSelectionModel().getSelectedItem();
					if (p != null) {
						if (polizaSelected != null) {
							if (polizaSelected != p) {
								polizaSelected = p;
								mostrarFormulario();
								lblPoliza.setText(polizaSelected.getNumeroPoliza());
								validCliente();
							} else {
								tablePolizas.getSelectionModel().clearSelection();
								polizaSelected = null;
								mostrarFormulario();
								lblPoliza.setText(ConstantesEtiquetas.VACIO);
							}
						} else {
							polizaSelected = p;
							mostrarFormulario();
							lblPoliza.setText(polizaSelected.getNumeroPoliza());
							validCliente();
						}
					}
				});
	}

	private void validCliente(){
		if(clienteSelected!=null){
			if (!polizaSelected
					.getCliente()
					.getCedulaIdentidad()
					.equals(clienteSelected.getCedulaIdentidad())){
				clienteSelected = polizaSelected.getCliente();
				lblCliente.setText(clienteSelected.getNombreYApellido());
			}
		}else{
			clienteSelected = polizaSelected.getCliente();
			lblCliente.setText(clienteSelected.getNombreYApellido());
		}
	}

	private void cargaTablaCliente(){
		if(TareaAppApplication.clientes==null){
			TareaAppApplication.clientes = clienteService.getClientes();
		}
		ObservableList<Cliente> clientes = FXCollections.observableArrayList(TareaAppApplication.clientes);
		UtilCargaTablas.cargaClienteTablaConFiltro(tableClientes, clientes, txtClienteBusqueda,lblInfoCliente);
		tableClientes.setOnMouseClicked(
				event -> {
					Cliente c = tableClientes.getSelectionModel().getSelectedItem();
					if (c != null) {
						if (clienteSelected != null) {
							if (clienteSelected != c) {
								clienteSelected = c;
								mostrarFormulario();
								lblCliente.setText(clienteSelected.getNombreYApellido());
								validPoliza();
							} else {
								tableClientes.getSelectionModel().clearSelection();
								clienteSelected = null;
								mostrarFormulario();
								lblCliente.setText(ConstantesEtiquetas.VACIO);
							}
						} else {
							clienteSelected = c;
							mostrarFormulario();
							lblCliente.setText(clienteSelected.getNombreYApellido());
							validPoliza();
						}
					}
				});
	}

	private void validPoliza(){
		txtPolizaBusqueda.setText(clienteSelected.getNombreYApellido().trim());
		if(polizaSelected!=null){
			if (!polizaSelected
					.getCliente()
					.getCedulaIdentidad()
					.equals(clienteSelected.getCedulaIdentidad())){
				polizaSelected = null;
				lblPoliza.setText(ConstantesEtiquetas.VACIO);
			}
		}
	}

	private void cargaComboEstado() {
		ObservableList<EstadoSiniestro> options
				= FXCollections.observableArrayList(estadoSiniestroService.getEstadoSiniestros());
		cmbEstado.setItems(options);
	}

	private void cargaTablaSiniestro(){
		btnAgregar.setDisable(false);
		siniestros = FXCollections.observableArrayList(siniestroService.getSiniestros());
		UtilCargaTablas.cargaTablaSiniestroConFiltro(tableSiniestros, siniestros,txtSiniestroBusqueda, lblInfoSiniestro);
		UtilsGeneral.refreshBusqueda(txtSiniestroBusqueda);
		tableSiniestros.setOnMouseClicked(
				event -> {
					Siniestro s = tableSiniestros.getSelectionModel().getSelectedItem();
					if (event.getClickCount() == 2 && s != null) {
						siniestroSelected = s;
						updateButton();
					}else {
						if (s != null) {
							if (siniestroSelected != null) {
								if (siniestroSelected != s) {
									siniestroSelected = s;
									buttonDisable(false);
								} else {
									tableSiniestros.getSelectionModel().clearSelection();
									siniestroSelected = null;
									buttonDisable(true);
								}
							} else {
								siniestroSelected = s;
								buttonDisable(false);
							}
						}
					}
				});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuPrincipal.Siniestros.getPagina(), btnActualizar, btnEliminar, null);
	}

	private void clear() {
		lblCliente.setText(ConstantesEtiquetas.VACIO);
		txtNumeroSiniestro.setText(ConstantesEtiquetas.VACIO);
		lblPoliza.setText(ConstantesEtiquetas.VACIO);
		cmbFecha.setValue(null);
		chkEsDeducible.setSelected(false);
		txtImporteDeducible.setText(ConstantesEtiquetas.VACIO);
		cmbEstado.setValue(null);
		txtInformacion.setText(ConstantesEtiquetas.VACIO);
		siniestroSelected = null;
		clienteSelected = null;
		polizaSelected = null;
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void mostrarTablaSiniestro() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		paneFormulario.setOpacity(0);
		new FadeInUpTransition(paneTableSiniestro).play();
	}

	private void mostrarTablaCliente(){
		paneTableSiniestro.setOpacity(0);
		paneTablePoliza.setOpacity(0);
		paneFormulario.setOpacity(0);
		new FadeInUpTransition(paneTableCliente).play();
	}

	private void mostrarTablaPoliza(){
		paneTableSiniestro.setOpacity(0);
		paneTableCliente.setOpacity(0);
		paneFormulario.setOpacity(0);
		new FadeInUpTransition(paneTablePoliza).play();
	}

	private void nuevoSiniestro(){
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		LocalDate now = Instant.ofEpochMilli(new Date().getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
		cmbFecha.setValue(now);
		mostrarFormulario();
	}

	private void mostrarFormulario(){
		buttonDisable(true);
		paneTableSiniestro.setOpacity(0);
		paneTableCliente.setOpacity(0);
		paneTablePoliza.setOpacity(0);
		new FadeInUpTransition(paneFormulario).play();
	}

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(siniestroSelected == null){
				siniestroSelected = new Siniestro();
			}
			siniestroSelected.setImporteDeducible(null);
			boolean updateProcess = siniestroSelected.getId()!=null;
			siniestroSelected.setNumeroSiniestro(txtNumeroSiniestro.getText());
			siniestroSelected.setCliente(polizaSelected.getCliente());
			siniestroSelected.setPoliza(polizaSelected);
			siniestroSelected.setFecha(UtilsGeneral.getDateFromLocalDate(cmbFecha.getValue()));
			siniestroSelected.setEsDeducible(chkEsDeducible.isSelected());
			siniestroSelected.setEstadoSiniestro(cmbEstado.getValue());
			siniestroSelected.setInformacion(txtInformacion.getText());
			siniestroService.saveSiniestro(siniestroSelected);
			if(updateProcess){
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTablaSiniestro();
			mostrarTablaSiniestro();
		}catch(ObjectOptimisticLockingFailureException e){
			UtilsGeneral.error(Errores.SINIESTRO_YA_ACTUALIZADO);
		}catch(Exception ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNumeroSiniestro.getText().equals(ConstantesEtiquetas.VACIO)) {
			txtNumeroSiniestro.requestFocus();
			return UtilsGeneral.error(Errores.NUMERO_SINIESTRO_VACIO);
		} else if((siniestroSelected==null || siniestroSelected.getId()==null) &&
				siniestros.stream().anyMatch(siniestro -> siniestro.getNumeroSiniestro().equals(txtNumeroSiniestro.getText()))){
			txtNumeroSiniestro.requestFocus();
			return UtilsGeneral.error(Errores.NUMERO_SINIESTRO_EXISTE);
		} else if(polizaSelected==null){
			btnPoliza.requestFocus();
			return UtilsGeneral.error(Errores.POLIZA_NO_EXISTE);
		} else if(clienteSelected==null){
			btnCliente.requestFocus();
			return UtilsGeneral.error(Errores.CLIENTE_NO_EXISTE);
		} else if(chkEsDeducible.isSelected() && (txtImporteDeducible.getText().equals(ConstantesEtiquetas.VACIO) || !UtilsGeneral.esNumero(txtImporteDeducible.getText()))){
			txtImporteDeducible.requestFocus();
			return UtilsGeneral.error(Errores.VERIFIQUE_DEDUCIBLE);
		}else if (cmbEstado.getValue()==null){
			cmbEstado.requestFocus();
			return UtilsGeneral.error(Errores.VERIFIQUE_ESTADO);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
