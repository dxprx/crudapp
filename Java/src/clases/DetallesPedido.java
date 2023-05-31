/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.List;

/**
 *
 * @author oscar
 */
public class DetallesPedido {
    private Pedido pedido;
    private int producto;
    private int cantidad;
    private float precio_venta;

    public DetallesPedido(Pedido pedido, int producto, int cantidad, float precio_venta) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio_venta = precio_venta;
    } 

    public DetallesPedido() {
    }
    
    
    
    public Pedido getIdpedido() {
        return pedido;
    }

    public void setIdpedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public int getIdproducto() {
        return producto;
    }

    public void setIdproducto(int producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(float precio_venta) {
        this.precio_venta = precio_venta;
    }
    
    
}
