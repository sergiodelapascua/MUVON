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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.muvon.R;
import com.example.muvon.activities.DetallesPartido;
import com.example.muvon.activities.Principal;
import com.example.muvon.adapters.AdapterNotificaciones;
import com.example.muvon.adapters.AdapterPartido;
import com.example.muvon.modelo.Notificacion;
import com.example.muvon.modelo.Reserva;

import java.util.ArrayList;
import java.util.List;

public class FragmentNotificaciones extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private LinearLayout ly;
    private Principal principal;

    public FragmentNotificaciones() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        principal = ((Principal) getActivity());

        //ly = view.findViewById(R.id.no_connection);

        context = this.getContext();

        recyclerView = view.findViewById(R.id.recyclerView_notificaciones);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        List<Notificacion> lista = buscarNotificaciones();
        LinearLayout linear = view.findViewById(R.id.sin_notificaciones);

        if (lista == null) {
            linear.setVisibility(View.VISIBLE);
        } else {
            linear.setVisibility(View.GONE);
            AdapterNotificaciones a = new AdapterNotificaciones(buscarNotificaciones(), R.layout.fila_notificacion, principal);
            recyclerView.setAdapter(a);
        }

        return view;
    }

    private List<Notificacion> buscarNotificaciones() {
        List<Notificacion> lista = new ArrayList<>();
        String[] mensajes = null;
        principal.escribir("15," + principal.cliente.getCorreo());

        String mensaje = "";
        mensaje = principal.leer();
        if (mensaje.equals("No se han encontrado reservas almacenadas")) {
            //ignorar
        } else {
            if (!mensaje.equals("")) {
                if (!mensaje.equals("No hay notificaciones")) {
                    mensajes = mensaje.split(";");
                    for (String arg : mensajes) {
                        lista.add(new Notificacion(arg));
                    }
                    System.out.println(lista);
                    return lista;
                }
            }
        }
        return null;
    }
}