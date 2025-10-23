package algoritmica_analisis;

import Clases.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;
import java.util.List;
import java.text.DecimalFormat;

/**
 * Versión final de la interfaz Swing
 * - Solo usa distribución uniforme
 * - Exporta CSV de resultados y comparaciones a src/comparaciones/
 * - Visualización gráfica de dataset y comparaciones
 */
public class MainGUI {

    private JFrame frame;
    private DatasetPanel datasetPanel;
    private JTable resultTable;
    private DefaultTableModel resultModel;
    private JTextArea logArea;

    private List<Punto> currentDataset = new ArrayList<>();
    private String currentDatasetName = "Ninguno";
    private File datasetsDirectory;
    private File comparacionesDirectory;

    private final DecimalFormat distFmt = new DecimalFormat("#0.00000000");
    private final DecimalFormat timeFmt = new DecimalFormat("#0.0000");

    // Colores de interfaz
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color ACCENT_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().init());
    }

    private void init() {
        frame = new JFrame("Análisis de Algoritmos - Par Más Cercano");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        initializeDirectories();
        setupGUI();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initializeDirectories() {
        File currentDir = new File(".");
        File srcDir = new File(currentDir, "src");

        File datasetsDir = new File(srcDir, "datasets");
        File comparacionesDir = new File(srcDir, "comparaciones");

        if (!datasetsDir.exists()) datasetsDir.mkdirs();
        if (!comparacionesDir.exists()) comparacionesDir.mkdirs();

        datasetsDirectory = datasetsDir;
        comparacionesDirectory = comparacionesDir;
    }

    private void setupGUI() {
        frame.add(createHeaderPanel(), BorderLayout.NORTH);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(800);
        mainSplitPane.setResizeWeight(0.6);

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);

        frame.add(mainSplitPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Análisis de Algoritmos - Par Más Cercano");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel datasetInfo = new JLabel("Dataset actual: " + currentDatasetName);
        datasetInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        datasetInfo.setForeground(Color.WHITE);
        datasetInfo.setName("datasetInfo");

        header.add(title, BorderLayout.WEST);
        header.add(datasetInfo, BorderLayout.EAST);
        return header;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        JPanel controlPanel = createControlPanel();

        datasetPanel = new DatasetPanel();
        datasetPanel.setBorder(BorderFactory.createTitledBorder("Visualización del Dataset"));

        leftPanel.add(controlPanel, BorderLayout.NORTH);
        leftPanel.add(datasetPanel, BorderLayout.CENTER);
        return leftPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Gestión de Datasets"));

        JPanel datasetManagement = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton loadBtn = createStyledButton("Cargar Dataset", PRIMARY_COLOR);
        JButton generateBtn = createStyledButton("Generar Dataset", SECONDARY_COLOR);
        JButton clearBtn = createStyledButton("Limpiar", new Color(149, 165, 166));

        loadBtn.addActionListener(e -> onLoadDataset());
        generateBtn.addActionListener(e -> onGenerateDataset());
        clearBtn.addActionListener(e -> onClearAll());

        datasetManagement.add(loadBtn);
        datasetManagement.add(generateBtn);
        datasetManagement.add(clearBtn);

        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton runAllBtn = createStyledButton("Ejecutar Todos los Algoritmos", ACCENT_COLOR);
        JButton compareBtn = createStyledButton("Comparar Rendimiento", new Color(46, 204, 113));

        runAllBtn.addActionListener(e -> onRunAll());
        compareBtn.addActionListener(e -> onComparePerformance());

        algorithmPanel.add(runAllBtn);
        algorithmPanel.add(compareBtn);

        controlPanel.add(datasetManagement);
        controlPanel.add(algorithmPanel);
        return controlPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(200, 35));
        return button;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        JPanel resultsPanel = createResultsPanel();
        JPanel logPanel = createLogPanel();

        rightPanel.add(resultsPanel, BorderLayout.CENTER);
        rightPanel.add(logPanel, BorderLayout.SOUTH);
        return rightPanel;
    }

    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Resultados de los Algoritmos"));

        String[] columnNames = {"Algoritmo", "Punto 1", "Punto 2", "Distancia", "Cálculos", "Tiempo (ms)"};
        resultModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        resultTable = new JTable(resultModel);
        resultTable.setRowHeight(25);
        resultTable.getTableHeader().setBackground(PRIMARY_COLOR);
        resultTable.getTableHeader().setForeground(Color.WHITE);
        resultTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton exportBtn = createStyledButton("Exportar CSV", new Color(243, 156, 18));
        exportBtn.addActionListener(e -> onExportCSV());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(exportBtn);
        resultsPanel.add(bottom, BorderLayout.SOUTH);

        return resultsPanel;
    }

    private JPanel createLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Registro de Actividad"));
        logPanel.setPreferredSize(new Dimension(0, 150));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(logArea);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        return logPanel;
    }

    /* ========================= EVENTOS ============================ */

    private void onLoadDataset() {
        JFileChooser fc = new JFileChooser(datasetsDirectory);
        fc.setFileFilter(new FileNameExtensionFilter("Archivos TSP", "tsp"));
        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fc.getSelectedFile();
                currentDataset = DatasetManager.cargarTSP(f.getAbsolutePath());
                currentDatasetName = f.getName().replace(".tsp", "");
                datasetPanel.setPoints(currentDataset);
                datasetPanel.repaint();
                updateDatasetInfo();
                log("✓ Dataset cargado: " + f.getName());
            } catch (Exception e) {
                showError("Error al cargar dataset: " + e.getMessage());
            }
        }
    }

    private void onGenerateDataset() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField sizeField = new JTextField("1000");
        JTextField nameField = new JTextField("dataset_generado");
        panel.add(new JLabel("Número de puntos:"));
        panel.add(sizeField);
        panel.add(new JLabel("Nombre del dataset:"));
        panel.add(nameField);

        int res = JOptionPane.showConfirmDialog(frame, panel, "Generar Dataset (Distribución Uniforme)",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                int n = Integer.parseInt(sizeField.getText().trim());
                if (n <= 0) { showError("Debe ingresar un número positivo."); return; }
                generateDataset(n);
                currentDatasetName = nameField.getText().trim();
                updateDatasetInfo();
                log("✓ Dataset generado: " + currentDatasetName + " (" + n + " puntos)");
            } catch (Exception e) {
                showError("Error al generar dataset: " + e.getMessage());
            }
        }
    }

    private void onRunAll() {
        if (currentDataset.isEmpty()) { showWarning("Primero carga o genera un dataset."); return; }
        resultModel.setRowCount(0);
        try {
            ParDePuntos ex = Algoritmos.algoritmo_exhaustivo(deepCopy(currentDataset));
            addResult("Exhaustivo", ex);
            List<Punto> sorted = deepCopy(currentDataset);
            sortByX(sorted);
            addResult("Exhaustivo con Poda", Algoritmos.algoritmo_busqueda_poda(sorted));
            List<Punto> sorted2 = deepCopy(currentDataset);
            sortByX(sorted2);
            addResult("Divide y Vencerás", Algoritmos.dyvNormal(sorted2));
            List<Punto> sorted3 = deepCopy(currentDataset);
            sortByX(sorted3);
            addResult("Divide y Vencerás Mejorado", Algoritmos.dyvMejorado(sorted3));
            datasetPanel.setSolution(ex.getP1(), ex.getP2());
            datasetPanel.repaint();
        } catch (Exception e) { showError("Error ejecutando algoritmos: " + e.getMessage()); }
    }

    private void onComparePerformance() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField sizesField = new JTextField("100,500,1000,2000");
        JTextField repsField = new JTextField("3");
        panel.add(new JLabel("Tamaños (separados por comas):")); panel.add(sizesField);
        panel.add(new JLabel("Repeticiones:")); panel.add(repsField);

        int res = JOptionPane.showConfirmDialog(frame, panel, "Comparar Rendimiento",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            List<Integer> tallas = new ArrayList<>();
            for (String s : sizesField.getText().split(",")) tallas.add(Integer.parseInt(s.trim()));
            int reps = Integer.parseInt(repsField.getText().trim());
            runPerformanceComparison(tallas, reps);
        } catch (Exception e) { showError("Entrada inválida: " + e.getMessage()); }
    }

    private void runPerformanceComparison(List<Integer> tallas, int reps) {
        Map<String, List<Double>> tiempos = new LinkedHashMap<>();
        String[] algos = {"Exhaustivo", "Exhaustivo con Poda", "Divide y Vencerás", "Divide y Vencerás Mejorado"};
        for (String a : algos) tiempos.put(a, new ArrayList<>());

        for (int n : tallas) {
            log("Procesando tamaño " + n);
            double[] sum = new double[4];
            for (int r = 0; r < reps; r++) {
                generateDataset(n);
                List<Punto> d = deepCopy(currentDataset);
                sum[0] += Algoritmos.algoritmo_exhaustivo(deepCopy(d)).getTiempo_de_ejecucion();
                List<Punto> t = deepCopy(d); sortByX(t);
                sum[1] += Algoritmos.algoritmo_busqueda_poda(t).getTiempo_de_ejecucion();
                t = deepCopy(d); sortByX(t);
                sum[2] += Algoritmos.dyvNormal(t).getTiempo_de_ejecucion();
                t = deepCopy(d); sortByX(t);
                sum[3] += Algoritmos.dyvMejorado(t).getTiempo_de_ejecucion();
            }
            for (int i = 0; i < 4; i++) tiempos.get(algos[i]).add(sum[i] / reps);
        }

        exportComparisonToCSV(tallas, tiempos);
        showComparisonChart(tallas, tiempos);
        log("✓ Comparación completada y CSV guardado en /src/comparaciones/");
    }

    private void onExportCSV() {
        if (resultModel.getRowCount() == 0) {
            showWarning("No hay resultados para exportar.");
            return;
        }
        try {
            File out = new File(comparacionesDirectory, "Resultados_" + currentDatasetName + ".csv");
            try (PrintWriter pw = new PrintWriter(out)) {
                for (int i = 0; i < resultModel.getColumnCount(); i++) {
                    pw.print(resultModel.getColumnName(i));
                    if (i < resultModel.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
                for (int r = 0; r < resultModel.getRowCount(); r++) {
                    for (int c = 0; c < resultModel.getColumnCount(); c++) {
                        pw.print(resultModel.getValueAt(r, c));
                        if (c < resultModel.getColumnCount() - 1) pw.print(",");
                    }
                    pw.println();
                }
            }
            JOptionPane.showMessageDialog(frame, "CSV guardado en: " + out.getAbsolutePath());
        } catch (Exception e) { showError("Error exportando CSV: " + e.getMessage()); }
    }

    private void exportComparisonToCSV(List<Integer> tallas, Map<String, List<Double>> tiempos) {
        try {
            // Crear carpeta si no existe
            if (!comparacionesDirectory.exists()) comparacionesDirectory.mkdirs();

            // Archivo de salida con nombre único
            File f = new File(comparacionesDirectory, "Comparacion_" + System.currentTimeMillis() + ".csv");

            try (PrintWriter pw = new PrintWriter(f, "UTF-8")) {
                // Encabezado ordenado
                pw.println("Tamano;Exhaustivo;Exhaustivo con Poda;Divide y Venceras;Divide y Venceras Mejorado");

                // Escribir filas con punto decimal (formato compatible con Excel)
                int numFilas = tallas.size();
                for (int i = 0; i < numFilas; i++) {
                    StringBuilder line = new StringBuilder();
                    line.append(tallas.get(i)); // Tamaño
                    for (String algoritmo : Arrays.asList(
                            "Exhaustivo", "Exhaustivo con Poda", "Divide y Vencerás", "Divide y Vencerás Mejorado")) {
                        List<Double> tiemposAlgo = tiempos.get(algoritmo);
                        if (tiemposAlgo != null && i < tiemposAlgo.size())
                            line.append(";").append(String.format(Locale.US, "%.6f", tiemposAlgo.get(i)));
                        else
                            line.append(";");
                    }
                    pw.println(line.toString());
                }
            }

            log("✓ CSV de comparación guardado en: " + f.getName());
            JOptionPane.showMessageDialog(frame,
                    "Archivo CSV exportado correctamente en:\n" + f.getAbsolutePath(),
                    "Exportación completada", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            showError("Error al exportar comparación: " + e.getMessage());
        }
    }

    private void onClearAll() {
        currentDataset.clear();
        datasetPanel.setPoints(new ArrayList<>());
        resultModel.setRowCount(0);
        logArea.setText("");
        currentDatasetName = "Ninguno";
        updateDatasetInfo();
        datasetPanel.repaint();
    }

    /* ======================= UTILIDADES ======================== */

    private void generateDataset(int n) {
        List<Punto> pts = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < n; i++)
            pts.add(new Punto(i + 1, rnd.nextDouble() * 1000, rnd.nextDouble() * 1000));
        currentDataset = pts;
        datasetPanel.setPoints(pts);
        datasetPanel.repaint();
    }

    private void updateDatasetInfo() {
        for (Component c : frame.getContentPane().getComponents())
            if (c instanceof JPanel) for (Component cc : ((JPanel)c).getComponents())
                if (cc instanceof JLabel && "datasetInfo".equals(cc.getName()))
                    ((JLabel)cc).setText("Dataset actual: " + currentDatasetName + " (" + currentDataset.size() + " puntos)");
    }

    private List<Punto> deepCopy(List<Punto> list) {
        List<Punto> copy = new ArrayList<>();
        for (Punto p : list) copy.add(new Punto(p.getId(), p.getX(), p.getY()));
        return copy;
    }

    private void sortByX(List<Punto> list) { list.sort(Comparator.comparingDouble(Punto::getX)); }

    private void addResult(String algo, ParDePuntos r) {
        resultModel.addRow(new Object[]{
                algo, r.getP1().getId(), r.getP2().getId(),
                distFmt.format(r.getDistancia()),
                r.getNum_distancias_calculadas(),
                timeFmt.format(r.getTiempo_de_ejecucion())
        });
    }

    private void log(String msg) { logArea.append("[" + new Date() + "] " + msg + "\n"); }
    private void showError(String msg) { JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE); }
    private void showWarning(String msg) { JOptionPane.showMessageDialog(frame, msg, "Atención", JOptionPane.WARNING_MESSAGE); }

    /* ======================= CLASES INTERNAS ======================== */

    private static class DatasetPanel extends JPanel {
        private List<Punto> points = new ArrayList<>();
        private Punto p1, p2;

        void setPoints(List<Punto> pts) { this.points = pts; }
        void setSolution(Punto a, Punto b) { this.p1 = a; this.p2 = b; }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (points == null || points.isEmpty()) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight(), pad = 20;
            double minX = points.stream().mapToDouble(Punto::getX).min().getAsDouble();
            double maxX = points.stream().mapToDouble(Punto::getX).max().getAsDouble();
            double minY = points.stream().mapToDouble(Punto::getY).min().getAsDouble();
            double maxY = points.stream().mapToDouble(Punto::getY).max().getAsDouble();
            g2.setColor(new Color(52, 152, 219));
            for (Punto p : points) {
                int x = pad + (int) ((p.getX() - minX) / (maxX - minX) * (w - 2 * pad));
                int y = h - pad - (int) ((p.getY() - minY) / (maxY - minY) * (h - 2 * pad));
                g2.fillOval(x - 2, y - 2, 4, 4);
            }
            if (p1 != null && p2 != null) {
                g2.setColor(Color.RED);
                int x1 = pad + (int) ((p1.getX() - minX) / (maxX - minX) * (w - 2 * pad));
                int y1 = h - pad - (int) ((p1.getY() - minY) / (maxY - minY) * (h - 2 * pad));
                int x2 = pad + (int) ((p2.getX() - minX) / (maxX - minX) * (w - 2 * pad));
                int y2 = h - pad - (int) ((p2.getY() - minY) / (maxY - minY) * (h - 2 * pad));
                g2.setStroke(new BasicStroke(2));
                g2.draw(new Line2D.Double(x1, y1, x2, y2));
            }
        }
    }

    private void showComparisonChart(List<Integer> tallas, Map<String, List<Double>> tiempos) {
        JFrame chart = new JFrame("Comparación de Rendimiento");
        chart.setSize(800, 500);
        chart.setLocationRelativeTo(frame);
        chart.add(new ChartPanel(tallas, tiempos));
        chart.setVisible(true);
    }

    private static class ChartPanel extends JPanel {
        private final List<Integer> tallas;
        private final Map<String, List<Double>> tiempos;
        ChartPanel(List<Integer> t, Map<String, List<Double>> ti) { this.tallas = t; this.tiempos = ti; }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth(), h = getHeight(), pad = 60;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            double maxT = tiempos.values().stream().flatMap(List::stream).mapToDouble(v -> v).max().orElse(1);
            g2.setColor(Color.BLACK);
            g2.drawLine(pad, h - pad, w - pad, h - pad);
            g2.drawLine(pad, h - pad, pad, pad);
            Color[] colors = {Color.RED, Color.BLUE, new Color(39, 174, 96), new Color(243, 156, 18)};
            int ci = 0;
            for (String key : tiempos.keySet()) {
                g2.setColor(colors[ci++ % colors.length]);
                List<Double> vals = tiempos.get(key);
                int prevX = -1, prevY = -1;
                for (int i = 0; i < tallas.size(); i++) {
                    int x = pad + (int) ((tallas.get(i) - tallas.get(0)) / (double) (tallas.get(tallas.size() - 1) - tallas.get(0)) * (w - 2 * pad));
                    int y = h - pad - (int) ((vals.get(i) / maxT) * (h - 2 * pad));
                    g2.fillOval(x - 3, y - 3, 6, 6);
                    if (prevX != -1) g2.drawLine(prevX, prevY, x, y);
                    prevX = x; prevY = y;
                }
                g2.drawString(key, w - 180, pad + ci * 20);
            }
        }
    }
}
