import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import controladores.TemperaturaControlador;
import datechooser.beans.DateChooserCombo;
import modelos.Temperatura;
import servicios.TemperaturaServicio;

public class FrmTemperaturas extends JFrame {

    private DateChooserCombo dccDesde;
    private DateChooserCombo dccHasta;
    private DateChooserCombo dccFecha;

    private JPanel pnlGrafica;
    private JPanel pnlResultado;

    private List<Temperatura> datos;

    public FrmTemperaturas() {

        setTitle("Temperaturas");
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // PESTAÑA 1: RANGOS

        JPanel pnlRangos = new JPanel(new BorderLayout());

        JPanel pnlDatosRango = new JPanel();
        pnlDatosRango.setPreferredSize(new Dimension(100, 50));
        pnlDatosRango.setLayout(null);

        JLabel lblDesde = new JLabel("Desde");
        lblDesde.setBounds(10, 10, 60, 25);
        pnlDatosRango.add(lblDesde);

        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(60, 10, 100, 25);
        pnlDatosRango.add(dccDesde);

        JLabel lblHasta = new JLabel("Hasta");
        lblHasta.setBounds(170, 10, 60, 25);
        pnlDatosRango.add(lblHasta);

        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(220, 10, 100, 25);
        pnlDatosRango.add(dccHasta);

        JButton btnGraficar = new JButton("Graficar");
        btnGraficar.setBounds(340, 10, 100, 25);
        pnlDatosRango.add(btnGraficar);

        pnlGrafica = new JPanel();
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);

        pnlRangos.add(pnlDatosRango, BorderLayout.NORTH);
        pnlRangos.add(spGrafica, BorderLayout.CENTER);

        btnGraficar.addActionListener(e -> graficar());


        // PESTAÑA 2: CONSULTA
        JPanel pnlConsulta = new JPanel(new BorderLayout());

        JPanel pnlDatosConsulta = new JPanel();
        pnlDatosConsulta.setPreferredSize(new Dimension(100, 50));
        pnlDatosConsulta.setLayout(null);

        JLabel lblFecha = new JLabel("Fecha");
        lblFecha.setBounds(10, 10, 60, 25);
        pnlDatosConsulta.add(lblFecha);

        dccFecha = new DateChooserCombo();
        dccFecha.setBounds(60, 10, 120, 25);
        pnlDatosConsulta.add(dccFecha);

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(200, 10, 120, 25);
        pnlDatosConsulta.add(btnConsultar);

        pnlResultado = new JPanel(new BorderLayout());

        pnlConsulta.add(pnlDatosConsulta, BorderLayout.NORTH);
        pnlConsulta.add(pnlResultado, BorderLayout.CENTER);

        btnConsultar.addActionListener(e -> consultar());


        tabs.addTab("Rangos", pnlRangos);
        tabs.addTab("Consulta", pnlConsulta);

        add(tabs);

        cargarDatos();
    }

    private void cargarDatos() {
        String archivo = System.getProperty("user.dir")
                + "/src/datos/Temperaturas.csv";

        datos = TemperaturaServicio.getDatos(archivo);
    }

    private LocalDate obtenerFecha(DateChooserCombo dcc) {
        return dcc.getSelectedDate()
                .getTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void graficar() {
        try {
            LocalDate desde = obtenerFecha(dccDesde);
            LocalDate hasta = obtenerFecha(dccHasta);

            TemperaturaControlador.graficar(
                    pnlGrafica,
                    datos,
                    desde,
                    hasta
            );

        } catch (Exception e) {
            pnlGrafica.removeAll();
            pnlGrafica.add(new JLabel("Error en fechas"));
            pnlGrafica.revalidate();
            pnlGrafica.repaint();
        }
    }

    private void consultar() {
        try {
            LocalDate fecha = obtenerFecha(dccFecha);

            TemperaturaControlador.mostrarResultado(
                    pnlResultado,
                    datos,
                    fecha
            );

        } catch (Exception e) {
            pnlResultado.removeAll();
            pnlResultado.add(new JLabel("Error en la fecha"));
            pnlResultado.revalidate();
            pnlResultado.repaint();
        }
    }
}