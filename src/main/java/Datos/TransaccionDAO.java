/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import static Datos.Conexion.getConnection;
import Domnio.EWallet;
import Domnio.Producto;
import Domnio.Transaccion;
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
public class TransaccionDAO {
    private static final String SQL_SELECT = "SELECT * FROM transacciones";
    static final String SQL_INSERT = "INSERT INTO transacciones (id, dni, nombreProducto, fecha) VALUES (?,?,?, ?)";
    private static final String SQL_UPDATE = "UPDATE transacciones SET id=?, dni=?, nombreProducto=?, fecha=? WHERE dni=?";
    private static final String SQL_DELETE = "DELETE FROM transacciones WHERE id=?";
    
    private Connection conexionTransaccional;
    
    public TransaccionDAO(){}
    
    public TransaccionDAO(Connection conexionTransanccional){
        this.conexionTransaccional = conexionTransanccional;
    }
    /**
    * Método para consultas de select simples en propietarios
    * @return la lista de propietarios que devuelve la consulta
    * @throws java.sql.SQLException 
    */
    public List<Transaccion> seleccionar() throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Transaccion transaccion = null;
        List<Transaccion> transacciones = new ArrayList<>();
        
        try{
            con = getConnection();
            stm = con.prepareStatement(SQL_SELECT);
            rs = stm.executeQuery();
            
            while(rs.next()){
                int id = rs.getInt("id");
                String dni = rs.getString("dni");
                String nombreProducto = rs.getString("nombreProducto");
                Date fecha = rs.getDate("fecha");     
                transaccion = new Transaccion(id, dni, nombreProducto, fecha);
                transacciones.add(transaccion);
            }
        }catch(SQLException e){
            e.printStackTrace(System.out);
        }finally{
            Conexion.close(con);
            Conexion.close(rs);
            Conexion.close(stm);
        }
        return transacciones;
    }
    
    //REVISAR
    /**
    * Método para insertar un propietario
     * @param transaccion
    * @return el numero de registros
     * @throws java.sql.SQLException
    */
    public int insertar(Transaccion transaccion) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_INSERT);
            stm.setInt(1, transaccion.getId());
            stm.setString(2, transaccion.getDni());
            stm.setString(3, transaccion.getNombreProducto());
            stm.setDate(4, transaccion.getFecha());
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
     * @param transaccion
    * @return el numero de registros
     * @throws java.sql.SQLException
    */
    public int actualizar(Transaccion transaccion) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_UPDATE);
            stm.setInt(1, transaccion.getId());
            stm.setString(2, transaccion.getDni());
            stm.setString(3, transaccion.getNombreProducto());
            stm.setDate(4, transaccion.getFecha());
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
    * Método para eliminar un propietario de la tabla junto a sus coches
     * @param transaccion
    * @return el numero de registros
     * @throws java.sql.SQLException
    */
    public int eliminar(Transaccion transaccion) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        try {
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_DELETE);
                  stm.setInt(1, transaccion.getId());      
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

    public void comprarProducto(EWallet ewallet, Producto producto, ProductoDAO productoDao, EWalletDAO ewalletDao) throws Exception{
        int nuevoStock;
        if(ewallet.getSaldo()<producto.getPrecio()){
            throw new Exception("El saldo de la E-Wallet es menor al precio del producto");
        }else{
            ewallet.setSaldo(ewallet.getSaldo()-producto.getPrecio());
            ewallet.setPuntos(ewallet.getPuntos()+producto.getValorPuntos());
            nuevoStock = Producto.restarStock(producto);
            producto.setStock(nuevoStock);
            ewalletDao.actualizar(ewallet);
            productoDao.actualizar(producto);
            System.out.println("Producto comprado, se han actualizado los datos de su E-Wallet.");
        }
    }

    
    public void devolverProducto(EWallet ewallet, Producto producto, ProductoDAO productoDao, EWalletDAO ewalletDao) throws Exception{
        int nuevoStock;
        ewallet.setSaldo(ewallet.getSaldo()+producto.getPrecio());
        ewallet.setPuntos(ewallet.getPuntos()-producto.getValorPuntos());
        nuevoStock = Producto.sumarStock(producto);
        producto.setStock(nuevoStock);
        ewalletDao.actualizar(ewallet);
        productoDao.actualizar(producto);
        System.out.println("Producto devuelto, se han actualizado los datos de su E-Wallet.");
    }
    
    public void comprarConPuntos(EWallet ewallet, Producto producto, ProductoDAO productoDao, EWalletDAO ewalletDao) throws Exception{
        int nuevoStock;
        double precioProducto = producto.getPrecio();
        if(ewallet.getPuntos()<producto.getValorPuntos() || precioProducto<5){
            throw new Exception("Error en la compra. No tienes suficientes puntos o el precio del producto es demasiado bajo.");
        }else{
            ewallet.setPuntos(ewallet.getPuntos()-producto.getValorPuntos());
            nuevoStock = Producto.restarStock(producto);
            producto.setStock(nuevoStock);
            ewalletDao.actualizar(ewallet);
            productoDao.actualizar(producto);
            System.out.println("Producto comprado con puntos, se han actualizado los datos de su E-Wallet.");
        }
    }
}
