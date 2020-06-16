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
            while (true) {
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
                        extraerListaClientes(mensajes[1]);
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
                    case "10":
                        loginMovil(mensajes);
                        break;
                    case "11":
                        listarParticipantes(mensajes[1]);
                        break;
                    case "12":
                        perfilUsuario(mensajes[1]);
                        break;
                    case "13":
                        invitados(mensajes[1]);
                        break;
                    case "14":
                        partidoDeUsuario(mensajes[1]);
                        break;
                    case "15":
                        comprobarNotificaciones(mensajes[1]);
                        break;
                    case "16":
                        unirsePartido(mensajes[1], mensajes[2]);
                        break;
                    case "17":
                        abandonarPartido(mensajes[1], mensajes[2]);
                        break;
                    case "18":
                        extraerListaPorDeporte(mensajes[1]);
                        break;
                    case "19":
                        listaReservasOrdenada(mensajes[1]);
                        break;
                    default:
                        System.out.println("Se ha liado en el hilo que atiende al usuario");
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
            HiloLogin hl = new HiloLogin(fsalida, mensajes[1], mensajes[2]);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loginMovil(String[] mensajes) {
        try {
            HiloLogin hl = new HiloLogin(fsalida, mensajes[1], mensajes[2], true);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void crearUsuario(String[] mensajes) {
        try {
            //nombre, apellido, correo, pwd, sexo, futbol, padel, basket, balonmano
            HiloInsertarUsuario hl = new HiloInsertarUsuario(fsalida, mensajes[1], mensajes[2], mensajes[3], mensajes[4], mensajes[5], mensajes[6], mensajes[7], mensajes[8], mensajes[9]);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void comprobarCorreo(String correo) {
        try {
            HiloCompruebaCorreo hl = new HiloCompruebaCorreo(fsalida, correo);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void extraerListaClientes(String busqueda) {
        HiloListaClientes hl = null;
        try {
            if (busqueda.equals(" ")) {
                hl = new HiloListaClientes(fsalida);
            } else {
                hl = new HiloListaClientes(fsalida, busqueda);
            }
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void borrarCliente(String correo) {
        try {
            HiloBorrarCliente hl = new HiloBorrarCliente(fsalida, correo);
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
            HiloBorrarReserva hl = new HiloBorrarReserva(fsalida, Integer.parseInt(id));
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscarPistasOcupadas(String deporte_id, String date) {
        try {
            HiloPistasOcupadas hl = new HiloPistasOcupadas(fsalida, Integer.parseInt(deporte_id), date);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void crearReserva(String correo, String pista_id, String horario_id, String date, String max, String base) {
        try {
            HiloCrearReserva hl = new HiloCrearReserva(fsalida, correo, pista_id, horario_id, date, max, base);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listarParticipantes(String mensaje) {
        try {
            HiloListaParticipantes hl = new HiloListaParticipantes(fsalida, mensaje);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void perfilUsuario(String mensaje) {
        try {
            HiloPerfilUsuario hl = new HiloPerfilUsuario(fsalida, mensaje);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void invitados(String mensaje) {
        try {
            HiloInvitados hl = new HiloInvitados(fsalida, mensaje);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void partidoDeUsuario(String mensaje) {
        try {
            HiloListaPartidosCreadoPorUsuario hl = new HiloListaPartidosCreadoPorUsuario(fsalida, mensaje);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void comprobarNotificaciones(String mensaje) {
        try {
            HiloNotificacion hl = new HiloNotificacion(fsalida, mensaje);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void unirsePartido(String correo, String idp) {
        try {
            HiloUnirsePartido hl = new HiloUnirsePartido(fsalida, correo, idp);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void abandonarPartido(String correo, String idp) {
        try {
            HiloAbandonarPartido hl = new HiloAbandonarPartido(fsalida, correo, idp);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void extraerListaPorDeporte(String idp) {
        try {
            HiloListaReservaPorDeporte hl = new HiloListaReservaPorDeporte(fsalida, idp);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaReservasOrdenada(String mensaje) {
        try {
            HiloListaReservaOrdenada hl = new HiloListaReservaOrdenada(fsalida, mensaje);
            hl.run();
            hl.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAtiendeUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
