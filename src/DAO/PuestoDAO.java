/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import POJO.Puesto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import conexionBD.ConexionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Carmona
 */
public class PuestoDAO {
    public static ArrayList<Puesto> obtenerPuestos()throws SQLException{
        ArrayList<Puesto> puesto = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idPuesto, puesto FROM puesto";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultadoConsulta = prepararConsulta.executeQuery();
                puesto = new ArrayList<>();
                while (resultadoConsulta.next()) {                    
                    Puesto puestoObj = new Puesto();
                    puestoObj.setIdPuesto(resultadoConsulta.getInt("idPuesto"));
                    puestoObj.setPuesto(resultadoConsulta.getString("puesto"));
                    puesto.add(puestoObj);
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
