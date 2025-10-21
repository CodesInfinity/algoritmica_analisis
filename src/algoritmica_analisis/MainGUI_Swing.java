package algoritmica_analisis;

import Clases.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;
import java.util.List;
import java.text.DecimalFormat;

public class MainGUI_Swing {

    private JFrame frame;
    private DatasetPanel datasetPanel;
    private JTable resultTable;
    private DefaultTableModel resultModel;
    private JTextArea logArea;
    
    private List<Punto> currentDataset = new ArrayList<>();
    private String currentDatasetName = "Ninguno";
    private File datasetsDirectory;
    
    private final DecimalFormat distFmt = new DecimalFormat("#0.00000000");
    private final DecimalFormat timeFmt = new DecimalFormat("#0.0000");
    
    // Colores para una apariencia profesional
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color ACCENT_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainGUI_Swing().init();
            }
        });
    }

    private void init() {
        
        frame = new JFrame("Análisis de Algoritmos - Par Más Cercano");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setLayout(new BorderLayout(10, 10));
        
        // Configurar fondo principal
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        
        initializeDatasetsDirectory();
        setupGUI();
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void initializeDatasetsDirectory() {
        File currentDir = new File(".");
        File srcDir = new File(currentDir, "src");
        File datasetsDir = new File(srcDir, "datasets");
        
        if (datasetsDir.exists() && datasetsDir.isDirectory()) {
            datasetsDirectory = datasetsDir;
        } else {
            if (datasetsDir.mkdirs()) {
                datasetsDirectory = datasetsDir;
            } else {
                datasetsDirectory = currentDir;
            }
        }
    }
    
    private void setupGUI() {
        // Panel superior con información y controles principales
        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Panel central dividido
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(800);
        mainSplitPane.setResizeWeight(0.6);
        
        // Panel izquierdo: Visualización y controles
        JPanel leftPanel = createLeftPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        
        // Panel derecho: Resultados y logs
        JPanel rightPanel = createRightPanel();
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        
        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);
        
        frame.add(mainSplitPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Título
        JLabel title = new JLabel("Análisis de Algoritmos - Par Más Cercano");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        
        // Información del dataset actual
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
        
        // Panel de controles
        JPanel controlPanel = createControlPanel();
        
        // Panel de visualización
        datasetPanel = new DatasetPanel();
        datasetPanel.setBorder(BorderFactory.createTitledBorder("Visualización del Dataset"));
        
        leftPanel.add(controlPanel, BorderLayout.NORTH);
        leftPanel.add(datasetPanel, BorderLayout.CENTER);
        
        return leftPanel;
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Gestión de Datasets"));
        
        // Fila 1: Gestión de datasets
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
        
        // Fila 2: Ejecución de algoritmos
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
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(200, 35));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
                button.setForeground(Color.BLACK);
            }
            
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
                button.setForeground(Color.BLACK);
            }
        });
        
        return button;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        
        // Panel de resultados
        JPanel resultsPanel = createResultsPanel();
        
        // Panel de logs
        JPanel logPanel = createLogPanel();
        
        rightPanel.add(resultsPanel, BorderLayout.CENTER);
        rightPanel.add(logPanel, BorderLayout.SOUTH);
        
        return rightPanel;
    }
    
    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Resultados de los Algoritmos"));
        
        String[] columnNames = {
            "Algoritmo", "Punto 1", "Punto 2", "Distancia", "Cálculos", "Tiempo (ms)"
        };
        
        resultModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultTable = new JTable(resultModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setRowHeight(25);
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        // Mejorar la apariencia de la tabla
        resultTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        resultTable.getTableHeader().setBackground(PRIMARY_COLOR);
        resultTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        return resultsPanel;
    }
    
    private JPanel createLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Registro de Actividad"));
        logPanel.setPreferredSize(new Dimension(0, 150));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        logArea.setBackground(new Color(253, 253, 253));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        logPanel.add(scrollPane, BorderLayout.CENTER);
        
        return logPanel;
    }
    
    private void onLoadDataset() {
        JFileChooser fileChooser = new JFileChooser(datasetsDirectory);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos TSP", "tsp"));
        fileChooser.setDialogTitle("Seleccionar Dataset TSP");
        
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadDatasetFromFile(selectedFile);
        }
    }
    
    private void loadDatasetFromFile(File file) {
        try {
            List<Punto> puntos = DatasetManager.cargarTSP(file.getAbsolutePath());
            currentDataset = puntos;
            currentDatasetName = file.getName().replace(".tsp", "");
            
            updateDatasetInfo();
            datasetPanel.setPoints(currentDataset);
            datasetPanel.setSolution(null, null);
            datasetPanel.repaint();
            
            log("✓ Dataset cargado: " + file.getName() + " (" + puntos.size() + " puntos)");
            
        } catch (Exception ex) {
            showError("Error al cargar el dataset: " + ex.getMessage());
        }
    }
    
    private void onGenerateDataset() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JTextField sizeField = new JTextField("1000");
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Distribución Uniforme", "Caso Patológico"});
        JTextField nameField = new JTextField("dataset_generado");
        
        panel.add(new JLabel("Número de puntos:"));
        panel.add(sizeField);
        panel.add(new JLabel("Tipo de distribución:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        
        int result = JOptionPane.showConfirmDialog(frame, panel, "Generar Nuevo Dataset", 
                                                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int n = Integer.parseInt(sizeField.getText().trim());
                boolean worstCase = typeCombo.getSelectedIndex() == 1;
                String name = nameField.getText().trim();
                
                if (n <= 0) {
                    showError("El número de puntos debe ser mayor que 0");
                    return;
                }
                
                generateDataset(n, worstCase);
                currentDatasetName = name;
                updateDatasetInfo();
                
                log("✓ Dataset generado: " + name + " (" + n + " puntos)");
                
            } catch (NumberFormatException e) {
                showError("Por favor, introduce un número válido");
            }
        }
    }
    
    private void onRunAll() {
        if (currentDataset.isEmpty()) {
            showWarning("Primero carga o genera un dataset");
            return;
        }
        
        resultModel.setRowCount(0);
        
        try {
            // Ejecutar todos los algoritmos con los nombres correctos
            ParDePuntos exhaustive = Algoritmos.algoritmo_exhaustivo(deepCopyList(currentDataset));
            addResult("Exhaustivo", exhaustive);
            
            List<Punto> sorted1 = deepCopyList(currentDataset);
            sortByX(sorted1);
            ParDePuntos branchBound = Algoritmos.algoritmo_busqueda_poda(sorted1);
            addResult("Exhaustivo con Poda", branchBound);
            
            List<Punto> sorted2 = deepCopyList(currentDataset);
            sortByX(sorted2);
            ParDePuntos divideConquer = Algoritmos.dyvNormal(sorted2);
            addResult("Divide y Vencerás", divideConquer);
            
            List<Punto> sorted3 = deepCopyList(currentDataset);
            sortByX(sorted3);
            ParDePuntos improvedDC = Algoritmos.dyvMejorado(sorted3);
            addResult("Divide y Vencerás Mejorado", improvedDC);
            
            // Mostrar la mejor solución
            datasetPanel.setSolution(exhaustive.getP1(), exhaustive.getP2());
            datasetPanel.repaint();
            
            log("✓ Ejecución completada - 4 algoritmos procesados");
            
        } catch (Exception e) {
            showError("Error durante la ejecución: " + e.getMessage());
        }
    }
    
    private void onComparePerformance() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JTextField sizesField = new JTextField("100,500,1000,2000");
        JTextField repsField = new JTextField("3");
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Distribución Uniforme", "Caso Patológico"});
        
        panel.add(new JLabel("Tamaños (separados por comas):"));
        panel.add(sizesField);
        panel.add(new JLabel("Repeticiones por tamaño:"));
        panel.add(repsField);
        panel.add(new JLabel("Tipo de dataset:"));
        panel.add(typeCombo);
        
        int result = JOptionPane.showConfirmDialog(frame, panel, "Comparación de Rendimiento", 
                                                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String[] sizes = sizesField.getText().split(",");
                List<Integer> tallas = new ArrayList<>();
                for (String size : sizes) {
                    tallas.add(Integer.parseInt(size.trim()));
                }
                
                int reps = Integer.parseInt(repsField.getText().trim());
                boolean worstCase = typeCombo.getSelectedIndex() == 1;
                
                if (reps <= 0) {
                    showError("El número de repeticiones debe ser mayor que 0");
                    return;
                }
                
                runPerformanceComparison(tallas, reps, worstCase);
                
            } catch (NumberFormatException e) {
                showError("Por favor, introduce valores numéricos válidos");
            }
        }
    }
    
    private void runPerformanceComparison(List<Integer> tallas, int reps, boolean worstCase) {
        // Usar los nombres exactos que se muestran en la tabla
        Map<String, List<Double>> tiempos = new LinkedHashMap<>();
        String[] algoritmos = {"Exhaustivo", "Exhaustivo con Poda", "Divide y Vencerás", "Divide y Vencerás Mejorado"};
        
        for (String algo : algoritmos) {
            tiempos.put(algo, new ArrayList<>());
        }
        
        // Ejecutar comparación
        for (int n : tallas) {
            log("Procesando tamaño: " + n + " puntos...");
            
            double[] sumas = new double[4];
            
            for (int r = 0; r < reps; r++) {
                generateDataset(n, worstCase);
                List<Punto> dataset = deepCopyList(currentDataset);
                
                // Exhaustivo
                sumas[0] += Algoritmos.algoritmo_exhaustivo(deepCopyList(dataset)).getTiempo_de_ejecucion();
                
                // Exhaustivo con Poda
                List<Punto> sorted1 = deepCopyList(dataset);
                sortByX(sorted1);
                sumas[1] += Algoritmos.algoritmo_busqueda_poda(sorted1).getTiempo_de_ejecucion();
                
                // Divide y Vencerás
                List<Punto> sorted2 = deepCopyList(dataset);
                sortByX(sorted2);
                sumas[2] += Algoritmos.dyvNormal(sorted2).getTiempo_de_ejecucion();
                
                // Divide y Vencerás Mejorado
                List<Punto> sorted3 = deepCopyList(dataset);
                sortByX(sorted3);
                sumas[3] += Algoritmos.dyvMejorado(sorted3).getTiempo_de_ejecucion();
            }
            
            // Calcular promedios
            for (int i = 0; i < 4; i++) {
                tiempos.get(algoritmos[i]).add(sumas[i] / reps);
            }
        }
        
        showComparisonChart(tallas, tiempos);
        log("✓ Comparación de rendimiento completada");
    }
    
    private void onClearAll() {
        currentDataset.clear();
        currentDatasetName = "Ninguno";
        updateDatasetInfo();
        
        datasetPanel.setPoints(new ArrayList<>());
        datasetPanel.setSolution(null, null);
        resultModel.setRowCount(0);
        logArea.setText("");
        
        datasetPanel.repaint();
        log("Sistema reiniciado - listo para nuevo análisis");
    }
    
    private void generateDataset(int n, boolean worstCase) {
        List<Punto> newDataset = new ArrayList<>();
        Random rnd = new Random();
        
        for (int i = 0; i < n; i++) {
            double x, y;
            if (worstCase) {
                x = 1.0;
                y = rnd.nextDouble() * 1000;
            } else {
                x = rnd.nextDouble() * 1000;
                y = rnd.nextDouble() * 1000;
            }
            newDataset.add(new Punto(i + 1, x, y));
        }
        
        currentDataset = newDataset;
        datasetPanel.setPoints(currentDataset);
        datasetPanel.setSolution(null, null);
        datasetPanel.repaint();
    }
    
    private void addResult(String algoritmo, ParDePuntos resultado) {
        String p1 = formatPoint(resultado.getP1());
        String p2 = formatPoint(resultado.getP2());
        
        resultModel.addRow(new Object[]{
            algoritmo,
            p1,
            p2,
            distFmt.format(resultado.getDistancia()),
            resultado.getNum_distancias_calculadas(),
            timeFmt.format(resultado.getTiempo_de_ejecucion())
        });
    }
    
    private String formatPoint(Punto p) {
        if (p == null) return "N/A";
        return String.format("P%d (%.2f, %.2f)", p.getId(), p.getX(), p.getY());
    }
    
    private List<Punto> deepCopyList(List<Punto> original) {
        List<Punto> copy = new ArrayList<>();
        for (Punto p : original) {
            copy.add(new Punto(p.getId(), p.getX(), p.getY()));
        }
        return copy;
    }
    
    private void sortByX(List<Punto> puntos) {
        Collections.sort(puntos, new Comparator<Punto>() {
            public int compare(Punto p1, Punto p2) {
                int cmp = Double.compare(p1.getX(), p2.getX());
                return cmp != 0 ? cmp : Double.compare(p1.getY(), p2.getY());
            }
        });
    }
    
    private void updateDatasetInfo() {
        Component[] comps = frame.getContentPane().getComponents();
        for (Component comp : comps) {
            if (comp instanceof JPanel) {
                JLabel infoLabel = (JLabel) ((JPanel) comp).getComponent(1);
                if (infoLabel.getName() != null && infoLabel.getName().equals("datasetInfo")) {
                    String info = String.format("Dataset actual: %s (%d puntos)", 
                                              currentDatasetName, currentDataset.size());
                    infoLabel.setText(info);
                    break;
                }
            }
        }
    }
    
    private void log(String message) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new Date());
        logArea.append("[" + timestamp + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        log("✗ " + message);
    }
    
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(frame, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
        log("⚠ " + message);
    }
    
    private void showComparisonChart(List<Integer> tallas, Map<String, List<Double>> tiempos) {
        JFrame chartFrame = new JFrame("Comparación de Rendimiento");
        chartFrame.setSize(1000, 600);
        chartFrame.setLocationRelativeTo(frame);
        chartFrame.add(new ComparisonPanel(tallas, tiempos));
        chartFrame.setVisible(true);
    }
    
    // Clase para el panel de visualización del dataset
    class DatasetPanel extends JPanel {
        private List<Punto> puntos = new ArrayList<>();
        private Punto puntoA, puntoB;
        
        public DatasetPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(600, 400));
        }
        
        public void setPoints(List<Punto> pts) {
            this.puntos = pts;
        }
        
        public void setSolution(Punto a, Punto b) {
            this.puntoA = a;
            this.puntoB = b;
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (puntos.isEmpty()) {
                drawEmptyState(g);
                return;
            }
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Calcular bounds
            double[] bounds = calculateBounds();
            double minX = bounds[0], maxX = bounds[1], minY = bounds[2], maxY = bounds[3];
            
            int pad = 40;
            int w = getWidth() - 2 * pad;
            int h = getHeight() - 2 * pad;
            
            // Dibujar puntos
            g2.setColor(SECONDARY_COLOR);
            for (Punto p : puntos) {
                int x = pad + (int) (((p.getX() - minX) / (maxX - minX)) * w);
                int y = pad + (int) (((maxY - p.getY()) / (maxY - minY)) * h);
                g2.fillOval(x - 2, y - 2, 4, 4);
            }
            
            // Dibujar línea de solución si existe
            if (puntoA != null && puntoB != null) {
                int x1 = pad + (int) (((puntoA.getX() - minX) / (maxX - minX)) * w);
                int y1 = pad + (int) (((maxY - puntoA.getY()) / (maxY - minY)) * h);
                int x2 = pad + (int) (((puntoB.getX() - minX) / (maxX - minX)) * w);
                int y2 = pad + (int) (((maxY - puntoB.getY()) / (maxY - minY)) * h);
                
                g2.setColor(ACCENT_COLOR);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(x1, y1, x2, y2);
                
                // Resaltar puntos de la solución
                g2.setColor(ACCENT_COLOR);
                g2.fillOval(x1 - 4, y1 - 4, 8, 8);
                g2.fillOval(x2 - 4, y2 - 4, 8, 8);
            }
            
            // Dibujar información
            drawInfo(g2, bounds);
        }
        
        private double[] calculateBounds() {
            double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
            double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
            
            for (Punto p : puntos) {
                minX = Math.min(minX, p.getX());
                maxX = Math.max(maxX, p.getX());
                minY = Math.min(minY, p.getY());
                maxY = Math.max(maxY, p.getY());
            }
            
            // Asegurar un mínimo de rango
            if (Math.abs(maxX - minX) < 1e-9) {
                maxX = minX + 1.0;
            }
            if (Math.abs(maxY - minY) < 1e-9) {
                maxY = minY + 1.0;
            }
            
            return new double[]{minX, maxX, minY, maxY};
        }
        
        private void drawEmptyState(Graphics g) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            String message = "No hay dataset cargado. Use 'Cargar Dataset' o 'Generar Dataset'";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g.drawString(message, x, y);
        }
        
        private void drawInfo(Graphics2D g2, double[] bounds) {
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            
            String info = String.format("Puntos: %d | X: [%.2f, %.2f] | Y: [%.2f, %.2f]", 
                                      puntos.size(), bounds[0], bounds[1], bounds[2], bounds[3]);
            
            if (puntoA != null && puntoB != null) {
                double distancia = Math.sqrt(Math.pow(puntoA.getX() - puntoB.getX(), 2) + 
                                           Math.pow(puntoA.getY() - puntoB.getY(), 2));
                info += String.format(" | Distancia mínima: %.6f", distancia);
            }
            
            g2.drawString(info, 10, getHeight() - 10);
        }
    }
    
    // Clase para el panel de comparación - VERSIÓN FINAL CORREGIDA
    static class ComparisonPanel extends JPanel {
        private List<Integer> tallas;
        private Map<String, List<Double>> tiempos;
        // Colores específicos para cada algoritmo
        private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN.darker(), Color.MAGENTA};
        
        ComparisonPanel(List<Integer> tallas, Map<String, List<Double>> tiempos) {
            this.tallas = tallas;
            this.tiempos = tiempos;
            setBackground(Color.WHITE);
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (tallas == null || tallas.isEmpty() || tiempos == null || tiempos.isEmpty()) {
                drawEmptyGraph(g);
                return;
            }
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth(), h = getHeight();
            int pad = 80;
            
            // Calcular máximo tiempo - CON ESCALA INTELIGENTE
            double maxT = 0.0;
            double minT = Double.MAX_VALUE;
            
            for (List<Double> valores : tiempos.values()) {
                for (Double val : valores) {
                    if (val > maxT) maxT = val;
                    if (val > 0 && val < minT) minT = val;
                }
            }
            
            // ESCALA INTELIGENTE: Si hay mucha diferencia, ajustar para ver algoritmos rápidos
            if (maxT > 0 && minT > 0 && (maxT / minT) > 1000) {
                // Buscar el segundo valor más grande (excluyendo outliers)
                double secondMax = 0.0;
                for (List<Double> valores : tiempos.values()) {
                    for (Double val : valores) {
                        if (val > secondMax && val < maxT * 0.9) {
                            secondMax = val;
                        }
                    }
                }
                if (secondMax > 0) {
                    maxT = secondMax * 2; // Usar el segundo máximo como referencia
                }
            }
            
            if (maxT <= 0) maxT = 1.0;
            
            int maxX = tallas.get(tallas.size() - 1);
            
            // Dibujar ejes
            g2.setColor(Color.BLACK);
            g2.drawLine(pad, h - pad, pad, pad);
            g2.drawLine(pad, h - pad, w - pad, h - pad);
            
            // Etiquetas de ejes
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString("Tiempo (ms)", 10, 20);
            g2.drawString("Tamaño del Dataset", w / 2 - 50, h - 20);
            
            // Dibujar series - ITERAR SOBRE TODOS LOS ALGORITMOS EN ORDEN
            int colorIndex = 0;
            String[] algoritmosOrdenados = {"Exhaustivo", "Exhaustivo con Poda", "Divide y Vencerás", "Divide y Vencerás Mejorado"};
            
            for (String algoritmo : algoritmosOrdenados) {
                if (!tiempos.containsKey(algoritmo)) continue;
                
                List<Double> valores = tiempos.get(algoritmo);
                g2.setColor(colors[colorIndex % colors.length]);
                
                // Dibujar línea
                g2.setStroke(new BasicStroke(2.5f));
                for (int i = 0; i < tallas.size() - 1; i++) {
                    int x1 = pad + (int) ((tallas.get(i) / (double) maxX) * (w - 2 * pad));
                    double scaledY1 = Math.max(valores.get(i), 0.001);
                    int y1 = h - pad - (int) ((scaledY1 / maxT) * (h - 2 * pad));
                    
                    int x2 = pad + (int) ((tallas.get(i + 1) / (double) maxX) * (w - 2 * pad));
                    double scaledY2 = Math.max(valores.get(i + 1), 0.001);
                    int y2 = h - pad - (int) ((scaledY2 / maxT) * (h - 2 * pad));
                    
                    // Asegurar que las líneas sean visibles
                    y1 = Math.max(pad, Math.min(h - pad, y1));
                    y2 = Math.max(pad, Math.min(h - pad, y2));
                    
                    g2.drawLine(x1, y1, x2, y2);
                }
                
                // Dibujar puntos
                for (int i = 0; i < tallas.size(); i++) {
                    int x = pad + (int) ((tallas.get(i) / (double) maxX) * (w - 2 * pad));
                    double scaledY = Math.max(valores.get(i), 0.001);
                    int y = h - pad - (int) ((scaledY / maxT) * (h - 2 * pad));
                    
                    y = Math.max(pad, Math.min(h - pad, y));
                    
                    g2.fillOval(x - 4, y - 4, 8, 8);
                    
                    // Mostrar valores numéricos para algoritmos muy rápidos
                    if (valores.get(i) < maxT * 0.05) {
                        g2.setColor(Color.BLACK);
                        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                        String valorStr = String.format("%.3f", valores.get(i));
                        g2.drawString(valorStr, x + 5, y - 5);
                        g2.setColor(colors[colorIndex % colors.length]);
                    }
                }
                
                // Leyenda
                g2.drawString(algoritmo, w - 200, 40 + colorIndex * 25);
                colorIndex++;
            }
            
            // Título del gráfico
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            g2.drawString("Comparación de Tiempos de Ejecución", w / 2 - 140, 30);
            
            // Información sobre la escala si hay mucha variación
            if (maxT > 0 && minT > 0 && (maxT / minT) > 100) {
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                g2.setColor(Color.RED);
                g2.drawString("Nota: Escala ajustada para visualizar algoritmos rápidos", w / 2 - 150, h - 40);
            }
            
            // Marcas en los ejes
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.setColor(Color.DARK_GRAY);
            
            // Marcas en eje Y (tiempo)
            for (int i = 0; i <= 5; i++) {
                double tiempo = maxT * i / 5;
                int y = h - pad - (int) ((i / 5.0) * (h - 2 * pad));
                g2.drawString(String.format("%.1f", tiempo), pad - 30, y + 5);
                g2.drawLine(pad - 5, y, pad, y);
            }
            
            // Marcas en eje X (tamaño)
            for (int i = 0; i < tallas.size(); i++) {
                int x = pad + (int) ((tallas.get(i) / (double) maxX) * (w - 2 * pad));
                g2.drawString(tallas.get(i).toString(), x - 10, h - pad + 20);
                g2.drawLine(x, h - pad, x, h - pad + 5);
            }
        }
        
        private void drawEmptyGraph(Graphics g) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            String message = "No hay datos para mostrar";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g.drawString(message, x, y);
        }
    }
}