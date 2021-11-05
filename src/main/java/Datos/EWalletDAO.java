/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import static Datos.Conexion.*;
import Domnio.EWallet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maria
 */
public class EWalletDAO {
    private static final String SQL_SELECT = "SELECT * FROM ewallets";
    private static final String SQL_INSERT = "INSERT INTO ewallets (dni, saldo, puntos) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE ewallets SET dni=?, saldo=?, puntos=? WHERE dni=?";
    private static final String SQL_DELETE = "DELETE FROM ewallets WHERE dni=?";
    
    /**
    * Método para consultas de select simples en propietarios
    * @return la lista de propietarios que devuelve la consulta
    * @throws java.sql.SQLException 
    */
    public List<EWallet> seleccionar() throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        EWallet ewallet = null;
        List<EWallet> ewallets = new ArrayList<>();
        
        try{
            con = getConnection();
            stm = con.prepareStatement(SQL_SELECT);
            rs = stm.executeQuery();
            
            while(rs.next()){
                String dni = rs.getString("dni");
                double saldo = rs.getDouble("saldo");
                int puntos = rs.getInt("puntos");
                        
                ewallet = new EWallet(dni, saldo, puntos);
                ewallets.add(ewallet);
            }
        }catch(SQLException e){
            e.printStackTrace(System.out);
        }finally{
            Conexion.close(con);
            Conexion.close(rs);
            Conexion.close(stm);
        }
        return ewallets;
    }
    
    /**
    * Método para insertar un propietario
     * @param ewallet
    * @return el numero de registros
    */
    public int insertar(EWallet ewallet){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        String dni;
        
        try{
            con = Conexion.getConnection();
            stm = con.prepareStatement(SQL_INSERT);
            stm.setString(1, ewallet.getDni());
            //stm.setString(2, coche.getMarca());
            //stm.setInt(3, coche.getPrecio());
            dni = ewallet.getDni();
            
            if(ClienteDAO.ComprobarDni(dni) == true){
                registros = stm.executeUpdate();
            }else{
                System.out.println("El dni introducido no coincide con ningún propietario");
                try{
                    close(stm);
                }catch(SQLException e){
                    e.printStackTrace(System.out);
                }try{
                    close(con);
                }catch(SQLException e){
                    e.printStackTrace(System.out);
                }
            }                
        }catch(SQLException e){
            e.printStackTrace(System.out);
        }finally{
            try{
                close(stm);
            }catch(SQLException e){
                e.printStackTrace(System.out);
            }try{
                close(con);
            }catch(SQLException e){
                 e.printStackTrace(System.out);
            }
        }   
        return registros; 
    }
    
    /**
    * Método para actualizar los datos de un propietario de la tabla
     * @param ewallet
    * @return el numero de registros
    */
    public int actualizar(EWallet ewallet){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = Conexion.getConnection();
            stm = con.prepareStatement(SQL_UPDATE);
            stm.setString(1, ewallet.getDni());
            stm.setDouble(2, ewallet.getSaldo());
            stm.setInt(3, ewallet.getPuntos());
            registros = stm.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            try {
                close(stm);
                close(con);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
    
    /**
    * Método para eliminar un propietario de la tabla junto a sus coches
    * @param cliente
    * @return el numero de registros
    */
    public int eliminar(EWallet ewallet){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        try {
            con = getConnection();
            stm = con.prepareStatement(SQL_DELETE);
                  stm.setString(1, ewallet.getDni());      
            registros = stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally{
            try {
                close(stm);
                close(con);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
}
