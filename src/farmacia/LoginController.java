/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package farmacia;

import DAO.TrabajadorDAO;
import POJO.Trabajador;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.ShowMessage;

/**
 *
 * @author Carmona
 */
public class LoginController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button button;
    @FXML
    private Label lbContraseña;
    @FXML
    private PasswordField tfContraseña;
    @FXML
    private TextField tfUsuario;
    @FXML
    private Label lbUsuario;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnIniciarSesion(ActionEvent event) {
        String usuario = tfUsuario.getText();
        String contraseña = tfContraseña.getText();
        
        if (tfUsuario.getText().isEmpty()) {
            lbUsuario.setText("Introduza su usuario");
        }else if(tfContraseña.getText().isEmpty()){
            lbContraseña.setText("Introduzca su contraseña");
        }else{
            //Llamar al DAO
            accederSistema(usuario, contraseña);
        }
    }
    
    private void accederSistema(String usuario, String contraseña){
        try {
            Trabajador sesion = TrabajadorDAO.iniciarSesion(usuario, contraseña);
            if (sesion != null && sesion.getIdTrabajador()>0 && sesion.getPuesto() == 1) {
                //ir pantalla principal administrador
                irPantallaPrincipalAdministrador(sesion.toString());
            }else if(sesion != null && sesion.getIdTrabajador()>0 && sesion.getPuesto() == 2){
                //ir pantalla encargado
                irPantallaPrincipalEncargado(sesion.toString());
            }else{
                ShowMessage.showAlertSimple("Información incorrecta", 
                        "Usuario y/o contraseña incorrectos", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void irPantallaPrincipalAdministrador(String nombre){
        try {
            ShowMessage.showAlertSimple(
                    "Bienvenido(a)", 
                    "Credenciales correctas, Bienvenido(a) administrador(a) "+nombre+" al sistema", 
                    Alert.AlertType.INFORMATION);
            Parent vista = FXMLLoader.load(getClass().getResource("/vistas/PrincipalAdministrador.fxml"));
            Scene escenaPrincipal = new Scene(vista);
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowMessage.showAlertSimple(
                    "Error", 
                    "No se puede mostrar la pantalla principal", 
                    Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipalEncargado(String nombre){
        try {
            ShowMessage.showAlertSimple(
                    "Bienvenido(a)", 
                    "Credenciales correctas, Bienvenido(a) encargado(a) "+nombre+" al sistema", 
                    Alert.AlertType.INFORMATION);
            Parent vista = FXMLLoader.load(getClass().getResource("/vistas/PrincipalEncargado.fxml"));
            Scene escenaPrincipal = new Scene(vista);
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowMessage.showAlertSimple(
                    "Error", 
                    "No se puede mostrar la pantalla principal", 
                    Alert.AlertType.ERROR);
        }
    }
}
