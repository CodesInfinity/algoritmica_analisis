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

	private static int distancia_DYV_MEJORADO = 0;

	private static ParDePuntos Punto_mas_cercano(ParDePuntos izq, ParDePuntos der, ParDePuntos central) {
		// ESCOGEMOS EL MEJOR ENTRE LA IZQUIERDA Y LA DERECHA
		ParDePuntos mejor_par = izq;

		if (der.getDistancia() < mejor_par.getDistancia()) {
			mejor_par = der;
		}

		if (central != null && central.getDistancia() < mejor_par.getDistancia()) {
			mejor_par = central;
		}

		return mejor_par;
	}

	private static ParDePuntos DyV_RECURSIVO(List<Punto> puntos) {
		int longitud_lista = puntos.size();

		// CASO BASE --> SI HAY 2 O 3 PUNTOS UTILIZAR BUSQUEDA EXHAUSTIVA QUE ES MAS
		// OPTIMO
		if (longitud_lista <= 3) {
			ParDePuntos resultado = algoritmo_exhaustivo(puntos);
			distancia_DYV_MEJORADO = distancia_DYV_MEJORADO + resultado.getNum_distancias_calculadas();
			return resultado;
		}

		// ENCONTRAR EL PUNTO MEDIO DE LA LISTA
		int medio = longitud_lista / 2;
		Punto punto_medio = puntos.get(medio);

		// UNA VEZ ENCONTRADO EL PUNTO MEDIO, SEPARAMOS LAS MITADOS
		List<Punto> puntos_izquierda = new ArrayList<Punto>(puntos.subList(0, medio));
		List<Punto> puntos_derecha = new ArrayList<Punto>(puntos.subList(medio, puntos.size()));

		// RESOLVEMOS DE FORMA RECURSIVA CADA MITAD Y OBTENEMOS LOS MEJORES PUNTOS
		ParDePuntos par_Izquierda = DyV_RECURSIVO(puntos_izquierda);
		ParDePuntos par_Derecha = DyV_RECURSIVO(puntos_derecha);

		// ENCONTRAMOS LA DISTANCIA MINIMA ENTRE EL PAR DE LA IZQUIERDA Y DE LA DERECHA
		double distancia_minima = Math.min(par_Izquierda.getDistancia(), par_Derecha.getDistancia());

		// BUSCAR PUNTOS CERCANOS QUE ESTEN EN LA LINEA DIVISORIA CENTRAL
		ParDePuntos par_linea_divisoria = BuscarEnFranjaCentral(puntos, punto_medio.getX(), distancia_minima);

		return Punto_mas_cercano(par_Izquierda, par_Derecha, par_linea_divisoria);

	}

	private static ParDePuntos BuscarEnFranjaCentral(List<Punto> puntos, double X_medio, double distancia_minima) {
		// LA FORMULA PARA ACCEDER A LA FRANJA CENTRAL ---> |X - X_medio| <
		// distancia_minima
		// RECOGEMOS LOS PUNTOS QUE ESTAN EN LA FRANJA CENTRAL
		List<Punto> franja = new ArrayList<Punto>();

		for (int i = 0; i < puntos.size(); i++) {
			if (Math.abs(puntos.get(i).getX() - X_medio) < distancia_minima) {
				franja.add(puntos.get(i));
			}
		}

		// AQUI VIENE LA PARTE MEJORADA DEL DIVIDE Y VENCERAS
		// SE ORDENA LA FRANJA TENIENDO EN CUENTA LA COORDENADA Y
		for (int i = 0; i < franja.size(); i++) {
			for (int j = 0; j < franja.size() - 1; j++) {
				if (franja.get(j).getY() > franja.get(j + 1).getY()) {
					Punto aux = franja.get(j + 1);
					franja.set(j + 1, franja.get(j));
					franja.set(j, aux);
				}
			}
		}

		// BUSCAMOS EN LA FRANJA UNA VEZ ORDENADA Y DEVOLVEMOS EL MEJOR PAR
		return BuscarFranjaOrdenada(franja, distancia_minima);
	}

	private static ParDePuntos BuscarFranjaOrdenada(List<Punto> franja, double distancia_minima) {
		ParDePuntos mejorPar = null;
		int num_distancias = 0;
		double d_min = distancia_minima;

		for (int i = 0; i < franja.size(); i++) {
			Punto puntoActual = franja.get(i);
			for (int j = i + 1; j < franja.size() && j <= i + 11; j++) // SEARCH WHY 11
			{
				Punto puntoComparar = franja.get(j);

				if (puntoComparar.getY() - puntoActual.getY() >= d_min) {
					break;
				} else {
					double distancia_real = distancia_euclidea(puntoActual, puntoComparar);
					num_distancias++;
					distancia_DYV_MEJORADO++;

					if (distancia_real < d_min) {
						d_min = distancia_real;
						mejorPar = new ParDePuntos(puntoActual, puntoComparar, d_min, num_distancias);
					}
				}
			}
		}

		return mejorPar;
	}

	/*
	 * Calcula la distancia euclidiana entre dos puntos en el plano 2D. La fórmula
	 * utilizada es: √[(x₁-x₂)² + (y₁-y₂)²]
	 */
	public static double distancia_euclidea(Punto p1, Punto p2) {
		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static double distancia_euclidea_cuadrada(Punto p1, Punto p2) {
		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY();
		return dx * dx + dy * dy; // Sin sqrt
	}

	/*
	 * Implementa la estrategia de búsqueda exhaustiva para encontrar el par de
	 * puntos más cercanos.
	 * 
	 * Complejidad temporal: O(n²) Compara todos los pares de puntos posibles
	 * (n·(n-1)/2 comparaciones)
	 */
	public static ParDePuntos algoritmo_exhaustivo(List<Punto> puntos) {
	    if (puntos == null || puntos.size() < 2) {
	        throw new IllegalArgumentException("NO HAY PUNTOS SUFICIENTES\n");
	    }

	    long start = System.nanoTime();

	    double distanciaMinimaCuadrada = Double.MAX_VALUE;
	    Punto punto1 = null;
	    Punto punto2 = null;
	    int distanciasCalculadas = 0;

	    for (int i = 0; i < puntos.size(); i++) {
	        Punto a = puntos.get(i);
	        for (int j = i + 1; j < puntos.size(); j++) {
	            Punto b = puntos.get(j);

	            double distanciaCuadrada = distancia_euclidea_cuadrada(a, b);
	            distanciasCalculadas++;

	            if (distanciaCuadrada < distanciaMinimaCuadrada) {
	                distanciaMinimaCuadrada = distanciaCuadrada;
	                punto1 = a;
	                punto2 = b;
	            }
	        }
	    }

	    // Solo calcular sqrt una vez al final para mejorar el tiempo de ejecución.
	    double distanciaMinima = Math.sqrt(distanciaMinimaCuadrada);

	    long end = System.nanoTime();
	    double tiempoEjecucion = (double) (end - start) / 1_000_000;

	    ParDePuntos resultadoFinal = new ParDePuntos(punto1, punto2, distanciaMinima, distanciasCalculadas);
	    resultadoFinal.setTiempo_de_ejecucion(tiempoEjecucion);

	    return resultadoFinal;
	}

	/*
	 * Implementa la estrategia de búsqueda exhaustiva con poda para encontrar el
	 * par de puntos más cercanos.
	 * 
	 * Complejidad temporal: O(n log n) para ordenar + O(n²) en peor caso, pero
	 * mucho mejor en caso medio debido a la poda
	 * 
	 * Estrategia: Ordena los puntos por coordenada X y descarta comparaciones
	 * cuando la diferencia en X ya supera la distancia mínima encontrada
	 */
	public static ParDePuntos algoritmo_busqueda_poda(List<Punto> puntos) {
		if (puntos == null || puntos.size() < 2) {
			throw new IllegalArgumentException("NO HAY PUNTOS SUFICIENTES\n");
		}

		// Ordenar los puntos por coordenada X usando QuickSort (Collections.sort usa
		// Timsort)
		puntos.sort(Comparator.comparingDouble(Punto::getX));

		long START = System.nanoTime();

		double distanciaMinima = Double.MAX_VALUE;
		Punto punto1 = null;
		Punto punto2 = null;
		int distanciasCalculadas = 0;

		for (int i = 0; i < puntos.size(); i++) {
			// Iterar mientras j sea válido Y la diferencia en X sea menor que
			// distanciaMinima
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

	public static ParDePuntos algoritmo_DIVIDE_Y_VENCERAS_MEJORADO(List<Punto> puntos) {
		distancia_DYV_MEJORADO = 0;
		long START = System.nanoTime();

		// SE NECESITAN COMO MINIMO 2 PUNTOS
		if (puntos == null || puntos.size() < 2) {
			throw new IllegalArgumentException("NO HAY PUNTOS SUFICIENTES\n");
		}

		// ORDENAR LA LISTO DE PUNTOS TENIENDO EN CUENTA LA COORDENADA X
		for (int i = 0; i < puntos.size(); i++) {
			for (int j = 0; j < puntos.size() - 1; j++) {
				if (puntos.get(j).getX() > puntos.get(j + 1).getX()) {
					Punto aux = puntos.get(j + 1);
					puntos.set(j + 1, puntos.get(j));
					puntos.set(j, aux);
				}
			}
		}

		// EJECUTAR ALGORITMO RECURSIVO QUE DIVIDARA LA LISTA POR LA MITAD
		ParDePuntos resultado_final = DyV_RECURSIVO(puntos);

		long END = System.nanoTime();
		double TIEMPO_DE_EJECUCION = (double) (END - START) / 1000000; // MILISEGUNDOS

		resultado_final.setNum_distancias_calculadas(distancia_DYV_MEJORADO);
		resultado_final.setTiempo_de_ejecucion(TIEMPO_DE_EJECUCION);

		return resultado_final;

	}

}
