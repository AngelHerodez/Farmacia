/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package farmacia;

import DAO.PresentacionDAO;
import DAO.ProductoDAO;
import DAO.TipoDAO;
import POJO.Presentacion;
import POJO.Producto;
import POJO.Tipo;
import conexionBD.ResultadoOperacion;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ShowMessage;
import static farmacia.Farmacia.productoActual;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
/**
 * FXML Controller class
 *
 * @author Frank_Simpson
 */
public class AdministrarProductosController implements Initializable {

    @FXML
    private TableView<Producto> tvProductos;
    @FXML
    private TableColumn cmID;
    @FXML
    private TableColumn cmNombre;
    @FXML
    private TableColumn cmPresentacion;
    @FXML
    private TableColumn cmTamaño;
    @FXML
    private TableColumn cmTipo;
    @FXML
    private TableColumn cmCaducidad;
    private ComboBox cbPresentacion = new ComboBox();
    @FXML
    private ComboBox cbTipo = new ComboBox();
    
    private ObservableList<Presentacion> listaPresentacion;
    private ObservableList<Tipo> listaTipo;
    private ObservableList<Producto> listaProductos;
    
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarListaPresentacion();
        cargarListaTipo();
        try {
            llenarTabla();
        } catch (SQLException ex) {
            Logger.getLogger(AdministrarProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//        cargarListaMedicamentos();
//        cbTipo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, 
//                newValue) -> {
//            if (newValue != null) {
//                if (newValue.equals(listaProductos.get(0))) {
//                    cargarListaMedicamentos();
//                    
//                } else if(newValue.equals(listaProductos.get(1))) {
//                    cargarListaMedicamentos();
//                    
//                } else if(newValue.equals(listaProductos.get(2))){
//                    
//                }else {
//                    
//                }
//            }
//        });
//    }
    
    public void llenarTabla() throws SQLException{

            this.cmID.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
            this.cmNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            this.cmPresentacion.setCellValueFactory(new PropertyValueFactory<>("consultPresentacion"));
            this.cmTamaño.setCellValueFactory(new PropertyValueFactory<>("tamaño"));
            this.cmTipo.setCellValueFactory(new PropertyValueFactory<>("consulttipoProducto"));
            this.cmCaducidad.setCellValueFactory(new PropertyValueFactory<>("fechaCaducidad"));
            List<Producto>medicamnetos = ProductoDAO.obtenerMedicamentos();
            
            ObservableList<Producto> list = FXCollections.observableArrayList(medicamnetos);
        tvProductos.setItems(list);

    }
    
//    private void cargarListaMedicamentos(){
//        try {
//            //TODO
//            listaProductos = FXCollections.observableArrayList();
//            ArrayList<Producto> listaProductos2 = ProductoDAO.obtenerMedicamentos();
//            listaProductos.addAll(listaProductos2);
//            tvProductos.setItems(listaProductos);
//            
//        } catch (NullPointerException e) {
//            System.out.println("Error de puntero");
//        } catch (SQLException n){
//            n.printStackTrace();
//        }
//    }
    
    private void cargarListaPresentacion(){
        listaPresentacion = FXCollections.observableArrayList();
        try {
            ArrayList<Presentacion> presentacionBD = PresentacionDAO.obtenerPresentacion();
            listaPresentacion.addAll(presentacionBD);
            cbPresentacion.setItems(listaPresentacion);    
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
    
    private void cargarListaTipo(){
        listaTipo = FXCollections.observableArrayList();
        try {
            ArrayList<Tipo>tipoBD = TipoDAO.obtenerPresentacion();
            listaTipo.addAll(tipoBD);
            cbTipo.setItems(listaTipo);    
        } catch (Exception e) {
            e.printStackTrace();
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

    @FXML
    private void ivEliminarProducto(MouseEvent event) throws SQLException {
        if (tvProductos.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Producto");
            alert.setHeaderText("Estas eliminando el producto seleccionado");
            alert.setContentText("¿Quieres continuar?");
            ResultadoOperacion resultado = null;
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                productoActual = (Producto) tvProductos.getSelectionModel().getSelectedItem();
                resultado = ProductoDAO.eliminarProducto(productoActual);
            }
            
            if (!resultado.isError()) {
                ShowMessage.showAlertSimple("Exito",
                        "Producto eliminado",
                        Alert.AlertType.INFORMATION);
            }
            llenarTabla();
        }else {
            ShowMessage.showAlertSimple("Prodcuto no seleccionado","Seleccione un producto para eliminarlo", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void ivAñadirProducto(MouseEvent event) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/vistas/agregarProducto.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            ShowMessage.showAlertSimple("Error", 
                    "No se puede mostrar "
                            + "la ventana", Alert.AlertType.ERROR);
        }
        System.out.println("llena");
        llenarTabla();
    }

    @FXML
    private void ivModificarProducto(MouseEvent event) throws SQLException {
        if (tvProductos.getSelectionModel().getSelectedItem() != null) {
            productoActual = (Producto) tvProductos.getSelectionModel().getSelectedItem();
            System.out.println("Iddd: "+productoActual.getIdProducto());
            try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/vistas/ModificarProducto.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            ModificarProductoController controlador =loader.getController();
            controlador.ponerProducto(productoActual);
                if (productoActual != null) {
                    System.out.println("Se paso un producto");
                } else {
                    System.out.println("Se paso un producto");
                }
            
            } catch (Exception e) {
                ShowMessage.showAlertSimple("Error", 
                        "No se puede mostrar "
                                + "la ventana", Alert.AlertType.ERROR);
            }   
        }else {
            ShowMessage.showAlertSimple("Prodcuto no seleccionado","Seleccione un producto para modificarlo", Alert.AlertType.WARNING);
        }
    }
    
     @FXML
    void eventActulizar(ActionEvent event) throws SQLException {
        llenarTabla();
    }
}
