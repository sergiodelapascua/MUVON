package com.example.muvon.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.muvon.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    String host = "192.168.1.149";
    int puerto = 4444;
    DataOutputStream flujosalida = null;
    DataInputStream flujoentrada = null;
    static Socket socket = null;
    TextView click;
    Button botonIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        click = (TextView) findViewById(R.id.textViewCrearCuenta);
        botonIniciarSesion = (Button) findViewById(R.id.iniciar_sesion);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(MainActivity.this, Registro.class);
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //ignorar
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("A continuación tendrás que añadir cierta información para crear tu perfil, ¿estás de acuerdo?")
                        .setPositiveButton("Aceptar", dialogClickListener)
                        .setNegativeButton("Cancelar", dialogClickListener).show();

            }
        });

        /*try {
            socket = new Socket(host, puerto);
            flujosalida = new DataOutputStream(socket.getOutputStream());
            flujoentrada = new DataInputStream(socket.getInputStream());
            System.out.println("LA ENTRADAAAAA : " + flujoentrada.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, Principal.class);
                startActivity(intent);*/
            }
        });



    }
}
