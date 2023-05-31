/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.Objects;

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

       @Override
    public String toString() {
        return this.linea; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public boolean equals(Object obj) {
        return this.linea.equals(obj.toString()); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.linea); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
