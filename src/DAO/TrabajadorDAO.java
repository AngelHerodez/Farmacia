/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import POJO.Trabajador;
import java.sql.Connection;
import java.sql.SQLException;
import conexionBD.ConexionBD;
import conexionBD.ResultadoOperacion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import utils.ShowMessage;

/**
 *
 * @author Carmona
 */
public class TrabajadorDAO {
    public static Trabajador iniciarSesion(String usuario, 
            String contraseña)throws SQLException{
        Trabajador sesion = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idTrabajador, nombre, apellidoPaterno,"
                        + " apellidoMaterno, puesto, sucursal FROM trabajador "
                        + "WHERE usuario = ? AND contraseña = ?";
                PreparedStatement consultaLogin = conexionBD.prepareStatement(consulta);
                consultaLogin.setString(1, usuario);
                consultaLogin.setString(2, contraseña);
                ResultSet resultadoConsulta = consultaLogin.executeQuery();
                sesion = new Trabajador();
                if (resultadoConsulta.next()) {
                    sesion.setIdTrabajador(resultadoConsulta.getInt("idTrabajador"));
                    sesion.setNombre(resultadoConsulta.getString("nombre"));
                    sesion.setApellidoPaterno(resultadoConsulta.getString("apellidoPaterno"));
                    sesion.setApellidoMaterno(resultadoConsulta.getString("apellidoMaterno"));
                    sesion.setPuesto(resultadoConsulta.getInt("puesto"));
                    sesion.setSucursal(resultadoConsulta.getInt("sucursal"));
                }else{
                    sesion.setIdTrabajador(-1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
            
        }
        return sesion;
    }
    
    public static ResultadoOperacion registrarTrabajador(Trabajador nuevoTrabajador) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(true);
        resultado.setNumberRowsAffected(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "INSERT INTO trabajador(nombre, apellidoPaterno, apellidoMaterno, puesto, "
                        + "sucursal, usuario, contraseña) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement setTrabajador = conexionBD.prepareStatement(consulta);
                setTrabajador.setString(1, nuevoTrabajador.getNombre());
                setTrabajador.setString(2, nuevoTrabajador.getApellidoPaterno());
                setTrabajador.setString(3, nuevoTrabajador.getApellidoMaterno());
                setTrabajador.setInt(4, nuevoTrabajador.getPuesto());
                setTrabajador.setInt(5, nuevoTrabajador.getSucursal());
                setTrabajador.setString(6, nuevoTrabajador.getUsuario());
                setTrabajador.setString(7, nuevoTrabajador.getContraseña());
                int filasAfectadas = setTrabajador.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Trabajador agregado");
                    resultado.setNumberRowsAffected(filasAfectadas);
                }else {
                    resultado.setMessage("Error al agregar al usuario");        
                }    
            }catch (NullPointerException n) {
                ShowMessage.showAlertSimple("Campos faltantes",
                    "Faltan campos en el formulario", Alert.AlertType.WARNING);
            }catch (SQLException e) {
                e.printStackTrace();
                ShowMessage.showAlertSimple("Usuario",
                        "El usuario ya existe",
                        Alert.AlertType.ERROR);
            }finally {
                conexionBD.close();
            }
        }else{
            resultado.setMessage("No hay conexión");
        }
        return resultado;
    }
    
    public static ArrayList<Trabajador> obtenerTrabajadores () throws SQLException{
        ArrayList<Trabajador> lista = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idTrabajador, nombre, apellidoPaterno,"
                    + "apellidoMaterno, puesto FROM trabajador";
                PreparedStatement consultaLista = conexionBD.prepareStatement(consulta);
                ResultSet resultado = consultaLista.executeQuery();
                lista = new ArrayList<>();
                while (resultado.next()) {                
                    Trabajador trabajador = new Trabajador();
                    trabajador.setIdTrabajador(resultado.getInt("idTrabajador"));
                    trabajador.setNombre(resultado.getString("nombre"));
                    trabajador.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    trabajador.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    trabajador.setPuesto(resultado.getInt("puesto"));
                    lista.add(trabajador);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }catch (NullPointerException n){
                n.printStackTrace();;
            }finally{
            conexionBD.close();
            }
        }else{
            System.out.println("No hay conexión");
        }
        return lista;
    }
    
    public static ResultadoOperacion eliminarTrabajador(int idTrabajador) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(true);
        resultado.setNumberRowsAffected(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String consulta = "DELETE FROM trabajador WHERE idTrabajador = ?";
                PreparedStatement eliminarUsuario = conexionBD.prepareStatement(consulta);
                eliminarUsuario.setInt(1, idTrabajador);
                int rowAffected = eliminarUsuario.executeUpdate();
                if (rowAffected > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Trabajador eliminado correctamente");
                    resultado.setNumberRowsAffected(rowAffected);
            }else {
                resultado.setMessage("No se pudo eliminar el usuario");
            }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return resultado;
    }
    
    public static ResultadoOperacion guardarEdicion(Trabajador trabajador) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE trabajador SET nombre = ?, apellidoPaterno = ?, "
                        + "apellidoMaterno = ?, puesto = ?, sucursal = ? "
                        + "WHERE idTrabajador = ?";
                PreparedStatement editar = conexionBD.prepareStatement(query);
                editar.setString(1, trabajador.getNombre());
                editar.setString(2, trabajador.getApellidoPaterno());
                editar.setString(3, trabajador.getApellidoMaterno());
                editar.setInt(4, trabajador.getPuesto());
                editar.setInt(5, trabajador.getSucursal());
                editar.setInt(6, trabajador.getIdTrabajador());
                int filasAfectadas = editar.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMessage("Trabajador editado");
                    resultado.setNumberRowsAffected(filasAfectadas);
                }else{
                    resultado.setMessage("Error al editar");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error en el query");
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
