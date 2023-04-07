package com.software.tareasApp.utils;

import com.software.tareasApp.persistence.model.Movimiento;
import com.software.tareasApp.persistence.model.PermisoUsuario;
import com.software.tareasApp.persistence.model.Tarea;
import com.software.tareasApp.persistence.model.TipoUsuario;
import com.software.tareasApp.persistence.model.Usuario;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import javax.security.auth.callback.Callback;


public class UtilCargaTablas {

    private final static double C120 = 120;

    public static void cargaTablaTipoUsuario(TableView<TipoUsuario> tableData, ObservableList<TipoUsuario> tipoProductos){
        tableData.getColumns().addAll(getNombre(), getDescripcion());
        tableData.setItems(tipoProductos);
    }

    public static void cargaTablaUsuario(TableView<Usuario> tableData, ObservableList<Usuario> usuarios){
        tableData.getColumns().addAll(getNombre(), getTipoUsuario());
        tableData.setItems(usuarios);
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

    public static void cargaTablaMovimientos(TableView<Movimiento> tableData, ObservableList<Movimiento> movimientos){
        tableData.getColumns().addAll(getFechaFormato(), getConcepto(),getDebito(), getCredito(), getSaldo());
        tableData.setItems(movimientos);
    }

    /*
    *
    * Columnas
    *
    * */

    public static TableColumn getDebito(){
        TableColumn debito = new TableColumn(ConstantesEtiquetas.DEBITO_UPPER);
        debito.setMinWidth(C120);
        debito.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.DEBITO));
        debito.setStyle("-fx-text-fill: #ff6961; -fx-font-weight: bold;");
        return debito;
    }

    public static TableColumn getCredito(){
        TableColumn credito = new TableColumn(ConstantesEtiquetas.CREDITO_UPPER);
        credito.setMinWidth(C120);
        credito.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.CREDITO));
        credito.setStyle("-fx-text-fill: #77dd77; -fx-font-weight: bold;");
        return credito;
    }

    public static TableColumn getSaldo(){
        TableColumn saldo = new TableColumn(ConstantesEtiquetas.SALDO_UPPER);
        saldo.setMinWidth(C120);
        saldo.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.SALDO));
        return saldo;
    }

    public static TableColumn getConcepto(){
        TableColumn tipoUsuario = new TableColumn(ConstantesEtiquetas.CONCEPTO_UPPER);
        tipoUsuario.setMinWidth(C120);
        tipoUsuario.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.CONCEPTO));

        return tipoUsuario;
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

    private static TableColumn getTotal(){
        TableColumn total = new TableColumn(ConstantesEtiquetas.TOTAL_UPPER);
        total.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.TOTAL));
        return total;
    }

    private static TableColumn getNombre(){
        TableColumn nombre = new TableColumn<>(ConstantesEtiquetas.NOMBRE_UPPER);

        nombre.setMinWidth(C120);
        nombre.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.NOMBRE));

        return nombre;
    }

    private static TableColumn getFechaFormato(){
        TableColumn fecha = new TableColumn(ConstantesEtiquetas.FECHA_TO_STRING_UPPER);

        fecha.setMinWidth(110);
        fecha.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.FECHA_TO_STRING));

        return fecha;
    }

    private static TableColumn getDescripcion(){
        TableColumn descripcion = new TableColumn(ConstantesEtiquetas.DESCRIPCION_UPPER);

        descripcion.setMinWidth(C120);
        descripcion.setCellValueFactory(new PropertyValueFactory<>(ConstantesEtiquetas.DESCRIPCION));

        return descripcion;
    }
}
