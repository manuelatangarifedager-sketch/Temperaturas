package servicios;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import modelos.Temperatura;

public class TemperaturaServicio {

        public static List<Temperatura> getDatos(String archivo) {

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");

        try {
            return Files.lines(Paths.get(archivo))
                    .skip(1)
                    .map(linea -> linea.split(","))
                    .map(textos -> new Temperatura(
                            textos[0],
                            LocalDate.parse(textos[1], formato),
                            Double.parseDouble(textos[2])
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<Temperatura> filtrar(List<Temperatura> datos,
            LocalDate desde,
            LocalDate hasta) {

        return datos.stream()
                .filter(dato ->
                        !dato.getFecha().isBefore(desde)
                        && !dato.getFecha().isAfter(hasta))
                .sorted(Comparator.comparing(Temperatura::getCiudad))
                .collect(Collectors.toList());
    }

    public static Map<String, Double> getPromedioPorCiudad(
            List<Temperatura> datos) {

        return datos.stream()
                .collect(Collectors.groupingBy(
                        Temperatura::getCiudad,
                        Collectors.averagingDouble(
                                Temperatura::getTemperatura)
                ));
    }

    public static String getCiudadMax(List<Temperatura> datos,
            LocalDate fecha) {

        return datos.stream()
                .filter(dato -> dato.getFecha().equals(fecha))
                .sorted(Comparator.comparingDouble(
                        Temperatura::getTemperatura).reversed())
                .map(Temperatura::getCiudad)
                .findFirst()
                .orElse("N/A");
    }

    public static double getTemperaturaMax(List<Temperatura> datos,
            LocalDate fecha) {

        return datos.stream()
                .filter(dato -> dato.getFecha().equals(fecha))
                .sorted(Comparator.comparingDouble(
                        Temperatura::getTemperatura).reversed())
                .map(Temperatura::getTemperatura)
                .findFirst()
                .orElse(0.0);
    }

    public static String getCiudadMin(List<Temperatura> datos,
            LocalDate fecha) {

        return datos.stream()
                .filter(dato -> dato.getFecha().equals(fecha))
                .sorted(Comparator.comparingDouble(
                        Temperatura::getTemperatura))
                .map(Temperatura::getCiudad)
                .findFirst()
                .orElse("N/A");
        }

        public static double getTemperaturaMin(List<Temperatura> datos,
                LocalDate fecha) {

        return datos.stream()
                .filter(dato -> dato.getFecha().equals(fecha))
                .sorted(Comparator.comparingDouble(
                        Temperatura::getTemperatura))
                .map(Temperatura::getTemperatura)
                .findFirst()
                .orElse(0.0);
        }
}