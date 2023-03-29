package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.*;
import com.software.tareasApp.domain.service.DTO.PolizaDTO;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.enums.TipoGrafica;
import com.software.tareasApp.persistence.model.*;
import com.software.tareasApp.utils.ExportarExcel;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.Constantes;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
public class ReportesController implements Initializable {

	@FXML
	private AnchorPane paneCliente;

	@FXML
	private AnchorPane panePoliza;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private AnchorPane paneTableProducto;

	@FXML
	private AnchorPane paneReportePolizasDTO;

	@FXML
	private ProgressBar bar;

	@FXML
	private ComboBox<String> cmbReportes;

	@FXML
	private ComboBox<Vendedor> cmbVendedores;

	@FXML
	private TableView<Cliente> tableCliente;

	@FXML
	private TextField txtClienteBusqueda;

	@FXML
	private Label lblInfoCliente;

	@FXML
	private TableView<Poliza> tablePoliza;

	@FXML
	private TextField txtPolizaBusqueda;

	@FXML
	private Label lblInfoPoliza;

	@FXML
	private TableView<Producto> tableProducto;

	@FXML
	private TextField txtProductoBusqueda;

	@FXML
	private Label lblInfoTableProducto;

	@FXML
	private ComboBox<Compania> cmbCompania;

	@FXML
	private ComboBox<String> cmbMeses;

	@FXML
	private DatePicker cmbDesde;

	@FXML
	private DatePicker cmbHasta;

	@FXML
	private CheckBox chkEsApp;

	@FXML
	private CheckBox chkProximoMes;

	@FXML
	private ComboBox<EstadoPoliza> cmbEstadoPoliza;

	@FXML
	private Button btnProducto;

	@FXML
	private Button btnBackPolizaDTO;

	@FXML
	private Button btnBackPoliza;

	@FXML
	private Button btnBackProducto;

	@FXML
	private Button btnTodosClientes;

	@FXML
	private Button btnTodosProductos;

	@FXML
	private Button btnBackCliente;

	@FXML
	private Button btnExportarExcelCliente;

	@FXML
	private Button btnExportarExcelPolizaDTO;

	@FXML
	private Button btnExportarExcelPoliza;

	@FXML
	private RadioButton radioProducto;

	@FXML
	private RadioButton radioTipoProducto;

	@FXML
	private Label lblProducto;

	@FXML
	private Label lblCliente;

	@FXML
	private Button btnCliente;

	@FXML
	private Button btnGenerarReporte;

	@FXML
	private PieChart chartPrimaPremio;

	@FXML
	private Label lblInfoPieChart;

	@FXML
	private TableView<PolizaDTO> tablePolizaDTO;

	@FXML
	private TextField txtCotizacion;

	@FXML
	private Text txtInfo;

	@FXML
	private Label lblInfoPolizaDTO;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	private Cliente clienteSelected;
	private Producto productoSelected;
	private ObservableList<Cliente> clientes;
	private ObservableList<PolizaDTO> premioReportes;
	List<PolizaDTO> premios;
	List<Poliza> polizas;

	@Autowired
	public ReportesController(ClienteService clienteService, EstadoPolizaService estadoPolizaService,
                              PolizaService polizaService, CompaniaService companiaService,
                              ProductoService productoService, VendedorService vendedorService,MonedaService monedaService){
		this.clienteService = clienteService;
		this.estadoPolizaService = estadoPolizaService;
		this.polizaService = polizaService;
		this.companiaService = companiaService;
		this.productoService = productoService;
		this.vendedorService = vendedorService;
		this.monedaService = monedaService;
	}

	private final MonedaService monedaService;
	private final CompaniaService companiaService;
	private final EstadoPolizaService estadoPolizaService;
	private final ClienteService clienteService;
	private final PolizaService polizaService;
	private final ProductoService productoService;
	private final VendedorService vendedorService;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			setvisible();
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void task() {
		Task longTask = UtilsGeneral.task();
		longTask.setOnSucceeded((EventHandler<WorkerStateEvent>) this::handle);
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void cargarTablas(){
		cargaTablaClientes();
	}

	private void cargaTablaClientes(){
		UtilCargaTablas.cargaClienteTablaConFiltro(tableCliente, FXCollections.observableArrayList(new ArrayList<>()), txtClienteBusqueda, lblInfoCliente);
	}

	private void btnOnAction(){
		btnBackPolizasTablaOnAction();
		btnProductoOnAction();
		btnClienteOnAction();
		btnBackOnAction();
		btnExportarExcelPolizaOnAction();
		btnExportarExcelPolizaDTOOnAction();
		btnExportarExcelClienteOnAction();
		btnTodosOnAction();
	}

	private void btnTodosOnAction(){
		btnTodosClientes.setOnAction((event) -> {
			lblCliente.setText("Todos los clientes");
			clienteSelected = null;
			tableCliente.getSelectionModel().clearSelection();
			mostrarAnchorPane();
		});
		btnTodosProductos.setOnAction((event) -> {
			lblProducto.setText("Todos los productos");
			productoSelected = null;
			tableProducto.getSelectionModel().clearSelection();
			mostrarAnchorPane();
		});
	}

	private void btnBackPolizasTablaOnAction(){
		btnGenerarReporte.setOnAction((event) -> seleccionMode());
	}

	private void btnBackOnAction(){
		btnBackPolizaDTO.setOnAction((event) -> mostrarAnchorPane());
		btnBackPoliza.setOnAction((event) -> mostrarAnchorPane());
		btnBackProducto.setOnAction((event) -> mostrarAnchorPane());
		btnBackCliente.setOnAction((event) -> {
			mostrarAnchorPane();
			btnExportarExcelCliente.setVisible(false);
		});
	}

	private void btnProductoOnAction(){
		btnProducto.setOnAction((event) -> cargaTablaProducto());
	}

	private void btnClienteOnAction(){
		btnCliente.setOnAction((event) -> cargaTablaCliente());
	}

	private void btnExportarExcelPolizaOnAction(){
		btnExportarExcelPoliza.setOnAction((event) -> crearExcelPoliza());
	}

	private void btnExportarExcelPolizaDTOOnAction(){
		btnExportarExcelPolizaDTO.setOnAction((event) -> crearExcelPolizaDTO());
	}

	private void btnExportarExcelClienteOnAction(){
		btnExportarExcelCliente.setOnAction((event) -> crearExcelCliente());
	}

	private void cargaCombos() {
		cargaComboReportes();
		cargaComboCompania();
		cargarComboEstado();
		cargarComboVendedores();
		cargaComboMeses();
		chkProximoMesListener();
		cmbReportesListener();
		radioProductoListener();
	}

	private void cargarComboVendedores(){
		List<Vendedor> vendedores = vendedorService.getVendedores();
		Vendedor vendedor = new Vendedor();
		vendedor.setId(-1);
		vendedor.setNombre("Todos");
		vendedor.setApellido("");
		vendedores.add(vendedor);
		cmbVendedores.setItems(FXCollections.observableArrayList(vendedores));
	}

	private void chkProximoMesListener(){
		chkProximoMes.selectedProperty().addListener((observable, oldValue, newValue) -> cmbMeses.setDisable(newValue));
	}

	private void cmbReportesListener(){
		cmbReportes.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> clear());
	}

	private void radioProductoListener(){
		radioProducto.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue && productoSelected!=null){
				lblProducto.setText(productoSelected.getNombre());
			}else if(!newValue && productoSelected!=null){
				lblProducto.setText(productoSelected.getTipoProducto().getNombre());
			}
		});
	}

	private void cargaComboReportes(){
		cmbReportes.setItems(FXCollections.observableArrayList(Constantes.LISTA_REPORTES));
		cmbReportes.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> checkReportesValue(newValue));
	}

	private void checkReportesValue(String value){
		switch(value){
			case "Poliza":
				cumple(false);
				displayFechas(true);
				cmbCompania.setVisible(true);
				cmbEstadoPoliza.setVisible(true);
				cmbVendedores.setVisible(false);
				chkEsApp.setVisible(true);
				displayProductoTipoProducto(false);
				displayCliente(true);
				txtCotizacion.setVisible(false);
				txtInfo.setVisible(false);
				break;
			case "Vencimiento Poliza":
				cumple(false);
				displayFechas(true);
				cmbCompania.setVisible(true);
				cmbEstadoPoliza.setVisible(false);
				cmbVendedores.setVisible(false);
				setChkEsAppFalse();
				displayProductoTipoProducto(true);
				setDisableRadioButtons(false);
				displayCliente(false);
				txtCotizacion.setVisible(false);
				txtInfo.setVisible(false);
				break;
			case "Cumpleaños Clientes":
				btnTodosClientes.setVisible(false);
				cumple(true);
				displayFechas(false);
				cmbCompania.setVisible(false);
				cmbEstadoPoliza.setVisible(false);
				cmbVendedores.setVisible(false);
				setChkEsAppFalse();
				displayProductoTipoProducto(false);
				displayCliente(false);
				txtCotizacion.setVisible(false);
				txtInfo.setVisible(false);
				break;
			case "Total Prima":
			case "Total Premio":
				cumple(false);
				displayFechas(true);
				cmbCompania.setVisible(true);
				cmbEstadoPoliza.setVisible(false);
				cmbVendedores.setVisible(true);
				setChkEsAppFalse();
				displayProductoTipoProducto(true);
				setDisableRadioButtons(false);
				displayCliente(true);
				txtCotizacion.setVisible(false);
				txtInfo.setVisible(false);
				break;
			case "Ventas por producto":
				cumple(false);
				displayFechas(true);
				cmbCompania.setVisible(false);
				cmbEstadoPoliza.setVisible(false);
				cmbVendedores.setVisible(false);
				setChkEsAppFalse();
				displayProductoTipoProducto(true);
				setDisableRadioButtons(true);
				radioTipoProducto.setSelected(true);
				displayCliente(false);
				txtCotizacion.setVisible(false);
				txtInfo.setVisible(false);
				break;
			case "Producto Prima Total":
			case "Comisiones":
				cumple(false);
				displayFechas(true);
				cmbCompania.setVisible(true);
				cmbEstadoPoliza.setVisible(false);
				cmbVendedores.setVisible(false);
				setChkEsAppFalse();
				displayProductoTipoProducto(false);
				displayCliente(false);
				txtCotizacion.setVisible(true);
				txtInfo.setVisible(true);
				break;
			default:
		}
	}

	private void setChkEsAppFalse(){
		chkEsApp.setVisible(false);
		chkEsApp.setSelected(false);
	}

	private void setDisableRadioButtons(boolean value){
		radioTipoProducto.setDisable(value);
		radioProducto.setDisable(value);
	}

	private void clear(){
		cmbMeses.setValue(null);
		cmbCompania.setValue(null);
		cmbEstadoPoliza.setValue(null);
		cmbVendedores.setValue(null);
		lblProducto.setText("Todos los productos");
		productoSelected = null;
		clienteSelected = null;
		lblCliente.setText("Todos los clientes");
	}

	private void setvisible(){
		btnExportarExcelCliente.setVisible(false);
		cumple(false);
		displayFechas(false);
		cmbCompania.setVisible(false);
		cmbEstadoPoliza.setVisible(false);
		cmbVendedores.setVisible(false);
		chkEsApp.setVisible(false);
		displayProductoTipoProducto(false);
		displayCliente(false);
		cmbVendedores.setVisible(false);
		txtCotizacion.setVisible(false);
		txtInfo.setVisible(false);
	}

	private void cumple(boolean value){
		chkProximoMes.setVisible(value);
		cmbMeses.setVisible(value);
	}

	private void displayFechas(boolean value){
		cmbDesde.setVisible(value);
		cmbHasta.setVisible(value);
	}

	private void displayCliente(boolean value){
		lblCliente.setVisible(value);
		btnCliente.setVisible(value);
	}

	private void displayProductoTipoProducto(boolean value){
		lblProducto.setVisible(value);
		btnProducto.setVisible(value);
		radioProducto.setVisible(value);
		radioTipoProducto.setVisible(value);
	}

	private void cargarComboEstado() {
		List<EstadoPoliza> estados = new ArrayList<>();
		estados.add(new EstadoPoliza("Todos"));
		estados.addAll(estadoPolizaService.getEstadoPolizas());
		ObservableList<EstadoPoliza> optionsList = FXCollections.observableArrayList(estados);
		cmbEstadoPoliza.setItems(optionsList);
	}

	private void cargaComboCompania(){
		List<Compania> companias = new ArrayList<>();
		companias.add(new Compania("Todas"));
		companias.addAll(companiaService.getCompanias());
		ObservableList<Compania> options = FXCollections.observableArrayList(companias);
		cmbCompania.setItems(options);
	}

	private void cargaComboMeses(){
		cmbMeses.setItems(null);
		ObservableList<String> optionsList = FXCollections.observableArrayList(Constantes.LISTA_MESES);
		cmbMeses.setItems(optionsList);
	}

	private void seleccionMode(){
		if(cmbReportes.getValue()!=null){
			switch(cmbReportes.getValue()){
				case "Poliza":
					cargaTablaPoliza();
					break;
				case "Vencimiento Poliza":
					cargaTablaPolizaVencimiento();
					break;
				case "Cumpleaños Clientes":
					if(chkProximoMes.isSelected()){
						btnExportarExcelCliente.setVisible(true);
						cargaTablaClienteReporte(UtilsGeneral.getFirstDayNextMonth(), UtilsGeneral.getLastDayNextMonth(), UtilsGeneral.getLastDayNextMonth().getMonth());
						mostrarClientePane();
					}else{
						if(cmbMeses.getValue()!=null){
							btnExportarExcelCliente.setVisible(true);
							setClientesCumpleanios(cmbMeses.getValue());
							mostrarClientePane();
						}else{
							btnExportarExcelCliente.setVisible(false);
							UtilsGeneral.error(Errores.SELECCIONE_OPCION);
						}
					}
					break;
				case "Total Premio":
				case "Total Prima":
					cargaTablaPolizaPrimaPremio();
					break;
				case "Producto Prima Total":
					cargaTablaProductoPrimaTotal();
					break;
				case "Ventas por producto":
					getVentasPorTipoProducto();
					break;
				case "Comisiones":
					if(UtilsSeguridad.traePermisoComision("Reportes")){
						cargaTablacomisiones();
					}else{
						UtilsGeneral.error(Errores.SIN_PERMISOS);
					}
					break;
				default:
					UtilsGeneral.error(Errores.NO_IMPLEMENTADO);
			}
		}else{
			UtilsGeneral.error(Errores.ELEGIR_REPORTE);
		}
	}

	private void cargaTablaPoliza(){
		if(checkCmbFechas()){
			Date desde = UtilsGeneral.getDateFromLocalDate(cmbDesde.getValue());
			Date hasta = UtilsGeneral.getDateFromLocalDate(cmbHasta.getValue());
			polizas = polizaService.getPolizasByFecha(desde, hasta);
			filtrosPoliza();
			cargaTablaPolizas();
			mostrarPolizaPane();
		}
	}

	private void cargaTablaPolizaVencimiento(){
		if(checkCmbFechas()){
			Date desde = UtilsGeneral.getDateFromLocalDate(cmbDesde.getValue());
			Date hasta = UtilsGeneral.getDateFromLocalDate(cmbHasta.getValue());
			polizas = polizaService.getPolizasVencimientoByFecha(desde, hasta);
			filtrosPoliza();
			cargaTablaPolizas();
			mostrarPolizaPane();
		}
	}

	private void setClientesCumpleanios(String date){
		tableCliente.getColumns().clear();
		clientes = FXCollections.observableArrayList(clienteService.getAniversary(
				UtilsGeneral.getFirstDayMonth(UtilsGeneral.getMesInteger(date)),
				UtilsGeneral.getLastDayMonth(UtilsGeneral.getMesInteger(date)),
				UtilsGeneral.getMesInteger(date)
		));
		UtilCargaTablas.cargaClienteTablaConFiltro(tableCliente, clientes, txtClienteBusqueda, lblInfoCliente);
	}

	private void cargaTablaProductoPrimaTotal(){
		if(checkCmbFechas()){
			Date desde = UtilsGeneral.getDateFromLocalDate(cmbDesde.getValue());
			Date hasta = UtilsGeneral.getDateFromLocalDate(cmbHasta.getValue());
			Moneda monedaDolar = monedaService.getMonedaById(2);
			if(UtilsGeneral.esNumero(txtCotizacion.getText())){
				premios = polizaService.getSUMPrimaProductos(desde, hasta, monedaDolar, Double.valueOf(txtCotizacion.getText()));
				filtros();
				cargaTablaProductosPrimaTotal();
				cargaGrafica(TipoGrafica.PRODUCTO_PRIMA_TOTAL);
				mostrarPaneReportePolizasPrimaPremio();
			}else {
				UtilsGeneral.error(Errores.FALTA_COTIZACION);
			}

		}
	}

	private void cargaTablacomisiones(){
		if(checkCmbFechas()){
			Date desde = UtilsGeneral.getDateFromLocalDate(cmbDesde.getValue());
			Date hasta = UtilsGeneral.getDateFromLocalDate(cmbHasta.getValue());
			Moneda monedaDolar = monedaService.getMonedaById(2);
			if(UtilsGeneral.esNumero(txtCotizacion.getText())){
				premios = polizaService.getPolizasComisionesByFecha(desde, hasta, monedaDolar, Double.valueOf(txtCotizacion.getText()));
				filtros();
				cargaTablaComisiones();
				mostrarPaneReportePolizasPrimaPremio();
				cargaGrafica(TipoGrafica.COMISIONES);
				lblInfoPolizaDTO.setText("Registros: " + premios.size());
			}else {
				UtilsGeneral.error(Errores.FALTA_COTIZACION);
			}
		}
	}

	private void getVentasPorTipoProducto(){
		if(checkCmbFechas()){
			Date desde = UtilsGeneral.getDateFromLocalDate(cmbDesde.getValue());
			Date hasta = UtilsGeneral.getDateFromLocalDate(cmbHasta.getValue());
			premios = polizaService.getVentasPorTipoProducto(desde, hasta);
			filtros();
			cargaTablaVentasPorTipoProducto();
			mostrarPaneReportePolizasPrimaPremio();
			cargaGrafica(TipoGrafica.TOTAL_VENDIDO_PRODUCTO);
		}
	}

	private boolean checkCmbFechas(){
		if(cmbHasta.getValue()!=null && cmbDesde.getValue()!=null){
			if(cmbDesde.getValue().isAfter(cmbHasta.getValue())){
				UtilsGeneral.error(Errores.FECHA_DESDE_MENOR_FECHA_HASTA);
				return false;
			}else{
				return true;
			}
		}else{
			UtilsGeneral.error(Errores.FALTA_FECHA);
			return false;
		}
	}

	private void cargaTablaPolizaPrimaPremio(){
		if(checkCmbFechas()){
			Date desde = UtilsGeneral.getDateFromLocalDate(cmbDesde.getValue());
			Date hasta = UtilsGeneral.getDateFromLocalDate(cmbHasta.getValue());
			if(cmbReportes.getValue().equals("Total Premio")){
				premios = polizaService.getTotalPremioByFechasGroupByProductos(desde, hasta);
				filtros();
				cargaTablaPremio();
				cargaGrafica(TipoGrafica.TOTAL_PREMIO);
			}else{
				premios = polizaService.getTotalPrimaByFechasGroupByProductos(desde, hasta);
				filtros();
				cargaTablaPrima();
				cargaGrafica(TipoGrafica.TOTAL_PRIMA);
			}
			//cambio
			tablePolizaDTO.setItems(FXCollections.observableArrayList(premios));
			mostrarPaneReportePolizasPrimaPremio();
		}
	}

	private void filtros(){
		//filters Compania
		if(cmbCompania.getValue()!=null){
			if(!cmbCompania.getValue().getNombre().equals("Todas")){
				premios = premios.stream().filter(premio ->
						premio.getCompania().getNombre().equals(cmbCompania.getValue().getNombre())).collect(Collectors.toList());
			}
		}
		//filters Producto y Tipo Producto
		if(productoSelected!=null && radioProducto.isSelected()){
			premios = premios.stream().filter(premio ->
					premio.getProducto().getNombre().equals(productoSelected.getNombre())).collect(Collectors.toList());
		}else if(productoSelected!=null){
			premios = premios.stream().filter(premio ->
					premio.getTipoProducto().getNombre().equals(productoSelected.getTipoProducto().getNombre())).collect(Collectors.toList());
		}
		//filter clientes
		if(clienteSelected!=null){
			premios = premios.stream().filter(premio ->
					premio.getCliente().getCedulaIdentidad().equals(clienteSelected.getCedulaIdentidad())).collect(Collectors.toList());
		}
		//filter vendedores
		if(cmbVendedores.getValue()!=null){
			if(!cmbVendedores.getValue().getNombre().equals("Todos")){
				premios = premios.stream().filter(premio ->
					premio.getVendedor()!=null &&
					premio.getVendedor().getNombreYApellido().equals(cmbVendedores.getValue().getNombreYApellido())).collect(Collectors.toList());
			}
		}
	}

	private void filtrosPoliza(){
		//filters Compania
		if(cmbCompania.getValue()!=null){
			if(!cmbCompania.getValue().getNombre().equals("Todas")){
				polizas = polizas.stream().filter(poliza ->
						poliza.getCompania().getNombre().equals(cmbCompania.getValue().getNombre())).collect(Collectors.toList());
			}
		}
		//filters Producto y Tipo Producto
		if(productoSelected!=null && radioProducto.isSelected()){
			polizas = polizas.stream().filter(poliza ->
					poliza.getProducto().getTipoProducto().getNombre().equals(productoSelected.getTipoProducto().getNombre())).collect(Collectors.toList());
		}else if(productoSelected!=null){
			polizas = polizas.stream().filter(poliza ->
					poliza.getProducto().getNombre().equals(productoSelected.getNombre())).collect(Collectors.toList());
		}
		//filter clientes
		if(clienteSelected!=null){
			polizas = polizas.stream().filter(poliza ->
					poliza.getCliente().getCedulaIdentidad().equals(clienteSelected.getCedulaIdentidad())).collect(Collectors.toList());
		}
		//filter vendedores
		if(cmbVendedores.getValue()!=null){
			if(!cmbVendedores.getValue().getNombre().equals("Todos")){
				polizas = polizas.stream().filter(poliza ->
						poliza.getVendedor()!=null &&
								poliza.getVendedor().getVendedor().getNombreYApellido().equals(cmbVendedores.getValue().getNombreYApellido())).collect(Collectors.toList());
			}
		}
		if(cmbEstadoPoliza.getValue()!=null){
			polizas = polizas.stream().filter(poliza ->
					poliza.getEstado().getNombre().equals(cmbEstadoPoliza.getValue().getNombre())).collect(Collectors.toList());
		}
		if(chkEsApp.isSelected()){
			polizas = polizas.stream().filter(Poliza::getEsApp).collect(Collectors.toList());
		}
	}

	private void cargaTablaPremio(){
		if(tablePolizaDTO.getColumns()!=null){
			tablePolizaDTO.getColumns().clear();
		}
		UtilCargaTablas.cargaTablaPremioReporte(tablePolizaDTO, premioReportes);
	}

	private void cargaTablaPrima(){
		if(tablePolizaDTO.getColumns()!=null){
			tablePolizaDTO.getColumns().clear();
		}
		UtilCargaTablas.cargaTablaPrimaReporte(tablePolizaDTO, premioReportes);
	}

	private void cargaTablaProductosPrimaTotal(){
		if(tablePolizaDTO.getColumns()!=null){
			tablePolizaDTO.getColumns().clear();
		}
		UtilCargaTablas.cargaTablaProductosPrimaTotal(tablePolizaDTO, FXCollections.observableArrayList(premios));
	}

	private void cargaTablaVentasPorTipoProducto(){
		if(tablePolizaDTO.getColumns()!=null){
			tablePolizaDTO.getColumns().clear();
		}
		UtilCargaTablas.cargaTablaVentasPorTipoProducto(tablePolizaDTO, FXCollections.observableArrayList(premios));
	}

	private void cargaTablaComisiones(){
		if(tablePolizaDTO.getColumns()!=null){
			tablePolizaDTO.getColumns().clear();
		}
		UtilCargaTablas.cargaTablaComision(tablePolizaDTO, FXCollections.observableArrayList(premios));
	}

	private void cargaTablaPolizas(){
		if(tablePoliza.getColumns()!=null){
			tablePoliza.getColumns().clear();
		}
    UtilCargaTablas.cargaTablaPoliza(
        tablePoliza,
        FXCollections.observableArrayList(polizas),
        txtPolizaBusqueda,
        lblInfoPoliza,
        MenuPrincipal.Reportes.getPagina());
	}

	private void cargaTablaClienteReporte(Date fechaDesde, Date fechaHasta, int mes){
		tableCliente.getColumns().clear();
		clientes = FXCollections.observableArrayList(clienteService.getAniversary(fechaDesde.getDate(), fechaHasta.getDate(), mes));
		txtClienteBusqueda.setText("");
		UtilCargaTablas.cargaClienteTablaConFiltro(tableCliente, clientes, txtClienteBusqueda, lblInfoCliente);
	}

	private void cargaTablaCliente() {
		btnTodosClientes.setVisible(true);
		mostrarClientePane();
		if(tableCliente.getColumns()!=null){
			tableCliente.getColumns().clear();
		}
		clientes = FXCollections.observableArrayList(clienteService.getClientes());
		UtilCargaTablas.cargaClienteTablaConFiltro(tableCliente, clientes, txtClienteBusqueda,lblInfoCliente);
		tableCliente.setOnMouseClicked(
				event -> {
					Cliente c = tableCliente.getSelectionModel().getSelectedItem();
					if (c != null) {
						if (clienteSelected != null) {
							if (!clienteSelected.getCedulaIdentidad().equals(c.getCedulaIdentidad())) {
								clienteSelected = c;
								lblCliente.setText(clienteSelected.getNombreYApellido());
							} else {
								tableCliente.getSelectionModel().clearSelection();
								clienteSelected = null;
								lblCliente.setText("Todos los clientes");
							}
						} else {
							clienteSelected = c;
							lblCliente.setText(clienteSelected.getNombreYApellido());
						}
						mostrarAnchorPane();
					}
				});
	}

	private void cargaTablaProducto() {
		mostrarProductoPane();
		if(tableProducto.getColumns()!=null){
			tableProducto.getColumns().clear();
		}
		ObservableList<Producto> productos = FXCollections.observableArrayList(productoService.getProductos());
    UtilCargaTablas.cargaTablaProductoConFiltro(
        tableProducto,
        productos,
        txtProductoBusqueda,
        lblInfoTableProducto,
        MenuPrincipal.Reportes.getPagina());
		tableProducto.setOnMouseClicked(
				event -> {
					Producto p = tableProducto.getSelectionModel().getSelectedItem();
					if (p != null) {
						if (clienteSelected != null) {
							if (productoSelected != p) {
								productoSelected = p;
								if(radioProducto.isSelected()){
									lblProducto.setText(productoSelected.getNombre());
									mostrarAnchorPane();
								}else{
									lblProducto.setText(productoSelected.getTipoProducto().getNombre());
									mostrarAnchorPane();
								}
							} else {
								tableProducto.getSelectionModel().clearSelection();
								productoSelected = null;
								lblProducto.setText("Todos los productos");
								mostrarAnchorPane();
							}
						} else {
							productoSelected = p;
							if(radioProducto.isSelected()){
								lblProducto.setText(productoSelected.getNombre());
								mostrarAnchorPane();
							}else{
								lblProducto.setText(productoSelected.getTipoProducto().getNombre());
								mostrarAnchorPane();
							}
						}
					}

				});
	}

	private void mostrarClientePane(){
		panePoliza.setOpacity(0);
		paneTableProducto.setOpacity(0);
		paneReportePolizasDTO.setOpacity(0);
		anchorPane.setOpacity(0);
		new FadeInUpTransition(paneCliente).play();
	}

	private void mostrarAnchorPane(){
		panePoliza.setOpacity(0);
		paneTableProducto.setOpacity(0);
		paneReportePolizasDTO.setOpacity(0);
		paneCliente.setOpacity(0);
		new FadeInUpTransition(anchorPane).play();
	}

	private void mostrarPaneReportePolizasPrimaPremio(){
		panePoliza.setOpacity(0);
		paneTableProducto.setOpacity(0);
		paneCliente.setOpacity(0);
		anchorPane.setOpacity(0);
		new FadeInUpTransition(paneReportePolizasDTO).play();
	}

	private void mostrarProductoPane(){
		panePoliza.setOpacity(0);
		paneReportePolizasDTO.setOpacity(0);
		paneCliente.setOpacity(0);
		anchorPane.setOpacity(0);
		new FadeInUpTransition(paneTableProducto).play();
	}

	private void mostrarPolizaPane(){
		paneTableProducto.setOpacity(0);
		paneReportePolizasDTO.setOpacity(0);
		paneCliente.setOpacity(0);
		anchorPane.setOpacity(0);
		new FadeInUpTransition(panePoliza).play();
	}

	private void crearExcelCliente(){
		try {
			File file = openFile();
			if (file != null) {
				LocalDate d = LocalDate.now();
				ExportarExcel.crearExcelCliente(tableCliente, file.getAbsolutePath(), d, d, cmbReportes.getValue());
				UtilsGeneral.correct("Se creo el archivo " + file.getName());
			} else {
				UtilsGeneral.error(Errores.ELEGIR_ARCHIVO);
			}
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void crearExcelPolizaDTO(){
		try {
			File file = openFile();
			if (file != null) {
				ExportarExcel.crearExcelPolizaDTO(tablePolizaDTO, file.getAbsolutePath(), cmbDesde.getValue(), cmbHasta.getValue(), cmbReportes.getValue());
				UtilsGeneral.correct("Se creo el archivo " + file.getName());
			} else {
				UtilsGeneral.error(Errores.ELEGIR_ARCHIVO);
			}
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void crearExcelPoliza(){
		try {
			File file = openFile();
			if (file != null) {
				ExportarExcel.crearExcelPoliza(tablePoliza, file.getAbsolutePath(), cmbDesde.getValue(), cmbHasta.getValue(), cmbReportes.getValue());
				UtilsGeneral.correct("Se creo el archivo " + file.getName());
			} else {
				UtilsGeneral.error(Errores.ELEGIR_ARCHIVO);
			}
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private File openFile(){
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(Constantes.EXCEL, Constantes.EXTENSION_EXCEL));
		Stage stage = (Stage) txtClienteBusqueda.getScene().getWindow();
		return fileChooser.showSaveDialog(stage);
	}

	private void handle(WorkerStateEvent t) {
		try {
			txtInfo.setText(Constantes.PESIFICADO);
			cargaCombos();
			btnOnAction();
			cargarTablas();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void installTooltip(PieChart.Data d) {
		String msg = String.format("%s : %s", d.getName(), d.getPieValue());
		Tooltip tt = new Tooltip(msg);
		tt.setStyle("-fx-background-color: gray; -fx-text-fill: whitesmoke;");

		Tooltip.install(d.getNode(), tt);
	}

	private void cargaGrafica(TipoGrafica tipoGrafica){
		lblInfoPieChart.setText(ConstantesEtiquetas.VACIO);
		List<PieChart.Data> dataList = new ArrayList<>();
		double total;
		switch (tipoGrafica){
			case TOTAL_PREMIO:
				total = premios.stream()
						.map(PolizaDTO::getPremio)
						.reduce(0, Integer::sum);
				premios.forEach(premio -> dataList.add(new PieChart.Data(premio.getProducto().getNombre(), premio.getPremio())));
				break;
			case TOTAL_PRIMA:
				total = premios.stream()
						.map(PolizaDTO::getPrima)
						.reduce(0, Integer::sum);
				premios.forEach(premio -> dataList.add(new PieChart.Data(premio.getProducto().getNombre(), premio.getPrima())));
				break;
			case PRODUCTO_PRIMA_TOTAL:
				total = premios.stream()
						.map(PolizaDTO::getTotal)
						.reduce(0, Integer::sum);
				premios.forEach(premio -> dataList.add(new PieChart.Data(premio.getTipoProducto().getNombre(), premio.getTotal())));
				break;
			case TOTAL_VENDIDO_PRODUCTO:
				total = premios.stream()
						.map(PolizaDTO::getTotal)
						.reduce(0, Integer::sum);
				premios.forEach(premio -> dataList.add(new PieChart.Data(premio.getTipoProducto().getNombre() + " - " + premio.getEstado().getNombre(), premio.getTotal())));
				break;
			case COMISIONES:
				total = premios.stream()
						.map(PolizaDTO::getComisionValor)
						.filter(comisionValor -> comisionValor !=null)
						.reduce(0, Integer::sum);
				Map<Compania, List<PolizaDTO>> mapPolizaDTO = premios.stream().collect(groupingBy(PolizaDTO::getCompania));
				mapPolizaDTO.forEach((k,v) -> {
					int subTotal = v.stream()
							.map(PolizaDTO::getComisionValor)
							.filter(Objects::nonNull)
							.reduce(0, Integer::sum);
					dataList.add(new PieChart.Data(k.getNombre(), subTotal));
				});
				break;
			default:
				total = 0;
				break;
		}
		procesoChart(dataList, total);
	}

	private void procesoChart(List<PieChart.Data> dataList, double total){
		ObservableList<PieChart.Data> lista = FXCollections.observableArrayList(dataList);
		chartPrimaPremio.setData(lista);
		chartPrimaPremio.getData().forEach(this::installTooltip);
		clickChart(total);
	}

	private void clickChart(double total){
		for (PieChart.Data data : chartPrimaPremio.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
				int num = (int) data.getPieValue();
				double porc = (((double) num * (double) 100) / total);
				DecimalFormat df = new DecimalFormat("#.#");
				df.setRoundingMode(RoundingMode.CEILING);
				String porcent = df.format(porc);
				lblInfoPieChart.setText(data.getName() + ": " + num + " (" + porcent + " % aprox.)");
			});
		}
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
