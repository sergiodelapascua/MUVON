/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author sergio
 */
public class HiloCompruebaCorreo extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String c;
 
    public HiloCompruebaCorreo(DataOutputStream dot,String correo){
        this.fsalida = dot;
        this.c = correo;
    }
    
    @Override
    public void run() {
        String nombre = "";
        String apellidos = "";
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            PreparedStatement p = conexion.prepareStatement("SELECT nombre, apellidos FROM usuario where correo = (?)");) {
            p.setString(1, c);
            ResultSet rset = p.executeQuery();
            while (rset.next()) {
                nombre = rset.getString("nombre");   
                apellidos = rset.getString("apellidos");
            }
            
            fsalida.writeUTF((nombre.equals(""))? "No se ha encontrado ningún usuario con ese nombre":(nombre+","+apellidos));
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
