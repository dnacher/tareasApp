package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.*;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.persistence.model.*;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PolizaController implements Initializable {

	@FXML
	private Label lblCompaniaValor;

	@FXML
	private Label lblCompania;

	@FXML
	private Label lblClienteValor;

	@FXML
	private Label lblCliente;

	@FXML
	private TextField txtNumeroPoliza;

	@FXML
	private Label lblNumeroPoliza;

	@FXML
	private DatePicker cmbFechaComienzo;

	@FXML
	private Label lblFechaComienzo;

	@FXML
	private DatePicker cmbFechaVencimiento;

	@FXML
	private Label lblProducto;

	@FXML
	private Label lblProductoValor;

	@FXML
	private Label lblTipoProductoValor;

	@FXML
	private Label lblTipoProducto;

	@FXML
	private TextField txtPremio;

	@FXML
	private Label lblPremio;

	@FXML
	private Label lblPrima;

	@FXML
	private TextField txtPrima;

	@FXML
	private ComboBox<Moneda> cmbMoneda;

	@FXML
	private Label lblMoneda;

	@FXML
	private Label lblComisionValor;

	@FXML
	private Label lblComision;

	@FXML
	private Label lblFormaPago;

	@FXML
	private ComboBox<FormaPago> cmbFormaPago;

	@FXML
	private Label lblCuotas;

	@FXML
	private TextField txtCuotas;

	@FXML
	private Label lblComienzoCuota;

	@FXML
	private DatePicker cmbComienzoCuota;

	@FXML
	private Label lblFinCuota;

	@FXML
	private DatePicker cmbFinCuota;

	@FXML
	private Label lblImporteCuotaValor;

	@FXML
	private Label lblImporteCuota;

	@FXML
	private Label lblCerradoPorValor;

	@FXML
	private Label lblCerradoPor;

	@FXML
	private Label lblEsApp;

	@FXML
	private CheckBox chkEsApp;

	@FXML
	private Label lblEstado;

	@FXML
	private ComboBox<EstadoPoliza> cmbEstado;

	@FXML
	private ComboBox<CotizacionVendedor> cmbVendedor;

	@FXML
	private TextArea txtObservaciones;

	@FXML
	private Label lblInfoObservaciones;

	@FXML
	private Label lblObservaciones;

	@FXML
	private AnchorPane paneFormulario;

	@FXML
	private AnchorPane paneTablePolizas;

	@FXML
	private TextField txtPolizaBusqueda;

	@FXML
	private TableView<Poliza> tablePoliza;

	@FXML
	private Label lblInfoPoliza;

	@FXML
	private AnchorPane paneTableProductos;

	@FXML
	private TextField txtProductoBusqueda;

	@FXML
	private TableView<Producto> tableProducto;

	@FXML
	private TableView<Siniestro> tableSiniestro;

	@FXML
	private Label lblInfoProducto;

	@FXML
	private AnchorPane paneTableClientes;

	@FXML
	private TextField txtClienteBusqueda;

	@FXML
	private TableView<Cliente> tableCliente;

	@FXML
	private TableView<Poliza> tablePolizaEndoso;

	@FXML
	private Label lblInfoCliente;

	@FXML
	private Label lblFechaVencimiento;

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
	private Button btnEndoso;

	@FXML
	private Button btnBackFormulario;

	@FXML
	private Button btnCompania;

	@FXML
	private Button btnCliente;

	@FXML
	private Button btnProducto;

	@FXML
	private Button btnBackTablaPolizas;

	@FXML
	private Button btnBackTablaClientes;

	@FXML
	private Button btnBackTablaProductos;

	@FXML
	private CheckBox chkVendedor;

	@FXML
	private Text title;

	@FXML
	private TabPane tabPane;

	@FXML
	private Text txtInfoPoliza;

	@FXML
	private Text lblInfoEndoso;

	@FXML
	private Text lblInfoSiniestro;

	private final LogManagerClass logger = new LogManagerClass(getClass());
	private Poliza polizaSelected;
	private Producto productoSelected;
	private Cliente clienteSelected;
	private String moneda = "";
	private Double importeCuota;
	private EstadoPoliza estadoEndoso;
	private ObservableList<CotizacionVendedor> todosVendedores;
	private ObservableList<Poliza> polizas;
	private boolean endoso = false;
	private boolean recarga = false;

	@Autowired
	public PolizaController(PolizaService polizaService, ProductoService productoService, ClienteService clienteService, MonedaService monedaService,
							FormaPagoService formaPagoService, EstadoPolizaService estadoPolizaService, CotizacionVendedorService cotizacionVendedorService, ConstantesPaginas constantesPaginas, SiniestroService siniestroService){
		this.polizaService = polizaService;
		this.productoService = productoService;
		this.clienteService = clienteService;
		this.monedaService = monedaService;
		this.formaPagoService = formaPagoService;
		this.estadoPolizaService = estadoPolizaService;
		this.cotizacionVendedorService = cotizacionVendedorService;
		this.constantesPaginas = constantesPaginas;
		this.siniestroService = siniestroService;
	}

	private final SiniestroService siniestroService;
	private final PolizaService polizaService;
	private final ProductoService productoService;
	private final ClienteService clienteService;
	private final MonedaService monedaService;
	private final FormaPagoService formaPagoService;
	private final EstadoPolizaService estadoPolizaService;
	private final CotizacionVendedorService cotizacionVendedorService;
	private final ConstantesPaginas constantesPaginas;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(btnAgregar, btnEndoso, btnActualizar, btnEliminar, btnGuardar);
			title.setText(constantesPaginas.POLIZA);
			constantesPaginas.setPoliza(lblCompania,btnCompania,lblCompaniaValor,
										lblCliente,btnCliente,lblClienteValor,
										lblNumeroPoliza, txtNumeroPoliza,
										lblFechaComienzo, cmbFechaComienzo,
										lblFechaVencimiento, cmbFechaVencimiento,
										lblProducto,btnProducto, lblProductoValor,
										lblTipoProducto, lblTipoProductoValor,
										lblPremio, txtPremio,
										lblPrima, txtPrima,
										lblMoneda, cmbMoneda,
										lblComision, lblComisionValor,
										lblFormaPago, cmbFormaPago,
										lblCuotas, txtCuotas,
										lblComienzoCuota, cmbComienzoCuota,
										lblFinCuota, cmbFinCuota,
										lblImporteCuota, lblImporteCuotaValor,
										lblCerradoPor,lblCerradoPorValor,
										lblEsApp, chkEsApp,
										lblEstado, cmbEstado,
										chkVendedor, cmbVendedor,
										lblObservaciones, txtObservaciones, lblInfoObservaciones);
      		seguridad();
			  txtObservaciones.setWrapText(true);
			task();
			tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void seguridad(){
		UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuPrincipal.Polizas.getPagina());
		UtilsSeguridad.setPermisoAdmin(btnEndoso, MenuPrincipal.Polizas.getPagina());
	}

	public void task() {
		Task longTask = UtilsGeneral.task();
		longTask.setOnSucceeded((EventHandler<WorkerStateEvent>) t -> {
			try {
				cmbVendedor.setVisible(false);
				lblCerradoPorValor.setText(Constantes.SBN);
				buttonDisable(true);
				cargaTablas();
				cargaCombos();
				buttonsOnAction();
				mostrarTablaPoliza();
				cargaListeners();
				textArea();
			} catch (Exception ex) {
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void textArea(){
		UtilsGeneral.textArea(lblInfoObservaciones, txtObservaciones);
	}

	private void buttonsOnAction(){
		btnAgregarOnAction();
		btnGuardarOnAction();
		btnActualizarOnAction();
		btnEndosoOnAction();
		btnEliminarOnAction();
		btnCompaniaProductoOnAction();
		btnClienteOnAction();
		btnBackFormularioOnAction();
		btnBackTablaProductoOnAction();
		btnBackTablaClientesOnAction();
	}

	private void cargaTablas(){
		cargaTablaPoliza();
		cargaTablaProducto();
		cargaTablaCliente();
	}

	private void cargaCombos(){
		cargaComboMoneda();
		cargaComboFormaPago();
		cargaComboEstado();
		cargaComboVendedor();
	}

	private void cargaListeners(){
		checkListener();
		cmbEstadoListener();
		txtPremioListener();
		cmbFormaPagoListener();
		cmbFinCuotaListener();
		cmbFechaComienzoListener();
		cuotasListener();
		cmbComienzoCuotaListener();
	}

	private void cuotasListener(){
		txtCuotas
			.textProperty()
			.addListener(
				(observable, oldValue, newValue) -> {
				  if (UtilsGeneral.numeroPositivo(newValue)) {
					  if(cmbComienzoCuota.getValue()!=null){
						  int cuota = Integer.parseInt(newValue);
						  cuota--;
						  cmbFinCuota.setValue(cmbComienzoCuota.getValue().plusMonths(cuota));
					  }
					  if (UtilsGeneral.esNumero(txtPremio.getText())) {
						if (Double.parseDouble(txtPremio.getText()) > 0) {
							Double value = Double.parseDouble(txtPremio.getText()) / Double.parseDouble(newValue);
							importeCuota = value;
							lblImporteCuotaValor.setText(moneda + String.format("%.2f", value));

						}
					  }
				  }
				});
	}

	private void cmbEstadoListener(){
		cmbEstado.valueProperty().addListener(((observable, oldValue, newValue) -> {
			if(newValue!=null){
				cmbFechaVencimiento.setDisable(newValue.getNombre().equals(ConstantesEtiquetas.ENDOSO));
			}
		}));
	}

	private void checkListener(){
		chkVendedor.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(productoSelected!=null){
				if(newValue){
					cmbVendedor.setVisible(true);
					if(cmbVendedor.getValue()!=null){
						lblCerradoPorValor.setText(cmbVendedor.getValue().getVendedor().getNombreYApellido());
						if(cmbEstado.getValue()!=null){
							seteaComision(cmbEstado.getValue().getNombre());
						}
					}else{
						if(cmbEstado.getValue()!=null){
							seteaComision(cmbEstado.getValue().getNombre());
						}
						lblCerradoPorValor.setText(Constantes.SBN);
					}
				}else{
					if(cmbEstado.getValue()!=null){
						seteaComision(cmbEstado.getValue().getNombre());
					}
					lblCerradoPorValor.setText(Constantes.SBN);
					cmbVendedor.setVisible(false);
				}
			}else{
				UtilsGeneral.error(Errores.ELEGIR_PRODUCTO);
				chkVendedor.setSelected(false);
			}

		});
	}

	private void txtPremioListener(){
		txtPremio.textProperty().addListener((observable, oldValue, newValue) -> {
			if(UtilsGeneral.esNumero(newValue)){
				if(UtilsGeneral.esNumero(txtCuotas.getText())){
					if(Integer.parseInt(txtCuotas.getText())>0){
						Double value = Double.parseDouble(newValue) / Double.parseDouble(txtCuotas.getText());
						importeCuota = value;
						lblImporteCuotaValor.setText(moneda + String.format("%.2f", value));
					}else{
						importeCuota = Double.valueOf(newValue);
						lblImporteCuotaValor.setText(moneda + newValue);
					}
				}else{
					importeCuota = Double.valueOf(newValue);
					lblImporteCuotaValor.setText(moneda + newValue);
				}
			}else{
				lblImporteCuotaValor.setText(ConstantesEtiquetas.VACIO);
			}
		});
	}

	private void cmbFormaPagoListener(){
		cmbFormaPago.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if(newValue!=null){
				if(newValue.getNombre().equals("contado")){
					cmbFinCuota.setDisable(true);
					txtCuotas.setText("1");
					txtCuotas.setDisable(true);
					cmbcuotasSetters();
				}else{
					cmbFinCuota.setDisable(false);
					txtCuotas.setText(ConstantesEtiquetas.VACIO);
					txtCuotas.setDisable(false);
				}
			}
		});
	}

	private void cmbcuotasSetters(){
		if(cmbComienzoCuota.getValue()!=null){
			cmbFinCuota.setValue(cmbComienzoCuota.getValue());
		}else{
			cmbFinCuota.setValue(null);
		}
	}

	private void cmbFinCuotaListener(){
		cmbComienzoCuota.valueProperty().addListener((ov, oldValue, newValue) -> {
			if(cmbFormaPago.getValue()!=null){
				if(cmbFormaPago.getValue().getNombre().equals("contado")){
					cmbFinCuota.setValue(cmbComienzoCuota.getValue());
				}
			}
		});
	}

	private void cmbComienzoCuotaListener(){
		cmbComienzoCuota.valueProperty().addListener((ov, oldValue, newValue) ->{
			if(newValue!=null){
				if(UtilsGeneral.numeroPositivo(txtCuotas.getText())){
					int i = Integer.parseInt(txtCuotas.getText());
					i--;
					cmbFinCuota.setValue(newValue.plusMonths(i));
				}
			}
		});
	}

	private void cmbFechaComienzoListener(){
    cmbFechaComienzo
        .valueProperty()
        .addListener(
            (ov, oldValue, newValue) -> {
            if (newValue != null) {
                if (cmbEstado.getValue() != null){
					if(!cmbEstado.getValue().getNombre().equals(ConstantesEtiquetas.ENDOSO)){
						cmbFechaVencimiento.setValue(newValue.plusYears(1));
					}else{
						if(polizaSelected!=null && polizaSelected.getPolizaMadre()!=null){
							cmbFechaVencimiento.setValue(UtilsGeneral.getLocalDateFromDate(polizaSelected.getPolizaMadre().getVencimiento()));
						}
					}
				}else {
					cmbFechaVencimiento.setValue(newValue.plusYears(1));
				}
			}
		});
	}

	private void btnBackTablaClientesOnAction(){
		btnBackTablaClientes.setOnAction((event) -> mostrarFormulario());
	}

	private void btnBackTablaProductoOnAction(){
		btnBackTablaProductos.setOnAction((event) -> mostrarFormulario());
	}

	private void btnBackFormularioOnAction(){
		btnBackFormulario.setOnAction((event) -> {
			mostrarTablaPoliza();
			tablePoliza.getSelectionModel().clearSelection();
			polizaSelected = null;
			buttonDisable(true);
		});
	}

	private void btnCompaniaProductoOnAction(){
		btnCompania.setOnAction((event) -> mostrarTablaProducto());
		btnProducto.setOnAction((event) -> mostrarTablaProducto());
	}

	private void btnClienteOnAction(){
		btnCliente.setOnAction((event) -> mostrarTablaCliente());
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction(
				(event) -> guardar());
	}

	private void btnActualizarOnAction() {
		btnActualizar.setOnAction((event) -> actualizar());
	}

	private void actualizar(){
		clear();
		endoso = false;
		String numPoliza = polizaSelected.getNumeroPoliza();
		String nombreCliente = null;
		if(polizaSelected.getCliente()!=null){
			nombreCliente = polizaSelected.getCliente().getNombreYApellido();
		}
		if(numPoliza!=null){
			if(nombreCliente!=null){
				txtInfoPoliza.setText(numPoliza + " - " + nombreCliente);
			}else{
				txtInfoPoliza.setText(numPoliza);
			}
		}

		ObservableList<Siniestro> siniestros = FXCollections.observableArrayList(siniestroService.findByPoliza(polizaSelected));
		ObservableList<Poliza> polizasEndoso =  FXCollections.observableArrayList(polizaService.findByPolizaMadre(polizaSelected));
		lblInfoSiniestro.setText(	"Siniestros: " + siniestros.size());
		lblInfoEndoso.setText(		"Endosos:   " + polizasEndoso.size());
		tableSiniestro.getColumns().clear();
		tablePolizaEndoso.getColumns().clear();
		UtilCargaTablas.cargaTablaSiniestroSinFiltro(tableSiniestro,siniestros);
    	UtilCargaTablas.cargaTablaPolizaSinFiltro(tablePolizaEndoso, polizasEndoso, MenuPrincipal.Polizas.getPagina());
		updateButton();
		txtNumeroPoliza.setDisable(false);
		mostrarFormulario();
	}

  	private void btnEliminarOnAction() {
		btnEliminar.setOnAction(
			(event) -> deleteButton());
	}

	private void btnEndosoOnAction() {
		btnEndoso.setOnAction(
				(event) -> {
					endoso = true;
					updateButton();
					txtNumeroPoliza.setText(UtilsGeneral.creaNumeroPolizaEndoso(polizaSelected.getNumeroPoliza()));
					cmbEstado.setValue(estadoEndoso);

					txtNumeroPoliza.setDisable(true);
					cmbFechaVencimiento.setDisable(true);
					txtPremio.setText(ConstantesEtiquetas.VACIO);
					txtPrima.setText(ConstantesEtiquetas.VACIO);
					cmbFormaPago.setValue(null);
					cmbComienzoCuota.setValue(null);
					cmbFinCuota.setValue(null);
					txtCuotas.setText(ConstantesEtiquetas.VACIO);
					cmbMoneda.setValue(null);
					mostrarFormulario();
					endoso();
				});
	}

	private void endoso(){
		Poliza poliza = UtilsGeneral.creaPoliza(polizaSelected);
		polizaSelected = UtilsGeneral.creaPoliza(poliza);
		polizaSelected.setPolizaMadre(poliza);
		polizaSelected.setId(null);
	}

	private void btnAgregarOnAction(){
		btnAgregar.setOnAction((event) -> {
			endoso = false;
			btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
			clear();
			LocalDate now = Instant.ofEpochMilli(new Date().getTime())
					.atZone(ZoneId.systemDefault())
					.toLocalDate();
			cmbFechaComienzo.setValue(now);
			cmbComienzoCuota.setValue(now);
			polizaSelected = null;
			txtNumeroPoliza.setDisable(false);
			mostrarFormulario();
		});
	}

	private void deleteButton() {
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneFormulario, polizaSelected.getNumeroPoliza(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				polizaService.deletePolicy(polizaSelected);
				recargarTablaPoliza();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
				txtPolizaBusqueda.setText(ConstantesEtiquetas.VACIO);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.POLIZA_ASOCIADA_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	private void cargaComboMoneda(){
		ObservableList<Moneda> optiones = FXCollections.observableArrayList(monedaService.getMonedas());
		cmbMoneda.setItems(optiones);
		cmbMoneda.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if(newValue!=null){
				moneda = newValue.getSimbolo() + " ";
				if(importeCuota!=null){
					lblImporteCuotaValor.setText(moneda + importeCuota);
				}else{
					lblImporteCuotaValor.setText(moneda + "0");
				}
			}
		});
	}

	private void cargaComboFormaPago(){
		ObservableList<FormaPago> options = FXCollections.observableArrayList(formaPagoService.getFormaPagos());
		cmbFormaPago.setItems(options);
	}

	private void cargaComboEstado(){
		List<EstadoPoliza> estados = estadoPolizaService.getEstadoPolizas();
		estados.forEach(estado -> {
			if(estado.getNombre().equals("Endoso")){
				estadoEndoso = estado;
			}
		});
		estados.removeIf(estado -> estado.getNombre().equals("Endoso"));
		ObservableList<EstadoPoliza> optionsList = FXCollections.observableArrayList(estados);
		cmbEstado.setItems(optionsList);
		cmbEstado.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if(productoSelected!=null){
				seteaComision(newValue.getNombre());
			}
		});
	}

	private void seteaComision(String newValue){
		if(newValue.equals("Nuevo")){
			if(chkVendedor.isSelected() && cmbVendedor.getValue()!=null){
				lblComisionValor.setText(cmbVendedor.getValue().getComisionNueva().toString());
			}else{
				lblComisionValor.setText(productoSelected.getComisionNueva().toString());
			}
		}else if(newValue.equals("Renovacion")){
			if(chkVendedor.isSelected() && cmbVendedor.getValue()!=null){
				lblComisionValor.setText(cmbVendedor.getValue().getComisionRenovacion().toString());
			}else{
				lblComisionValor.setText(productoSelected.getComisionRenovacion().toString());
			}
		}
	}

	private void cargaComboVendedor(){
		todosVendedores = FXCollections.observableArrayList(cotizacionVendedorService.getCotizacionVendedores());
		cmbVendedor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
					if(newValue!=null){
						lblCerradoPorValor.setText(newValue.getVendedor().getNombreYApellido());
						if(cmbEstado.getValue()!=null){
							seteaComision(cmbEstado.getValue().getNombre());
						}
					}
				});
	}

	private void cargaTablaPoliza(){
		if(TareaAppApplication.polizas==null || recarga){
			TareaAppApplication.polizas = polizaService.getPolizas();
			recarga = false;
		}
		polizas = FXCollections.observableArrayList(TareaAppApplication.polizas);
    	UtilCargaTablas.cargaTablaPoliza(
        tablePoliza, polizas, txtPolizaBusqueda, lblInfoPoliza, MenuPrincipal.Polizas.getPagina());
		UtilsGeneral.refreshBusqueda(txtPolizaBusqueda);
		boolean visible = UtilsSeguridad.traePermisoComision(MenuPrincipal.Polizas.getPagina());
		lblComision.setVisible(visible);
		lblComisionValor.setVisible(visible);
    	tablePoliza.setOnMouseClicked(
        	event -> {
//          if (event.getButton() == MouseButton.SECONDARY) {
//            System.out.println("hola");
//          }
				Poliza p = tablePoliza.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && p != null) {
					polizaSelected = p;
					actualizar();
				}else{
					if (p != null) {
					  if (polizaSelected != null) {
						  if (polizaSelected != p) {
							  polizaSelected = p;
							  buttonDisable(false);
						  } else {
							  tablePoliza.getSelectionModel().clearSelection();
							  polizaSelected = null;
							  buttonDisable(true);
						  }
					  } else {
						  polizaSelected = p;
						  buttonDisable(false);
					  }
					}
				}
        	});
	}

	private void cargaTablaProducto(){
		ObservableList<Producto> productos = FXCollections.observableArrayList(productoService.getProductos());
    	UtilCargaTablas.cargaTablaProductoConFiltro(
        tableProducto,
        productos,
        txtProductoBusqueda,
        lblInfoProducto,
        MenuPrincipal.Polizas.getPagina());
		tableProducto.setOnMouseClicked(
				event -> {
					Producto p = tableProducto.getSelectionModel().getSelectedItem();
					if (p != null) {
						if (productoSelected != null) {
							if (productoSelected != p) {
								seteaProducto(p);
								filtraVendedores();
							} else {
								tableProducto.getSelectionModel().clearSelection();
								productoSelected = null;
							}
						} else {
							seteaProducto(p);
							filtraVendedores();
						}
					}
				});
	}

	private void filtraVendedores(){
		List<CotizacionVendedor> vendedoresFiltrados = todosVendedores.stream().filter( vendedor -> vendedor.getProducto().getNombre().equals(productoSelected.getNombre())).collect(Collectors.toList());
		ObservableList<CotizacionVendedor> filtrados = FXCollections.observableArrayList(vendedoresFiltrados);
		cmbVendedor.setItems(filtrados);
	}

	private void seteaProducto(Producto p){
		productoSelected = p;
		if(cmbEstado.getValue()!=null){
			seteaComision(cmbEstado.getValue().getNombre());
		}
		setLabelText();
		mostrarFormulario();
	}

	private void setLabelText(){
		lblProductoValor.setText(productoSelected.getNombre());
		lblTipoProductoValor.setText(productoSelected.getTipoProducto().getNombre());
		lblCompaniaValor.setText(productoSelected.getCompania().getNombre());
	}

	private void cargaTablaCliente(){
		if(TareaAppApplication.clientes==null){
			TareaAppApplication.clientes = clienteService.getClientes();
		}
		ObservableList<Cliente> clientes = FXCollections.observableArrayList(TareaAppApplication.clientes);
		UtilCargaTablas.cargaClienteTablaConFiltro(tableCliente, clientes, txtClienteBusqueda, lblInfoCliente);
		tableCliente.setOnMouseClicked(
				event -> {
					Cliente c = tableCliente.getSelectionModel().getSelectedItem();
					if (c != null) {
						if (clienteSelected != null) {
							if (clienteSelected != c) {
								clienteSelected = c;
								lblClienteValor.setText(clienteSelected.getNombreYApellido());
								mostrarFormulario();
							} else {
								tableCliente.getSelectionModel().clearSelection();
								clienteSelected = null;
							}
						} else {
							clienteSelected = c;
							lblClienteValor.setText(clienteSelected.getNombreYApellido());
							mostrarFormulario();
						}
					}
				});
	}

	private void recargarTablaPoliza(){
		try{
			buttonDisable(true);
			tablePoliza.getColumns().clear();
			recarga = true;
			cargaTablaPoliza();
			mostrarTablaPoliza();
		}catch (Exception ex){
			errorLog(ex);
		}
	}

	private void mostrarFormulario(){
		buttonDisable(true);
		paneTablePolizas.setOpacity(0);
		paneTableProductos.setOpacity(0);
		paneTableClientes.setOpacity(0);
		new FadeInUpTransition(paneFormulario).play();
	}

	private void mostrarTablaPoliza(){
		paneFormulario.setOpacity(0);
		paneTableProductos.setOpacity(0);
		paneTableClientes.setOpacity(0);
		new FadeInUpTransition(paneTablePolizas).play();
	}

	private void mostrarTablaProducto(){
		paneTablePolizas.setOpacity(0);
		paneFormulario.setOpacity(0);
		paneTableClientes.setOpacity(0);
		new FadeInUpTransition(paneTableProductos).play();
	}

	private void mostrarTablaCliente(){
		paneTablePolizas.setOpacity(0);
		paneFormulario.setOpacity(0);
		paneTableProductos.setOpacity(0);
		new FadeInUpTransition(paneTableClientes).play();
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuPrincipal.Polizas.getPagina(), btnActualizar, btnEliminar, btnEndoso);
	}

	private void updateButton() {
		if(polizaSelected.getCompania()!=null){
			lblCompaniaValor.setText(polizaSelected.getCompania().getNombre());
		}
		if(polizaSelected.getCliente()!=null){
			lblClienteValor.setText(polizaSelected.getCliente().getNombreYApellido());
			clienteSelected = polizaSelected.getCliente();
		}
		if(polizaSelected.getNumeroPoliza()!=null){
			txtNumeroPoliza.setText(polizaSelected.getNumeroPoliza());
		}
		if(polizaSelected.getComienzo()!=null){
			cmbFechaComienzo.setValue(UtilsGeneral.getLocalDateFromDate(polizaSelected.getComienzo()));
		}
		if(polizaSelected.getVencimiento()!=null){
			cmbFechaVencimiento.setValue(
					UtilsGeneral.getLocalDateFromDate(polizaSelected.getVencimiento()));
			cmbFechaVencimiento.setDisable(polizaSelected.getEstado().getNombre().equals(ConstantesEtiquetas.ENDOSO));
		}
		if(polizaSelected.getProducto()!=null){
			lblProductoValor.setText(polizaSelected.getProducto().getNombre());
			productoSelected = polizaSelected.getProducto();
		}
		if(polizaSelected.getTipoProducto()!=null){
			lblTipoProductoValor.setText(polizaSelected.getTipoProducto().getNombre());
		}
		if(polizaSelected.getPremio()!=null){
			txtPremio.setText(polizaSelected.getPremio().toString());
		}
		if(polizaSelected.getPrima()!=null){
			txtPrima.setText(polizaSelected.getPrima().toString());
		}
		if(polizaSelected.getMoneda()!=null){
			cmbMoneda.setValue(polizaSelected.getMoneda());
		}
		if(polizaSelected.getComisionPorcentaje()!=null){
			lblComisionValor.setText(polizaSelected.getComisionPorcentaje().toString());
		}
		if(polizaSelected.getFormaPago()!=null){
			cmbFormaPago.setValue(polizaSelected.getFormaPago());
		}
		if(polizaSelected.getCuotas()!=null){
			txtCuotas.setText(polizaSelected.getCuotas().toString());
		}
		if(polizaSelected.getComienzoCuota()!=null){
			cmbComienzoCuota.setValue(UtilsGeneral.getLocalDateFromDate(polizaSelected.getComienzoCuota()));
		}
		if(polizaSelected.getFinCuota()!=null){
			cmbFinCuota.setValue(UtilsGeneral.getLocalDateFromDate(polizaSelected.getFinCuota()));
		}
		if(polizaSelected.getImporteCuota()!=null){
			lblImporteCuotaValor.setText(polizaSelected.getImporteCuota().toString());
		}
		if(polizaSelected.getCerradoPor()!=null){
			lblCerradoPorValor.setText(polizaSelected.getCerradoPor());
		}
		if(polizaSelected.getEsApp()!=null){
			chkEsApp.setSelected(polizaSelected.getEsApp());
		}
		if(polizaSelected.getEstado()!=null){
			cmbEstado.setValue(polizaSelected.getEstado());
		}
		if(polizaSelected.getObservaciones()!=null){
			txtObservaciones.setText(polizaSelected.getObservaciones());
		}
		if(polizaSelected.getVendedor()!=null){
			chkVendedor.setSelected(true);
			getVendedor(polizaSelected.getVendedor().getVendedor().getNombreYApellido());
			lblCerradoPorValor.setText(cmbVendedor.getValue().toString());
		}else{
			lblCerradoPorValor.setText(Constantes.SBN);
		}
		if(endoso){
			btnGuardar.setText(ConstantesEtiquetas.ENDOSO);
		}else{
			btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		}
	}

	private void getVendedor(String nombreApellido){
		for(CotizacionVendedor c: todosVendedores){
			if(nombreApellido.equals(c.getVendedor().getNombreYApellido())){
				cmbVendedor.setValue(c);
				break;
			}
		}
	}

	private void guardar(){
		if (validarDatos() == 0) {
			procesoGuardar();
	  	}
	}

	private void procesoGuardar(){
		try{
			if (polizaSelected == null) {
				polizaSelected = new Poliza();
			}
			boolean updateProcess = polizaSelected.getId() != null;
			polizaSelected.setCompania(productoSelected.getCompania());
			polizaSelected.setProducto(productoSelected);
			polizaSelected.setCliente(clienteSelected);
			polizaSelected.setTipoProducto(productoSelected.getTipoProducto());
			polizaSelected.setNumeroPoliza(txtNumeroPoliza.getText());
			polizaSelected.setEstado(cmbEstado.getValue());
			polizaSelected.setEsApp(chkEsApp.isSelected());

			if(!UtilsGeneral.isNullOrEmpty(txtPremio.getText())){
				polizaSelected.setPremio(Double.parseDouble(txtPremio.getText()));
			}
			if(!UtilsGeneral.isNullOrEmpty(txtPrima.getText())){
				polizaSelected.setPrima(Double.parseDouble(txtPrima.getText()));
			}
			if(!UtilsGeneral.isNullOrEmpty(lblComisionValor.getText())){
				polizaSelected.setComisionPorcentaje(Double.parseDouble(lblComisionValor.getText()));
			}
			if(!UtilsGeneral.isNullOrEmpty(txtCuotas.getText())){
				polizaSelected.setCuotas(Integer.valueOf(txtCuotas.getText()));
			}
			if(importeCuota!=null){
				polizaSelected.setImporteCuota(importeCuota.intValue());
			}
			if(!UtilsGeneral.isNullOrEmpty(lblCerradoPorValor.getText())){
				polizaSelected.setCerradoPor(lblCerradoPorValor.getText());
			}

			polizaSelected.setMoneda(cmbMoneda.getValue());
			polizaSelected.setComienzo(UtilsGeneral.getDateFromLocalDate(cmbFechaComienzo.getValue()));
			polizaSelected.setVencimiento(UtilsGeneral.getDateFromLocalDate(cmbFechaVencimiento.getValue()));
			polizaSelected.setFormaPago(cmbFormaPago.getValue());
			polizaSelected.setComienzoCuota(UtilsGeneral.getDateFromLocalDate(cmbComienzoCuota.getValue()));
			polizaSelected.setFinCuota(UtilsGeneral.getDateFromLocalDate(cmbFinCuota.getValue()));

			if (polizaSelected.getPolizaMadre() != null) {
				polizaSelected
						.getPolizaMadre()
						.setVencimiento(UtilsGeneral.getDateFromLocalDate(cmbFechaComienzo.getValue()));
				Poliza poliza = polizaSelected.getPolizaMadre();
				polizaService.savePolicy(poliza);
			}
			double comisionPorcentaje;

			if (chkVendedor.isSelected()) {
				double comisionVendedorPorcentaje;
				polizaSelected.setVendedor(cmbVendedor.getValue());
				comisionVendedorPorcentaje = valorVendedor();
				polizaSelected.setComisionVendedorPorcentaje(comisionVendedorPorcentaje);
				polizaSelected.setComisionVendedorValor( calculaPorcentaje (comisionVendedorPorcentaje,polizaSelected.getPrima()));
				comisionPorcentaje = valorProducto() - valorVendedor();
				polizaSelected.setComisionPorcentaje(comisionPorcentaje);
				polizaSelected.setComisionValor(calculaPorcentaje(comisionPorcentaje,polizaSelected.getPrima()));
			}else{
				comisionPorcentaje = valorProducto();
				polizaSelected.setComisionPorcentaje(comisionPorcentaje);
				if(comisionPorcentaje>0 && polizaSelected.getPrima()!=null){
					polizaSelected.setComisionValor(calculaPorcentaje(comisionPorcentaje,polizaSelected.getPrima()));
				}
			}
			if(!UtilsGeneral.isNullOrEmpty(txtObservaciones.getText())){
				polizaSelected.setObservaciones(txtObservaciones.getText());
			}

			polizaSelected = polizaService.savePolicy(polizaSelected);
			if (updateProcess) {
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			} else {
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTablaPoliza();
			mostrarTablaPoliza();
			cmbEstado.setDisable(false);
			cmbFechaVencimiento.setDisable(false);
		}catch(ObjectOptimisticLockingFailureException e){
			UtilsGeneral.error(Errores.POLIZA_YA_ACTUALIZADA);
		}catch(Exception ex){
			errorLog(ex);
		}
		polizaSelected = null;
	}

	private double calculaPorcentaje(double porcentaje, double valor){
		return (porcentaje/100*(valor));
	}

	private double valorProducto(){
		if(cmbEstado.getValue().getNombre().equals("Nuevo")){
			return productoSelected.getComisionNueva();
		}else{
			return productoSelected.getComisionRenovacion();
		}
	}

	private int valorVendedor(){
		if(cmbEstado.getValue().getNombre().equals("Nuevo")){
			return cmbVendedor.getValue().getComisionNueva();
		}else{
			return cmbVendedor.getValue().getComisionRenovacion();
		}
	}

	private int exception512(Poliza poliza){
		if(lblCompaniaValor.getText().equals(ConstantesEtiquetas.PORTO_SEGURO)){
			// 01/08/2022 todos los productos de porto mantienen el numero de poliza.
			if(!cmbFechaComienzo.getValue().equals(UtilsGeneral.getLocalDateFromDate(poliza.getComienzo()))
			&& !cmbFechaVencimiento.getValue().equals(UtilsGeneral.getLocalDateFromDate(poliza.getVencimiento()))){
				return ConstantesEtiquetas.EX_CORRECTO;
			}else{
				return ConstantesEtiquetas.EX_FECHA_DISTINTA;
			}
		}else{
			return ConstantesEtiquetas.EX_NUMERO_POLIZA_YA_EXISTE;
		}

	}

	private boolean exception517(){
    	if (productoSelected != null) {
      		return !productoSelected.getNombre().equals(ConstantesEtiquetas.VIAJES);
		}
		return true;
	}

	private boolean isNull(){
		if(polizaSelected!=null){
			return polizaSelected.getId() == null;
		}
		return true;
	}

	private boolean noEsEstado(String estado){
		if(cmbEstado.getValue()!=null){
			return !cmbEstado.getValue().getNombre().equals(estado);
		}else{
			return false;
		}
	}

	private int validarDatos(){
		Optional<Poliza> pol = polizas.stream().filter(poliza -> poliza.getNumeroPoliza().equals(txtNumeroPoliza.getText())).findFirst();
		int ex=0;
		if(pol.isPresent()){
			ex= exception512(pol.get());
		}
		if(noEsEstado(ConstantesEtiquetas.NOMINACION) && txtNumeroPoliza.getText().equals(ConstantesEtiquetas.VACIO)) {
			txtNumeroPoliza.requestFocus();
			return UtilsGeneral.error(Errores.NUMERO_POLIZA_FALTANTE);
		}else if(pol.isPresent() && isNull() && ex!=0){
			switch (ex){
				case 1:
					return UtilsGeneral.error(Errores.FECHAS_DISTINTAS);
				case 2:
					return UtilsGeneral.error(Errores.NUMERO_POLIZA_YA_EXISTE);
				default:
					return 0;
			}
		} else if(noEsEstado(ConstantesEtiquetas.NOMINACION) &&
										(cmbFechaVencimiento.getValue()==null ||
										cmbComienzoCuota.getValue()==null ||
										cmbFinCuota.getValue()==null)){
			cmbFechaVencimiento.requestFocus();
			return UtilsGeneral.error(Errores.FECHAS_VACIAS);
		}else if(cmbFechaVencimiento.getValue().isBefore(cmbFechaComienzo.getValue())) {
			cmbFechaVencimiento.requestFocus();
			return UtilsGeneral.error(Errores.VERIFICAR_FECHAS_VENCIMIENTO);
		} else if(noEsEstado(ConstantesEtiquetas.NOMINACION) &&
				noEsEstado(ConstantesEtiquetas.ENDOSO) &&
				cmbFechaComienzo.getValue().plusYears(1).isBefore(cmbFinCuota.getValue())) {
			cmbFechaVencimiento.requestFocus();
			return UtilsGeneral.error(Errores.VERIFICAR_FIN_CUOTA_VENCIMIENTO);
    	} else if (noEsEstado(ConstantesEtiquetas.NOMINACION) && cmbFinCuota.getValue().isBefore(cmbComienzoCuota.getValue())) {
			cmbComienzoCuota.requestFocus();
			return UtilsGeneral.error(Errores.VERIFICAR_FECHAS_CUOTAS);
		}else if(cmbEstado.getValue()==null){
			cmbEstado.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_ESTADO);
		}else if(chkVendedor.isSelected() && cmbVendedor.getValue()==null){
			cmbVendedor.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_VENDEDOR);
		}else if (noEsEstado(ConstantesEtiquetas.NOMINACION) && !UtilsGeneral.esNumero(txtCuotas.getText())) {
			txtCuotas.requestFocus();
			return UtilsGeneral.error(Errores.CUOTAS_NUMERO);
		}else if (noEsEstado(ConstantesEtiquetas.NOMINACION) && !UtilsGeneral.esNumero(txtPremio.getText())) {
			txtPremio.requestFocus();
			return UtilsGeneral.error(Errores.PREMIO_NUMERO);
		}else if (noEsEstado(ConstantesEtiquetas.NOMINACION) && !UtilsGeneral.esNumero(txtPrima.getText())) {
			txtPrima.requestFocus();
			return UtilsGeneral.error(Errores.PRIMA_NUMERO);
		}else if (productoSelected==null || clienteSelected==null) {
			txtPrima.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_PRODUCTO);
		}else if (noEsEstado(ConstantesEtiquetas.NOMINACION) && cmbMoneda.getValue()==null) {
			txtPrima.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_MONEDA);
		}else if (noEsEstado(ConstantesEtiquetas.NOMINACION) &&
				noEsEstado(ConstantesEtiquetas.ENDOSO) &&
				!lblCompaniaValor.getText().equals(ConstantesEtiquetas.MAPFRE) &&
				(Double.parseDouble(txtPremio.getText()) < Double.parseDouble(txtPrima.getText()))) {
			txtPremio.requestFocus();
			return UtilsGeneral.error(Errores.PREMIO_MAYOR_PRIMA);
		}else if (noEsEstado(ConstantesEtiquetas.NOMINACION) && exception517() && cmbComienzoCuota.getValue().isBefore(cmbFechaComienzo.getValue())) {
			cmbComienzoCuota.requestFocus();
			return UtilsGeneral.error(Errores.COMIENZO_CUOTA_DESPUES_FECHA_COMIENZO);
		}else{
			return 0;
		}
	}

	private void clear() {
		txtNumeroPoliza.setText(ConstantesEtiquetas.VACIO);
		cmbFechaComienzo.setValue(null);
		cmbFechaVencimiento.setValue(null);
		txtPremio.setText(ConstantesEtiquetas.VACIO);
		txtPrima.setText(ConstantesEtiquetas.VACIO);
		cmbMoneda.setValue(null);
		cmbFormaPago.setValue(null);
		txtCuotas.setText(ConstantesEtiquetas.VACIO);
		cmbComienzoCuota.setValue(null);
		cmbFinCuota.setValue(null);
		lblImporteCuotaValor.setText(ConstantesEtiquetas.VACIO);
		chkEsApp.setSelected(false);

		lblCompaniaValor.setText(ConstantesEtiquetas.VACIO);
		lblProductoValor.setText(ConstantesEtiquetas.VACIO);
		lblTipoProductoValor.setText(ConstantesEtiquetas.VACIO);
		lblClienteValor.setText(ConstantesEtiquetas.VACIO);
		lblComisionValor.setText(ConstantesEtiquetas.VACIO);
		txtObservaciones.setText(ConstantesEtiquetas.VACIO);
		productoSelected = null;
		clienteSelected = null;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}


}
