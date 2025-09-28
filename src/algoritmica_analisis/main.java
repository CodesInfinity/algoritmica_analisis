package algoritmica_analisis;

import java.util.ArrayList;
import java.util.List;

import Clases.*;

public class main {

	public static void main(String[] args) {
		   try {
	            // ⚠️ Cambia la ruta por la ubicación real de tu fichero .tsp
	            String rutaDataset = "./src/datasets/ch130.tsp";

	            // 1. Cargar el dataset desde el fichero
	            List<Punto> puntos = DatasetManager.cargarTSP(rutaDataset);

	            System.out.println("✅ Dataset cargado correctamente");
	            System.out.println("Número de puntos: " + puntos.size());

	            // Mostrar los primeros 5 puntos para comprobar
	            for (int i = 0; i < Math.min(5, puntos.size()); i++) {
	                System.out.println(puntos.get(i));
	            }

	            // 2. Ejecutar el algoritmo exhaustivo
	            ParDePuntos resultado = Algoritmos.busquedaExhaustiva(puntos);

	            // 3. Mostrar el resultado
	            System.out.println("\n🔎 Resultado búsqueda exhaustiva:");
	            System.out.println("Par más cercano: " + resultado);

			   
			   System.out.println("Directorio de trabajo actual: " + System.getProperty("user.dir"));

	        } catch (Exception e) {
	            System.err.println("❌ Error al leer el dataset: " + e.getMessage());
	            e.printStackTrace();
	        }
	}

}
