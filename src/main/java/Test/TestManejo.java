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
import Datos.TransaccionDAO;
import Domnio.Cliente;
import Domnio.EWallet;
import Domnio.Producto;
import Domnio.Transaccion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;


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
            TransaccionDAO transaccionDao = new TransaccionDAO(conexion);

            List<Cliente> clientes = clienteDao.seleccionar();
            List<EWallet> ewallets = ewalletDao.seleccionar();
            List<Producto> productos = productoDao.seleccionar();
            List<Transaccion> transacciones = transaccionDao.seleccionar();

            //MENÚ CON OPERACIONES
            Scanner teclado = new Scanner(System.in);
            teclado.useDelimiter("-");
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            int op;
        
            do {
                System.out.println("BIENVENIDO A SUPERCOMPRÍN");
                System.out.println("¿Qué quiere hacer?");
                System.out.println("1. Operaciones de cliente");
                System.out.println("2. Operaciones de administrador");
                System.out.println("3. Salir");

                op = teclado.nextInt();
                teclado.nextLine();
                System.out.println();
                switch (op) {
                    case 1:
                        do {
                            System.out.println("1. Registrarse como Cliente");
                            System.out.println("2. Gestionar sus datos de Cliente");
                            System.out.println("3. Realizar una operación");
                            System.out.println("4. Salir");
                            op = teclado.nextInt();
                            teclado.nextLine();
                            System.out.println();

                            switch (op) {
                                case 1:
                                    System.out.println("Introduzca su dni");
                                    String dni = teclado.nextLine();
                                    System.out.println("Introduzca su nombre");
                                    String nombre = teclado.nextLine();
                                    System.out.println("Introduzca sus apellidos");
                                    String apellidos = teclado.nextLine();
                                    System.out.println("Introduzca su fecha de nacimiento (dd-mm-aaaa)");
                                    String sFecha = teclado.nextLine();
                                    Date fechaN = (Date) formatter.parse(sFecha);
                                    System.out.println("Introduzca su email");
                                    String email = teclado.nextLine();
                                    Cliente cliente = new Cliente(dni, nombre, apellidos, fechaN, email);
                                    clienteDao.insertar(cliente);
                                    break;
                                case 2:
                                    do {
                                        System.out.println("1. Gestionar perfil de Cliente");
                                        System.out.println("2. Gestionar E-Wallet");
                                        System.out.println("3. Salir");
                                        op = teclado.nextInt();
                                        teclado.nextLine();
                                        System.out.println();

                                        switch (op) {
                                            case 1:
                                                do {
                                                    System.out.println("1. Modificar datos Cliente");
                                                    System.out.println("2. Eliminar cuenta de cliente");
                                                    System.out.println("3. Ver sus datos de Cliente");
                                                    System.out.println("4.Salir");
                                                    op = teclado.nextInt();
                                                    teclado.nextLine();
                                                    System.out.println();

                                                    switch (op) {
                                                        case 1:

                                                            break;
                                                        case 2:

                                                            break;
                                                        case 3:

                                                        case 4:

                                                        case 5:

                                                    }
                                                } while (op != 4);

                                                break;
                                            case 2:

                                                break;
                                        }
                                    } while (op != 3);

                                    break;
                                case 3:

                                    break;
                            }
                        } while (op != 5);
                        break;
                    case 2:
                        do {
                            System.out.println("1. Insertar Propietario");
                            System.out.println("2. Modificar Propietario");
                            System.out.println("3. Eliminar Propietario y sus coches");
                            System.out.println("4. Listar un propietario y su coche");
                            System.out.println("5. Listar propietarios");
                            System.out.println("6. Salir");
                            op = teclado.nextInt();
                            teclado.nextLine();
                            System.out.println();

                            switch (op) {
                                case 1:

                                    break;
                                case 2:

                                    break;
                                case 3:

                                case 4:

                                case 5:

                            }
                        } while (op != 6);
                        break;
                }
            } while (op != 4);
        
            /*
            Cliente c = new Cliente();
            c.setDni("16315638J");
            c.setNombre("Maria");
            c.setApellidos("Martinez");
            c.setFechaNacimiento(Date.valueOf("1994-08-07"));
            c.setEmail("maria@org.com");

            System.out.println(c);
            
            clienteDao.insertar(c);
            
            Producto p = new Producto("Manzana", 0.20, 2, 180);
            
            String c = "16315638J";
           EWallet ewallet = null;
            for(int i=0; i<ewallets.size(); i++){
                if(ewallets.get(i).getDni().equals(c))
                    ewallet = ewallets.get(i);
            }
            //ewalletDao.comprarProducto(ewallet, p, productoDao); */
            
            
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
