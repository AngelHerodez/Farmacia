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
import static farmacia.AgregarProductoController.noLettersTextField;
import static farmacia.Farmacia.productoActual;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import utils.ShowMessage;

/**
 * FXML Controller class
 *
 * @author simpson
 */
public class ModificarProductoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfPrecio;
    @FXML
    private ComboBox cbTipo = new ComboBox();
    @FXML
    private TextField tfMililitros;
    @FXML
    private ComboBox cbPresentacion = new ComboBox();
    @FXML
    private TextField tfCantidad;

    private ObservableList<Presentacion> listaPresentacion;
    private ObservableList<Tipo> listaTipo;
    
    @FXML
    private DatePicker fechaCaducidad;
    
    private Producto producto;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        noLettersTextField(tfPrecio);
        noLettersTextField(tfMililitros);
        noLettersTextField(tfCantidad);
        
        cargarListaPresentacion();
        cargarListaTipo();
        //llenarCampos();
        //cargarDatos();
        // Crear un TextFormatter con una expresión regular para validar el formato de precio
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^\\d+(\\.\\d{0,2})?$")) {
                return change; // Aceptar cambios válidos de precio
            }
            return null; // Rechazar cambios inválidos
        });
        
        //Aplicar formater a textfield 
        tfPrecio.setTextFormatter(formatter);
        
        // Evento para evitar copiar y pegar valores no válidos
        tfPrecio.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            if (!input.matches("\\d|\\.|\\b")) {
                event.consume(); // Consumir el evento si no es un dígito, punto o tecla de retroceso
            }
        });

        // Crear un TextFormatter con una expresión regular para validar el formato de precio
        TextFormatter<String> formatoFecha = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{1,2}/\\d{1,2}/\\d{1,4}")) {
                return change; // Aceptar cambios válidos de precio
            }
            return null; // Rechazar cambios inválidos
        });
        
        //tfCaducidad.setTextFormatter(formatoFecha);
    }    

    @FXML
    private void ivVolver(MouseEvent event) {
        cerrarVentana();
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if(checkFields()){
            if(LocalDate.now().isBefore(fechaCaducidad.getValue()) ){
                String nombre = tfNombre.getText();
            String textPrecio = tfPrecio.getText();
            String textCantidad = tfCantidad.getText();
            String textMililitros = tfMililitros.getText();
            Tipo tipo = (Tipo)cbTipo.getValue();
            int tipoProducto = tipo.geIdTipo();
            Presentacion presentacion = (Presentacion)cbPresentacion.getValue();
            int presentacionProducto = presentacion.getIdPresentacion();
            System.out.println(presentacionProducto);
            System.out.println(tipoProducto);

            try {

                double precio = Double.parseDouble(textPrecio);
                int cantidad = Integer.parseInt(textCantidad);
                int mililitros = Integer.parseInt(textMililitros);

                //nuevo producto
                Producto nuevoPro = new Producto();
                nuevoPro.setIdProducto(productoActual.getIdProducto());
                nuevoPro.setNombre(nombre);
                nuevoPro.setPrecio(precio);
                nuevoPro.setCantidad(cantidad);
                nuevoPro.setFechaCaducidad(fechaCaducidad.getValue());
                nuevoPro.setTipoProducto(tipoProducto);
                nuevoPro.setIdPresentacion(presentacionProducto);
                nuevoPro.setTamaño(mililitros);

                System.out.println(nuevoPro.getIdPresentacion());
                System.out.println(nuevoPro.getTipoProducto());

                guardarProducto(nuevoPro);
            } catch (NumberFormatException e) {
                ShowMessage.showAlertSimple("Error",
                        "Introduzca un precio valido",
                        Alert.AlertType.WARNING);
            } catch (NullPointerException n){
                ShowMessage.showAlertSimple("Error",
                        "Faltan datos",
                        Alert.AlertType.WARNING);
            }   
            }else{
                ShowMessage.showAlertSimple("Fecha invalida","La fecha de caducidad debe ser porterior a la fecha actual",Alert.AlertType.WARNING);
            }
        }else{
            ShowMessage.showAlertSimple("Datos faltantes","Llene todos los campos por favor",Alert.AlertType.WARNING);
        } 
    }
    
    private void guardarProducto(Producto nuevoPro){
    try {
                ResultadoOperacion resultado = ProductoDAO.modificarProducto(nuevoPro);
                if (!resultado.isError()) {
                    ShowMessage.showAlertSimple("Exito",
                            "Producto modificado",
                            Alert.AlertType.INFORMATION);
                    cerrarVentana();
                }
            } catch (Exception e) {
            }
    }
    private void cerrarVentana(){
        Stage escenario = (Stage)tfNombre.getScene().getWindow();
        escenario.close();
    }
    
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
    
    private void llenarCampos() {
        
        tfNombre.setText(productoActual.getNombre());
        tfPrecio.setText(Double.toString(productoActual.getPrecio()));
        tfCantidad.setText(Integer.toString(productoActual.getCantidad()));
        tfMililitros.setText(Integer.toString(productoActual.getTamaño()));
        
        
    }
    
    private boolean checkFields(){
        return !("".equals(tfNombre.getText()) || "".equals(tfPrecio.getText()) || fechaCaducidad.getValue() == null || "".equals(tfCantidad.getText()) || cbPresentacion.getSelectionModel().getSelectedItem() == null || cbTipo.getSelectionModel().getSelectedItem() == null);
    }
    
    
    public static void noLettersTextField(TextField textField){
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
        String input = event.getCharacter();
                if (!input.matches("[0-9]")) {
                    event.consume();
                }
            });
    }
    
    public void ponerProducto(Producto producto){
        this.producto = producto;
    }
    
    private void cargarDatos(){
        tfNombre.setText(this.producto.getNombre());
        String cantidad = String.valueOf(this.producto.getCantidad());
        String precio = Double.toString(this.producto.getPrecio());
        System.out.println(cantidad);
        System.out.println(precio);
        tfCantidad.setText(cantidad);
        tfPrecio.setText(precio);
    }
}
