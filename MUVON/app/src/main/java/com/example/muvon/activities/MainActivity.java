package com.example.muvon.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.muvon.R;
import com.example.muvon.Util.Constantes;
import com.example.muvon.modelo.Cliente;
import com.example.muvon.modelo.Conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements Constantes {

    DataOutputStream fsalida = null;
    DataInputStream fentrada = null;
    static Socket socket = null;
    TextView click;
    Button botonIniciarSesion;
    EditText correo;
    EditText pwd;
    boolean conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        conexion = true;
        try {
            //System.out.println(0);
            socket = new Socket(host, puerto);
            //System.out.println(1);
            fsalida = new DataOutputStream(socket.getOutputStream());
            //System.out.println(2);
            fentrada = new DataInputStream(socket.getInputStream());
            //System.out.println(3);
            String descartar = fentrada.readUTF();
            //System.out.println("LA ENTRADAAAAA : " + descartar);
        } catch (ConnectException ex) {
            conexion = false;
            System.err.println("No se pudo realizar conexión con el servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (conexion) {
            click = (TextView) findViewById(R.id.textViewCrearCuenta);
            botonIniciarSesion = (Button) findViewById(R.id.iniciar_sesion);
            correo = findViewById(R.id.editTextCorreo);
            pwd = findViewById(R.id.editTextContrasena);

            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(MainActivity.this, Registro.class);
                                    startActivity(intent);
                                    try {
                                        socket.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    finish();
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


            botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!correo.getText().equals("") & !pwd.getText().equals("")) {
                        try {
                            fsalida.writeUTF("10," + correo.getText() + "," + pwd.getText());
                            String mensaje = "";
                            Cliente cl = null;
                            mensaje = fentrada.readUTF();
                            //System.out.println("EL MENSAJE: " + mensaje);
                            if (!mensaje.equals("false")) {
                                cl = new Cliente(mensaje+" ," + correo.getText());
                                if (cl != null) {
                                    Intent intent = new Intent(MainActivity.this, Principal.class);
                                    intent.putExtra("cliente", cl);
                                    startActivity(intent);
                                    socket.close();
                                    finish();
                                }
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
                                builder.setMessage("Error al iniciar sesión")
                                        .setPositiveButton("Aceptar", dialogClickListener).show();
                            }
                        } catch (NullPointerException ex) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            System.exit(0);
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //ignorar
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("¡ERROR! Fallo al conectar con el servidor")
                                    .setPositiveButton("Aceptar", dialogClickListener).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            System.exit(0);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //ignorar
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¡ERROR! Fallo al conectar con el servidor")
                    .setPositiveButton("Aceptar", dialogClickListener).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        conectar();
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
}
