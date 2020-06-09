/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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
public class HiloCrearReserva extends Thread {

    private String url = "jdbc:mariadb://localhost:3306/proyecto";
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String correo;
    private int usuario_id;
    private int pista_id;
    private int horario_id;
    private Date fecha;
    private int max;
    private int num;

    public HiloCrearReserva(DataOutputStream dot, String correo, String pista_id, String horario_id, String date, String max, String num) {
        this.fsalida = dot;
        this.correo = correo;
        this.pista_id = Integer.parseInt(pista_id);
        this.horario_id = Integer.parseInt(horario_id);
        this.fecha = Date.valueOf(date);
        this.max = Integer.parseInt(max);
        this.num = Integer.parseInt(num);
    }

    @Override
    public void run() {
        try ( Connection conexion = DriverManager.getConnection(url, username, password); 
            Statement sentencia = conexion.createStatement();
                PreparedStatement p1 = conexion.prepareStatement("SELECT usuario_id FROM usuario where correo = (?)");
                PreparedStatement p2 = conexion.prepareStatement("INSERT INTO partido (usuario_id, pista_id, horario_id, fecha, max_jugadores, num_jugadores_inicio) "
                        + "VALUES (?,?,?,?,?,?)");) {
            p1.setString(1, correo);
            ResultSet rset = p1.executeQuery();
            while (rset.next()) {
                usuario_id = rset.getInt("usuario_id");
            }
                
            p2.setInt(1, usuario_id);
            p2.setInt(2, pista_id);
            p2.setInt(3, horario_id);
            p2.setDate(4, fecha);
            p2.setInt(5, max);
            p2.setInt(6, num);
            
            int insercion = 0;
            insercion  = p2.executeUpdate();
            
            ResultSet resultado = sentencia.executeQuery("SELECT MAX(partido_id) FROM partido");
            int partido_id = -1;
            while (resultado.next()) {
                partido_id = resultado.getInt(1);
            }
            
            String consulta = "INSERT INTO usuario_partido VALUES ("+usuario_id+", "+partido_id+")";
            sentencia.executeUpdate(consulta);            

            fsalida.writeUTF((insercion != 0) ? "OK" : "");

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
