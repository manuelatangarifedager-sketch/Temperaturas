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
            LocalDate desde,
            LocalDate hasta) {

        if (desde == null || hasta == null) {
            mostrarMensaje(pnlGrafica, "Fechas inválidas");
            return;
        }

        if (desde.isAfter(hasta)) {
            LocalDate temp = desde;
            desde = hasta;
            hasta = temp;
        }

        var filtrados = TemperaturaServicio.filtrar(datos, desde, hasta);

        if (filtrados.isEmpty()) {
            mostrarMensaje(pnlGrafica, "No hay datos en ese rango");
            return;
        }

        var promedios = TemperaturaServicio.getPromedioPorCiudad(filtrados);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> dato : promedios.entrySet()) {
            dataset.addValue(
                    dato.getValue(),
                    "Temperatura Promedio",
                    dato.getKey()
            );
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Promedio de temperatura por ciudad",
                "Ciudad",
                "Temperatura (°C)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));

        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(chartPanel, BorderLayout.CENTER);

        pnlGrafica.revalidate();
        pnlGrafica.repaint();
    }

    public static void mostrarResultado(JPanel pnlResultado,
            List<Temperatura> datos,
            LocalDate fecha) {

        var datosFecha = datos.stream()
                .filter(d -> d.getFecha().equals(fecha))
                .toList();

        pnlResultado.removeAll();
        pnlResultado.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        if (datosFecha.isEmpty()) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            pnlResultado.add(new JLabel("No hay datos para esa fecha"), gbc);

            pnlResultado.revalidate();
            pnlResultado.repaint();
            return;
        }

        Temperatura max = datosFecha.stream()
                .max((a, b) -> Double.compare(
                        a.getTemperatura(),
                        b.getTemperatura()))
                .orElse(null);

        Temperatura min = datosFecha.stream()
                .min((a, b) -> Double.compare(
                        a.getTemperatura(),
                        b.getTemperatura()))
                .orElse(null);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        pnlResultado.add(
                new JLabel("Resultados de la consulta"),
                gbc
        );

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlResultado.add(
                new JLabel("Ciudad más calurosa:"),
                gbc
        );

        gbc.gridx = 1;
        pnlResultado.add(
                new JLabel(
                        max.getCiudad() + " (" +
                        max.getTemperatura() + " °C)"
                ),
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlResultado.add(
                new JLabel("Ciudad menos calurosa:"),
                gbc
        );

        gbc.gridx = 1;
        pnlResultado.add(
                new JLabel(
                        min.getCiudad() + " (" +
                        min.getTemperatura() + " °C)"
                ),
                gbc
        );

        pnlResultado.revalidate();
        pnlResultado.repaint();
    }

    private static void mostrarMensaje(JPanel panel, String mensaje) {
        panel.removeAll();
        panel.add(new JLabel(mensaje));
        panel.revalidate();
        panel.repaint();
    }
}