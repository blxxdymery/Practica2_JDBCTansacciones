/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import static Datos.Conexion.*;
import Domnio.Cliente;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maria
 */
public class ClienteDAO {
    private static final String SQL_SELECT = "SELECT * FROM clientes";
    private static final String SQL_INSERT = "INSERT INTO clientes (dni, nombre, apellidos, fechaNacimiento, email) VALUES (?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE clientes SET dni=?, nombre=?, apellidos=?, fechaNacimiento=?, email=? WHERE dni=?";
    private static final String SQL_DELETE = "DELETE FROM clientes WHERE dni=?";
    //private static final String SQL_DELETE2 = "DELETE p.*, c.* FROM propietarios p LEFT JOIN coches c ON p.dni = c.dni WHERE p.dni=?";
    
    /**
    * Método para consultas de select simples en propietarios
    * @return la lista de propietarios que devuelve la consulta
    * @throws java.sql.SQLException 
    */
    public List<Cliente> seleccionar() throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Cliente cliente = null;
        List<Cliente> clientes = new ArrayList<>();
        
        try{
            con = getConnection();
            stm = con.prepareStatement(SQL_SELECT);
            rs = stm.executeQuery();
            
            while(rs.next()){
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                Date fechaNacimiento = rs.getDate("fechaNacimiento");
                String email = rs.getString("email");
                cliente = new Cliente(dni, nombre, apellidos, fechaNacimiento, email);
                clientes.add(cliente);
            }
        }catch(SQLException e){
            e.printStackTrace(System.out);
        }finally{
            Conexion.close(con);
            Conexion.close(rs);
            Conexion.close(stm);
        }
        return clientes;
    }
    
    /**
    * Método para insertar un propietario
    * @param cliente a insertar
    * @return el numero de registros
    */
    public int insertar(Cliente cliente){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = Conexion.getConnection();
            stm = con.prepareStatement(SQL_INSERT);
            stm.setString(1, cliente.getDni());
            stm.setString(2, cliente.getNombre());
            stm.setString(3, cliente.getApellidos());
            stm.setDate(4, cliente.getFechaNacimiento());
            stm.setString(5, cliente.getEmail());
            registros = stm.executeUpdate();
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
    * @param cliente
    * @return el numero de registros
    */
    public int actualizar(Cliente cliente){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = Conexion.getConnection();
            stm = con.prepareStatement(SQL_UPDATE);
            stm.setString(1, cliente.getDni());
            stm.setString(2, cliente.getNombre());
            stm.setString(3, cliente.getApellidos());
            stm.setDate(4, cliente.getFechaNacimiento());
            stm.setString(5, cliente.getEmail());
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
    public int eliminar(Cliente cliente){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        try {
            con = getConnection();
            stm = con.prepareStatement(SQL_DELETE);
                  stm.setString(1, cliente.getDni());      
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
