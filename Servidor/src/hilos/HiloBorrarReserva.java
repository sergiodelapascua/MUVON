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
 *
 * @author sergio
 */
public class HiloBorrarReserva extends Thread{
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private int id;
 
    public HiloBorrarReserva(DataOutputStream dot,int id){
        this.fsalida = dot;
        this.id = id;
    }
    
    @Override
    public void run() {
        String mensaje = "";
        
        try (Connection conexion = DriverManager.getConnection(url, username, password);
             PreparedStatement p = conexion.prepareStatement("DELETE FROM partido where partido_id = (?)");) {

            p.setInt(1, id);
            int afectados = p.executeUpdate();            
            fsalida.writeUTF((afectados == 1)? "OK":"");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloListaClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
