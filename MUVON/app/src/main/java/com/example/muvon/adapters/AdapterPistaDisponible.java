package com.example.muvon.adapters;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muvon.R;
import com.example.muvon.modelo.PistaDisponible;
import com.example.muvon.modelo.Reserva;

import java.util.List;

public class AdapterPistaDisponible extends RecyclerView.Adapter<AdapterPistaDisponible.ViewHolder>{

    private List<PistaDisponible> pistaDisponibles;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    public AdapterPistaDisponible(List<PistaDisponible> p, int layout, Activity activity, OnItemClickListener onItemClickListener) {
        this.pistaDisponibles = p;
        this.layout = layout;
        this.activity = activity;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public AdapterPistaDisponible.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(layout, viewGroup, false);
        AdapterPistaDisponible.ViewHolder viewHolder = new AdapterPistaDisponible.ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AdapterPistaDisponible.ViewHolder viewHolder, final int i) {

        final PistaDisponible p1 = pistaDisponibles.get(i);

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                listener.onItemClick(p1,i);
            }
        });

        viewHolder.pista.setText(""+p1.getId_pista());
        viewHolder.horario.setText(p1.getFranja());
    }

    @Override
    public int getItemCount() {
        if(pistaDisponibles != null)
            return pistaDisponibles.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView pista;
        TextView horario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.fila_disponibilidad);
            pista = itemView.findViewById(R.id.pista);
            horario = itemView.findViewById(R.id.franja);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PistaDisponible p, int position);
    }
}
