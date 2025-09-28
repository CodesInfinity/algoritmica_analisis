package Clases;

public class ParDePuntos {
    private Punto p1;
    private Punto p2;
    private double distancia;

    public ParDePuntos(Punto p1, Punto p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.distancia = p1.distancia(p2);
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

    @Override
    public String toString() {
        return p1 + " <-> " + p2 + " Distancia: " + String.format("%.8f", distancia);
    }
}
