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
import com.example.muvon.modelo.Cliente;

import java.util.List;

public class AdapterParticipantesPartido extends RecyclerView.Adapter<AdapterParticipantesPartido.ViewHolder>{

    private List<Cliente> clientes;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    public AdapterParticipantesPartido(List<Cliente> c, int layout, Activity activity, OnItemClickListener onItemClickListener) {
        this.clientes = c;
        this.layout = layout;
        this.activity = activity;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public AdapterParticipantesPartido.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(layout, viewGroup, false);
        AdapterParticipantesPartido.ViewHolder viewHolder = new AdapterParticipantesPartido.ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AdapterParticipantesPartido.ViewHolder viewHolder, final int i) {

        final Cliente cliente = clientes.get(i);

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                listener.onItemClick(cliente,i);
            }
        });
        viewHolder.participante_partido.setText(cliente.getNombre()+ " " + cliente.getApellidos());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView participante_partido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.fila_participante_partido);
            participante_partido = itemView.findViewById(R.id.participante_partido);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Cliente cliente, int position);
    }
}