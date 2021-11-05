/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domnio;

import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author maria
 */
public class Cliente {
    private String dni;
    private String nombre;
    private String apellidos;
    private Date fechaNacimiento;
    private String email;

    public Cliente() {
    }
    
    public Cliente(String dni, String nombre, String apellidos, Date fechaNacimiento, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
    }
    
    public void setDni(String dni) throws Exception {
        if(validarDNI(dni) ==  true){
            this.dni = dni;
        }else{
            throw new Exception("El DNI debe tener 8 números y una letra al final");
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setFechaNacimiento(Date fechaNacimiento) throws Exception {
        if(esMayorDeEdad(fechaNacimiento)==true){
            this.fechaNacimiento = fechaNacimiento; 
        }else{
            throw new Exception("Para registrarte como cliente debes tener más de 18 años");
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Cliente{" + "dni=" + dni + ", nombre=" + nombre + ", apellidos=" + apellidos + ", fechaNacimiento=" + fechaNacimiento + ", email=" + email + '}';
    }
    
    
    
    public boolean esMayorDeEdad(Date fecha){
        Date fechaTope = Date.valueOf("2003-11-12");
        if(fecha.before(fechaTope)){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean validarDNI(String dni){
        if(dni.length()!=9){
            return false;
        }

        if(soloNumeros(dni)==true && esLetra(dni)== true){
            return true;
        }else{
            return false;
        }
    }

    private boolean soloNumeros(String dni){
        String numero = "";
        String miDNI = "";
        String[] listaNumeros = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for(int i=0; i<dni.length()-1; i++) {
            numero = dni.substring(i, i+1);

            for(int j=0; j<listaNumeros.length; j++) {
                if (numero.equals(listaNumeros[j])) {
                    miDNI+= listaNumeros[j];
                }
            }
        }

        if(miDNI.length()!=8){
            return false;
        }else{
            return true;
        }
    }
    
    private boolean esLetra(String dni) {
        char letra = dni.charAt(8);

        if ((letra>='a' && letra<='z') || (letra>='A' && letra<='Z')) {
            return true;
        } else {
            return false;
        }
    }
}
