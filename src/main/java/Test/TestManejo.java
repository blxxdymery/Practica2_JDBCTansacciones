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
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;


/**
 * Main con menú que ejecuta nuestras operaciones y conecta con nuestra BD
 * @author maria
 */
public class TestManejo {

    public static void main(String[] args) throws Exception {
        Connection conexion = null;

        try {
            conexion = Conexion.getConnection();
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            Cliente cliente = null;
            String dni = null;
            EWallet ewallet = null;
            Producto producto = null;
            Transaccion transaccion = null;

            ClienteDAO clienteDao = new ClienteDAO(conexion);
            ProductoDAO productoDao = new ProductoDAO(conexion);
            EWalletDAO ewalletDao = new EWalletDAO(conexion);
            TransaccionDAO transaccionDao = new TransaccionDAO(conexion);

            List<Cliente> clientes = clienteDao.seleccionar();
            List<EWallet> ewallets = ewalletDao.seleccionar();
            List<Producto> productos = productoDao.seleccionar();
            List<String> nombreProductos = productoDao.seleccionarNombre();
            List<Transaccion> transacciones = transaccionDao.seleccionar();

            
            Scanner teclado = new Scanner(System.in);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            long timeInMilliSeconds = date.getTime();
            java.sql.Date fechaActual = new java.sql.Date(timeInMilliSeconds);
            int op;

            //MENÚ CON OPERACIONES
            do {
                System.out.println("BIENVENIDO A SUPERCOMPRÍN");
                System.out.println("¿Qué quiere hacer?");
                System.out.println("1. Registrarse como Cliente");
                System.out.println("2. Acceder a su portal de Cliente");
                System.out.println("3. Salir");
                op = teclado.nextInt();
                teclado.nextLine();

                switch (op) {
                    case 1:
                        System.out.println("Introduzca su dni");
                        dni = teclado.nextLine();
                        System.out.println("Introduzca su nombre");
                        String nombre = teclado.nextLine();
                        System.out.println("Introduzca sus apellidos");
                        String apellidos = teclado.nextLine();
                        System.out.println("Introduzca su fecha de nacimiento (yyyy-MM-dd)");
                        String sFecha = teclado.nextLine();
                        sFecha = simpleDateFormat.format(date);
                        java.sql.Date fechaN = java.sql.Date.valueOf(sFecha);

                        System.out.println("Introduzca su email");
                        String email = teclado.nextLine();
                        cliente = new Cliente(dni, nombre, apellidos, fechaN, email);
                        ewallet = new EWallet(dni);
                        try {
                            clienteDao.insertar(cliente);
                            System.out.println("Perfil creado correctamente");
                            conexion.commit();
                        } catch (SQLException e) {
                            conexion.rollback();
                            System.out.println("Error creando el perfil de cliente y su e-wallet");
                            e.getMessage();
                        }
                        System.out.println();
                        break;
                    case 2:
                        int aux = 0;
                        do {
                            System.out.println("Introduzca su DNI para acceder: ");
                            dni = teclado.nextLine();
                            aux = 1;
                        } while (aux == 0);

                        for (int i = 0; i < clientes.size(); i++) {
                            if (clientes.get(i).getDni().equals(dni)) {
                                cliente = clientes.get(i);
                            }
                        }

                        for (int i = 0; i < ewallets.size(); i++) {
                            if (ewallets.get(i).getDni().equals(dni)) {
                                ewallet = ewallets.get(i);
                            }
                        }

                        do {
                            System.out.println("1. Gestionar perfil de Cliente");
                            System.out.println("2. Gestionar E-Wallet");
                            System.out.println("3. Realizar una operación");
                            System.out.println("4. Salir");
                            op = teclado.nextInt();
                            teclado.nextLine();

                            switch (op) {
                                case 1:
                                    do {
                                        System.out.println("1. Modificar datos Cliente");
                                        System.out.println("2. Eliminar cuenta de cliente");
                                        System.out.println("3. Ver sus datos de Cliente");
                                        System.out.println("4.Salir");
                                        op = teclado.nextInt();
                                        teclado.nextLine();

                                        switch (op) {
                                            case 1:
                                                String seleccion;
                                                do {
                                                    System.out.println("Indique que quiere cambiar(nombre/apellidos/fechaNacimiento/email o nada)");
                                                    seleccion = teclado.nextLine();
                                                    switch (seleccion) {
                                                        case "nombre":
                                                            System.out.println("Introduzca nuevo nombre");
                                                            nombre = teclado.nextLine();
                                                            cliente.setNombre(nombre);
                                                            clienteDao.actualizar(cliente);
                                                            break;
                                                        case "apellidos":
                                                            System.out.println("Introduzca los nuevos apellidos");
                                                            apellidos = teclado.nextLine();
                                                            cliente.setApellidos(apellidos);
                                                            clienteDao.actualizar(cliente);
                                                            break;
                                                        case "FechaNacimiento":
                                                            System.out.println("Introduzca la nueva fecha de nacimiento (dd-mm-aaaa)");
                                                            sFecha = teclado.nextLine();
                                                            sFecha = simpleDateFormat.format(date);
                                                            fechaN = java.sql.Date.valueOf(sFecha);
                                                            cliente.setFechaNacimiento(fechaN);
                                                            clienteDao.actualizar(cliente);
                                                            break;
                                                        case "email":
                                                            System.out.println("Introduzca el nuevo email");
                                                            email = teclado.nextLine();
                                                            cliente.setEmail(email);
                                                            clienteDao.actualizar(cliente);
                                                            break;
                                                    }
                                                } while (!"nada".equals(seleccion));
                                                System.out.println();
                                                break;
                                            case 2:
                                                System.out.println("¿Estás seguro de querer eliminar la cuenta?(si/no)");
                                                seleccion = teclado.nextLine();
                                                if (seleccion.equals("si")) {
                                                    clienteDao.eliminar(cliente);
                                                    ewalletDao.eliminar(ewallet);
                                                    System.out.println("Cuenta de cliente eliminada...");
                                                    op = 4;
                                                } else {
                                                    System.out.println("Saliendo del menú de eliminar...");
                                                    op = 4;
                                                }
                                                System.out.println();
                                                break;
                                            case 3:
                                                System.out.println(cliente);
                                                break;
                                        }
                                    } while (op != 4);
                                    dni = null;
                                    break;
                                case 2:
                                    do {
                                        System.out.println("1. Ingresar dinero en la E-Wallet");
                                        System.out.println("2. Ver datos de su E-Wallet");
                                        System.out.println("3. Salir");
                                        op = teclado.nextInt();
                                        teclado.nextLine();
                                        switch (op) {
                                            case 1:
                                                double dIngreso;
                                                System.out.print("Introduzca el dinero a ingresar:");
                                                dIngreso = teclado.nextDouble();
                                                teclado.nextLine();
                                                ewalletDao.ingresarDinero(dIngreso, ewallet, fechaActual);
                                                break;
                                            case 2:
                                                System.out.println(ewallet);
                                                break;
                                        }
                                    } while (op != 3);
                                    System.out.println();
                                    break;
                                case 3:
                                    do {

                                        System.out.println("1. Comprar producto con dinero");
                                        System.out.println("2. Comprar producto con puntos");
                                        System.out.println("3. Devolver producto");
                                        System.out.println("4. Salir");
                                        op = teclado.nextInt();
                                        teclado.nextLine();
                                        int cantidad;

                                        switch (op) {

                                            case 1:
                                                String prod;
                                                System.out.println("Elige el producto a comprar");
                                                System.out.println(nombreProductos);
                                                prod = teclado.nextLine();
                                                for (int i = 0; i < productos.size(); i++) {
                                                    if (productos.get(i).getNombre().equals(prod)) {
                                                        producto = productos.get(i);
                                                    }
                                                }
                                                System.out.println("Introduce la cantidad a comprar");
                                                cantidad = teclado.nextInt();
                                                teclado.nextLine();

                                                try {
                                                    transaccionDao.comprarProducto(ewallet, producto, productoDao, ewalletDao, cantidad);
                                                    System.out.println("Producto comprado, se ha actualizado el saldo de su E-Wallet.");
                                                    conexion.commit();
                                                } catch (SQLException e) {
                                                    conexion.rollback();
                                                    e.getMessage();
                                                }
                                                transaccion = new Transaccion(ewallet.getDni(), producto.getNombre(), fechaActual);
                                                transaccionDao.insertar(transaccion);
                                                System.out.println();
                                                break;
                                            case 2:
                                                System.out.println("Elige el producto a comprar");
                                                System.out.println(nombreProductos);
                                                prod = teclado.nextLine();
                                                for (int i = 0; i < productos.size(); i++) {
                                                    if (productos.get(i).getNombre().equals(prod)) {
                                                        producto = productos.get(i);
                                                    }
                                                }
                                                System.out.println("introduce la cantidad a comprar");
                                                cantidad = teclado.nextInt();
                                                teclado.nextLine();

                                                try {
                                                    transaccionDao.comprarConPuntos(ewallet, producto, productoDao, ewalletDao, cantidad);
                                                    System.out.println("Producto comprado, se han actualizado los puntos de su E-Wallet.");
                                                    conexion.commit();
                                                } catch (SQLException e) {
                                                    conexion.rollback();
                                                    e.getMessage();
                                                }
                                                transaccion = new Transaccion(ewallet.getDni(), producto.getNombre(), fechaActual);
                                                transaccionDao.insertar(transaccion);
                                                System.out.println();
                                                break;
                                            case 3:
                                                System.out.println("Elige el producto a devolver");
                                                System.out.println(nombreProductos);
                                                prod = teclado.nextLine();
                                                for (int i = 0; i < productos.size(); i++) {
                                                    if (productos.get(i).getNombre().equals(prod)) {
                                                        producto = productos.get(i);
                                                    }
                                                }
                                                System.out.println("introduce la cantidad a devolver");
                                                cantidad = teclado.nextInt();
                                                teclado.nextLine();
                                                try {
                                                    transaccionDao.devolverProducto(ewallet, producto, productoDao, ewalletDao, cantidad);
                                                    System.out.println("Producto devuelto, se han actualizado los datos de su E-Wallet.");
                                                    conexion.commit();
                                                } catch (SQLException e) {
                                                    conexion.rollback();
                                                    e.getMessage();
                                                }
                                                transaccion = new Transaccion(ewallet.getDni(), producto.getNombre(), fechaActual);
                                                transaccionDao.insertar(transaccion);
                                                System.out.println();
                                                break;
                                        }
                                    } while (op != 4);
                                    break;
                            }
                        } while (op != 4);
                        break;
                }
            } while (op != 3);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            System.out.println("Entramos en rollback");
            try {
                conexion.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace(System.out);
            }
        }

    }
}
