/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class HiloInsertarUsuario extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String nombre;
    private String apellidos;
    private String c;
    private String pwd;
 
    public HiloInsertarUsuario(DataOutputStream dot, String no, String ap, String correo, String p){
        this.fsalida = dot;
        this.nombre = no;
        this.apellidos = ap;
        this.c = correo;
        this.pwd = p;
    }
    
    @Override
    public void run() {
        int inserciones = 0;
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            PreparedStatement p = conexion.prepareStatement("INSERT INTO usuario (nombre, apellidos, correo, pwd) VALUES (?,?,?,?)");) {
            p.setString(1, nombre);
            p.setString(2, apellidos);
            p.setString(3, c);
            p.setString(4, pwd);
            inserciones = p.executeUpdate();
            fsalida.writeUTF((inserciones == 1)? "true":"false");
        } catch (SQLException ex) {
            Logger.getLogger(HiloInsertarUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloLogin.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
