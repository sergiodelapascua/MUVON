/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author sergio
 */
public class HiloInsertarUsuario extends Thread{
    
    private String url = "jdbc:mariadb://localhost:3306/proyecto";    
    private String username = "root";
    private String password = "";
    private DataOutputStream fsalida;
    private String nombre;
    private String apellidos;
    private String c;
    private String pwd;
    private int sexo;
    private int futlvl;
    private int padlvl;
    private int baslvl;
    private int ballvl;
 
    public HiloInsertarUsuario(DataOutputStream dot, String no, String ap, String correo, String p, String sex, String nivelFutbol,
            String nivelPadel, String nivelBasket, String nivelBalonmano){
        this.fsalida = dot;
        this.nombre = no;
        this.apellidos = ap;
        this.c = correo;
        this.pwd = p;
        this.sexo = Integer.parseInt(sex);
        this.futlvl = Integer.parseInt(nivelFutbol);
        this.padlvl = Integer.parseInt(nivelPadel);
        this.baslvl = Integer.parseInt(nivelBasket);
        this.ballvl = Integer.parseInt(nivelBalonmano);
    }
    
    @Override
    public void run() {
        int inserciones = 0;
        try (Connection conexion = DriverManager.getConnection(url, username, password);
            Statement sentencia = conexion.createStatement();
            PreparedStatement p = conexion.prepareStatement("INSERT INTO usuario (nombre, apellidos, correo, pwd, avatar_id) VALUES (?,?,?,?,?)");) {
            p.setString(1, nombre);
            p.setString(2, apellidos);
            p.setString(3, c);
            p.setString(4, encriptarPwd(pwd));
            p.setInt(5, sexo);
            inserciones = p.executeUpdate();
            
            
            ResultSet resultado = sentencia.executeQuery("SELECT MAX(usuario_id) FROM usuario");
            int id = -1;
            while (resultado.next()) {
                id = resultado.getInt(1);
            }
            if (inserciones == 1){
                if(futlvl != 0){
                    String consulta = "INSERT INTO usuario_deporte VALUES (" + id + ", 1 , " + futlvl + ")";
                    sentencia.executeUpdate(consulta);
                }
                //System.out.println("futbol dentro");
                if(padlvl != 0){
                    String consulta = "INSERT INTO usuario_deporte VALUES (" + id + ", 2 , " + padlvl + ")";
                    sentencia.executeUpdate(consulta);
                }
                //System.out.println("padel ok");
                if(baslvl != 0){
                    String consulta = "INSERT INTO usuario_deporte VALUES (" + id + ", 3 , " + baslvl + ")";
                    sentencia.executeUpdate(consulta);
                }
                //System.out.println("");
                if(ballvl != 0){
                    String consulta = "INSERT INTO usuario_deporte VALUES (" + id + ", 4 , " + ballvl + ")";
                    sentencia.executeUpdate(consulta);
                }
            }
            fsalida.writeUTF((inserciones == 1)? "Insertado nuevo usuario correctamente":"Fallo al insertar el usuario");
            
        } catch (SQLIntegrityConstraintViolationException e) {
                System.err.println("\n------------------------------------------");
                System.err.println("Correo repetido, introduzca otra por favor."); //IMPLEMENTAR
                System.err.println("------------------------------------------\n");
            try {
                fsalida.writeUTF("Correo repetido");
            } catch (IOException ex) {
                Logger.getLogger(HiloInsertarUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch (SQLException ex) {
            Logger.getLogger(HiloInsertarUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloLogin.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    private String encriptarPwd(String password){
        MessageDigest md = null;
        byte[] mb = null;
        try {
            
            //SHA-512
            md= MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            mb = md.digest();
            //System.out.println(String.valueOf(Hex.encodeHex(mb)));
            
        } catch (NoSuchAlgorithmException e) {
            //Error
        }
        
        return String.valueOf(Hex.encodeHex(mb));
    }
}
