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
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ShowMessage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class AgregarTrabajadorController implements Initializable {

    @FXML
    private TextField tfNombres;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private ComboBox cbPuesto = new ComboBox();
    
    private ObservableList<Sucursal> listaSucursales;
    private ObservableList<Puesto> listaPuestos;
    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField tfContraseña;
    @FXML
    private ComboBox cbSucursal = new ComboBox();
    
     /**
     * Initializes the controller class.
     */
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarListaPuestos();
        cargarSucursales();
        configurarNombre();;
        configurarTextFielApellidoM();
        configurarTextFielApellidoP();
    }    
    
    private void configurarTextFielApellidoP(){
        Pattern pattern = Pattern.compile("[a-zA-Z ]*");
        
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                return change; // Aceptar cambios válidos (solo letras)
            }
            return null; // Rechazar cambios inválidos
        };
        
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tfApellidoPaterno.setTextFormatter(textFormatter);
    }
    
    private void configurarTextFielApellidoM(){
        Pattern pattern = Pattern.compile("[a-zA-Z ]*");
        
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                return change; // Aceptar cambios válidos (solo letras)
            }
            return null; // Rechazar cambios inválidos
        };
        
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tfApellidoMaterno.setTextFormatter(textFormatter);
    }
    
    private void configurarNombre(){
        Pattern pattern = Pattern.compile("[a-zA-Z ]*");
        
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches()) {
                return change; // Aceptar cambios válidos (solo letras)
            }
            return null; // Rechazar cambios inválidos
        };
        
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        tfNombres.setTextFormatter(textFormatter);
    }

    @FXML
    private void btnRegistrar(ActionEvent event) {
    try {
        String nombre = tfNombres.getText();
        String apellidoPaterno = tfApellidoPaterno.getText();
        String apellidoMaterno = tfApellidoMaterno.getText();
        String usuario = tfUsuario.getText();
        String contraseña = tfContraseña.getText();
        Puesto puesto = (Puesto) cbPuesto.getValue();
        int idPuesto = puesto.getIdPuesto();
        Sucursal sucursal = (Sucursal) cbSucursal.getValue();
        int numSucursal = sucursal.getIdSucursal();

        //Crear nuevo trabajador
        if (nombre.isEmpty()) {
            tfNombres.setStyle("-fx-border-color: red;");
        } else if(usuario.isEmpty()) {
            tfUsuario.setStyle("-fx-border-color: red;");
        } else {
            // Restaurar el estilo del campo de texto
            tfNombres.setStyle("-fx-border-color: -fx-focus-color, -fx-box-border, -fx-control-inner-background;");
            Trabajador nuevoTrabajador = new Trabajador();
            nuevoTrabajador.setNombre(nombre);
            nuevoTrabajador.setApellidoPaterno(apellidoPaterno);
            nuevoTrabajador.setApellidoMaterno(apellidoMaterno);
            nuevoTrabajador.setPuesto(idPuesto);
            nuevoTrabajador.setSucursal(numSucursal);
            nuevoTrabajador.setUsuario(usuario);
            nuevoTrabajador.setContraseña(contraseña);
            guardarTrabajador(nuevoTrabajador);
        }
    } catch (NullPointerException e) {
        ShowMessage.showAlertSimple("Campos faltantes",
                "Faltan campos en el formulario", Alert.AlertType.WARNING);
    }
}
    
    private void guardarTrabajador(Trabajador nuevoTrabajador){
        try {
            ResultadoOperacion resultado = TrabajadorDAO.registrarTrabajador(nuevoTrabajador);
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Exito",
                    "Usuario registrado", Alert.AlertType.WARNING);
                tfApellidoMaterno.getScene().getWindow();
                regresarGestionTrabajadores();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void cerrarVentana(){
        Stage escenarioPrincipal = (Stage) tfUsuario.getScene().getWindow();
        escenarioPrincipal.close();
        
    }

    @FXML
    private void ivVolver(MouseEvent event) {
        
        ShowMessage.showAlertSimple("Cancelado", 
                "Registro cancelado", Alert.AlertType.INFORMATION);
        regresarGestionTrabajadores();
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
    
    private void regresarGestionTrabajadores(){
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
