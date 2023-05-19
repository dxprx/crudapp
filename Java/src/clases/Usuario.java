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
public class Usuario {
    private String usuario;
    private String pass;
    private int idUsuario;
    private String role;

    public Usuario(String usuario, int idUsuario, String role) {
        this.usuario = usuario;
        this.idUsuario = idUsuario;
        this.role = role;
    }

    
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Usuario [usuario=" + usuario + ", pass=" + pass + ", idUsuario=" + idUsuario + ", role=" + role + "]";
    }
}
