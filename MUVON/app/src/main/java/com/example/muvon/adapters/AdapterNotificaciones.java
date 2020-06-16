package com.example.muvon.adapters;

import android.app.Activity;
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
import com.example.muvon.modelo.Notificacion;

import java.util.List;

public class AdapterNotificaciones extends RecyclerView.Adapter<AdapterNotificaciones.ViewHolder>{

    private List<Notificacion> notificaciones;
    private int layout;
    private Activity activity;

    public AdapterNotificaciones(List<Notificacion> n, int layout, Activity activity) {
        this.notificaciones = n;
        this.layout = layout;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterNotificaciones.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(layout, viewGroup, false);
        AdapterNotificaciones.ViewHolder viewHolder = new AdapterNotificaciones.ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AdapterNotificaciones.ViewHolder viewHolder, final int i) {

        final Notificacion notificacion = notificaciones.get(i);

        viewHolder.icono.setBackgroundResource(elegirImagen(notificacion.getPista()));
        viewHolder.pista_notificacion.setText(ellipsize(notificacion.getPista()));
        viewHolder.horario_notificacion.setText(notificacion.getHorario());
    }

    @Override
    public int getItemCount() {
        if(notificaciones != null)
            return notificaciones.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView icono;
        TextView pista_notificacion;
        TextView horario_notificacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.fila_notificacion);
            icono = itemView.findViewById(R.id.icono_noti);
            pista_notificacion = itemView.findViewById(R.id.pista_notificacion);
            horario_notificacion = itemView.findViewById(R.id.horario_notificacion);
        }
    }

    public static String ellipsize(String input) {
        int maxCharacters = 25;
        if (input == null || input.length() < maxCharacters) {
            return input;
        }
        return input.substring(0, maxCharacters) + "...";
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
}