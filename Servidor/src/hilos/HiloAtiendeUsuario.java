/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class HiloAtiendeUsuario extends Thread {

    private DataInputStream fentrada;
    private DataOutputStream fsalida;
    private Socket socket;
    private String[] mensajes;

    public HiloAtiendeUsuario(Socket socket, DataOutputStream dos, DataInputStream dis) {
        this.socket = socket;
        fsalida = dos;
        fentrada = dis;
    }
    
     @Override
    public void run() {
        try {
            while(true){
                mensajes = fentrada.readUTF().split(",");
                switch (mensajes[0]) {
                    case "1":
                        login(mensajes);
                        break;
                    case "2":
                        crearUsuario(mensajes);
                        break;
                    default:
                        System.out.println("Se ha liado");
                        System.exit(0);
                }                    
            }        
        } catch (IOException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
                fsalida.close();
                fentrada.close();
            } catch (IOException ex) {
                Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void login(String[] mensajes) {
        try {
            HiloLogin hl = new HiloLogin(fsalida,mensajes[1], mensajes[2]);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void crearUsuario(String[] mensajes) {
        try {
            HiloInsertarUsuario hl = new HiloInsertarUsuario(fsalida,mensajes[1], mensajes[2], mensajes[3], mensajes[4]);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
