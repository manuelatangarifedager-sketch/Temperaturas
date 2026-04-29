import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.swing.*;

import controladores.TemperaturaControlador;
import datechooser.beans.DateChooserCombo;
import modelos.Temperatura;
import servicios.TemperaturaServicio;

public class FrmTemperaturas extends JFrame {

    private DateChooserCombo dccDesde, dccHasta, dccFecha;
    private JPanel pnlGrafica;
    private JPanel pnlResultado;

    private List<Temperatura> datos;

    public FrmTemperaturas() {

        setTitle("Temperaturas");
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tb = new JToolBar();

        JButton btnGraficar = new JButton("Graficar");
        btnGraficar.addActionListener(e -> graficar());
        tb.add(btnGraficar);

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(e -> consultar());
        tb.add(btnConsultar);

        JPanel pnlDatos = new JPanel();
        pnlDatos.setPreferredSize(new Dimension(700, 50));

        dccDesde = new DateChooserCombo();
        dccHasta = new DateChooserCombo();
        dccFecha = new DateChooserCombo();

        pnlDatos.add(new JLabel("Desde"));
        pnlDatos.add(dccDesde);
        pnlDatos.add(new JLabel("Hasta"));
        pnlDatos.add(dccHasta);
        pnlDatos.add(new JLabel("Fecha"));
        pnlDatos.add(dccFecha);

        pnlGrafica = new JPanel();
        pnlResultado = new JPanel();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Gráfica", pnlGrafica);
        tabs.addTab("Resultado", pnlResultado);

        add(tb, BorderLayout.NORTH);
        add(pnlDatos, BorderLayout.CENTER);
        add(tabs, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        String archivo = System.getProperty("user.dir") + "/src/datos/Temperaturas.csv";
        datos = TemperaturaServicio.getDatos(archivo);
    }

    private void graficar() {
        LocalDate desde = dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hasta = dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        TemperaturaControlador.graficar(pnlGrafica, datos, desde, hasta);
    }

    private void consultar() {
        LocalDate fecha = dccFecha.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        TemperaturaControlador.mostrarResultado(pnlResultado, datos, fecha);
    }
}