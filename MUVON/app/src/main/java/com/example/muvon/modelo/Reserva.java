package com.example.muvon.modelo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;

public class Reserva implements Serializable {

    private int id;
    private String nombre;
    private String pista;
    private String horario;
    private String fecha;
    private int jugadores;

    public Reserva() {
    }

    public Reserva(String argumentos) {
        String[] arg = argumentos.split(",");
        this.id = Integer.parseInt(arg[0]);
        this.nombre = arg[1];
        this.pista = arg[2];
        this.horario = arg[3];
        this.fecha = arg[4];
        this.jugadores = Integer.parseInt(arg[5]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getFecha() {
        LocalDate ld = LocalDate.parse(fecha);
        return ld;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getJugadores() {
        return jugadores;
    }

    public void setJugadores(int jugadores) {
        this.jugadores = jugadores;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pista='" + pista + '\'' +
                ", horario='" + horario + '\'' +
                ", fecha='" + fecha + '\'' +
                ", jugadores=" + jugadores +
                '}';
    }
}