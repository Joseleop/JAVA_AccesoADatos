package es.studium.ejercicio01;

import java.io.*;
import java.util.Scanner;

public class FicheroTexto {
    private static final String FILE_NAME = "alumnos.txt";
    static String nameStudent;

    public static void main(String[] args) {
        if (writeFile()) {
            newStudent();
        }
        readFile(FILE_NAME);

        countStudents(FILE_NAME);

        AskAndSearch();
    }

    private static void AskAndSearch() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre de un alumno para buscar: ");
        nameStudent = scanner.nextLine();
        searchStudent(nameStudent, FILE_NAME);
    }

    private static void searchStudent(String nameStudent, String fileName) {
        boolean is_find = false;
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = input.readLine()) != null) {
                try {
                    String[] data = line.split(":");
                    if (data.length < 4) {
                        continue;
                    }

                    if (data[1].equalsIgnoreCase(nameStudent)) {
                        System.out.println("=====Alumno encontrado=====");
                        System.out.println("ID: " + data[0]);
                        System.out.println("Nombre: " + data[1]);
                        System.out.println("Módulo: " + data[2]);
                        System.out.println("Nota: " + data[3]);
                        is_find = true;
                    }
                } catch (Exception e) {
                }
            }
            if (!is_find) {
                System.out.println("No se encuentra al alumno");
            }
        } catch (IOException e) {
            System.err.println("Error al buscar alumnos: " + e.getMessage());
        }
    }

    private static void countStudents(String fileName) {
        int totalStudents = 0;
        double notasGlobales = 0;
        double notaDataAccess = 0;
        int numDataAccess = 0;

        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = input.readLine()) != null) {
                try {
                    String[] data = line.split(":");

                    if (data.length < 4) {
                        System.err.println("⚠️ Aviso: Línea mal formada ignorada: " + line);
                        continue;
                    }

                    totalStudents++;
                    notasGlobales += Double.parseDouble(data[3]);

                    if (data[2].equals("Acceso a datos")) {
                        notaDataAccess += Double.parseDouble(data[3]);
                        numDataAccess++;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("⚠️ Aviso: Error numérico en línea, omitida: " + line);
                }
            }

            if (totalStudents > 0) {
                double mediaGlobal = notasGlobales / totalStudents;
                double mediaDataAccess = (numDataAccess > 0) ? (notaDataAccess / numDataAccess) : 0;

                System.out.println("====== Estadísticas =======");
                System.out.println("Número total de alumnos: " + totalStudents);
                System.out.println("Nota media global: " + Math.round(mediaGlobal));
                System.out.println("La nota media del módulo Acceso a datos es: " + Math.round(mediaDataAccess));
            } else {
                System.out.println("No hay datos válidos para procesar estadísticas.");
            }

        } catch (IOException e) {
            System.err.println("Error al procesar la información del archivo: " + e.getMessage());
        }
    }

    private static void readFile(String fileName) {
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            String line;
            System.out.println("======Datos de Alumnos:=======");
            while ((line = input.readLine()) != null) {
                try {
                    String[] data = line.split(":");
                    if (data.length >= 4) {
                        System.out.println("ID del alumno: " + data[0]);
                        System.out.println("Nombre de alumno: " + data[1]);
                        System.out.println("Asignatura: " + data[2]);
                        System.out.println("Nota obtenida: " + data[3]);
                    }
                } catch (Exception e) {
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }

    private static void newStudent() {
        try (BufferedWriter newData = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            newData.write("5:Nora:Programación:8.0");
            newData.newLine();
        } catch (IOException e) {
            System.err.println("Error al añadir un nuevo alumno: " + e.getMessage());
        }
    }

    private static boolean writeFile() {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(FILE_NAME))) {
            output.write("1:Ana:Programación:8.5");
            output.newLine();
            output.write("2:Luis:Bases de datos:7.0");
            output.newLine();
            output.write("3:Marta:Acceso a datos:9.0");
            output.newLine();
            output.write("4:Hugo:Acceso a datos:6.5");
            output.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
            return false;
        }
    }
}