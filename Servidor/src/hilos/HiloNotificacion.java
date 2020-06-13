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
public class HiloNotificacion extends Thread {

    private String url = "jdbc:mariadb://localhost:3306/proyecto";
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String c;

    public HiloNotificacion(DataOutputStream dot, String correo) {
        this.fsalida = dot;
        this.c = correo;
    }

    @Override
    public void run() {
        String franja = "";
        String desc = "";
        String mensaje = "";
        try ( Connection conexion = DriverManager.getConnection(url, username, password);  PreparedStatement p = conexion.prepareStatement("select h.franja as franja, pi.descripcion as desc\n"
                + "from usuario_partido up, partido p, horario h, pista pi\n"
                + "where up.usuario_id = (Select usuario_id from usuario where correo = (?))\n"
                + "AND up.partido_id = p .partido_id\n"
                + "and p.horario_id = h.horario_id\n"
                + "and p.pista_id = pi.pista_id\n"
                + "and p.fecha = DATE(NOW()))");) {
            p.setString(1, c);
            ResultSet rset = p.executeQuery();
            while (rset.next()) {
                franja = rset.getString("franja");
                desc = rset.getString("desc");
                mensaje += franja+","+desc+";";
            }

            fsalida.writeUTF((mensaje.equals("")) ? "No hay notificaciones" : mensaje);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloCompruebaCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
