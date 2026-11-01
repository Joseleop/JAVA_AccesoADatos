package es.studium.ejercicio02;

import java.io.*;

public class FicheroBinario {
    private static final String FILE_NAME = "alumnos.dat";

    public static void main(String[] args) {
        writeBinaryFile();

        readBinaryFile();

        calculateActiveStats();
    }

    private static void writeBinaryFile() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FILE_NAME))) {
            dos.writeInt(1);
            dos.writeUTF("Ana");
            dos.writeDouble(8.5);
            dos.writeBoolean(true);

            dos.writeInt(2);
            dos.writeUTF("Luis");
            dos.writeDouble(7.0);
            dos.writeBoolean(true);

            dos.writeInt(3);
            dos.writeUTF("Marta");
            dos.writeDouble(9.0);
            dos.writeBoolean(false);

            System.out.println("Fichero binario escrito correctamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el fichero: " + e.getMessage());
        }
    }

    private static void readBinaryFile() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("\n====== Lectura de Alumnos (Binario) =======");
            while (true) {
                int id = dis.readInt();
                String nombre = dis.readUTF();
                double nota = dis.readDouble();
                boolean activo = dis.readBoolean();

                System.out.println("ID: " + id + " | Nombre: " + nombre + " | Nota: " + nota + " | Activo: " + activo);
            }
        } catch (EOFException e) {
            System.out.println("--- Fin de lectura del fichero ---");
        } catch (IOException e) {
            System.err.println("Error al leer el fichero: " + e.getMessage());
        }
    }

    private static void calculateActiveStats() {
        int activeCount = 0;
        double sumNotes = 0;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(FILE_NAME))) {
            while (true) {
                int id = dis.readInt();
                String nombre = dis.readUTF();
                double nota = dis.readDouble();
                boolean activo = dis.readBoolean();

                if (activo) {
                    activeCount++;
                    sumNotes += nota;
                }
            }
        } catch (EOFException e) {
        } catch (IOException e) {
            System.err.println("Error al procesar las estadísticas: " + e.getMessage());
        }

        System.out.println("====== Estadísticas de Alumnos Activos =======");
        System.out.println("Número de registros activos leídos: " + activeCount);

        if (activeCount > 0) {
            double average = sumNotes / activeCount;
            System.out.println("Nota media de los alumnos activos: " + average);
        } else {
            System.out.println("No hay alumnos activos para calcular la media.");
        }
    }
}
