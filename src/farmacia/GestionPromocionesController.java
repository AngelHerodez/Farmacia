/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package farmacia;

import DAO.ProductoDAO;
import DAO.PromocionDAO;
import DAO.SucursalDAO;
import POJO.Producto;
import POJO.Promocion;
import POJO.Sucursal;
import conexionBD.ResultadoOperacion;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.ShowMessage;
import java.util.logging.Logger;

/**
 *
 * @author Angel
 */
public class GestionPromocionesController implements Initializable {
    ObservableList<Promocion> listaPromociones;
    ObservableList<Producto> listaProductos;
    ObservableList<Sucursal> listaSucursales;
    @FXML
    private TableView tablePromociones;
    @FXML
    private TableColumn colId;
    
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colProducto;
    @FXML
    private TableColumn colSucursal;
    @FXML
    private ImageView ivVolver;
    @FXML
    private ImageView ivAgregarPromocion;
    @FXML
    private ImageView ivModificarPromocion;
    @FXML
    private ImageView ivEliminarPromocion;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarListaPromociones();
        cargarListaProductos();
        cargarListaSucursales();
        try{
            configurarTabla();
        } catch (SQLException ex) {
            Logger.getLogger(GestionPromocionesController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    } 
    @FXML
    private void ivAgregarPromocionClic(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/vistas/AgregarPromocion.fxml"));
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
                    "No se puede mostrar "
                            + "la ventana", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void ivModificarPromocionClic(MouseEvent event){
        Promocion promocion = verificarPromocionSeleccioando();
        if (promocion != null) {
            irFormulario(promocion);
         
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();
            stageActual.close();
        }else {
            ShowMessage.showAlertSimple("Seleccionar promocion",
                    "Seleccione una promocion",
                    Alert.AlertType.WARNING);
        }
    }
    
    private void irFormulario(Promocion promocion){
        String ruta = "/vistas/ModificarPromocion.fxml";
        try {
            FXMLLoader controladorAcceso = new FXMLLoader(getClass().getResource(ruta));
            Parent vista = controladorAcceso.load();
            ModificarPromocionController modificarPromocion = controladorAcceso.getController();
            modificarPromocion.iniciarEdicion(promocion);
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
    private void ivEliminarPromocionClic(MouseEvent event){
        Promocion promocionEliminada = verificarPromocionSeleccioando();
        if(promocionEliminada != null){
            boolean eliminar = ShowMessage.showConfirmationDialog("Eliminar promocion", "Desea eliminar la promocion?");
            if(eliminar){
                try{
                    ResultadoOperacion resultado = PromocionDAO.eliminarPromocion(promocionEliminada.getIdPromocion());
                    if(!resultado.isError()){
                        ShowMessage.showAlertSimple("Mensaje", "Promocion eliminada", Alert.AlertType.INFORMATION);
                        FXMLLoader loader = new FXMLLoader(getClass()
                            .getResource("/vistas/GestionPromociones.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
            
            
                        Node source = (Node) event.getSource();
                        Stage stageActual = (Stage) source.getScene().getWindow();
                        stageActual.close();
                    }else{
                        ShowMessage.showAlertSimple("Error", "Error", Alert.AlertType.NONE);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    @FXML
    private void ivVolverclic(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/vistas/PrincipalEncargado.fxml"));
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

    private void configurarTabla() throws SQLException{
        
            this.colId.setCellValueFactory(new PropertyValueFactory<>("idPromocion"));
            this.colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            this.colProducto.setCellValueFactory(new PropertyValueFactory<>("consultaProducto"));
            this.colSucursal.setCellValueFactory(new PropertyValueFactory<>("consultaSucursal"));
            
            List<Promocion> promo = PromocionDAO.obtenerPromocion();
            
            ObservableList<Promocion> list = FXCollections.observableArrayList(promo);
            tablePromociones.setItems(list);
       
    }

    

    private Promocion verificarPromocionSeleccioando(){
        int filaSeleccionada = tablePromociones.getSelectionModel().getSelectedIndex();
        return (filaSeleccionada > -1) ? listaPromociones.get(filaSeleccionada):null;
    }

    private void cargarListaProductos() {
        try {
            listaProductos = FXCollections.observableArrayList();
            ArrayList<Producto> listaProducto = ProductoDAO.obtenerMedicamentos();
            listaProductos.addAll(listaProducto);
            tablePromociones.setItems(listaProductos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarListaSucursales() {
        try {
            listaSucursales = FXCollections.observableArrayList();
            ArrayList<Sucursal> listaSucursal = SucursalDAO.obtenerSucursa();
            listaSucursales.addAll(listaSucursal);
            tablePromociones.setItems(listaSucursales);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarListaPromociones() {
        try {
            listaPromociones = FXCollections.observableArrayList();
            ArrayList<Promocion> list = PromocionDAO.obtenerPromocion();
            listaPromociones.addAll(list);
            tablePromociones.setItems(listaPromociones);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
