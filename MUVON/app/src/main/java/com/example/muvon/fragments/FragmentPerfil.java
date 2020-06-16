package com.example.muvon.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.muvon.R;
import com.example.muvon.activities.DetallesPartido;
import com.example.muvon.activities.DetallesPartido2;
import com.example.muvon.activities.PerfilUsuario;
import com.example.muvon.activities.Principal;
import com.example.muvon.adapters.AdapterPartido;
import com.example.muvon.modelo.Reserva;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentPerfil extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private LinearLayout ly;
    private Principal principal;

    TextView nombre;
    ImageView perfil;
    ImageView futbol;
    ImageView padel;
    ImageView basket;
    ImageView balonmano;

    public FragmentPerfil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        principal = ((Principal) getActivity());

        //ly = view.findViewById(R.id.no_connection);

        perfil = view.findViewById(R.id.imagen_perfil);
        nombre = view.findViewById(R.id.nombre_perfil);
        nombre.setText(principal.cliente.getNombre() + " " + principal.cliente.getApellidos());
        futbol = view.findViewById(R.id.futbol_estrellas);
        padel = view.findViewById(R.id.padel_estrellas);
        basket = view.findViewById(R.id.basket_estrellas);
        balonmano = view.findViewById(R.id.balonmano_estrellas);

        principal.conectar();
        principal.escribir("12," + principal.cliente.getCorreo());
        String mensaje = principal.leer();
        System.out.println("LO QUE HA LEIDOOOOOOO\n" + mensaje);
        String[] arg = mensaje.split(";");
        interpretarArgumentos(arg);
        String foto = principal.leer();
        System.out.println("LO QUE HA LEIDOOOOOOO22222\n" + foto);
        cargarFoto(foto);

        recyclerView = view.findViewById(R.id.recyclerViewPartidosPerfil);
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        List<Reserva> lista = pedirLista();
        LinearLayout linear = view.findViewById(R.id.sin_partido_creado_fragment);

        if (lista == null) {
            linear.setVisibility(View.VISIBLE);
        } else {
            linear.setVisibility(View.GONE);
            AdapterPartido a = new AdapterPartido(lista, R.layout.fila_partidos, principal, new AdapterPartido.OnItemClickListener() {
                @Override
                public void onItemClick(Reserva reserva, int position) {
                    Intent intent = new Intent(principal, DetallesPartido2.class);
                    intent.putExtra("reserva", reserva);
                    intent.putExtra("login", principal.cliente);
                    try {
                        principal.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(a);
        }
        return view;

    }

    private List<Reserva> pedirLista() {
        List<Reserva> lista = new ArrayList<>();
        String[] mensajes = null;
        principal.escribir("14," + principal.cliente.getCorreo());

        String mensaje = "";
        mensaje = principal.leer();
        if (mensaje.equals("No se han encontrado reservas almacenadas")) {
            System.err.println("ERROR al buscar partidos del ususario " + principal.cliente.toString());
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

    private void cargarFoto(String mensaje) {
        if(mensaje.equals("1"))
            perfil.setBackgroundResource(R.drawable.female_ic);
        else
            perfil.setBackgroundResource(R.drawable.male_ic);
    }

    private void interpretarArgumentos(String[] arg) {
        for (int i = 0; i < arg.length; i++) {
            String[] info = arg[i].split(",");
            if (info[0].equals("1")) //futbol
                futbol.setBackgroundResource(imagenNivel(info[1]));
            else if (info[0].equals("2")) //padel
                padel.setBackgroundResource(imagenNivel(info[1]));
            else if (info[0].equals("3")) //basket
                basket.setBackgroundResource(imagenNivel(info[1]));
            else if (info[0].equals("4")) //balonmano
                balonmano.setBackgroundResource(imagenNivel(info[1]));

        }
    }

    private int imagenNivel(String lvl) {
        switch (lvl) {
            case "1":
                return R.drawable.uno_stars_ic;
            case "2":
                return R.drawable.dos_stars_ic;
            case "3":
                return R.drawable.tres_stars_ic;
            case "4":
                return R.drawable.cuatro_stars_ic;
            case "5":
                return R.drawable.cinco_stars_ic;
        }
        return -1;
    }

    @Override
    public void onResume() {
        super.onResume();

        AdapterPartido a = new AdapterPartido(pedirLista(), R.layout.fila_partidos, principal, new AdapterPartido.OnItemClickListener() {
            @Override
            public void onItemClick(Reserva reserva, int position) {
                Intent intent = new Intent(principal, DetallesPartido2.class);
                intent.putExtra("reserva", reserva);
                intent.putExtra("login", principal.cliente);
                try {
                    principal.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(a);
    }
}