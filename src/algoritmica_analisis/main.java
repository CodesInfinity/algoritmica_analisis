package algoritmica_analisis;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import Clases.*;

public class main {

    public static void main(String[] args) {
        try {
            System.out.println("\nDirectorio de trabajo actual: " + System.getProperty("user.dir"));

            // Listar los datasets disponibles
            File carpeta = new File("./src/datasets");
            File[] ficheros = carpeta.listFiles((dir, name) -> name.endsWith(".tsp")); // solo archivos .tsp

            if (ficheros == null || ficheros.length == 0) {
                System.out.println("No se encontraron datasets en ./src/datasets");
                return;
            }

            System.out.println("\nDatasets disponibles:");
            for (File f : ficheros) {
                System.out.println(" - " + f.getName());
            }

            Scanner sc = new Scanner(System.in);
            System.out.println("\nIntroduce el nombre del dataset que quieres ejecutar (sin la extensión si quieres):");
            String dataset = sc.nextLine().trim();

            // Si el usuario no escribió la extensión, añadirla automáticamente
            if (!dataset.endsWith(".tsp")) {
                dataset += ".tsp";
            }

            String rutaDataset = "./src/datasets/" + dataset;

            File ficheroSeleccionado = new File(rutaDataset);
            if (!ficheroSeleccionado.exists()) {
                System.out.println("El fichero " + dataset + " no existe en ./src/datasets");
                return;
            }

            // 1. Cargar el dataset desde el fichero
            List<Punto> puntos = DatasetManager.cargarTSP(rutaDataset);

            System.out.println("Dataset cargado correctamente");
            System.out.println("Número de puntos: " + puntos.size());

            // Mostrar los primeros 5 puntos para comprobar
            for (int i = 0; i < Math.min(5, puntos.size()); i++) {
                System.out.println(puntos.get(i));
            }

            // 2. Ejecutar los algoritmos
            ParDePuntos resultado = Algoritmos.algoritmo_exhaustivo(puntos);
            ParDePuntos resultado2 = Algoritmos.algoritmo_busqueda_poda(puntos);

            // 3. Mostrar resultados
            System.out.println("\nResultado búsqueda exhaustiva:");
            System.out.println("Par más cercano: " + resultado);
            System.out.println("Distancia del par más cercano: " + resultado.getDistancia());
            System.out.println("TIEMPO DE EJECUCION: " + resultado.getTiempo_de_ejecucion() + " ms");
            System.out.println("NUMERO DE DISTANCIAS CALCULADAS: " + resultado.getNum_distancias_calculadas());

            System.out.println("\nResultado búsqueda con poda:");
            System.out.println("Par más cercano: " + resultado2);
            System.out.println("Distancia del par más cercano: " + resultado2.getDistancia());
            System.out.println("TIEMPO DE EJECUCION: " + resultado2.getTiempo_de_ejecucion() + " ms");
            System.out.println("NUMERO DE DISTANCIAS CALCULADAS: " + resultado2.getNum_distancias_calculadas());

        } catch (Exception e) {
            System.err.println("Error al leer el dataset: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

