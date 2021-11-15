/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import static Datos.Conexion.*;
import Domnio.EWallet;
import Domnio.Producto;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maria
 */
public class EWalletDAO {
    private static final String SQL_SELECT = "SELECT * FROM ewallets";
    static final String SQL_INSERT = "INSERT INTO ewallets (dni, saldo, puntos) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE ewallets SET saldo=?, puntos=? WHERE dni=?";
    private static final String SQL_DELETE = "DELETE FROM ewallets WHERE dni=?";
    
    private Connection conexionTransaccional;
    
    public EWalletDAO(){}
    
    /**
    * Constructor de EWalletDao con conexion como parametro
     * @param conexionTransanccional  
    */
    public EWalletDAO(Connection conexionTransanccional){
        this.conexionTransaccional = conexionTransanccional;
    }
    /**
    * Método para consultas de select simples en ewallets
    * @return la lista de ewallets que devuelve la consulta
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
    * Método para insertar una ewallet, es llamada al crear un cliente
     * @param ewallet
    * @return el numero de registros
    */
    public static int insertar(EWallet ewallet){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = Conexion.getConnection();
            stm = con.prepareStatement(SQL_INSERT);
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
    * Método para actualizar los datos de una ewallet de la tabla
     * @param ewallet
    * @return el numero de registros
     * @throws java.sql.SQLException
    */
    public int actualizar(EWallet ewallet) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_UPDATE);        
            stm.setDouble(1, ewallet.getSaldo());
            stm.setInt(2, ewallet.getPuntos());
            stm.setString(3, ewallet.getDni());
            registros = stm.executeUpdate();
            con.commit();
            con.rollback();
        }finally{
            try {
                Conexion.close(stm);
                if(this.conexionTransaccional == null)
                    Conexion.close(con);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
    
    /**
    * Método para eliminar una ewallet de la tabla
     * @param ewallet
    * @return el numero de registros
     * @throws java.sql.SQLException
    */
    public int eliminar(EWallet ewallet) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        try {
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_DELETE);
            stm.setString(1, ewallet.getDni());      
            registros = stm.executeUpdate();
            con.commit();
            con.rollback();
        }finally{
            try {
                Conexion.close(stm);
                if(this.conexionTransaccional == null)
                    Conexion.close(con);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
    
    /**
    * Método para ingresar saldo en la ewallet
     * @param dinero dinero a ingresar
     * @param ewallet ewallet en la que se ingresa
     * @param fechaActual fecha de la transacción
     * @throws java.sql.SQLException
    */
    public void ingresarDinero(double dinero, EWallet ewallet, Date fechaActual) throws SQLException, Exception{
        int dia = sacarDiaFecha(fechaActual);
        if(dia>=1 && dia<=5){
            ewallet.setSaldo(ewallet.getSaldo()+dinero);
            actualizar(ewallet);
            System.out.println("Ingreso realizado exitosamente. Saldo actual: "+ewallet.getSaldo());
        }else{
            throw new Exception("Operación rechazada. Solo se puede ingresar entre el día 1 y el 5 de cada mes.");
        }   
    }

    /**
    * Método para sacar el día de la fecha actual
    * @param date la fecha completa
    * @return el numero de registros
    */
    public int sacarDiaFecha(Date date){
        LocalDate currentDate = LocalDate.parse(date.toString());
        int day = currentDate.getDayOfMonth();
        return day;
    }

 
}
