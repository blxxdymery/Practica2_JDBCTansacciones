package Domnio;

/**
 *  Clase Producto que corresponde a los datos de la tabla Productos de nuestra BD
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
    
    /**
     * Método que resta stock al comprar un producto
     * @param p el producto al que restar stock
     * @param c la cantidad de stock a restar
     * @return devuelve el stock actual
     */
    public static int restarStock(Producto p, int c) {
        int nuevoStock = (p.getStock()) - c;
        return nuevoStock;

    }
    
    /**
     * Método que suma stock al devolver un producto
     * @param p el producto al que sumar stock
     * @param c la cantidad de stock a sumar
     * @return devuelve el stock actual
     */
    public static int sumarStock(Producto p, int c){
        int nuevoStock = (p.getStock())+c;
        return nuevoStock;
    }
}
