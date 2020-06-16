package com.example.muvon.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvon.R;
import com.example.muvon.modelo.Reserva;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdapterPartido extends RecyclerView.Adapter<AdapterPartido.ViewHolder>{

    private List<Reserva> partidos;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    public AdapterPartido(List<Reserva> p, int layout, Activity activity, OnItemClickListener onItemClickListener) {
        this.partidos = p;
        this.layout = layout;
        this.activity = activity;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public AdapterPartido.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(layout, viewGroup, false);
        AdapterPartido.ViewHolder viewHolder = new AdapterPartido.ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AdapterPartido.ViewHolder viewHolder, final int i) {

        final Reserva partido = partidos.get(i);

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    listener.onItemClick(partido,i);
                }
        });
        viewHolder.icono.setBackgroundResource(elegirImagen(partido.getPista()));
        viewHolder.titulo_partido.setText(ellipsize(partido.getNombre()));
        viewHolder.horario_partido.setText(partido.getHorario());
        viewHolder.asistentes_partido.setText(""+partido.getJugadores());
        viewHolder.fecha_partido.setText(partido.getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
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

    @Override
    public int getItemCount() {
        if(partidos != null)
            return partidos.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView icono;
        TextView titulo_partido;
        TextView fecha_partido;
        TextView horario_partido;
        TextView asistentes_partido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.fila_partido);
            icono = itemView.findViewById(R.id.ic_deporte);
            titulo_partido = itemView.findViewById(R.id.titulo_partido);
            fecha_partido = itemView.findViewById(R.id.fecha_partido);
            horario_partido = itemView.findViewById(R.id.horario_partido);
            asistentes_partido = itemView.findViewById(R.id.asistentes_partido);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Reserva reserva, int position);
    }

    public static String ellipsize(String input) {
        int maxCharacters = 19;
        if (input == null || input.length() < maxCharacters) {
            return input;
        }
        return input.substring(0, maxCharacters) + "...";
    }
}
