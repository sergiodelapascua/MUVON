/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import hilos.HiloAtiendeUsuario;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author sergio
 */
public class Servidor {

    public static void main(String[] args) {
        Servidor s = new Servidor();
        s.listen();
    }

    public void listen() {
        int puerto = 4444;
        ServerSocket servidor = null;
        try {
            servidor = new ServerSocket(puerto);
        } catch (IOException ex) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }

        System.out.println("Servidor iniciado...");

        while (true) {
            Socket socket1 = null;
            try {

                socket1 = servidor.accept();
                DataOutputStream fsalida = new DataOutputStream(socket1.getOutputStream());
                DataInputStream fentrada = new DataInputStream(socket1.getInputStream());
                fsalida.writeUTF("1");
                
                HiloAtiendeUsuario hilo = new HiloAtiendeUsuario(socket1, fsalida, fentrada);
                hilo.start();

            } catch (IOException ex) {
                System.out.println("SE HA DESCONECTADO EL CLIENTE");
            }
        }
    }
}

