package com.example.muvon.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvon.R;
import com.example.muvon.Util.Constantes;
import com.example.muvon.adapters.AdapterPistaDisponible;
import com.example.muvon.modelo.Cliente;
import com.example.muvon.modelo.PistaDisponible;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NuevoPartido extends AppCompatActivity implements Constantes{

    private Cliente login;

    DataOutputStream fsalida = null;
    DataInputStream fentrada = null;
    static Socket socket = null;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private Spinner spinner;
    private ImageButton fechaButton;
    private Button disponibilidad;
    private EditText fecha_escogida;
    private EditText max;
    private EditText invitados;
    private Button crearReserva;

    private PistaDisponible pistaSeleccionada;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_partido);

        conectar();

        Intent intent = getIntent();
        login = (Cliente) intent.getSerializableExtra("login");

        spinner = (Spinner) findViewById(R.id.spinner_deporte);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.deportes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        fechaButton = findViewById(R.id.fecha);
        disponibilidad = findViewById(R.id.disponibilidad);
        fecha_escogida = findViewById(R.id.fecha_escogida);
        fecha_escogida.setFocusable(false);
        invitados = findViewById(R.id.invitados);
        max = findViewById(R.id.max);
        crearReserva = findViewById(R.id.crear);

        fechaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner.getSelectedItemPosition() != 0) {
                    obtenerFecha();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(NuevoPartido.this);
                    builder.setMessage("Escoja un deporte")
                            .setPositiveButton("Aceptar", dialogClickListener).show();
                }
            }
        });

        disponibilidad.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(spinner.getSelectedItemPosition() != 0)
                    popupDisponibilidad();
            }
        });

        crearReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner.getSelectedItemPosition() != 0 && !fecha_escogida.getText().equals(" ")) {

                    java.sql.Date sqlDate = null;
                    String text = fecha_escogida.getText().toString().trim();

                    try {
                        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        format.setTimeZone(TimeZone.getDefault());

                        java.util.Date date = format.parse(text);

                        sqlDate = new java.sql.Date(date.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    escribir("9," + login.getCorreo() + "," + pistaSeleccionada.getId_pista() + "," +
                            pistaSeleccionada.getId_horario() + "," + sqlDate + "," +
                            max.getText().toString().trim() + "," + invitados.getText().toString().trim());
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });
    }

    public void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                fecha_escogida.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
        }, anio, mes, dia);

        recogerFecha.show();
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

    private List<PistaDisponible> getDisponibilidad(int deporte_id, Date date) {
        List<PistaDisponible> listaPistasOcupadas = new ArrayList<>();
        List<PistaDisponible> lista = new ArrayList<>();
        String[] mensajes = null;
        escribir("8," + deporte_id + "," + date);

        String mensaje = "";
        mensaje = leer();
        mensajes = mensaje.split(";");

        if (!mensaje.equals("No se ha encontrado ninguna reserva ese día")) {

            for (String arg : mensajes) {
                listaPistasOcupadas.add(new PistaDisponible(arg));
            }

            List<Integer> pistasDeportivas = getPistasDeportivas(deporte_id);
            for (int i = 1; i < 7; i++) {
                for (Integer id : pistasDeportivas) {
                    lista.add(new PistaDisponible(id, i));
                }
            }

            List<PistaDisponible> listaRepetidas = new ArrayList<>();

            for (PistaDisponible p : listaPistasOcupadas) {
                for (PistaDisponible p2 : lista) {
                    if (p2.getId_pista() == p.getId_pista() && p2.getId_horario() == p.getId_horario()) {
                        listaRepetidas.add(p2);
                    }
                }
            }

            for (PistaDisponible p : listaRepetidas) {
                lista.remove(p);
            }

        } else {
            for (Integer id : getPistasDeportivas(deporte_id)) {
                for (int i = 1; i < 7; i++) {
                    lista.add(new PistaDisponible(id, i));
                }
            }
        }
        return lista;
    }

    private List<Integer> getPistasDeportivas(int deporte_id) {
        List<Integer> lista = new ArrayList<>();
        switch (deporte_id) {
            case 1:
                lista.add(4);
                lista.add(5);
                return lista;
            case 2:
                lista.add(1);
                lista.add(2);
                lista.add(3);
                return lista;
            case 3:
                lista.add(6);
                return lista;
            case 4:
                lista.add(4);
                lista.add(5);
                return lista;
            default:
                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void popupDisponibilidad() {

        builder = new AlertDialog.Builder(NuevoPartido.this);
        View view = LayoutInflater.from(NuevoPartido.this).inflate(R.layout.popup_disponibilidad, null);
        linear = (LinearLayout) view.findViewById(R.id.sin_disponibilidad_horas);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        recyclerView = view.findViewById(R.id.popup);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        String text = fecha_escogida.getText().toString().trim();

        //System.out.println("LA FECHA: "+text);
        java.sql.Date sqlDate = null;

        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setTimeZone(TimeZone.getDefault());

            java.util.Date date = format.parse(text);

            sqlDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<PistaDisponible> lista = getDisponibilidad(spinner.getSelectedItemPosition(), sqlDate);

        //System.out.println("LA LISTA DISPONIBLE\n"+lista);

        if(lista.size() != 0) {
            linear.setVisibility(View.GONE);
            AdapterPistaDisponible a = new AdapterPistaDisponible(lista
                    , R.layout.fila_disponibilidad, NuevoPartido.this, new AdapterPistaDisponible.OnItemClickListener() {
                @Override
                public void onItemClick(PistaDisponible p, int position) {
                    pistaSeleccionada = p;
                    System.out.println("PISTA SELECCIONADA" + p);
                    dialog.dismiss();
                }
            });
            recyclerView.setAdapter(a);
        } else
            linear.setVisibility(View.VISIBLE);
    }
}