/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import static Datos.Conexion.close;
import static Datos.Conexion.getConnection;
import Domnio.Producto;
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
public class ProductoDAO {
    private static final String SQL_SELECT = "SELECT * FROM productos";
    private static final String SQL_SELECT_NOMBRE = "SELECT nombre FROM productos";
    private static final String SQL_INSERT = "INSERT INTO productos (nombre, precio, valorPuntos, stock) VALUES (?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE productos SET precio=?, valorPuntos=?, stock=? WHERE nombre=?";
    private static final String SQL_DELETE = "DELETE FROM productos WHERE nombre=?";
    private Connection conexionTransaccional;
    
    
    public ProductoDAO(){}
    
    public ProductoDAO(Connection conexionTransanccional){
        this.conexionTransaccional = conexionTransanccional;
    }
    
    /**
    * Método para consultas de select simples en propietarios
    * @return la lista de propietarios que devuelve la consulta
    * @throws java.sql.SQLException 
    */
    public List<Producto> seleccionar() throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Producto producto = null;
        List<Producto> productos = new ArrayList<>();
        
        try{
            con = getConnection();
            stm = con.prepareStatement(SQL_SELECT);
            rs = stm.executeQuery();
            
            while(rs.next()){
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int valorPuntos = rs.getInt("valorPuntos");
                int stock = rs.getInt("stock");
                producto = new Producto(nombre, precio, valorPuntos, stock);
                productos.add(producto);
            }
        }catch(SQLException e){
            e.printStackTrace(System.out);
        }finally{
            Conexion.close(con);
            Conexion.close(rs);
            Conexion.close(stm);
        }
        return productos;
    }
    
    public List<String> seleccionarNombre() throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Producto producto = null;
        List<String> nombreProductos = new ArrayList<>();
        
        try{
            con = getConnection();
            stm = con.prepareStatement(SQL_SELECT_NOMBRE);
            rs = stm.executeQuery();
            
            while(rs.next()){
                String nombre = rs.getString("nombre");
                nombreProductos.add(nombre);
            }
        }catch(SQLException e){
            e.printStackTrace(System.out);
        }finally{
            Conexion.close(con);
            Conexion.close(rs);
            Conexion.close(stm);
        }
        return nombreProductos;
    }
    
    /**
    * Método para insertar un propietario
     * @param producto
    * @return el numero de registros
     * @throws java.sql.SQLException
    */
    public int insertar(Producto producto) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_INSERT);
            stm.setString(1, producto.getNombre());
            stm.setDouble(2, producto.getPrecio());
            stm.setInt(3, producto.getValorPuntos());
            stm.setInt(4, producto.getStock());
            registros = stm.executeUpdate();
            con.commit();
            con.rollback();
        }finally{
            try{
                Conexion.close(stm);
            }catch(SQLException e){
                e.printStackTrace(System.out);
            }try{
                if(this.conexionTransaccional == null)
                    Conexion.close(con);
            }catch(SQLException e){
                 e.printStackTrace(System.out);
            }
        }   
        return registros; 
    }
    
    /**
    * Método para actualizar los datos de un propietario de la tabla
     * @param producto
    * @return el numero de registros
    */
    public int actualizar(Producto producto){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_UPDATE);
            stm.setDouble(1, producto.getPrecio());
            stm.setInt(2, producto.getValorPuntos());
            stm.setInt(3, producto.getStock());
            stm.setString(4, producto.getNombre());
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
     * @param producto
    * @return el numero de registros
     * @throws java.sql.SQLException
    */
    public int eliminar(Producto producto) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        try {
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_DELETE);
                  stm.setString(1, producto.getNombre());  
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
    
 
}

