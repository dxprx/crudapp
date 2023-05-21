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
public class Ciudad {
    private int idciudad;
    private int idccaa;
    private String nombreCiudad;
    private String nombreComunidad;

    public Ciudad(int idciudad, int idccaa, String nombreCiudad, String nombreComunidad) {
        this.idciudad = idciudad;
        this.idccaa = idccaa;
        this.nombreCiudad = nombreCiudad;
        this.nombreComunidad = nombreComunidad;
    }

    public Ciudad() {
    }

    
    
    public int getIdciudad() {
        return idciudad;
    }

    public void setIdciudad(int idciudad) {
        this.idciudad = idciudad;
    }

    public int getIdccaa() {
        return idccaa;
    }

    public void setIdccaa(int idccaa) {
        this.idccaa = idccaa;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public String getNombreComunidad() {
        return nombreComunidad;
    }

    public void setNombreComunidad(String nombreComunidad) {
        this.nombreComunidad = nombreComunidad;
    }

    @Override
    public String toString() {
        return this.nombreCiudad; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public boolean equals(Object obj) {
        return this.nombreCiudad.equals(obj.toString()); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreCiudad); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
