import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import datechooser.beans.DateChooserCombo;
import modelos.Temperatura;
import servicios.TemperaturaServicio;

public class FrmTemperaturas extends JFrame {

    private DateChooserCombo dccFecha, dccDesde, dccHasta;
    private JTabbedPane tpTemperaturas;
    private JPanel pnlResultado, pnlGrafica;

    private JTextArea txtResultado;

    private List<Temperatura> datos;

    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public FrmTemperaturas() {

        setTitle("Temperaturas");
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 🔹 TOOLBAR
        JToolBar tb = new JToolBar();

        JButton btnGraficar = new JButton("Graficar");
        btnGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnGraficarClick();
            }
        });
        tb.add(btnGraficar);

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnConsultarClick();
            }
        });
        tb.add(btnConsultar);

        // 🔹 CONTENEDOR
        JPanel pnlTemperaturas = new JPanel();
        pnlTemperaturas.setLayout(new BoxLayout(pnlTemperaturas, BoxLayout.Y_AXIS));

        // 🔹 PANEL SUPERIOR
        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setPreferredSize(new Dimension(pnlDatosProceso.getWidth(), 50));
        pnlDatosProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlDatosProceso.setLayout(null);

        // 🔹 DESDE
        JLabel lblDesde = new JLabel("Desde");
        lblDesde.setBounds(10, 10, 60, 25);
        pnlDatosProceso.add(lblDesde);

        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(60, 10, 100, 25);
        dccDesde.setFormat(2);
        pnlDatosProceso.add(dccDesde);

        // 🔹 HASTA
        JLabel lblHasta = new JLabel("Hasta");
        lblHasta.setBounds(170, 10, 60, 25);
        pnlDatosProceso.add(lblHasta);

        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(220, 10, 100, 25);
        dccHasta.setFormat(2);
        pnlDatosProceso.add(dccHasta);

        // 🔹 FECHA
        JLabel lblFecha = new JLabel("Fecha");
        lblFecha.setBounds(330, 10, 60, 25);
        pnlDatosProceso.add(lblFecha);

        dccFecha = new DateChooserCombo();
        dccFecha.setBounds(380, 10, 100, 25);
        dccFecha.setFormat(2);
        pnlDatosProceso.add(dccFecha);

        // 🔹 PANEL RESULTADO
        pnlResultado = new JPanel(new BorderLayout());
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        pnlResultado.add(new JScrollPane(txtResultado), BorderLayout.CENTER);

        // 🔹 PANEL GRAFICA
        pnlGrafica = new JPanel();
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);

        // 🔹 TABS
        tpTemperaturas = new JTabbedPane();
        tpTemperaturas.addTab("Gráfica", spGrafica);
        tpTemperaturas.addTab("Resultado", pnlResultado);

        // 🔹 AGREGAR
        pnlTemperaturas.add(pnlDatosProceso);
        pnlTemperaturas.add(tpTemperaturas);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlTemperaturas, BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        String archivo = System.getProperty("user.dir") + "/src/datos/Temperaturas.csv";
        datos = TemperaturaServicio.getDatos(archivo);
    }

    // 🔥 CONSULTAR (YA FUNCIONA)
    private void btnConsultarClick() {

        try {
            LocalDate fecha = LocalDate.parse(dccFecha.getText(), formato);

            Temperatura mayor = null;

            for (Temperatura t : datos) {
                if (t.getFecha().equals(fecha)) {

                    if (mayor == null || t.getTemperatura() > mayor.getTemperatura()) {
                        mayor = t;
                    }
                }
            }

            tpTemperaturas.setSelectedIndex(1);

            if (mayor != null) {
                txtResultado.setText(
                        "Fecha: " + fecha + "\n\n" +
                        "Ciudad más calurosa: " + mayor.getCiudad() + "\n" +
                        "Temperatura: " + mayor.getTemperatura() + " °C"
                );
            } else {
                txtResultado.setText("No hay datos para esa fecha");
            }

        } catch (Exception e) {
            txtResultado.setText("Error al leer la fecha");
        }
    }

    // 🔥 GRAFICAR (POR AHORA SOLO PRUEBA)
    private void btnGraficarClick() {

        try {
            LocalDate desde = LocalDate.parse(dccDesde.getText(), formato);
            LocalDate hasta = LocalDate.parse(dccHasta.getText(), formato);

            tpTemperaturas.setSelectedIndex(0);

            pnlGrafica.removeAll();
            pnlGrafica.add(new JLabel("Aquí irá la gráfica entre " + desde + " y " + hasta));
            pnlGrafica.revalidate();
            pnlGrafica.repaint();

        } catch (Exception e) {
            pnlGrafica.removeAll();
            pnlGrafica.add(new JLabel("Error en fechas"));
            pnlGrafica.revalidate();
            pnlGrafica.repaint();
        }
    }
}