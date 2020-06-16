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
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class HiloPistasOcupadas extends Thread {

    private String url = "jdbc:mariadb://localhost:3306/proyecto";
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private int id;
    private Date fecha;

    public HiloPistasOcupadas(DataOutputStream dot, int deporte_id, String date) {
        this.fsalida = dot;
        this.id = deporte_id;
        //System.out.println("fecha que llega" + date);
        this.fecha = Date.valueOf(date);
        //System.out.println("fecha escogida" + fecha);
    }

    @Override
    public void run() {
        int pista_id = -1;
        int horario_id = -1;
        String mensaje = "";
        try ( Connection conexion = DriverManager.getConnection(url, username, password);  
                PreparedStatement p = conexion.prepareStatement("SELECT pista_id, horario_id\n"
                + "FROM partido\n"
                + "WHERE pista_id IN (SELECT pista_id FROM deporte_pista where deporte_id = (?))\n"
                + "AND fecha = (?)");) {
            p.setInt(1, id);
            p.setDate(2, fecha);
            ResultSet rset = p.executeQuery();
            while (rset.next()) {
                pista_id = rset.getInt("pista_id");
                horario_id = rset.getInt("horario_id");
                
                mensaje += pista_id + "," + horario_id + ";";
            }
            System.out.println("OCUPADAS: "+mensaje);
            fsalida.writeUTF((mensaje.equals("")) ? "No se ha encontrado ninguna reserva ese día" : mensaje);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
