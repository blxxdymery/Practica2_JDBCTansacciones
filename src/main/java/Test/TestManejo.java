/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Datos.ClienteDAO;
import Datos.Conexion;
import Domnio.Cliente;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;


/**
 *
 * @author maria
 */
public class TestManejo {
    
    public static void main(String[] args) throws Exception {
        Connection conexion = null;
        
        try{
            conexion = Conexion.getConnection();
            if(conexion.getAutoCommit()){
                conexion.setAutoCommit(false);
            }
            
            //MENÚ CON OPERACIONES
            ClienteDAO clienteDao = new ClienteDAO(conexion);

            Cliente c = new Cliente();
            c.setDni("16315638J");
            c.setNombre("Maria");
            c.setApellidos("Martinez");
            c.setFechaNacimiento(Date.valueOf("1994-08-07"));
            c.setEmail("maria@org.com");

            System.out.println(c);
            
            clienteDao.insertar(c);
        
        }catch(SQLException e){
            e.printStackTrace(System.out);
            System.out.println("Entramos en rollback");
            try{
                conexion.rollback();
            }catch(SQLException e1){
                e1.printStackTrace(System.out);
            }
        }
        
    }
}
