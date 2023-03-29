package com.software.tareasApp.utils;

import com.software.tareasApp.domain.service.DTO.PolizaDTO;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.persistence.model.*;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Date;


public class UtilCargaTablas {

    private final static double C170 = 170;
    private final static double C150 = 150;
    private final static double C120 = 120;
    private final static double C100 = 100;

    public static void cargaBancoTabla(TableView<Banco> tableData, ObservableList<Banco> bancos){
        tableData.getColumns().addAll(getNombre(), getDescripcion());
        tableData.setItems(bancos);
    }

    public static void cargaClienteTablaConFiltro(TableView<Cliente> tableData, ObservableList<Cliente> clientes, TextField txtClienteBusqueda, Label lblInfo){
        tableData.getColumns().addAll(getNombre(), getApellido(), getDireccion(), getCiudad(), getDepartamento(),
                getFechaNacimiento(), getTelefono(), getCelular(),getEmail(), getCedulaIdentidad(), getLibretaPropiedad(),
                getTipoProducto(), getRecomendadoPor(), getFechaComienzoToString(), getRut(), getObservaciones(), getActivo());
        tableData.setItems(clientes);

        if(clientes==null){
            clientes = FXCollections.observableArrayList(new ArrayList<>());
        }

        FilteredList<Cliente> filteredData = new FilteredList<>(clientes, b -> true);

        txtClienteBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(cliente -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (checkNombreApellido(lowerCaseFilter, cliente.getNombre(), cliente.getApellido(), cliente.getNombreYApellido())) {
                            return true;
                        } else if (cliente.getCelular().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (cliente.getTelefono().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (cliente.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (cliente.getCedulaIdentidad().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (cliente.getLibretaPropiedad()!=null && cliente.getLibretaPropiedad().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else return cliente.getNombre().toLowerCase().contains(lowerCaseFilter);
                });
                    lblInfo.setText("Clientes: " + filteredData.size());
                });

        SortedList<Cliente> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableData.comparatorProperty());
        lblInfo.setText("Clientes: " + filteredData.size());
        tableData.setItems(sortedData);
    }

    public static void cargaCompaniaTabla(TableView<Compania> tableData, ObservableList<Compania> companias){
        tableData.getColumns().addAll(getNombre(), getDescripcion(), getEmail(), getTelefono(), getWeb(), getNumeroAuxilio());
        tableData.setItems(companias);
    }

    public static void cargaTablaCotizacionVendedores(TableView<CotizacionVendedor> tableData, ObservableList<CotizacionVendedor> cotizacionVendedores, FilteredList<CotizacionVendedor> filteredData, Label lblInfo){
        tableData.getColumns().addAll(getVendedorToString(), getProducto(), getTipoProducto(), getCompania());

        if (UtilsSeguridad.traePermisoComision(MenuConfiguracion.CotizacionVendedores.getPagina())) {
            tableData.getColumns().addAll(getComisionNueva(), getComisionRenovacion());
        }
        if(cotizacionVendedores==null){
            cotizacionVendedores = FXCollections.observableArrayList(new ArrayList<>());
        }

        filteredData = new FilteredList<>(cotizacionVendedores, b -> true);
        SortedList<CotizacionVendedor> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableData.comparatorProperty());

        tableData.setItems(sortedData);
        lblInfo.setText("Cotizaciones: " + filteredData.size());
    }

    public static void cargaTablaProductoConFiltro(TableView<Producto> tableData, ObservableList<Producto> productos, TextField txtClienteCompania, Label lblInfo, String pagina){
        tableData.getColumns().addAll(getNombre(), getDescripcion(), getCompania(), getTipoProducto());

        if(UtilsSeguridad.traePermisoComision(pagina)){
            tableData.getColumns().addAll(getComisionNueva(), getComisionRenovacion());
        }

        tableData.getColumns().addAll(getFechaComienzoToString(), getFechaFinalToString());
        if(productos==null){
            productos = FXCollections.observableArrayList(new ArrayList<>());
        }
        FilteredList<Producto> filteredData = new FilteredList<>(productos, b -> true);
        txtClienteCompania.textProperty().addListener((observable, oldValue, newValue) ->
                {
                filteredData.setPredicate(producto -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (producto.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (producto.getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (producto.getCompania().getNombre().contains(lowerCaseFilter)) {
                        return true;
                    } else if (producto.getTipoProducto().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else return producto.getCompania().getNombre().toLowerCase().contains(lowerCaseFilter);
                });
                    lblInfo.setText("Productos: " + filteredData.size());
                });
        SortedList<Producto> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableData.comparatorProperty());
        tableData.setItems(sortedData);
        lblInfo.setText("Productos: " + filteredData.size());
    }

    public static void cargaTablaEstadoPoliza(TableView<EstadoPoliza> tableData, ObservableList<EstadoPoliza> estadoPolizas) {
        tableData.getColumns().addAll(getNombre(), getDescripcion());
        tableData.setItems(estadoPolizas);
    }

    public static void cargaTablaEstadoSiniestro(TableView<EstadoSiniestro> tableData, ObservableList<EstadoSiniestro> estadoSiniestros) {
        tableData.getColumns().addAll(getNombre() ,getDescripcion());
        tableData.setItems(estadoSiniestros);
    }

    public static void cargaTablaFormaPago(TableView<FormaPago> tableData, ObservableList<FormaPago> formaPagos) {
        tableData.getColumns().addAll(getNombre(), getDescripcion());
        tableData.setItems(formaPagos);
    }

    public static void cargaTablaIngresos(TableView<Ingreso> tableData, ObservableList<Ingreso> ingresos) {
        tableData.getColumns().addAll(getBanco(), getValor(), getMes(), getAnio());
        tableData.setItems(ingresos);
    }

    public static void cargaTablaMoneda(TableView<Moneda> tableData, ObservableList<Moneda> monedas) {
        tableData.getColumns().addAll(getNombre(), getSimbolo());
        tableData.setItems(monedas);
    }

    public static void cargaTablaPolizaSinFiltro(TableView<Poliza> tableData, ObservableList<Poliza> polizas, String pagina) {
        tableData.getColumns().addAll(getCompania(), getCliente(), getNumeroPoliza(),getComienzo(),
                getVencimiento(), getProducto(),getTipoProducto(), getPremio(), getPrima(), getMoneda());

        if (UtilsSeguridad.traePermisoComision(pagina)) {
            tableData.getColumns().add(getComision());
        }
        tableData.getColumns().addAll(getFormaCuota(), getCuotas(),
                getComienzoCuota(), getFinCuota(), getImporteCuota(), getCerradoPor(), getApp(), getEstado(), getObservaciones());

        if(polizas==null){
            polizas = FXCollections.observableArrayList(new ArrayList<>());
        }

        tableData.setItems(polizas);
    }

    public static void cargaTablaPoliza(TableView<Poliza> tableData, ObservableList<Poliza> polizas, TextField txtClienteCompania, Label lblInfo, String pagina) {
        tableData.getColumns().addAll(getCompania(), getCliente(), getNumeroPoliza(),getComienzo(),
                getVencimiento(), getProducto(),getTipoProducto(), getPremio(), getPrima(), getMoneda());

        if (UtilsSeguridad.traePermisoComision(pagina)) {
          tableData.getColumns().add(getComision());
        }
        tableData.getColumns().addAll(getFormaCuota(), getCuotas(),
                getComienzoCuota(), getFinCuota(), getImporteCuota(), getCerradoPor(), getApp(), getEstado());

        if(polizas==null){
            polizas = FXCollections.observableArrayList(new ArrayList<>());
        }

        FilteredList<Poliza> filteredData = new FilteredList<>(polizas, b -> true);

        txtClienteCompania.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(poliza -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (poliza.getCliente()!=null && checkNombreApellido(lowerCaseFilter, poliza.getCliente().getNombre(), poliza.getCliente().getApellido(), poliza.getCliente().getNombreYApellido())) {
                    return true;
                } else if (poliza.getNumeroPoliza()!=null && poliza.getNumeroPoliza().contains(lowerCaseFilter)) {
                    return true;
                } else if (poliza.getTipoProducto()!=null && poliza.getTipoProducto().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (poliza.getProducto()!=null && poliza.getProducto().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else return poliza.getCompania()!=null && poliza.getCompania().getNombre().toLowerCase().contains(lowerCaseFilter);
            });
            lblInfo.setText("Polizas: " + filteredData.size());
        });

        SortedList<Poliza> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableData.comparatorProperty());
        lblInfo.setText("Polizas: " + filteredData.size());
        tableData.setItems(sortedData);
    }

    public static void cargaTablaSiniestroSinFiltro(TableView<Siniestro> tableData, ObservableList<Siniestro> siniestros){
        tableData.getColumns().addAll(getCliente(), getNumeroSiniestro(), getPoliza(), getFechaFormato(), getEsDeducibleToString(), getImporteDeducible(), getEstadoSiniestro(), getInformacion(),getEstadoColor());
        tableData.setItems(siniestros);
    }

    public static void cargaTablaSiniestroConFiltro(TableView<Siniestro> tableData, ObservableList<Siniestro> siniestros, TextField txtSiniestroBusqueda, Label lblInfo){
        if(siniestros==null){
            siniestros = FXCollections.observableArrayList(new ArrayList<>());
        }

        FilteredList<Siniestro> filteredData = new FilteredList<>(siniestros, b -> true);
        txtSiniestroBusqueda
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              filteredData.setPredicate(
                  siniestro -> {
                    if (newValue == null || newValue.isEmpty()) {
                      return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if(checkNombreApellido(lowerCaseFilter,
                            siniestro.getCliente().getNombre(),
                            siniestro.getCliente().getApellido(),
                            siniestro.getCliente().getNombreYApellido())){
                        return true;
                    } else if (siniestro.getNumeroSiniestro().contains(lowerCaseFilter)) {
                      return true;
                    } else {
                      return siniestro.getPoliza().getNumeroPoliza().contains(lowerCaseFilter);
                    }
                  });
              lblInfo.setText("Siniestros: " + filteredData.size());
            });

        SortedList<Siniestro> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableData.comparatorProperty());
        lblInfo.setText("Siniestros: " + filteredData.size());
        tableData.setItems(sortedData);
        tableData.getColumns().addAll(getCliente(), getNumeroSiniestro(), getPoliza(), getFechaFormato(), getEsDeducibleToString(), getImporteDeducible(), getEstadoSiniestro(), getInformacion(),getEstadoColor());

    }

    private static boolean checkNombreApellido(String lowerCaseFilter,String nombre, String apellido, String nombreYApellido){
        String[] diferentWords = lowerCaseFilter.toLowerCase().split(" ");
        if (nombre.toLowerCase().contains(lowerCaseFilter)) {
            return true;
        } else if (apellido!=null && apellido.toLowerCase().contains(lowerCaseFilter)) {
            return true;
        } else if (nombreYApellido.toLowerCase().contains(lowerCaseFilter)) {
            return true;
        } else if (diferentWords.length>1 && nombre.toLowerCase().contains(diferentWords[0].trim()) && (apellido!=null && apellido.toLowerCase().contains(diferentWords[1].trim()))){
            return true;
        } else {
            return false;
        }
    }

    public static void cargaTablaTipoProducto(TableView<TipoProducto> tableData, ObservableList<TipoProducto> tipoProductos){
        tableData.getColumns().addAll(getNombre(), getDescripcion());
        tableData.setItems(tipoProductos);
    }

    public static void cargaTablaTipoUsuario(TableView<TipoUsuario> tableData, ObservableList<TipoUsuario> tipoProductos){
        tableData.getColumns().addAll(getNombre(), getDescripcion());
        tableData.setItems(tipoProductos);
    }

    public static void cargaTablaUsuario(TableView<Usuario> tableData, ObservableList<Usuario> usuarios){
        tableData.getColumns().addAll(getNombre(), getTipoUsuario());
        tableData.setItems(usuarios);
    }

    public static void cargaTablaVendedor(TableView<Vendedor> tableData, ObservableList<Vendedor> vendedores){
        tableData.getColumns().addAll(getNombre(), getApellido(), getDireccion(), getCiudad(), getDepartamento(), getFechaNacimiento(), getCelular(), getEmail());
        tableData.setItems(vendedores);
    }

    public static void cargaTablaPremioReporte(TableView<PolizaDTO> tableData, ObservableList<PolizaDTO> premioReportes){
        tableData.getColumns().addAll(getCompania(), getProducto(), getTipoProducto(), getMoneda(), getPremio());
        tableData.setItems(premioReportes);
    }

    public static void cargaTablaPrimaReporte(TableView<PolizaDTO> tableData, ObservableList<PolizaDTO> primaReportes){
        tableData.getColumns().addAll(getCompania(), getProducto(), getTipoProducto(), getMoneda(), getPrima());
        tableData.setItems(primaReportes);
    }

    public static void cargaTablaProductosPrimaTotal(TableView<PolizaDTO> tableData, ObservableList<PolizaDTO> primaReportes){
        tableData.getColumns().addAll(getCompania(), getTipoProducto(),getMoneda(), getTotalComision());
        tableData.setItems(primaReportes);
    }

    public static void cargaTablaVentasPorTipoProducto(TableView<PolizaDTO> tableData, ObservableList<PolizaDTO> primaReportes){
        tableData.getColumns().addAll(getTipoProducto(), getTotalComision(), getEstado());
        tableData.setItems(primaReportes);
    }

    public static void cargaTablaComision(TableView<PolizaDTO> tableData, ObservableList<PolizaDTO> primaReportes){
        tableData.getColumns().addAll(getCompania(), getCerradoPor(), getTipoProducto());

        if (UtilsSeguridad.traePermisoComision(MenuPrincipal.Reportes.getPagina())) {
            tableData.getColumns().add(getMoneda());
            tableData.getColumns().add(getComision());
            tableData.getColumns().add(getComisionCobrada());
        }
        tableData.setItems(primaReportes);
    }

    public static void cargaTablaRegistroCuotas(TableView<RegistroCuotas> tableData, ObservableList<RegistroCuotas> registroCuotasList){
        tableData.getColumns().addAll(getNumeroPoliza(), getCliente(), getCompania(), getNumeroCuotasPagas(), getCuotas(), getAction(), getUltimaActualizacion());
        tableData.setItems(registroCuotasList);
        tableData.setEditable(true);
    }

    public static void cargaTablaPermisosUsuarios(TableView<PermisoUsuario> tblPermisos, ObservableList<PermisoUsuario> listaPermisos){
        tblPermisos.getColumns().addAll(getPagina(), getPermiso());
        tblPermisos.setItems(listaPermisos);
    }

    public static void cargaTablaTipoUsuarios(TableView<TipoUsuario> tableData, ObservableList<TipoUsuario> listaTipoUsuarios){
        tableData.getColumns().addAll(getNombre(), getDescripcion());
        tableData.setItems(listaTipoUsuarios);
    }

    public static void cargaTablaTareas(TableView<Tarea> tableData, ObservableList<Tarea> tareas){
        tableData.getColumns().addAll(getNombre(), getFechaFormato(), getTotal());
        tableData.setItems(tareas);
    }

    /*
    *
    * Columnas
    *
    * */

    public static TableColumn getBanco(){
        TableColumn banco = new TableColumn(ConstantesEtiquetas.BANCO_UPPER);
        banco.setMinWidth(C120);
        banco.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.BANCO));

       return banco;
    }

    public static TableColumn getValor(){
        TableColumn valor = new TableColumn(ConstantesEtiquetas.VALOR);
        valor.setMinWidth(C120);
        valor.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.VALOR));

        return valor;
    }

    public static TableColumn getMes(){
        TableColumn mes = new TableColumn(ConstantesEtiquetas.MES_UPPER);
        mes.setMinWidth(C120);
        mes.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.MES_STRING));

        return mes;
    }

    public static TableColumn getAnio(){
        TableColumn anio = new TableColumn(ConstantesEtiquetas.ANIO_UPPER);
        anio.setMinWidth(C120);
        anio.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.ANIO));

        return anio;
    }

    public static TableColumn getTipoUsuario(){
        TableColumn tipoUsuario = new TableColumn(ConstantesEtiquetas.TIPO_USUARIO_UPPER);
        tipoUsuario.setMinWidth(C120);
        tipoUsuario.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.TIPO_USUARIO));

        return tipoUsuario;
    }

    private static TableColumn getPermiso(){
        TableColumn permiso = new TableColumn(ConstantesEtiquetas.PERMISO_UPPER);
        permiso.setMinWidth(C120);
        permiso.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.PERMISO));

        return permiso;
    }

    private static TableColumn getPagina(){
        TableColumn pagina = new TableColumn(ConstantesEtiquetas.PAGINA_UPPER);
        pagina.setMinWidth(C120);
        pagina.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.PAGINA));

        return pagina;
    }

    private static TableColumn getNumeroAuxilio(){
        TableColumn numeroAuxilio = new TableColumn(ConstantesEtiquetas.NUMERO_AUXILIO_UPPER);
        numeroAuxilio.setMinWidth(C120);
        numeroAuxilio.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.NUMERO_AUXILIO));

        return numeroAuxilio;
    }

    private static TableColumn getWeb(){
        TableColumn web = new TableColumn(ConstantesEtiquetas.WEB_UPPER);
        web.setMinWidth(C120);
        web.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.WEB));

        return web;
    }

    private static TableColumn getUltimaActualizacion(){
        TableColumn ultimaActualizacion = new TableColumn(ConstantesEtiquetas.ULTIMA_ACTUALIZACION_UPPER);
        ultimaActualizacion.setMinWidth(C150);
        ultimaActualizacion.setCellValueFactory(new PropertyValueFactory<RegistroCuotas, Date>(ConstantesEtiquetas.ULTIMA_ACTUALIZACION_TO_STRING));

        return ultimaActualizacion;
    }

    private static TableColumn getNumeroCuotasPagas(){
        TableColumn finCuota = new TableColumn(ConstantesEtiquetas.NUMERO_CUOTAS_PAGAS_UPPER);
        finCuota.setMinWidth(C100);
        finCuota.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.NUMERO_CUOTAS_PAGAS));

        return finCuota;
    }

    private static TableColumn getFinCuota(){
        TableColumn finCuota = new TableColumn(ConstantesEtiquetas.FIN_CUOTA_UPPER);
        finCuota.setMinWidth(C100);
        finCuota.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.FIN_CUOTA_TO_STRING));

        return finCuota;
    }

    private static TableColumn getComienzoCuota(){
        TableColumn comienzoCuota = new TableColumn(ConstantesEtiquetas.COMIENZO_CUOTA_UPPER);
        comienzoCuota.setMinWidth(C100);
        comienzoCuota.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.COMIENZO_CUOTA_TO_STRING));

        return comienzoCuota;
    }

    private static TableColumn getCuotas(){
        TableColumn cuotas = new TableColumn(ConstantesEtiquetas.CUOTA_UPPER);
        cuotas.setMinWidth(C100);
        cuotas.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.CUOTAS));

        return cuotas;
    }

    private static TableColumn getFormaCuota(){
        TableColumn formaPago = new TableColumn(ConstantesEtiquetas.FORMA_PAGO_UPPER);
        formaPago.setMinWidth(C100);
        formaPago.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.FORMA_PAGO));

        return formaPago;
    }

    private static TableColumn getImporteCuota(){
        TableColumn importeCuota = new TableColumn(ConstantesEtiquetas.IMPORTE_CUOTA_UPPER);
        importeCuota.setMinWidth(C100);
        importeCuota.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.IMPORTE_CUOTA));

        return importeCuota;
    }

    private static TableColumn getApp(){
        TableColumn esApp = new TableColumn(ConstantesEtiquetas.ES_APP_UPPER);
        esApp.setMinWidth(C100);
        esApp.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.ES_APP));

        return esApp;
    }

    private static TableColumn getEstado(){
        TableColumn estado = new TableColumn(ConstantesEtiquetas.ESTADO_UPPER);
        estado.setMinWidth(C100);
        estado.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.ESTADO));

        return estado;
    }

    private static TableColumn getMoneda(){
        TableColumn moneda = new TableColumn(ConstantesEtiquetas.MONEDA_UPPER);
        moneda.setMinWidth(50);
        moneda.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.MONEDA));

        return moneda;
    }

    private static TableColumn getVencimiento(){
        TableColumn vencimiento = new TableColumn(ConstantesEtiquetas.VENCIMIENTO_UPPER);
        vencimiento.setMinWidth(C100);
        vencimiento.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.VENCIMIENTO_TO_STRING));

        return vencimiento;
    }

    private static TableColumn getComienzo(){
        TableColumn comienzo = new TableColumn(ConstantesEtiquetas.COMIENZO_UPPER);
        comienzo.setMinWidth(C100);
        comienzo.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.COMIENZO_TO_STRING));
        return comienzo;
    }

    private static TableColumn getNumeroPoliza(){
        TableColumn numeroPoliza = new TableColumn(ConstantesEtiquetas.NUMERO_POLIZA_UPPER);
        numeroPoliza.setMinWidth(C150);
        numeroPoliza.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.NUMERO_POLIZA));

        return numeroPoliza;
    }

    private static TableColumn getAction(){
        TableColumn activo = new TableColumn(ConstantesEtiquetas.PAGO_CUOTA_UPPER);
        activo.setMinWidth(C120);
        activo.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.PAGO_CUOTA));
        return activo;
    }

    private static TableColumn getActivo(){
        TableColumn activo = new TableColumn(ConstantesEtiquetas.ACTIVO_UPPER);
        activo.setMinWidth(C100);
        activo.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.ACTIVO_TO_STRING));

        return activo;
    }

    private static TableColumn getVendedorToString(){
        TableColumn vendedoresToString = new TableColumn(ConstantesEtiquetas.VENDEDOR_UPPER);
        vendedoresToString.setMinWidth(C100);
        vendedoresToString.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.VENDEDOR_TO_STRING));

        return vendedoresToString;
    }

    private static TableColumn getComision(){
        TableColumn comision = new TableColumn(ConstantesEtiquetas.COMISION_UPPER);
        comision.setMinWidth(C100);
        comision.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.COMISION));

        return comision;
    }

    private static TableColumn getComisionCobrada(){
        TableColumn comisionCobrada = new TableColumn(ConstantesEtiquetas.COMISION_COBRADA_UPPER);
        comisionCobrada.setMinWidth(C100);
        comisionCobrada.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.COMISION_COBRADA));
        return comisionCobrada;
    }

    private static TableColumn getCerradoPor(){
        TableColumn cerradoPor = new TableColumn(ConstantesEtiquetas.CERRADO_POR_UPPER);
        cerradoPor.setMinWidth(C100);
        cerradoPor.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.CERRADO_POR));

        return cerradoPor;
    }

    private static TableColumn getTotalComision(){
        TableColumn total = new TableColumn(ConstantesEtiquetas.TOTAL_COMISION_UPPER);
        total.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.TOTAL));
        return total;
    }

    private static TableColumn getTotal(){
        TableColumn total = new TableColumn(ConstantesEtiquetas.TOTAL_UPPER);
        total.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.TOTAL));
        return total;
    }

    private static TableColumn getRut(){
        TableColumn rut = new TableColumn(ConstantesEtiquetas.RUT_UPPER);
        rut.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.RUT));
        return rut;
    }

    private static TableColumn getObservaciones(){
        TableColumn observaciones = new TableColumn(ConstantesEtiquetas.OBSERVACIONES_UPPER);
        observaciones.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.OBSERVACIONES));
        return observaciones;
    }

    private static TableColumn getEstadoColor(){
        TableColumn estadoColor = new TableColumn(ConstantesEtiquetas.ESTADO_UPPER);
        estadoColor.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.ESTADO_COLOR));
        return estadoColor;
    }

    private static TableColumn getInformacion(){
        TableColumn informacion = new TableColumn(ConstantesEtiquetas.INFORMACION_UPPER);

        informacion.setMinWidth(110);
        informacion.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.INFORMACION));
        return informacion;
    }

    private static TableColumn getEstadoSiniestro(){
        TableColumn estado = new TableColumn(ConstantesEtiquetas.ESTADO_UPPER);

        estado.setMinWidth(110);
        estado.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.ESTADO_SINIESTRO));
        return estado;
    }

    private static TableColumn getImporteDeducible(){
        TableColumn importeDeducible = new TableColumn(ConstantesEtiquetas.IMPORTE_DEDUCIBLE_UPPER);

        importeDeducible.setMinWidth(110);
        importeDeducible.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.IMPORTE_DEDUCIBLE));
        return importeDeducible;
    }

    private static TableColumn getEsDeducibleToString(){
        TableColumn esDeducible = new TableColumn(ConstantesEtiquetas.ES_DEDUCIBLE_UPPER);

        esDeducible.setMinWidth(110);
        esDeducible.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.IMPORTE_DEDUCIBLE_TO_STRING));
        return esDeducible;
    }

    private static TableColumn getFecha(){
        TableColumn fecha = new TableColumn(ConstantesEtiquetas.FECHA_UPPER);

        fecha.setMinWidth(100);
        fecha.setCellValueFactory(new PropertyValueFactory<Siniestro, Date>(ConstantesEtiquetas.FECHA));
        return fecha;
    }

    private static TableColumn getPoliza(){
        TableColumn poliza = new TableColumn(ConstantesEtiquetas.POLIZA_UPPER);

        poliza.setMinWidth(150);
        poliza.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.POLIZA));
        return poliza;
    }

    private static TableColumn getNumeroSiniestro(){
        TableColumn numeroSiniestro = new TableColumn(ConstantesEtiquetas.NUMERO_SINIESTRO_UPPER);

        numeroSiniestro.setMinWidth(150);
        numeroSiniestro.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.NUMERO_SINIESTRO));
        return numeroSiniestro;
    }

    private static TableColumn getFechaFinalToString(){
        TableColumn fechaFinalToString = new TableColumn(ConstantesEtiquetas.FECHA_FINAL_UPPER);

        fechaFinalToString.setMinWidth(130);
        fechaFinalToString.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.FECHA_FINAL_TO_STRING));
        return fechaFinalToString;
    }

    private static TableColumn getFechaComienzoToString(){
        TableColumn fechaComienzoToString = new TableColumn(ConstantesEtiquetas.FECHA_COMIENZO_UPPER);

        fechaComienzoToString.setMinWidth(130);
        fechaComienzoToString.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.FECHA_COMIENZO_TO_STRING));
        return fechaComienzoToString;
    }

    private static TableColumn getRecomendadoPor(){
        TableColumn recomendadoPor = new TableColumn(ConstantesEtiquetas.RECOMENDADO_POR_UPPER);

        recomendadoPor.setMinWidth(130);
        recomendadoPor.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.RECOMENDADO_POR));
        return recomendadoPor;
    }

    private static TableColumn getLibretaPropiedad(){
        TableColumn libretaPropiedad = new TableColumn(ConstantesEtiquetas.LIBRETA_PROPIEDAD_UPPER);

        libretaPropiedad.setMinWidth(130);
        libretaPropiedad.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.LIBRETA_PROPIEDAD));
        return libretaPropiedad;
    }

    private static TableColumn getCedulaIdentidad(){
        TableColumn cedulaIdentidad = new TableColumn(ConstantesEtiquetas.CEDULA_IDENTIDAD_UPPER);

        cedulaIdentidad.setMinWidth(C100);
        cedulaIdentidad.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.CEDULA_IDENTIDAD));
        return cedulaIdentidad;
    }

    private  static TableColumn getPremio(){
        TableColumn premio = new TableColumn(ConstantesEtiquetas.PREMIO_UPPER);

        premio.setMinWidth(C100);
        premio.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.PREMIO));
        return premio;
    }

    private  static TableColumn getPrima(){
        TableColumn prima = new TableColumn(ConstantesEtiquetas.PRIMA_UPPER);

        prima.setMinWidth(C100);
        prima.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.PRIMA));
        return prima;
    }

    private static TableColumn getProducto(){
        TableColumn producto = new TableColumn(ConstantesEtiquetas.PRODUCTO_UPPER);

        producto.setMinWidth(C120);
        producto.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.PRODUCTO));
        return producto;
    }

    private static TableColumn getCliente(){
        TableColumn cliente = new TableColumn(ConstantesEtiquetas.CLIENTE_UPPER);

        cliente.setMinWidth(C170);
        cliente.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.CLIENTE));

        return cliente;
    }

    private static TableColumn getSimbolo(){
        TableColumn<Object, Object> simbolo = new TableColumn<>(ConstantesEtiquetas.SIMBOLO_UPPER);

        simbolo.setMinWidth(150);
        simbolo.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.SIMBOLO));
        return simbolo;
    }

    private static TableColumn getNombre(){
        TableColumn nombre = new TableColumn<>(ConstantesEtiquetas.NOMBRE_UPPER);

        nombre.setMinWidth(C120);
        nombre.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.NOMBRE));

        return nombre;
    }

    private static TableColumn getApellido(){
        TableColumn apellido = new TableColumn(ConstantesEtiquetas.APELLIDO_UPPER);

        apellido.setMinWidth(C120);
        apellido.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.APELLIDO));

        return apellido;
    }

    private static TableColumn getDireccion(){
        TableColumn direccion = new TableColumn(ConstantesEtiquetas.DIRECCION_UPPER);

        direccion.setMinWidth(100);
        direccion.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.DIRECCION));

        return direccion;
    }

    private static TableColumn getCiudad(){
        TableColumn ciudad = new TableColumn(ConstantesEtiquetas.CIUDAD_UPPER);

        ciudad.setMinWidth(100);
        ciudad.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.CIUDAD));

        return ciudad;
    }

    private static TableColumn getDepartamento(){
        TableColumn departamento = new TableColumn(ConstantesEtiquetas.DEPARTAMENTO_UPPER);

        departamento.setMinWidth(110);
        departamento.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.DEPARTAMENTO));

        return departamento;
    }

    private static TableColumn getFechaNacimiento(){
        TableColumn fechaNacimiento = new TableColumn(ConstantesEtiquetas.FECHA_NACIMIENTO_UPPER);

        fechaNacimiento.setMinWidth(110);
        fechaNacimiento.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.FECHA_NACIMIENTO_STRING));

        return fechaNacimiento;
    }

    private static TableColumn getFechaFormato(){
        TableColumn fecha = new TableColumn(ConstantesEtiquetas.FECHA_TO_STRING_UPPER);

        fecha.setMinWidth(110);
        fecha.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.FECHA_TO_STRING));

        return fecha;
    }

    private static TableColumn getCelular(){
       TableColumn celular = new TableColumn(ConstantesEtiquetas.CELULAR_UPPER);

       celular.setMinWidth(110);
       celular.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.CELULAR));

       return celular;
    }

    private static TableColumn getDescripcion(){
        TableColumn descripcion = new TableColumn(ConstantesEtiquetas.DESCRIPCION_UPPER);

        descripcion.setMinWidth(C120);
        descripcion.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.DESCRIPCION));

        return descripcion;
    }

    private static TableColumn getTelefono(){
        TableColumn telefono = new TableColumn(ConstantesEtiquetas.TELEFONO_UPPER);

        telefono.setMinWidth(C120);
        telefono.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.TELEFONO));

        return telefono;
    }

    private static TableColumn getEmail(){
        TableColumn email = new TableColumn(ConstantesEtiquetas.EMAIL_UPPER);

        email.setMinWidth(C120);
        email.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.EMAIL));

        return email;
    }

    private static TableColumn getTipoProducto(){
        TableColumn tipoProducto = new TableColumn(ConstantesEtiquetas.TIPO_PRODUCTO_UPPER);

        tipoProducto.setMinWidth(C120);
        tipoProducto.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.TIPO_PRODUCTO));

        return tipoProducto;
    }

    private static TableColumn getCompania(){
        TableColumn compania = new TableColumn(ConstantesEtiquetas.COMPANIA_UPPER);

        compania.setMinWidth(C120);
        compania.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.COMPANIA));

        return compania;
    }

    private static TableColumn getComisionNueva(){
        TableColumn comisionNueva = new TableColumn(ConstantesEtiquetas.COMISION_NUEVA_UPPER);

        comisionNueva.setMinWidth(C120);
        comisionNueva.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.COMISION_NUEVA));

        return comisionNueva;
    }

    private static TableColumn getComisionRenovacion(){
        TableColumn comisionRenovacion = new TableColumn(ConstantesEtiquetas.COMISION_RENOVACION_UPPER);

        comisionRenovacion.setMinWidth(C120);
        comisionRenovacion.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.COMISION_RENOVACION_TABLE));

        return comisionRenovacion;
    }
}
