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
public class HiloUnirsePartido extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String correo;
    private int partido_id;
 
    public HiloUnirsePartido(DataOutputStream dot,String c, String pid){
        this.fsalida = dot;
        this.correo = c;
        this.partido_id = Integer.parseInt(pid);
    }
    
    @Override
    public void run() {
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            PreparedStatement p = conexion.prepareStatement("INSERT INTO usuario_partido VALUES ((SELECT usuario_id from usuario where correo = (?)),?)");) {
            p.setString(1, correo);
            p.setInt(2, partido_id);
            int insercion = 0;
            insercion  = p.executeUpdate();
            
            fsalida.writeUTF((insercion != 0) ? "OK" : "Error al unirse al partido");
            
        } catch (SQLException ex) {
            System.out.println("AQUIIII");
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
