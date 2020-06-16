package com.example.muvon.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muvon.R;
import com.example.muvon.Util.Constantes;
import com.example.muvon.adapters.AdapterParticipantesPartido;
import com.example.muvon.modelo.Cliente;
import com.example.muvon.modelo.Reserva;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DetallesPartido extends AppCompatActivity implements Constantes {

    private Reserva reserva;
    private Cliente clienteLogin;
    TextView partido;
    TextView pista;
    TextView fecha;
    TextView horario;
    TextView jugadores;
    TextView invitados;
    ImageView icono;
    Button unirse;

    DataOutputStream fsalida = null;
    DataInputStream fentrada = null;
    static Socket socket = null;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private boolean jugando;
    private boolean propietario;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //System.out.println("HA ENTRADO EN DETALLES1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_partido);

        Intent intent = getIntent();
        reserva = (Reserva) intent.getSerializableExtra("reserva");
        clienteLogin = (Cliente) intent.getSerializableExtra("cliente");

        /*System.out.println(reserva.getNombre());
        System.out.println("Nombre cliente "+clienteLogin.getNombre()+" "+ clienteLogin.getApellidos());
        System.out.println();*/

        if (reserva.getNombre().trim().equals(clienteLogin.getNombre().trim() + " " + clienteLogin.getApellidos().trim()))
            propietario = true;
        jugando = false;

        //System.out.println("propietario"+propietario);

        partido = findViewById(R.id.partido_de);
        icono = findViewById(R.id.icono_detalle);
        icono.setBackgroundResource(elegirImagen(reserva.getPista()));
        partido.setText("Partido de " + reserva.getNombre());
        pista = findViewById(R.id.pista_partido_details);
        pista.setText(reserva.getPista());
        fecha = findViewById(R.id.fecha_partido_details);
        fecha.setText(reserva.getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        horario = findViewById(R.id.horario_partido_details);
        horario.setText(reserva.getHorario());
        jugadores = findViewById(R.id.capacidad_partido_details);
        jugadores.setText("" + reserva.getJugadores());
        invitados = findViewById(R.id.invitados);
        unirse = findViewById(R.id.unirse);

        recyclerView = findViewById(R.id.recyclerViewParticipantesPartido);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        if (propietario)
            unirse.setText("Borrar Partido");

        unirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!propietario) {
                    if (!jugando)
                        escribir("16," + clienteLogin.getCorreo() + "," + reserva.getId());
                    else
                        escribir("17," + clienteLogin.getCorreo() + "," + reserva.getId());

                    String mensaje = leer();
                    if (mensaje.equals("OK")) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //finish();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //ignorar
                                        break;
                                }
                            }
                        };

                        AdapterParticipantesPartido a = new AdapterParticipantesPartido(pedirLista(), R.layout.fila_participantes, DetallesPartido.this, new AdapterParticipantesPartido.OnItemClickListener() {
                            @Override
                            public void onItemClick(Cliente cliente, int position) {
                                Intent intent = new Intent(DetallesPartido.this, PerfilUsuario.class);
                                intent.putExtra("usuario", cliente);
                                intent.putExtra("login", clienteLogin);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Te has unido al partido")
                                .setPositiveButton("Aceptar", dialogClickListener).show();
                    } else if (mensaje.equals("Se ha borrado")) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        AdapterParticipantesPartido a = new AdapterParticipantesPartido(pedirLista(), R.layout.fila_participantes, DetallesPartido.this, new AdapterParticipantesPartido.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(Cliente cliente, int position) {
                                                Intent intent = new Intent(DetallesPartido.this, PerfilUsuario.class);
                                                intent.putExtra("usuario", cliente);
                                                intent.putExtra("login", clienteLogin);
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
                                        //finish();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //ignorar
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Se te ha borrado del partido")
                                .setPositiveButton("Aceptar", dialogClickListener).show();
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //ignorar
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //ignorar
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("ERROR, no se puedo realizar la operación")
                                .setPositiveButton("Aceptar", dialogClickListener).show();
                    }
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    escribir("7," + reserva.getId());
                                    leer();
                                    finish();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //ignorar
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("¿Estás segur@ de querer eliminar el partido?")
                            .setPositiveButton("Aceptar", dialogClickListener)
                            .setNegativeButton("Cancelar", dialogClickListener).show();

                    AdapterParticipantesPartido a = new AdapterParticipantesPartido(pedirLista(), R.layout.fila_participantes, DetallesPartido.this, new AdapterParticipantesPartido.OnItemClickListener() {
                        @Override
                        public void onItemClick(Cliente cliente, int position) {
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

            }
        });

        conectar();

        invitados();
        AdapterParticipantesPartido a = new AdapterParticipantesPartido(pedirLista(), R.layout.fila_participantes, this, new AdapterParticipantesPartido.OnItemClickListener() {
            @Override
            public void onItemClick(Cliente cliente, int position) {
                Intent intent = new Intent(DetallesPartido.this, PerfilUsuario.class);
                intent.putExtra("usuario", cliente);
                intent.putExtra("login", clienteLogin);
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
        escribir("13," + reserva.getId());
        invitados.setText(leer());
    }

    public void conectar() {
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

    public String leer() {
        String mensaje = "";
        try {
            mensaje = fentrada.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mensaje;
    }

    private List<Cliente> pedirLista() {
        jugando = false;
        List<Cliente> lista = new ArrayList<>();
        String[] mensajes = null;
        escribir("11," + reserva.getId());

        String mensaje = "";
        mensaje = leer();
        //System.out.println("PARTICIPANTES\n"+mensaje);
        if (mensaje.equals("Error")) {
            System.out.println("PROBLEMOOON al pedir la lista de jugadores en la activity DetallesPartidos");
        } else {
            mensajes = mensaje.split(";");
            for (String arg : mensajes) {
                Cliente c = new Cliente(arg);
                lista.add(new Cliente(arg));
                if (c.getCorreo().equals(clienteLogin.getCorreo()))
                    jugando = true;
            }
            if (!propietario) {
                if (jugando)
                    unirse.setText("Abandonar");
                else
                    unirse.setText("Unirse");
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

    private int elegirImagen(String pista) {
        System.out.println("NOMBRE DE LA PISTA "+pista);
        if(pista.contains("Padel"))
            return R.drawable.padel;
        else if(pista.contains("Futbol"))
            return R.drawable.futbol;
        else if(pista.contains("Basket"))
            return R.drawable.basket;
        return 0;
    }
}