/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domnio;

/**
 *
 * @author maria
 */
public class Producto {
    private String nombre;
    private double precio;
    private int valorPuntos;
    private int stock;

    public Producto(String nombre, double precio, int valorPuntos, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.valorPuntos = valorPuntos;
        this.stock = stock;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setValorPuntos(int valorPuntos) {
        this.valorPuntos = valorPuntos;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }
    
    public int getValorPuntos() {
        return valorPuntos;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "Producto{" + "nombre=" + nombre + ", precio=" + precio + ", valorPuntos=" + valorPuntos + ", stock=" + stock + '}';
    }
    
    public static int restarStock(Producto p) throws Exception{
        if(p.getStock()<=0){
             throw new Exception("No hay suficiente stock en el almacén");
        }else{
            int nuevoStock = p.getStock()-1;
            return nuevoStock;
        }
    }
    
    public static int sumarStock(Producto p) throws Exception{
        int nuevoStock = p.getStock()+1;
        return nuevoStock;
    }
}
