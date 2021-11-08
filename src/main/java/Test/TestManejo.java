/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Datos.ClienteDAO;
import Datos.Conexion;
import Datos.EWalletDAO;
import Datos.ProductoDAO;
import Domnio.Cliente;
import Domnio.EWallet;
import Domnio.Producto;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


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
            
            ClienteDAO clienteDao = new ClienteDAO(conexion);
            ProductoDAO productoDao = new ProductoDAO(conexion);
            EWalletDAO ewalletDao = new EWalletDAO(conexion);

            List<Cliente> clientes = clienteDao.seleccionar();
            List<EWallet> ewallets = ewalletDao.seleccionar();
            List<Producto> productos = productoDao.seleccionar();

            //MENÚ CON OPERACIONES
            
            Cliente c = new Cliente();
            c.setDni("16315638J");
            c.setNombre("Maria");
            c.setApellidos("Martinez");
            c.setFechaNacimiento(Date.valueOf("1994-08-07"));
            c.setEmail("maria@org.com");

            System.out.println(c);
            
            clienteDao.insertar(c);
        
            Producto p = new Producto("Manzana", 0.20, 2, 180);
            
           EWallet ewallet = null;
            for(int i=0; i<ewallets.size(); i++){
                if(ewallets.get(i).getDni().equals(c.getDni()))
                    ewallet = ewallets.get(i);
            }
            ewalletDao.comprarProducto(ewallet, p);
            
            
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
