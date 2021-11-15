package Datos;

import static Datos.Conexion.*;
import Domnio.Cliente;
import Domnio.EWallet;
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
    private static final String SQL_UPDATE = "UPDATE clientes SET nombre=?, apellidos=?, fechaNacimiento=?, email=? WHERE dni=?";
    //private static final String SQL_DELETE = "DELETE FROM clientes WHERE dni=?";
    private static final String SQL_DELETE = "DELETE c.*, e.* FROM clientes c LEFT JOIN ewallets e ON c.dni = e.dni WHERE c.dni=?";
    private Connection conexionTransaccional;
    
    public ClienteDAO(){}
    
    /**
    * Constructor de ClienteDao con conexion como parametro
     * @param conexionTransanccional  
    */
    public ClienteDAO(Connection conexionTransanccional){
        this.conexionTransaccional = conexionTransanccional;
    }
    
    
    /**
    * Método para consultas de select simples en la tabla Clientes
    * @return la lista de clientes que devuelve la consulta
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
    * Método para insertar un cliente que a la vez llama al insert de EWalletDAO
    * @param cliente a insertar
    * @return el numero de registros
    */
    public int insertar(Cliente cliente){
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        EWallet ewallet;
        
        try{
            con = Conexion.getConnection();
            stm = con.prepareStatement(SQL_INSERT);
            stm.setString(1, cliente.getDni());
            stm.setString(2, cliente.getNombre());
            stm.setString(3, cliente.getApellidos());
            stm.setDate(4, cliente.getFechaNacimiento());
            stm.setString(5, cliente.getEmail());
            
            ewallet = new EWallet(cliente.getDni());
            EWalletDAO.insertar(ewallet);
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
    * Método para actualizar los datos de un cliente de la tabla
    * @param cliente a cambiar
    * @return el numero de registros
    * @throws java.sql.SQLException
    */
    public int actualizar(Cliente cliente) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        
        try{
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_UPDATE);
            stm.setString(1, cliente.getNombre());
            stm.setString(2, cliente.getApellidos());
            stm.setDate(3, cliente.getFechaNacimiento());
            stm.setString(4, cliente.getEmail());
            stm.setString(5, cliente.getDni());
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
    * Método para eliminar un cliente de la tabla junto a su ewallet
    * @param cliente
    * @return el numero de registros
     * @throws java.sql.SQLException
    */
    public int eliminar(Cliente cliente) throws SQLException{
        Connection con = null;
        PreparedStatement stm = null;
        int registros = 0;
        try {
            con = this.conexionTransaccional != null ?
                this.conexionTransaccional : Conexion.getConnection();
            con.setAutoCommit(false);
            stm = con.prepareStatement(SQL_DELETE);
                  stm.setString(1, cliente.getDni());      
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
