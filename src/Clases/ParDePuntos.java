package Clases;

public class ParDePuntos {
    private Punto p1;
    private Punto p2;
    private double distancia;
    private int Num_distancias_calculadas ;
    private double Tiempo_de_ejecucion ;

    public ParDePuntos(Punto p1, Punto p2, double distancia , int num) {
        this.p1 = p1;
        this.p2 = p2;
        //this.distancia = p1.distancia(p2);
        this.distancia = distancia ;
        this.Num_distancias_calculadas = num ;
    }

    public Punto getP1() {
        return p1;
    }

    public Punto getP2() {
        return p2;
    }

    public double getDistancia() {
        return distancia;
    }

    /*@Override
    public String toString() {
        return p1 + " <-> " + p2 + " Distancia: " + String.format("%.8f", distancia);
    }
     */

    @Override
    public String toString() {
        return "PAR_DE_PUNTOS{ " +
                "punto1=" + p1.toString() +
                ", punto2=" + p2.toString() + '}';
    }

    public void setPunto1(Punto punto1) {
        this.p1 = punto1;
    }

    public void setPunto2(Punto punto2) {
        this.p2 = punto2;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getNum_distancias_calculadas() {
        return Num_distancias_calculadas;
    }

    public void setNum_distancias_calculadas(int num_distancias_calculadas) {
        Num_distancias_calculadas = num_distancias_calculadas;
    }

    public double getTiempo_de_ejecucion() {
        return Tiempo_de_ejecucion;
    }

    public void setTiempo_de_ejecucion(double tiempo_de_ejecucion) {
        Tiempo_de_ejecucion = tiempo_de_ejecucion;
    }
}
