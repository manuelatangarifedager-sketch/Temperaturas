package controladores;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import modelos.Temperatura;
import servicios.TemperaturaServicio;

public class TemperaturaControlador {

    public static void graficar(JPanel panel,
            List<Temperatura> datos,
            LocalDate desde, LocalDate hasta) {

        var filtrados = TemperaturaServicio.filtrar(datos, desde, hasta);
        var promedios = TemperaturaServicio.getPromedioPorCiudad(filtrados);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> e : promedios.entrySet()) {
            dataset.addValue(e.getValue(), "Temperatura", e.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Promedio de temperatura por ciudad",
                "Ciudad",
                "Temperatura",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));

        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.revalidate();
    }

    
    public static void mostrarResultado(JPanel panel,
            List<Temperatura> datos,
            LocalDate fecha) {

        String max = TemperaturaServicio.getCiudadMasCalurosa(datos, fecha);
        String min = TemperaturaServicio.getCiudadMenosCalurosa(datos, fecha);

        panel.removeAll();

        panel.add(new JLabel("Más calurosa: " + max));
        panel.add(new JLabel("Menos calurosa: " + min));

        panel.revalidate();
    }
}