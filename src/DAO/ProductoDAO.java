/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import POJO.Producto;
import conexionBD.ResultadoOperacion;
import java.sql.Connection;
import java.sql.SQLException;
import conexionBD.ConexionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import utils.ShowMessage;

/**
 *
 * @author Carmona
 */
public class ProductoDAO {
    public static ResultadoOperacion registrarProducto(Producto nuevoPro) throws SQLException{
        System.out.println("entrooooo");
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(true);
        resultado.setNumberRowsAffected(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null ) {
            try {
                String consulta = "INSERT INTO producto(nombre, fechaCaducidad, precio,"
                        + "tipo, tamaño, presentacion, cantidad) "
                        + "VALUES (?,?,?,?,?,?,?)";
                PreparedStatement setPro = conexionBD.prepareStatement(consulta);
                setPro.setString(1, nuevoPro.getNombre());
                setPro.setString(2, nuevoPro.getFechaCaducidad().format(DateTimeFormatter.ofPattern("yyyy-LL-dd")));
                setPro.setDouble(3, nuevoPro.getPrecio());
                setPro.setInt(4, nuevoPro.getTipoProducto());
                setPro.setInt(5, nuevoPro.getTamaño());
                setPro.setInt(6, nuevoPro.getIdPresentacion());
                setPro.setInt(7, nuevoPro.getCantidad());
                int filasAfectadas = setPro.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Producto agregado");
                }else{
                    resultado.setMessage("Error al agregar el producto");
                }
            } catch (SQLException ex) {
                System.out.println("Entro catch");
                System.out.println("Tipo de excepción: " + ex.getClass().getName());
                if (ex.getErrorCode() == 1062) {
                    ShowMessage.showAlertSimple("Medicamento existente",
                        "Este medicamento ya esta registrado.",
                        Alert.AlertType.INFORMATION);
                }
                ex.printStackTrace();
            } catch (NullPointerException n) {
                n.printStackTrace();
            } finally {
                conexionBD.close();
            }   
        }else {
            System.out.println("No hay conexión");
        }
        return resultado;
    }
    
    public static ResultadoOperacion modificarProducto(Producto nuevoPro) throws SQLException{
        System.out.println("entro update");
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(true);
        resultado.setNumberRowsAffected(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null ) {
            try {
                System.out.println("Id dao:"+nuevoPro.getIdProducto());
                String consulta = "UPDATE producto SET nombre = ?, fechaCaducidad = ?, precio = ?,"
                        + " tipo = ?, tamaño = ?, presentacion = ?, cantidad = ?"
                        + " WHERE idProducto = ?;";
                System.out.println("entro try");
                PreparedStatement setPro = conexionBD.prepareStatement(consulta);
                setPro.setString(1, nuevoPro.getNombre());
                setPro.setString(2, nuevoPro.getFechaCaducidad().format(DateTimeFormatter.ofPattern("yyyy-LL-dd")));
                setPro.setDouble(3, nuevoPro.getPrecio());
                setPro.setInt(4, nuevoPro.getTipoProducto());
                setPro.setInt(5, nuevoPro.getTamaño());
                setPro.setInt(6, nuevoPro.getIdPresentacion());
                setPro.setInt(7, nuevoPro.getCantidad());
                setPro.setInt(8, nuevoPro.getIdProducto());
                int filasAfectadas = setPro.executeUpdate();
                System.out.println("filas:"+filasAfectadas);
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Producto agregado");
                }else{
                    resultado.setMessage("Error al agregar el producto");
                }
                System.out.println("paso");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                n.printStackTrace();
            } finally {
                conexionBD.close();
            }   
        }else {
            System.out.println("No hay conexión");
        }
        return resultado;
    }
    
    
    public static ResultadoOperacion eliminarProducto(Producto nuevoPro) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(true);
        resultado.setNumberRowsAffected(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null ) {
            try {
                String consulta = "DELETE FROM producto WHERE idProducto = ?";
                System.out.println("entro try");
                PreparedStatement setPro = conexionBD.prepareStatement(consulta);
                setPro.setInt(1, nuevoPro.getIdProducto());
                int filasAfectadas = setPro.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Producto agregado");
                }else{
                    resultado.setMessage("Error al agregar el producto");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                n.printStackTrace();
            } finally {
                conexionBD.close();
            }   
        }else {
            System.out.println("No hay conexión");
        }
        return resultado;
    }
    
    public static ArrayList<Producto> obtenerMedicamentos() throws SQLException{
        ArrayList<Producto> lista = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT p.idProducto, p.nombre, p.fechaCaducidad, pr.presentacion, p.tamaño, t.tipo FROM producto p\n" +
                                    "inner join tipo t on p.tipo = t.idtipo\n" +
                                    "inner join presentacion pr on p.presentacion = pr.idpresentacion;";
                PreparedStatement obtener = conexionBD.prepareStatement(consulta);
                ResultSet resultado = obtener.executeQuery();
                lista = new ArrayList<>();
                while (resultado.next()) {                    
                    Producto producto = new Producto();
                    producto.setIdProducto(resultado.getInt("idProducto"));
                    producto.setNombre(resultado.getString("nombre"));
                    producto.setFechaCaducidad(resultado.getObject("fechaCaducidad", LocalDate.class));
                    producto.setTamaño(resultado.getInt("tamaño"));
                    producto.setConsultPresentacion(resultado.getString("presentacion"));
                    producto.setConsulttipoProducto(resultado.getString("tipo"));
                    lista.add(producto);      
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        } else{
            System.out.println("Sin conexion");
        }
        return lista;
    }
    
    public static ArrayList<Producto> obteneHigiene() throws SQLException{
        ArrayList<Producto> lista = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idProducto, nombre, fechaCaducidad,"
                        + "presentacion, tamaño, tipo FROM producto WHERE tipo = 2";
                PreparedStatement obtener = conexionBD.prepareStatement(consulta);
                ResultSet resultado = obtener.executeQuery();
                lista = new ArrayList<>();
                while (resultado.next()) {                    
                    Producto producto = new Producto();
                    producto.setIdProducto(resultado.getInt("idProducto"));
                    producto.setNombre(resultado.getString("nombre"));
                    producto.setFechaCaducidad(resultado.getObject("fechaCaducidad", LocalDate.class));
                    producto.setTamaño(resultado.getInt("tamaño"));
                    producto.setIdPresentacion(resultado.getInt("presentacion"));
                    producto.setTipoProducto(resultado.getInt("tipo"));
                    lista.add(producto);      
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        } else{
            System.out.println("Sin conexion");
        }
        return lista;
    }
    
    public static ArrayList<Producto> obtenerMiscelaneos() throws SQLException{
        ArrayList<Producto> lista = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idProducto, nombre, fechaCaducidad,"
                        + "presentacion, tamaño, tipo FROM producto WHERE tipo = 3";
                PreparedStatement obtener = conexionBD.prepareStatement(consulta);
                ResultSet resultado = obtener.executeQuery();
                lista = new ArrayList<>();
                while (resultado.next()) {                    
                    Producto producto = new Producto();
                    producto.setIdProducto(resultado.getInt("idProducto"));
                    producto.setNombre(resultado.getString("nombre"));
                    producto.setFechaCaducidad(resultado.getObject("fechaCaducidad", LocalDate.class));
                    producto.setTamaño(resultado.getInt("tamaño"));
                    producto.setIdPresentacion(resultado.getInt("presentacion"));
                    producto.setTipoProducto(resultado.getInt("tipo"));
                    lista.add(producto);      
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        } else{
            System.out.println("Sin conexion");
        }
        return lista;
    }
    
}
