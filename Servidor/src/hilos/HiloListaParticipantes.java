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
public class HiloListaParticipantes extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private int idp;
 
    public HiloListaParticipantes(DataOutputStream dot,String partido){
        this.fsalida = dot;
        this.idp = Integer.parseInt(partido);
    }
    
    @Override
    public void run() {
        String nombre = "";
        String apellidos = "";
        String correo = "";
        String mensaje = "";
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            PreparedStatement p = conexion.prepareStatement("SELECT u.nombre, u.apellidos, u.correo FROM usuario u, usuario_partido up where up.partido_id = (?) AND up.usuario_id = u.usuario_id");) {
            p.setInt(1, idp);
            ResultSet rset = p.executeQuery();
            while (rset.next()) {
                nombre = rset.getString("nombre");   
                apellidos = rset.getString("apellidos");
                correo = rset.getString("correo");
                mensaje += nombre + "," + apellidos + "," + correo + ";";
                //System.out.println(mensaje);
            }
            System.out.println(mensaje);
            fsalida.writeUTF((mensaje.equals(""))? "Error":mensaje);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
