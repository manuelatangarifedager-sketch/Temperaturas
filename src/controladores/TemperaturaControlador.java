package controladores;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

    public static void graficar(JPanel pnlGrafica,
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
        chartPanel.setPreferredSize(new Dimension(500, 270));

        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(chartPanel, BorderLayout.CENTER);
        pnlGrafica.revalidate();
    }

    public static void mostrarResultado(JPanel pnlResultado,
            List<Temperatura> datos,
            LocalDate fecha) {

        String max = TemperaturaServicio.getCiudadMax(datos, fecha);
        String min = TemperaturaServicio.getCiudadMin(datos, fecha);

        pnlResultado.removeAll();
        pnlResultado.setLayout(new GridBagLayout());

        var gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlResultado.add(new JLabel("Ciudad más calurosa:"), gbc);

        gbc.gridx = 1;
        pnlResultado.add(new JLabel(max), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlResultado.add(new JLabel("Ciudad menos calurosa:"), gbc);

        gbc.gridx = 1;
        pnlResultado.add(new JLabel(min), gbc);

        pnlResultado.revalidate();
    }
}