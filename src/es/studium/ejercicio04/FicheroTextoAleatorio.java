package es.studium.ejercicio04;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FicheroTextoAleatorio {
    private static final String FILE_NAME = "nombres.dat";
    private static final int RECORD_SIZE = 12; // 12 caracteres = 12 bytes (1 byte por carácter)

    public static void main(String[] args) {
        // 1. Crear nombres.dat, reservar 12 caracteres por registro y rellenar con espacios
        writeNamesFile();

        // 2. Calcular tamaño del registro y leer directamente el tercer nombre (índice 2)
        readThirdRecordDirectly();

        // 3. Sustituir el segundo nombre (índice 1) por "Pablo" manteniendo la longitud fija
        replaceSecondName();

        // 4. Volver a leer y mostrar los cuatro registros eliminando los espacios de relleno
        readAllRecordsTrimmed();
    }

    private static void writeNamesFile() {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw")) {
            raf.setLength(0); // Borra contenido anterior

            String[] names = {"Ana", "Luis", "Marta", "Nora"};

            for (String name : names) {
                // String.format("%-12s", name) alinea a la izquierda (-) y rellena con espacios hasta 12
                String paddedName = String.format("%-12s", name);
                raf.writeBytes(paddedName);
            }
            System.out.println("Fichero nombres.dat creado con registros de longitud fija.");
        } catch (IOException e) {
            System.err.println("Error al escribir el fichero: " + e.getMessage());
        }
    }

    private static void readThirdRecordDirectly() {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
            System.out.println("\n====== Acceso Directo al Tercer Nombre =======");
            System.out.println("El tamaño de cada registro es exactamente: " + RECORD_SIZE + " bytes.");

            // El tercer nombre está en el índice 2 (0: Ana, 1: Luis, 2: Marta)
            int index = 2;
            long offset = (long) index * RECORD_SIZE;

            raf.seek(offset); // Salta directamente sin leer los anteriores

            byte[] buffer = new byte[RECORD_SIZE];
            raf.readFully(buffer);

            // Convertimos los bytes a String y usamos .trim() para quitar los espacios de relleno
            String name = new String(buffer).trim();
            System.out.println("Registro[" + index + "] leído directamente con seek: " + name);

        } catch (IOException e) {
            System.err.println("Error en el acceso directo: " + e.getMessage());
        }
    }

    private static void replaceSecondName() {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw")) {
            // El segundo nombre está en el índice 1 (0: Ana, 1: Luis)
            int index = 1;
            long offset = (long) index * RECORD_SIZE;

            raf.seek(offset);

            // Preparamos "Pablo" asegurando que también ocupe exactamente 12 caracteres con espacios
            String newName = String.format("%-12s", "Pablo");
            raf.writeBytes(newName);

            System.out.println("\n¡El registro del índice " + index + " ha sido sustituido por 'Pablo'!");

        } catch (IOException e) {
            System.err.println("Error al modificar el fichero: " + e.getMessage());
        }
    }

    private static void readAllRecordsTrimmed() {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
            System.out.println("\n====== Verificación Final (Todos los registros) =======");
            long totalRecords = raf.length() / RECORD_SIZE;

            for (int i = 0; i < totalRecords; i++) {
                byte[] buffer = new byte[RECORD_SIZE];
                raf.readFully(buffer);

                // .trim() elimina los espacios en blanco sobrantes del relleno fijo
                String name = new String(buffer).trim();
                System.out.println("Registro [" + i + "] -> " + name);
            }

        } catch (IOException e) {
            System.err.println("Error al leer el fichero completo: " + e.getMessage());
        }
    }
}