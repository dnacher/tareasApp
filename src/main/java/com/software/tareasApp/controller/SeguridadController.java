package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.PermisoUsuarioService;
import com.software.tareasApp.domain.service.TipoUsuarioService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.enums.MenuSeguridad;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.PermisoUsuario;
import com.software.tareasApp.persistence.model.TipoUsuario;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.ConstantePermisos;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class SeguridadController implements Initializable {

    @FXML
    private AnchorPane paneCrud;

    @FXML
    private AnchorPane paneTabel;

    @FXML
    private CheckBox chkAgregar;

    @FXML
    private CheckBox chkVer;

    @FXML
    private CheckBox chkAdministrador;

    @FXML
    private CheckBox chkComisiones;

    @FXML
    private TableView<TipoUsuario> tableData;

    @FXML
    private ProgressBar bar;

    @FXML
    private TableView<PermisoUsuario> tblPermisos;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private Label lblTIpoUsuario;

    @FXML
    private Label lblInicio;

    @FXML
    private Label lblEstados;

    @FXML
    private Label lblConfiguracion;

    @FXML
    private Label lblSeguridad;

    @FXML
    private ComboBox<String> cmbPagina;

    @FXML
    private Button btnAgregarTodos;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnAgregarEliminar;

    @FXML
    private Button btnAgregarEliminarTodos;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnBack;

    @Autowired
    public SeguridadController(PermisoUsuarioService usuarioPermisoService, TipoUsuarioService tipoUsuarioService, ConstantesPaginas constantesPaginas){
        this.constantesPaginas = constantesPaginas;
        this.tipoUsuarioService = tipoUsuarioService;
        this.usuarioPermisoService = usuarioPermisoService;
    }

    private final ConstantesPaginas constantesPaginas;
    private final TipoUsuarioService tipoUsuarioService;
    private final PermisoUsuarioService usuarioPermisoService;

    public ObservableList<TipoUsuario> listaTipoUsuarios;
    public ObservableList<PermisoUsuario> listaPermisos;
    public ObservableList<String> listaPaginas;
    public List<PermisoUsuario> listaPermisosUsuario;
    public TipoUsuario tu;

    PermisoUsuario inicio;
    PermisoUsuario estados;
    PermisoUsuario configuracion;
    PermisoUsuario seguridad;

    private final LogManagerClass logger = new LogManagerClass(getClass());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            tableData.setDisable(!UtilsSeguridad.traePermisos(MenuSeguridad.Permisos.getPagina(), ConstantePermisos.PERMISO_ADMIN));
            lblnoVisibles(false);
            task();
            atras();
            buttonOnAction();
            agregaListenerChkBox();
        } catch (Exception ex) {
            errorLog(ex);
        }
    }

    private void buttonOnAction(){
        btnAgregarTodosOnAction();
        btnAgregarOnAction();
        btnEliminarOnAction();
        btnAgregarEliminarTodosOnAction();
        btnGuardarOnAction();
        btnBackOnAction();

    }

    private void btnAgregarTodosOnAction(){
        btnAgregarTodos.setOnAction(event -> agregarTodos());
    }

    private void btnAgregarOnAction(){
        btnAgregar.setOnAction(event -> agregar());
    }

    private void btnEliminarOnAction(){
        btnAgregarEliminar.setOnAction(event -> eliminar());
    }

    private void btnGuardarOnAction(){ btnGuardar.setOnAction(event -> guardar());}

    private void btnBackOnAction(){ btnBack.setOnAction(event -> atras());}

    public void lblnoVisibles(boolean visible) {
        lblInicio.setVisible(visible);
        lblEstados.setVisible(visible);
        lblConfiguracion.setVisible(visible);
        lblSeguridad.setVisible(visible);
    }

    private void btnAgregarEliminarTodosOnAction(){
        btnAgregarEliminarTodos.setOnAction(event -> eliminarTodos());
    }

    public void agregaListenerChkBox() {
        chkVer.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (chkAgregar.isSelected()) {
                chkVer.setSelected(true);
            } else {
                if (chkAdministrador.isSelected()) {
                    chkVer.setSelected(true);
                    chkAgregar.setSelected(true);
                }
            }
            if(!newValue){
                if(!chkAgregar.isSelected() && !chkAdministrador.isSelected()){
                    chkComisiones.setSelected(false);
                }
            }
        });

        chkAgregar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (chkAgregar.isSelected()) {
                chkVer.setSelected(true);
            } else {
                if (chkAdministrador.isSelected()) {
                    chkVer.setSelected(true);
                    chkAgregar.setSelected(true);
                }
            }
        });

        chkAdministrador.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (chkAdministrador.isSelected()) {
                chkVer.setSelected(true);
                chkAgregar.setSelected(true);
            }
        });

        chkComisiones.selectedProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue){
               if(!chkAdministrador.isSelected() && !chkAgregar.isSelected() && !chkVer.isSelected()){
                   chkComisiones.setSelected(false);
               }
           }
        });
    }

    public void task() {
        Task<Void> longTask = UtilsGeneral.task();

        longTask.setOnSucceeded(t -> {
            try {
                listaTipoUsuarios = FXCollections.observableArrayList(tipoUsuarioService.getTipoUsuarios());
                cargaTabla();
                cargaTablaPermisosUsuarios();
                cargaComboPagina();
            } catch (TareasAppException ex) {
                errorLog(ex);
            }
        });
        bar.progressProperty().bind(longTask.progressProperty());
        new Thread(longTask).start();
    }

    private void cargaComboPagina() {
        if (listaPaginas == null) {
            listaPaginas = FXCollections.observableArrayList(new ArrayList());
        }
        for (PermisoUsuario pu : TareaAppApplication.permisosUsuario) {
            if (pu.getPermiso() == 7 || pu.getPermiso() == 15) {
                if (noEs(pu.getPagina())) {
                    listaPaginas.add(pu.getPagina());
                }
            }
        }
        cmbPagina.setItems(listaPaginas);
    }

    private boolean noEs(String pagina) {
        boolean noEs = true;
        switch (pagina) {
            case "Inicio":
            case "Estados":
            case "Tipos":
            case "Configuracion":
            case "Seguridad":
                noEs = false;
                break;
        }
        return noEs;
    }

    public void nuevoGrupo() {
        if (tableData.getSelectionModel().getSelectedItem() != null) {
            tu = tableData.getSelectionModel().getSelectedItem();
            inicio = new PermisoUsuario(tu, MenuPrincipal.Inicio.getPagina(), 7);
            seguridad = new PermisoUsuario(tu, MenuPrincipal.Seguridad.getPagina(), 7);

            lblTIpoUsuario.setText(tu.getNombre());
            if (listaPermisos != null) {
                listaPermisos.clear();
                listaPermisosUsuario.clear();
            } else {
                listaPermisosUsuario = new ArrayList<>();
                listaPermisos = FXCollections.observableArrayList(listaPermisosUsuario);
            }
            listaPermisosUsuario = usuarioPermisoService.findAllByUserType(tu);
            removeCarpetas(listaPermisosUsuario);
            listaPermisos = FXCollections.observableArrayList(listaPermisosUsuario);
            for (PermisoUsuario pu : listaPermisos) {
                verificaCarpeta(pu.getPagina(), true);
            }
            tblPermisos.setItems(listaPermisos);
            paneTabel.setOpacity(0);
            new FadeInUpTransition(paneCrud).play();
        }
    }

    private void removeCarpetas(List<PermisoUsuario> listaPermisosUsuario){
        listaPermisosUsuario.removeIf(pu -> !noEs(pu.getPagina()));
    }

    public void cargaTablaPermisosUsuarios() {
        UtilCargaTablas.cargaTablaPermisosUsuarios(tblPermisos,listaPermisos);
    }

    public void agregar() {
        if (cmbPagina.getSelectionModel().getSelectedItem() != null) {
            if (listaPermisosUsuario == null) {
                listaPermisosUsuario = new ArrayList<>();
            }
            if (!existePagina(cmbPagina.getSelectionModel().getSelectedItem())) {
                PermisoUsuario pu = new PermisoUsuario();
                pu.setPagina(cmbPagina.getSelectionModel().getSelectedItem());
                pu.setUserType(tu);
                pu.setPermiso(traeValorPermiso());
                listaPermisosUsuario.add(pu);
                listaPermisos = FXCollections.observableArrayList(listaPermisosUsuario);
                tblPermisos.setItems(listaPermisos);
                verificaCarpeta(pu.getPagina(), true);
            } else {
                UtilsGeneral.warning(Errores.PAGINA_EXISTE);
            }

        } else {
            UtilsGeneral.warning(Errores.SELECCIONAR_COMBOS);
        }
    }

    public void verificaCarpeta(String pagina, boolean agregar) {
        Arrays.stream(MenuPrincipal.values()).filter(ma -> ma.getPagina().equals(pagina)).findFirst().ifPresent(menuPrincipal -> lblInicio.setVisible(agregar));
        Arrays.stream(MenuSeguridad.values()).filter(ma -> ma.getPagina().equals(pagina)).findFirst().ifPresent(menuSeguridad -> lblSeguridad.setVisible(agregar));
    }

    public void agregarTodos() {
        if(traeValorPermiso()==0){
            UtilsGeneral.error(Errores.MARCAR_VALOR_PERMISO);
        }else{
            listaPermisos.clear();
            listaPermisosUsuario.clear();
            for (String s : cmbPagina.getItems()) {
                PermisoUsuario pu = new PermisoUsuario();
                pu.setPermiso(traeValorPermiso());
                pu.setPagina(s);
                pu.setUserType(tu);
                listaPermisosUsuario.add(pu);
            }
            listaPermisos = FXCollections.observableArrayList(listaPermisosUsuario);
            tblPermisos.setItems(listaPermisos);
            lblnoVisibles(true);
        }
    }

    public void eliminar() {
        if (tblPermisos.getSelectionModel().getSelectedItem() != null) {
            listaPermisosUsuario.remove(tblPermisos.getSelectionModel().getSelectedItem());
            verificaCarpeta(tblPermisos.getSelectionModel().getSelectedItem().getPagina(), false);
            listaPermisos.remove(tblPermisos.getSelectionModel().getSelectedItem());
        } else {
            UtilsGeneral.warning(Errores.SELECCIONAR_PERMISO);
        }
    }

    public void eliminarTodos() {
        listaPermisos.clear();
        listaPermisosUsuario.clear();
        lblnoVisibles(false);
    }

    public boolean existePagina(String pagina) {
        boolean existe = false;
        for (PermisoUsuario pu : listaPermisosUsuario) {
            if (pu.getPagina().equals(pagina)) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public int traeValorPermiso() {
        int permiso = 0;
        if (chkVer.isSelected()) {
            permiso = 1;
        }
        if (chkAgregar.isSelected()) {
            permiso = 3;
        }
        if (chkAdministrador.isSelected()) {
            permiso = 7;
        }
        if(chkComisiones.isSelected()){
            permiso+=8;
        }
        return permiso;
    }

    private void guardar() {
        if (listaPermisos != null && listaPermisos.size() > 0) {
            try {
                usuarioPermisoService.deleteByTipoUsuario(tu);
                agregaCarpetas();
                usuarioPermisoService.savePermissionUserList(listaPermisos);
                tu = null;
                atras();
            } catch (TareasAppException ex) {
               errorLog(ex);
            }
        }
    }

    private boolean agregaCarpeta(boolean isVisible, PermisoUsuario pu, boolean esta){
        if(isVisible){
            esta = listaPermisos.stream().anyMatch(permisoUsuario -> permisoUsuario.getPagina().equals(pu.getPagina()));
            if (!esta) {
                listaPermisos.add(pu);
            }
        }
        return esta;
    }

    private void agregaCarpetas(){
        boolean esta = false;
        esta = agregaCarpeta(lblInicio.isVisible(), inicio, esta);
        esta = agregaCarpeta(lblEstados.isVisible(), estados, esta);
        esta = agregaCarpeta(lblConfiguracion.isVisible(), configuracion, esta);
        agregaCarpeta(lblSeguridad.isVisible(), seguridad, esta);
    }

    private void atras() {
        paneCrud.setOpacity(0);
        new FadeInUpTransition(paneTabel).play();
        lblnoVisibles(false);
    }

    public void cargaTabla() {
        UtilCargaTablas.cargaTablaTipoUsuarios(tableData,listaTipoUsuarios);
        tableData.setOnMouseClicked(event -> nuevoGrupo());
    }

    private void errorLog(Exception ex){
        logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
        UtilsGeneral.errorEx(ex);
    }
}
