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
public class HiloPerfilUsuario extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String correo;
 
    public HiloPerfilUsuario(DataOutputStream dot,String c){
        this.fsalida = dot;
        this.correo = c;
    }
    
    @Override
    public void run() {
        String deporte_id = "";
        String nivel = "";
        int avatar = 0;
        String mensaje = "";
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            PreparedStatement p = conexion.prepareStatement("SELECT deporte_id, nivel FROM usuario_deporte "
                    + "WHERE usuario_id = (SELECT usuario_id from usuario where correo = (?))");
            PreparedStatement p2 = conexion.prepareStatement("SELECT avatar_id FROM usuario "
                    + "WHERE correo = (?)");) {
            p.setString(1, correo);
            ResultSet rset = p.executeQuery();
            while (rset.next()) {
                deporte_id = rset.getString("deporte_id");   
                nivel = rset.getString("nivel");
                mensaje += deporte_id + "," + nivel + ";";
                //System.out.println(mensaje);
            }
            
            fsalida.writeUTF((mensaje.equals(""))? "Error":mensaje);
            
            p2.setString(1,correo);
            
            ResultSet rset2 = p2.executeQuery();
            while (rset2.next()) {
                avatar = rset2.getInt("avatar_id");   
                mensaje = ""+avatar;
                //System.out.println("ID DEL AVATAR "+mensaje);
            }
            
            fsalida.writeUTF((mensaje.equals(""))? "Error al devolver el avatar":mensaje);
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

