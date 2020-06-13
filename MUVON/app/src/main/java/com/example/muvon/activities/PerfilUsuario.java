package com.example.muvon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muvon.R;
import com.example.muvon.adapters.AdapterPartido;
import com.example.muvon.modelo.Cliente;
import com.example.muvon.modelo.Reserva;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PerfilUsuario extends AppCompatActivity {

    private static Cliente cliente;
    String host = "192.168.1.81";
    int puerto = 4444;
    DataOutputStream fsalida = null;
    DataInputStream fentrada = null;
    static Socket socket = null;

    TextView nombre;
    ImageView futbol;
    ImageView padel;
    ImageView basket;
    ImageView balonmano;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        Intent intent = getIntent();
        cliente = (Cliente) intent.getSerializableExtra("usuario");

        nombre = findViewById(R.id.nombre_perfil_usuario);
        nombre.setText(cliente.getNombre()+ " "+cliente.getApellidos());
        futbol = findViewById(R.id.futbol_estrellas_usuario);
        padel = findViewById(R.id.padel_estrellas_usuario);
        basket = findViewById(R.id.basket_estrellas_usuario);
        balonmano = findViewById(R.id.balonmano_estrellas_usuario);

        conectar();
        escribir("12,"+cliente.getCorreo());
        String mensaje = leer();
        //System.out.println("LO QUE HA LEIDOOOOOOO\n"+mensaje);
        String[] arg = mensaje.split(";");
        interpretarArgumentos(arg);

        recyclerView = findViewById(R.id.recyclerViewPartidosPerfilActivity);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        AdapterPartido a = new AdapterPartido(pedirLista(), R.layout.fila_partidos, this, new AdapterPartido.OnItemClickListener() {
            @Override
            public void onItemClick(Reserva reserva, int position) {
                Intent intent = new Intent(PerfilUsuario.this, DetallesPartido2.class);
                intent.putExtra("reserva", reserva);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(a);

    }

    private List<Reserva> pedirLista(){
        List<Reserva> lista = new ArrayList<>();
        String[] mensajes = null;
        escribir("14,"+cliente.getCorreo());

        String mensaje = "";
        mensaje = leer();
        if (mensaje.equals("No se han encontrado reservas almacenadas")) {
            System.err.println("ERROR al buscar partidos del ususario "+cliente.toString());
        } else {
            mensajes = mensaje.split(";");
            for (String arg : mensajes) {
                lista.add(new Reserva(arg));
            }
            System.out.println("RESERVAS "+lista);
            return lista;
        }
        return null;
    }

    private void interpretarArgumentos(String[] arg) {
        for (int i = 0; i < arg.length; i++){
            String[] info = arg[i].split(",");
            if(info[0].equals("1")) //futbol
                futbol.setBackgroundResource(imagenNivel(info[1]));
            else if(info[0].equals("2")) //padel
                padel.setBackgroundResource(imagenNivel(info[1]));
            else if(info[0].equals("3")) //basket
                basket.setBackgroundResource(imagenNivel(info[1]));
            else if(info[0].equals("4")) //balonmano
                balonmano.setBackgroundResource(imagenNivel(info[1]));

        }
    }

    private int imagenNivel(String lvl){
        switch (lvl){
            case "1":
                return  R.drawable.uno_stars_ic;
            case "2":
                return  R.drawable.dos_stars_ic;
            case "3":
                return  R.drawable.tres_stars_ic;
            case "4":
                return  R.drawable.cuatro_stars_ic;
            case "5":
                return  R.drawable.cinco_stars_ic;
        }
        return -1;
    }

    public void escribir(String mensaje) {
        try {
            fsalida.writeUTF(mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("MENSAJE QUE SALE "+mensaje);
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