package com.example.muvon.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.muvon.R;
import com.example.muvon.adapters.AdapterParticipantesPartido;
import com.example.muvon.adapters.AdapterPartido;
import com.example.muvon.modelo.Cliente;
import com.example.muvon.modelo.Reserva;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DetallesPartido extends AppCompatActivity {

    private Reserva reserva;
    TextView partido;
    TextView pista;
    TextView fecha;
    TextView horario;
    TextView jugadores;
    TextView invitados;

    String host = "192.168.1.81";
    int puerto = 4444;
    DataOutputStream fsalida = null;
    DataInputStream fentrada = null;
    static Socket socket = null;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_partido);

        Intent intent = getIntent();
        reserva = (Reserva) intent.getSerializableExtra("reserva");

        System.out.println(reserva);

        partido = findViewById(R.id.partido_de);
        partido.setText("Partido de "+reserva.getNombre());
        pista = findViewById(R.id.pista_partido_details);
        pista.setText(reserva.getPista());
        fecha = findViewById(R.id.fecha_partido_details);
        fecha.setText(reserva.getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        horario = findViewById(R.id.horario_partido_details);
        horario.setText(reserva.getHorario());
        jugadores = findViewById(R.id.capacidad_partido_details);
        jugadores.setText(""+reserva.getJugadores());
        invitados = findViewById(R.id.invitados);

        recyclerView = findViewById(R.id.recyclerViewParticipantesPartido);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        conectar();

        invitados();
        AdapterParticipantesPartido a = new AdapterParticipantesPartido(pedirLista(), R.layout.fila_participantes, this, new AdapterParticipantesPartido.OnItemClickListener() {
            @Override
            public void onItemClick(Cliente cliente, int position) {
                Intent intent = new Intent(DetallesPartido.this,PerfilUsuario.class);
                intent.putExtra("usuario", cliente);
                startActivity(intent);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                //System.out.println(cliente.toString());
            }
        });
        recyclerView.setAdapter(a);
    }

    private void invitados() {
        escribir("13,"+reserva.getId());
        invitados.setText(leer());
    }

    public void conectar(){
        try {
            socket = new Socket(host, puerto);
            fsalida = new DataOutputStream(socket.getOutputStream());
            fentrada = new DataInputStream(socket.getInputStream());
            String descartar = fentrada.readUTF();
            //System.out.println("LA ENTRADAAAAA : " + descartar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escribir(String mensaje) {
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

    private List<Cliente> pedirLista(){
        List<Cliente> lista = new ArrayList<>();
        String[] mensajes = null;
        escribir("11,"+reserva.getId());

        String mensaje = "";
        mensaje = leer();
        System.out.println("PARTICIPANTES\n"+mensaje);
        if (mensaje.equals("Error")) {
            System.out.println("PROBLEMOOON");
        } else {
            mensajes = mensaje.split(";");
            for (String arg : mensajes) {
                lista.add(new Cliente(arg));
            }
            return lista;
        }
        return null;
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        conectar();
    }
}