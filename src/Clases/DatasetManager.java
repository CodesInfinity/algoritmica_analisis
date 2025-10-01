package Clases;

import java.io.*;
import java.util.*;

/**
 * Clase encargada de la lectura y carga de datasets TSP
 * (Travelling Salesman Problem) desde un fichero.
 * 
 * El formato esperado es el estándar TSPLIB, donde los puntos
 * aparecen tras la línea "NODE_COORD_SECTION" y finalizan con "EOF".
 */
public class DatasetManager {

    /**
     * Carga un conjunto de puntos a partir de un fichero en formato TSPLIB.
     * 
     * @param rutaFichero Ruta al fichero de entrada (.tsp o .txt con formato TSPLIB).
     * @return Lista de objetos Punto con las coordenadas leídas.
     * @throws IOException Si ocurre un error al leer el fichero.
     */
    public static List<Punto> cargarTSP(String rutaFichero) throws IOException {
        // Lista donde se guardarán los puntos leídos
        List<Punto> puntos = new ArrayList<>();

        // Lectura del fichero línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;
            boolean seccionCoordenadas = false; // Marca el inicio de la sección de coordenadas

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // Cuando se encuentra "NODE_COORD_SECTION", comienza la parte con los puntos
                if (linea.startsWith("NODE_COORD_SECTION")) {
                    seccionCoordenadas = true;
                    continue;
                }

                // Cuando se encuentra "EOF", significa fin de datos
                if (linea.equals("EOF")) {
                    break;
                }

                // Si estamos dentro de la sección de coordenadas, procesamos las líneas
                if (seccionCoordenadas) {
                    String[] partes = linea.split("\\s+"); // Separar por espacios
                    if (partes.length >= 3) {
                        // Cada línea tiene: id, x, y
                        int id = Integer.parseInt(partes[0]);
                        double x = Double.parseDouble(partes[1]);
                        double y = Double.parseDouble(partes[2]);
                        
                        // Crear y añadir el punto a la lista
                        puntos.add(new Punto(id, x, y));
                    }
                }
            }
        }

        return puntos;
    }
}
