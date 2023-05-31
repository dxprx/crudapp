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
public class Cliente {

    private int idClientes;
    private String nombre;
    private String telefono;
    private String direccion;
    private Ciudad ciudad;
    private String codigoPostal;
    private String email;

    public Cliente(int idClientes, String nombre, String telefono, String direccion, Ciudad ciudad, String codigoPostal, String email) {
        this.idClientes = idClientes;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.email = email;
    }

    public Cliente() {
    }

    public int getIdClientes() {
        return idClientes;
    }

    public void setIdClientes(int idClientes) {
        this.idClientes = idClientes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Cliente [idClientes=" + idClientes + ", nombre=" + nombre + ", telefono=" + telefono + ", direccion="
                + direccion + ", ciudad=" + ciudad + ", codigoPostal=" + codigoPostal + ", email=" + email + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cliente cliente = (Cliente) obj;
        return idClientes == cliente.idClientes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClientes);
    }
}
