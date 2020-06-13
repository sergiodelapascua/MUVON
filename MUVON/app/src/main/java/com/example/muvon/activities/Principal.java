package com.example.muvon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.muvon.R;
import com.example.muvon.fragments.FragmentPartidos;
import com.example.muvon.modelo.Cliente;
import com.example.muvon.modelo.Reserva;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Principal extends AppCompatActivity {

    public static Cliente cliente;
    String host = "192.168.1.81";
    int puerto = 4444;
    DataOutputStream fsalida = null;
    DataInputStream fentrada = null;
    public static Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setVisibility(View.VISIBLE);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        Intent intent = getIntent();
        cliente = (Cliente) intent.getSerializableExtra("cliente");

        conectar();

        System.out.println(cliente);
    }

    public void escribir(String mensaje) {
        System.out.println("MENSAJE QUE SALE "+mensaje);
        try {
            fsalida.writeUTF(mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String leer(){
        String mensaje = "";
        try {
            mensaje = fentrada.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mensaje;
    }

    public void conectar(){
        try {
            socket = new Socket(host, puerto);
            fsalida = new DataOutputStream(socket.getOutputStream());
            fentrada = new DataInputStream(socket.getInputStream());
            String descartar = fentrada.readUTF();
            //System.out.println("LA ENTRADAAAAA : " + descartar);
        } catch (ConnectException ex) {
            System.err.println("No se pudo realizar conexión con el servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        conectar();
    }
}
