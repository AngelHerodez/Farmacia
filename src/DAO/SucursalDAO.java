package DAO;

import POJO.Sucursal;
import conexionBD.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Carmona
 */
public class SucursalDAO {
    public static ArrayList<Sucursal> obtenerSucursa ()throws SQLException{
        ArrayList<Sucursal> puesto = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idSucursal, nombre, ciudad,"
                        + "calle, numeroDeLocal FROM sucursal";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultadoConsulta = prepararConsulta.executeQuery();
                puesto = new ArrayList<>();
                while (resultadoConsulta.next()) {                    
                    Sucursal sucursalObj = new Sucursal();
                    sucursalObj.setIdSucursal(resultadoConsulta.getInt("idSucursal"));
                    sucursalObj.setNombreSucursal(resultadoConsulta.getString("nombre"));
                    sucursalObj.setCiudad(resultadoConsulta.getString("ciudad"));
                    sucursalObj.setCalle(resultadoConsulta.getString("calle"));
                    sucursalObj.setNumeroDeLocal(resultadoConsulta.getInt("numeroDeLocal"));
                    puesto.add(sucursalObj);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
            
        }
        return puesto;
    }
    
            
    
}
