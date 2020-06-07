/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author sergio
 */
public class PistaDisponible {
    
    private int id_pista;
    private int id_horario;
    private String franja;

    public PistaDisponible() {
    }

    public PistaDisponible(int id_pista, int id_horario) {
        this.id_pista = id_pista;
        this.id_horario = id_horario;
        this.franja = buscarFranjaHoraria();
    }

    public PistaDisponible(String argumentos) {
        String[] arg = argumentos.split(",");
        this.id_pista = Integer.parseInt(arg[0]);
        this.id_horario = Integer.parseInt(arg[1]);
        this.franja = buscarFranjaHoraria();
    }

    public int getId_pista() {
        return id_pista;
    }

    public void setId_pista(int id_pista) {
        this.id_pista = id_pista;
    }

    public int getId_horario() {
        return id_horario;
    }

    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }

    public String getFranja() {
        return franja;
    }

    public void setFranja(String franja) {
        this.franja = franja;
    }

    private String buscarFranjaHoraria() {
        switch (id_horario) {
            case 1:
                return "16:00/17:00";
            case 2:
                return "17:00/18:00";
            case 3:
                return "18:00/19:00";
            case 4:
                return "19:00/20:00";
            case 5:
                return "20:00/21:00";
            case 6:
                return "21:00/22:00";
            default:
                throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "PistaDisponible{" + "id_pista=" + id_pista + ", id_horario=" + id_horario + ", franja=" + franja + '}';
    }
    
    
}
