package com.example.muvon.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.muvon.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Registro extends AppCompatActivity {

    EditText correo;
    EditText nombre;
    EditText apellidos;
    EditText pwd1;
    EditText pwd2;
    RadioButton hombre;
    SeekBar futbol;
    SeekBar padel;
    SeekBar basket;
    SeekBar balonmano;
    Button crearUsuario;

    String host = "192.168.1.81";
    int puerto = 4444;
    DataOutputStream fsalida = null;
    DataInputStream fentrada = null;
    static Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_usuario);

        conectar();

        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        correo = findViewById(R.id.correo);
        pwd1 = findViewById(R.id.pwd1);
        pwd2 = findViewById(R.id.pwd2);
        hombre = findViewById(R.id.hombre);
        futbol = findViewById(R.id.seekBarFutbol);
        padel = findViewById(R.id.seekBarPadel);
        basket = findViewById(R.id.seekBarBasket);
        balonmano = findViewById(R.id.seekBarBalonmano);
        crearUsuario = findViewById(R.id.crearUsuario);

        crearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sexo = (hombre.isChecked()) ? 0 : 1;
                String email = String.valueOf(correo.getText());

                // onClick of button perform this simplest code.
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (pwd1.getText().equals(pwd2.getText())) {
                        escribir("2," + nombre.getText() + "," + apellidos.getText() + "," + email + ","
                                + pwd1.getText() + "," + sexo + "," + futbol.getProgress()
                                + "," + padel.getProgress() + "," + basket.getProgress() + "," + balonmano.getProgress());
                        String mensaje = "";
                        mensaje = leer();
                        System.out.println("EL MENSAJE LEIDO: " + mensaje);
                        if (mensaje.equals("Insertado nuevo usuario correctamente")) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Intent intent = new Intent(Registro.this, Principal.class);
                                            startActivity(intent);
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //ignorar
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("Usuario creado")
                                    .setPositiveButton("Aceptar", dialogClickListener).show();
                        } else if (mensaje.equals("Correo repetido")) {
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
                            builder.setMessage("La cuenta de correo ya existe")
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
                            builder.setMessage("¡¡ERROR!!")
                                    .setPositiveButton("Aceptar", dialogClickListener).show();
                        }
                    }else {
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
                        builder.setMessage("¡¡Las contraseñas deben coincidir!!")
                                .setPositiveButton("Aceptar", dialogClickListener).show();
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
                    builder.setMessage("¡¡La cuenta de correo no es válida!!")
                            .setPositiveButton("Aceptar", dialogClickListener).show();
                }
            }
        });
    }

    public void escribir(String mensaje) {
        System.out.println("MENSAJE QUE SALE " + mensaje);
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

    public void conectar() {
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
