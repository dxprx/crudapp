/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.Date;
import java.util.List;

/**
 *
 * @author oscar
 */
public class Pedido {
    private int idpedido;
    private Cliente cliente;
    private Empleado empleado;
    private Date fechaPedido;
    private String direccionPedido;
    private int ciudadPedido;
    private String codigopostalPedido;
    private List<DetallesPedido> detalles_pedido;

    public Pedido(int idpedido, Cliente cliente, Empleado empleado, Date fechaPedido, String direccionPedido, int ciudadPedido, String codigopostalPedido, List<DetallesPedido> detalles_pedido) {
        this.idpedido = idpedido;
        this.cliente = cliente;
        this.empleado = empleado;
        this.fechaPedido = fechaPedido;
        this.direccionPedido = direccionPedido;
        this.ciudadPedido = ciudadPedido;
        this.codigopostalPedido = codigopostalPedido;
        this.detalles_pedido = detalles_pedido;
    }

    public Pedido(Cliente cliente, Empleado empleado, Date fechaPedido, String direccionPedido, int ciudadPedido, String codigopostalPedido, List<DetallesPedido> detalles_pedido) {
        this.cliente = cliente;
        this.empleado = empleado;
        this.fechaPedido = fechaPedido;
        this.direccionPedido = direccionPedido;
        this.ciudadPedido = ciudadPedido;
        this.codigopostalPedido = codigopostalPedido;
        this.detalles_pedido = detalles_pedido;
    }
    


    public List<DetallesPedido> getDetalles_pedido() {
        return detalles_pedido;
    }

    public void setDetalles_pedido(List<DetallesPedido> detalles_pedido) {
        this.detalles_pedido = detalles_pedido;
    }

    
    
    public int getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(int idpedido) {
        this.idpedido = idpedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getDireccionPedido() {
        return direccionPedido;
    }

    public void setDireccionPedido(String direccionPedido) {
        this.direccionPedido = direccionPedido;
    }

    public int getCiudadPedido() {
        return ciudadPedido;
    }

    public void setCiudadPedido(int ciudadPedido) {
        this.ciudadPedido = ciudadPedido;
    }

    public String getCodigopostalPedido() {
        return codigopostalPedido;
    }

    public void setCodigopostalPedido(String codigopostalPedido) {
        this.codigopostalPedido = codigopostalPedido;
    }
    
    
}
