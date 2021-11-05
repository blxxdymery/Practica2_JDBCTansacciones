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
public class EWallet {
    private String dni;
    private double saldo;
    private int puntos;

    
    /**
     * Constructor inicial de una E-Wallet de un cliente nuevo
     * @param dni dni del Cliente propietario de la e-wallet
     */
    public EWallet(String dni) {
        this.dni = dni;
        saldo = 0;
        puntos = 0;
    }

    public EWallet(String dni, double saldo, int puntos) {
        this.dni = dni;
        this.saldo = saldo;
        this.puntos = puntos;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getDni() {
        return dni;
    }

    public double getSaldo() {
        return saldo;
    }

    public int getPuntos() {
        return puntos;
    }

    @Override
    public String toString() {
        return "EWallet{" + "dni=" + dni + ", saldo=" + saldo + ", puntos=" + puntos + '}';
    }
    
}
