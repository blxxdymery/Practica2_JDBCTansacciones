/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase Conexión que guarda los parámetros para poder conectarse a nuestra BD (url, usuario y contraseña)
 * @author marolt
 */
public class Conexion {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/supercomprin?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD= "U[7E_DYgq.b6LHoW";
    
    /**
    * Método que te muestra la conexión 
    * @return la conexión que hemos realizado
    * @throws java.sql.SQLException 
    */
    public static Connection getConnection() throws SQLException{
      return DriverManager.getConnection (JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
    
    /**
    * Método para cerrar el ResultSet
    * @param rs nombre del ResultSet
    * @throws java.sql.SQLException 
    */
    public static void close(ResultSet rs) throws SQLException{
        rs.close();
    }
    
    /**
    * Método para cerrar el Statement
    * @param stm nombre del Statement
    * @throws java.sql.SQLException 
    */
    public static void close(Statement stm) throws SQLException{
        stm.close();
    }
    
    /**
    * Método para cerrar la Conexión
    * @param con nombre de la conexión
    * @throws java.sql.SQLException 
    */
    public static void close(Connection con) throws SQLException{
        con.close();
    }
}