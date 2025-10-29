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
     * Calcula la distancia euclidiana entre dos puntos en el plano 2D. La fórmula
     * utilizada es: √[(x₁-x₂)² + (y₁-y₂)²]
     * 
     * @param p1 Primer punto
     * @param p2 Segundo punto
     * @return Distancia euclidiana real entre los puntos
     */
    public static double distancia_euclidea(Punto p1, Punto p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /*
     * Calcula la distancia euclidiana al cuadrado entre dos puntos.
     * Más eficiente que calcular la distancia real ya que evita la operación sqrt.
     * Útil para comparaciones donde solo interesa el orden relativo de distancias.
     * 
     * @param p1 Primer punto
     * @param p2 Segundo punto
     * @return Distancia euclidiana al cuadrado entre los puntos
     */
    public static double distancia_euclidea_cuadrada(Punto p1, Punto p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return dx * dx + dy * dy; // Sin sqrt para mejor rendimiento
    }

    /*
     * Implementa la estrategia de búsqueda exhaustiva para encontrar el par de
     * puntos más cercanos.
     * 
     * Complejidad temporal: O(n²) - Compara todos los pares de puntos posibles
     * (n·(n-1)/2 comparaciones)
     * 
     * @param puntos Lista de puntos a analizar
     * @return ParDePuntos con los dos puntos más cercanos y su distancia
     * @throws IllegalArgumentException si hay menos de 2 puntos
     */
    public static ParDePuntos algoritmo_exhaustivo(List<Punto> puntos) {
        // PASO 1: Validación de entrada
        if (puntos == null || puntos.size() < 2) {
            throw new IllegalArgumentException("NO HAY PUNTOS SUFICIENTES\n");
        }

        // PASO 2: Inicio de medición de tiempo
        long start = System.nanoTime();

        // PASO 3: Inicialización de variables para tracking
        double distanciaMinimaCuadrada = Double.MAX_VALUE;
        Punto punto1 = null;
        Punto punto2 = null;
        int distanciasCalculadas = 0;

        // PASO 4: Doble bucle para comparar todos los pares posibles
        // Bucle externo: recorre cada punto i
        for (int i = 0; i < puntos.size(); i++) {
            Punto a = puntos.get(i);
            // Bucle interno: recorre cada punto j > i (evita comparaciones duplicadas)
            for (int j = i + 1; j < puntos.size(); j++) {
                Punto b = puntos.get(j);

                // PASO 5: Calcular distancia al cuadrado (más eficiente)
                double distanciaCuadrada = distancia_euclidea_cuadrada(a, b);
                distanciasCalculadas++;

                // PASO 6: Actualizar mínimo si encontramos una distancia menor
                if (distanciaCuadrada < distanciaMinimaCuadrada) {
                    distanciaMinimaCuadrada = distanciaCuadrada;
                    punto1 = a;
                    punto2 = b;
                }
            }
        }

        // PASO 7: Calcular distancia real solo una vez al final
        double distanciaMinima = Math.sqrt(distanciaMinimaCuadrada);

        // PASO 8: Finalizar medición de tiempo y calcular duración
        long end = System.nanoTime();
        double tiempoEjecucion = (double) (end - start) / 1_000_000;

        // PASO 9: Crear y retornar resultado
        ParDePuntos resultadoFinal = new ParDePuntos(punto1, punto2, distanciaMinima, distanciasCalculadas);
        resultadoFinal.setTiempo_de_ejecucion(tiempoEjecucion);

        return resultadoFinal;
    }

    /*
     * Implementa la estrategia de búsqueda exhaustiva con poda para encontrar el
     * par de puntos más cercanos.
     * 
     * Complejidad temporal: O(n log n) para ordenar + O(n²) en peor caso, pero
     * mucho mejor en caso medio debido a la poda por coordenada X.
     * 
     * Estrategia: Ordena los puntos por coordenada X y descarta comparaciones
     * cuando la diferencia en X ya supera la distancia mínima encontrada.
     * 
     * @param puntos Lista de puntos a analizar
     * @return ParDePuntos con los dos puntos más cercanos y su distancia
     * @throws IllegalArgumentException si hay menos de 2 puntos
     */
    public static ParDePuntos algoritmo_busqueda_poda(List<Punto> puntos) {
        // PASO 1: Validación de entrada
        if (puntos == null || puntos.size() < 2) {
            throw new IllegalArgumentException("NO HAY PUNTOS SUFICIENTES\n");
        }

        // PASO 2: Ordenar puntos por coordenada X (prerrequisito para la poda)
        puntos.sort(Comparator.comparingDouble(Punto::getX));

        // PASO 3: Inicio de medición de tiempo
        long START = System.nanoTime();

        // PASO 4: Inicialización de variables
        double distanciaMinima = Double.MAX_VALUE;
        Punto punto1 = null;
        Punto punto2 = null;
        int distanciasCalculadas = 0;

        // PASO 5: Bucle principal con poda
        for (int i = 0; i < puntos.size(); i++) {
            // Iterar mientras j sea válido Y la diferencia en X sea menor que distanciaMinima
            int j = i + 1;
            while (j < puntos.size() && Math.abs(puntos.get(j).getX() - puntos.get(i).getX()) < distanciaMinima) {
                // PASO 6: Calcular distancia real entre puntos candidatos
                double distancia = distancia_euclidea(puntos.get(i), puntos.get(j));
                distanciasCalculadas++;

                // PASO 7: Actualizar mínimo si encontramos una distancia menor
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    punto1 = puntos.get(i);
                    punto2 = puntos.get(j);
                }
                j++;
            }
        }

        // PASO 8: Finalizar medición de tiempo
        long END = System.nanoTime();
        double tiempoEjecucion = (double) (END - START) / 1000000;

        // PASO 9: Crear y retornar resultado
        ParDePuntos resultadoFinal = new ParDePuntos(punto1, punto2, distanciaMinima, distanciasCalculadas);
        resultadoFinal.setTiempo_de_ejecucion(tiempoEjecucion);

        return resultadoFinal;
    }

    /*
     * Implementa la estrategia Divide y Vencerás para encontrar el par de puntos
     * más cercanos.
     * 
     * Complejidad temporal: O(n log n) en caso promedio
     * 
     * Estrategia:
     * 1. Dividir: Separar los puntos en dos mitades por la coordenada X media
     * 2. Vencer: Resolver recursivamente cada mitad
     * 3. Combinar: Considerar pares que cruzan la división (puntos en la franja central)
     * 
     * @param ordenadosX Lista de puntos (será ordenada por X si no lo está)
     * @return ParDePuntos con los dos puntos más cercanos y su distancia
     */
    public static ParDePuntos dyvNormal(List<Punto> ordenadosX) {
        int n = ordenadosX.size();
        
        // PASO 1: Asegurar que los puntos están ordenados por X
        ordenadosX.sort(Comparator.comparingDouble(Punto::getX));
        
        // PASO 2: Caso base - usar algoritmo exhaustivo para conjuntos pequeños
        if (n <= 3) {
            return algoritmo_exhaustivo(new ArrayList<>(ordenadosX));
        }
        
        // PASO 3: Dividir - encontrar punto medio y crear subconjuntos
        int mid = n / 2;
        List<Punto> izquierda = new ArrayList<>(ordenadosX.subList(0, mid));
        List<Punto> derecha = new ArrayList<>(ordenadosX.subList(mid, n));
        double xm = derecha.get(0).getX(); // Coordenada X del punto de división
        
        // PASO 4: Resolver recursivamente cada mitad (Vencer)
        ParDePuntos solIzq = dyvNormal(izquierda);
        ParDePuntos solDer = dyvNormal(derecha);
        
        // PASO 5: Combinar contadores de distancias calculadas
        int distanciasCalculadas = solIzq.getNum_distancias_calculadas() + solDer.getNum_distancias_calculadas();
        
        // PASO 6: Encontrar la distancia mínima entre las dos soluciones
        double dIzq = solIzq.getDistancia();
        double dDer = solDer.getDistancia();
        double d = Math.min(dIzq, dDer);
        ParDePuntos mejor = (dIzq <= dDer) ? solIzq : solDer;
        
        // PASO 7: Identificar puntos en la franja central (cerca del eje de división)
        ArrayList<Punto> bandaIzq = new ArrayList<>();
        ArrayList<Punto> bandaDer = new ArrayList<>();
        
        // Puntos de la izquierda que están a menos de d del eje
        for (Punto p : izquierda)
            if (Math.abs(p.getX() - xm) < d)
                bandaIzq.add(p);
        
        // Puntos de la derecha que están a menos de d del eje
        for (Punto p : derecha)
            if (Math.abs(p.getX() - xm) < d)
                bandaDer.add(p);
        
        // PASO 8: Comparar puntos entre las dos bandas que podrían formar pares más cercanos
        for (Punto li : bandaIzq) {
            for (Punto rd : bandaDer) {
                // Poda: si la diferencia en X ya es mayor que d, no calcular distancia
                if (Math.abs(li.getX() - rd.getX()) >= d) 
                    continue;
                
                // Poda adicional: si la diferencia en Y es mayor que d, no calcular distancia
                if (Math.abs(li.getY() - rd.getY()) >= d) 
                    continue;
                
                // Calcular distancia entre puntos candidatos
                double dist = distancia_euclidea(li, rd);
                distanciasCalculadas++;
                
                // Actualizar si encontramos un par más cercano
                if (dist < d) {
                    d = dist;
                    mejor = new ParDePuntos(li, rd, dist, distanciasCalculadas);
                }
            }
        }
        
        // PASO 9: Actualizar contador y retornar resultado
        mejor.setNum_distancias_calculadas(distanciasCalculadas);
        return mejor;
    }

    /*
     * Implementa la estrategia Divide y Vencerás mejorada con optimización
     * en el manejo de la franja central mediante ordenación por Y.
     * 
     * Complejidad temporal: O(n log n) garantizado
     * 
     * Mejoras respecto a la versión normal:
     * - Mantiene listas ordenadas por Y para eficiencia en la franja
     * - Reduce el número de comparaciones en la fase de combinación
     * 
     * @param ordenados Lista de puntos a analizar
     * @return ParDePuntos con los dos puntos más cercanos y su distancia
     */
    public static ParDePuntos dyvMejorado(List<Punto> ordenados) {
        // PASO 1: Preordenar por Y una sola vez al inicio
        List<Punto> ordenadosY = new ArrayList<>(ordenados);
        ordenadosY.sort(Comparator.comparingDouble(Punto::getY));
        
        // PASO 2: Llamar a la función recursiva principal
        return dyvMejoradoRec(ordenados, ordenadosY);
    }

    /*
     * Función recursiva auxiliar para el algoritmo Divide y Vencerás mejorado.
     * 
     * @param ordenadosX Lista de puntos ordenados por coordenada X
     * @param ordenadosY Lista de puntos ordenados por coordenada Y
     * @return ParDePuntos con los dos puntos más cercanos y su distancia
     */
    private static ParDePuntos dyvMejoradoRec(List<Punto> ordenadosX, List<Punto> ordenadosY) {
        int n = ordenadosX.size();

        // PASO 1: Caso base - usar algoritmo exhaustivo para conjuntos pequeños
        if (n <= 3) {
            return algoritmo_exhaustivo(new ArrayList<>(ordenadosX));
        }

        // PASO 2: Dividir - encontrar punto medio
        int mid = n / 2;
        double xm = ordenadosX.get(mid).getX();

        // PASO 3: Crear subconjuntos para la división recursiva
        List<Punto> izquierdaX = ordenadosX.subList(0, mid);
        List<Punto> derechaX = ordenadosX.subList(mid, n);

        // PASO 4: Dividir la lista ordenada por Y manteniendo el orden
        List<Punto> izquierdaY = new ArrayList<>();
        List<Punto> derechaY = new ArrayList<>();

        for (Punto p : ordenadosY) {
            if (p.getX() <= xm)
                izquierdaY.add(p);
            else
                derechaY.add(p);
        }

        // PASO 5: Resolver recursivamente cada mitad
        ParDePuntos solIzq = dyvMejoradoRec(izquierdaX, izquierdaY);
        ParDePuntos solDer = dyvMejoradoRec(derechaX, derechaY);

        // PASO 6: Combinar contadores de distancias
        int distancias = solIzq.getNum_distancias_calculadas() + solDer.getNum_distancias_calculadas();

        // PASO 7: Encontrar la distancia mínima entre las dos soluciones
        double d = Math.min(solIzq.getDistancia(), solDer.getDistancia());
        ParDePuntos mejor = (solIzq.getDistancia() <= solDer.getDistancia()) ? solIzq : solDer;

        // PASO 8: Construir franja directamente a partir de ordenadosY (ya ordenada por Y)
        List<Punto> franja = new ArrayList<>();
        for (Punto p : ordenadosY) {
            if (Math.abs(p.getX() - xm) < d) {
                franja.add(p);
            }
        }

        // PASO 9: Buscar en la franja (optimizado gracias al orden por Y)
        for (int i = 0; i < franja.size(); i++) {
            // Solo comparar con puntos siguientes cuya diferencia en Y sea menor que d
            for (int j = i + 1; j < franja.size() && (franja.get(j).getY() - franja.get(i).getY()) < d; j++) {
                double dist = distancia_euclidea(franja.get(i), franja.get(j));
                distancias++;
                if (dist < d) {
                    d = dist;
                    mejor = new ParDePuntos(franja.get(i), franja.get(j), d, distancias);
                }
            }
        }

        // PASO 10: Actualizar contador y retornar resultado
        mejor.setNum_distancias_calculadas(distancias);
        return mejor;
    }
}
