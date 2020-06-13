package com.example.muvon.modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Conexion {

    private static Conexion conn;
    private static Socket socket;
    private static String host = "192.168.1.81";
    private static int puerto = 4444;
    private static DataOutputStream fsalida;
    private static DataInputStream fentrada;

    private Conexion() {
    }

    public Socket getSocket() {
        return socket;
    }

    public static DataOutputStream getFsalida() {
        return fsalida;
    }

    public static DataInputStream getFentrada() {
        return fentrada;
    }

    public static Conexion getSingletonInstance() {
        conn = new Conexion();
        try {
            Conexion.socket = new Socket(host, puerto);
            Conexion.fsalida = new DataOutputStream(Conexion.socket.getOutputStream());
            Conexion.fentrada = new DataInputStream(Conexion.socket.getInputStream());
            System.out.println("LA ENTRADAAAAA : " + Conexion.fentrada.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
