/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import POJO.Promocion;
import conexionBD.ConexionBD;
import conexionBD.ResultadoOperacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import utils.ShowMessage;

/**
 *
 * @author Angel
 */
public class PromocionDAO {
    
    public static ArrayList<Promocion> obtenerPromocion () throws SQLException{
        ArrayList<Promocion>  lista = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null){
            try{
                String consulta = "select a.idPromocion, a.descripcion, pr.nombre, s.nombre \n" +
                                    "from promocion as a\n" +
                                    "inner join producto as pr\n" +
                                    "on a.producto = pr.idProducto\n" +
                                    "inner join sucursal as s\n" +
                                    "on a.sucursal = s.idSucursal;";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultadoConsulta = prepararConsulta.executeQuery();
                lista = new ArrayList<>();
                while (resultadoConsulta.next()) {                    
                    Promocion promocionObj = new Promocion();
                    promocionObj.setIdPromocion(resultadoConsulta.getInt("idPromocion"));
                    promocionObj.setDescripcion(resultadoConsulta.getString("descripcion"));
                    promocionObj.setConsultaProducto(resultadoConsulta.getString("pr.nombre"));
                    promocionObj.setConsultaSucursal(resultadoConsulta.getString("s.nombre"));
                    lista.add(promocionObj);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PromocionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                conexionBD.close();
            }
        }else{
            System.out.println("No hay conexión");
        }
        return lista;
    }
    
    public static ResultadoOperacion registrarPromocion (Promocion nuevaPromocion) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(true);
        resultado.setNumberRowsAffected(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "INSERT INTO promocion( descripcion, producto, sucursal) VALUES (?,?,?)";
                PreparedStatement setPromocion = conexionBD.prepareStatement(consulta);
                setPromocion.setString(1, nuevaPromocion.getDescripcion());
                setPromocion.setInt(2, nuevaPromocion.getProducto());
                setPromocion.setInt(3, nuevaPromocion.getSucursal());

                int filasAfectadas = setPromocion.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Promocion agregado");
                    resultado.setNumberRowsAffected(filasAfectadas);
                }else {
                    resultado.setMessage("Error al agregar la promocion");
                }
            } catch (NullPointerException n) {
                ShowMessage.showAlertSimple("Campos faltantes",
                    "Faltan campos en el formulario", Alert.AlertType.WARNING);
            }catch (SQLException e) {
                e.printStackTrace();
                ShowMessage.showAlertSimple("promocion ya registrada",
                        "La promocion ya existe",
                        Alert.AlertType.ERROR);
            }finally {
                conexionBD.close();
            }
        }else{
            resultado.setMessage("No hay conexión");
        }
        return resultado;
    }
    
    public static ResultadoOperacion eliminarPromocion(int idPromocion) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(true);
        resultado.setNumberRowsAffected(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String consulta = "DELETE FROM promocion WHERE idPromocion = ?";
                PreparedStatement eliminarPromocion = conexionBD.prepareStatement(consulta);
                eliminarPromocion.setInt(1, idPromocion);
                int rowAffected = eliminarPromocion.executeUpdate();
                if (rowAffected > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Promocion eliminada correctamente");
                    resultado.setNumberRowsAffected(rowAffected);
            }else {
                resultado.setMessage("No se pudo eliminar la promocion");
            }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return resultado;
    }
    
    public static ResultadoOperacion guardarEdicion(Promocion promocion) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE promocion SET descripcion = ?, producto = ?, sucursal = ? "
                        + "WHERE idPromocion = ?";
                PreparedStatement editar = conexionBD.prepareStatement(query);
            
                editar.setString(1, promocion.getDescripcion());
                editar.setInt(2, promocion.getProducto());
                editar.setInt(3, promocion.getSucursal());
                editar.setInt(4, promocion.getIdPromocion());
                int filasAfectadas = editar.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Promocion editado");
                    resultado.setNumberRowsAffected(filasAfectadas);
                }else{
                    resultado.setMessage("Error al editar");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                ShowMessage.showAlertSimple("promocion ya registrada",
                        "La promocion ya existe",
                        Alert.AlertType.ERROR);
            }finally {
                conexionBD.close();
            }
            
        }else{
            ShowMessage.showAlertSimple("Sin conexión",
                    "No hay conexión con la BD",
                    Alert.AlertType.ERROR);
        }
        return resultado;
    }
}
