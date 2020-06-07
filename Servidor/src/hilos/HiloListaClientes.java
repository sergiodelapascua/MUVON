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
public class HiloListaClientes extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
 
    public HiloListaClientes(DataOutputStream dot){
        this.fsalida = dot;
    }
    
    @Override
    public void run() {
        String nombre = "";
        String apellidos = "";
        String correo = "";
        String mensaje = "";
        
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            Statement sentencia = conexion.createStatement();
            ResultSet rset = sentencia.executeQuery("SELECT nombre, apellidos, correo FROM usuario");) {
            while (rset.next()) {
                nombre = rset.getString("nombre");   
                apellidos = rset.getString("apellidos");
                correo = rset.getString("correo");
                
                mensaje += nombre + "," + apellidos + "," + correo + ";";
            }
            
            //System.out.println(mensaje);
            fsalida.writeUTF((mensaje.equals(""))? "No se han encontrado clientes almacenados":mensaje);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloListaClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
