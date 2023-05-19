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
    
    
}
