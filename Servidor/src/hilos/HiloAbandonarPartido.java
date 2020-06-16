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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * delete from usuario_partido where usuario_id = 9 and partido_id = 1
 * @author sergio
 */
public class HiloAbandonarPartido extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String correo;
    private int partido_id;
 
    public HiloAbandonarPartido(DataOutputStream dot,String c, String pid){
        this.fsalida = dot;
        this.correo = c;
        this.partido_id = Integer.parseInt(pid);
    }
    
    @Override
    public void run() {
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            PreparedStatement p = conexion.prepareStatement("delete from usuario_partido where usuario_id = (SELECT usuario_id from usuario where correo = (?)) "
                    + "and partido_id = (?)");) {
            p.setString(1, correo);
            p.setInt(2, partido_id);
            int afectados = p.executeUpdate();            
            fsalida.writeUTF((afectados == 1)? "Se ha borrado":"");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
