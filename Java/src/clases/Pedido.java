/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author oscar
 */
public class Pedido {

    private int idpedido;
    private Cliente cliente;
    private Empleado empleado;
    private Date fechaPedido;

    public Pedido(int idpedido, Cliente cliente, Empleado empleado, Date fechaPedido) {
        this.idpedido = idpedido;
        this.cliente = cliente;
        this.empleado = empleado;
        this.fechaPedido = fechaPedido;
    }

    public Pedido() {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pedido pedido = (Pedido) obj;
        return idpedido == pedido.idpedido;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idpedido);
    }

}
