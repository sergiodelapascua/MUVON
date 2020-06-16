package com.example.muvon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.muvon.R;
import com.example.muvon.Util.Constantes;
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

public class PerfilUsuario extends AppCompatActivity implements Constantes {

    private static Cliente usuarioRandom;
    private static Cliente clienteLogin;
    DataOutputStream fsalida = null;
    DataInputStream fentrada = null;
    static Socket socket = null;

    TextView nombre;
    ImageView perfil;
    ImageView futbol;
    ImageView padel;
    ImageView basket;
    ImageView balonmano;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //System.out.println("HA ENTRADO EN PERFIL USUARIO");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        Intent intent = getIntent();
        usuarioRandom = (Cliente) intent.getSerializableExtra("usuario");
        clienteLogin = (Cliente) intent.getSerializableExtra("login");

        perfil = findViewById(R.id.imagen_perfil_activity);
        nombre = findViewById(R.id.nombre_perfil_usuario_activity);
        nombre.setText(usuarioRandom.getNombre()+ " "+ usuarioRandom.getApellidos());
        futbol = findViewById(R.id.futbol_estrellas_usuario);
        padel = findViewById(R.id.padel_estrellas_usuario);
        basket = findViewById(R.id.basket_estrellas_usuario);
        balonmano = findViewById(R.id.balonmano_estrellas_usuario);

        conectar();
        escribir("12,"+ usuarioRandom.getCorreo());
        String mensaje = leer();
        //System.out.println("LO QUE HA LEIDOOOOOOO\n"+mensaje);
        String[] arg = mensaje.split(";");
        interpretarArgumentos(arg);
        mensaje = leer();
        //System.out.println("LO QUE HA LEIDOOOOOOO\n"+mensaje);
        cargarFoto(mensaje);

        recyclerView = findViewById(R.id.recyclerViewPartidosPerfilActivity);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        List<Reserva> lista = pedirLista();
        LinearLayout linear = findViewById(R.id.sin_partido_creado_activity);

        if (lista == null) {
            linear.setVisibility(View.VISIBLE);
        } else {
            linear.setVisibility(View.GONE);
            AdapterPartido a = new AdapterPartido(lista, R.layout.fila_partidos, this, new AdapterPartido.OnItemClickListener() {
                @Override
                public void onItemClick(Reserva reserva, int position) {
                    Intent intent = new Intent(PerfilUsuario.this, DetallesPartido2.class);
                    intent.putExtra("reserva", reserva);
                    intent.putExtra("login", clienteLogin);
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(a);
        }
    }

    private void cargarFoto(String mensaje) {
        if(mensaje.equals("1"))
            perfil.setBackgroundResource(R.drawable.female_ic);
        else
            perfil.setBackgroundResource(R.drawable.male_ic);
    }

    private List<Reserva> pedirLista(){
        List<Reserva> lista = new ArrayList<>();
        String[] mensajes = null;
        escribir("14,"+ usuarioRandom.getCorreo());

        String mensaje = "";
        mensaje = leer();
        if (mensaje.equals("No se han encontrado reservas almacenadas")) {
            System.err.println("ERROR al buscar partidos del ususario "+ usuarioRandom.toString());
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