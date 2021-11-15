package Datos;

import static Datos.Conexion.getConnection;
import Domnio.EWallet;
import Domnio.Producto;
import Domnio.Transaccion;
import java.sql.Connection;
import java.util.Date;
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
    //private static final String SQL_UPDATE = "UPDATE transacciones SET dni=?, nombreProducto=?, fecha=? WHERE id=?"; NO QUEREMOS MODIFICAR UN REGISTRO DE COMPRA7DEVOLUCION
    private static final String SQL_DELETE = "DELETE FROM transacciones WHERE id=?";
    
    private Connection conexionTransaccional;
    
    public TransaccionDAO(){}
    
    /**
    * Constructor de TransaccionDao con conexion como parametro
     * @param conexionTransanccional  
    */
    public TransaccionDAO(Connection conexionTransanccional){
        this.conexionTransaccional = conexionTransanccional;
    }
    
    /**
    * Método para consultas de select simples en transacciones
    * @return la lista de las transacciones realizadas
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
                transaccion = new Transaccion(dni, nombreProducto, (java.sql.Date) fecha);
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
    
    /**
    * Método para insertar una transaccion
     * @param transaccion transaccion a ingresar
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
            stm.setDate(4, (java.sql.Date)transaccion.getFecha());
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
    
 
    /* No tiene sentido cambiar una transacción así que la comento
    
    public int actualizar(Transaccion transaccion) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_UPDATE);
            stm.setString(1, transaccion.getDni());
            stm.setString(2, transaccion.getNombreProducto());
            stm.setDate(3, transaccion.getFecha());
            stm.setInt(4, transaccion.getId());
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
    }*/
    
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

    /**
    * Método para comprar un producto con dinero
     * @param ewallet ewallet para la transaccion
     * @param producto producto a comprar
     * @param productoDao 
     * @param ewalletDao
     * @param cantidad cantidad de producto a comprar
     * @throws java.sql.SQLException
    */
    public void comprarProducto(EWallet ewallet, Producto producto, ProductoDAO productoDao, EWalletDAO ewalletDao, int cantidad) throws Exception{
        if(ewallet.getSaldo()<producto.getPrecio()){
            throw new Exception("El saldo de la E-Wallet es menor al precio del producto");
        }else{
            ewallet.setSaldo((ewallet.getSaldo()-(producto.getPrecio()*cantidad)));
            ewallet.setPuntos((ewallet.getPuntos()+(producto.getValorPuntos()*cantidad)));
            producto.setStock(Producto.restarStock(producto, cantidad));
            ewalletDao.actualizar(ewallet);
            productoDao.actualizar(producto);
            //se quitan los commits de los actualizar y se pone en el main
        }
    }

    /**
    * Método para devolver un producto
     * @param ewallet ewallet para la transaccion
     * @param producto producto a devolver
     * @param productoDao 
     * @param ewalletDao
     * @param cantidad cantidad de producto a devolver
     * @throws java.sql.SQLException
    */
    public void devolverProducto(EWallet ewallet, Producto producto, ProductoDAO productoDao, EWalletDAO ewalletDao, int cantidad) throws SQLException{
        ewallet.setSaldo((ewallet.getSaldo()+(producto.getPrecio()*cantidad)));
        ewallet.setPuntos((ewallet.getPuntos()-(producto.getValorPuntos()*cantidad)));
        producto.setStock(Producto.sumarStock(producto, cantidad));
        ewalletDao.actualizar(ewallet);
        productoDao.actualizar(producto);
        
    }
    
    /**
    * Método para comprar un producto con puntos de la ewallet
     * @param ewallet ewallet para la transaccion
     * @param producto producto a comprar
     * @param productoDao 
     * @param ewalletDao
     * @param cantidad cantidad de producto a comprar
     * @throws java.sql.SQLException
    */
    public void comprarConPuntos(EWallet ewallet, Producto producto, ProductoDAO productoDao, EWalletDAO ewalletDao, int cantidad) throws Exception{
        double precioProducto = producto.getPrecio();
        if(ewallet.getPuntos()<producto.getValorPuntos() || precioProducto<5){
            throw new Exception("Error en la compra. No tienes suficientes puntos o el precio del producto es demasiado bajo.");
        }else{
            ewallet.setPuntos((ewallet.getPuntos()-(producto.getValorPuntos()*cantidad)));
            producto.setStock(Producto.restarStock(producto, cantidad));
            ewalletDao.actualizar(ewallet);
            productoDao.actualizar(producto);
            
        }
    }
}
