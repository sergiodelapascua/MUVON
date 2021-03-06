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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class HiloListaReservaOrdenada extends Thread {

    private String url = "jdbc:mariadb://localhost:3306/proyecto";
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String busqueda;

    public HiloListaReservaOrdenada(DataOutputStream dot, String s) {
        this.fsalida = dot;
        this.busqueda = s;
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
                Statement sentencia = conexion.createStatement();  
                ResultSet rset = sentencia.executeQuery("SELECT p.partido_id, u.nombre, u.apellidos, pi.descripcion, h.franja, p.fecha, COUNT(up.partido_id)+p.num_jugadores_inicio as jugadores\n"
                + "FROM usuario u, partido p, horario h, pista pi, usuario_partido up\n"
                + "WHERE p.pista_id = pi.pista_id\n"
                + "AND p.horario_id = h.horario_id\n"
                + "AND u.usuario_id = p.usuario_id\n"
                + "AND u.usuario_id IN (SELECT usuario_id FROM usuario WHERE nombre LIKE '%"+busqueda+"%' OR apellidos LIKE '%"+busqueda+"%' OR correo LIKE '%"+busqueda+"%')\n"
                + "AND p.partido_id = up.partido_id\n"
                + "AND p.fecha >= DATE(NOW())\n"
                + "GROUP BY up.partido_id");) {
            while (rset.next()) {
                id = rset.getInt("partido_id");
                nombre = rset.getString("nombre") + " " + rset.getString("apellidos");
                pista = rset.getString("descripcion");
                horario = rset.getString("franja");
                fecha = rset.getString("fecha");
                jugadores = rset.getInt("jugadores");

                mensaje += id + "," + nombre + "," + pista + "," + horario + "," + fecha + "," + jugadores + ";";
            }

            //System.out.println(mensaje);
            fsalida.writeUTF((mensaje.equals("")) ? "No se han encontrado reservas almacenadas" : mensaje);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(HiloListaClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
