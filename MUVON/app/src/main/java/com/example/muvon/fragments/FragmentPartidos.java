package com.example.muvon.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvon.R;
import com.example.muvon.activities.DetallesPartido;
import com.example.muvon.activities.MainActivity;
import com.example.muvon.activities.Principal;
import com.example.muvon.adapters.AdapterPartido;
import com.example.muvon.modelo.Reserva;

import java.util.ArrayList;
import java.util.List;

public class FragmentPartidos extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private LinearLayout ly;
    private Principal principal;

    public FragmentPartidos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partidos, container, false);

        principal = ((Principal)getActivity());

        //ly = view.findViewById(R.id.no_connection);

        context = this.getContext();

        recyclerView = view.findViewById(R.id.recyclerViewPartidos);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.deportes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        AdapterPartido a = new AdapterPartido(pedirLista(), R.layout.fila_partidos, principal, new AdapterPartido.OnItemClickListener() {
            @Override
            public void onItemClick(Reserva reserva, int position) {
                Intent intent = new Intent(principal, DetallesPartido.class);
                intent.putExtra("reserva", reserva);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(a);

        return view;
    }

    private List<Reserva> pedirLista(){
        List<Reserva> lista = new ArrayList<>();
        String[] mensajes = null;
        principal.escribir("6,");

        String mensaje = "";
        mensaje = principal.leer();
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
}
