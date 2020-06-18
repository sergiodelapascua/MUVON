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
class HiloInvitados extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private int idp;
 
    public HiloInvitados(DataOutputStream dot,String c){
        this.fsalida = dot;
        this.idp = Integer.parseInt(c);
    }
    
    @Override
    public void run() {
        String mensaje = "";
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            PreparedStatement p = conexion.prepareStatement("SELECT num_jugadores_inicio FROM partido "
                    + "WHERE partido_id = (?)");) {
            p.setInt(1, idp);
            ResultSet rset = p.executeQuery();
            while (rset.next()) {
                mensaje = rset.getString("num_jugadores_inicio"); 
                //System.out.println("Invitados al partido "+mensaje);
            }
            
            fsalida.writeUTF((mensaje.equals(""))? "Error":mensaje);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}