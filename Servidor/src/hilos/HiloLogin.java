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
public class HiloLogin extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String c;
    private String pwd;
    private boolean movil;
 
    public HiloLogin(DataOutputStream dot,String correo, String p){
        this.fsalida = dot;
        this.c = correo;
        this.pwd = p;
        this.movil = false;
    }
 
    public HiloLogin(DataOutputStream dot,String correo, String p, boolean m){
        this.fsalida = dot;
        this.c = correo;
        this.pwd = p;
        this.movil = m;
    }
    
    @Override
    public void run() {
        boolean admin = false;
        String nombre = "";
        if (!movil){
            try (Connection conexion = DriverManager.getConnection(url, username, password);
                PreparedStatement p = conexion.prepareStatement("SELECT administrador FROM usuario where correo = (?) and pwd = (?)");) {
                p.setString(1, c);
                p.setString(2, encriptarPwd(pwd));
                ResultSet rset = p.executeQuery();
                while (rset.next()) {
                    admin = rset.getBoolean("administrador");            
                }


                fsalida.writeUTF((admin)? "true":"false");

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(HiloLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try (Connection conexion = DriverManager.getConnection(url, username, password);
                PreparedStatement p = conexion.prepareStatement("SELECT nombre FROM usuario where correo = (?) and pwd = (?)");) {
                p.setString(1, c);
                p.setString(2, encriptarPwd(pwd));
                ResultSet rset = p.executeQuery();
                while (rset.next()) {
                    nombre = rset.getString("nombre");            
                }


                fsalida.writeUTF((!nombre.equals(""))? "true":"false");

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(HiloLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    private String encriptarPwd(String password){
        MessageDigest md = null;
        byte[] mb = null;
        try {
            
            //SHA-512
            md= MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            mb = md.digest();
            //System.out.println(String.valueOf(Hex.encodeHex(mb)));
            
        } catch (NoSuchAlgorithmException e) {
            //Error
        }
        
        return String.valueOf(Hex.encodeHex(mb));
    }
}
