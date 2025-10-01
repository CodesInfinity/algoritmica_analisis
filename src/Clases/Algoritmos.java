package Clases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
 * Clase que implementa diferentes estrategias algorítmicas para resolver
 * el problema del par de puntos más cercanos en un plano 2D.
 * 
 * Incluye las siguientes estrategias:
 * - Búsqueda exhaustiva: O(n²)
 * - Búsqueda exhaustiva con poda: O(n²) en peor caso, mejor en caso medio
 * - Divide y Vencerás: O(n log n)
 * - Divide y Vencerás mejorado: O(n log n) con optimización en franja
 */
public class Algoritmos {

    /*
     * Calcula la distancia euclidiana entre dos puntos en el plano 2D.
     * La fórmula utilizada es: √[(x₁-x₂)² + (y₁-y₂)²]
     */
    public static double distancia_euclidea(Punto p1, Punto p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + 
                        Math.pow(p1.getY() - p2.getY(), 2));
    }

    /*
     * Implementa la estrategia de búsqueda exhaustiva para encontrar
     * el par de puntos más cercanos.
     * 
     * Complejidad temporal: O(n²)
     * Compara todos los pares de puntos posibles (n·(n-1)/2 comparaciones)
     */
    public static ParDePuntos algoritmo_exhaustivo(List<Punto> puntos) {
        if (puntos == null || puntos.size() < 2) {
            throw new IllegalArgumentException("NO HAY PUNTOS SUFICIENTES\n");
        }

        long START = System.nanoTime();

        double distanciaMinima = Double.MAX_VALUE;
        Punto punto1 = null;
        Punto punto2 = null;
        int distanciasCalculadas = 0;

        // Comparar todos los pares de puntos posibles
        for (int i = 0; i < puntos.size(); i++) {
            for (int j = i + 1; j < puntos.size(); j++) {
                double distancia = distancia_euclidea(puntos.get(i), puntos.get(j));
                distanciasCalculadas++;

                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    punto1 = puntos.get(i);
                    punto2 = puntos.get(j);
                }
            }
        }

        long END = System.nanoTime();
        double tiempoEjecucion = (double) (END - START) / 1000000; // Convertir a milisegundos

        ParDePuntos resultadoFinal = new ParDePuntos(punto1, punto2, distanciaMinima, distanciasCalculadas);
        resultadoFinal.setTiempo_de_ejecucion(tiempoEjecucion);

        return resultadoFinal;
    }

    /*
     * Implementa la estrategia de búsqueda exhaustiva con poda para encontrar
     * el par de puntos más cercanos.
     * 
     * Complejidad temporal: O(n log n) para ordenar + O(n²) en peor caso,
     * pero mucho mejor en caso medio debido a la poda
     * 
     * Estrategia: Ordena los puntos por coordenada X y descarta comparaciones
     * cuando la diferencia en X ya supera la distancia mínima encontrada
     */
    public static ParDePuntos algoritmo_busqueda_poda(List<Punto> puntos) {
        if (puntos == null || puntos.size() < 2) {
            throw new IllegalArgumentException("NO HAY PUNTOS SUFICIENTES\n");
        }

        // Ordenar los puntos por coordenada X usando QuickSort (Collections.sort usa Timsort)
        puntos.sort(Comparator.comparingDouble(Punto::getX));

        long START = System.nanoTime();

        double distanciaMinima = Double.MAX_VALUE;
        Punto punto1 = null;
        Punto punto2 = null;
        int distanciasCalculadas = 0;

        for (int i = 0; i < puntos.size(); i++) {
            // Iterar mientras j sea válido Y la diferencia en X sea menor que distanciaMinima
            int j = i + 1;
            while (j < puntos.size() && Math.abs(puntos.get(j).getX() - puntos.get(i).getX()) < distanciaMinima) {
                double distancia = distancia_euclidea(puntos.get(i), puntos.get(j));
                distanciasCalculadas++;

                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    punto1 = puntos.get(i);
                    punto2 = puntos.get(j);
                }
                j++;
            }
        }

        long END = System.nanoTime();
        double tiempoEjecucion = (double) (END - START) / 1000000;

        ParDePuntos resultadoFinal = new ParDePuntos(punto1, punto2, distanciaMinima, distanciasCalculadas);
        resultadoFinal.setTiempo_de_ejecucion(tiempoEjecucion);

        return resultadoFinal;
    }

}
