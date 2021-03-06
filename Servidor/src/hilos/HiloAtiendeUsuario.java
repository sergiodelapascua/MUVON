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
public class HiloAtiendeUsuario extends Thread implements Constantes{

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
                    case LOGIN:
                        login(mensajes);
                        break;
                    case CREAR_USUARIO:
                        crearUsuario(mensajes);
                        break;
                    case COMPROBAR_CORREO:
                        comprobarCorreo(mensajes[1]);
                        break;
                    case LISTA_CLIENTES:
                        extraerListaClientes(mensajes[1]);
                        break;
                    case BORRAR_CLIENTE:
                        borrarCliente(mensajes[1]);
                        break;
                    case LISTA_RESERVAS:
                        extraerListaReservas();
                        break;
                    case BORRAR_RESERVA:
                        borrarReserva(mensajes[1]);
                        break;
                    case BUSCAR_INFO_PISTAS:
                        buscarPistasOcupadas(mensajes[1], mensajes[2]);
                        break;
                    case CREAR_RESERVA:
                        crearReserva(mensajes[1], mensajes[2], mensajes[3], mensajes[4], mensajes[5], mensajes[6]);
                        break;
                    case LOGIN_MOVIL:
                        loginMovil(mensajes);
                        break;
                    case LISTA_PARTICIPANTES:
                        listarParticipantes(mensajes[1]);
                        break;
                    case PERFIL_USUARIO:
                        perfilUsuario(mensajes[1]);
                        break;
                    case INVITADOS:
                        invitados(mensajes[1]);
                        break;
                    case PARTIDOS_USUARIO:
                        partidoDeUsuario(mensajes[1]);
                        break;
                    case NOTIFICACIONES:
                        comprobarNotificaciones(mensajes[1]);
                        break;
                    case UNIRSE_PARTIDO:
                        unirsePartido(mensajes[1], mensajes[2]);
                        break;
                    case ABANDONAR_PARTIDO:
                        abandonarPartido(mensajes[1], mensajes[2]);
                        break;
                    case LISTA_DEPORTE_FILTRADO:
                        extraerListaPorDeporte(mensajes[1]);
                        break;
                    case LISTA_FILTRADA_RESERVA:
                        listaReservasOrdenada(mensajes[1]);
                        break;
                    case HISTORIAL:
                        historial(mensajes[1]);
                        break;
                    case HISTORIAL_ORDENADO:
                        extraerHistorialPorDeporte(mensajes[1], mensajes[2]);
                        break;
                    case RESERVAS:
                        listarTodasLasReservas();
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

    private void listarTodasLasReservas() {
        try {
            HiloTodasLasReservas hl = new HiloTodasLasReservas(fsalida);
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

    private void historial(String mensaje) {
        try {
            HiloHistorial hl = new HiloHistorial(fsalida, mensaje);
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

    private void extraerHistorialPorDeporte(String idp, String c) {
        try {
            HiloHistorialPorDeporte hl = new HiloHistorialPorDeporte(fsalida, idp, c);
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
