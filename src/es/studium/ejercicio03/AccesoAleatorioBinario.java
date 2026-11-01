package es.studium.ejercicio03;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class AccesoAleatorioBinario {
    private static final String FILE_NAME = "notas.dat";
    private static final int RECORD_SIZE = 12; // 4 bytes (int) + 8 bytes (double)

    public static void main(String[] args) {
        writeRandomAccessFile();
        checkFileSize();
        promptAndSeekRecord();
        modifyAndReadSequentially();
    }

    private static void writeRandomAccessFile() {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw")) {
            raf.setLength(0);

            int[] ids = {101, 102, 103, 104, 105};
            double[] notas = {6.5, 8.0, 5.5, 9.0, 7.0};

            for (int i = 0; i < ids.length; i++) {
                raf.writeInt(ids[i]);
                raf.writeDouble(notas[i]);
            }
            System.out.println("Fichero notas.dat generado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir el fichero: " + e.getMessage());
        }
    }

    private static void checkFileSize() {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
            long fileSize = raf.length();
            System.out.println("\n====== Verificación de Tamaño =======");
            System.out.println("Tamaño total del fichero: " + fileSize + " bytes");
            System.out.println("Número de registros: " + (fileSize / RECORD_SIZE));
            System.out.println("Cada registro ocupa exactamente: " + RECORD_SIZE + " bytes (4 de ID + 8 de nota).");
        } catch (IOException e) {
            System.err.println("Error al leer el tamaño del fichero: " + e.getMessage());
        }
    }

    private static void promptAndSeekRecord() {
        try (Scanner scanner = new Scanner(System.in);
             RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {

            System.out.print("\nIntroduce un índice de registro entre 0 y 4: ");
            int index = scanner.nextInt();

            if (index < 0 || index > 4) {
                System.out.println("⚠️ Índice fuera de rango.");
                return;
            }

            long offset = (long) index * RECORD_SIZE;
            raf.seek(offset);

            int id = raf.readInt();
            double nota = raf.readDouble();

            System.out.println("--- Acceso directo (seek) al índice " + index + " ---");
            System.out.println("ID: " + id + " | Nota: " + nota);

        } catch (IOException e) {
            System.err.println("Error en el acceso aleatorio: " + e.getMessage());
        }
    }

    private static void modifyAndReadSequentially() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("\nIntroduce el índice del registro a modificar (0 a 4): ");
            int index = scanner.nextInt();

            if (index < 0 || index > 4) {
                System.out.println("⚠️ Índice fuera de rango.");
                return;
            }

            System.out.print("Introduce la nueva nota: ");
            double nuevaNota = scanner.nextDouble();

            try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw")) {
                long noteOffset = ((long) index * RECORD_SIZE) + 4;
                raf.seek(noteOffset);
                raf.writeDouble(nuevaNota);
                System.out.println("¡Nota modificada correctamente!");
            }

            System.out.println("\n====== Fichero Final =======");
            try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
                long totalRecords = raf.length() / RECORD_SIZE;
                for (int i = 0; i < totalRecords; i++) {
                    int id = raf.readInt();
                    double nota = raf.readDouble();
                    System.out.println("Registro [" + i + "] -> ID: " + id + " | Nota: " + nota);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al modificar o leer el fichero: " + e.getMessage());
        }
    }
}
