package servicios;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import modelos.Temperatura;

public class TemperaturaServicio {

    public static List<Temperatura> getDatos(String archivo) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d/M/yyyy");

        try {
            return Files.lines(Paths.get(archivo))
                    .skip(1)
                    .map(linea -> linea.split(","))
                    .map(t -> new Temperatura(
                            t[0],
                            LocalDate.parse(t[1], formato),
                            Double.parseDouble(t[2])))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<String> getCiudades(List<Temperatura> datos) {
        return datos.stream()
                .map(Temperatura::getCiudad)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<Temperatura> filtrar(List<Temperatura> datos,
            LocalDate desde, LocalDate hasta) {

        return datos.stream()
                .filter(d -> !d.getFecha().isBefore(desde)
                        && !d.getFecha().isAfter(hasta))
                .collect(Collectors.toList());
    }

    // 🔥 PROMEDIO POR CIUDAD
    public static Map<String, Double> getPromedioPorCiudad(List<Temperatura> datos) {

        return datos.stream()
                .collect(Collectors.groupingBy(
                        Temperatura::getCiudad,
                        Collectors.averagingDouble(Temperatura::getTemperatura)
                ));
    }


    public static String getCiudadMasCalurosa(List<Temperatura> datos, LocalDate fecha) {

        return datos.stream()
                .filter(d -> d.getFecha().equals(fecha))
                .max(Comparator.comparingDouble(Temperatura::getTemperatura))
                .map(Temperatura::getCiudad)
                .orElse("N/A");
    }


    public static String getCiudadMenosCalurosa(List<Temperatura> datos, LocalDate fecha) {

        return datos.stream()
                .filter(d -> d.getFecha().equals(fecha))
                .min(Comparator.comparingDouble(Temperatura::getTemperatura))
                .map(Temperatura::getCiudad)
                .orElse("N/A");
    }
}