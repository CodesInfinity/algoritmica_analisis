package Clases;

import java.io.*;
import java.util.*;

public class DatasetManager {

    public static List<Punto> cargarTSP(String rutaFichero) throws IOException {
        List<Punto> puntos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;
            boolean seccionCoordenadas = false;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.startsWith("NODE_COORD_SECTION")) {
                    seccionCoordenadas = true;
                    continue;
                }

                if (linea.equals("EOF")) {
                    break;
                }

                if (seccionCoordenadas) {
                    String[] partes = linea.split("\\s+");
                    if (partes.length >= 3) {
                        int id = Integer.parseInt(partes[0]);
                        double x = Double.parseDouble(partes[1]);
                        double y = Double.parseDouble(partes[2]);
                        puntos.add(new Punto(id, x, y));
                    }
                }
            }
        }

        return puntos;
    }
}