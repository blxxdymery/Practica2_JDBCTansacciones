/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Domnio.Cliente;
import java.sql.Date;


/**
 *
 * @author maria
 */
public class TestManejo {
    public static void main(String[] args) throws Exception {
        Cliente c = new Cliente();
        c.setDni("21742364J");
        c.setNombre("Juan");
        c.setApellidos("García");
        c.setFechaNacimiento(Date.valueOf("2001-02-3"));
        c.setEmail("juan@org.com");
        
        System.out.println(c);
    }
}
