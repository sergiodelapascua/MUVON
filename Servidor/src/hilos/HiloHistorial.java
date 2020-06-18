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
public class HiloHistorial extends Thread {

    private String url = "jdbc:mariadb://localhost:3306/proyecto";
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String correo;

    public HiloHistorial(DataOutputStream dot, String c) {
        this.fsalida = dot;
        this.correo = c;
    }

    @Override
    public void run() {
        int id = 0;
        String nombre = "";
        String pista = "";
        String horario = "";
        String fecha = "";
        int jugadores = 1;
        String mensaje = "";

        try ( Connection conexion = DriverManager.getConnection(url, username, password);  
                PreparedStatement p = conexion.prepareStatement("SELECT p.partido_id\n"
                + "FROM usuario u, partido p, usuario_partido up\n"
                + "Where up.usuario_id = (SELECT usuario_id FROM usuario WHERE correo = (?))\n"
                + "AND p.partido_id = up.partido_id\n"
                + "AND p.fecha >= DATE(NOW())\n"
                + "AND p.usuario_id != up.usuario_id\n"
                + "GROUP BY up.partido_id");  
                PreparedStatement p2 = conexion.prepareStatement("SELECT p.partido_id, u.nombre, u.apellidos, pi.descripcion, h.franja, p.fecha, COUNT(up.partido_id)+p.num_jugadores_inicio as jugadores\n"
                        + "FROM usuario u, partido p, horario h, pista pi, usuario_partido up\n"
                        + "WHERE p.pista_id = pi.pista_id\n"
                        + "AND p.horario_id = h.horario_id\n"
                        + "AND u.usuario_id = p.usuario_id\n"
                        + "AND p.partido_id = up.partido_id\n"
                        + "AND p.partido_id = (?)\n"
                        + "AND p.fecha >= DATE(NOW())\n"
                        + "GROUP BY up.partido_id");) {

            p.setString(1, correo);
            ResultSet rset = p.executeQuery();

            while (rset.next()) {
                id = rset.getInt("partido_id");
                mensaje += id + ",";
            }
            
            if (!mensaje.equals("")) {
                String[] partidos = mensaje.split(",");

                mensaje = "";

                for (String partido : partidos) {
                    int i = Integer.parseInt(partido);
                    p2.setInt(1, i);
                    ResultSet rset2 = p2.executeQuery();
                    while (rset2.next()) {
                        id = rset2.getInt("partido_id");
                        nombre = rset2.getString("nombre") + " " + rset2.getString("apellidos");
                        pista = rset2.getString("descripcion");
                        horario = rset2.getString("franja");
                        fecha = rset2.getString("fecha");
                        jugadores = rset2.getInt("jugadores");

                        mensaje += id + "," + nombre + "," + pista + "," + horario + "," + fecha + "," + jugadores + ";";
                    }

                }
            }
            //System.out.println("Lo que importa: "+mensaje);
            fsalida.writeUTF((mensaje.equals("")) ? "No se han encontrado reservas almacenadas" : mensaje);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloListaClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
