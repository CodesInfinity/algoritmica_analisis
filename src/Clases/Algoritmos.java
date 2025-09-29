package Clases;

import java.util.List;

public class Algoritmos {

    /*
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
     */


    public static double DISTANCIA_EUCLIDEA (Punto p1, Punto p2)
    {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX() , 2) + Math.pow(p1.getY() - p2.getY() , 2)) ;
    }

    //LO PONGO STATIC PARA PODER LLAMARLO DESDE OTRO SITIO
    //SIN TENER QUE CREAR UN OBJETO
    public static ParDePuntos algoritmo_exhaustivo(List<Punto> puntos)
    {
        if (puntos == null || puntos.size() < 2) {
            throw new IllegalArgumentException("NO HAY PUNTOS SUFICIENTES\n");
        }

        long START = System.nanoTime() ;

        double Distancia_minima = Double.MAX_VALUE ;
        Punto punto1 = null ;
        Punto punto2 = null ;
        int Distancias_Calculadas = 0 ;

        for (int i = 0 ; i < puntos.size() ; i++)
        {
            for (int j = i+1 ; j < puntos.size() ; j++)
            {
                double distancia = DISTANCIA_EUCLIDEA(puntos.get(i) , puntos.get(j)) ;
                Distancias_Calculadas++ ;

                if (distancia < Distancia_minima)
                {
                    Distancia_minima = distancia ;
                    punto1 = puntos.get(i) ;
                    punto2 = puntos.get(j) ;
                }
            }
        }

        long END = System.nanoTime() ;
        double TIEMPO_DE_EJECUCION = (double) (END - START) / 1000000 ; // MILISEGUNDOS

        ParDePuntos resultado_final = new ParDePuntos(punto1,punto2,Distancia_minima,Distancias_Calculadas) ;
        resultado_final.setTiempo_de_ejecucion(TIEMPO_DE_EJECUCION) ;

        return resultado_final ;
    }
}

