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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ShowMessage;

/**
 *
 * @author Angel
 */
public class ModificarPromocionController implements Initializable{
    
    @FXML
    private TextField tfDescripcion;
    @FXML
    private ComboBox cbSucursal = new ComboBox();
    @FXML
    private ComboBox cbProducto = new ComboBox();
    
    private Promocion promocionEditable;
    

    ObservableList<Producto> listaProductos;
    ObservableList<Sucursal> listaSucursales;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarListaProductos();
        cargarListaSucursales();
    }    
    
    private void iniciarDatos(){
        
        tfDescripcion.setText(promocionEditable.getDescripcion());
        cbProducto.setValue(promocionEditable.getConsultaProducto());
        cbSucursal.setValue(promocionEditable.getConsultaSucursal());
    }
    
    public void iniciarEdicion(Promocion promocion){
        this.promocionEditable = promocion;
        if(promocion != null){
            iniciarDatos();
        }
    }
    
    @FXML
    private void btnRegistrar(ActionEvent event){
        try {
            
            String descripcion = tfDescripcion.getText();
            Producto producto = (Producto)cbProducto.getValue();
            int idProducto = producto.getIdProducto();
            Sucursal sucursal = (Sucursal)cbSucursal.getValue();
            int idSucursal = sucursal.getIdSucursal();            
           
            promocionEditable.setDescripcion(descripcion);
            promocionEditable.setProducto(idProducto);
            promocionEditable.setSucursal(idSucursal);
            
            if (descripcion.isEmpty()) {
                tfDescripcion.setStyle("-fx-border-color: red; ");
            }if(cbProducto == null) {
                ShowMessage.showAlertSimple("Mensaje",
                        "Faltan datos en el formulario",
                        Alert.AlertType.WARNING);       
            }
            else {
             guardarEdicion(promocionEditable);
            }
            
        } catch (NullPointerException e) {
            ShowMessage.showAlertSimple("Mensaje",
                        "Faltan datos en el formulario",
                        Alert.AlertType.WARNING);       
        }
    }
    
    private void guardarEdicion(Promocion promocionEditada) {
        try{
            ResultadoOperacion resultado = PromocionDAO.
                    guardarEdicion(promocionEditada);
            if(!resultado.isError()){
                ShowMessage.showAlertSimple("Mensaje",
                        resultado.getMessage(),
                        Alert.AlertType.INFORMATION);
                RegresarPantallaGestora();
            }
        }catch(NullPointerException n){
            ShowMessage.showAlertSimple("Faltan datos", "Faltan datos", Alert.AlertType.NONE);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage)tfDescripcion.getScene().getWindow();
        escenario.close();
    }
    
    @FXML
    private void ivVolver(MouseEvent event){
        ShowMessage.showAlertSimple("Cancelado", 
                "Registro cancelado", Alert.AlertType.INFORMATION);
        RegresarPantallaGestora();
    }
    
    

    private void cargarListaProductos() {
        listaProductos = FXCollections.observableArrayList();
        try {
            ArrayList<Producto> productoBD = ProductoDAO.obtenerMedicamentos();
            listaProductos.addAll(productoBD);
            cbProducto.setItems(listaProductos);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void cargarListaSucursales() {
        listaSucursales = FXCollections.observableArrayList();
        try {
            ArrayList<Sucursal> sucursalBD = SucursalDAO.obtenerSucursa();
            listaSucursales.addAll(sucursalBD);
            cbSucursal.setItems(listaSucursales);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void RegresarPantallaGestora() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/vistas/GestionPromociones.fxml"));
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
