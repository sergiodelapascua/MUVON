/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author sergio
 */
public class Reserva {

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

    public String getFecha() {
        LocalDate date = LocalDate.parse(fecha);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = date.format(formatter);

        return formattedString;
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

}
