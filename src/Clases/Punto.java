package Clases;

public class Punto {
    private int id;
    private double x;
    private double y;

    public Punto(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distancia(Punto otro) {
        double dx = this.x - otro.x;
        double dy = this.y - otro.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return id + " (" + String.format("%.3f", x) + ", " + String.format("%.3f", y) + ")";
    }
}
