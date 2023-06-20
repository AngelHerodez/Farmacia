/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package farmacia;

import DAO.PuestoDAO;
import DAO.SucursalDAO;
import DAO.TrabajadorDAO;
import POJO.Puesto;
import POJO.Sucursal;
import POJO.Trabajador;
import conexionBD.ResultadoOperacion;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ShowMessage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class ModificarTrabajadorController implements Initializable {

    @FXML
    private TextField tfNombres;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private ComboBox cbSucursal = new ComboBox();
    @FXML
    private ComboBox cbPuesto = new ComboBox();
    
    private Trabajador trabajadorEditable;
    private ObservableList<Sucursal> listaSucursales;
    private ObservableList<Puesto> listaPuestos;
  
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarListaPuestos();
        cargarSucursales();
    }    

    @FXML
    private void ivVolver(MouseEvent event) {
        ShowMessage.showAlertSimple("Mensaje", 
                "Edicion cancelada", Alert.AlertType.INFORMATION);
        RegresarPantallaGestora();
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        try {
            String nombre = tfNombres.getText();
            String apellidoPaterno = tfApellidoPaterno.getText();
            String apellidoMaterno = tfApellidoMaterno.getText();
            Puesto puesto = (Puesto)cbPuesto.getValue();
            int idPuesto = puesto.getIdPuesto();
            Sucursal sucursal = (Sucursal)cbSucursal.getValue();
            int idSucursal = sucursal.getIdSucursal();            
            trabajadorEditable.setNombre(nombre);
            trabajadorEditable.setApellidoPaterno(apellidoPaterno);
            trabajadorEditable.setApellidoMaterno(apellidoMaterno);
            trabajadorEditable.setPuesto(idPuesto);
            trabajadorEditable.setSucursal(idSucursal);
            guardarEdicion(trabajadorEditable);
        } catch (NullPointerException e) {
            ShowMessage.showAlertSimple("Mensaje",
                        "Faltan datos en el formulario",
                        Alert.AlertType.WARNING);       
        }
        
    }
    
    public void iniciarEdicion(Trabajador trabajador){
        this.trabajadorEditable = trabajador;
        if (trabajador != null) {
            iniciarDatos();
        }
    }
    
    private void iniciarDatos(){
        tfNombres.setText(trabajadorEditable.getNombre());
        tfApellidoPaterno.setText(trabajadorEditable.getApellidoPaterno());
        tfApellidoMaterno.setText(trabajadorEditable.getApellidoMaterno());
    }
    
    private void guardarEdicion(Trabajador trabajadorEditado){
        try {
            ResultadoOperacion resultado = TrabajadorDAO
                    .guardarEdicion(trabajadorEditado);
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Mensaje",
                        resultado.getMessage(),
                        Alert.AlertType.INFORMATION);
                RegresarPantallaGestora();
            }
        } catch(NullPointerException n) {
            ShowMessage.showAlertSimple("Faltan datos",
                    "Faltan datos", Alert.AlertType.WARNING);      
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage)tfApellidoMaterno.getScene().getWindow();
        escenario.close();
    }
    
    private void cargarListaPuestos(){
        listaPuestos = FXCollections.observableArrayList();
        try {
            ArrayList<Puesto> puestoBD = PuestoDAO.obtenerPuestos();
            listaPuestos.addAll(puestoBD);
            cbPuesto.setItems(listaPuestos);    
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    public void cargarSucursales(){
        listaSucursales = FXCollections.observableArrayList();
        try {
            ArrayList<Sucursal> sucursalBD = SucursalDAO.obtenerSucursa();
            listaSucursales.addAll(sucursalBD);
            cbSucursal.setItems(listaSucursales);
            
        } catch (Exception e) {
        }
    }
    
    private void RegresarPantallaGestora() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/vistas/GestionPersonal.fxml"));
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
    
}
