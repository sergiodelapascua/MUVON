/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
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
                    case "3":
                        comprobarCorreo(mensajes[1]);
                        break;
                    case "4":
                        extraerListaClientes();
                        break;
                    case "5":
                        borrarCliente(mensajes[1]);
                        break;
                    case "6":
                        extraerListaReservas();
                        break;
                    case "7":
                        borrarReserva(mensajes[1]);
                        break;
                    case "8":
                        buscarPistasOcupadas(mensajes[1], mensajes[2]);
                        break;
                    case "9":
                        crearReserva(mensajes[1], mensajes[2], mensajes[3], mensajes[4], mensajes[5], mensajes[6]);
                        break;
                    default:
                        System.out.println("Se ha liado");
                        System.exit(0);
                }                    
            }        
        } catch (EOFException e) {
            //Se ha cerrao un cliente inesperadamente
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
            //nombre, apellido, correo, pwd, sexo, futbol, padel, basket, balonmano
            HiloInsertarUsuario hl = new HiloInsertarUsuario(fsalida,mensajes[1], mensajes[2], mensajes[3], mensajes[4], mensajes[5],mensajes[6], mensajes[7], mensajes[8], mensajes[9]);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void comprobarCorreo(String correo){
        try {
            HiloCompruebaCorreo hl = new HiloCompruebaCorreo(fsalida,correo);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void extraerListaClientes() {
        try {
            HiloListaClientes hl = new HiloListaClientes(fsalida);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void borrarCliente(String correo) {
        try {
            HiloBorrarCliente hl = new HiloBorrarCliente(fsalida,correo);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void extraerListaReservas() {
        try {
            HiloListaReservas hl = new HiloListaReservas(fsalida);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void borrarReserva(String id) {
        try {
            HiloBorrarReserva hl = new HiloBorrarReserva(fsalida,Integer.parseInt(id));
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscarPistasOcupadas(String deporte_id, String date) {
        try {
            HiloPistasOcupadas hl = new HiloPistasOcupadas(fsalida,Integer.parseInt(deporte_id),date);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void crearReserva(String correo, String pista_id, String horario_id, String date, String max, String base) {
        try {
            HiloCrearReserva hl = new HiloCrearReserva(fsalida,correo, pista_id, horario_id, date, max, base);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
