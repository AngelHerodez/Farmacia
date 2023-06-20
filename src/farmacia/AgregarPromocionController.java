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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ShowMessage;

/**
 *
 * @author Angel
 */
public class AgregarPromocionController implements Initializable{
    @FXML
    private TextField tfDescripcion;
    @FXML
    private ComboBox cbProducto = new ComboBox(); 
    @FXML
    private ComboBox cbSucursal = new ComboBox();
    @FXML
    private ImageView ivVolver;
    
    private ObservableList<Producto> listaProductos;
    private ObservableList<Sucursal> listaSucursales;
    private ObservableList<Promocion> listaPromociones;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarListaProductos();
        cargarListaSucursales();
    }
    @FXML
    private void ivVolver(MouseEvent event) {
        ShowMessage.showAlertSimple("Cancelado", 
                "Registro cancelado", Alert.AlertType.INFORMATION);
        RegresarPantallaGestora();
    }
    
    
    @FXML
    private void btnRegistrar(ActionEvent event) {
        try {
            
            String descripcion = tfDescripcion.getText();
            
            Producto producto = (Producto) cbProducto.getValue();
            int idProducto = producto.getIdProducto();
            
            Sucursal sucursal = (Sucursal) cbSucursal.getValue();
            int idSucursal = sucursal.getIdSucursal();
            
            
            if (descripcion.isEmpty()) {
                tfDescripcion.setStyle("-fx-border-color: red; ");
            } else {
                //Crear nueva promocion
                Promocion  nuevaPromocion = new Promocion();
                
                nuevaPromocion.setDescripcion(descripcion);
                nuevaPromocion.setProducto(idProducto);
                nuevaPromocion.setSucursal(idSucursal);
                guardarPromocion(nuevaPromocion);   
            }
        } catch (NullPointerException e) {
            ShowMessage.showAlertSimple("Campos faltantes",
                    "Faltan campos en el formulario", Alert.AlertType.WARNING);
        } 
    }

    

    private void guardarPromocion(Promocion nuevaPromocion) {
            try {
               ResultadoOperacion resultado = PromocionDAO.registrarPromocion(nuevaPromocion);
                if (!resultado.isError()) {
                    ShowMessage.showAlertSimple("Exito",
                        "Promocion registrada", Alert.AlertType.WARNING);
                    cbProducto.getScene().getWindow();
                    RegresarPantallaGestora();   
                }   
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
    }    
    
    private void cerrarVentana(){
        Stage escenarioPrincipal = (Stage) cbProducto.getScene().getWindow();
        escenarioPrincipal.close();
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

    /*private boolean existePromocion(Promocion nuevaPromocion) {
        return listaPromociones.contains(nuevaPromocion);
    }*/

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
