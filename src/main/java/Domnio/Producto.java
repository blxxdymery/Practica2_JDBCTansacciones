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
    private int valorPuntos;
    private int stock;

    public Producto(String nombre, int valorPuntos, int stock) {
        this.nombre = nombre;
        this.valorPuntos = valorPuntos;
        this.stock = stock;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getValorPuntos() {
        return valorPuntos;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "Producto{" + "nombre=" + nombre + ", valorPuntos=" + valorPuntos + ", stock=" + stock + '}';
    }
}
