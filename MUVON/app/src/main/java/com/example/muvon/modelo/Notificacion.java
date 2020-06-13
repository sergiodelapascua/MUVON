package com.example.muvon.modelo;

import java.io.Serializable;

public class Notificacion implements Serializable {

    private String pista;
    private String horario;

    public Notificacion() {
    }

    public Notificacion(String arg) {
        String[] argumentos = arg.split(",");
        this.pista = argumentos[0];
        this.horario = argumentos[1];
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

    @Override
    public String toString() {
        return "Notificacion{" +
                "pista='" + pista + '\'' +
                ", horario='" + horario + '\'' +
                '}';
    }
}
