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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class HiloBorrarCliente extends Thread{
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String c;
 
    public HiloBorrarCliente(DataOutputStream dot,String correo){
        this.fsalida = dot;
        this.c = correo;
    }
    
    @Override
    public void run() {
        String mensaje = "";
        
        try (Connection conexion = DriverManager.getConnection(url, username, password);
             PreparedStatement p = conexion.prepareStatement("DELETE FROM usuario where correo = (?)");) {

            p.setString(1, c);
            int afectados = p.executeUpdate();            
            fsalida.writeUTF((afectados == 1)? "OK":"");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloListaClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
