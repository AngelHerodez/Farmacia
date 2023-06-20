/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package farmacia;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ShowMessage;

/**
 * FXML Controller class
 *
 * @author Carmona
 */
public class PrincipalAdministradorController implements Initializable {

    @FXML
    private ImageView ivAdministrarPersonal;
    @FXML
    private ImageView ivAdministrarProductos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void ivAdministrarPersonalClic(MouseEvent event) {
        System.out.println("XD");
        try {
            //Abrir la ventana
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/vistas/GestionPersonal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            //Cerrar la ventana actual
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();
            stageActual.close();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error", 
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        
        
       
    }

    @FXML
    private void ivAdministrarProductos(MouseEvent event) {
        try {
            //Abrir la ventana
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/vistas/AdministrarProductos.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            //Cerrar la ventana actual
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();
            stageActual.close();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error", 
                    "No se puede mostrar la ventana",
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
}
