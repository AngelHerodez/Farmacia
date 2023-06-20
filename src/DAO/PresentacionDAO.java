/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import POJO.Presentacion;
import conexionBD.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Carmona
 */
public class PresentacionDAO {
    public static ArrayList<Presentacion> obtenerPresentacion() throws SQLException{
        ArrayList<Presentacion> presentacion = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idPresentacion, presentacion FROM presentacion";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultadoConsulta = prepararConsulta.executeQuery();
                presentacion = new ArrayList<>();
                while (resultadoConsulta.next()) {                    
                    Presentacion objeto = new Presentacion();
                    objeto.setIdPresentacion(resultadoConsulta.getInt("idPresentacion"));
                    objeto.setPresentacion(resultadoConsulta.getString("presentacion"));
                    presentacion.add(objeto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }   
        }
        return presentacion;
    }
}
