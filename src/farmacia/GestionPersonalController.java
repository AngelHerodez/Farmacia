/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package farmacia;


import DAO.TrabajadorDAO;
import POJO.Trabajador;
import conexionBD.ResultadoOperacion;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.ShowMessage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class GestionPersonalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    ObservableList<Trabajador> listaTrabajadores;
    @FXML
    private TableView tableTrabajadores;
    @FXML
    private TableColumn colID;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    @FXML
    private TableColumn colPuesto;
    private ImageView ivCode;
    @FXML
    private Button idBtnAgregarTrabajador;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabla();
        cargarListaTrabajadores();
    }    

    private void ivAgregarUsuario(MouseEvent event) {
        irCrearTrabajador();
    }
    
    private void irCrearTrabajador(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/vistas/AgregarTrabajador.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            cerrarVentana();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) idBtnAgregarTrabajador.getScene().getWindow();
        escenario.close();
    }

    @FXML
    private void ivModificarUsuario(MouseEvent event) {
        Trabajador trabajador = verificarTrabajadorSeleccioando();
        if (trabajador != null) {
            irFormulario(trabajador);
            
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();
            stageActual.close();
        }else {
            ShowMessage.showAlertSimple("Seleccionar trabajador",
                    "Seleccione un trabajador",
                    Alert.AlertType.WARNING);
        }
    }
    
    private void irFormulario(Trabajador trabajador){
        String ruta = "/vistas/ModificarTrabajador.fxml";
        try {
            FXMLLoader controladorAcceso = new FXMLLoader(getClass()
                    .getResource(ruta));
            Parent vista = controladorAcceso.load();
            ModificarTrabajadorController modificarTrabajador = controladorAcceso
                    .getController();
            modificarTrabajador.iniciarEdicion(trabajador);
            Scene escenaEdicion = new Scene(vista);
            Stage escenarioNuevo = new Stage();
            escenarioNuevo.setScene(escenaEdicion);
            escenarioNuevo.initModality(Modality.APPLICATION_MODAL);
            escenarioNuevo.showAndWait();  

        } catch (IOException e) {
            ShowMessage.showAlertSimple("Error",
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void ivEliminarUsuario(MouseEvent event) {
        Trabajador trabajadorEliminado = verificarTrabajadorSeleccioando();
        if (trabajadorEliminado != null) {
            boolean eliminar = ShowMessage.showConfirmationDialog("Eliminar usuario",
                    "¿Desea eliminar el usuario?");
            if (eliminar) {
                try {
                    ResultadoOperacion resultado = TrabajadorDAO.eliminarTrabajador(trabajadorEliminado
                            .getIdTrabajador());
                    if (!resultado.isError()) {
                        ShowMessage.showAlertSimple("Mensaje", 
                                "Usuario eliminado", Alert.AlertType.INFORMATION);
                        cargarListaTrabajadores();
                    }else {
                        ShowMessage.showAlertSimple("Error", "Error",
                                Alert.AlertType.WARNING);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    @FXML
    private void ivVolver(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/vistas/PrincipalAdministrador.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();
            stageActual.close();
        } catch (Exception e) {
        }
    }
    
    public void cargarListaTrabajadores(){
        try {
            listaTrabajadores = FXCollections.observableArrayList();
            ArrayList<Trabajador> listaTrabajador = TrabajadorDAO.obtenerTrabajadores();
            listaTrabajadores.addAll(listaTrabajador);
            tableTrabajadores.setItems(listaTrabajadores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    private void configurarTabla(){
        try {
            colID.setCellValueFactory(new PropertyValueFactory<>("idTrabajador"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
            colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
            colPuesto.setCellValueFactory(new PropertyValueFactory<>("puesto"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Trabajador verificarTrabajadorSeleccioando(){
        int filaSeleccionada = tableTrabajadores.getSelectionModel().getSelectedIndex();
        return (filaSeleccionada > 0) ? listaTrabajadores.get(filaSeleccionada):null;
    }

    @FXML
    private void btnAgregarTrabajador(ActionEvent event) {
        irCrearTrabajador();
        cerrarVentana();
    }
}
