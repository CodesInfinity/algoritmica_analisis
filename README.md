# 📌 Práctica 1 - Algoritmos Exhaustivos y Divide y Vencerás  

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Status](https://img.shields.io/badge/Status-En%20desarrollo-blue?style=for-the-badge)
![License](https://img.shields.io/badge/Licencia-Académica-green?style=for-the-badge)

---

## 🎯 Descripción  

Este proyecto implementa y compara diferentes algoritmos para resolver el **problema del par de puntos más cercanos en un plano**.  

Forma parte de la asignatura **Algorítmica y Modelos de Computación (3º Ingeniería Informática, curso 2024-25)**.  

Se estudian cuatro estrategias algorítmicas:  

1. **Búsqueda exhaustiva**  
2. **Búsqueda exhaustiva con poda**  
3. **Divide y Vencerás**  
4. **Divide y Vencerás mejorado**  

El objetivo es analizar su **eficiencia teórica** y **rendimiento práctico** mediante experimentación.  

---

## ⚙️ Funcionalidades  

✔️ Generar datasets aleatorios en formato `.tsp` (caso medio o peor caso).  
✔️ Cargar datasets desde ficheros `.tsp` propios o de la librería [TSPLIB95](http://www.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/).  
✔️ Ejecutar las 4 estrategias sobre un dataset cargado.  
✔️ Comparar todas las estrategias con diferentes tamaños de entrada.  
✔️ Comparar 2 algoritmos concretos en tiempo y número de distancias calculadas.  
✔️ Mostrar resultados en **modo texto** y opcionalmente en **modo gráfico** (panel con puntos y línea de unión).  
✔️ Exportar resultados a ficheros `.tsp`.  

---

## 📂 Estructura del Proyecto  

---

## 🖥️ Ejemplo de salida en consola  

```txt
ch130.tsp
Estrategia        Punto1                     Punto2                     distancia   calculadas   tiempo(mseg)
Exhaustivo        12 (252.749, 535.743)      87 (252.429, 535.166)      0.66018099     8385        0.1729
ExhaustivoPoda    87 (252.429, 535.166)      12 (252.749, 535.743)      0.66018099       48        0.0301
DivideVenceras    87 (252.429, 535.166)      12 (252.749, 535.743)      0.66018099      290        0.0402
DyV Mejorado      87 (252.429, 535.166)      12 (252.749, 535.743)      0.66018099      160        0.0586
```
🚀 Ejecución
1. Compilar el proyecto
javac src/*.java -d bin

2. Ejecutar el programa
java -cp bin Main

3. Opciones disponibles

Crear dataset aleatorio

Cargar dataset existente

Probar las 4 estrategias

Comparar todas las estrategias

Comparar 2 estrategias

🔧 Requisitos Técnicos

Lenguaje: Java 17+

Ordenación: Implementación propia de HeapSort o QuickSort

Bibliotecas opcionales:

Para gráficas: JFreeChart
, JMathPlot

📖 Documentación

En la carpeta docs/ se incluye:

Estudio teórico (pseudocódigo, análisis de complejidad).

Ejemplos de mejor y peor caso.

Comparación experimental con tablas y gráficas.

Conclusiones sobre eficiencia de cada estrategia.

📝 Licencia

Este proyecto se desarrolla con fines académicos en el marco de la asignatura Algorítmica y Modelos de Computación (Universidad de Huelva, curso 2025-26).
