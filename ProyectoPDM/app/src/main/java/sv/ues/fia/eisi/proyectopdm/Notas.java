package sv.ues.fia.eisi.proyectopdm;

import java.util.Comparator;
import java.lang.Float;

public class Notas  {
    private float nota;
    private int cantidad;
    public Notas() {
    }

    public Notas(float nota, int cantidad) {
        this.nota = nota;
        this.cantidad = cantidad;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    //Comparador por notas
    public static Comparator<Notas> ComparadorNotas = new Comparator<Notas>() {

        public int compare(Notas nota1, Notas nota2) {
            float Nota1 = nota1.getNota();
            float Nota2 = nota2.getNota();

            //ascending order
            return Float.compare(Nota1,Nota2);

        }};

    //Comparador por cantidad
    public static Comparator<Notas> ComparadorCantidadNotas = new Comparator<Notas>() {

        public int compare(Notas nota1, Notas nota2) {
            float Cantidad1 = nota1.getCantidad();
            float Cantidad2 = nota2.getCantidad();

            return Float.compare(Cantidad1,Cantidad2);

        }};
}
