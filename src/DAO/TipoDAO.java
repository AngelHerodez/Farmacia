/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import POJO.Tipo;
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
public class TipoDAO {
    public static ArrayList<Tipo> obtenerPresentacion() throws SQLException{
        ArrayList<Tipo> presentacion = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idtipo, tipo FROM tipo";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultadoConsulta = prepararConsulta.executeQuery();
                presentacion = new ArrayList<>();
                while (resultadoConsulta.next()) {                    
                    Tipo objeto = new Tipo();
                    objeto.setIdTipo(resultadoConsulta.getInt("idtipo"));
                    objeto.setTipo(resultadoConsulta.getString("tipo"));
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
