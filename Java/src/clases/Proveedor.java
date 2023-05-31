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
public class Proveedor {
    private int idproveedor;
    private String nombreEmpresa;
    private String nombreContacto;
    private Ciudad ciudad;
    private String telefono;
    private String paginaweb;

    public Proveedor(int idproveedor, String nombreEmpresa, String nombreContacto, Ciudad ciudad, String telefono, String paginaweb) {
        this.idproveedor = idproveedor;
        this.nombreEmpresa = nombreEmpresa;
        this.nombreContacto = nombreContacto;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.paginaweb = paginaweb;
    }

    public Proveedor() {
    }

    
    
    public int getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(int idproveedor) {
        this.idproveedor = idproveedor;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPaginaweb() {
        return paginaweb;
    }

    public void setPaginaweb(String paginaweb) {
        this.paginaweb = paginaweb;
    }
    
           @Override
    public String toString() {
        return this.nombreEmpresa; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public boolean equals(Object obj) {
        return this.nombreEmpresa.equals(obj.toString()); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nombreEmpresa); //To change body of generated methods, choose Tools | Templates.
    }
}
