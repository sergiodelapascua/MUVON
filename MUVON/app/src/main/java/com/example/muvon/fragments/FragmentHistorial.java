package com.example.muvon.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvon.R;
import com.example.muvon.activities.DetallesPartido;
import com.example.muvon.activities.MainActivity;
import com.example.muvon.activities.NuevoPartido;
import com.example.muvon.activities.Principal;
import com.example.muvon.adapters.AdapterPartido;
import com.example.muvon.modelo.Reserva;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentHistorial extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private LinearLayout ly;
    private Principal principal;
    private static ArrayAdapter<CharSequence> adapterSpinner;

    public FragmentHistorial() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        principal = ((Principal) getActivity());

        ly = view.findViewById(R.id.sin_historial);

        context = this.getContext();

        recyclerView = view.findViewById(R.id.recyclerViewPartidos_historial);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner_historial);
        adapterSpinner = ArrayAdapter.createFromResource(context,
                R.array.deportes_array, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedItem = parent.getSelectedItemPosition();
                //System.out.println("LO SELECCIONADOOOO "+selectedItem);
                if (selectedItem != 0) {
                    principal.escribir("21," + selectedItem + "," + principal.cliente.getCorreo());
                    String mensaje = principal.leer();
                    System.out.println("=======================================================0\nEL MENSAJEEEEEEEEEEEEEEEEEE \n"+mensaje);
                    if (!mensaje.equals("No se han encontrado reservas almacenadas")) {

                        if (mensaje == null || mensaje.equals("")) {
                            ly.setVisibility(View.VISIBLE);
                        } else {
                            ly.setVisibility(View.GONE);
                            AdapterPartido a2 = new AdapterPartido(cargarLista(mensaje), R.layout.fila_partidos, principal, new AdapterPartido.OnItemClickListener() {
                                @Override
                                public void onItemClick(Reserva reserva, int position) {
                                    Intent intent = new Intent(principal, DetallesPartido.class);
                                    intent.putExtra("reserva", reserva);
                                    intent.putExtra("cliente", principal.cliente);
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(a2);
                        }
                    } else {
                        ly.setVisibility(View.VISIBLE);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("No te has unido a ninún partido de ese deporte aún.\n" +
                                "¡Aprovecha y crea tú uno!")
                                .setPositiveButton("Aceptar", dialogClickListener).show();
                        adapterSpinner = ArrayAdapter.createFromResource(context,
                                R.array.deportes_array, android.R.layout.simple_spinner_item);
                        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapterSpinner);
                    }
                } else {
                    List<Reserva> lista = pedirLista();

                    if (lista != null) {
                        ly.setVisibility(View.GONE);
                        AdapterPartido a = new AdapterPartido(lista, R.layout.fila_partidos, principal, new AdapterPartido.OnItemClickListener() {
                            @Override
                            public void onItemClick(Reserva reserva, int position) {
                                Intent intent = new Intent(principal, DetallesPartido.class);
                                intent.putExtra("reserva", reserva);
                                intent.putExtra("cliente", principal.cliente);
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(a);
                    } else
                        ly.setVisibility(View.VISIBLE);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
                List<Reserva> lista = pedirLista();

                if (lista != null) {
                    ly.setVisibility(View.GONE);
                    AdapterPartido a = new AdapterPartido(lista, R.layout.fila_partidos, principal, new AdapterPartido.OnItemClickListener() {
                        @Override
                        public void onItemClick(Reserva reserva, int position) {
                            Intent intent = new Intent(principal, DetallesPartido.class);
                            intent.putExtra("reserva", reserva);
                            intent.putExtra("cliente", principal.cliente);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(a);
                } else
                    ly.setVisibility(View.VISIBLE);
            }
        });

        List<Reserva> lista = pedirLista();
        System.out.println("=======================================================0LA LISTAAAA\n"+lista);
        if (lista != null) {
            ly.setVisibility(View.GONE);
            AdapterPartido a = new AdapterPartido(lista, R.layout.fila_partidos, principal, new AdapterPartido.OnItemClickListener() {
                @Override
                public void onItemClick(Reserva reserva, int position) {
                    Intent intent = new Intent(principal, DetallesPartido.class);
                    intent.putExtra("reserva", reserva);
                    intent.putExtra("cliente", principal.cliente);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(a);
        } else
            ly.setVisibility(View.VISIBLE);

        return view;
    }

    private List<Reserva> cargarLista(String mensaje) {
        List<Reserva> lista = new ArrayList<>();
        String[] mensajes = null;
        if (mensaje.equals("No se han encontrado reservas almacenadas")) {
            //ignorar
        } else {
            mensajes = mensaje.split(";");
            for (String arg : mensajes) {
                lista.add(new Reserva(arg));
            }
            return lista;
        }
        return null;
    }

    private List<Reserva> pedirLista() {
        List<Reserva> lista = new ArrayList<>();
        String[] mensajes = null;
        principal.escribir("20," + principal.cliente.getCorreo());

        String mensaje = "";
        mensaje = principal.leer();
        //System.out.println("LO QUE RECIBE AL CARGAR LOS PARTIDOS\n" + mensaje);
        if (mensaje.equals("No se han encontrado reservas almacenadas")) {
            //ignorar
        } else {
            if (!mensaje.equals("")) {
                mensajes = mensaje.split(";");
                for (String arg : mensajes) {
                    lista.add(new Reserva(arg));
                }
                System.out.println("RESERVAS " + lista);
                return lista;
            }
        }
        return null;
    }
}
