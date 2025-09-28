package Clases;

import java.util.List;

public class Algoritmos {

    // Estrategia 1: Búsqueda Exhaustiva
    public static ParDePuntos busquedaExhaustiva(List<Punto> puntos) {
        if (puntos.size() < 2) {
            return null;
        }

        ParDePuntos mejorPar = new ParDePuntos(puntos.get(0), puntos.get(1));

        for (int i = 0; i < puntos.size(); i++) {
            for (int j = i + 1; j < puntos.size(); j++) {
                ParDePuntos actual = new ParDePuntos(puntos.get(i), puntos.get(j));
                if (actual.getDistancia() < mejorPar.getDistancia()) {
                    mejorPar = actual;
                }
            }
        }

        return mejorPar;
    }
}

