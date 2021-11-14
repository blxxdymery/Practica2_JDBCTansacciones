/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domnio;

import java.sql.Date;

/**
 *
 * @author maria
 */
public class Transaccion {
    private int id;
    private String dni;
    private String nombreProducto;
    private Date fecha;

    public Transaccion() {
    }

    public Transaccion(String dni, String nombreProducto, Date fecha) {
        this.dni = dni;
        this.nombreProducto = nombreProducto;
        this.fecha = fecha;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public Date getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "Transaccion{" + "id=" + id + ", dni=" + dni + ", nombreProducto=" + nombreProducto + ", fecha=" + fecha + '}';
    }

}
