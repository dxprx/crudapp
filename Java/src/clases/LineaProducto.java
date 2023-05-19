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
public class LineaProducto {
    private String linea;
    private String descripcion;

    public LineaProducto(String linea, String descripcion) {
        this.linea = linea;
        this.descripcion = descripcion;
    }

    public LineaProducto() {
    }

    
    
    public String getLinea() {
        return linea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
