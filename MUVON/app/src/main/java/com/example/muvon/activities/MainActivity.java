package com.example.muvon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import com.example.muvon.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    String host = "192.168.1.149";
    int puerto = 4444;
    DataOutputStream flujosalida = null;
    DataInputStream flujoentrada = null;
    static Socket socket = null;
    TextView click;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        click = (TextView) findViewById(R.id.textViewCrearCuenta);
        txt = (TextView) findViewById(R.id.textViewPrueba);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText("HOLA MUNDO");
            }
        });

        try {
            socket = new Socket(host, puerto);
            flujosalida = new DataOutputStream(socket.getOutputStream());
            flujoentrada = new DataInputStream(socket.getInputStream());
            System.out.println("LA ENTRADAAAAA : " + flujoentrada.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
