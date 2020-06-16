package com.example.muvon.modelo;

import java.io.Serializable;

public class Cliente implements Serializable {

    private String nombre;
    private String apellidos;
    private String correo;

    public Cliente() {
    }

    public Cliente(String nombre, String apellidos, String correo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public Cliente(String arg) {
        String[] argumentos = arg.split(",");
        this.nombre = argumentos[0];
        this.apellidos = argumentos[1];
        this.correo = argumentos[2];
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                '}';
    }
}