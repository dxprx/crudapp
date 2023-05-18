/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author oscar
 */
public class Producto {
    private int idProducto;
    private String nombre;
    private LineaProducto lineaProducto;
    private String descripcion;
    private int stock;
    private double pvp;

    //Constructor para select
    public Producto(int idProducto, String nombre, LineaProducto lineaProducto, String descripcion, int stock, double pvp) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.lineaProducto = lineaProducto;
        this.descripcion = descripcion;
        this.stock = stock;
        this.pvp = pvp;
    }
    //Constructor para insert
    public Producto(String nombre, LineaProducto lineaProducto, String descripcion, int stock, double pvp) {
        this.nombre = nombre;
        this.lineaProducto = lineaProducto;
        this.descripcion = descripcion;
        this.stock = stock;
        this.pvp = pvp;
    }
    

    public LineaProducto getLineaProducto() {
        return lineaProducto;
    }

    public void setLineaProducto(LineaProducto lineaProducto) {
        this.lineaProducto = lineaProducto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPvp() {
        return pvp;
    }

    public void setPvp(double pvp) {
        this.pvp = pvp;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Producto [idProducto=" + idProducto + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio="
                + pvp + ", stock=" + stock + "]";
    }
}
